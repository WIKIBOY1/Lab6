package mainPart;

import commands.*;
import exceptions.*;
import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientPart {
    private String lastString;
    private final InputStream inputStream;
    private final Scanner in;
    private final ServerConnect serverDeliver;
    private final CommandDecoder cd = new CommandDecoder();
    private int attempt = 0;

    public ClientPart (ServerConnect serverDeliver, InputStream inputStream){
        this.serverDeliver = serverDeliver;
        this.inputStream = inputStream;
        this.in = new Scanner(this.inputStream);
        try {
            readFromFile();
        } catch (ConnectionException e) {System.out.println(e.getMessage());System.exit(1);}
    }

    private void readFromFile() {
        attempt++;
        if (attempt == 10) System.exit(0);
        String filePath = safeRead("Введите путь к файлу, из которого необходимо считать коллекцию:");
        try {
            serverDeliver.writeData(filePath);
            String a = (String) serverDeliver.readData();
            System.out.println(a);
        } catch (InvalidInputException e) {System.out.println(e.getMessage());readFromFile();}
    }

    private String safeRead(String field) {
        if (this.inputStream == System.in) {
            System.out.println(field);
        }
            try {
                lastString = in.nextLine();
            } catch (NoSuchElementException e) {
                in.close();
                System.exit(0);
            }
            return lastString;
    }

    public void understanding() {
        String command = "";
        while (!command.equals("exit")) {
            command = safeRead("Введите команду: (help - узнать список команд, exit - выход из программы (без сохранения))");
            try {
                understand(command);
            } catch (ConnectionException e) {
                System.out.println(e.getMessage());
                break;
            }
            catch (InvalidInputException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void understand(String command) throws ConnectionException{
            String s = "";
        try {
            if (!command.equals("exit")) {
                if (!command.equals("Unreadable Command")) {
                    Command inputCommand = cd.decode(command.trim());
                    if (inputCommand.getClass() == ExecuteScriptCommand.class) {
                        try {
                            inputCommand.execute();
                            if (((ExecuteScriptCommand) inputCommand).getScanner() != null) {
                                String com = ((ExecuteScriptCommand) inputCommand).giveNewCommandFromFile();
                                while (com != null) {
                                    try {
                                        understand(com);
                                    } catch (ConnectionException e) {
                                        System.out.println(e.getMessage());
                                        System.exit(1);
                                    }
                                    com = ((ExecuteScriptCommand) inputCommand).giveNewCommandFromFile();
                                }
                                ExecuteScriptCommand.executeScriptCommands.clear();
                            }
                        } catch (InfiniteRecursionException e) {
                            e.getMessage();
                        }
                    } else {
                        serverDeliver.writeData(inputCommand);
                        s = ((String) serverDeliver.readData());
                    }
                    if (s != null && s.startsWith("Введите название билета:")) {
                        serverDeliver.toggleReconnectionIsNeeded();
                        while (!s.contains("Ticket created")) {
                            String safeRead = safeRead(s);
                            try {
                                serverDeliver.writeData(safeRead);
                            } catch (ConnectionException e) {
                                System.out.println(e.getMessage());
                                serverDeliver.toggleReconnectionIsNeeded();
                                return;
                            }
                            s = ((String) serverDeliver.readData());
                        }
                    }
                    String output = null;
                    if (s != null) {
                        output = s.replace("Ticket created", "");
                    }
                    System.out.println(output);
                }
            }
        } catch (NumberFormatException e) {System.out.println("Аргумент имеет неправльный тип (для id - int, для price - double)");}
        catch(NullPointerException | IllegalArgumentException e){
                if (command.equals("exit")) { System.exit(0); }
                System.out.println("Такой команды не существует.");
        }
        catch(IllegalCountOfArgumentsException e){
                System.out.println(e.getMessage());
        }
    }
}
