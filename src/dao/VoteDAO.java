package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VoteDAO {
    private final Connection connection;

    public VoteDAO(Connection connection) {
        this.connection = connection;
    }

    public void saveVote(int candidatId, int electeurId) {
        String sql = "INSERT INTO votes (candidat_id, electeur_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, candidatId);
            stmt.setInt(2, electeurId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}