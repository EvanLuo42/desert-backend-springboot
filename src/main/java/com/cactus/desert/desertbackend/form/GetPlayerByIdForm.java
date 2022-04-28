package com.cactus.desert.desertbackend.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * @author EvanLuo42
 * @date 4/28/22 4:42 PM
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetPlayerByIdForm extends Form {
    @NotNull
    @Positive
    private Long playerId;
}
