package ua.svitl.enterbankonline.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "credit_card")
public class CreditCard {
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "credit_card_id")
    private int creditCardId;

    @Basic@Column(name = "card_number", nullable = false, length = 16, unique = true,
        columnDefinition = "bigint(16)")
    @NotEmpty(message = "{card.number.not.empty}")
    private BigInteger cardNumber;

    @Basic@Column(name = "issue_date", nullable = false,
        columnDefinition = "datetime default '2021-01-05 00-00-00'")
    private LocalDateTime issueDate;

    @Basic@Column(name = "expire_date", nullable = false,
            columnDefinition = "datetime default '2022-01-05 23-59-59'")
    private LocalDateTime expireDate;

    @Basic@Column(name = "is_active", nullable = false,
        columnDefinition = "tinyint default 1")
    private Boolean isActive = true;

    @Basic@Column(name = "card_name", nullable = false,
            columnDefinition = "varchar(45) default '" + EntityConstants.DEFAULT_CREDIT_CARD_NAME + "'")
    private String cardName;

    @Basic@Column(name = "owner_name", nullable = false)
    @NotEmpty(message = "{card.owner.full.name}")
    private String ownerName;

    @Basic@Column(name = "cvc2", nullable = false, length = 3)
    private String cvc2;

    @ManyToOne
    @JoinColumn(name = "bank_account_id", referencedColumnName = "bank_account_id", nullable = false)
    private BankAccount bankAccountByBankAccountId;
}
