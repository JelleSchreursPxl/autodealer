package be.pxl.autodealer.container;

import be.pxl.autodealer.domain.Car;
import be.pxl.autodealer.domain.DTO.CarDto;
import be.pxl.autodealer.domain.DTO.DealerRequest;
import be.pxl.autodealer.domain.Dealer;
import be.pxl.autodealer.repository.CarRepository;
import be.pxl.autodealer.repository.DealerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class ContainerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private DealerRepository dealerRepository;

    @Autowired
    private CarRepository carRepository;

    @Container
    static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8:0:18");

    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @Test
    public void newDealer() throws Exception{
        DealerRequest dealerRequest = DealerRequest.builder().name("Other Dealer").build();
        String requestString = mapper.writeValueAsString(dealerRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/carDealers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestString)).andExpect(status().isCreated());

        Dealer dealer = dealerRepository.findAll().stream().findFirst().orElse(null);

        Assertions.assertEquals(1, dealerRepository.findAll().size());
        Assertions.assertNotNull(dealer);
        Assertions.assertEquals(dealerRequest.getName(), dealer.getName());
    }

    @Test
    public void newCarBought() throws Exception{
        DealerRequest dealerRequest = DealerRequest.builder().name("Other Dealer").build();
        String requestString = mapper.writeValueAsString(dealerRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/carDealers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestString)).andExpect(status().isCreated());

        Dealer dealer = dealerRepository.findAll().stream().findFirst().orElse(null);
        CarDto carDto = CarDto.builder().brand("Lada").build();
        String carDtoRequest = mapper.writeValueAsString(carDto);

        assert dealer != null;
        mockMvc.perform(MockMvcRequestBuilders.put("/api/dealer/{dealerId}/car", dealer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(carDtoRequest)).andExpect(status().isCreated());

        Car car = carRepository.findAll().stream().findFirst().orElse(null);

        Assertions.assertNotNull(car);
        Assertions.assertNotNull(dealer);
        Assertions.assertNotNull(car.getBrand());
        Assertions.assertEquals(1, carRepository.findAll().size());
        Assertions.assertEquals(car.getBrand(), carDto.getBrand());


    }
}
