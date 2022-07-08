package nl.han.project.skilltree.domain.impl.mapper;

import nl.han.project.skilltree.domain.assessment.data.Assessment;
import nl.han.project.skilltree.domain.assessment.presentation.dto.AssessmentRequest;
import nl.han.project.skilltree.domain.assessment.presentation.dto.AssessmentResponse;
import nl.han.project.skilltree.domain.mapper.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AssessmentMapperImpl implements Mapper<AssessmentRequest, Assessment, AssessmentResponse> {
    @Override
    public Assessment mapRequestToEntity(AssessmentRequest req, Object... args) {
        return new Assessment(0, req.getGrade(), req.getDescription(), req.getSkillId(), req.getUserId());
    }

    @Override
    public Assessment mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new Assessment(
                rs.getInt("Id"),
                rs.getDouble("Grade"),
                rs.getString("Description"),
                rs.getInt("SkillId"),
                rs.getInt("UserId")
        );
    }

    @Override
    public AssessmentResponse mapEntityToResponse(Assessment entity, Object... args) {
        return new AssessmentResponse(
            entity.getId(),
            entity.getGrade(),
            entity.getDescription(),
            entity.getSkillId(),
            entity.getUserId()
        );
    }
}
