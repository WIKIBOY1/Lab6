package commands;

import collection.Flat;
import collection.Flats;

import java.util.TreeSet;

/**
 * Базовый класс-скелет для команд без дополнительного аргумента
 */
public abstract class CommandWithoutAdditionalArgument implements Command{

    protected Flats c = new Flats();


    public void updateCollection(TreeSet<Flat> collection) {
        c.setFlats(collection);
    }

    /**
     * @param countOfArguments - количество параметров
     * @return true если количество аргументов = 0, иначе false
     */
    @Override
    public boolean correctCountOfArguments(int countOfArguments) {
        return countOfArguments == 0;
    }
}
