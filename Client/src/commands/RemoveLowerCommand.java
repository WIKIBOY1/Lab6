package commands;

import collection.Flat;

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
        //Flat flat;
//        try {
//            flat = c.getFlats().stream().filter(t -> t == getFlat()).collect(Collectors.toList()).get(0);
//        } catch (IndexOutOfBoundsException e) {
//            return "Элемента с таким именем найти не удалось.";
//        }
       // Flat finalFlat = flat;
        if (c.getFlats().removeIf(i -> i.hashCode() < getFlat().hashCode())) return "Сколько-то элементов было удалено";
        return "К сожалению, ничего удалить не удалось";
    }

//    /**
//     * Добавление дополнительного параметра flat name
//     * @param obj - flat name
//     */
//    @Override
//    public void addArgument(String obj) {
//        id = Integer.parseInt(obj);
//    }

    @Override
    public String toString() {
        return "remove_lower <flat id> : удалить из коллекции все элементы, меньшие, чем заданный";
    }
}
