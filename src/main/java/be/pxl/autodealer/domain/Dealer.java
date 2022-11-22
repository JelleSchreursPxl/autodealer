package be.pxl.autodealer.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "dealer")
public class Dealer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "dealer", cascade = CascadeType.ALL)
    private List<Car> cars = new ArrayList<>();

    public boolean addCarToCarDealerList(Car car){
        if(!cars.contains(car)){
            cars.add(car);
            return true;
        }
        return false;
    }

}