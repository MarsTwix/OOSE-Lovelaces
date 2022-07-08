package nl.han.project.skilltree.domain.skilltree.data;

import nl.han.project.skilltree.domain.skill.data.UserSkill;

import java.util.List;

public class UserSkillTree extends SkillTree {
    private int studentId;
    private List<UserSkill> userSkills;

    public void setStudentId(int studentId) { this.studentId = studentId; }
    public int getStudentId() { return this.studentId; }

    public List<UserSkill> getUserSkills() {
        return userSkills;
    }

    public void setUserSkills(List<UserSkill> userSkills) {
        this.userSkills = userSkills;
    }
}
