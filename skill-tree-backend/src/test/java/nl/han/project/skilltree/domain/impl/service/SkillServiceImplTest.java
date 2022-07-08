package nl.han.project.skilltree.domain.impl.service;

import nl.han.project.skilltree.domain.exceptions.EmptyParameterException;
import nl.han.project.skilltree.domain.exceptions.InvalidRequestException;
import nl.han.project.skilltree.domain.exceptions.ServerException;
import nl.han.project.skilltree.domain.impl.data.SkillDaoImpl;
import nl.han.project.skilltree.domain.skill.data.Skill;
import nl.han.project.skilltree.domain.skill.data.SkillCoupling;

import nl.han.project.skilltree.domain.skill.data.UserSkill;
import nl.han.project.skilltree.domain.skilltree.data.SkillTree;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SkillServiceImplTest {

    @Mock
    SkillDaoImpl dao;

    @InjectMocks
    SkillServiceImpl sut;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateSkillNameRequired() {
        Skill skill = new Skill();
        skill.setName(null);
        skill.setDescription("description");
        skill.setEvidenceRequired(true);
        skill.setSkillTreeId(1);
        skill.setX(2);
        skill.setY(3);

        assertThrows(InvalidRequestException.class, () -> sut.createSkill(skill));
    }

    @Test
    public void testCreateSkillDescriptionRequired() {
        Skill skill = new Skill();
        skill.setName("name");
        skill.setDescription(null);
        skill.setEvidenceRequired(true);
        skill.setSkillTreeId(1);
        skill.setX(2);
        skill.setY(3);

        assertThrows(InvalidRequestException.class, () -> sut.createSkill(skill));
    }

    @Test
    public void testCreateSkillInvalidSkillTreeId() {
        Skill skill = new Skill();
        skill.setName("name");
        skill.setDescription("description");
        skill.setEvidenceRequired(true);
        skill.setSkillTreeId(0);
        skill.setX(1);
        skill.setY(2);

        assertThrows(InvalidRequestException.class, () -> sut.createSkill(skill));
    }

    @Test
    public void testCreateSkillSuccessful() {
        Skill skill = new Skill();
        skill.setName("name");
        skill.setDescription("description");
        skill.setEvidenceRequired(true);
        skill.setSkillTreeId(1);
        skill.setX(2);
        skill.setY(3);

        when(dao.insertSkill(skill)).thenReturn(1);
        when(dao.getSkill(1)).thenReturn(new Skill());

        assertDoesNotThrow(() -> {
            sut.createSkill(skill);
        });
    }

    @Test
    public void testGetSkillsForSkillTreeSuccessful() {
        SkillTree skillTree = new SkillTree();
        skillTree.setId(1);
        List<Skill> skills = new ArrayList<>();

        Date date = new Date();

        for(int i = 0; i < 10; i++) {
            Skill skill = new Skill();
            skill.setId(i);
            skill.setCreatedOn(date);
            skill.setName("Skill " + i);
            skill.setDescription("Description " + i);
            skill.setEvidenceRequired(true);
            skill.setSkillTreeId(skillTree.getId());
            skill.setX(0);
            skill.setY(0);
            skills.add(skill);
        }

        when(dao.getSkillsForSkillTree(skillTree.getId(), false)).thenReturn(skills);

        List<Skill> result = sut.getSkillsForSkillTree(skillTree.getId(), false);

        assertEquals(10, result.size());
        assertEquals(skills.get(0).getId(), result.get(0).getId());
        assertEquals(skillTree.getId(), result.get(9).getSkillTreeId());
        assertEquals(date , result.get(0).getCreatedOn());
    }

    @Test
    public void testGetSkillsForSkillTreeInvalidId() {
        SkillTree skillTree = new SkillTree();
        skillTree.setId(1);

        assertThrows(InvalidRequestException.class, () -> sut.getSkillsForSkillTree(0, false));
    }

    @Test
    public void testGetSkillCouplingsForSkillTreeSuccessful() {
        SkillTree skillTree = new SkillTree();
        skillTree.setId(1);
        List<SkillCoupling> skillCouplingList = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            SkillCoupling skillCoupling = new SkillCoupling();
            skillCoupling.setId(i);
            skillCoupling.setSourceId(i+1);
            skillCoupling.setTargetId(i+2);
            skillCouplingList.add(skillCoupling);
        }

        when(dao.getSkillCouplingsForSkillTree(skillTree.getId())).thenReturn(skillCouplingList);
        sut.getSkillCouplingsForSkillTree(skillTree.getId());

        assertEquals(10, skillCouplingList.size());
        assertEquals(3, skillCouplingList.get(2).getSourceId());
        assertEquals(5, skillCouplingList.get(3).getTargetId());
    }

    @Test
    public void testArchiveSkillInvalidId(){
        assertThrows(InvalidRequestException.class, () -> sut.archiveSkill(0));
    }

    @Test
    public void testArchiveSkillNoRowsAffected() {
        when(dao.archiveSkill(1)).thenReturn(0);
        assertThrows(ServerException.class, () -> sut.archiveSkill(1));
    }

    @Test
    public void testArchiveSkillSuccessful() {
        when(dao.archiveSkill(1)).thenReturn(1);
        assertDoesNotThrow(() -> sut.archiveSkill(1));
    }

    @Test
    public void testRestoreSkillInvalidId(){
        assertThrows(InvalidRequestException.class, () -> sut.restoreSkill(0));
    }

    @Test
    public void testRestoreSkillNoRowsAffected() {
        when(dao.restoreSkill(1)).thenReturn(0);
        assertThrows(ServerException.class, () -> sut.restoreSkill(1));
    }

    @Test
    public void testRestoreSkillSuccessful() {
        when(dao.restoreSkill(1)).thenReturn(1);
        assertDoesNotThrow(() -> sut.restoreSkill(1));
    }

    @Test
    public void testDeleteSkillInvalidId(){
        Assertions.assertThrows(InvalidRequestException.class, () -> sut.deleteSkill(0));
    }

    @Test
    public void testDeleteSkillNoRowsAffected() {
        when(dao.deleteSkill(1)).thenReturn(0);
        assertThrows(ServerException.class, () -> sut.deleteSkill(1));
    }

    @Test
    public void testDeleteSkillSuccessful() {
        when(dao.deleteSkill(1)).thenReturn(1);
        assertDoesNotThrow(() -> sut.deleteSkill(1));
    }

    @Test
    public void testUpdateSkillPositionSuccessful() {
        Skill skill = new Skill();
        skill.setId(1);
        skill.setX(2);
        skill.setY(3);
        when(dao.updateSkillPosition(skill)).thenReturn(1);
        assertDoesNotThrow(() -> sut.updateSkillPosition(skill));
    }

    @Test
    public void testUpdateSkillPositionInvalidId(){
        Skill skill = new Skill();
        skill.setId(0);
        skill.setX(1);
        skill.setY(2);
        assertThrows(InvalidRequestException.class, () -> sut.updateSkillPosition(skill));
    }

    @Test
    public void testUpdateSkillPositionNoRowsAffected() {
        Skill skill = new Skill();
        skill.setId(1);
        skill.setX(2);
        skill.setY(3);
        when(dao.updateSkillPosition(skill)).thenReturn(0);
        assertThrows(ServerException.class, () -> sut.updateSkillPosition(skill));
    }


    @Test
    public void testUpdateSkillSuccessful() {
        Skill skill = new Skill();
        skill.setId(1);
        skill.setName("Skill 1");
        skill.setDescription("Description 1");
        skill.setEvidenceRequired(true);
        when(dao.updateSkill(skill)).thenReturn(1);
        assertDoesNotThrow(() -> sut.updateSkill(skill));
    }

    @Test
    public void testUpdateSkillNameNull() {
        Skill skill = new Skill();
        skill.setId(1);
        skill.setName(null);
        skill.setDescription("description");
        skill.setEvidenceRequired(true);
        when(dao.updateSkill(skill)).thenReturn(1);
        assertThrows(ServerException.class, () -> sut.updateSkill(skill));
    }

    @Test
    public void testUpdateSkillNameEmpty() {
        Skill skill = new Skill();
        skill.setId(1);
        skill.setName("");
        skill.setDescription("description");
        skill.setEvidenceRequired(true);
        when(dao.updateSkill(skill)).thenReturn(1);
        assertThrows(ServerException.class, () -> sut.updateSkill(skill));
    }

    @Test
    public void testUpdateSkillDescriptionNull() {
        Skill skill = new Skill();
        skill.setId(1);
        skill.setName("name");
        skill.setDescription(null);
        skill.setEvidenceRequired(true);
        when(dao.updateSkill(skill)).thenReturn(1);
        assertThrows(ServerException.class, () -> sut.updateSkill(skill));
    }

    @Test
    public void testUpdateSkillDescriptionEmpty() {
        Skill skill = new Skill();
        skill.setId(1);
        skill.setName("name");
        skill.setDescription("");
        skill.setEvidenceRequired(true);
        when(dao.updateSkill(skill)).thenReturn(1);
        assertThrows(ServerException.class, () -> sut.updateSkill(skill));
    }

    @Test
    public void testUpdateSkillInvalidId() {
        Skill skill = new Skill();
        skill.setId(0);
        skill.setName("name");
        skill.setDescription("description");
        skill.setEvidenceRequired(true);
        when(dao.updateSkill(skill)).thenReturn(1);
        assertThrows(ServerException.class, () -> sut.updateSkill(skill));
    }

    @Test
    public void testUpdateSkillNoRowsAffected() {
        Skill skill = new Skill();
        skill.setId(1);
        skill.setName("name");
        skill.setDescription("description");
        skill.setEvidenceRequired(true);
        when(dao.updateSkill(skill)).thenReturn(0);
        assertThrows(ServerException.class, () -> sut.updateSkill(skill));
    }

    @Test
    public void testCreateSkillCouplingInvalidSourceId() {
        SkillCoupling coupling = new SkillCoupling();
        coupling.setSourceId(0);
        coupling.setTargetId(1);
        coupling.setSkillTreeId(2);
        assertThrows(EmptyParameterException.class, () -> sut.createSkillCoupling(coupling));
    }

    @Test
    public void testCreateSkillCouplingInvalidTargetId() {
        SkillCoupling coupling = new SkillCoupling();
        coupling.setSourceId(1);
        coupling.setTargetId(0);
        coupling.setSkillTreeId(2);
        assertThrows(EmptyParameterException.class, () -> sut.createSkillCoupling(coupling));
    }

    @Test
    public void testCreateSkillCouplingInvalidSkillTreeId() {
        SkillCoupling coupling = new SkillCoupling();
        coupling.setSourceId(1);
        coupling.setTargetId(2);
        coupling.setSkillTreeId(0);
        assertThrows(EmptyParameterException.class, () -> sut.createSkillCoupling(coupling));
    }

    @Test
    public void testCreateSkillCouplingSuccessful() {
        SkillCoupling coupling = new SkillCoupling();
        coupling.setSourceId(1);
        coupling.setTargetId(2);
        coupling.setSkillTreeId(3);
        when(dao.insertSkillCoupling(coupling)).thenReturn(1);

        assertDoesNotThrow(() -> sut.createSkillCoupling(coupling));
        SkillCoupling result =  sut.createSkillCoupling(coupling);
        assertEquals(1, result.getId());
    }

    @Test
    public void testDeleteSkillCouplingSuccessful() {
        when(dao.deleteSkillCoupling(1, 2)).thenReturn(1);
        assertDoesNotThrow(() -> sut.deleteSkillCoupling(1, 2));
    }

    @Test
    public void testDeleteSkillCouplingInvalidSkillCouplingId() {
        assertThrows(ServerException.class, () -> sut.deleteSkillCoupling(1, 0));
    }

    @Test
    public void testDeleteSkillCouplingNoRowsAffected() {
        when(dao.deleteSkillCoupling(1, 2)).thenReturn(0);
        assertThrows(ServerException.class, () -> sut.deleteSkillCoupling(1, 2));
    }

    @Test
    public void testGetSkillsForSkillTreeAndUserInvalidId(){
        assertThrows(InvalidRequestException.class, () -> sut.getSkillsForSkillTreeAndUser(0, 1));
    }

    @Test
    public void testGetSkillsForSkillTreeAndUserInvalidUserId(){
        assertThrows(InvalidRequestException.class, () -> sut.getSkillsForSkillTreeAndUser(1, 0));
    }

    @Test
    public void testGetSkillsForSkillTreeAndUserSuccessful() {
        when(dao.getSkillsForSkillTreeAndUser(1, 1)).thenReturn(new ArrayList<>());
        assertDoesNotThrow(() -> sut.getSkillsForSkillTreeAndUser(1, 1));
    }
}
