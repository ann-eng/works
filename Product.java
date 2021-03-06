package lab5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.TreeMap;
/**
 * Класс, описывающий продукт.
 * Основной класс коллекции.
 * @see Organization
 * @see Coordinates
 * @see UnitOfMeasure
 * */
public class Product implements Comparable<Product>{
    /**Поле не может быть null, Значение поля должно быть больше 0, Значение это го поля должно быть уникальным, Значение этого поля должно генерироваться автоматически*/
    private Long id;
    private String name; //Поле не может быть null, Строка не может быть пустой;
    private Coordinates coordinates; //Поле не может быть null;
    private java.time.LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически;
    private Long price; //Поле может быть null, Значение поля должно быть больше 0;
    private String partNumber; //Длина строки должна быть не меньше 16, Поле может быть null;
    private int manufactureCost;
    private UnitOfMeasure unitOfMeasure; //Поле не может быть null;
    private Organization manufacturer; //Поле может быть null;
    /**
     * Стандартный конструктор.
     * */
    public Product(){
        this.manufacturer = new Organization();
        this.creationDate = LocalDate.now();
        this.coordinates = new Coordinates();
        this.manufactureCost = 0;
    }

    /**
     * Возвращает Id объекта.
     * @return  значение {@link Product#id}
     * */
    public Long getId() { return id; }

    /**
     * Возвращает имя объекта.
     * @return  значение поля {@link Product#name}
     * */
    public String getName(){ return this.name; }

    /**
     * Возвращает класс координат объекта.
     * @return  значение поля {@link Product#coordinates}
     * */
    public Coordinates getCoordinates() { return coordinates; }

    /**
     * Возвращает дату создания объекта.
     * @return   {@link Product#creationDate}
     * */
    public LocalDate getCreationDate() { return creationDate; }

    /**
     * Возвращает стоимость объекта.
     * @return   {@link Product#price}
     * */
    public Long getPrice() { return price; }

    /**
     * Возвращает инвентарный номер объекта.
     * @return  {@link Product#partNumber}
     * */
    public String getPartNumber() { return partNumber; }

    /**
     * Возвращает стоимость производства объекта.
     * @return  {@link Product#manufactureCost}
     * */
    public int getManufactureCost() { return manufactureCost; }

    /**
     * Возвращает единицы измерения объекта.
     * @return  {@link Product#unitOfMeasure}
     * */
    public UnitOfMeasure getUnitOfMeasure() { return unitOfMeasure; }

    /**
     * Возвращает объект организации-производителя объекта.
     * @return  {@link Product#manufacturer}
     * */
    public Organization getManufacturer() { return manufacturer; }

    /**
     * Генерирует Id объекта.
     * @param map  коллекця, в которую добавляется объект
     * */
    public void createId(TreeMap<Long,Product> map){
        Long i = 1L;
        while (map.containsKey(i)){ i++; }
        this.id = i;
    }

    /**
     * Устанавливает Id объекта.
     * @param i  значение Id
     * */
    public void createId(Long i) { this.id = i; }


    /**
     * Проверяет корректность значения заполняемого поля объекта.
     * @param s  строковое представление значения поля
     * @param i  номер заполняемого поля
     * @return  true, если значение подходит, false - если не подходит
     * */
    public boolean checking(int i, String s){
        switch (i){
            case 0: return !s.equals("");
            case 1: return s.matches("-?\\d+");
            case 2:
                if (s.matches("-?\\d+")){
                    if (Long.parseUnsignedLong(s) < 2147483647) return true;
                    else return false;
                }else return false;
            case 3: return s.matches("\\d*");
            case 4: return  (s.length() > 15) || (s.length() ==0);
            case 5: return s.matches("\\d+");
            case 6: return UnitOfMeasure.isCorrect(s);
            case 7: return manufacturer.checking(i - 7, s);
            case 8: return manufacturer.checking(i - 7, s);
            case 9: return manufacturer.checking(i - 7, s);
            default: return false;
        }
    }

    /**
     * Проверяет, что все поля объекта установлены.
     * @return  true, если значение подходит, false - если не подходит
     * */
    public boolean checkAll(){
        return  name != null && coordinates.checkAll() && unitOfMeasure != null && manufacturer.checkAll();
    }

