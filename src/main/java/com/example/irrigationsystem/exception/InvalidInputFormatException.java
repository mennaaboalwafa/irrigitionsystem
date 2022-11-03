package com.example.irrigationsystem.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
@Getter
@Setter
public class InvalidInputFormatException  extends GeneralException{
    public InvalidInputFormatException() {
        super(HttpStatus.BAD_REQUEST,"400","input is invalid");
    }
}
