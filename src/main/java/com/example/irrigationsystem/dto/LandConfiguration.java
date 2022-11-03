package com.example.irrigationsystem.dto;

import com.example.irrigationsystem.core.constants.SoilType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class LandConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;
    private  Long waterNeeded;
    private  Long availableTimeSlots;
    private SoilType soilType;
    private  Double temperature;


    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.DETACH)
    @JoinColumn(name = "land_id")
    @ToString.Exclude
    private Land land;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        LandConfiguration that = (LandConfiguration) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
