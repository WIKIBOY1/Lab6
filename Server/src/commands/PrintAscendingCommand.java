package commands;

import collection.Flat;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Команда, которая выводит коллекцию в отсортированом виде, по увелечению hashcode
 */
public class PrintAscendingCommand extends CommandWithoutAdditionalArgument{

    public PrintAscendingCommand(TreeSet<Flat> c) {
        this.c.setFlats(c);
    }

    /**
     * Вывод коллекции в отсортированном виде
     */
    @Override
    public String execute() {
        StringBuilder builder = new StringBuilder();
        Set<Flat> set = c.getFlats().stream().sorted(Comparator.comparing(Flat::hashCode)).collect(Collectors.toSet());
        set.forEach(e -> builder.append(e.toString()).append("\n"));
        return builder.toString();
    }

    @Override
    public String toString() {
        return  "print_ascending : вывести элементы коллекции в порядке возрастания";
    }
}
