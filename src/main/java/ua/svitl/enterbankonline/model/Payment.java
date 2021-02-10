package ua.svitl.enterbankonline.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "payment")
public class Payment {
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "payment_id")
    private int paymentId;

    @Column(name = "payment_date", nullable = false,
        columnDefinition = "timestamp default current_timestamp")
    private LocalDateTime paymentDate = LocalDateTime.now();

    @Basic@Column(name = "to_bank_account", nullable = false, length = 14,
            columnDefinition = "varchar(14) ")
    @Length(min = 14, message = "{bank.account.number.format}")
    @NotEmpty(message = "{bank.account.number.notnull}")
    private String toBankAccount;

    @Basic@Column(name = "payment_amount", nullable = false, precision = 2,
        columnDefinition = "double(16,2)")
    @DecimalMin(value = "0.01", message = "{payment.payment.amount}")
    private Double paymentAmount;

    @Basic@Column(name = "is_sent", nullable = false,
        columnDefinition = "tinyint default 0")
    private Boolean isSent;

    @ManyToOne
    @JoinColumn(name = "bank_account_id", referencedColumnName = "bank_account_id", nullable = false)
    private BankAccount bankAccount;

}
