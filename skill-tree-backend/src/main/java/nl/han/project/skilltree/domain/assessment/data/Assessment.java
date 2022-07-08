package nl.han.project.skilltree.domain.assessment.data;

public class Assessment {
    int id;
    double grade;
    String description;
    int skillId;
    int userId;

    public Assessment(int id, double grade, String description, int skillId, int userId) {
        this.id = id;
        this.grade = grade;
        this.description = description;
        this.skillId = skillId;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
