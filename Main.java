package lab5;

import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        AllProducts allProducts = new AllProducts();
        FileReader fileReader = new FileReader();
        String envar = System.getenv("E");
        try {
            fileReader.read(envar);
            for (Product p : fileReader.getArrayOfProducts()){
                allProducts.setProdcts(p);
            }
            System.out.println("Коллекция загружена.");
        }catch (NullPointerException | IndexOutOfBoundsException e){
            System.out.println("Файл с коллекцией не задан. Коллекция пустая.");
            System.out.println("Чтобы задать коллекцию, при запуске передайте переменную окружения E.");
        }finally {
            System.out.println();
            System.out.println("Переход в интерактивный режим.");
            System.out.print("Введите команду: ");
            Command command = new Command(allProducts);
            command.work();
        }
    }
}
