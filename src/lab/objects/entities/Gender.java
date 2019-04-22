package lab.objects.entities;

public enum Gender {

    AGENDER("Без пола"),
    MALE("Мужской пол"),
    FEMALE("Женский пол"),
    STEAMSHIPMAN("Человек-пароход"),
    AIRSHIPMAN("Человек-дирижабль"),
    CYBORG("Киборг"),
    SPIDERMAN("Человек-паук"),
    CONCRETEMIXER("Бетономешалка"),
    IMLIQUID("Я жидкий"),
    IMCUBE("Я куб");
    private String description;

    Gender(String aDescription){
        description = aDescription;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString(){
        return getDescription();
    }

}
