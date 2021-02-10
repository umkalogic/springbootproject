package ua.svitl.enterbankonline.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "address")
public class Address {
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "address_id")
    private int addressId;

    @Basic@Column(name = "city", nullable = false)
    @NotEmpty(message = "{city.of.living}")
    private String city;

    @Basic@Column(name = "address_line1", nullable = false)
    @NotEmpty(message = "{users.address}")
    private String addressLine1;

    @Basic@Column(name = "address_line2")
    private String addressLine2;

    @Column(name = "is_registered", columnDefinition = "tinyint default 1")
    private boolean isRegistered;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", referencedColumnName = "region_id", nullable = false)
    private UkraineRegion regionByRegionId;

}
