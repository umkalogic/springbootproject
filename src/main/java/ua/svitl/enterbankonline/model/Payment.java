package ua.svitl.enterbankonline.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payment")
public class Payment {
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "payment_id")
    private int paymentId;

    @Column(name = "payment_date", nullable = false,
        columnDefinition = "timestamp default current_timestamp")
    private LocalDateTime paymentDate;

    @Basic@Column(name = "payment_amount", nullable = false, precision = 2)
    @DecimalMin(value = "0.01", message = "{payment.payment.amount}")
    private BigDecimal paymentAmount;

    @Basic@Column(name = "is_sent", nullable = false,
        columnDefinition = "tinyint default 0")
    private Boolean isSent;

    @ManyToOne
    @JoinColumn(name = "bank_account_id", referencedColumnName = "bank_account_id", nullable = false)
    private BankAccount bankAccountByBankAccountId;

}
