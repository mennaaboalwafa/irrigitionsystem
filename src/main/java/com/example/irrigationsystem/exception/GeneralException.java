package com.example.irrigationsystem.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeneralException extends RuntimeException {
    private HttpStatus httpStatus;
    private String code;
    private String key;

}
