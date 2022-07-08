package nl.han.project.skilltree.domain.impl.mapper;

import nl.han.project.skilltree.domain.mapper.Mapper;
import nl.han.project.skilltree.domain.skill.data.Skill;
import nl.han.project.skilltree.domain.skill.presentation.dto.SkillRequest;
import nl.han.project.skilltree.domain.skill.presentation.dto.SkillResponse;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SkillMapperImpl implements Mapper<SkillRequest, Skill, SkillResponse> {

    @Override
    public Skill mapRequestToEntity(SkillRequest req, Object... args) {
        Skill entity = new Skill();
        entity.setName(req.getName());
        entity.setDescription(req.getDescription());
        entity.setAssessmentCriteria(req.getAssessmentCriteria());
        entity.setEvidenceRequired(req.isEvidenceRequired());
        entity.setSkillTreeId(req.getSkillTreeId());
        entity.setX(req.getX());
        entity.setY(req.getY());

        if(args.length == 1) {
            entity.setId((Integer) args[0]);
        }
        return entity;
    }

    @Override
    public Skill mapResultSetToEntity(ResultSet rs) throws SQLException {
        Skill entity = new Skill();
        entity.setId(rs.getInt("Id"));
        entity.setCreatedOn(rs.getTimestamp("CreatedOn"));
        entity.setName(rs.getString("Name"));
        entity.setDescription(rs.getString("Description"));
        entity.setAssessmentCriteria(rs.getString("AssessmentCriteria"));
        entity.setEvidenceRequired(rs.getBoolean("EvidenceRequired"));
        entity.setSkillTreeId(rs.getInt("SkillTreeId"));
        entity.setX(rs.getInt("PositionX"));
        entity.setY(rs.getInt("PositionY"));
        return entity;
    }

    @Override
    public SkillResponse mapEntityToResponse(Skill entity, Object... args) {
        SkillResponse response = new SkillResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setAssessmentCriteria(entity.getAssessmentCriteria());
        response.setEvidenceRequired(entity.isEvidenceRequired());
        response.setSkillTreeId(entity.getSkillTreeId());
        response.setX(entity.getX());
        response.setY(entity.getY());
        return response;
    }
}
