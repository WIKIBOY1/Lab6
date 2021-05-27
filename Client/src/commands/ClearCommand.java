package commands;

import collection.Flat;

import java.util.TreeSet;

/**
 * Команда для очищения коллекции
 */
public class ClearCommand extends CommandWithoutAdditionalArgument{

    public ClearCommand(TreeSet<Flat> c) {
        this.c.setFlats(c);
    }

    /**
     * Очищение коллекции
     */
    @Override
    public String execute() {
        return c.getFlats().removeAll(c.getFlats()) ? "Коллекция была очищена." : "Коллекция итак была пуста.";
    }

    @Override
    public String toString() {
        return "clear : очистить коллекцию";
    }
}
