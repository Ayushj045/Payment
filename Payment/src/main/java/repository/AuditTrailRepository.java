package repository;

import Model.AuditTrail;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuditTrailRepository {

    public void save(AuditTrail entry) {
        String sql = "INSERT INTO audit_trail (action, username, timestamp, details) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entry.getAction());
            ps.setString(2, entry.getUsername());
            ps.setTimestamp(3, Timestamp.valueOf(entry.getTimestamp()));
            ps.setString(4, entry.getDetails());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entry.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving audit trail entry", e);
        }
    }

    public List<AuditTrail> findAll() {
        String sql = "SELECT id, action, username, timestamp, details FROM audit_trail ORDER BY timestamp DESC";
        List<AuditTrail> logs = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                AuditTrail entry = new AuditTrail();
                entry.setId(rs.getLong("id"));
                entry.setAction(rs.getString("action"));
                entry.setUsername(rs.getString("username"));
                entry.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                entry.setDetails(rs.getString("details"));
                logs.add(entry);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all audit trail entries", e);
        }
        return logs;
    }

    public List<AuditTrail> findByUsername(String username) {
        String sql = "SELECT id, action, username, timestamp, details FROM audit_trail WHERE username = ? ORDER BY timestamp DESC";
        List<AuditTrail> logs = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AuditTrail entry = new AuditTrail();
                    entry.setId(rs.getLong("id"));
                    entry.setAction(rs.getString("action"));
                    entry.setUsername(rs.getString("username"));
                    entry.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                    entry.setDetails(rs.getString("details"));
                    logs.add(entry);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding audit trail entries by username", e);
        }
        return logs;
    }
}
