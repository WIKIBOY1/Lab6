package commands;

/**
 * Базовый класс-скелет для команд c дополнительными аргументами
 */
public abstract class CommandWithAdditionalArgument extends CommandWithoutAdditionalArgument{

    /**
     * Добавить дополнительный аргумент
     * @param obj - дополнительный аргумент
     */
    public void addArgument(String obj) {};

    /**
     * @param countOfArguments - количество аргументов
     * @return true если количество аргументов 1 = 1, иначе false
     */
    @Override
    public boolean correctCountOfArguments(int countOfArguments) {
        return countOfArguments == 1;
    }
}
