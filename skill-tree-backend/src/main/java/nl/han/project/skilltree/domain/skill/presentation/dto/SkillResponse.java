package nl.han.project.skilltree.domain.skill.presentation.dto;

public class SkillResponse {

    private int id;
    private String name;
    private String description;
    private String assessmentCriteria;
    private boolean evidenceRequired;
    private int skillTreeId;
    private int x;
    private int y;

    public SkillResponse(){

    }

    public SkillResponse(int id, String name, String description, String assessmentCriteria, boolean evidenceRequired, int skillTreeId, int x, int y) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.assessmentCriteria = assessmentCriteria;
        this.evidenceRequired = evidenceRequired;
        this.skillTreeId = skillTreeId;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEvidenceRequired() {
        return evidenceRequired;
    }

    public void setEvidenceRequired(boolean evidenceRequired) {
        this.evidenceRequired = evidenceRequired;
    }

    public int getSkillTreeId() {
        return skillTreeId;
    }

    public void setSkillTreeId(int skillTreeId) {
        this.skillTreeId = skillTreeId;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getAssessmentCriteria() {
        return assessmentCriteria;
    }

    public void setAssessmentCriteria(String assessmentCriteria) {
        this.assessmentCriteria = assessmentCriteria;
    }
}
