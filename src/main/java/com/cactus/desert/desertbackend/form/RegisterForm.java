package com.cactus.desert.desertbackend.form;

import com.cactus.desert.desertbackend.util.I18nUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    @NotNull(message = "form.player.name.required")
    @Size(min = 3, max = 20, message = "form.player.name.length")
    private String playerName;

    @NotNull(message = "form.player.password.required")
    private String playerPassword;

    @NotNull(message = "form.player.email.require")
    @Email(message = "form.player.email.format")
    private String playerEmail;
}
