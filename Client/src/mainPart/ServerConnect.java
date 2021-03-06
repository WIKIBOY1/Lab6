package mainPart;

import exceptions.ConnectionException;
import exceptions.InvalidInputException;

import java.io.*;
import java.net.*;

public class ServerConnect implements Serializable {

    private final int connectionAttempts = 10;

    protected Socket client;
    private final String ip;
    private final int port;
    private boolean reconnectionIsNeeded = true;

    private final int maxLengthOfBytesArray = 4096;

    public ServerConnect (String ADDR, int PORT) {
        ip = ADDR;
        port = PORT;
    }

    public void writeData(Serializable data) throws ConnectionException {
        for (int attempt = 0; attempt <= connectionAttempts; attempt++) {
            try {
                client = new Socket(ip, port);
                OutputStream outputStream = client.getOutputStream();
                byte[] bytes = serialize(data);
                if (bytes.length <= maxLengthOfBytesArray) outputStream.write(bytes);
                else throw new InvalidInputException();
                break;
            } catch (IOException e) {
                if (reconnectionIsNeeded) makeDelay(attempt);
                else attempt = connectionAttempts;
                if (attempt == connectionAttempts) throw new ConnectionException("Время ожидания переподключения для отправки данных исстекло");
            }
        }
    }

    public Serializable readData() throws ConnectionException{
        try {
            InputStream inputStream = client.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            Object obj = objectInputStream.readObject();
            return (Serializable) obj;
        } catch (IOException | ClassNotFoundException e) {
            throw new ConnectionException("Нет подключения с сервером, чтобы получить данные");
        }
    }

    private byte[] serialize(Serializable data) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(data);
        objectOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }

    private void makeDelay(int attempt) {
        System.out.println("Переподключение... (Попытка " + attempt + " из " + connectionAttempts + ")");
        try {
            int connectionDelayMillis = 5000;
            Thread.sleep(connectionDelayMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
