package ua.svitl.enterbankonline.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bank_accounts")
public class BankAccount {
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "bank_account_id")
    private int bankAccountId;

    @Basic@Column(name = "bank_account_number", unique = true, nullable = false, length = 14,
            columnDefinition = "bigint(14) default " + EntityConstants.DEFAULT_BANK_ACCOUNT + "")
    @UniqueElements(message = "{bank.account.unique}")
    @NotEmpty(message = "{bank.account.number.format}")
    private BigInteger bankAccountNumber;

    @Basic@Column(name = "account_type", nullable = false,
            columnDefinition = "varchar(255) default '" + EntityConstants.DEFAULT_BANK_ACCOUNT_TYPE + "'")
    private String accountType;

    @Basic@Column(name = "account_amount", nullable = false,
            columnDefinition = "double(16,2) default 0.0")
    private Double accountAmount;

    @Basic@Column(name = "currency", nullable = false,
            columnDefinition = "varchar(10) default '" + EntityConstants.DEFAULT_CURRENCY + "'")
    private String currency;

    @Basic@Column(name = "is_active", nullable = false, columnDefinition = "tinyint default 1")
    private Boolean isActive;

    @Basic@Column(name = "enable_request", columnDefinition = "tinyint default 0")
    private Boolean enableRequest;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "bankAccountByBankAccountId", orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Payment> bankAccountPayments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "bankAccountByBankAccountId", orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<CreditCard> bankAccountCreditCards = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User userByUserId;
}
