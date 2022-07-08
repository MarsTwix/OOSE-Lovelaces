package nl.han.project.skilltree.domain.skill.presentation.dto;

public class SkillRequest {
    private String name;
    private String description;
    private String assessmentCriteria;
    private boolean evidenceRequired;
    private int skillTreeId;
    private int x;
    private int y;

    public SkillRequest() {
    }

    public SkillRequest(String name, String description, String assessmentCriteria, boolean evidenceRequired, int skillTreeId, int x, int y) {
        this.name = name;
        this.description = description;
        this.assessmentCriteria = assessmentCriteria;
        this.evidenceRequired = evidenceRequired;
        this.skillTreeId = skillTreeId;
        this.x = x;
        this.y = y;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEvidenceRequired(boolean evidenceRequired) {
        this.evidenceRequired = evidenceRequired;
    }

    public void setSkillTreeId(int skillTreeId) {
        this.skillTreeId = skillTreeId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isEvidenceRequired() {
        return evidenceRequired;
    }

    public int getSkillTreeId() {
        return skillTreeId;
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
