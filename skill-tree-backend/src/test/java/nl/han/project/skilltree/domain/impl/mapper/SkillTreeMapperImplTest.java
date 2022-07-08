package nl.han.project.skilltree.domain.impl.mapper;

import nl.han.project.skilltree.domain.skill.data.Skill;
import nl.han.project.skilltree.domain.skill.data.SkillCoupling;
import nl.han.project.skilltree.domain.skill.presentation.dto.SkillCouplingResponse;
import nl.han.project.skilltree.domain.skill.presentation.dto.SkillResponse;
import nl.han.project.skilltree.domain.skilltree.data.SkillTree;
import nl.han.project.skilltree.domain.skilltree.presentation.dto.SkillTreeRequest;
import nl.han.project.skilltree.domain.skilltree.presentation.dto.SkillTreeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SkillTreeMapperImplTest {

    @Mock
    private SkillMapperImpl skillMapper;

    @Mock
    private SkillCouplingMapperImpl skillCouplingMapper;

    @InjectMocks
    private SkillTreeMapperImpl sut;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMapRequestToEntity() throws SQLException {
        SkillTreeRequest req = new SkillTreeRequest();
        req.setName("name");
        SkillTree entity = new SkillTree();
        entity.setName("name");

        SkillTree result = sut.mapRequestToEntity(req);

        assertEquals(entity.getName(), result.getName());
    }

    @Test
    void testMapRequestToEntityWithId() throws SQLException {
        SkillTreeRequest req = new SkillTreeRequest();
        req.setName("name");
        SkillTree entity = new SkillTree();
        entity.setName("name");
        entity.setId(1);

        SkillTree result = sut.mapRequestToEntity(req, 1);

        assertEquals(entity.getName(), result.getName());
        assertEquals(entity.getId(), result.getId());
    }

    @Test
    void testMapResultSetToEntity() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getInt("Id")).thenReturn(1);
        when(rs.getString("Name")).thenReturn("name");
        when(rs.getTimestamp("CreatedOn")).thenReturn(null);
        when(rs.getInt("UserId")).thenReturn(2);
        when(rs.getBoolean("Archived")).thenReturn(false);

        SkillTree entity = new SkillTree();
        entity.setId(1);
        entity.setName("name");
        entity.setCreatedOn(null);
        entity.setUserId(2);
        entity.setArchived(false);

        SkillTree result = sut.mapResultSetToEntity(rs);

        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getName(), result.getName());
        assertEquals(entity.getCreatedOn(), result.getCreatedOn());
        assertEquals(entity.getUserId(), result.getUserId());
        assertEquals(entity.isArchived(), result.isArchived());
    }

    @Test
    void testMapEntityToResponse() {
        Skill skill = new Skill();
        skill.setId(1);
        skill.setName("name");
        skill.setDescription("description");
        skill.setEvidenceRequired(true);
        skill.setSkillTreeId(2);
        skill.setX(3);
        skill.setY(4);
        skill.setCreatedOn(null);

        SkillCoupling skillCoupling = new SkillCoupling();
        skillCoupling.setId(1);
        skillCoupling.setSourceId(2);
        skillCoupling.setTargetId(3);
        skillCoupling.setSkillTreeId(4);

        SkillTree entity = new SkillTree();
        entity.setId(1);
        entity.setName("name");
        entity.setUserId(2);
        entity.setArchived(false);
        entity.setCreatedOn(null);
        entity.setSkills(List.of(skill));
        entity.setEdges(List.of(skillCoupling));

        SkillResponse skillResponse = new SkillResponse();
        skillResponse.setId(1);
        skillResponse.setName("name");
        skillResponse.setDescription("description");
        skillResponse.setEvidenceRequired(true);
        skillResponse.setSkillTreeId(2);
        skillResponse.setX(3);
        skillResponse.setY(4);

        SkillCouplingResponse skillCouplingResponse = new SkillCouplingResponse();
        skillCouplingResponse.setId(1);
        skillCouplingResponse.setSourceId(2);
        skillCouplingResponse.setTargetId(3);

        SkillTreeResponse response = new SkillTreeResponse();
        response.setId(1);
        response.setName("name");
        response.setUserId(2);
        response.setSkills(List.of(skillResponse));
        response.setEdges(List.of(skillCouplingResponse));

        when(skillMapper.mapEntityToResponse(skill)).thenReturn(skillResponse);
        when(skillCouplingMapper.mapEntityToResponse(skillCoupling)).thenReturn(skillCouplingResponse);

        SkillTreeResponse result = sut.mapEntityToResponse(entity);

        assertEquals(response.getId(), result.getId());
        assertEquals(response.getName(), result.getName());
        assertEquals(response.getUserId(), result.getUserId());
        assertTrue(!result.getSkills().isEmpty());
        assertTrue(!result.getEdges().isEmpty());
    }

    @Test
    void testMapEntityToResponseSkillsNull(){

        SkillCoupling skillCoupling = new SkillCoupling();
        skillCoupling.setId(1);
        skillCoupling.setSourceId(2);
        skillCoupling.setTargetId(3);
        skillCoupling.setSkillTreeId(4);

        SkillTree entity = new SkillTree();
        entity.setId(1);
        entity.setName("name");
        entity.setUserId(2);
        entity.setArchived(false);
        entity.setCreatedOn(null);
        entity.setSkills(null);
        entity.setEdges(List.of(skillCoupling));

        SkillResponse skillResponse = new SkillResponse();
        skillResponse.setId(1);
        skillResponse.setName("name");
        skillResponse.setDescription("description");
        skillResponse.setEvidenceRequired(true);
        skillResponse.setSkillTreeId(2);
        skillResponse.setX(3);
        skillResponse.setY(4);

        SkillCouplingResponse skillCouplingResponse = new SkillCouplingResponse();
        skillCouplingResponse.setId(1);
        skillCouplingResponse.setSourceId(2);
        skillCouplingResponse.setTargetId(3);

        SkillTreeResponse response = new SkillTreeResponse();
        response.setId(1);
        response.setName("name");
        response.setUserId(2);
        response.setSkills(List.of(skillResponse));
        response.setEdges(List.of(skillCouplingResponse));

        SkillTreeResponse result = sut.mapEntityToResponse(entity);

        assertEquals(response.getId(), result.getId());
        assertEquals(response.getName(), result.getName());
        assertEquals(response.getUserId(), result.getUserId());
        assertTrue(result.getSkills().isEmpty());
        assertTrue(!result.getEdges().isEmpty());
    }

    @Test
    void testMapEntityToResponseSkillCouplingNull() {
        Skill skill = new Skill();
        skill.setId(1);
        skill.setName("name");
        skill.setDescription("description");
        skill.setEvidenceRequired(true);
        skill.setSkillTreeId(2);
        skill.setX(3);
        skill.setY(4);
        skill.setCreatedOn(null);

        SkillTree entity = new SkillTree();
        entity.setId(1);
        entity.setName("name");
        entity.setUserId(2);
        entity.setArchived(false);
        entity.setCreatedOn(null);
        entity.setSkills(List.of(skill));
        entity.setEdges(null);

        SkillResponse skillResponse = new SkillResponse();
        skillResponse.setId(1);
        skillResponse.setName("name");
        skillResponse.setDescription("description");
        skillResponse.setEvidenceRequired(true);
        skillResponse.setSkillTreeId(2);
        skillResponse.setX(3);
        skillResponse.setY(4);

        SkillCouplingResponse skillCouplingResponse = new SkillCouplingResponse();
        skillCouplingResponse.setId(1);
        skillCouplingResponse.setSourceId(2);
        skillCouplingResponse.setTargetId(3);

        SkillTreeResponse response = new SkillTreeResponse();
        response.setId(1);
        response.setName("name");
        response.setUserId(2);
        response.setSkills(List.of(skillResponse));
        response.setEdges(List.of(skillCouplingResponse));

        when(skillMapper.mapEntityToResponse(skill)).thenReturn(skillResponse);

        SkillTreeResponse result = sut.mapEntityToResponse(entity);

        assertEquals(response.getId(), result.getId());
        assertEquals(response.getName(), result.getName());
        assertEquals(response.getUserId(), result.getUserId());
        assertTrue(!result.getSkills().isEmpty());
        assertTrue(result.getEdges().isEmpty());
    }
}

