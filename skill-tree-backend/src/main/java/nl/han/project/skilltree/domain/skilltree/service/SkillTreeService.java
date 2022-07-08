package nl.han.project.skilltree.domain.skilltree.service;

import nl.han.project.skilltree.domain.skilltree.data.SkillTree;
import nl.han.project.skilltree.domain.skilltree.data.UserSkillTree;

import java.util.List;

public interface SkillTreeService {
    SkillTree createSkillTree(SkillTree skillTree, String token);

    List<SkillTree> getSkillTrees(boolean archived);

    SkillTree getSkillTree(int id);

    void archiveSkillTree(int id);

    void restoreSkillTree(int id);

    void deleteSkillTree(int id);

    void updateSkillTree(SkillTree skillTree);

    UserSkillTree getSkillTreeForUser(int id, int studentId);
}
