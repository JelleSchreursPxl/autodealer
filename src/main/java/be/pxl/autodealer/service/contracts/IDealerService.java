package be.pxl.autodealer.service.contracts;

import be.pxl.autodealer.domain.DTO.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IDealerService {
    List<DealerDto> getDealerWithAvailableCars() throws Exception;

    RentalConfirmationDto rentACar(Long dealerId, DriverRequest driverRequest) throws Exception;

    CarDto buyNewCarForRental(Long dealerId, CarDto carDto) throws Exception;
    CarDto maintenanceOfACar(Long carId, CarDto carDto) throws Exception;

    String writeCarOff(Long carId) throws Exception;

    DealerDto newDealerInTown(DealerRequest dealerRequest);
}
