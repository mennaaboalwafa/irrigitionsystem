package com.example.irrigationsystem.model;

import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class SensorDevice {

    private Long id;

    private Boolean isAvailable=true;

    private String correlationId;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SensorDevice that = (SensorDevice) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
