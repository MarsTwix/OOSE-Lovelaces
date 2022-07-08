package nl.han.project.skilltree.domain.impl.mapper;

import nl.han.project.skilltree.domain.mapper.Mapper;
import nl.han.project.skilltree.domain.skill.data.Skill;
import nl.han.project.skilltree.domain.skill.data.SkillCoupling;
import nl.han.project.skilltree.domain.skilltree.data.SkillTree;
import nl.han.project.skilltree.domain.skilltree.presentation.dto.SkillTreeRequest;
import nl.han.project.skilltree.domain.skilltree.presentation.dto.SkillTreeResponse;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SkillTreeMapperImpl implements Mapper<SkillTreeRequest, SkillTree, SkillTreeResponse> {
    @Inject
    private SkillMapperImpl skillMapper;

    @Inject
    private SkillCouplingMapperImpl skillCouplingMapper;

    @Override
    public SkillTree mapRequestToEntity(SkillTreeRequest req, Object... args) {
        SkillTree entity = new SkillTree();
        entity.setName(req.getName());

        if(args.length == 1) {
            entity.setId((Integer) args[0]);
        }
        return entity;
    }

    @Override
    public SkillTree mapResultSetToEntity(ResultSet rs) throws SQLException {
        SkillTree entity = new SkillTree();
        entity.setId(rs.getInt("Id"));
        entity.setName(rs.getString("Name"));
        entity.setCreatedOn(rs.getTimestamp("CreatedOn"));
        entity.setUserId(rs.getInt("UserId"));
        entity.setArchived(rs.getBoolean("Archived"));
        return entity;
    }

    @Override
    public SkillTreeResponse mapEntityToResponse(SkillTree entity, Object... args) {
        SkillTreeResponse response =  new SkillTreeResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setUserId(entity.getUserId());
        response.setSkills(new ArrayList<>());
        response.setEdges(new ArrayList<>());

        if(entity.getSkills() != null) {
            for (Skill skill : entity.getSkills()) {
                response.getSkills().add(skillMapper.mapEntityToResponse(skill));
            }
        }

        if(entity.getEdges() != null) {
            for (SkillCoupling skillCoupling : entity.getEdges()) {
                response.getEdges().add(skillCouplingMapper.mapEntityToResponse(skillCoupling));
            }
        }
        return response;
    }
}
