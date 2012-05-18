/*
 * Proiect PDSD - EasyFilter
 *
 * @author Gherghescu Teo, 343 C1
 * @author Stoean Bogdan, 343 C1
 * @author Marin Alexandru, 343 C1
 *
 */
package EasyFilterClient.Communication;

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
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the blocking TCP connection module
 */
public class BlockingUdpConnection implements ClientCommunicationInterface {

    /**
     * The server socket
     */
    private DatagramSocket udpSocket;
    /**
     * The server address
     */
    private String serverIP;
    private int serverPort;

    /**
     * Constructor
     * Remembers the server address
     */
    public BlockingUdpConnection() {
        EasyPropertiesReader epr = new EasyPropertiesReader("config/config.ini");
        this.setServerIP(epr.readProperty("Address", "IP"));
        this.setServerPort(Integer.parseInt(epr.readProperty("Address", "port")));
    }

    /**
     * Opens a connection to a server
     */
    @Override
    public void openConnection() {
        try {
            // Open server socket
            this.setUdpSocket(new DatagramSocket());
        } catch (IOException ex) {
            Logger.getLogger(BlockingUdpConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Sends a request to a server
     *
     * @param Package instance to be sent
     */
    @Override
    public void sendRequest(common.Package pkg) {
        ObjectOutputStream oos = null;
        int i, j, numberOfSentPackets, chunkSize, tmp;
        boolean notFinishedSendingImage = false;
        byte[] packageBytes = null;
        ByteBuffer buf;
        byte b;
        String port;

        // split the image in packages
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(pkg);
            oos.flush();
            oos.close();
            baos.close();

            packageBytes = baos.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(BlockingUdpConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                oos.close();
            } catch (IOException ex) {
                Logger.getLogger(BlockingUdpConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // the buffer for the received packet
        // bytes 0-14  contain the client IP
        // bytes 15-19          - the client port
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
                // add server IP
                for (j = 0; j < this.getServerIP().length(); j++) {
                    buf.put((byte) this.getServerIP().charAt(j));
                }
                for (j = 0; j < 15 - this.getServerIP().length(); j++) {
                    buf.put((byte) 0);
                }
                // add server port
                port = this.getServerPort() + "";
                for (j = 0; j < port.length(); j++) {
                    buf.put((byte) port.charAt(j));
                }
                for (j = 0; j < 5 - port.length(); j++) {
                    buf.put((byte) 0);
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
                    packet.setAddress(InetAddress.getByName(this.getServerIP()));
                    packet.setPort(this.getServerPort());
                    // finally send the packet
                    this.getUdpSocket().send(packet);
                } catch (Exception ex) {
                    Logger.getLogger(BlockingUdpConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Receive an image from the server
     *
     * @return Package instance sent by the server
     */
    @Override
    public common.Package receiveRequest() {
        common.Package pkg = null;

        int chunkPosition, numberOfChunks, i, j, imageSize;
        Vector<byte[]> chunk = null;
        byte[] image;
        ByteArrayInputStream baos = null;
        ObjectInputStream ois = null;
        boolean isFirstPackage = true;

        // the buffer for the packet to be sent
        // bytes 0-19        - not used
        // bytes 20-21       - the chunk position
        // byte  22-23       - number of chunks for current client
        byte[] buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        while (true) {
            try {
                this.getUdpSocket().receive(packet);

                // get the client address
                chunkPosition = buf[20];
                chunkPosition = chunkPosition << 8;
                chunkPosition |= buf[21];
                
                numberOfChunks = buf[22];
                numberOfChunks = chunkPosition << 8;
                numberOfChunks |= buf[23];
                
                
                if (isFirstPackage) {
                    chunk = new Vector<byte[]>(numberOfChunks);
                    isFirstPackage = false;
                }

                // add image chunks
                chunk.setElementAt(buf, chunkPosition);
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
                        Logger.getLogger(BlockingUdpConnection.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    return pkg;
                }
            } catch (IOException ex) {
                Logger.getLogger(BlockingUdpConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // ~~~~~~~~ Getters and Setters ~~~~~~~~~~
    public DatagramSocket getUdpSocket() {
        return udpSocket;
    }

    private void setUdpSocket(DatagramSocket udpSocket) {
        this.udpSocket = udpSocket;
    }

    public String getServerIP() {
        return serverIP;
    }

    private void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public int getServerPort() {
        return serverPort;
    }

    private void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }
}