package nl.han.project.skilltree.domain.impl.mapper;

import nl.han.project.skilltree.domain.assessment.data.Assessment;
import nl.han.project.skilltree.domain.assessment.presentation.dto.AssessmentRequest;
import nl.han.project.skilltree.domain.assessment.presentation.dto.AssessmentResponse;
import nl.han.project.skilltree.domain.skill.data.SkillCoupling;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AssessmentMapperImplTest {

    private AssessmentMapperImpl sut;

    @BeforeEach
    public void setup() {
        sut = new AssessmentMapperImpl();
    }

    @Test
    public void testMapRequestToEntity() {
        AssessmentRequest req = new AssessmentRequest();
        req.setDescription("Test");
        req.setGrade(5);
        req.setSkillId(1);
        req.setUserId(1);

        Assessment entity = new Assessment(0, 5, "Test", 1, 1);

        Assessment result = sut.mapRequestToEntity(req);

        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getDescription(), result.getDescription());
        assertEquals(entity.getSkillId(), result.getSkillId());
        assertEquals(entity.getUserId(), result.getUserId());
        Assertions.assertEquals(entity.getGrade(), result.getGrade());
    }

    @Test
    public void testMapResultSetToEntity() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getInt("Id")).thenReturn(1);
        when(rs.getDouble("Grade")).thenReturn(2.5);
        when(rs.getString("Description")).thenReturn("Test");
        when(rs.getInt("SkillId")).thenReturn(4);
        when(rs.getInt("UserId")).thenReturn(5);

        Assessment entity = new Assessment(1, 2.5, "Test", 4, 5);

        Assessment result = sut.mapResultSetToEntity(rs);

        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getDescription(), result.getDescription());
        assertEquals(entity.getGrade(), result.getGrade());
        assertEquals(entity.getSkillId(), result.getSkillId());
        assertEquals(entity.getUserId(), result.getUserId());
    }

    @Test
    public void testMapEntityToResponse() {
        Assessment entity = new Assessment(0, 5, "Test", 1, 1);

        AssessmentResponse result = sut.mapEntityToResponse(entity);

        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getGrade(), result.getGrade());
        assertEquals(entity.getDescription(), result.getDescription());
        assertEquals(entity.getSkillId(), result.getSkillId());
        assertEquals(entity.getUserId(), result.getUserId());
    }
}
