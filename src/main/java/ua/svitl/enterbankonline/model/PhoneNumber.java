package ua.svitl.enterbankonline.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "phone_number")
public class PhoneNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "phone_id")
    private int phoneId;

    @Basic
    @Column(name = "phone_country_code",
            columnDefinition = "varchar(4) default '+380' not null")
    private String phoneCountryCode;

    @Basic
    @Column(name = "phone", nullable = false, length = 9, unique = true)
    @UniqueElements(message = "{phone.number.unique}")
    @NotEmpty(message = "{phone.number.not.empty}")
    private String phone;

    @Basic
    @Column(name = "is_primary", nullable = false)
    private boolean isPrimary;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User userByUserId;

}
