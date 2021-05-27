package commands;

import collection.Flat;

public abstract class CommandWithFlatWithoutArgument extends CommandWithoutAdditionalArgument{
    Flat flat;

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
