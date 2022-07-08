package nl.han.project.skilltree.domain.skilltree.data;

import nl.han.project.skilltree.domain.user.data.User;

import java.util.List;

public interface SkillTreeDao {
    int insertSkillTree(SkillTree skillTree, User user);
    SkillTree getSkillTreeById(int id);

    List<SkillTree> getSkillTrees(boolean archived);

    int deleteSkillTree(int id);

    int updateSkillTree(SkillTree skillTree);

    int archiveSkillTree(int id);

    int restoreSkillTree(int id);
}
