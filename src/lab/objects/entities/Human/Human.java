package lab.objects.entities.Human;

import lab.skills.Skill;
import lab.objects.ContainsSkills;
import lab.objects.entities.Creature;
import lab.objects.entities.Gender;
import lab.objects.entities.Mood;

import java.util.ArrayList;

public class Human extends Creature implements ContainsSkills {

    private ArrayList<Skill> skillsList = new ArrayList<Skill>();
    private float power = 10;
    private Gender gender = Gender.AGENDER;
    private Mood mood = Mood.NORMAL;

    public Human(String aName, int aAge, float aPower, Gender aGender){
        super(aName, aAge);
        try {
            setPower(aPower);
        }
        catch (NegativePowerException e){
            System.out.println("Возникло исключение " + e);
        }
        setGender(aGender);
        System.out.println("Создан человек с именем " + getName() + ". Его возраст: " + getAge() + ", гендер: " + getGender().getDescription() + ".");
    }

    public ArrayList<Skill> getSkills(){
        return skillsList;
    }

    public void addSkills(ArrayList<Skill> skills){
        for(Skill skill : skills)
            addSkill(skill);
    }

    public void addSkill(Skill aSkill){
        if (aSkill != null)
            skillsList.add(aSkill);
        System.out.println(getName() + " научился способности: " + aSkill.getName() + ".");
    }

    public void delSkillByReference(Skill skill){
        if(skill != null) {
            System.out.println(getName() + " больше не умеет использовать способность: " + skill.getName() + ".");
            skillsList.remove(skill);
        }
        else
            System.out.println("Некорректная способность.");
    }

    public void delSkillByIndex(int index){
        if((index >= 0)&&(index < skillsList.size())) {
            System.out.println(getName() + " больше не умеет использовать способность: " + skillsList.get(index).getName() + ".");
            skillsList.remove(index);
        }
        else
            System.out.println("Некорректный индекс способности.");
    }

    public void clearSkills(){
        skillsList = new ArrayList<Skill>();
        System.out.println(getName() + " больше не имеет способностей.");
    }

    public void useSkill(int index){
        System.out.println(getName() + " использует способность " + skillsList.get(index).getName() + ".");
        skillsList.get(index).use(this);
    }

    public float getPower(){
        return power;
    }

    public void setPower(float aPower) throws NegativePowerException{
        if(aPower < 0)
            throw new NegativePowerException("Введите неотрицательное значение силы!", aPower);
        else
            power = aPower;
    }

    public Gender getGender(){
        return gender;
    }

    public void setGender(Gender aGender){
        if(aGender != null)
            gender = aGender;
        else
            System.out.println("Некорректное значение гендера.");
    }

    public Mood getMood(){
        return mood;
    }

    public void setMood(Mood aMood){
        if(aMood != null) {
            if(!mood.equals(aMood))
                System.out.println("Установлено новое настроение для " + getName() + ": " + mood.name() + ".");
            else
                System.out.println("Настроение " + getName() + " не изменилось.");
            mood = aMood;
        }
        else
            System.out.println("Некорректное значение настроения.");
    }

    public void changeMood(int grade){
        int changedGrade = mood.getGrade() + grade;
        if ((changedGrade >= 0)&&(changedGrade < Mood.values().length)) {
            mood = Mood.values()[changedGrade];
            if (grade > 0)
                System.out.println("Настроение " + getName() + " повысилось до " + mood.name() + ".");
            else if (grade < 0)
                System.out.println("Настроение " + getName() + " понизилось до " + mood.name() + ".");
            else
                System.out.println("Настроение " + getName() + " не изменилось.");
        }
        else
            System.out.println("Не корректное значение изменения настроения.");
    }

    @Override
    public boolean equals(Object obj){
        if(!super.equals(obj))
            return false;
        Human object = (Human) obj;
        if (!skillsList.equals(object.skillsList))
            return false;
        if (!(Math.abs(power - object.power) < 0.000000001))
            return false;
        if (gender != object.gender)
            return false;
        if (mood != object.mood)
            return false;
        return true;
    }

    @Override
    public String toString(){
        return "Человек с именем " + getName() + ". Его возраст: " + getAge() + ", гендер: " + getGender().getDescription() + ", настроение: " + getMood().name() + ", сила: " + getPower() + ". Количество способностей: " + skillsList.size() + ".";
    }

    @Override
    public int hashCode(){
        final int prime = 17;
        int result = super.hashCode();
        result = result*prime + (int)power*1000000;
        result = result*prime + ((mood == null) ? 0 : mood.hashCode());
        result = result*prime + ((gender == null) ? 0 : gender.hashCode());
        result = result*prime + ((skillsList == null) ? 0 : skillsList.hashCode());
        return result;
    }

}
