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
    /**Поток информации с консоли.*/
    private BufferedReader reader;
    /**Поток информации из файла.*/
    private BufferedInputStream fileReader;
    /**Флаг для определения обрабатываемого потока.*/
    private int streamID;
    /**
     * Сохраняет количество вызовов команды execute_script.
     * */
    private int countOfScripts;
    /**
     * Коллекция {@link AllProducts}, которую необходимо обработать
     * */
    private AllProducts all;

    /**Стандартный конструктор.*/
    public Command(){
        historyList = new History();
        countOfScripts = 0;
        reader = new BufferedReader(new InputStreamReader(System.in));
        all = new AllProducts();
        streamID = 0;
    }

    /**Конструктор с сохранением коллекции.*/
    public Command(AllProducts a){
        historyList = new History();
        countOfScripts = 0;
        reader = new BufferedReader(new InputStreamReader(System.in));
        all = a;
        streamID = 0;
    }

    /**
     * Выводит историю команд.
     * */
    public void history(){
        historyList.getHistory();
    }
    /**
     * Выполняет команду {@code execute_script}.
     * @param s имя файла, в котором хранятся команды
     * */
    public void executeScript( String s) {
        countOfScripts++;
        if (countOfScripts <= 20) {
            File file = new File(s.trim());
            try  {
                fileReader = new BufferedInputStream(new FileInputStream(file));
                StringBuilder command = new StringBuilder();
                int i;
                int lineNumber = 0;
                fileReader.mark(8192);
                do {
                    i = fileReader.read();
                    if (i == 10 || i == -1) {
                            lineNumber++;
                            try {
                                streamID = 1;
                                fileReader.mark(8192);
                                execution(command.toString());
                            }catch (UknownCommandException | UncorrectArgumentException e){
                                System.out.println("Ошибка в строке " + lineNumber + ".");
                                System.out.println( e.getMessage());
                            }
                            fileReader.reset();
                            command.delete(0, command.length());
                    }else if (i != 13) command.append((char) i);
                } while(i != -1);
                fileReader.close();
                streamID = 0;
            } catch (FileNotFoundException e) {
                System.out.println("Файл не найден.");
            } catch (SecurityException e) {
                System.out.println("Нет доступа к файлу.");
            } catch (IOException e) {
                System.out.println("Чтение не удалось.");
            }
        }
    }
    /**
     * Вызывает заданные команды.
     *<p>Проверяет на корректность вводимые команды, выполняет команды с помощью {@link CommandsList}.</p>
     * @param s имя файла, в котором хранятся команды
     * */
    public void execution(String s) throws UknownCommandException, UncorrectArgumentException{
        String[] task = s.split(" ");
        Product p = new Product();
        if (!task[0].equals("")) historyList.add(task[0]);
        switch (task[0]){
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
                    break;
                case "remove_lower":
                    p.create(reader);
                    removeLower(p, all);
                    break;
                case "remove_key":
                    removeKey(all, s.replaceFirst("remove_key", "").trim());
                    break;
                case "execute_script":
                    executeScript(s.replaceFirst("execute_script", "").trim());
                    break;
                case  "count_less_than_manufacturer":
                    Organization o = new Organization();
                    if (streamID == 0) o.create(reader);
                    if (streamID == 1) o.create(readFromFileOrganization());
                    countLessThanManufacturer(o, all);
                    break;
                case "print_field_descending_part_number":
                    printFieldDescendingPartNumber(all, "PartNumber");
                    break;
                case "insert":
                    s = s.replaceFirst("insert", "").trim();
                    if (s.matches("\\d+")){
                        if (Long.decode(s) > 0){
                            if (streamID == 0) p.create(reader);
                            if (streamID == 1) p.create(readFromFileProduct());
                            all.setProduct(Long.decode(s), p);
                            System.out.println("Продукт добавлен в коллекцию.");
                            break;
                        }else throw new UncorrectArgumentException("Индекса " + s + " не существует.");
                    }else  throw  new UncorrectArgumentException("Аргумент команды введен неверно.");
                case "update":
                    s = s.replaceFirst("update", "").trim();
                    if (s.matches("\\d+")){
                        if (all.getProdcts().containsKey(Long.decode(s))){
                            if (streamID == 0) p.create(reader);
                            if (streamID == 1) p.create(readFromFileProduct());
                            all.setProduct(Long.decode(s), p);
                            System.out.println("Продукт " + s + " обновлен.");
                            break;
                        }else throw new UncorrectArgumentException("В коллекции нет элемента с индексом " + s + ".");
                    }else throw new UncorrectArgumentException("Аргумент команды введен неверно.");
                case "replace_if_lowe":
                    s = s.replaceFirst("replace_if_lowe", "").trim();
                    if (s.matches("\\d+")){
                        if (all.getProdcts().containsKey(Long.decode(s))){
                            if (streamID == 0) p.create(reader);
                            if (streamID == 1) p.create(readFromFileProduct());
                            if (all.getProdcts().get(Long.decode(s)).compareTo(p) < 0) {
                                all.setProduct(Long.decode(s), p);
                                System.out.println("Элемент " + s + "заменен.");
                                break;
                            }
                        }else throw  new UncorrectArgumentException("В коллекции нет элемента с индексом " + s + ".");
                    }else throw  new UncorrectArgumentException("Аргумент команды введен неверно.");
                default:
                    throw new UknownCommandException(s);
            }
    }

    /**
     * Читает команды пользователя с консоли.
     * */
    public void work(){
        try {
            String message;
            while (!(message = reader.readLine()).matches("[ ]*exit[ ]*")){
                if (message == null) message = "";
                else message = message.trim();
                try {
                    streamID = 0;
                    execution(message);
                }catch (UknownCommandException | UncorrectArgumentException e) {
                    System.out.println(e.getMessage());
                }
                System.out.print(": ");
            }
            System.out.println("Вход из программы. Завершение работы.");
            reader.close();
        }catch (java.lang.NullPointerException e){
            System.out.println("Завершение работы программы.");
        }catch (IOException e) {
            System.out.println("Завершение работы программы...");
        }
    }
    /**
     * Находит информацию о продукте в файле.
     * @return список полей
     * */
    public String[] readFromFileProduct(){
        String[] options = new String[10];
        try  {
            StringBuilder item = new StringBuilder();
            int i;
            String s;
            do {
                i = fileReader.read();
                if (i == -1 || i == 10) {
                        if (item.toString().matches("[ ]*[Nn]ame[ ]+of[ ]+product[ ]*:[ ]*\\w+[ ]*")){
                            options[0] = item.toString().replaceFirst("[ ]*[Nn]ame[ ]+of[ ]+product[ ]*:[ ]*", "").trim();
                        }else if (item.toString().matches("[ ]*[cC]oordinate[ ]+[Xx][ ]*:[ ]*\\d+[ ]*")){
                            options[1] = item.toString().replaceAll("[CcordinateXx :]+", "");
                        }else if (item.toString().matches("[ ]*[cC]oordinate[ ]+[Yy][ ]*:[ ]*\\d+[ ]*")){
                            options[2] = item.toString().replaceAll("[ :CcordinetaYy]+", "");
                        }else if (item.toString().matches("[ ]*[pP]rice[ ]*:[ ]*\\d*[ ]*")){
                            options[3] = item.toString().replaceAll("[Pprice: ]", "");
                        }else if (item.toString().matches("[ ]*[pP]art[ ]+number[ ]*:[ ]*\\w*[ ]*")){
                            s = item.toString().replaceFirst("[ ]*[pP]art[ ]+number[ ]*:", "").trim();
                            if (s.equals("") || s.length() > 15) options[4] = s;
                        }else if (item.toString().matches("[ ]*[mM]anufacture[ ]+cost[ ]*:[ ]*\\d+[ ]*")){
                            options[5] = item.toString().replaceAll("[ :Mmanufctreos]", "");
                        }else if (item.toString().matches("[ ]*[uU]nit[ ]+of[ ]+measure[ ]*:[ ]*\\w+[ ]*")){
                           s = item.toString().replaceFirst("[ ]*[uU]nit[ ]+of[ ]+measure[ ]*:", "").trim();
                            if (UnitOfMeasure.isCorrect(s)) options[6] = s;
                        }else if (item.toString().matches("[ ]*[Nn]ame[ ]+of[ ]+manufacturer[ ]*:[ ]*\\w+[ ]*")){
                            options[7] = item.toString().replaceFirst("[ ]*[Nn]ame[ ]+of[ ]+manufacturer[ ]*:", "").trim();
                        }else if (item.toString().matches("[ ]*[Aa]nnual[ ]+turnover[ ]*:[ ]*\\d+[ ]*")){
                            options[8] = item.toString().replaceAll("[: Aanultrove]+", "");
                        }else if (item.toString().matches("[ ]*[Tt]ype[ ]*:[ ]*\\w+[ ]*")){
                            s = item.toString().replaceFirst("[ ]*[Tt]ype[ ]*:", "").trim();
                            if (OrganizationType.isCorrect(s)) options[9] = s;
                        } else{
                            fileReader.mark(8192);
                            return options;
                        }
                        item.delete(0, item.length());
                    }else if (i != 13) item.append((char) i);
            } while(i != -1);
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден.");
        } catch (SecurityException e) {
            System.out.println("Нет доступа к файлу.");
        } catch (IOException e) {
            System.out.println("Чтение не удалось.");
        }
        fileReader.mark(8192);
        return options;
    }
    /**
     * Находит информацию об организации в файле.
     * @return список полей
     * */
    public String[] readFromFileOrganization(){
        String[] options = new String[3];
        try  {
            StringBuilder item = new StringBuilder();
            int i;
            String s;
            do {
                i = fileReader.read();
                if (i == -1 || i == 10) {
                    if (item.toString().matches("[ ]*[Nn]ame[ ]+of[ ]+manufacturer[ ]*:[ ]*\\w+[ ]*")) {
                        options[0] = item.toString().replaceFirst("[ ]*[Nn]ame[ ]+of[ ]+manufacturer[ ]*:", "").trim();
                    } else if (item.toString().matches("[ ]*[Aa]nnual[ ]+turnover[ ]*:[ ]*\\d+[ ]*")) {
                        options[1] = item.toString().replaceAll("[: Aanultrove]+", "");
                    } else if (item.toString().matches("[ ]*[Tt]ype[ ]*:[ ]*\\w+[ ]*")) {
                        s = item.toString().replaceFirst("[ ]*[Tt]ype[ ]*:", "").trim();
                        if (OrganizationType.isCorrect(s)) options[2] = s;
                    }   else {
                            fileReader.mark(8192);
                            return options;
                        }
                        item.delete(0, item.length());
                    } else if (i != 13) item.append((char) i);
            } while(i != -1);
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден.");
        } catch (SecurityException e) {
            System.out.println("Нет доступа к файлу.");
        } catch (IOException e) {
            System.out.println("Чтение не удалось.");
        }
        fileReader.mark(8192);
        return options;
    }
}
