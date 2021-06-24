package mainPart;

/**
 * @author Ivanov Georgii R3140
 */
public class Main {
    public static void main(String[] args) {
        ServerConnect serverConnect = new ServerConnect("127.0.0.1", 4649);
        ClientPart clientPart = new ClientPart(serverConnect, System.in, System.console());
        clientPart.authorization();
        clientPart.understanding();
    }
}
