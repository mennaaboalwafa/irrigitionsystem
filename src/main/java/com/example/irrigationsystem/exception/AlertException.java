package com.example.irrigationsystem.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
public class AlertException extends GeneralException{
    Long correlationId;

    public AlertException(HttpStatus httpStatus, String code, String key) {
        super(httpStatus, code, key);
    }

    public Long getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(Long correlationId) {
        this.correlationId = correlationId;
    }

    @Override
    public String getMessage() {
        return  "calling Sensor Device not available with correlationId{}\n "+this.correlationId +getHttpStatus()+" "+getCode()+" "+getKey() ;
    }
}
