package Model;

import java.time.LocalDateTime;
import java.util.Objects;

public class AuditTrail {
    private Long id;
    private String action;
    private String username;
    private LocalDateTime timestamp;
    private String details;

    public AuditTrail() {
    }

    public AuditTrail(Long id, String action, String username, LocalDateTime timestamp, String details) {
        this.id = id;
        this.action = action;
        this.username = username;
        this.timestamp = timestamp;
        this.details = details;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    @Override
    public String toString() {
        return "AuditTrail{" +
                "id=" + id +
                ", action='" + action + '\'' +
                ", username='" + username + '\'' +
                ", timestamp=" + timestamp +
                ", details='" + details + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditTrail that = (AuditTrail) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}