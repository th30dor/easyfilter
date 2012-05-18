/*
 * Proiect PDSD - EasyFilter
 *
 * @author Gherghescu Teo, 343 C1
 * @author Stoean Bogdan, 343 C1
 * @author Marin Alexandru, 343 C1
 *
 */
package EasyFilterServer.Communication;

import com.sun.org.apache.bcel.internal.generic.InstructionConstants.Clinit;
import common.EasyPropertiesReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the blocking TCP connection module for server-client
 */
public class ClientNonBlockingTcpConnection implements CommunicationInterface {

    /**
     * The channel on which we'll accept connections
     */
    private static ServerSocketChannel serverChannel;
    /**
     * The channel to the client
     */
    private SocketChannel socketChannel;
    /**
     * The selector we'll be monitoring
     */
    private static Selector selector;
    private ByteBuffer rBuffer;
    private ByteBuffer wBuffer;
    /**
     * The client socket
     */
    private Socket clientSocket = null;

    /**
     * Constructor
     */
    public ClientNonBlockingTcpConnection() {
        // todo getters and setters
        this.rBuffer = ByteBuffer.allocate(1000000);
        this.wBuffer = ByteBuffer.allocate(1000000);
    }

    /**
     * Opens a socket with the data read from the config file;
     * Used to communicate with the client/other server instances
     */
    @Override
    public void openConnection() {
        EasyPropertiesReader epr = new EasyPropertiesReader("config/config.ini");

        try {
            this.setSelector(SelectorProvider.provider().openSelector());
            this.setServerSocketChannel(ServerSocketChannel.open());
            this.getServerSocketChannel().configureBlocking(false);
            InetSocketAddress isa = new InetSocketAddress(
                    epr.readProperty("Address", "IP"),
                    Integer.parseInt(epr.readProperty("Address", "port")));
            this.getServerSocketChannel().socket().bind(isa);
            this.getServerSocketChannel().register(
                    this.getSelector(),
                    SelectionKey.OP_ACCEPT);
        } catch (IOException ex) {
            Logger.getLogger(ClientNonBlockingTcpConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Whether the connection is successfully created or not
     *
     * @return bool
     */
    @Override
    public boolean connectionAccepted() {
        try {
            // wait for connection to be accepted
            while (true) {
                this.getSelector().select(10000);
                // process keys with request
                Iterator<SelectionKey> selectedKeys = this.getSelector().selectedKeys().iterator();

                while (selectedKeys.hasNext()) {
                    SelectionKey key = selectedKeys.next();
                    selectedKeys.remove();

                    if (key.isAcceptable()) {
                        return this.doAccept(key);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Exceptie in thread-ul Selectorului: " + e);
            e.printStackTrace();
            return false;
        }
    }

    protected boolean doAccept(SelectionKey key) throws Exception {
        try {
            this.setSocketChannel(this.getServerSocketChannel().accept());
            this.getSocketChannel().configureBlocking(false);
            this.getSocketChannel().register(
                this.getSelector(),
                SelectionKey.OP_READ | SelectionKey.OP_WRITE
            );
//            key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        } catch (Exception ex) {
            return false;
        }

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
        try {
            // wait for connection to be accepted
            boolean isDone = false;
            while (! isDone) {
                this.getSelector().select(100);
                // process keys with request
                Iterator<SelectionKey> selectedKeys = this.getSelector().selectedKeys().iterator();

                while (selectedKeys.hasNext()) {
                    SelectionKey key = selectedKeys.next();
//                    selectedKeys.remove();

                    if (key.isWritable()) {
                        this.doWrite(key, obj);
                        isDone = true;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Exceptie in thread-ul Selectorului: " + e);
            e.printStackTrace();
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
        try {
            // wait for connection to be accepted
            while (true) {
                this.getSelector().select(100);
                // process keys with request
                Iterator<SelectionKey> selectedKeys = this.getSelector().selectedKeys().iterator();

                while (selectedKeys.hasNext()) {
                    SelectionKey key = selectedKeys.next();
                    selectedKeys.remove();

                    if (key.isReadable()) {
                        return this.doRead(key);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Exceptie in thread-ul Selectorului: " + e);
            e.printStackTrace();
            return null;
        }
    }

    // todo mai multi clienti
    protected common.Package doRead(SelectionKey key) throws Exception {
        common.Package localPackage = null;

        SocketChannel socketChannel = (SocketChannel) key.channel();
        this.rBuffer.clear();
        int numRead = 1;

        // read the whole file into the buffer
        while(numRead >= 0) {
            Thread.sleep(10);
            try {
                numRead = socketChannel.read(this.rBuffer);
            } catch (Exception e) {
                // finish reading
                numRead = -1000000000;
            }

            try {
                // convert ByteArray to Package
                InputStream stream = new ByteArrayInputStream(this.rBuffer.array(), 0, this.rBuffer.limit());
                ObjectInputStream ois = new ObjectInputStream(stream);
                localPackage = (common.Package)ois.readObject();
                break;
            } catch (Exception ex) {
                continue;
            }
        }
        this.rBuffer.flip();
        // close the key channel
//        if (numRead <= 0) {
//            System.out.println("[NIOTCPServer] S-a inchis socket-ul asociat cheii " + key);
//            key.channel().close();
//            key.cancel();
//        }

        return localPackage;
    }

    protected void doWrite(SelectionKey key, Object obj) throws Exception {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        
        // convert package to byte array
        byte[] bytes = null;
        int numWritten = 0;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            baos.close();
            bytes = baos.toByteArray();
        } catch (Exception ex) {
        }

        this.wBuffer = ByteBuffer.wrap(bytes);
        int limit = this.wBuffer.limit();
        
        while(numWritten < limit) {
            numWritten += socketChannel.write(this.wBuffer);
        }
    }

    /**
     * Closes the connection to a client / server
     */
    @Override
    public void closeConnection() {
    }

    // ~~~~~~~~ Getters and Setters ~~~~~~~~~~
    public Selector getSelector() {
        return ClientNonBlockingTcpConnection.selector;
    }

    public void setSelector(Selector selector) {
        ClientNonBlockingTcpConnection.selector = selector;
    }

    public ServerSocketChannel getServerSocketChannel() {
        return serverChannel;
    }

    private void setServerSocketChannel(ServerSocketChannel serverChannel) {
        ClientNonBlockingTcpConnection.serverChannel = serverChannel;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    protected void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    private void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }
}