/*
 * Proiect PDSD - EasyFilter
 *
 * @author Gherghescu Teo, 343 C1
 * @author Stoean Bogdan, 343 C1
 * @author Marin Alexandru, 343 C1
 *
 */
package EasyFilterClient.Communication;

import EasyFilterClient.UI.EasyFilterClientUI;
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
    private int clientPort;

    /**
     * Constructor Remembers the server address
     */
    public BlockingUdpConnection() {
        this.setServerIP(EasyFilterClientUI.getClientIP());
        this.setServerPort(EasyFilterClientUI.getClientPort());
    }

    /**
     * Opens a connection to a server
     */
    @Override
    public void openConnection() {
        try {
            // Open server socket
            this.setUdpSocket(new DatagramSocket());
            this.setClientPort(this.getUdpSocket().getLocalPort());
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
        boolean notFinishedSendingImage = true;
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

        // download
        if (pkg.getRequestType() == 1) {
            buf = ByteBuffer.allocate(22 + packageBytes.length);
            b = 0;
            // add server IP
            for (j = 0; j < this.getServerIP().length(); j++) {
                buf.put((byte) this.getServerIP().charAt(j));
            }
            for (j = 0; j < 15 - this.getServerIP().length(); j++) {
                buf.put((byte) 0);
            }
            // add server port

            port = this.getClientPort() + "";
            for (j = 0; j < port.length(); j++) {
                buf.put((byte) port.charAt(j));
            }
            for (j = 0; j < 5 - port.length(); j++) {
                buf.put((byte) 0);
            }

            buf.put((byte) -1);
            buf.put((byte) -1);

            // add the package to the stream
            buf.put(ByteBuffer.wrap(packageBytes));
            buf.flip();
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
            return;
        }

        // the buffer for the received packet
        // bytes 0-14  contain the client IP
        // bytes 15-19          - the client port
        // bytes 20-21          - the chunk position
        // byte  22-23          - number of chunks for current client
        numberOfSentPackets = (int) Math.ceil((double) packageBytes.length / 14000);
        for (i = 0; i < numberOfSentPackets; i++) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(BlockingUdpConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
            // compute the chunk size
            if (14000 * numberOfSentPackets == packageBytes.length) {
                chunkSize = 14000;
            } else {
                if (i == numberOfSentPackets - 1) {
                    chunkSize = packageBytes.length - (numberOfSentPackets - 1) * 14000;
                } else {
                    chunkSize = 14000;
                }
            }

            buf = ByteBuffer.allocate(chunkSize + 22);
            b = 0;
            // add server IP
            for (j = 0; j < this.getServerIP().length(); j++) {
                buf.put((byte) this.getServerIP().charAt(j));
            }
            for (j = 0; j < 15 - this.getServerIP().length(); j++) {
                buf.put((byte) 0);
            }
            // add server port

            port = this.getClientPort() + "";
            for (j = 0; j < port.length(); j++) {
                buf.put((byte) port.charAt(j));
            }
            for (j = 0; j < 5 - port.length(); j++) {
                buf.put((byte) 0);
            }

            buf.put((byte) i);
            buf.put((byte) numberOfSentPackets);

            // add image content to packet
            buf.put(ByteBuffer.wrap(packageBytes, i * 14000, chunkSize));
            buf.flip();
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

    /**
     * Receive an image from the server
     *
     * @return Package instance sent by the server
     */
    @Override
    public common.Package receiveRequest() {
        common.Package pkg = null;

        int chunkPosition, numberOfChunks, i, j, k, imageSize;
        Vector<byte[]> chunk = null;
        byte[] image;
        ByteArrayInputStream baos = null;
        ObjectInputStream ois = null;
        boolean isFirstPackage = true;
        byte[] tempByteArray;
        int receivedChunks = 0;

        // the buffer for the packet to be sent
        // bytes 0-19        - not used
        // bytes 20-21       - the chunk position
        // byte  22-23       - number of chunks for current client

        while (true) {
            try {
                byte[] buf = new byte[14022];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);

                this.getUdpSocket().receive(packet);

                chunkPosition = buf[20];
                numberOfChunks = buf[21];

                if (isFirstPackage) {
                    chunk = new Vector<byte[]>(numberOfChunks);
                    for (k = 0; k < numberOfChunks; k++) {
                        tempByteArray = new byte[1];
                        chunk.add(tempByteArray);
                    }
                    isFirstPackage = false;
                }
                // add image chunks
                chunk.setElementAt(buf, chunkPosition);
                receivedChunks++;
                // check for last chunk
                if (numberOfChunks == receivedChunks) {
                    // the image is complete
                    // get the image size
                    int lastChunkSize = chunk.elementAt(numberOfChunks - 1).length;
                    imageSize = 14000 * (numberOfChunks - 1) + lastChunkSize;
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
                        return pkg;
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(BlockingUdpConnection.class.getName()).log(Level.SEVERE, null, ex);
                    }
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

    public int getClientPort() {
        return clientPort;
    }

    private void setClientPort(int clientPort) {
        this.clientPort = clientPort;
    }
}