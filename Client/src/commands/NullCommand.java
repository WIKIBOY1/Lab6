package commands;

public class NullCommand extends CommandWithoutAdditionalArgument{
    @Override
    public String execute() {
        return null;
    }
}
