package com.cactus.desert.desertbackend;

import lombok.Data;

/**
 * @author EvanLuo42
 * @date 4/28/22 4:51 PM
 */
@Data
public class Result {
    private Status status;
    private String message;
    private Object data;

    public enum Status {
        SUCCESS, ERROR
    }
}
