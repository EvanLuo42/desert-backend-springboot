package com.cactus.desert.desertbackend.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author EvanLuo42
 * @date 4/29/22 8:04 AM
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LoginForm extends Form {
    @NotNull(message = "form.player.name.required")
    @Size(min = 3, max = 20, message = "form.player.name.length")
    private String playerName;

    @NotNull(message = "form.player.password.required")
    private String playerPassword;
}
