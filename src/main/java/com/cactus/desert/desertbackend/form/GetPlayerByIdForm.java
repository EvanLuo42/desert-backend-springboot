package com.cactus.desert.desertbackend.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author EvanLuo42
 * @date 4/28/22 4:42 PM
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetPlayerByIdForm extends Form {
    @NotNull(message = "form.player.name.required")
    private Long playerId;
}
