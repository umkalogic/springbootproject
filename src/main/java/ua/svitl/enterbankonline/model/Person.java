package ua.svitl.enterbankonline.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import ua.svitl.enterbankonline.model.validation.groups.AdvancedUserInfo;
import ua.svitl.enterbankonline.model.validation.groups.BasicUserInfo;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "person_data")
public class Person {
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "person_id")
    private int personId;

    @Basic
    @Column(name = "id_number_tax_code", length = 10, nullable = false, unique = true,
            columnDefinition = "bigint(10)")
    @DecimalMin(groups = AdvancedUserInfo.class, value = "26000000000000", message = "{tax.number.notnull}")
    private BigInteger idNumberTaxCode;

    @Basic@Column(name = "last_name", nullable = false)
    @Length(min = 2, message = "{user.user.last.name.min}")
    @NotEmpty(groups = BasicUserInfo.class, message = "{user.last.name}")
    private String lastName;

    @Basic@Column(name = "first_name", nullable = false)
    @Length(min = 2, message = "{user.user.first.name.min}")
    @NotEmpty(groups = BasicUserInfo.class, message = "{user.first.name}")
    private String firstName;

    @Basic@Column(name = "middle_name", columnDefinition = "varchar(255) default ' '")
    private String middleName;

    @Basic@Column(name = "birth_date", nullable = false)
    @DateTimeFormat
    private LocalDateTime birthDate;

    @Basic@Column(name = "secret_word", nullable = false)
    @Length(min = 6, message = "{user.secret.word.min}")
    @NotEmpty(message = "{user.secret.word}")
    private String secretWord;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "person_addresses",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id")
    )
    private Set<Address> userAddresses;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
            mappedBy = "personByPersonId", orphanRemoval = true)
    private List<PassportData> personPassports = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
            mappedBy = "personByPersonId", orphanRemoval = true)
    private List<BankAccount> personAccounts = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
            mappedBy = "person", orphanRemoval = true)
    private List<PhoneNumber> personPhoneNumbers = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,
            mappedBy = "personByPersonId", orphanRemoval = true)
    private List<User> personUsers = new ArrayList<>();
}
