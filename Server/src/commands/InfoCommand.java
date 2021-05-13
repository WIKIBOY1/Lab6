package commands;

import collection.Ticket;

import java.util.Date;
import java.util.LinkedList;
import java.util.TreeSet;

/**
 * Command class which outputs info about collection
 */
public class InfoCommand extends CommandWithoutAdditionalArgument {


    /**
     * Constructor with argument
     * @param c - collection
     */
    public InfoCommand(TreeSet<Ticket> c) {
        this.c = c;
    }

    /**
     * Output info about collection (type, count of elements, creation time)
     */
    public String execute() {
        String result;
        result = "Тип коллекции: " + c.getClass() + "\n";
        result += "Количество элементов: " + c.size() + "\n";
        if (c.size() != 0 ) {
            Date date = new Date();
            for (Ticket t : c) {
                if (t.getDateOfCreation().getTime() < date.getTime()) date = t.getDateOfCreation();
            }
            result += "Время создания: " + date;
        }
        return result;
    }

    /**
     * @return info about command
     */
    @Override
    public String toString() {
        return "info : вывести в стандартный поток вывода информацию о коллекции.";
    }
}
