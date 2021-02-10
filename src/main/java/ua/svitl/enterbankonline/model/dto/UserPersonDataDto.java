package ua.svitl.enterbankonline.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserPersonDataDto {
    private int userId;
    private int personId;
    private String userName;
    private String userEmail;
    private String lastName;
    private String firstName;
    private String middleName;
    private LocalDateTime birthDate;
    private String phoneCountryCode;
    private String phone;
    private Boolean isActive;

}
