package be.pxl.autodealer.service;

import be.pxl.autodealer.domain.Car;
import be.pxl.autodealer.domain.DTO.*;
import be.pxl.autodealer.domain.Dealer;
import be.pxl.autodealer.domain.Driver;
import be.pxl.autodealer.repository.CarRepository;
import be.pxl.autodealer.repository.DealerRepository;
import be.pxl.autodealer.service.contracts.IDealerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class DealerService implements IDealerService {
    private final DealerRepository dealerRepository;
    private final CarRepository carRepository;
    private final Random RANDOM = new Random();

    @Override
    @Transactional(readOnly = true)
    public List<DealerDto> getDealerWithAvailableCars() throws Exception {
        List<Dealer> dealers = dealerRepository.findAll();

        if(dealers.isEmpty()) {
            throw new Exception("No car dealers have been found");
        } else {
            List<DealerDto> dealerDtos = new ArrayList<>();
            for (Dealer dealer : dealers ) {
                DealerDto dealerDto = new DealerDto();
                List<CarDto> carDtos = new ArrayList<>();

                for (var car : dealer.getCars()) {
                    if(!car.isOccupied()){
                        carDtos.add(new CarDto(car.getBrand()));
                    }
                }

                dealerDto.setName(dealer.getName());
                dealerDto.setCars(carDtos);

                dealerDtos.add(dealerDto);
            }
            return dealerDtos;
        }
    }

    @Override
    public RentalConfirmationDto rentACar(Long dealerId, DriverRequest driverRequest) throws Exception {
        Dealer dealer = find_dealerById(dealerId);

        // lijst van beschikbare wagens
        List<Car> availableCars = dealer.getCars().stream().filter(c -> !c.isOccupied()).toList();
        if(availableCars.isEmpty()){
            throw new Exception("No more cars available at the moment");
        }
        Driver newDriver = createDriver(driverRequest);
        Car carByRandomChoice = getRandomCar(availableCars);

        carByRandomChoice.setOccupied(true);
        carByRandomChoice.setDriver(newDriver);

        return new RentalConfirmationDto(dealer.getName(), carByRandomChoice.getBrand(), newDriver.getName());
    }

    private Dealer find_dealerById(Long dealerId) throws Exception {
        return dealerRepository.findById(dealerId).orElseThrow(() -> new Exception("No rental dealer found"));
    }

    @Override
    public CarDto buyNewCarForRental(Long dealerId, CarDto carDto) throws Exception {
        Dealer dealer = find_dealerById(dealerId);
        Car newCar = Car.builder().brand(carDto.getBrand()).isOccupied(false).dealer(dealer).build();
        if(!dealer.addCarToCarDealerList(newCar)){
            throw new Exception("Car is already in the dealership");
        }
        return carDto;
    }

    @Override
    public CarDto maintenanceOfACar(Long carId, CarDto carDto) throws Exception {
        Car car = carRepository.findById(carId).orElseThrow(() -> new Exception("No car has been found"));
        car.setBrand(carDto.getBrand());
        return carDto;
    }

    @Override
    public String writeCarOff(Long carId) throws Exception {
        Car car = carRepository.findById(carId).orElseThrow(() -> new Exception("Car not found"));
        if(!car.isOccupied()){
            return "Car is still in usage";
        }
        carRepository.delete(car);
        return MessageFormat.format("The car with brand: {0}, has been written off.", car.getBrand());
    }

    @Override
    public DealerDto newDealerInTown(DealerRequest dealerRequest) {
        Dealer newDealer = Dealer.builder().name(dealerRequest.getName()).cars(new ArrayList<>()).build();
        dealerRepository.save(newDealer);
        return new DealerDto(newDealer.getName(), new ArrayList<>());
    }

    private Driver createDriver(DriverRequest driverRequest) {
        Driver newDriver = new Driver();
        newDriver.setName(driverRequest.getName());
        newDriver.setNumberOfLicense(driverRequest.getLicenseNumber());
        return newDriver;
    }

    private Car getRandomCar(List<Car> cars){
        // random wagen uit de lijst van beschikbare wagens
        return cars.get(RANDOM.nextInt(cars.size()));

        // return cars.stream().filter(c -> !c.isOccupied() && c.getDriver() == null).toList().get(RANDOM.nextInt(cars.size()));
        // return cars.get(cars.size());
    }
}
