package mainPart;


import exceptions.ConnectionException;

import java.io.IOException;

/**
 * @author Ivanov Georgii R3140
 */
public class Main {
    public static void main(String[] args) throws IOException {
        ServerConnect serverConnect = new ServerConnect("127.0.0.1", 5556);
        ClientPart clientPart = new ClientPart(serverConnect, System.in);
        try{clientPart.readFromFile();
        } catch (ConnectionException e) {System.out.println(e.getMessage());System.exit(1);}
        clientPart.understanding();
        }
}
