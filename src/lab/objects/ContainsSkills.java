package lab.objects;

import lab.skills.Skill;

import java.util.ArrayList;

public interface ContainsSkills {

    ArrayList<Skill> getSkills();
    void addSkills(ArrayList<Skill> skills);
    void addSkill(Skill skill);
    void delSkillByReference(Skill skill);
    void delSkillByIndex(int index);
    void clearSkills();
    void useSkill(int index);

}
