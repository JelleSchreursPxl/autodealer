package be.pxl.autodealer.domain;

import be.pxl.autodealer.domain.DTO.CarDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Dealer dealer;

    private String brand;
    private boolean isOccupied;

    @OneToOne(cascade = {CascadeType.ALL})
    private Driver driver;

}