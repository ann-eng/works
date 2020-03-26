package lab5;
/**
 * Класс-перечисление единиц измерения продуктов.
 * @see Product
 * */
public enum UnitOfMeasure {
    SQUARE_METERS,
    PCS,
    LITERS,
    MILLILITERS,
    GRAMS;
    /**
     * Возвращает список значений enum'а.
     * @return список значений enum'а
     * */
    public static String get() {
        return "(SQUARE_METERS, PCS, LITERS, MILLILITERS, GRAMS)";
    }
    /**
     * Возвращает значение enum'а.
     * @param s - строковое представление enum'а
     * @return единицы измерения
     * */
    public static UnitOfMeasure set(String s){
        switch (s){
            case "SQUARE_METERS":
                return UnitOfMeasure.SQUARE_METERS;
            case "PCS":
                return UnitOfMeasure.PCS;
            case "LITERS":
                return UnitOfMeasure.LITERS;
            case "MILLILITERS":
                return UnitOfMeasure.MILLILITERS;
            default:
                return UnitOfMeasure.GRAMS;
        }
    }

    /**
     * Проверяет сотроку на соответствие значению enum'а
     * @param s проверяемая строка
     * @return true, если строка верна, иначе - false
     * */
    public static boolean isCorrect(String s){
        switch (s){
            case "SQUARE_METERS":
                return true;
            case "PCS":
                return true;
            case "LITERS":
                return true;
            case "MILLILITERS":
                return true;
            case "GRAMS":
                return true;
            default:
                return false;
        }
    }
}
