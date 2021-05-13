package commands;

import collection.Ticket;

import java.util.LinkedList;
import java.util.TreeSet;

/**
 * Basic interface for all commands without additional argument
 */
public abstract class CommandWithoutAdditionalArgument implements Command{

    protected TreeSet<Ticket> c;


    public void updateCollection(TreeSet<Ticket> collection) {
        c = collection;
    }
    /**
     * @param countOfArguments - count of arguments
     * @return true if count of arguments = 0, else false
     */

    @Override
    public boolean correctCountOfArguments(int countOfArguments) {
        return countOfArguments == 0;
    }
}
