package nl.han.project.skilltree.domain.skilltree.data;

import nl.han.project.skilltree.domain.skill.data.Skill;
import nl.han.project.skilltree.domain.skill.data.SkillCoupling;

import java.util.Date;
import java.util.List;

public class SkillTree {
    private int id;
    private String name;
    private Date createdOn;
    private int userId;
    private boolean archived;

    private List<Skill> skills;

    private List<SkillCoupling> edges;

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

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public List<SkillCoupling> getEdges() {
        return edges;
    }

    public void setEdges(List<SkillCoupling> edges) {
        this.edges = edges;
    }
}
