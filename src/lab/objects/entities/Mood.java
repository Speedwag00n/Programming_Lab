package lab.objects.entities;

public enum Mood {

    PANIC(0),
    DEPRESSED(1),
    SAD(2),
    NORMAL(3),
    HAPPIENESS(4),
    RAPTURE(5),
    EUPHORIA(6);

    private int index;

    private Mood(int aIndex){
        index = aIndex;
    }

    public int getGrade(){
        return index;
    }

    @Override
    public String toString(){
        return this.name();
    }

}
