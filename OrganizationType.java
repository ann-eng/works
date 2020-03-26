package lab5;
/**
 * Класс-перечисление типов огранизаций.
 * */
public enum OrganizationType {
    PUBLIC,
    TRUST,
    PRIVATE_LIMITED_COMPANY;

    /**
     * Возвращает список значений enum'а.
     * @return список значений enum'а
     * */
    public static String get() { return "(PUBLIC, TRUST, PRIVATE_LIMITED_COMPANY)";    }

    /**
     * Возвращает значение enum'а.
     * @param s  строковое представление enum'а
     * @return единицы измерения
     * */
    public static OrganizationType set(String s){
        switch (s){
            case "PUBLIC":
                return OrganizationType.PUBLIC;
            case "TRUST":
                return OrganizationType.TRUST;
            default:
                return OrganizationType.PRIVATE_LIMITED_COMPANY;
        }
    }
/**
 * Проверяет сотроку на соответствие значению enum'а
 * @param s проверяемая строка
 * @return true, если строка верна, иначе - false
 * */
    public static boolean isCorrect(String s){
        switch (s){
            case "PUBLIC":
                return true;
            case "TRUST":
                return true;
            case "PRIVATE_LIMITED_COMPANY":
                return true;
            default:
                return false;
        }
    }
}
