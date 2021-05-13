package commands;

import collection.Ticket;

import java.util.LinkedList;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Command class that remove tickets from the collection greater than given one
 */
public class RemoveGreaterCommand extends CommandWithAdditionalArgument{
    /**ticket name*/
    private int id;

    /**
     * Constructor with parameter
     * @param c - collection of tickets
     */
    public RemoveGreaterCommand(TreeSet<Ticket> c) {this.c = c;}



    /**
     * Remove tickets from the collection greater than given one
     */
    @Override
    public String execute() {
        Ticket ticket;
        try {
            ticket = c.stream().filter(t -> t.getId() == id).collect(Collectors.toList()).get(0);
        } catch (IndexOutOfBoundsException e) {
            return "Элемента с таким именем найти не удалось.";
        }
        Ticket finalTicket = ticket;
        if (c.removeIf(i -> i.getPrice() > finalTicket.getPrice())) return "Сколько-то элементов было удалено";
        return "К сожалению, ничего удалить не удалось";
    }

    /**
     * Getting ticket name
     * @param obj - ticket name
     */
    @Override
    public void addArgument(String obj) {
        id = Integer.parseInt(obj);
    }

    /**
     * @return info about command
     */
    @Override
    public String toString() {
        return "remove_greater <ticket id> : удалить из коллекции все элементы, превышающие заданный";
    }
}
