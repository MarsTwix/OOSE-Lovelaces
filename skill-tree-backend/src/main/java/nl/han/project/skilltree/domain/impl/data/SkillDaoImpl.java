package nl.han.project.skilltree.domain.impl.data;

import nl.han.project.skilltree.datasource.util.DatabaseConnection;
import nl.han.project.skilltree.domain.exceptions.ServerException;
import nl.han.project.skilltree.domain.impl.mapper.SkillCouplingMapperImpl;
import nl.han.project.skilltree.domain.impl.mapper.SkillMapperImpl;
import nl.han.project.skilltree.domain.skill.data.Skill;
import nl.han.project.skilltree.domain.skill.data.SkillCoupling;
import nl.han.project.skilltree.domain.skill.data.SkillDao;
import nl.han.project.skilltree.domain.skill.data.UserSkill;

import javax.inject.Inject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SkillDaoImpl implements SkillDao {

    @Inject
    private SkillMapperImpl skillMapper;

    @Inject
    SkillCouplingMapperImpl skillCouplingMapper;

    @Override
    public List<Skill> getSkillsForSkillTree(int id, boolean archived) {
        List<Skill> skills = new ArrayList<>();
        try (Connection conn = DatabaseConnection.buildConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Skills WHERE SkillTreeId = ? AND Archived = ? ORDER BY CreatedOn DESC")) {
            stmt.setInt(1, id);
            stmt.setBoolean(2, archived);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                skills.add(skillMapper.mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw new ServerException("Could not get skills for skill tree");
        }

        return skills;
    }

    @Override
    public Skill getSkill(int id) {
        Skill skill = null;
        try (Connection conn = DatabaseConnection.buildConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Skills WHERE Id = ?")) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                skill = skillMapper.mapResultSetToEntity(rs);
            }
        } catch (SQLException e) {
            throw new ServerException("Could not get skill");
        }

        return skill;
    }

    @Override
    public int insertSkill(Skill skill) {
        int insertedSkillId = 0;
        try (Connection conn = DatabaseConnection.buildConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO Skills (Name, Description, AssessmentCriteria, EvidenceRequired, SkillTreeId, PositionX, PositionY) VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, skill.getName());
            stmt.setString(2, skill.getDescription());
            stmt.setString(3, skill.getAssessmentCriteria());
            stmt.setBoolean(4, skill.isEvidenceRequired());
            stmt.setInt(5, skill.getSkillTreeId());
            stmt.setInt(6, skill.getX());
            stmt.setInt(7, skill.getY());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                insertedSkillId = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new ServerException("Failed to insert skill");
        }

        return insertedSkillId;
    }

    @Override
    public int archiveSkill(int id) {
        try (Connection conn = DatabaseConnection.buildConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE Skills SET Archived = 1 WHERE Id = ?;");
             PreparedStatement stmtCouplings = conn.prepareStatement("DELETE FROM SkillCouplings WHERE SourceId = ? OR TargetId = ?")
        ) {
            conn.setAutoCommit(false);

            stmt.setInt(1, id);
            stmtCouplings.setInt(1, id);
            stmtCouplings.setInt(2, id);
            int affectedRows = stmt.executeUpdate();
            stmtCouplings.execute();
            conn.commit();

            return affectedRows;
        } catch (SQLException e) {
            throw new ServerException("Failed to archive skill");
        }
    }

    @Override
    public int restoreSkill(int id) {
        try (Connection conn = DatabaseConnection.buildConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE Skills SET Archived = 0 WHERE Id = ? AND Archived = 1")
        ) {
            conn.setAutoCommit(false);
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            conn.commit();

            return affectedRows;
        } catch (SQLException e) {
            throw new ServerException("Failed to archive skill");
        }
    }

    @Override
    public int deleteSkill(int id) {
        int rowcount;
        try (Connection conn = DatabaseConnection.buildConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM Skills WHERE Id = ? AND Archived = 1")
        ) {
            stmt.setInt(1, id);
            rowcount = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new ServerException("Failed to delete skill");
        }

        return rowcount;
    }

    @Override
    public List<SkillCoupling> getSkillCouplingsForSkillTree(int id) {
        List<SkillCoupling> skillCouplings = new ArrayList<>();
        try (Connection conn = DatabaseConnection.buildConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM SkillCouplings WHERE SkillTreeId = ?")
        ) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                skillCouplings.add(skillCouplingMapper.mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw new ServerException("Could not get skill couplings for skill tree");
        }

        return skillCouplings;
    }

    @Override
    public int updateSkillPosition(Skill skill) {
        int affected;
        try (Connection conn = DatabaseConnection.buildConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE Skills SET PositionX = ?, PositionY = ? WHERE Id = ?")
        ) {
            stmt.setInt(1, skill.getX());
            stmt.setInt(2, skill.getY());
            stmt.setInt(3, skill.getId());
            affected = stmt.executeUpdate();

        } catch (SQLException e) {
            throw new ServerException("Failed to update skill position");
        }

        return affected;
    }

    @Override
    public int updateSkill(Skill skill) {
        int affected;
        try (Connection conn = DatabaseConnection.buildConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE Skills SET Name = ?, Description = ?, AssessmentCriteria = ?, EvidenceRequired = ? WHERE Id = ?")
        ) {
            stmt.setString(1, skill.getName());
            stmt.setString(2, skill.getDescription());
            stmt.setString(3, skill.getAssessmentCriteria());
            stmt.setBoolean(4, skill.isEvidenceRequired());
            stmt.setInt(5, skill.getId());
            affected = stmt.executeUpdate();

        } catch (SQLException e) {
            throw new ServerException("Failed to update skill");
        }

        return affected;
    }

    @Override
    public List<UserSkill> getSkillsForSkillTreeAndUser(int id, int studentId) {
        List<UserSkill> skills = new ArrayList<>();
        try (Connection conn = DatabaseConnection.buildConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT s.*, a.Grade FROM Skills s LEFT JOIN Assessments a ON s.Id = a.SkillId AND a.UserId = ? WHERE s.SkillTreeId = ? AND Archived = 0")
        ) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, id);
            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {
                skills.add(mapResultSetToUserSkill(rs));
            }
        } catch (SQLException e) {
            throw new ServerException("Failed to get skills for skill tree and user");
        }

        return skills;
    }

    @Override
    public int deleteSkillCoupling(int id, int skillCouplingId) {
        int affected;
        try (Connection conn = DatabaseConnection.buildConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM SkillCouplings WHERE SkillTreeId = ? AND Id = ?")
        ) {
            stmt.setInt(1, id);
            stmt.setInt(2, skillCouplingId);
            affected = stmt.executeUpdate();

        } catch (SQLException e) {
            throw new ServerException("Failed to delete skill coupling");
        }

        return affected;
    }

    @Override
    public int insertSkillCoupling(SkillCoupling skillCoupling) {
        int skillCouplingId = 0;
        try (Connection conn = DatabaseConnection.buildConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO SkillCouplings (SourceId, TargetId, SkillTreeId) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setInt(1, skillCoupling.getSourceId());
            stmt.setInt(2, skillCoupling.getTargetId());
            stmt.setInt(3, skillCoupling.getSkillTreeId());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                skillCouplingId = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new ServerException("Failed to insert skill coupling");
        }

        return skillCouplingId;
    }

    public UserSkill mapResultSetToUserSkill(ResultSet rs) throws SQLException {
        UserSkill userSkill = new UserSkill();
        userSkill.setId(rs.getInt("Id"));
        userSkill.setName(rs.getString("Name"));
        userSkill.setDescription(rs.getString("Description"));
        userSkill.setAssessmentCriteria(rs.getString("AssessmentCriteria"));
        userSkill.setEvidenceRequired(rs.getBoolean("EvidenceRequired"));
        userSkill.setSkillTreeId(rs.getInt("SkillTreeId"));
        userSkill.setX(rs.getInt("PositionX"));
        userSkill.setY(rs.getInt("PositionY"));
        userSkill.setGrade(rs.getDouble("Grade"));
        return userSkill;
    }
}
