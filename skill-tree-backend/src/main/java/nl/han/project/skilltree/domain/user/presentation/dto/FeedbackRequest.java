package nl.han.project.skilltree.domain.user.presentation.dto;

public class FeedbackRequest {
    private int skillId;

    private int forUserId;
    private String description;

    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    public int getForUserId() {
        return forUserId;
    }

    public void setForUserId(int forUserId) {
        this.forUserId = forUserId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
