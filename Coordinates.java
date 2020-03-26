package lab5;
/**
 * Класс, хранящий координаты продукта.
 * */
public class Coordinates {

    private Long x; //Поле не может быть null;
    private Integer y; //Поле не может быть null;

    /**
     * Возвращает значение координаты X.
     * @return  значение координаты X
     * */
    public Long getX(){return this.x;}

    /**
     * Возвращает значение координаты Y.
     * @return  значение координаты Y
     * */
    public Integer getY(){return this.y;}

    /**
     * Возвращает значение координат X и Y.
     * @return   строку со значением координат X и Y
     * */
    public String getCoordinates(){return "(" + x.toString() + ", " + y.toString() + ")";}

    /**
     * Устанавливает значение координаты X.
     * @param x   значение координаты X
     * */
    public void setX(Long x) { this.x = x; }

    /**
     * Устанавливает значение координаты Y.
     * @param y  значение координаты Y
     * */
    public void setY(Integer y) { this.y = y; }

    /**
     * Проверяет, что все поля объекта установлены.
     * @return  true, если значение подходит, false - если не подходит
     * */
    public boolean checkAll() { return x != null && y != null; }
}
