package service;

import Model.Payment;
import dto.ReportDTO;
import repository.PaymentRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ReportService {
    private PaymentRepository paymentRepository;
    public ReportService(PaymentRepository paymentRepository) { this.paymentRepository = paymentRepository; }

    public ReportDTO generateReport(LocalDateTime from, LocalDateTime to) {
        List<Payment> payments = paymentRepository.findAllByDateBetween(from, to);
        BigDecimal incoming = payments.stream()
                .filter(p->p.isIncoming())
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal outgoing = payments.stream()
                .filter(p->!p.isIncoming())
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new ReportDTO(incoming, outgoing, payments);
    }
}