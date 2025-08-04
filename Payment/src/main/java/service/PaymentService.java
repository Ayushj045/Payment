package service;

import Model.Payment;
import Model.User;
import repository.PaymentRepository;

import java.time.LocalDateTime;

public class PaymentService {
    private PaymentRepository paymentRepository;
    private AuditService auditService;

    public PaymentService(PaymentRepository paymentRepository, AuditService auditService) {
        this.paymentRepository = paymentRepository;
        this.auditService = auditService;
    }

    public Payment addPayment(dto.PaymentDTO dto, User user) {
        Payment payment = new Payment();
        payment.setAmount(dto.amount);
        payment.setDate(LocalDateTime.now());
        payment.setIncoming(dto.incoming);
        payment.setCategory(Payment.Category.valueOf(dto.category.name()));
        payment.setStatus(Payment.Status.PENDING);
        payment.setCreatedBy(user.getUsername());
        paymentRepository.save(payment);
        auditService.logAction(user, payment, "ADD_PAYMENT");
        return payment;
    }

    public Payment updateStatus(Long paymentId, Payment.Status newStatus, User user) {
        Payment payment = paymentRepository.findById(paymentId);
        if (payment == null) throw new IllegalArgumentException("Payment not found");
        if (user.getRole() == User.Role.VIEWER)
            throw new SecurityException("Access denied. Viewers cannot update payment status.");

        Payment.Status oldStatus = payment.getStatus();
        payment.setStatus(newStatus);
        paymentRepository.update(payment);
        auditService.logAction(user, payment, "UPDATE_STATUS: " + oldStatus + " -> " + newStatus);
        return payment;
    }

    public java.util.List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}
