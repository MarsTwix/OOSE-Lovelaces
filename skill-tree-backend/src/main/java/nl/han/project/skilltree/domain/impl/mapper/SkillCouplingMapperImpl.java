package nl.han.project.skilltree.domain.impl.mapper;

import nl.han.project.skilltree.domain.mapper.Mapper;
import nl.han.project.skilltree.domain.skill.data.SkillCoupling;
import nl.han.project.skilltree.domain.skill.presentation.dto.SkillCouplingRequest;
import nl.han.project.skilltree.domain.skill.presentation.dto.SkillCouplingResponse;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SkillCouplingMapperImpl implements Mapper<SkillCouplingRequest, SkillCoupling, SkillCouplingResponse> {
    @Override
    public SkillCoupling mapRequestToEntity(SkillCouplingRequest req, Object... args) {
        SkillCoupling entity = new SkillCoupling();
        entity.setId(req.getId());
        entity.setSourceId(req.getSourceId());
        entity.setTargetId(req.getTargetId());
        entity.setSkillTreeId(req.getSkillTreeId());
        return entity;
    }

    @Override
    public SkillCoupling mapResultSetToEntity(ResultSet rs) throws SQLException {
        SkillCoupling entity = new SkillCoupling();
        entity.setId(rs.getInt("Id"));
        entity.setSourceId(rs.getInt("SourceId"));
        entity.setTargetId(rs.getInt("TargetId"));
        entity.setSkillTreeId(rs.getInt("SkillTreeId"));
        return entity;
    }

    @Override
    public SkillCouplingResponse mapEntityToResponse(SkillCoupling entity, Object... args) {
        SkillCouplingResponse response = new SkillCouplingResponse();
        response.setId(entity.getId());
        response.setSourceId(entity.getSourceId());
        response.setTargetId(entity.getTargetId());
        return response;
    }
}
