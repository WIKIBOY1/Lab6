package mainPart;

import commands.*;
import exceptions.*;
import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.Console;

public class ClientPart {
    private String lastString = "";
    private final InputStream inputStream;
    private final Scanner in;
    private final ServerConnect serverDeliver;
    private final CommandDecoder cd = new CommandDecoder();
    private int attempt = 0;
    private UserData userData;
    private BufferedReader br;
    private Console console;

    public ClientPart (ServerConnect serverDeliver, InputStream inputStream, Console console) {
        this.serverDeliver = serverDeliver;
        this.inputStream = inputStream;
        this.in = new Scanner(this.inputStream);
        this.br = new BufferedReader(new InputStreamReader(inputStream));
        this.console = console;
    }

    private String safeRead(String field) {
        do {
            if (this.inputStream == System.in) {
                System.out.println(field);
            }
            try {
                lastString = in.nextLine();
            } catch (NoSuchElementException e) {
                in.close();
                System.exit(0);
            }
        } while (lastString.length() > 200);
        return lastString;
    }

    public void authorization() {
        String answer = safeRead("Есть ли у вас аккаунт? (Введите No, чтобы создать аккаунт) ");
        if (answer.trim().toLowerCase().equals("no")) createNewAccount();
        String result = "";
        while (!result.equals("Вход успешно выполнен")) {
            System.out.println("Авторизация: ");
            String[] loginAndPassword = loginAndPasswordInput();
            userData = new UserData(loginAndPassword[0], loginAndPassword[1]);
            serverDeliver.writeData(userData);
            result = (String) serverDeliver.readData();
            System.out.println(result);
        }
    }

    public void writeUserData(Serializable data) {
        userData.setData(data);
        serverDeliver.writeData(userData);
    }

    public void createNewAccount() {
        String[] loginAndPassword = loginAndPasswordInput();
        serverDeliver.writeData(new UserData(loginAndPassword[0], loginAndPassword[1], false));
        System.out.println(serverDeliver.readData());
    }

    private String[] loginAndPasswordInput() {
        String login = "";
        String password = "";
        char[] pass;
        do {
            login = safeRead("Введите логин: ");
        } while (login.equals(""));
        do {
            System.out.println("Введите пароль: (не допускаются пробельные символы)");
            pass = console.readPassword();
        } while (pass.equals("") || pass.equals(" "));
        for(char c : pass){
            password += password + c;
        }
        String[] result = new String[2];
        result[0] = login;
        result[1] = password;
        return result;
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
                    ((CommandWithFlatWithoutArgument) inputCommand).getFlat().setId(-1);
                }else if(inputCommand.getClass() == UpdateCommand.class){
                    AddFlat addFlatCommand = new AddFlat();
                    ((UpdateCommand) inputCommand).setFlat(addFlatCommand.addFlat(in));
                } else if(inputCommand.getClass() == RemoveAllByHouseCommand.class){
                    AddFlat addFlatCommand = new AddFlat();
                    ((RemoveAllByHouseCommand) inputCommand).setHouse(addFlatCommand.addHouse(in));
                }
                if(inputCommand.getClass() != ExecuteScriptCommand.class){
                    writeUserData(inputCommand);
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
