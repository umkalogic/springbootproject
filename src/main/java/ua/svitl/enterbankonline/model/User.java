package ua.svitl.enterbankonline.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UniqueElements;
import ua.svitl.enterbankonline.model.validation.groups.AdvancedUserInfo;
import ua.svitl.enterbankonline.model.validation.groups.BasicUserInfo;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

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
    @NotEmpty(groups = BasicUserInfo.class, message = "{user.user.name}")
    private String userName;

    @Column(name = "password", nullable = false)
    @Length(groups = AdvancedUserInfo.class, min = 4, message = "{user.password.min}")
    @NotEmpty(groups = AdvancedUserInfo.class, message = "{user.password}")
    private String password;

    @Basic@Column(name = "email", nullable = false, unique = true)
    @Email(groups = BasicUserInfo.class, message = "{valid.email}")
    @NotEmpty(groups = BasicUserInfo.class, message = "{email.not.empty}")
    private String email;

    @Column(name = "is_active", columnDefinition = "tinyint default 1")
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "role_id", nullable = false)
    private Role roleByRoleId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id", referencedColumnName = "person_id", nullable = false)
    private Person personByPersonId;
}
