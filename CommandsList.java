package lab5;
import java.io.*;
import java.util.Collection;
/**
 * Класс, хранящий методы для выполнения команд.
 * @see Command
 * */
public interface CommandsList {
    /**
     * Команда {@code help}. Выводит описание всех возможных команд.
     * */
    default void help(){
        for(Information i : Information.values()){
            System.out.println(i.name().toLowerCase()+ i.getInf());
        }
    }
    /**
     * Команда {@code info}. Выводит информацию о рабочей коллекции.
     * @param all класс {@link AllProducts}, хранящий коллекцию
     * */
    default void info(AllProducts all){
        System.out.println("Тип данной коллекции: TreeMap");
        Product p = all.getProdcts().get(1L);
        if (p != null) System.out.println("Дата инициализации: " + p.getCreationDate().toString());
        System.out.println("Количество элементов: " + all.getProdcts().size());
    }
    /**
     *  Команда {@code show}. Выводит информацию об элементах рабочей коллекции.
     * @param all класс {@link AllProducts}, хранящий коллекцию
     * */
    default void show(AllProducts all) {
        try {
            for(Product p : all.getCollection()){
                p.getInfo();
                System.out.println("-----------------");
            }
        }catch (NullPointerException e){
            System.out.println("Коллекция пустая.");
        }

    }
    /**
     *  Команда {@code insert}. Добавляет элемент в рабочую коллекцию.
     * @param all класс {@link AllProducts}, хранящий коллекцию
     * @param s строка, содержащая Id вводимого объекта
     * @throws UncorrectArgumentException , если введенные аргументы неверны
     * */
    default void insert(AllProducts all, String s) throws UncorrectArgumentException{
        if (s.matches("\\d+")){
            if (Long.decode(s) > 0){
                Product p = new Product();
                p.create();
                all.setProduct(Long.decode(s), p);
                System.out.println("Продукт добавлен в коллекцию.");
            }else throw new UncorrectArgumentException("Индекса " + s + " не существует.");
        }else  throw  new UncorrectArgumentException("Аргумент команды введен неверно.");
    }
    /**
     *  Команда {@code update}. Обновляет элемент в рабочей коллекции.
     * @param all класс {@link AllProducts}, хранящий коллекцию
     * @param s строка, содержащая Id обновляемого объекта
     * @throws UncorrectArgumentException , если введенные аргументы неверны
     * */
    default void update(AllProducts all, String s) throws UncorrectArgumentException{
        if (s.matches("\\d+")){
            if (all.getProdcts().containsKey(Long.decode(s))){
                Product p = new Product();
                p.create();
                all.setProduct(Long.decode(s), p);
                System.out.println("Продукт " + s + " обновлен.");
            }else throw new UncorrectArgumentException("В коллекции нет элемента с индексом " + s + ".");
        }else throw new UncorrectArgumentException("Аргумент команды введен неверно.");
    }
    /**
     * Команда {@code remowe_key}. Удаляет элемент в рабочей коллекции.
     * @param all класс {@link AllProducts}, хранящий коллекцию
     * @param s строка, содержащая Id удаляемого объекта
     * @throws UncorrectArgumentException , если введенные аргументы неверны
     * */
    default void removeKey(AllProducts all, String s) throws UncorrectArgumentException{
        if (s.trim().matches("\\d+")){
            if (all.getProdcts().containsKey(Long.decode(s))){
                all.remove(Long.decode(s));
                System.out.println("Продукт " + s + " удален.");
            }else throw new UncorrectArgumentException("В коллекции нет элемента с индексом " + s + ".");
        }else throw new UncorrectArgumentException("Аргумент команды введен неверно.");

    }
    /**
     * Команда {@code save}. Сохраняет коллекцию в файл в формате json.
     * @param all рабочая коллекция
     * */
    default void save(AllProducts all) {
        File file = new File("newfile.json");
        try(FileOutputStream fis = new FileOutputStream(file)) {
            byte[] s = "AllProducts: [\n".getBytes();
            fis.write(s);
            if (all.getCollection().size() > 0){
                for (Product p : all.getCollection()){
                    StringBuilder option = new StringBuilder(" {\n");
                    option.append("  \"name\": \"").append(p.getName()).append("\",\n");
                    option.append("  \"coordinates\": {\n");
                    option.append("   \"x\": ").append(p.getCoordinates().getX()).append(",\n");
                    option.append("   \"y\": ").append(p.getCoordinates().getY()).append(",\n");
                    option.append("  },\n");
                    option.append("  \"price\": ").append(p.getPrice().toString()).append(",\n");
                    option.append("  \"partNumber\": \"").append(p.getPartNumber()).append("\",\n");
                    option.append("  \"manufactureCost\": ").append(p.getManufactureCost()).append(",\n");
                    option.append("  \"unitOfMeasure\": \"").append(p.getUnitOfMeasure().name()).append("\",\n");
                    option.append("  \"manufacturer\": {\n");
                    option.append("   \"name\": \"").append(p.getManufacturer().getName()).append("\",\n");
                    option.append("   \"annualTurnover\": ").append(p.getManufacturer().getAnnualTurnover()).append(",\n");
                    option.append("   \"type\": \"").append(p.getManufacturer().getType().name()).append("\",\n");
                    option.append("  }\n");
                    option.append(" },\n");
                    fis.write(option.toString().getBytes());
                }
            }
            fis.write("\n]".getBytes());
        }catch (FileNotFoundException e){
            System.out.println("Файл не найден.");
        }catch (IOException e){
            System.out.println("Запись не удалась.");
        }
        System.out.println("Коллекция сохранена в файл newfile.json.");
    }

