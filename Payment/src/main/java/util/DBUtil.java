package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
    private static final String URL = "jdbc:postgresql://127.0.0.1:5433/Payment";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Aman";

    static {
        try {
            Class.forName("org.postgresql.Driver");
            // Create tables if not exist
            try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
                st.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                        "username VARCHAR(50) PRIMARY KEY, " +
                        "password VARCHAR(255) NOT NULL, " +
                        "role VARCHAR(50) NOT NULL)");
                st.executeUpdate("CREATE TABLE IF NOT EXISTS payments (" +
                        "id SERIAL PRIMARY KEY, " +
                        "amount NUMERIC(15,2) NOT NULL, " +
                        "incoming BOOLEAN NOT NULL, " +
                        "category VARCHAR(50) NOT NULL, " +
                        "status VARCHAR(50) NOT NULL, " +
                        "created_at TIMESTAMP NOT NULL, " +
                        "created_by VARCHAR(50) REFERENCES users(username))");
                st.executeUpdate("CREATE TABLE IF NOT EXISTS audit_trail (" +
                        "id SERIAL PRIMARY KEY, " +
                        "action VARCHAR(255) NOT NULL, " +
                        "username VARCHAR(50) REFERENCES users(username), " +
                        "timestamp TIMESTAMP NOT NULL, " +
                        "details TEXT)");
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC Driver not found.", e);
        } catch (Exception e) {
            throw new RuntimeException("Error initializing database/tables", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
