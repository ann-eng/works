package lab5;
/**
 * Класс, хранящий историю команд.
 * */
public class History {
    /** Массив, хранящий последние 10 запросов. */
    private String[] history = new String[10];

    public History(){ for (int i = 0; i < 10; i++) history[i] = null; }
    /**
     * Добавляет запрос в список.
     * @param s  последний запрос
     * */
    void add(String s){
        if (history[9] != null){
            System.arraycopy(history, 1, history, 0, 9);
        }
        history[9] = s;
    }

    /**
     * Выводит историю запросов в стандартный поток вывода.
     * */
    void getHistory(){
        for(String i : history) {
            if (i != null)  System.out.println(i);
        }
    }
}
