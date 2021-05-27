package commands;

import collection.Flat;
import exceptions.IdNotFoundException;

import java.util.TreeSet;

/**
 * Команда, которая удаляет элемент коллекции с соответствующим id
 */
public class RemoveByIdCommand extends CommandWithAdditionalArgument{

    private int ID;

    public RemoveByIdCommand(TreeSet<Flat> c) {
        this.c.setFlats(c);
    }

    /**
     * Удаление квартиры с введёным id
     */
    @Override
    public String execute() {
        if (!c.getFlats().removeIf(i -> i.getId() == ID)) throw new IdNotFoundException();
        return "Билет с id: " + ID + " был успешно удалён.";
    }

    /**
     * Добавление дополнительного параметра flat id
     * @param obj - flat id
     */
    @Override
    public void addArgument(String obj) {
        ID = Integer.parseInt(obj);
    }

    @Override
    public String toString() {
        return "remove_by_id <id> : удалить элемент из коллекции по его id";
    }
}
