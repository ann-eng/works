package lab5;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashSet;

/**
 * Класс, описывающий оргазнизации-производители продуктов.
 * @see Product
 * */
public class Organization implements Comparable<Organization>{
    /**Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически*/
    private long id;
    private String name; //Поле не может быть null, Строка не может быть пустой;
    private Long annualTurnover; //Поле не может быть null, Значение поля должно быть больше 0;
    private OrganizationType type; //Поле не может быть null;

    /**
     * Возвращает значение {@link Organization#id} производителя.
     * @return значение поля {@link Organization#id}
     * */
    public Long getId() { return this.id; }

    /**
     *  Возвращает название производителя.
     * @return значение поля {@link Organization#name}
     * */
    public String getName() { return name; }

    /**
     *  Возвращает годовой оборот производителя.
     * @return значение поля {@link Organization#annualTurnover}
     * */
    public Long getAnnualTurnover() { return annualTurnover; }

    /**
     * Возвращает тип организации-производителя.
     * @return значение поля {@link Organization#type}
     * */
    public OrganizationType getType() { return type; }

    /**
     * Устанавливает Id организации.
     * <p>Id устанавливается в соответствии со списком, в который добавляется организация.</p>
     * @param list  список уже существующих организаций
     * @see AllProducts#setProdcts(Product)
     * */
    public void createId(LinkedHashSet<Organization> list){ this.id = list.size() + 1; }

    /**
     * Устанавливает Id организации.
     * <p>Id устанавливается в соответствии со списком, в который добавляется организация.</p>
     * @param id  id организации
     * */
    public void createId(Long id) { this.id = id;}

    /**
     * Проверяет корректность значения заполняемого поля объекта.
     * @param s  строковое представление значения поля
     * @param i  номер заполняемого поля
     * @return  true, если значение подходит, false - если не подходит
     * */
    public boolean checking(int i, String s) {
        switch (i){
            case 0: return !s.matches("");
            case 1: return !s.matches("0") && s.matches("\\d+");
            case 2: return OrganizationType.isCorrect(s);
            default: return false;
        }
    }

    /**
     * Устанавливает одно из полей объекта.
     * @param i  номер поля
     * @param s  строковое представление значения поля
     * */
    public void set(int i, String s){
        switch (i){
            case 0:
                this.name = s;
                break;
            case 1:
                this.annualTurnover = Long.decode(s);
                break;
            case 2:
                this.type = OrganizationType.set(s);
                break;
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
            for (int i = 0 ; i < 3; i++){
                boolean check;
                do {
                    this.getInfoAboutOrganization(i);
                    answ = reader.readLine().trim();
                    check = checking(i, answ);
                } while (!check);
                this.set(i, answ);
            }
        }catch (IOException e){ System.out.println("Ввод не удался"); }
    }



    /**
     * Проверяет, что все поля объекта установлены.
     * @return  true, если значение подходит, false - если не подходит
     * */
    public boolean checkAll() { return  name != null && annualTurnover != null && type != null; }

    /**
     * Сравнивает объекты для сортировки.
     * @param o объект {@link Organization} для сравнения
     * @return результат сравнения в числовом формате
     * */
    @Override
    public int compareTo(Organization o) {
        return (int) (this.annualTurnover - o.getAnnualTurnover());
    }



    /**
     * Проверяет объекты на равенство.
     * @return  true, если значение подходит, false - если не подходит
     * */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return name.equals(that.name) && annualTurnover.equals(that.annualTurnover) &&
                type == that.type;
    }

    /**
     * Выводит на консоль просьбу ввести значение поля.
     * @param i  номер поля
     * */
    public void getInfoAboutOrganization(int i){
        final String[] questions = {"имя элемента",  "годовой оборот", "тип " + OrganizationType.get()};
        System.out.print("Введите " + questions[i] + ": ");
    }
    /**
     * Заполняет поля продукта данными из файла.
     * @param readFromFile считанные поля
     * */
    public void create(String[] readFromFile) throws UncorrectArgumentException{
        for (int i =0; i < 3; i++) set(i, readFromFile[i]);
        if (!checkAll()) throw new UncorrectArgumentException("Не все поля продукта заполнены");
    }

}
