package nl.han.project.skilltree.domain.skill.presentation.dto;

public class UserSkillResponse extends SkillResponse {
    private double grade;

    public void setGrade(double grade) { this.grade = grade; }
    public double getGrade() { return this.grade; }
}
