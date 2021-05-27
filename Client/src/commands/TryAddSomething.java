package commands;

import exceptions.IncorrectInputDataException;

public class TryAddSomething {
    /**
     * Добавляет что-то(квартиру или попытку ввести данные заново)
     */
    public static boolean addSomething(String inputString){
        if (!inputString.isEmpty()) {
            int answer = Integer.parseInt(inputString);
            if (answer == 1) {
                return true;
            } else if (answer == 2) {
                return false;
            } else {
                throw new IncorrectInputDataException("Неверный ответ. Попробуйте ещё раз");
            }
        }else {
            throw new IncorrectInputDataException("Введите что-нибудь");
        }
    }
}
