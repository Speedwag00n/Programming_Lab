package lab.skills;

import lab.objects.ContainsSkills;
import lab.interfaces.Named;

public abstract class Skill implements Named {

    private String name;

    public Skill(){

    }

    public Skill(String aName){
        name = aName;
    }

    public String getName(){
        if (isNamed())
            return name;
        else
            return "Безымянный";
    }

    public void setName(String aName){

    }

    public boolean isNamed(){
        return (name != null);
    }

    public abstract void use(ContainsSkills executor);

    @Override
    public boolean equals(Object obj){
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Skill object = (Skill) obj;
        if (!name.equals(object.name))
            return false;
        return true;
    }

    @Override
    public String toString(){
        return "Способность: " + getName();
    }

    @Override
    public int hashCode(){
        final int prime = 17;
        int result = ((name == null) ? 0 : name.hashCode())*prime;
        return result;
    }

}
