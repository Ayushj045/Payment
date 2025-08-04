package dto;

import Model.Category;
import Model.Payment;

import java.math.BigDecimal;

public class PaymentDTO {
    public BigDecimal amount;
    public boolean incoming;
    public Category category;
}