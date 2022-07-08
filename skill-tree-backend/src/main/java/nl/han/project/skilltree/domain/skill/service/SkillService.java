package nl.han.project.skilltree.domain.skill.service;

import nl.han.project.skilltree.domain.skill.data.Skill;
import nl.han.project.skilltree.domain.skill.data.SkillCoupling;
import nl.han.project.skilltree.domain.skill.data.UserSkill;

import java.util.List;

public interface SkillService {
    List<Skill> getSkillsForSkillTree(int id, boolean archived);
    Skill createSkill(Skill skill);
    SkillCoupling createSkillCoupling(SkillCoupling skillCoupling);

    List<SkillCoupling> getSkillCouplingsForSkillTree(int id);

    void deleteSkill(int id);

    void archiveSkill(int id);

    void restoreSkill(int id);

    void updateSkillPosition(Skill skill);

    void updateSkill(Skill skill);

    void deleteSkillCoupling(int id, int skillCouplingId);

    List<UserSkill> getSkillsForSkillTreeAndUser(int id, int studentId);
}
