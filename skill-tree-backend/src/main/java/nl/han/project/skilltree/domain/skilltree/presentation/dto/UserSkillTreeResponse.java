package nl.han.project.skilltree.domain.skilltree.presentation.dto;

import nl.han.project.skilltree.domain.skill.presentation.dto.UserSkillResponse;

import java.util.List;

public class UserSkillTreeResponse extends SkillTreeResponse {
    private int studentId;
    private List<UserSkillResponse> skills;

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public List<UserSkillResponse> getUserSkills() {
        return skills;
    }

    public void setUserSkills(List<UserSkillResponse> userSkills) {
        this.skills = userSkills;
    }
}
