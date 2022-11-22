package be.pxl.autodealer.controller;

import be.pxl.autodealer.domain.DTO.*;
import be.pxl.autodealer.service.contracts.IDealerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor // autowiring wordt voorzien
@Slf4j
public class DealerController {
    private final IDealerService service;

    @GetMapping("/dealers")
    public ResponseEntity<List<DealerDto>> getDealers() {
        try {
            List<DealerDto> result = service.getDealerWithAvailableCars();
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/dealers/{dealerId}")
    public ResponseEntity<RentalConfirmationDto> reserverRentalCarFromDealer(@PathVariable Long dealerId,
                                                                             @RequestBody DriverRequest driverRequest){
        try{
            RentalConfirmationDto confirmationDto = service.rentACar(dealerId, driverRequest);
            return new ResponseEntity<>(confirmationDto, HttpStatus.ACCEPTED);
        } catch(Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping( "/dealer/{dealerId}/car")
    public ResponseEntity<CarDto> newCarForRental(@PathVariable Long dealerId,
                                                  @RequestBody @Valid CarDto carDto){           // request aanmaken mag en kan ook maar DTO
                                                                                                // is hier identiek aan de vooropgestelde request
        try{
            CarDto newCar = service.buyNewCarForRental(dealerId, carDto);
            return new ResponseEntity<>(newCar, HttpStatus.CREATED);
        } catch ( Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/cars/{carId}")
    public ResponseEntity<CarDto> carMaintenance(@PathVariable Long carId,
                                                 @RequestBody CarDto carDto){
        try{
            CarDto maintainCar = service.maintenanceOfACar(carId, carDto);
            return new ResponseEntity<>(maintainCar, HttpStatus.METHOD_NOT_ALLOWED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/writtenoff/{carId}")
    public ResponseEntity<String> carIsWrittenOff(@PathVariable Long carId)
    {
        try{
            String message = service.writeCarOff(carId);
            return new ResponseEntity<>(message, HttpStatus.IM_USED);
        } catch ( Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/carDealers")
    public ResponseEntity<DealerDto> newDealer(@RequestBody DealerRequest dealerRequest)
    {
        try{
            DealerDto dealer = service.newDealerInTown(dealerRequest);
            return new ResponseEntity<>(dealer, HttpStatus.CREATED);
        } catch ( Exception e ) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}
