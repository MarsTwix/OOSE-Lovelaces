package nl.han.project.skilltree.domain.impl.service;

import nl.han.project.skilltree.domain.exceptions.EmptyParameterException;
import nl.han.project.skilltree.domain.exceptions.InvalidRequestException;
import nl.han.project.skilltree.domain.exceptions.ServerException;
import nl.han.project.skilltree.domain.skill.data.SkillCoupling;
import nl.han.project.skilltree.domain.skill.data.UserSkill;
import nl.han.project.skilltree.domain.skilltree.data.SkillTree;
import nl.han.project.skilltree.domain.skilltree.data.SkillTreeDao;
import nl.han.project.skilltree.domain.skilltree.data.UserSkillTree;
import nl.han.project.skilltree.domain.skilltree.service.SkillTreeService;
import nl.han.project.skilltree.domain.user.data.User;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.List;

@Default
public class SkillTreeServiceImpl implements SkillTreeService {

    @Inject
    private SkillTreeDao dao;

    @Inject
    private UserServiceImpl userService;

    @Inject
    private SkillServiceImpl skillService;

    @Override
    public SkillTree createSkillTree(SkillTree skillTree, String token) {
        if (skillTree.getName() == null || skillTree.getName().isEmpty()) {
            throw new EmptyParameterException("Body value name cannot be empty!");
        }
        User user = userService.getUserByToken(token);

        int insertedId;


        insertedId = dao.insertSkillTree(skillTree, user);
        skillTree = dao.getSkillTreeById(insertedId);

        return skillTree;
    }

    @Override
    public void archiveSkillTree(int id) {
        if (id < 1) {
            throw new InvalidRequestException("Skill tree id is invalid");
        }
        if (dao.archiveSkillTree(id) < 1) {
            throw new ServerException("Could not archive skill tree");
        }
    }

    @Override
    public void deleteSkillTree(int id) {

        if (id < 1) {
            throw new InvalidRequestException("Skill tree id is invalid");
        }
        if (dao.deleteSkillTree(id) < 1) {
            throw new ServerException("Could not delete skill tree");
        }

    }

    @Override
    public void restoreSkillTree(int id) {

        if (id < 1) {
            throw new InvalidRequestException("Id can not be lower than 1");
        }
        if (dao.restoreSkillTree(id) < 1) {
            throw new ServerException("No records modified");
        }

    }

    @Override
    public void updateSkillTree(SkillTree skillTree) {

        if (skillTree.getId() < 1) {
            throw new InvalidRequestException("Skill tree id is invalid");
        } else if (skillTree.getName() == null || skillTree.getName().isEmpty()) {
            throw new EmptyParameterException("Body value name cannot be empty!");
        } else if (dao.updateSkillTree(skillTree) < 1) {
            throw new ServerException("Could not update skill tree");
        }
    }

    @Override
    public UserSkillTree getSkillTreeForUser(int id, int studentId) {
        SkillTree skillTreeFromDb = dao.getSkillTreeById(id);
        List<UserSkill> userSkills = skillService.getSkillsForSkillTreeAndUser(id, studentId);
        List<SkillCoupling> edges = skillService.getSkillCouplingsForSkillTree(id);
        return mapSkillTreeToUserSkillTree(studentId, skillTreeFromDb, userSkills, edges);
    }

    @Override
    public List<SkillTree> getSkillTrees(boolean archived) {
        return dao.getSkillTrees(archived);
    }

    @Override
    public SkillTree getSkillTree(int id) {
        SkillTree skillTree;

        skillTree = dao.getSkillTreeById(id);
        skillTree.setSkills(skillService.getSkillsForSkillTree(id, false));
        skillTree.setEdges(skillService.getSkillCouplingsForSkillTree(id));

        return skillTree;
    }

    private UserSkillTree mapSkillTreeToUserSkillTree(int studentId, SkillTree skillTree, List<UserSkill> userSkills, List<SkillCoupling> edges) {
        UserSkillTree userSkillTree = new UserSkillTree();
        userSkillTree.setStudentId(studentId);
        userSkillTree.setId(skillTree.getId());
        userSkillTree.setName(skillTree.getName());
        userSkillTree.setArchived(skillTree.isArchived());
        userSkillTree.setUserId(skillTree.getUserId());
        userSkillTree.setCreatedOn(skillTree.getCreatedOn());
        userSkillTree.setUserSkills(userSkills);
        userSkillTree.setEdges(edges);
        return userSkillTree;
    }
}
