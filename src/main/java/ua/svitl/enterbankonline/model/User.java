package ua.svitl.enterbankonline.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.format.annotation.DateTimeFormat;
import ua.svitl.enterbankonline.model.validation.groups.AdvancedUserInfo;
import ua.svitl.enterbankonline.model.validation.groups.BasicUserInfo;

import javax.persistence.*;
import javax.validation.constraints.Email;
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
@Table(name = "users")
public class User {
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "user_name", nullable = false, unique = true)
    @Length(groups = BasicUserInfo.class, min = 4, message = "{user.user.name.min}")
    @UniqueElements(groups = BasicUserInfo.class, message = "{user.name.unique}")
    @NotEmpty(groups = BasicUserInfo.class, message = "{user.user.name}")
    private String userName;

    @Column(name = "password", nullable = false)
    @Length(groups = AdvancedUserInfo.class, min = 4, message = "{user.password.min}")
    @NotEmpty(groups = AdvancedUserInfo.class, message = "{user.password}")
    private String password;

    @Column(name = "is_active", columnDefinition = "tinyint default 1")
    private Boolean isActive = true;

    @Basic@Column(name = "id_number_tax_code", length = 10, nullable = false, unique = true,
            columnDefinition = "bigint(10)")
    @Length(groups = AdvancedUserInfo.class, min = 10, message = "{tax.number.min}")
    @NotNull(groups = AdvancedUserInfo.class, message = "{tax.number.notnull}")
    private BigInteger idNumberTaxCode;

    @Basic@Column(name = "last_name", nullable = false)
    @Length(groups = BasicUserInfo.class, min = 2, message = "{user.user.last.name.min}")
    @NotEmpty(groups = BasicUserInfo.class, message = "{user.last.name}")
    private String lastName;

    @Basic@Column(name = "first_name", nullable = false)
    @Length(groups = BasicUserInfo.class, min = 2, message = "{user.user.first.name.min}")
    @NotEmpty(groups = BasicUserInfo.class, message = "{user.first.name}")
    private String firstName;

    @Basic@Column(name = "middle_name", columnDefinition = "varchar(255) default ' '")
    private String middleName;

    @Basic@Column(name = "email", nullable = false, unique = true)
    @Email(groups = BasicUserInfo.class, message = "{valid.email}")
    @NotEmpty(groups = BasicUserInfo.class, message = "{email.not.empty}")
    private String email;

    @Basic@Column(name = "birth_date", nullable = false)
    @DateTimeFormat
    @NotNull(groups = AdvancedUserInfo.class, message = "{user.birth.date}")
    private LocalDateTime birthDate;

    @Basic@Column(name = "secret_word", nullable = false)
    @Length(groups = AdvancedUserInfo.class, min = 6, message = "{user.secret.word.min}")
    @NotEmpty(groups = AdvancedUserInfo.class, message = "{user.secret.word}")
    private String secretWord;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
            mappedBy = "userByUserId", orphanRemoval = true)
    private List<PassportData> userPassports = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
            mappedBy = "userByUserId", orphanRemoval = true)
    private List<BankAccount> userAccounts = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,
            mappedBy = "userByUserId", orphanRemoval = true)
    private List<PhoneNumber> userPhoneNumbers = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_addresses",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id")
    )
    private Set<Address> userAddresses;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

}
