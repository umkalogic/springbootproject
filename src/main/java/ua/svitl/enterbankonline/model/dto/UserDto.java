package ua.svitl.enterbankonline.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import ua.svitl.enterbankonline.model.validation.groups.BasicUserInfo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {
    private int userId;
    @Length(groups = BasicUserInfo.class, min = 4, message = "{user.user.name.min}")
    @NotEmpty(groups = BasicUserInfo.class, message = "{user.user.name}")
    private String userName;
    @Email(groups = BasicUserInfo.class, message = "{valid.email}")
    @NotEmpty(groups = BasicUserInfo.class, message = "{email.not.empty}")
    private String email;
    private Boolean isActive;
}
