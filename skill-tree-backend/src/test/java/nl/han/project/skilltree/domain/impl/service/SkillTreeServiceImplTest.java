package nl.han.project.skilltree.domain.impl.service;

import nl.han.project.skilltree.domain.exceptions.EmptyParameterException;
import nl.han.project.skilltree.domain.exceptions.InvalidRequestException;
import nl.han.project.skilltree.domain.exceptions.ServerException;
import nl.han.project.skilltree.domain.impl.data.SkillDaoImpl;
import nl.han.project.skilltree.domain.impl.data.SkillTreeDaoImpl;
import nl.han.project.skilltree.domain.skill.data.UserSkill;
import nl.han.project.skilltree.domain.skilltree.data.SkillTree;
import nl.han.project.skilltree.domain.skilltree.data.UserSkillTree;
import nl.han.project.skilltree.domain.user.data.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SkillTreeServiceImplTest {

    @Mock
    SkillTreeDaoImpl dao;

    @Mock
    UserServiceImpl userService;

    @Mock
    SkillServiceImpl skillService;

    @Mock
    SkillDaoImpl skillDao;

    @InjectMocks
    SkillTreeServiceImpl sut;

    @Test
    void testCreateSkillTreeNameNull() {
        SkillTree skillTree = new SkillTree();
        skillTree.setName(null);
        assertThrows(EmptyParameterException.class, () -> sut.createSkillTree(skillTree, "token"));
    }

    @Test
    void testCreateSkillTreeNameEmpty() {
        SkillTree skillTree = new SkillTree();
        skillTree.setName("");
        assertThrows(EmptyParameterException.class, () -> sut.createSkillTree(skillTree, "token"));
    }

    @Test
    void testCreateSkillTreeSuccessful() {
        SkillTree skillTree = new SkillTree();
        skillTree.setName("test");
        User user = new User();
        user.setId(1);
        when(dao.insertSkillTree(skillTree, user)).thenReturn(1);
        when(dao.getSkillTreeById(1)).thenReturn(skillTree);
        when(userService.getUserByToken("token")).thenReturn(user);
        SkillTree result = sut.createSkillTree(skillTree, "token");
        assertEquals(skillTree, result);
    }

    @Test
    void testArchiveSkillTreeIdInvalid() {
        assertThrows(InvalidRequestException.class, () -> sut.archiveSkillTree(0));
    }

    @Test
    void testArchiveSkillTreeNoRowsAffected() {
        when(dao.archiveSkillTree(1)).thenReturn(0);
        assertThrows(ServerException.class, () -> sut.archiveSkillTree(1));
    }

    @Test
    void testArchiveSkillTreeSuccessful() {
        when(dao.archiveSkillTree(1)).thenReturn(1);
        assertDoesNotThrow(() -> sut.archiveSkillTree(1));
    }

    @Test
    void testGetSkillTreesSuccessful() {
        Date date = new Date();
        List<SkillTree> skillTrees = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            SkillTree skillTree = new SkillTree();
            skillTree.setId(i);
            skillTree.setName("SkillTree" + i);
            skillTree.setArchived(false);
            skillTree.setCreatedOn(date);

            skillTrees.add(skillTree);
        }
        when(dao.getSkillTrees(false)).thenReturn(skillTrees);

        sut.getSkillTrees(false);

        assertEquals(9, sut.getSkillTrees(false).size());
        for(SkillTree skillTree : skillTrees) {
            assertTrue(sut.getSkillTrees(false).contains(skillTree));
        }
    }

    @Test
    void testGetSkillTreeSuccessful() {
        Date date = new Date();
        SkillTree skillTree = new SkillTree();
        skillTree.setId(1);
        skillTree.setName("SkillTree 1");
        skillTree.setCreatedOn(date);
        skillTree.setUserId(2);
        skillTree.setArchived(false);


        when(dao.getSkillTreeById(1)).thenReturn(skillTree);

        SkillTree result = sut.getSkillTree(1);

        assertEquals(skillTree.getId(), result.getId());
        assertEquals(skillTree.getName(), result.getName());
        assertEquals(skillTree.getCreatedOn(), result.getCreatedOn());
        assertEquals(skillTree.getUserId(), result.getUserId());
    }

    @Test
    void testRestoreSkillTreeInvalidId() {
        assertThrows(InvalidRequestException.class, () -> sut.restoreSkillTree(0));
    }

    @Test
    void testRestoreSkillTreeNoRowsAffected() {
        when(dao.restoreSkillTree(1)).thenReturn(0);

        assertThrows(ServerException.class, () -> sut.restoreSkillTree(1));
    }

    @Test
    void testRestoreSkillTreeSuccessful() {
        when(dao.restoreSkillTree(1)).thenReturn(1);
        assertDoesNotThrow(() -> sut.restoreSkillTree(1));
    }

    @Test
    void testUpdateSkillTreeInvalidId() {
        SkillTree skillTree = new SkillTree();
        skillTree.setId(0);
        skillTree.setName("test");
        assertThrows(InvalidRequestException.class, () -> sut.updateSkillTree(skillTree));
    }

    @Test
    void testUpdateSkillTreeNameNull() {
        SkillTree skillTree = new SkillTree();
        skillTree.setId(1);
        skillTree.setName(null);
        assertThrows(EmptyParameterException.class, () -> sut.updateSkillTree(skillTree));
    }

    @Test
    void testUpdateSkillTreeNameEmpty() {
        SkillTree skillTree = new SkillTree();
        skillTree.setId(1);
        skillTree.setName("");
        assertThrows(EmptyParameterException.class, () -> sut.updateSkillTree(skillTree));
    }

    @Test
    void testUpdateSkillTreeNoRowsAffected() {
        SkillTree skillTree = new SkillTree();
        skillTree.setId(1);
        skillTree.setName("test");
        when(dao.updateSkillTree(skillTree)).thenReturn(0);
        assertThrows(ServerException.class, () -> sut.updateSkillTree(skillTree));
    }

    @Test
    void testUpdateSkillTreeSuccessful() {
        SkillTree skillTree = new SkillTree();
        skillTree.setId(1);
        skillTree.setName("test");
        when(dao.updateSkillTree(skillTree)).thenReturn(1);
        assertDoesNotThrow(() -> sut.updateSkillTree(skillTree));
    }

    @Test
    public void testDeleteSkillTreeInvalidId(){
        Assertions.assertThrows(InvalidRequestException.class, () -> sut.deleteSkillTree(0));
    }

    @Test
    public void testDeleteNoRowsAffected() {
        when(dao.deleteSkillTree(1)).thenReturn(0);
        assertThrows(ServerException.class, () -> sut.deleteSkillTree(1));
    }

    @Test
    public void testDeleteSkillTreeSuccessful() {
        when(dao.deleteSkillTree(1)).thenReturn(1);
        assertDoesNotThrow(() -> sut.deleteSkillTree(1));
    }

    @Test
    public void testSuccessfulGetSkillTreeForUser() {
        SkillTree skillTree = new SkillTree();
        skillTree.setId(1);
        skillTree.setName("test");
        skillTree.setCreatedOn(new Date());
        skillTree.setUserId(2);
        skillTree.setArchived(false);

        when(dao.getSkillTreeById(1)).thenReturn(skillTree);

        assertDoesNotThrow(() -> sut.getSkillTreeForUser(1, 2));
    }
}