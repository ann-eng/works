package lab5;


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
        }catch (NullPointerException e){
            System.out.println("Файл с коллекцией не задан. Коллекция пустая.");
        }finally {
            System.out.println();
            System.out.println("Переход в интерактивный режим.");
            System.out.println("Введите команду:");
            Command command = new Command();
            String task;
            Scanner scanner = new Scanner(System.in);
            while (!(task = scanner.nextLine()).matches("[ ]*exit[ ]*")){
                try {
                    command.execution(allProducts, task);
                }catch (UknownCommandException | UncorrectArgumentException e){
                    System.out.println(e.getMessage());
                }
            }
            scanner.close();
        }

    }
}
