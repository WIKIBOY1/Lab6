package mainPart;

import java.util.Scanner;

/**
 * This is main
 */
public class Main {

    private static volatile Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            ServerMaker serverMaker = new ServerMaker(4649);
            ServerPart serverPart = new ServerPart(serverMaker);
            Computer computer = new Computer(serverPart, serverMaker);
            serverPart.getDataBaseWorker().connectToDB();
            serverPart.getDataBaseWorker().getCollectionFromDB();
            Thread thread = new Thread(computer::computingRead);
            Thread thread1 = new Thread(computer::computingWrite);
            Thread thread2 = new Thread(() -> {
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNext()) {
                    String next = scanner.next();
                    if (next.trim().toLowerCase().equals("exit")) System.exit(0);
                }
            });
            thread.start();
            thread1.start();
            thread2.start();
            serverPart.readCommands();
        } catch (Exception e) {
            System.out.println("Что-то пошло не так или клиенту надоело здесь сидеть и вводить рандомные комманды");
            e.printStackTrace();
        }
        System.exit(0);
    }
}