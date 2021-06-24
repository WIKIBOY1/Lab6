package commands;

import collection.Flat;

import java.util.HashSet;
import java.util.TreeSet;

/**
 * Команда, которая удаляет все элементы коллекции меньше заданного
 */
public class RemoveLowerCommand extends CommandWithFlatWithoutArgument {

    public RemoveLowerCommand(TreeSet<Flat> c) {this.c.setFlats(c);}

    /**
     * Удаление всех элементов коллекции меньше заданного
     */
    @Override
    public String execute() {
        HashSet<Integer> hashSet = new HashSet<>();
        for(Flat flat : c.getFlats()) {
            getHashOfFlats().add(flat.hashCode());
        }
        if (c.getFlats().removeIf(i -> i.hashCode() < getFlat().hashCode())) {
            for(Flat flat : c.getFlats()) {
                hashSet.add(flat.hashCode());
            }
            getHashOfFlats().removeAll(hashSet);
            return "Сколько-то элементов было удалено";
        }
        return "К сожалению, ничего удалить не удалось";
    }

    @Override
    public String toString() {
        return "remove_lower <flat id> : удалить из коллекции все элементы, меньшие, чем заданный";
    }
}
