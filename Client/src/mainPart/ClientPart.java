package mainPart;

import collection.States;
import commands.*;
import exceptions.*;
import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static commands.Checker.check;

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
    }

    public void readFromFile() throws IOException {
        boolean again = false;
        do {
            String filePath = safeRead("Введите переменную окружения файла, из которого необходимо считать коллекцию:");
            try {
//                byte[] bytes = serverDeliver.serialize(filePath);
//                Integer integer = bytes.length;
//                //String integerToString = integer.toString();
//                serverDeliver.writeData(integer);
//                System.out.println(integer);
//                Integer integer1 = Integer.parseInt((String) serverDeliver.readData());
//                System.out.println(integer1);
//                serverDeliver.writeData(bytes);
                serverDeliver.writeData(filePath);
                String b = (String) serverDeliver.readData();
                System.out.println(b);
                /*
                byte[] bytes = serverDeliver.serialize(inputCommand);
                    Integer integer = bytes.length;
                    serverDeliver.writeData(integer);
                    System.out.println(integer);
                    serverDeliver.writeData(inputCommand);
                    s = ((String) serverDeliver.readData());
                 */
                if (b.contains("успешно")) {
                    again = true;
                } else {
                    if(!tryAgain()){ System.out.println("Приятно было с вами поработать");System.exit(0);}
                }
            } catch (InvalidInputException e) {
                System.out.println(e.getMessage());
            }
        }while (!again);
    }

    private boolean tryAgain(){
        System.out.println("Хотите ввести переменную окружения[Да - 1, Нет - 2]?");
        return TryAddSomething.addSomething(check(in));
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

    public void understanding() throws IOException{
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

    private void understand(String command) throws ConnectionException, IOException{
            String s = "";
        try {
            if (!command.equals("exit")) {
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
                } else if (inputCommand.getClass() == AddCommand.class || inputCommand.getClass() == RemoveLowerCommand.class
                || inputCommand.getClass() == RemoveGreaterCommand.class) {
                    AddFlat addFlatCommand = new AddFlat();
                    ((CommandWithFlatWithoutArgument) inputCommand).setFlat(addFlatCommand.addFlat(in));
                }else if(inputCommand.getClass() == UpdateCommand.class){
                    AddFlat addFlatCommand = new AddFlat();
                    ((UpdateCommand) inputCommand).setFlat(addFlatCommand.addFlat(in));
                } else if(inputCommand.getClass() == RemoveAllByHouseCommand.class){
                    AddFlat addFlatCommand = new AddFlat();
                    ((RemoveAllByHouseCommand) inputCommand).setHouse(addFlatCommand.addHouse(in));
                }
                if(inputCommand.getClass() != ExecuteScriptCommand.class){
//                    byte[] bytes = serverDeliver.serialize(inputCommand);
//                    Integer integer = bytes.length;
//                    String integerToString = integer.toString();
//                    serverDeliver.writeData(integerToString);
//                    System.out.println(integer);
//                    Integer integer1 = Integer.parseInt((String) serverDeliver.readData());
//                    System.out.println(integer1);
                    serverDeliver.writeData(inputCommand);
                    s = ((String) serverDeliver.readData());
                }
                String output = null;
                if (s != null) {
                    output = s.replace("Flat created", "");
                }
                System.out.println(output);
            }
        } catch (NumberFormatException e) {System.out.println("Аргумент имеет неправльный тип (id - int)");}
        catch(NullPointerException | IllegalArgumentException e){
                if (command.equals("exit")) { System.exit(0); }
                System.out.println("Такой команды не существует.");
        }
        catch(IllegalCountOfArgumentsException e){
                System.out.println(e.getMessage());
        }
    }
}
