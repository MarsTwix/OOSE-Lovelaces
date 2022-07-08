package nl.han.project.skilltree.domain.skill.presentation.dto;

public class SkillCouplingRequest {
    private int id;
    private int sourceId;
    private int targetId;

    private int skillTreeId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public int getSkillTreeId() {
        return skillTreeId;
    }

    public void setSkillTreeId(int skillTreeId) {
        this.skillTreeId = skillTreeId;
    }
}
