package commands;

import collection.Flat;

import java.util.Date;
import java.util.TreeSet;

/**
 * Команда, которая выводит информацию о коллекции (тип, количество элементов, время создания)
 */
public class InfoCommand extends CommandWithoutAdditionalArgument {

    public InfoCommand(TreeSet<Flat> c) {
        this.c.setFlats(c);
    }

    /**
     * Вывод информации о коллекции (тип, количество элементов, время создания)
     */
    public String execute() {
        String result;
        result = "Тип коллекции: " + c.getClass() + "\n";
        result += "Количество элементов: " + c.getFlats().size() + "\n";
        result += "Время создания: " + c.getCreationDateString();
        return result;
    }

    @Override
    public String toString() {
        return "info : вывести в стандартный поток вывода информацию о коллекции.";
    }
}
