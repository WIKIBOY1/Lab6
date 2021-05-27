package mainPart;

import collection.Flat;
import commands.*;
import exceptions.IllegalCountOfArgumentsException;

import java.io.Serializable;
import java.util.*;

/**
 * Класс, который хранит команды и декодирует их
 */
public class CommandDecoder implements Serializable {
    private final LinkedHashMap<String, Command> commands = new LinkedHashMap<>();
    private final TreeSet<Flat> c;

    /**
     * Конструктор без параметров
     */
    public CommandDecoder() {
        c = new TreeSet<>(Comparator.comparing(Flat::getId));
        addCommands();
    }

    /**
     * Конструктор с параметром
     * @param c - коллекция квартир
     */
    public CommandDecoder(TreeSet<Flat> c) {
        this.c = c;
        addCommands();
    }

    /**
     * Добавление команд
     */
    private void addCommands() {
        commands.put("info", new InfoCommand(c));
        commands.put("show", new ShowCommand(c));
        commands.put("add", new AddCommand(c));
        commands.put("update", new UpdateCommand(c));
        commands.put("remove_by_id", new RemoveByIdCommand(c));
        commands.put("clear", new ClearCommand(c));
        commands.put("execute_script", new ExecuteScriptCommand(c));
        commands.put("remove_greater", new RemoveGreaterCommand(c));
        commands.put("remove_lower", new RemoveLowerCommand(c));
        commands.put("print_ascending", new PrintAscendingCommand(c));
        commands.put("filter_contains_name", new FilterContainsName(c));
        commands.put("remove_all_by_house", new RemoveAllByHouseCommand(c));
        commands.put("help", new HelpCommand(commands));
    }

    /**
     * Командный декодер
     * @param com - команда
     */
    public Command decode(String com) {

            String[] s, s1;
            s = com.split(" ");
            s1 = com.split("\t");
            if (s1.length > s.length) s = s1;
            if (s.length == 0) throw new NullPointerException();
            //if (!s[0].equals("exit")) {
                int countOfArguments = s.length - 1;
                Command cd = commands.get(s[0].toLowerCase());
                try {
                    if (cd.correctCountOfArguments(countOfArguments))  {
                        if (countOfArguments == 1) ((CommandWithAdditionalArgument) cd).addArgument(s[1]);
                        return cd;
                    } else throw new IllegalCountOfArgumentsException();
                } catch (NumberFormatException e) {
                    System.out.println("Аргумент имеет неправльный тип (для id - int, для price - double)");
                }

          //  }
        return null;
    }

    public TreeSet<Flat> getCollection() {
        return c;
    }

}
