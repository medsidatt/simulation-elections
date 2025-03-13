package dao;

import model.Electeur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ElecteurDAO implements GenericDAO<Electeur, Integer> {
    private final Connection connection;

    public ElecteurDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Electeur find(Integer id) {
        String sql = "SELECT * FROM electeurs WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Electeur electeur = new Electeur(rs.getString("numero_identification"), rs.getString("nom"), rs.getString("ville"));
                electeur.setId(rs.getInt("id"));
                electeur.setAVote(rs.getInt("a_vote") == 1);
                return electeur;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Electeur> findAll() {
        List<Electeur> electeurs = new ArrayList<>();
        String sql = "SELECT * FROM electeurs";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Electeur electeur = new Electeur(rs.getString("numero_identification"), rs.getString("nom"), rs.getString("ville"));
                electeur.setId(rs.getInt("id"));
                electeur.setAVote(rs.getInt("a_vote") == 1);
                electeurs.add(electeur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return electeurs;
    }

    @Override
    public void save(Electeur electeur) {
        String sql = "INSERT INTO electeurs (numero_identification, nom, ville, a_vote) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, electeur.getNumeroIdentification());
            stmt.setString(2, electeur.getNom());
            stmt.setString(3, electeur.getVille());
            stmt.setInt(4, electeur.isAVote() ? 1 : 0);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                electeur.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Electeur electeur) {
        String sql = "UPDATE electeurs SET numero_identification = ?, nom = ?, ville = ?, a_vote = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, electeur.getNumeroIdentification());
            stmt.setString(2, electeur.getNom());
            stmt.setString(3, electeur.getVille());
            stmt.setInt(4, electeur.isAVote() ? 1 : 0);
            stmt.setInt(5, electeur.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM electeurs WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean aVote(String numeroIdentification) {
        String sql = "SELECT a_vote FROM electeurs WHERE numero_identification = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, numeroIdentification);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("a_vote") == 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void marquerCommeAyantVote(String numeroIdentification) {
        String sql = "UPDATE electeurs SET a_vote = 1 WHERE numero_identification = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, numeroIdentification);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Electeur findByNumeroIdentification(String numeroIdentification) {
        String sql = "SELECT * FROM electeurs WHERE numero_identification = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, numeroIdentification);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Electeur electeur = new Electeur(rs.getString("numero_identification"), rs.getString("nom"), rs.getString("ville"));
                electeur.setId(rs.getInt("id"));
                electeur.setAVote(rs.getInt("a_vote") == 1);
                return electeur;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}