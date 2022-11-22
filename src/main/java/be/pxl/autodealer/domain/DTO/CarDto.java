package be.pxl.autodealer.domain.DTO;

import be.pxl.autodealer.domain.Car;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarDto implements Serializable {
    @NotNull
    @NotEmpty
    @NotBlank
    private String brand;
}