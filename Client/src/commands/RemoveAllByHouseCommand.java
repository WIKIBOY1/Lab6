package commands;

import collection.Flat;
import collection.House;
import exceptions.IdNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class RemoveAllByHouseCommand extends CommandWithoutAdditionalArgument{
    private House house;

    public void setHouse(House house) {
        this.house = house;
    }

    public House getHouse() {
        return house;
    }

    public RemoveAllByHouseCommand(TreeSet<Flat> flats) {
        c.setFlats(flats);
    }

    @Override
    public String execute() {
        if(house == null){
            if (!c.getFlats().removeIf(i -> i.getHouse() == null)) throw new IdNotFoundException();
        }
        else {
            int sizeOfCollectionBefore = c.getFlats().size();
            List<Flat> newList = c.getFlats().stream().filter(t -> t.getHouse() != null).filter(i -> i.getHouse().equals(house))
                    .collect(Collectors.toList());
            c.getFlats().removeAll(newList);
            int sizeOfCollectionAfter = c.getFlats().size();
            if(sizeOfCollectionBefore == sizeOfCollectionAfter) throw new IdNotFoundException();
        }
        return "Билет с house: " + house + " был успешно удалён.";
    }

    @Override
    public String toString() {
        return "remove_all_by_house house : удалить из коллекции все элементы, значение поля house которого эквивалентно";
    }
}
