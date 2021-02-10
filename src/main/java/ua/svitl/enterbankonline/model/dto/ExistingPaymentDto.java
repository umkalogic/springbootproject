package ua.svitl.enterbankonline.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExistingPaymentDto {
    private int paymentId;
    @Length(min = 14, message = "{bank.account.number.format}")
    @NotEmpty(message = "{bank.account.number.notnull}")
    private String toBankAccount;
    @DecimalMin(value = "0.01", message = "{payment.payment.amount}")
    private Double paymentAmount;

}
