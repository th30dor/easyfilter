/*
 * Proiect PDSD - EasyFilter
 *
 * @author Gherghescu Teo, 343 C1
 * @author Stoean Bogdan, 343 C1
 * @author Marin Alexandru, 343 C1
 *
 */
package EasyFilterServer.Communication;

import common.EasyPropertiesReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the blocking TCP connection module for server-client
 */
public class ClientBlockingUdpConnection implements CommunicationInterface {

    /**
     * The server socket
     */
    private static DatagramSocket udpSocket;
    /**
     * Hash table for mapping images to clients
     */
    private static Hashtable<String, Vector<byte[]>> images;
    private String currentClientIP;
    private int currentClientPort;

    /**
     * Opens a socket with the data read from the config file;
     * Used to communicate with the client/other server instances
     */
    @Override
    public void openConnection() {
        try {
            EasyPropertiesReader epr = new EasyPropertiesReader("config/config.ini");
            ClientBlockingUdpConnection.setImages(new Hashtable<String, Vector<byte[]>>());

            // Open server socket
            ClientBlockingUdpConnection.setUdpSocket(
                    new DatagramSocket(Integer.parseInt(epr.readProperty("Address", "port"))));
        } catch (IOException ex) {
            Logger.getLogger(ClientBlockingUdpConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Whether the connection is successfully created or not
     *
     * @return bool
     */
    @Override
    public boolean connectionAccepted() {
        return true;
    }

    /**
     * Sends a file to either a client or other servers
     *
     * @param obj the object to be sent
     *
     * @return void
     */
    @Override
    public void sendFile(Object obj) {
        ObjectOutputStream oos = null;
        int i, j, numberOfSentPackets, chunkSize, tmp;
        boolean notFinishedSendingImage = false;
        byte[] packageBytes = null;
        ByteBuffer buf;
        byte b;

        // split the image in packages
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            baos.close();

            packageBytes = baos.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(ClientBlockingUdpConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                oos.close();
            } catch (IOException ex) {
                Logger.getLogger(ClientBlockingUdpConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // the buffer for the packet to be sent
        // bytes 0-19           - not used
        // bytes 20-21          - the chunk position
        // byte  22-23          - number of chunks for current client
        numberOfSentPackets = (int) Math.ceil(packageBytes.length / 1000);
        while (notFinishedSendingImage) {
            for (i = 0; i < numberOfSentPackets; i++) {
                // compute the chunk size
                if (1000 * numberOfSentPackets == packageBytes.length) {
                    chunkSize = 1000;
                } else {
                    if (i == numberOfSentPackets - 1) {
                        chunkSize = packageBytes.length - (numberOfSentPackets - 1) * 1000;
                    } else {
                        chunkSize = 1000;
                    }
                }

                buf = ByteBuffer.allocate(chunkSize + 21);
                b = 0;
                // add unused bytes
                for (j = 0; j < 20; j++) {
                    buf.put(b);
                }
                // add chunk position
                tmp = i >> 8;
                b = (byte)(tmp);
                buf.put(b);
                tmp = i & 255;
                b = (byte)(tmp);
                buf.put(b);

                // add number of chunks
                tmp = numberOfSentPackets >> 8;
                b = (byte)(tmp);
                buf.put(b);
                tmp = numberOfSentPackets & 255;
                b = (byte)(tmp);
                buf.put(b);

                // add image content to packet
                buf.put(ByteBuffer.wrap(packageBytes, i * 1000, chunkSize));

                // create the actual packet
                DatagramPacket packet = new DatagramPacket(buf.array(), buf.array().length);
                try {
                    // set the address for the packet
                    packet.setAddress(InetAddress.getByName(this.getCurrentClientIP()));
                    packet.setPort(this.getCurrentClientPort());
                    // finally send the packet
                    ClientBlockingUdpConnection.getUdpSocket().send(packet);
                } catch (Exception ex) {
                    Logger.getLogger(ClientBlockingUdpConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Receives an Object from either a client or other servers
     *
     * @return the object received
     */
    @Override
    public Object receiveFile() {
        common.Package pkg = null;
        String clientIP, clientPort, key;
        int chunkPosition, numberOfChunks, i, j, imageSize;
        Vector<byte[]> chunk;
        byte[] image;
        ByteArrayInputStream baos = null;
        ObjectInputStream ois = null;

        // the buffer for the received packet
        // bytes 0-14  contain the client IP
        // bytes 15-19       - the client port
        // bytes 20-21       - the chunk position
        // byte  22-23       - number of chunks for current client
        byte[] buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        while (true) {
            try {
                ClientBlockingUdpConnection.getUdpSocket().receive(packet);

                // get the client address
                clientIP = new String(buf, 0, 15);
                clientPort = new String(buf, 15, 5);
                key = clientIP + ":" + clientPort;
                chunkPosition = buf[20];
                chunkPosition = chunkPosition << 8;
                chunkPosition |= buf[21];

                numberOfChunks = buf[22];
                numberOfChunks = chunkPosition << 8;
                numberOfChunks |= buf[23];

                // add image chunks to hash table
                if (images.containsKey(key)) {
                    chunk = images.get(key);
                    chunk.setElementAt(buf, chunkPosition);
                    // replace the old vector
                    images.put(key, chunk);

                    // check for last chunk
                    if (numberOfChunks == chunk.size()) {
                        // the image is complete
                        // get the image size
                        imageSize = 1000 * (numberOfChunks - 1) + chunk.elementAt(numberOfChunks - 1).length;
                        image = new byte[imageSize];

                        // create the package and return it to the client
                        int imagePosition = 0;
                        for (i = 0; i < chunk.size(); i++) {
                            for (j = 22; j < chunk.elementAt(i).length; j++) {
                                image[imagePosition] = chunk.elementAt(i)[j];
                                imagePosition++;
                            }
                        }
                        baos = new ByteArrayInputStream(image);
                        ois = new ObjectInputStream(baos);
                        try {
                            pkg = (common.Package) ois.readObject();
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(ClientBlockingUdpConnection.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        // remove the image from the hashmap
                        images.remove(key);
                        // set the current client address
                        this.setCurrentClientIP(clientIP);
                        this.setCurrentClientPort(Integer.parseInt(clientPort));

                        return pkg;
                    }
                } else {
                    chunk = new Vector<byte[]>(numberOfChunks);
                    chunk.setElementAt(buf, chunkPosition);
                    images.put(key, chunk);
                }
            } catch (IOException ex) {
                Logger.getLogger(ClientBlockingUdpConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Closes the connection to a client / server
     */
    @Override
    public void closeConnection() {
        try {
            this.getUdpSocket().close();
        } catch (Exception ex) {
            Logger.getLogger(ClientBlockingUdpConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ~~~~~~~~ Getters and Setters ~~~~~~~~~~
    public static Hashtable<String, Vector<byte[]>> getImages() {
        return images;
    }

    private static void setImages(Hashtable<String, Vector<byte[]>> images) {
        ClientBlockingUdpConnection.images = images;
    }

    public static DatagramSocket getUdpSocket() {
        return udpSocket;
    }

    private static void setUdpSocket(DatagramSocket udpSocket) {
        ClientBlockingUdpConnection.udpSocket = udpSocket;
    }

    public String getCurrentClientIP() {
        return currentClientIP;
    }

    public void setCurrentClientIP(String currentClientIP) {
        this.currentClientIP = currentClientIP;
    }

    public int getCurrentClientPort() {
        return currentClientPort;
    }

    public void setCurrentClientPort(int currentClientPort) {
        this.currentClientPort = currentClientPort;
    }
}