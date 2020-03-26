package lab5;
/**
 * Исключение для проверки команд.
 * */
public class UknownCommandException extends RuntimeException {
/**
 * Конструктор.
 * @param s - имя неизвестной команды
 * */
    public UknownCommandException(String s){ super("Команды " + s + " не существует. Воспользуйтесь командой help."); }
}
