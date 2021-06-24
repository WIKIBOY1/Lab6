package commands;

import collection.Flat;

import java.util.*;

public class HistoryCommand extends CommandWithoutAdditionalArgument {
    private final LinkedHashMap<Class, String> history = new LinkedHashMap<>();
    private final List<String> commandsHistory = new ArrayList<>();

    @Override
    public String execute() {
        return commandsHistory.toString();
    }

    /**
     * Добавление команд
     */
    public void addHistory() {
        history.put(InfoCommand.class, "info");
        history.put(ShowCommand.class, "show");
        history.put(AddCommand.class, "add");
        history.put(UpdateCommand.class, "update");
        history.put(RemoveByIdCommand.class, "remove_by_id");
        history.put(ClearCommand.class, "clear");
        history.put(ExecuteScriptCommand.class, "execute_script");
        history.put(RemoveGreaterCommand.class, "remove_greater");
        history.put(RemoveLowerCommand.class, "remove_lower");
        history.put(PrintAscendingCommand.class, "print_ascending");
        history.put(FilterContainsName.class, "filter_contains_name");
        history.put(RemoveAllByHouseCommand.class, "remove_all_by_house");
        history.put(HistoryCommand.class, "history");
        history.put(HelpCommand.class, "help");
    }

    private String getNameOfCommand(Command command){
        String str = history.get(command.getClass());
        return str;
    }

    public void addPlus(Command command){
        String commandToString = getNameOfCommand(command);
        if (commandsHistory.size() == 9){
            commandsHistory.remove(0);
        }
        commandsHistory.add(commandToString);
    }

    @Override
    public String toString() {
        return "history : вывести последние 9 команд (без их аргументов)";
    }
}


