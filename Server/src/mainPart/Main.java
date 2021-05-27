package mainPart;

import collection.Flat;
import collection.IdComparator;

import java.util.Scanner;
import java.util.TreeSet;

/**
 * This is main
 */
public class Main {

    private static volatile Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        TreeSet<Flat> collection = new TreeSet<>(new IdComparator());
        boolean wait = false;
        try {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    scanner = new Scanner(System.in);
                    FileWorker fl = new FileWorker(collection);
                    String command;
                    do {
                        if (isInterrupted()) break;
                        command = scanner.nextLine();
                        if (command.toLowerCase().trim().equals("save")) {
                            System.out.println(fl.save ("USER"));
                        }
                    } while (!command.toLowerCase().trim().equals("exit"));
                    System.out.println(fl.save ("USER"));
                    System.exit(0);
                }
            };
            thread.start();
            ServerPart serverPart;
            ServerMaker  serverMaker = new ServerMaker(5556);
            serverMaker.connect();
            while (!wait) {
                serverMaker.setSelector();
                System.out.println("1");
                serverPart = new ServerPart(collection, serverMaker);
                System.out.println("2");
                serverPart.readCommands();
                System.out.println("3");
            }
        } catch (Exception e) {
            System.out.println("Что-то пошло не так или клиенту надоело здесь сидеть и вводить рандомные комманды");
            e.printStackTrace();
        }
        FileWorker fl = new FileWorker(collection);
        System.out.println(fl.save ("USER"));
        System.exit(0);
    }
}
