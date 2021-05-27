package commands;

import collection.Flat;
import exceptions.IdNotFoundException;
import java.util.TreeSet;

/**
 * Команда, которая обновляет квартиру по id
 */
public class UpdateCommand extends CommandWithAdditionalArgument{
    private int ID;

    Flat flat;

    public void setFlat(Flat flat) {
        this.flat = flat;
    }

    public Flat getFlat() {
        return flat;
    }

    public UpdateCommand(TreeSet<Flat> c) {
        this.c.setFlats(c);
    }

    /**
     * Обновление квартиры
     */
    @Override
    public String execute() {
        flat.setId(ID);
        boolean check = false;
        TreeSet<Flat> newFlats = new TreeSet<>(c.getFlats());
        for (Flat t : newFlats) {
            if (t.getId() == ID) {
                c.getFlats().remove(t);
                c.getFlats().add(flat);
                check = true;
            }
        }
        if (check) throw new IdNotFoundException();
        return "Элемент с заданным id был успешно обновлён";
    }

    /**
     * Добавление дополнительного параметра flat id {@link UpdateCommand#ID}
     * @param obj - flat id
     */
    @Override
    public void addArgument(String obj) {
        ID = Integer.parseInt(obj);
    }


    @Override
    public String toString() {
        return "update <id> : обновить значение элемента коллекции, id которого равен заданному";
    }
}
