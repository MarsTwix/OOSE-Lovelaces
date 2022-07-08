package nl.han.project.skilltree.domain.skill.data;

import java.util.List;

public interface SkillDao {

    List<Skill> getSkillsForSkillTree(int id, boolean archived);
    Skill getSkill(int id );
    int insertSkill(Skill skill);

    int archiveSkill(int id);

    int restoreSkill(int id);

    int deleteSkill(int id);

    int insertSkillCoupling(SkillCoupling skillCoupling);

    List<SkillCoupling> getSkillCouplingsForSkillTree(int id);

    int updateSkillPosition(Skill skill);

    int updateSkill(Skill skill);

    List<UserSkill> getSkillsForSkillTreeAndUser(int id, int studentId);

    int deleteSkillCoupling(int id, int skillCouplingId);
}
