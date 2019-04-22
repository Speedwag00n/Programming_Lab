package lab.skills;

import lab.objects.ContainsSkills;
import lab.objects.entities.Human.Human;
import lab.objects.items.Item;
import lab.objects.items.rock.Rock;

public class LookAtItem extends Skill {

    private Item item;

    public LookAtItem(Item aItem){
        super("Смотреть на " + aItem.getName());
        setItem(aItem);
    }

    public Item getItem(){
        if (item == null)
            System.out.println("Не указан предмет, на который нужно смотреть.");
        return item;
    }

    public void setItem(Item aItem){
        if (aItem != null)
            item = aItem;
        else
            System.out.println("Некорректный предмет.");
    }

    public void use(ContainsSkills executor){
        if (getItem() != null) {
            if(executor instanceof Human){
                if((item instanceof Rock.Piece)&&(item.getName().equals("Кусок лунита"))) {
                    System.out.println("Посмотрет на " + item.getName() + ", нахлынули воспоминания.");
                    if (Math.random() < 0.5)
                        ((Human) executor).changeMood(-2);
                    else
                        ((Human) executor).changeMood(-1);
                }
                else if(Math.random() < 0.1) {
                    System.out.println("Посмотрев на " + item.getName() + ", нахлынули воспоминания.");
                    ((Human) executor).changeMood(1);
                }
                else if(Math.random() < 0.1) {
                    System.out.println("Посмотрев на " + item.getName() + ", нахлынули воспоминания.");
                    ((Human) executor).changeMood(-1);
                }
                else
                    System.out.println("Посмотрев на " + item.getName() + ", ничего не произошло.");
            }
        }
    }

    @Override
    public boolean equals(Object obj){
        if(!super.equals(obj))
            return false;
        LookAtItem object = (LookAtItem) obj;
        if (!item.equals(object.item))
            return false;
        return true;
    }

    @Override
    public String toString(){
        return "Способность: " + getName() + ". Использующий смотрит на " + item.getName() + ".";
    }

    @Override
    public int hashCode(){
        final int prime = 17;
        int result = super.hashCode();
        result = result*prime + item.hashCode();
        return item.hashCode();
    }

}
