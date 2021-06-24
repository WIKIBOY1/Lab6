package commands;

import collection.Flat;
import sun.reflect.generics.tree.Tree;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Команда, которая выводит все элементы, содержащие подстроку в имени
 */
public class FilterContainsName extends CommandWithAdditionalArgument{
    String name;

    public FilterContainsName(TreeSet<Flat> c){
        this.c.setFlats(c);
    }

    /**
     * Вывод всех элементов, содержащих подстроку в имени
     */
    @Override
    public String execute() {
        StringBuilder builder = new StringBuilder();
        try {
            Set<Flat> flats =  c.getFlats().stream().filter(x -> x.getName().contains(name)).collect(Collectors.toSet());
            flats.forEach(e -> builder.append(e.toString()).append("\n"));
            return builder.toString();
        } catch (IndexOutOfBoundsException e) {
            return "Элемента с таким именем найти не удалось.";
        }
    }

    /**
     * Добавление дополнительного параметра flat name
     * @param obj - flat name
     */
    @Override
    public void addArgument(String obj) {
        name = obj;
    }

    @Override
    public String toString() {
        return "filter_contains_name name : вывести элементы, значение поля name которых содержит заданную подстроку";
    }
}
