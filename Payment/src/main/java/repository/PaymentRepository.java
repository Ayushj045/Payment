package repository;

import Model.Payment;
import util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class PaymentRepository {

    public void save(Payment payment) {
        String sql = "INSERT INTO payments (amount, incoming, category, status, created_at, created_by) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setBigDecimal(1, payment.getAmount());
            ps.setBoolean(2, payment.isIncoming());
            ps.setString(3, payment.getCategory().name());
            ps.setString(4, payment.getStatus().name());
            ps.setTimestamp(5, Timestamp.valueOf(payment.getDate()));
            ps.setString(6, payment.getCreatedBy());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    payment.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving payment", e);
        }
    }

    public void update(Payment payment) {
        String sql = "UPDATE payments SET amount = ?, incoming = ?, category = ?, status = ?, created_at = ?, created_by = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, payment.getAmount());
            ps.setBoolean(2, payment.isIncoming());
            ps.setString(3, payment.getCategory().name());
            ps.setString(4, payment.getStatus().name());
            ps.setTimestamp(5, Timestamp.valueOf(payment.getDate()));
            ps.setString(6, payment.getCreatedBy());
            ps.setLong(7, payment.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating payment", e);
        }
    }

    public Payment findById(Long id) {
        String sql = "SELECT id, amount, incoming, category, status, created_at, created_by FROM payments WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPayment(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding payment", e);
        }
        return null;
    }

    public List<Payment> findAll() {
        String sql = "SELECT id, amount, incoming, category, status, created_at, created_by FROM payments ORDER BY created_at DESC";
        List<Payment> payments = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all payments", e);
        }
        return payments;
    }

    public List<Payment> findAllByDateBetween(LocalDateTime from, LocalDateTime to) {
        String sql = "SELECT id, amount, incoming, category, status, created_at, created_by FROM payments WHERE created_at BETWEEN ? AND ? ORDER BY created_at DESC";
        List<Payment> payments = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(from));
            ps.setTimestamp(2, Timestamp.valueOf(to));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    payments.add(mapResultSetToPayment(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding payments by date range", e);
        }
        return payments;
    }

    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setId(rs.getLong("id"));
        payment.setAmount(rs.getBigDecimal("amount"));
        payment.setIncoming(rs.getBoolean("incoming"));
        payment.setCategory(Payment.Category.valueOf(rs.getString("category")));
        payment.setStatus(Payment.Status.valueOf(rs.getString("status")));
        payment.setDate(rs.getTimestamp("created_at").toLocalDateTime());
        payment.setCreatedBy(rs.getString("created_by"));
        return payment;
    }
}