package nl.han.project.skilltree.domain.skill.data;

public class SkillCoupling {

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

    public void setSourceId(int source) {
        this.sourceId = source;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int target) {
        this.targetId = target;
    }

    public int getSkillTreeId() {
        return skillTreeId;
    }

    public void setSkillTreeId(int skillTreeId) {
        this.skillTreeId = skillTreeId;
    }
}
