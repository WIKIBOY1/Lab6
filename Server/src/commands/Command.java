package commands;

import java.io.Serializable;

/**
 * Базовый интерфейс для каждой команды
 */
public interface Command extends Serializable {
    /**
     * Метод для реализации команды
     */
    String execute();

    /**
     * @param countOfArguments - количество аргументов
     * @return зависит от количества параметров
     */
    boolean correctCountOfArguments(int countOfArguments);
}

