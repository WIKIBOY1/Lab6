package mainPart;

import collection.IdComparator;
import collection.Ticket;
import sun.reflect.generics.tree.Tree;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * This is main
 */
public class Main {

    private static volatile Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        TreeSet<Ticket> collection = new TreeSet<>(new IdComparator());
        try {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    scanner = new Scanner(System.in);
                    FileWorker fl = new FileWorker(collection);
                    String command = "";
                    do {
                        if (isInterrupted()) break;
                        command = scanner.nextLine();
                        if (command.toLowerCase().trim().equals("save")) {
                            System.out.println(fl.write ("Col.csv"));
                        }
                    } while (!command.toLowerCase().trim().equals("exit"));
                    System.out.println(fl.write ("Col.csv"));
                    System.exit(0);
                }
            };
            thread.start();
            ServerMaker serverMaker = new ServerMaker(2424);
            ServerPart serverPart = new ServerPart(collection, serverMaker);
            serverPart.readCommands();
        } catch (Exception e) {
            System.out.println("Что-то пошло не так или клиенту надоело здесь сидеть и вводить рандомные комманды");
            e.printStackTrace();
        }
        FileWorker fl = new FileWorker(collection);
        System.out.println(fl.write("Col.csv"));
        System.exit(0);
    }
}
