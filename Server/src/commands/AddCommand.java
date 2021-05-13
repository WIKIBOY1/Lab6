package commands;
import collection.Ticket;
import mainPart.CommandDecoder;

import java.util.LinkedList;
import java.util.TreeSet;

/**
 * Command class that adds the element to the collection
 */
public class AddCommand extends CommandWithoutAdditionalArgument {

    public Ticket ticket = new Ticket();

    /**
     * Constructors with parameter
     * @param c - collection of tickets
     */
    public AddCommand(TreeSet<Ticket> c) {
        this.c = c;
    }


    /**
     * add element to the collection
     */
    @Override
    public String execute() {
        c.add(ticket);
       // CommandDecoder.sort(c);
        return "Новый элемент был успешно добавлен в коллекцию";
    }

    /**
     * @return info about command
     */
    @Override
    public String toString() {
        return "add : добавить новый элемент в коллекцию";
    }
}
