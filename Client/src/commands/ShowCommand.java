package commands;

import collection.Ticket;

import java.util.LinkedList;
import java.util.TreeSet;

/**
 * Command class which outputs elements in the collection
 */
public class ShowCommand extends CommandWithoutAdditionalArgument{

    /**
     * Output elements in the collection
     * @param c - collection of tickets
     */
    public ShowCommand(TreeSet<Ticket> c) {
        this.c = c;
    }

    @Override
    public String execute() {
        StringBuilder result = new StringBuilder();
        c.forEach(t -> result.append(t).append("\n"));
        if (c.size() != 0) return result.toString();
        return "Нечего показывать";
    }

    @Override
    public String toString() {
        return "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    }
}
