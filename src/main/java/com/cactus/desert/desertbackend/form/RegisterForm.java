package com.cactus.desert.desertbackend.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author EvanLuo42
 * @date 4/24/22 12:40 PM
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RegisterForm extends Form {
    @NotNull(message = "Player name could not be empty.")
    @Size(min = 3, max = 20, message = "Player name need to be between 3 and 20 characters.")
    private String playerName;

    @NotNull(message = "Password could not be empty.")
    private String playerPassword;

    @NotNull(message = "Email could not be empty.")
    @Email(message = "Need to be an email address")
    private String playerEmail;
}
