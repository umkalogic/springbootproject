package ua.svitl.enterbankonline.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id", unique = true)
    private int roleId;

    @Column(name = "role", columnDefinition = "varchar(255) default 'USER'")
    private String role;

}
