package ua.svitl.enterbankonline.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ukraine_regions")
public class UkraineRegion {
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "region_id", nullable = false, length = 3)
    private int ukraineRegionId;

    @Basic@Column(name = "ukraine_region_name", nullable = false, unique = true)
    @NotEmpty(message = "*Please provide name of the region")
    private String ukraineRegionName;

    @OneToMany(mappedBy = "regionByRegionId")
    private List<Address> addresses = new ArrayList<>();

}
