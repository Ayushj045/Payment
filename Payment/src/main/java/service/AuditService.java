package service;

import Model.AuditTrail;
import Model.Payment;
import Model.User;
import repository.AuditTrailRepository;

import java.time.LocalDateTime;

public class AuditService {
    private final AuditTrailRepository repo;
    public AuditService(AuditTrailRepository repo) { this.repo = repo; }

    public void logAction(User user, Payment payment, String action) {
        AuditTrail entry = new AuditTrail();
        entry.setAction(action);
        entry.setUsername(user.getUsername());
        entry.setTimestamp(LocalDateTime.now());
        entry.setDetails("Payment ID: " + payment.getId() + ", Amount: " + payment.getAmount());
        repo.save(entry);
    }

    public void logUserAction(User user, String action, String details) {
        AuditTrail entry = new AuditTrail();
        entry.setAction(action);
        entry.setUsername(user.getUsername());
        entry.setTimestamp(LocalDateTime.now());
        entry.setDetails(details);
        repo.save(entry);
    }
}
