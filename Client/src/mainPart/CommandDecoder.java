package mainPart;

import java.io.Serializable;
import java.util.*;

import collection.Flat;
import collection.IdComparator;
import commands.*;
import exceptions.*;

/**
 * Класс, который контролирует коллекцию с командами
 */
public class CommandDecoder implements Serializable {
    private final LinkedHashMap<String, Command> commands = new LinkedHashMap<>();
    private final TreeSet<Flat> c;

    public CommandDecoder() {
        c = new TreeSet<>(new IdComparator());
        addCommands();
    }

    public CommandDecoder(TreeSet<Flat> c) {
        this.c = c;
        addCommands();
    }

    /**
     * Добавление команд в hashmap
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
     * Декодер команд
     * @param com - incoming command
     */
    public Command decode(String com) {

            String[] s, s1;
            com = com.trim();
            s = com.split(" ");
            s1 = com.split("\t");
            if (s1.length > s.length) s = s1;
            if (s.length == 0) throw new NullPointerException();
            if (!s[0].equals("exit")) {
                int countOfArguments = s.length - 1;
                Command cd = commands.get(s[0].toLowerCase());
                    if (cd.correctCountOfArguments(countOfArguments))  {
                        if (countOfArguments == 1) {
                            ((CommandWithAdditionalArgument) cd).addArgument(s[1]);
                        }
                        return cd;
                    } else throw new IllegalCountOfArgumentsException();
            }
        return null;
    }

    public TreeSet<Flat> getCollection() {
        return c;
    }
}
