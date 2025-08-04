package dto;

import Model.Payment;

import java.math.BigDecimal;
import java.util.List;

public class ReportDTO {
    public BigDecimal totalIncoming;
    public BigDecimal totalOutgoing;
    public List<Payment> payments;

    public ReportDTO(BigDecimal totalIncoming, BigDecimal totalOutgoing, List<Payment> payments) {
        this.totalIncoming = totalIncoming;
        this.totalOutgoing = totalOutgoing;
        this.payments = payments;
    }
}