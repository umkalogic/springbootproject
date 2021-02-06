package ua.svitl.enterbankonline.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "passport_data")
public class PassportData {
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "passport_id")
    private int passportId;

    @Basic@Column(name = "passport_series", nullable = false, length = 2)
    private String passportSeries;

    @Basic@Column(name = "passport_number", nullable = false, length = 6)
    private int passportNumber;

    @Column(name = "passport_issue_date", nullable = false)
    private LocalDateTime passportIssueDate;

    @Basic@Column(name = "passport_issued_by", nullable = false)
    private String passportIssuedBy;

    @Basic@Column(name = "is_domestic", nullable = false, columnDefinition = "tinyint default 1")
    private Boolean isDomestic;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id", referencedColumnName = "person_id", nullable = false)
    private Person personByPersonId;

}
