package commands;

import collection.Ticket;

import java.util.LinkedList;
import java.util.TreeSet;

/**
 * Command class that clears the collection
 */
public class ClearCommand extends CommandWithoutAdditionalArgument{

    /**
     * Constructor with parameter
     * @param c - collection of tickets
     */
    public ClearCommand(TreeSet<Ticket> c) {
        this.c = c;
    }

    /**
     * Clear the collection
     */
    @Override
    public String execute() {
        return c.removeAll(c) ? "Коллекция была очищена." : "Коллекция итак была пуста.";
}

    /**
     * @return info about command
     */
    @Override
    public String toString() {
        return "clear : очистить коллекцию";
    }
}