    /**
     *  Команда {@code remowe_lower}. Удаляет все элементы коллекции, меньшие, чем заданный
     * @param all обрабатываемая коллекция
     * */
    default void removeLower(AllProducts all) {
       Product p = new Product();
       p.create();
       Collection<Product> collection = all.getProdcts().values();
       if (collection.size() > 0){
           for (Product o : collection){
               if (o.compareTo(p) < 0){
                   all.remove(o.getId());
               }
           }
       }
        System.out.println("Все элементы, ниже заданного, удалены.");
    }

    /**
     *  Команда {@code replace_if_lowe}. Заменяет элемент коллекции, на заданный, если он меньше
     * @param all обрабатываемая коллекция
     * @param s Id заменяемого элемента
     * @throws UncorrectArgumentException , если введенные аргументы неверны
     * */
    default void replaceIfLowe(AllProducts all, String s) throws UncorrectArgumentException{
        if (s.matches("\\d+")){
            if (all.getProdcts().containsKey(Long.decode(s))){
                Product p = new Product();
                p.create();
                if (all.getProdcts().get(Long.decode(s)).compareTo(p) < 0) {
                    all.setProduct(Long.decode(s), p);
                    System.out.println("Элемент заменен.");
                }
            }else throw  new UncorrectArgumentException("В коллекции нет элемента с индексом " + s + ".");
        }else throw  new UncorrectArgumentException("Аргумент команды введен неверно.");

    }

    /**
     *  Команда {@code count_less_than_manufacturer}. Выводит количество элементов, значение организации которых меньше заданного
     * @param all обрабатываемая коллекция
     * */
    default void countLessThanManufacturer(AllProducts all) {
        Organization o =new Organization();
        o.create();
        if (all.getCollection().size() == 0) System.out.println("Количество элементов, меньше заданного равно 0.");
        else {
            int i = 0;
            for (Product p : all.getCollection())  if (p.getManufacturer().compareTo(o) < 0) i++;
            System.out.println("Количество элементов, меньше заданного равно " + i + ".");
        }
    }

    /**
     * Команда {@code print_field_descending_PartNumber}. Выводит значения инвентарных номеров элементов коллекции в порядке убывания.
     * @param all обрабатываемая коллекция
     * @param s параметр
     * */
    default void printFieldDescendingPartNumber(AllProducts all, String s) {
        if (all.getCollection().size() == 0) System.out.println("Коллекция пустая.");
        else {
            System.out.println("Инвентарные имена продуктов в порядке убывания:");
            Product[] sortedProducts = (Product[]) all.getCollection().toArray();
            all.PartNumberSort(sortedProducts, 0, sortedProducts.length - 1);
            for (int i = sortedProducts.length - 1; i >= 0; i--) System.out.println(sortedProducts[i].getPartNumber());
        }
    }
}
