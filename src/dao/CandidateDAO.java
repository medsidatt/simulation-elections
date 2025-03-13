package dao;

import model.Candidate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CandidateDAO implements GenericDAO<Candidate, Integer> {
    private final Connection connection;

    public CandidateDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Candidate find(Integer id) {
        String sql = "SELECT * FROM candidats WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Candidate candidate = new Candidate(rs.getString("name"), rs.getString("party"));
                candidate.setId(rs.getInt("id"));
                candidate.setVotes(getVoteCount(id));
                return candidate;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Candidate> findAll() {
        List<Candidate> candidates = new ArrayList<>();
        String sql = "SELECT * FROM candidats";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Candidate candidate = new Candidate(rs.getString("name"), rs.getString("party"));
                candidate.setId(rs.getInt("id"));
                candidate.setVotes(getVoteCount(candidate.getId()));
                candidates.add(candidate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return candidates;
    }

    @Override
    public void save(Candidate candidate) {
        String sql = "INSERT INTO candidats (name, party) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, candidate.getName());
            stmt.setString(2, candidate.getParty());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                candidate.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Candidate candidate) {
        String sql = "UPDATE candidats SET name = ?, party = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, candidate.getName());
            stmt.setString(2, candidate.getParty());
            stmt.setInt(3, candidate.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM candidats WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getVoteCount(int candidateId) {
        String sql = "SELECT COUNT(*) FROM votes WHERE candidat_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, candidateId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}