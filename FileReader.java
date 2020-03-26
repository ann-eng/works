package lab5;

import java.io.*;
import java.util.ArrayList;

/**
 * Класс для обработки входящего файла с начальной коллекцией.
 * */
public class FileReader {

    /** Строка с содержимым файла */
    private StringBuilder file;
    /** Найденные в файле продукты */
    private ArrayList<Product> products;
    /**
     * Стандартный конструктор.
     * */
    public FileReader(){
        file = new StringBuilder();
        products = new ArrayList<>();
    }
    /**
     * Возвращает список найденных продуктов.
     * @return  список найденных продуктов
     * */
    public ArrayList<Product> getArrayOfProducts(){return this.products;}

    /**
     * Считывает содержимое файла.
     * @param s  имя файла
     * */
    public void read(String s){
        try(BufferedInputStream f = new BufferedInputStream( new FileInputStream(s))){
            int i;
            do {
                i = f.read();
                if (i != -1) file.append((char) i);
            } while(i != -1);
        }catch (FileNotFoundException e){
            System.out.println("Файл не найден.");
            System.out.println("Коллекция пустая.");
        }catch (SecurityException e){
            System.out.println("Нет доступа к файлу.");
            System.out.println("Коллекция пустая.");
        } catch(IOException e){
            System.out.println("Ввод-вывод не удался.");
            System.out.println("Коллекция пустая.");
        }
        if (!file.toString().equals("")) this.setProducts();
    }

    /**
     * Добавляет продукты в список.
     * */
    public void setProducts(){
        String[] strings = this.file.toString().split("\n");
        ArrayList<String> element = new ArrayList<>();
        int countofelements = 1;
        for(int i = 0; i < strings.length - 1; i++){
            if ((strings[i].matches("[ ]*\\}[,]?[ ]*") && strings[i+1].matches("[ ]*\\{[ ]*")) ||
                    (strings[i].matches("[ ]*\\}[,]?[ ]*") && strings[i+1].matches("[ ]*\\][ ]*"))){
               if (!this.decode(element)) System.out.println("Данные для элемента " + countofelements + " некорректны.");
                element.clear();
                countofelements ++;
            }else {
                element.add(strings[i]);
            }
        }
    }

    /**
     * Находит в содержимом файла проукты.
     * @param list  часть содержимого файла, в котором может быть продукт
     * @return  true, если текст корректно декодирован, false - если обнаружены не все поля объекта
     * */
    public boolean decode(ArrayList<String> list){
        Product prod = new Product();
        String item;
        int manName = -2;
        int prodName = -1;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).matches("[ ]*\"coordinates\"[ ]*:[ ]*\\{[ ]*") && i < list.size() - 2) {
                    for (int j = i + 1; j < i + 3; j++) {
                        if (list.get(j).matches("[ ]*\"x\"[ ]*:[ ]*\\d+[ ]*[,]?[ ]*")) {
                            item = list.get(j).replaceAll("[\"x:,]+", "");
                            prod.setCoordinateX(item.trim());
                        }
                        if (list.get(j).matches("[ ]*\"y\"[ ]*:[ ]*\\d+[ ]*[,]?[ ]*")) {
                            item = list.get(j).replaceAll("[\"y:,]+", "");
                            prod.setCoordinateY(item.trim());
                        }
                    }
                } else if (list.get(i).matches("[ ]*\"manufacturer\"[ ]*:[ ]*\\{[ ]*") && i < list.size() - 3) {
                    for (int j = i + 1; j < i + 4; j++) {
                        if (list.get(j).matches("[ ]*\"name\"[ ]*:[ ]*\"\\w+\"[ ]*[,]?[ ]*") && j != prodName) {
                            item = list.get(j).replaceAll("[\":,]+", "").replaceFirst("name", "");
                            prod.getManufacturer().setName(item.trim());
                            manName = j;
                        }
                        if (list.get(j).matches("[ ]*\"annualTurnover\"[ ]*:[ ]*\\d+[ ]*[,]?[ ]*")) {
                            item = list.get(j).replaceAll("[\"anulTrove:,]+", "");
                            prod.getManufacturer().setAnnualTurnover(item.trim());
                        }
                        if (list.get(j).matches("[ ]*\"type\"[ ]*:[ ]*\"\\w+\"[ ]*[,]?[ ]*")) {
                            item = list.get(j).replaceAll("[\"type:,]+", "").trim();
                            if (OrganizationType.isCorrect(item)) {
                                prod.getManufacturer().setType(item);
                            }
                        }
                    }
                } else if (list.get(i).matches("[ ]*\"price\"[ ]*:[ ]*\\d*[ ]*[,]?[ ]*")) {
                    item = list.get(i).replaceAll("[\"price:,]", "").trim();
                    prod.setPrice(item);
                } else if (list.get(i).matches("[ ]*\"manufactureCost\"[ ]*:[ ]*\\d+[ ]*[,]?[ ]*")) {
                    item = list.get(i).replaceAll("[\"manufctreCos:,]", "").trim();
                    prod.setManufactureCost(item);
                } else if (list.get(i).matches("[ ]*\"unitOfMeasure\"[ ]*:[ ]*\"\\w+\"[ ]*[,]?[ ]*")) {
                    item = list.get(i).replaceAll("[\"unitOfeasr:,]", "").replaceFirst("M", "").trim();
                    if (UnitOfMeasure.isCorrect(item)) prod.setUnitOfMeasure(item);
                } else if (list.get(i).matches("[ ]*\"partNumber\"[ ]*:[ ]*\"\\w{16,}\"[ ]*[,]?[ ]*")) {
                    item = list.get(i).replaceAll("[\":,]", "").replaceFirst("partNumber", "").trim();
                    prod.setPartNumber(item);
                } else if (list.get(i).matches("[ ]*\"name\"[ ]*:[ ]*\"\\w+\"[ ]*[,]?[ ]*") & i != manName) {
                    item = list.get(i).replaceAll("[\":,]", "").replaceFirst("name", "").trim();
                    prod.setName(item);
                }
            }
            if (prod.checkAll()) {
                this.products.add(prod);
                return true;
            } else return false;
    }
}
