package commands;

import collection.Ticket;
import exceptions.IdNotFoundException;

import java.util.LinkedList;
import java.util.TreeSet;

/**
 * Command class that outputs the ticket by its id and delete it
 */
public class RemoveByIdCommand extends CommandWithAdditionalArgument{
    /**ticket id*/
    private int ID;

    /**
     * Constructor with parameter
     * @param c - collection of tickets
     */
    public RemoveByIdCommand(TreeSet<Ticket> c) {
        this.c = c;
    }

    /**
     * Output the ticket by its id and delete it
     */
    @Override
    public String execute() {
        if (!c.removeIf(i -> i.getId() == ID)) throw new IdNotFoundException();
        return "Билет с id: " + ID + " был успешно удалён.";
    }

    /**
     * Getting ticket id
     * @param obj - ticket id
     */
    @Override
    public void addArgument(String obj) {
        ID = Integer.parseInt(obj);
    }

    /**
     * @return info about command
     */
    @Override
    public String toString() {
        return "remove_by_id <id> : удалить элемент из коллекции по его id";
    }
}
