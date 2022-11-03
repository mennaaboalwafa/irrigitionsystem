package com.example.irrigationsystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Land {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long landArea;
    private String agriculturalCrop;
    private String landLocation;

    private boolean irrigated = false;

    @OneToOne(mappedBy = "land", orphanRemoval = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LandConfiguration landConfiguration;

    public Land(Long id, Long landArea, String landLocation, String agriculturalCrop, Land oldLand) {
        this.id = id;
        this.landArea = landArea != null ? landArea : oldLand.landArea;
        this.landLocation = landLocation != null && !landLocation.isEmpty() ? landLocation : oldLand.getLandLocation();
        this.agriculturalCrop = agriculturalCrop != null && !agriculturalCrop.isEmpty() ? agriculturalCrop : oldLand.agriculturalCrop;
    }

    public Land(Long landArea, String agriculturalCrop, String landLocation) {
        this.landArea = landArea;
        this.agriculturalCrop = agriculturalCrop;
        this.landLocation = landLocation;
        this.irrigated=isIrrigated();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Land land = (Land) o;
        return id != null && Objects.equals(id, land.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


    public Land updateWith(Land land, Land oldItem) {
        return new Land(
                this.id,
                land.landArea,
                land.landLocation,
                land.agriculturalCrop,
                oldItem
        );
    }

}
