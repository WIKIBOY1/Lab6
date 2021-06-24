package commands;
import collection.Flat;

import java.util.NoSuchElementException;
import java.util.TreeSet;

/**
 * Команда для добавления нового элемента коллекции
 */
public class AddCommand extends CommandWithFlatWithoutArgument {

    private int searchMaxId(){
        return c.getFlats().last().getId();
    }

    public AddCommand(TreeSet<Flat> c) {
        this.c.setFlats(c);
    }

    /**
     * добавление элемента коллекции
     */
    @Override
    public String execute() {
        try {
            c.getFlats().add(getFlat());
        }catch (NoSuchElementException e){
            c.getFlats().add(getFlat());
        }
        return "Новый элемент был успешно добавлен в коллекцию";
    }

    @Override
    public String toString() {
        return "add : добавить новый элемент в коллекцию";
    }
}
