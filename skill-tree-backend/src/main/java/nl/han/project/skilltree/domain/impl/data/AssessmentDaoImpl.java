package nl.han.project.skilltree.domain.impl.data;

import nl.han.project.skilltree.datasource.util.DatabaseConnection;
import nl.han.project.skilltree.domain.assessment.data.Assessment;
import nl.han.project.skilltree.domain.assessment.data.AssessmentDao;
import nl.han.project.skilltree.domain.exceptions.ServerException;

import javax.enterprise.inject.Default;
import java.sql.*;

@Default
public class AssessmentDaoImpl implements AssessmentDao {
    @Override
    public int createAssessment(Assessment assessment) {
        int id = 0;
        try(Connection conn = DatabaseConnection.buildConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Assessments (Grade, Description, SkillId, UserId) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)
        ){
            stmt.setDouble(1, assessment.getGrade());
            stmt.setString(2, assessment.getDescription());
            stmt.setInt(3, assessment.getSkillId());
            stmt.setInt(4, assessment.getUserId());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getInt(1);
            }
        }catch(SQLException e){
            throw new ServerException("Error creating assessment");
        }

        return id;
    }
}
