package nl.han.project.skilltree.domain.impl.mapper;

import nl.han.project.skilltree.domain.skill.data.Skill;
import nl.han.project.skilltree.domain.skill.presentation.dto.SkillRequest;
import nl.han.project.skilltree.domain.skill.presentation.dto.SkillResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SkillMapperImplTest {

    SkillMapperImpl sut;

    @BeforeEach
    public void setup() {
        sut = new SkillMapperImpl();
    }

    @Test
    void testMapRequestToEntity() {
        SkillRequest req = new SkillRequest();
        req.setName("name");
        req.setDescription("description");
        req.setAssessmentCriteria("assessmentCriteria");
        req.setEvidenceRequired(true);
        req.setSkillTreeId(1);
        req.setX(2);
        req.setY(3);

        Skill entity = new Skill();
        entity.setName("name");
        entity.setDescription("description");
        entity.setAssessmentCriteria("assessmentCriteria");
        entity.setEvidenceRequired(true);
        entity.setSkillTreeId(1);
        entity.setX(2);
        entity.setY(3);

        Skill result = sut.mapRequestToEntity(req);

        assertEquals(entity.getName(), result.getName());
        assertEquals(entity.getDescription(), result.getDescription());
        assertEquals(entity.getAssessmentCriteria(), result.getAssessmentCriteria());
        assertEquals(entity.isEvidenceRequired(), result.isEvidenceRequired());
        assertEquals(entity.getSkillTreeId(), result.getSkillTreeId());
        assertEquals(entity.getX(), result.getX());
        assertEquals(entity.getY(), result.getY());
    }

    @Test
    void testMapRequestToEntityWithId() {
        SkillRequest req = new SkillRequest();
        req.setName("name");
        req.setDescription("description");
        req.setAssessmentCriteria("assessmentCriteria");
        req.setEvidenceRequired(true);
        req.setSkillTreeId(1);
        req.setX(2);
        req.setY(3);

        Skill entity = new Skill();
        entity.setName("name");
        entity.setDescription("description");
        entity.setAssessmentCriteria("assessmentCriteria");
        entity.setEvidenceRequired(true);
        entity.setSkillTreeId(1);
        entity.setX(2);
        entity.setY(3);

        Skill result = sut.mapRequestToEntity(req, 1);

        assertEquals(entity.getName(), result.getName());
        assertEquals(entity.getDescription(), result.getDescription());
        assertEquals(entity.getAssessmentCriteria(), result.getAssessmentCriteria());
        assertEquals(entity.isEvidenceRequired(), result.isEvidenceRequired());
        assertEquals(entity.getSkillTreeId(), result.getSkillTreeId());
        assertEquals(entity.getX(), result.getX());
        assertEquals(entity.getY(), result.getY());
    }

    @Test
    void testMapResultSetToEntity() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getInt("Id")).thenReturn(1);
        when(rs.getTimestamp("Created")).thenReturn(null);
        when(rs.getString("Name")).thenReturn("name");
        when(rs.getString("Description")).thenReturn("description");
        when(rs.getString("AssessmentCriteria")).thenReturn("assessmentCriteria");
        when(rs.getBoolean("EvidenceRequired")).thenReturn(true);
        when(rs.getInt("SkillTreeId")).thenReturn(2);
        when(rs.getInt("PositionX")).thenReturn(3);
        when(rs.getInt("PositionY")).thenReturn(4);

        Skill entity = new Skill();
        entity.setId(1);
        entity.setCreatedOn(null);
        entity.setName("name");
        entity.setDescription("description");
        entity.setAssessmentCriteria("assessmentCriteria");
        entity.setEvidenceRequired(true);
        entity.setSkillTreeId(2);
        entity.setX(3);
        entity.setY(4);
        Skill result = sut.mapResultSetToEntity(rs);

        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getCreatedOn(), result.getCreatedOn());
        assertEquals(entity.getName(), result.getName());
        assertEquals(entity.getDescription(), result.getDescription());
        assertEquals(entity.getAssessmentCriteria(), result.getAssessmentCriteria());
        assertEquals(entity.isEvidenceRequired(), result.isEvidenceRequired());
        assertEquals(entity.getSkillTreeId(), result.getSkillTreeId());
        assertEquals(entity.getX(), result.getX());
        assertEquals(entity.getY(), result.getY());
    }

    @Test
    void testMapEntityToResponse() {
        Skill entity = new Skill();
        entity.setId(1);
        entity.setName("name");
        entity.setDescription("description");
        entity.setAssessmentCriteria("assessmentCriteria");
        entity.setEvidenceRequired(true);
        entity.setSkillTreeId(2);
        entity.setX(3);
        entity.setY(4);

        SkillResponse response = new SkillResponse();
        response.setId(1);
        response.setName("name");
        response.setDescription("description");
        response.setAssessmentCriteria("assessmentCriteria");
        response.setEvidenceRequired(true);
        response.setSkillTreeId(2);
        response.setX(3);
        response.setY(4);

        SkillResponse result = sut.mapEntityToResponse(entity);

        assertEquals(response.getId(), result.getId());
        assertEquals(response.getName(), result.getName());
        assertEquals(response.getDescription(), result.getDescription());
        assertEquals(response.getAssessmentCriteria(), result.getAssessmentCriteria());
        assertEquals(response.isEvidenceRequired(), result.isEvidenceRequired());
        assertEquals(response.getSkillTreeId(), result.getSkillTreeId());
        assertEquals(response.getX(), result.getX());
        assertEquals(response.getY(), result.getY());
    }
}
