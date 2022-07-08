package nl.han.project.skilltree.domain.skilltree.presentation.dto;

import nl.han.project.skilltree.domain.skill.presentation.dto.SkillCouplingResponse;
import nl.han.project.skilltree.domain.skill.presentation.dto.SkillResponse;

import java.util.List;

public class SkillTreeResponse {
    private int id;
    private String name;
    private int userId;

    private List<SkillResponse> skills;

    private List<SkillCouplingResponse> edges;

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<SkillResponse> getSkills() { return skills; }

    public void setSkills(List<SkillResponse> skills) {
        this.skills = skills;
    }

    public List<SkillCouplingResponse> getEdges() {
        return edges;
    }

    public void setEdges(List<SkillCouplingResponse> edges) {
        this.edges = edges;
    }
}
