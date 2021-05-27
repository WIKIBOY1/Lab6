package commands;

import exceptions.IdNotFoundException;
import exceptions.IllegalCountOfArgumentsException;
import exceptions.InfiniteRecursionException;
import mainPart.CommandDecoder;
import collection.Flat;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * Команда, которая выполняет execute script
 */
public class ExecuteScriptCommand extends CommandWithAdditionalArgument{
    private String filePath;
    private final TreeSet<Flat> c;
    public static final HashSet<String> executeScriptCommands = new HashSet<>();
    private transient Scanner scanner;
    private CommandDecoder cd;


    public ExecuteScriptCommand(TreeSet<Flat> c) {this.c = c;}


    /**
     * Execute script
     */
    @Override
    public String execute() {
        File file = new File(filePath);
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("Указанный файл не был найден");
        }
        catch (SecurityException ex) {
            System.out.println("Не хватает прав доступа для работы с файлом.");
        }
        cd = new CommandDecoder(c);
        //executeScriptCommands.clear();
        return "Начало выполнения скрипта";
    }

    public String giveNewCommandFromFile() throws InfiniteRecursionException {

        String command;
        if (scanner.hasNextLine()) {
            command = scanner.nextLine();
            try {
                if (executeScriptCommands.contains(command)) {
                    throw new InfiniteRecursionException();
                }
                if (command.contains("execute_script")) executeScriptCommands.add(command);
                System.out.println(command);
                if (command.equals("exit")) System.exit(0);
                cd.decode(command);
                return command;
            } catch (NullPointerException | IllegalArgumentException | IllegalCountOfArgumentsException | IdNotFoundException e) {
                System.out.println("Не удалось выполнить команду");
            }
        } else return null;
        return "Unreadable Command";
    }

    public Scanner getScanner() {return scanner;}


    /**
     * Добавление дополнительного параметра flat filePath
     * @param obj - flat filePath
     */
    @Override
    public void addArgument(String obj) {
        filePath = obj;

    }

    @Override
    public String toString() {
        return "execute_script <file_path> : считать и исполнить скрипт из указанного файла.";
    }
}