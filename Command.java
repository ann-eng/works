package lab5;

import java.io.*;

/**
 * Класс для работы в интерактивном режиме, обработки и выполнения команд.
 * @see History
 * @see CommandsList
 * */
public class Command implements CommandsList{
    /**Сохраняет историю команд.*/
    private History historyList;
    /**
     * Сохраняет количество вызовов команды execute_script.
     * */
    private int countOfScripts;
    /**Стандартный конструктор.*/
    public Command(){
        historyList = new History();
        countOfScripts = 0;
    }
    /**
     * Выводит историю команд.
     * */
    public void history(){ historyList.getHistory(); }
    /**
     * Выполняет команду {@code execute_script}.
     * @param all коллекция {@link AllProducts}, которую необходимо обработать
     * @param s имя файла, в котором хранятся команды
     * */
    public void executeScript(AllProducts all, String s) {
        countOfScripts++;
        if (countOfScripts <= 20) {
            File file = new File(s.trim());
            try (BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file))) {
                StringBuilder command = new StringBuilder();
                int i;
                do {
                    i = reader.read();
                    if (i != -1) {
                        if (i == 10){
                            execution(all, command.toString());
                            command.delete(0, command.length());
                        }else command.append((char) i);
                    }
                } while(i != -1);
            } catch (FileNotFoundException e) {
                System.out.println("Файл не найден.");
            } catch (SecurityException e) {
                System.out.println("Нет доступа к файлу.");
            } catch (IOException e) {
                System.out.println("Ввод-вывод не удался.");
            }
        }
    }
    /**
     * Вызывает заданные команды.
     *<p>Проверяет на корректность вводимые команды, выполняет команды с помощью {@link CommandsList}.</p>
     * @param all коллекция {@link AllProducts}, которую необходимо обработать
     * @param s имя файла, в котором хранятся команды
     * */
    public void execution(AllProducts all, String s) throws UknownCommandException{
        historyList.add(s);
        String[] message = s.trim().split(" ");
        if (message.length == 1){
            switch (message[0]){
                case "help":
                    help();
                    break;
                case "info":
                    info(all);
                    break;
                case "show":
                    show(all);
                    break;
                case "clear":
                    all.clear();
                    break;
                case "save":
                    save(all);
                    break;
                case "history":
                    history();
                    break;
                case "min_by_name":
                    all.findMinName();
                    break;
                case "":
                    System.out.println("Введите команду:");
                    break;
                default:
                    throw new UknownCommandException(s);
            }
        }else if (message.length == 2){
            switch (message[0]){
                case "remove_lower":
                    removeLower(all);
                    break;
                case "remove_key":
                    removeKey(all, message[1]);
                    break;
                case "execute_script":
                    executeScript(all, message[1]);
                    break;
                case  "count_less_than_manufacturer":
                    countLessThanManufacturer(all);
                    break;
                case "print_field_descending_part_number":
                    printFieldDescendingPartNumber(all, "PartNumber");
                    break;
                case "insert":
                    insert(all, message[1]);
                    break;
                case "update":
                    update(all, message[1]);
                    break;
                case "replace_if_lowe":
                    replaceIfLowe(all, message[1]);
                    break;

                default:
                    throw new UknownCommandException(s);
            }
        } else throw new UknownCommandException(s);
    }


}
