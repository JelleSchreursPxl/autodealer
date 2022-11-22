package be.pxl.autodealer.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RentalConfirmationDto {
    private String dealerName;
    private String carBrand;
    private String driverName;
}
