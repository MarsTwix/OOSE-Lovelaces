package nl.han.project.skilltree.domain.impl.mapper;

import nl.han.project.skilltree.domain.skill.data.SkillCoupling;
import nl.han.project.skilltree.domain.skill.presentation.dto.SkillCouplingRequest;
import nl.han.project.skilltree.domain.skill.presentation.dto.SkillCouplingResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SkillCouplingMapperImplTest {

    private SkillCouplingMapperImpl sut;

    @BeforeEach
    public void setup() {
        sut = new SkillCouplingMapperImpl();
    }

    @Test
    public void testMapRequestToEntity() {
        SkillCouplingRequest req = new SkillCouplingRequest();
        req.setId(1);
        req.setSourceId(2);
        req.setTargetId(3);
        req.setSkillTreeId(4);
        SkillCoupling entity = new SkillCoupling();
        entity.setId(1);
        entity.setSourceId(2);
        entity.setTargetId(3);
        entity.setSkillTreeId(4);

        SkillCoupling result = sut.mapRequestToEntity(req);

        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getSourceId(), result.getSourceId());
        assertEquals(entity.getTargetId(), result.getTargetId());
        assertEquals(entity.getSkillTreeId(), result.getSkillTreeId());
    }

    @Test
    public void testMapResultSetToEntity() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getInt("Id")).thenReturn(1);
        when(rs.getInt("SourceId")).thenReturn(2);
        when(rs.getInt("TargetId")).thenReturn(3);
        when(rs.getInt("SkillTreeId")).thenReturn(4);

        SkillCoupling entity = new SkillCoupling();
        entity.setId(1);
        entity.setSourceId(2);
        entity.setTargetId(3);
        entity.setSkillTreeId(4);

        SkillCoupling result = sut.mapResultSetToEntity(rs);

        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getSourceId(), result.getSourceId());
        assertEquals(entity.getTargetId(), result.getTargetId());
        assertEquals(entity.getSkillTreeId(), result.getSkillTreeId());
    }

    @Test
    public void testMapEntityToResponse() {
        SkillCoupling entity = new SkillCoupling();
        entity.setId(1);
        entity.setSourceId(2);
        entity.setTargetId(3);
        entity.setSkillTreeId(4);

        SkillCouplingResponse response = new SkillCouplingResponse();
        response.setId(1);
        response.setSourceId(2);
        response.setTargetId(3);

        SkillCouplingResponse result = sut.mapEntityToResponse(entity);

        assertEquals(response.getId(), result.getId());
        assertEquals(response.getSourceId(), result.getSourceId());
        assertEquals(response.getTargetId(), result.getTargetId());
    }
}
