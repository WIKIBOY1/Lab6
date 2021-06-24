package commands;

import collection.Flat;

import java.util.TreeSet;

/**
 * Команда, показывающая элементы коллекции
 */
public class ShowCommand extends CommandWithoutAdditionalArgument{

    public ShowCommand(TreeSet<Flat> c) {
        this.c.setFlats(c);
    }

    @Override
    public String execute() {
        StringBuilder result = new StringBuilder();
        c.getFlats().forEach(t -> result.append(t).append("\n"));
        if (c.getFlats().size() != 0) return result.toString();
        return "Нечего показывать";
    }

    @Override
    public String toString() {
        return "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    }
}
