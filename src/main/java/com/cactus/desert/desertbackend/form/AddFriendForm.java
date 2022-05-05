package com.cactus.desert.desertbackend.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author EvanLuo42
 * @date 5/3/22 10:53 AM
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AddFriendForm extends Form {
    @NotNull
    private Long friendId;
}