    /**
     * Устанавливает одно из полей объекта.
     * @param i  номер поля
     * @param option  строковое представление значения поля
     * */
    public void set(int i, String option){
        switch (i){
            case 0:
                if (checking(0,option)) this.name = option.trim();
                break;
            case 1:
                if (checking(1,option)) this.coordinates.setX(Long.decode(option));
                break;
            case 2:
                if (checking(2,option)) this.coordinates.setY(Integer.decode(option));
                break;
            case 3:
                if (checking(3,option)){
                    if (option.equals("")) this.price = null;
                    else  this.price = Long.decode(option);
                }
                break;
            case 4:
                if (checking(4,option)){
                    if (option.equals("")) this.partNumber = null;
                    else  this.partNumber = option;
                }
                break;
            case 5:
                if (checking(5 ,option)) this.manufactureCost = Integer.decode(option);
                break;
            case 6:
                if (checking(6,option)) this.unitOfMeasure = UnitOfMeasure.set(option);
                break;
            case 7:
                if (checking(7,option)) this.manufacturer.set(0, option);
                break;
            case 8:
                if (checking(8,option)) this.manufacturer.set(1, option);
                break;
            case 9:
                if (checking(9,option)) this.manufacturer.set(2, option);
            default:
                //
                break;
        }
    }

    /**
     * Заполняет все поля объекта.
     * @param reader поток ввода информации об продукте
     * */
    public void create(BufferedReader reader){
        try {
            String answ;
            for (int i = 0 ; i < 10; i++){
                boolean check;
                do {
                    getInfoAboutProduct(i);
                    answ = reader.readLine().trim();
                    check = this.checking(i, answ);
                    if (!check) System.out.println("Данные введены неверно. " + getHelp(i) + ".");
                } while (!check);
                this.set(i, answ);
            }
        }catch (IOException e){ System.out.println("Ввод не удался"); }
    }

    /**
     * Возвращает строку, содержащую описание вводимых данных
     * @param i номер поля
     * @return строку-описание
     **/
    private String getHelp(int i) {
        String[] inf = {"Имя не может быть пустой строкой и должно существовать",
                "Координата X вводится в числовом формате и должна существовать",
                "Координата Y вводится в числовом формате и должна существовать",
                "Цена продукта либо не существует, либо является положительным числом и вводится в числовом формате",
                "Инветарный номер продукта может не существовать, код должен быть длиной не менее 16 символов",
                "Цена производства вводится в формате положительного числа",
                "Единицы измерения вводятся в формате " + UnitOfMeasure.get().replaceAll("[()]",""),
                "Название производителя не может быть пустой строкой и должно существовать",
                "Годовой оборот производителя является положительным числом и вводится в числовом формате",
                "Тип организации-производителя вводится в формате: " + OrganizationType.get().replaceAll("[()]", "")};
        return inf[i];
    }

    /**
     * Сравнивает объекты.
     * @param o  объект {@link Product} для сравнения
     * @return результат сравнения в числовом формате
     * */
    @Override
    public int compareTo(Product o) {
        int i = (int) (this.getCoordinates().getY() * this.getCoordinates().getX() * this.getManufactureCost());
        int j = (int) (o.getCoordinates().getY() * o.getCoordinates().getX() * o.getManufactureCost());
        return i - j;
    }

    /**
     * Выводит на консоль просьбу ввести значение поля.
     * @param i  номер поля
     * */
    public void getInfoAboutProduct(int i){
        final String[] questions = {"имя элемента", "координату X элемента", "координату Y элемента", "цену", "инвентарный номер",
                "стоимость изготовления", "единицы измерения " + UnitOfMeasure.get(),
                " название производителя", "годовой оборот производителя", "тип организации" + OrganizationType.get()};
        System.out.print("Введите " + questions[i] + ": ");
    }

    /**
     * Выводит информацию об объекте в стандартный поток вывода.
     * */
    public void getInfo(){
        System.out.println("Id элемента: " + this.id.toString());
        System.out.println("Имя продукта: " + this.name);
        System.out.println("Координаты: " + this.coordinates.getCoordinates());
        System.out.println("Дата создания: " + this.creationDate.toString());
        if (this.price == null) System.out.println("Цена: 0" );
        else System.out.println("Цена: " + this.price.toString());
        if (this.partNumber == null) System.out.println("Инвентарный номер: " );
        else System.out.println("Инвентарный номер: " + this.partNumber);
        System.out.println("Стоимость изготовления: " +  this.manufactureCost);
        System.out.println("Единицы измерения: " + this.unitOfMeasure.name().toLowerCase());
        System.out.println("Производитель: " + this.manufacturer.getType().name() + " " + this.manufacturer.getName());
    }

    /**
     * Заполняет поля продукта данными из файла.
     * @param readFromFile считанные поля
     * */
    public void create(String[] readFromFile) throws UncorrectArgumentException{
        for (int i =0; i < 10; i++) {
            if (readFromFile[i] == null) readFromFile[i] = "";
        }
        for (int i =0; i < 10; i++) set(i, readFromFile[i]);
        if (!checkAll()) throw new UncorrectArgumentException("Не все поля продукта заполнены");
    }
}
