package commands;

import collection.Flat;

import java.util.HashSet;

public abstract class CommandWithFlatWithoutArgument extends CommandWithoutAdditionalArgument{
    private Flat flat;

    private HashSet<Integer> hashOfFlats = new HashSet<>();

    public HashSet<Integer> getHashOfFlats() {
        return hashOfFlats;
    }

    public void setHashOfFlats(HashSet<Integer> hashOfFlats) {
        this.hashOfFlats = hashOfFlats;
    }

    public void setFlat(Flat flat) {
        this.flat = flat;
    }

    public Flat getFlat() {
        return flat;
    }

    @Override
    public String execute() {
        return null;
    }
}
