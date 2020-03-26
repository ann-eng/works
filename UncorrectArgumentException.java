package lab5;
/**
 * Исключение для проверки аргументов команды.
 * */
public class UncorrectArgumentException extends RuntimeException{

    /**
     * Конструктор.
     * @param s строка, описывающая аргумент.
     * */
    public UncorrectArgumentException(String s){ super(s);}
}
