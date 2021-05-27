package commands;

import java.util.HashMap;

/**
 * Команда, которая выводит информацию о доступных командах
 */
public class HelpCommand extends CommandWithoutAdditionalArgument {

    private final HashMap<String, Command> h;

    public HelpCommand(HashMap<String, Command> h) {this.h = h;}

    /**
     * Вывод информации о доступных командах
     */
    @Override
    public String execute() {
        StringBuilder builder = new StringBuilder();
        h.values().stream().forEach(e -> builder.append(e).append("\n"));
        return builder.toString();
    }

    @Override
    public String toString() {
        return "help : вывести справку по доступным командам";
    }
}
