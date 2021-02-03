package ua.svitl.enterbankonline.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
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

    @Column(name = "user_name", nullable = false)
    @Length(min = 6, message = "*Username must have at least 6 characters long")
    @NotEmpty(message = "*Please provide username")
    private String userName;

    @Column(name = "password", nullable = false)
    @Length(min = 6, message = "*User password must be at least 6 characters long")
    @NotEmpty(message = "*Please provide user password")
    private String password;

    @Column(name = "is_active", columnDefinition = "tinyint default 1")
    private Boolean isActive = true;

    @Basic@Column(name = "id_number_tax_code", length = 10, nullable = false, unique = true,
            columnDefinition = "bigint(10)")
    @NotEmpty(message = "*Please provide user's tax code")
    private BigInteger idNumberTaxCode;

    @Basic@Column(name = "last_name", nullable = false)
    @NotEmpty(message = "*Please provide user's last name")
    private String lastName;

    @Basic@Column(name = "first_name", nullable = false)
    @NotEmpty(message = "*Please provide user's first name")
    private String firstName;

    @Basic@Column(name = "middle_name", columnDefinition = "varchar(255) default ' '")
    private String middleName;

    @Basic@Column(name = "email", nullable = false, unique = true)
    @NotEmpty(message = "*Please provide user's email")
    private String email;

    @Basic@Column(name = "birth_date", nullable = false)
    @NotEmpty(message = "*Please provide user's birth date")
    private LocalDateTime birthDate;

    @Basic@Column(name = "secret_word", nullable = false)
    @NotEmpty(message = "*Please provide user's secret word")
    private String secretWord;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
            mappedBy = "userByUserId", orphanRemoval = true)
    private List<PassportData> userPassports = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "userByUserId", orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
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
