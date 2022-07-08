package nl.han.project.skilltree.domain.impl.service;

import nl.han.project.skilltree.domain.exceptions.EmptyParameterException;
import nl.han.project.skilltree.domain.exceptions.InvalidRequestException;
import nl.han.project.skilltree.domain.exceptions.ServerException;
import nl.han.project.skilltree.domain.skill.data.Skill;
import nl.han.project.skilltree.domain.skill.data.SkillCoupling;
import nl.han.project.skilltree.domain.skill.data.SkillDao;
import nl.han.project.skilltree.domain.skill.data.UserSkill;
import nl.han.project.skilltree.domain.skill.service.SkillService;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.List;

@Default
public class SkillServiceImpl implements SkillService {
    @Inject
    private SkillDao dao;

    @Override
    public Skill createSkill(Skill skill) {
        if (skill.getSkillTreeId() < 1) {
            throw new InvalidRequestException("Skill tree id is invalid");
        } else if (skill.getName() == null || skill.getDescription() == null) {
            throw new InvalidRequestException("Skill name, description and skilltree id are required");

        }
        int insertedId = dao.insertSkill(skill);

        skill = dao.getSkill(insertedId);

        return skill;
    }

    @Override
    public void archiveSkill(int id) {
        if (id < 1) {
            throw new InvalidRequestException("Skill id is invalid");
        }

        int affectedRows = dao.archiveSkill(id);

        if (affectedRows < 1) {
            throw new ServerException("Could not archive skill");
        }
    }

    @Override
    public void restoreSkill(int id) {
        if (id < 1) {
            throw new InvalidRequestException("Skill id is invalid");
        }

        int affectedRows = dao.restoreSkill(id);

        if (affectedRows < 1) {
            throw new ServerException("Could not restore skill");
        }
    }

    @Override
    public void updateSkillPosition(Skill skill) {
        if (skill.getId() < 1) {
            throw new InvalidRequestException("Skill id is invalid");
        }
        if (dao.updateSkillPosition(skill) < 1) {
            throw new ServerException("Could not update skill position");
        }
    }

    @Override
    public void updateSkill(Skill skill) {
        if (skill.getId() < 1 || skill.getName() == null || skill.getName().isEmpty() || skill.getDescription() == null || skill.getDescription().isEmpty()) {
            throw new ServerException("Could not update skill, fields are required");
        } else if (dao.updateSkill(skill) < 1) {
            throw new ServerException("Could not update skill");
        }
    }

    @Override
    public void deleteSkillCoupling(int id, int skillCouplingId) {
        if (skillCouplingId < 1) {
            throw new ServerException("Could not delete skill coupling, fields are required");
        } else if (dao.deleteSkillCoupling(id, skillCouplingId) < 1) {
            throw new ServerException("Could not delete skill coupling");
        }
    }

    @Override
    public List<UserSkill> getSkillsForSkillTreeAndUser(int id, int studentId) {
        if (id < 1) {
            throw new InvalidRequestException("Skill tree id is invalid");
        }
        if (studentId < 1) {
            throw new InvalidRequestException("Student id is invalid");
        }
        return dao.getSkillsForSkillTreeAndUser(id, studentId);
    }

    @Override
    public void deleteSkill(int id) {
        if (id < 1) {
            throw new InvalidRequestException("Skill id is invalid");
        }

        int affectedRows = dao.deleteSkill(id);

        if (affectedRows < 1) {
            throw new ServerException("Could not delete skill");
        }
    }

    @Override
    public List<Skill> getSkillsForSkillTree(int id, boolean archived) {
        if (id < 1) {
            throw new InvalidRequestException("Skill tree id is invalid");
        }

        return dao.getSkillsForSkillTree(id, archived);
    }

    @Override
    public SkillCoupling createSkillCoupling(SkillCoupling skillCoupling) {
        if (skillCoupling.getSourceId() == 0 || skillCoupling.getTargetId() == 0 || skillCoupling.getSkillTreeId() == 0) {
            throw new EmptyParameterException("Body value name cannot be empty!");
        }

        int insertedId = dao.insertSkillCoupling(skillCoupling);

        skillCoupling.setId(insertedId);
        return skillCoupling;
    }

    @Override
    public List<SkillCoupling> getSkillCouplingsForSkillTree(int id) {
        return dao.getSkillCouplingsForSkillTree(id);
    }
}
