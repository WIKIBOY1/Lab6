package mainPart;

import exceptions.ConnectionException;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class ServerMaker {
    protected SocketAddress socketAddress;
    protected ServerSocketChannel serverChannel;
    protected Selector selector;
    protected Serializable request;
    protected Serializable response;
    protected Integer messageLength;

    public ServerMaker(int PORT) {
        this.socketAddress = new InetSocketAddress(PORT);
        try {
            selector = Selector.open();
            this.serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.bind(socketAddress);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Ожидание подключения.");
        } catch (IOException e) {
            System.out.println("Клиент не подключён к серверу");
            System.exit(1);
        }
    }

    public Serializable waitForRead() throws  ConnectionException {
        while (response == null) {
            try {
                selectorProcessing(selector);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Serializable responce1 = response;
        response = null;
        return responce1;
    }

    public void waitForWrite(Serializable request) throws ConnectionException {
        this.request = request;
        while (this.request != null) {
            try {
                selectorProcessing(selector);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void selectorProcessing(Selector selector) throws IOException, ConnectionException {
        if (selector.selectNow() == 0) return;
        Set<SelectionKey> keys = selector.selectedKeys();
            for (Iterator<SelectionKey> iterator = keys.iterator(); iterator.hasNext(); ) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isValid()) {
                    if (key.isAcceptable()) acceptConnection(key);
                    if (key.isReadable()) {
                        response = deserialize(readData(key));
                    }
                    if (key.isWritable()) {
                        writeData(serialize(request), key);
                    }
                }
        }
    }

    private void acceptConnection(SelectionKey key) throws IOException {
        SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(key.selector(), SelectionKey.OP_READ);
    }

    private byte[] readData(SelectionKey key)  {
    SocketChannel channel = (SocketChannel) key.channel();
    byte[] a = new byte[4096];
    ByteBuffer buffer = ByteBuffer.wrap(a);
    try {
        buffer.clear();
        channel.read(buffer);
        try {
            channel.configureBlocking(false);
            channel.register(key.selector(), SelectionKey.OP_WRITE);
        } catch (IOException e) {
            closeChannel(channel);
        }
        buffer.flip();
        buffer.clear();
        return a;
    } catch (IOException e) {
        throw new ConnectionException("Клиент не подключён серверу для получения данных");
    }
    }

    public byte[] serialize(Serializable obj) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(obj);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return null;
    }

    public Serializable deserialize(byte[] rawData) {

        try {
            if (rawData != null) {
                ByteArrayInputStream bis = new ByteArrayInputStream(rawData);
                ObjectInputStream objectInputStream = new ObjectInputStream(bis);
                return (Serializable) objectInputStream.readObject();
            }
        } catch (IOException | ClassNotFoundException ex) {
            //ex.printStackTrace();
        }
        return null;

    }

    private void writeData(byte[] bytes, SelectionKey key) throws ConnectionException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        try {
            channel.write(buffer);
            request = null;
        } catch (IOException e) {
            throw new ConnectionException("Клиент не подключён к серверу для отправки данных");
        }
        buffer.flip();
        buffer.clear();
        try {
            channel.configureBlocking(false);
            channel.register(key.selector(), SelectionKey.OP_READ);
        } catch (IOException e) {
            closeChannel(channel);
        }


    }

    private void closeChannel(SocketChannel channel) {
        try {
            channel.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
