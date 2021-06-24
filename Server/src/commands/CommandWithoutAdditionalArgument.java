package commands;

import collection.Flat;
import collection.Flats;

import java.util.TreeSet;

/**
 * Базовый класс-скелет для команд без дополнительного аргумента
 */
public abstract class CommandWithoutAdditionalArgument implements Command{

    public Flats c = new Flats();
    private boolean collectionChanged = false;

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

    public boolean isCollectionChanged() {
        return collectionChanged;
    }

    public void setCollectionChanged(boolean collectionChanged) {
        this.collectionChanged = collectionChanged;
    }
}
