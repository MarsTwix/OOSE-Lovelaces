package nl.han.project.skilltree.domain.impl.data;

import nl.han.project.skilltree.datasource.util.DatabaseConnection;
import nl.han.project.skilltree.domain.exceptions.ServerException;
import nl.han.project.skilltree.domain.impl.mapper.SkillTreeMapperImpl;
import nl.han.project.skilltree.domain.skilltree.data.SkillTree;
import nl.han.project.skilltree.domain.skilltree.data.SkillTreeDao;
import nl.han.project.skilltree.domain.user.data.User;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Default
public class SkillTreeDaoImpl implements SkillTreeDao {

    @Inject
    private SkillTreeMapperImpl mapper;

    @Override
    public int insertSkillTree(SkillTree skillTree, User user) {
        int id = 0;
        try (Connection conn = DatabaseConnection.buildConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO SkillTrees (Name, UserId) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, skillTree.getName());
            stmt.setInt(2, user.getId());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new ServerException("Error inserting skilltree");
        }
        return id;
    }

    public SkillTree getSkillTreeById(int id) {
        SkillTree skillTree = null;
        try (Connection conn = DatabaseConnection.buildConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM SkillTrees WHERE Id = ?")
        ) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                skillTree = mapper.mapResultSetToEntity(rs);
            }
        } catch (SQLException e) {
            throw new ServerException("Error getting skilltree");
        }
        return skillTree;
    }

    @Override
    public int archiveSkillTree(int id) {
        int affectedRows;
        try (Connection conn = DatabaseConnection.buildConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE SkillTrees SET Archived = 1 WHERE Id = ?")
        ) {
            stmt.setInt(1, id);
            affectedRows = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new ServerException("Error archiving skilltree");
        }

        return affectedRows;
    }

    @Override
    public int updateSkillTree(SkillTree skillTree) {
        int affectedRows;
        try (Connection conn = DatabaseConnection.buildConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE SkillTrees SET Name = ? WHERE Id = ?")
        ) {
            stmt.setString(1, skillTree.getName());
            stmt.setInt(2, skillTree.getId());
            affectedRows = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new ServerException("Error updating skilltree");
        }
        return affectedRows;
    }

    @Override
    public int deleteSkillTree(int id) {
        int affectedRows;
        try (Connection conn = DatabaseConnection.buildConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM SkillTrees WHERE Id = ? AND Archived = 1")
        ) {
            stmt.setInt(1, id);
            affectedRows = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new ServerException("Error deleting skilltree");
        }

        return affectedRows;
    }

    @Override
    public int restoreSkillTree(int id) {
        int affectedRows;
        try (Connection conn = DatabaseConnection.buildConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE SkillTrees SET Archived = 0 WHERE Id = ?")
        ) {
            stmt.setInt(1, id);
            affectedRows = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new ServerException("Error restoring skilltree");
        }

        return affectedRows;
    }

    @Override
    public List<SkillTree> getSkillTrees(boolean archived) {
        List<SkillTree> skillTrees = new ArrayList<>();
        try (Connection conn = DatabaseConnection.buildConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM SkillTrees WHERE Archived = ?")
        ) {
            stmt.setBoolean(1, archived);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                skillTrees.add(mapper.mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw new ServerException("Error getting skilltrees");
        }
        return skillTrees;
    }
}
