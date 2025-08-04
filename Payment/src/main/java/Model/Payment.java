package Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Payment {
    private Long id;                // Unique payment ID
    private BigDecimal amount;      // Payment amount
    private LocalDateTime date;     // Payment date/time
    private boolean incoming;       // true if incoming from client; false if outgoing(payment to vendor/salary)
    private Category category;      // Payment category (SALARY, VENDOR_PAYMENT, CLIENT_INVOICE)
    private Status status;          // Payment status (PENDING, PROCESSING, COMPLETED)
    private String createdBy;       // Username who created the payment
    private LocalDateTime createdAt;// Timestamp of record creation

    // Enums for category and status
    public enum Category {
        SALARY, VENDOR_PAYMENT, CLIENT_INVOICE
    }

    public enum Status {
        PENDING, PROCESSING, COMPLETED
    }

    public Payment() {
    }

    public Payment(Long id, BigDecimal amount,
                   LocalDateTime date,
                   boolean incoming,
                   Category category,
                   Status status,
                   String createdBy,
                   LocalDateTime createdAt) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.incoming = incoming;
        this.category = category;
        this.status = status;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public boolean isIncoming() { return incoming; }
    public void setIncoming(boolean incoming) { this.incoming = incoming; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", amount=" + amount +
                ", date=" + date +
                ", incoming=" + incoming +
                ", category=" + category +
                ", status=" + status +
                ", createdBy='" + createdBy + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment)) return false;
        Payment payment = (Payment) o;
        return Objects.equals(id, payment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}