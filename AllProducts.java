package lab5;

import java.util.*;

/**
 * Класс для хранения и работы с коллекцией объектов Product.
 *
 * @see Product
 * @see Organization
 */
public class AllProducts {
    /**Основная коллекция. */
    private TreeMap<Long,Product> prodcts;
    /**Дополнительная коллекция объектов класса Organization.*/
    private LinkedHashSet<Organization> organizations;
    /**Дополнительная коллекция для сортировки элементов. */
    private ArrayList<Product> collection;
    /**Стандартный конструктор.*/
    public AllProducts() {
        prodcts = new TreeMap<>(Long::compareTo);
        organizations = new LinkedHashSet<>();
        collection = new ArrayList<>();
    }
    /**Метод для очистки коллекции.*/
    public void clear() {
        prodcts.clear();
        organizations.clear();
        collection.clear();
    }

    /**
     * Удаляет элемент из коллекции.
     * @param i Id удаляемого элемента
     * */
    public void remove(Long i) {
        Product p = prodcts.get(i);
        collection.remove(p);
        prodcts.remove(i);
    }

    /**
     * Возвращает основную коллекцию.
     * @return коллекцию элементов
     * */
    public TreeMap<Long,Product> getProdcts(){return prodcts;}
    /**
     * Возвращает перебираемую коллекцию.
     * @return  перебираемую коллекцию
     * */
    public ArrayList<Product> getCollection(){return collection;}

    /**
     * Добавляет объект в коллекцию, генерируя Id элемента и Id организации.
     * @param p загружаемый объект.
     * */
    public void setProdcts(Product p){
        p.createId(this.prodcts);
        p.getManufacturer().createId(this.organizations);
        prodcts.put(p.getId(), p);
        collection.add(p);
        organizations.add(p.getManufacturer());
    }

    /**
     * Добавление элемента по его Id.
     * @param i Id новое элемента
     * @param p новый элемент
     * */
    public void setProduct(Long i, Product p){
        if (prodcts.containsKey(i)) {
            collection.set(i.intValue() - 1, p);
        }else collection.add(p);
        p.createId(i);
        prodcts.put(i, p);
        if (organizations.add(p.getManufacturer())) {
            p.getManufacturer().createId(organizations);
        } else {
            for (Organization o: organizations){
                if (o.equals(p.getManufacturer())) p.getManufacturer().createId(o.getId());
            }
        }
    }

    /**
     * Возвращает элемент коллекции с минимальным именем.
     * */
    public  void findMinName(){
        Long l = 0L;
        try {
            Product minNameProduct = collection.get(0);
            for (Product p : collection){
                if (minNameProduct.getName().length() > p.getName().length()){
                    minNameProduct = p;
                    l = p.getId();
                }
            }
            minNameProduct.createId(l);
            minNameProduct.getInfo();
        }catch (NullPointerException e){
            System.out.println("Коллекция пустая.");
        }
    }

    /**
     * Метод для сортировки коллекции по инвентарному номеру методом быстрой сортировки.
     * @param array сортируемый массив
     * @param high верхний индекс
     * @param low нижний индекс
     * */
    public void PartNumberSort(Product[] array, int low, int high) {
        if (array.length == 0) return;
        if (low >= high) return;
        int middle = low + (high - low) / 2;
        String opora = array[middle].getPartNumber();

        int i = low, j = high;
        while (i <= j) {
            while (array[i].getPartNumber().compareTo(opora) < 0) { i++; }
            while (array[j].getPartNumber().compareTo(opora) > 0) { j--; }
            if (i <= j) {
                Product temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                i++;
                j--;
            }
        }
        if (low < j) PartNumberSort(array, low, j);
        if (high > i) PartNumberSort(array, i, high);
    }

}
