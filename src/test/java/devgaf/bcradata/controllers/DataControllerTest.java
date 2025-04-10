package devgaf.bcradata.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import devgaf.bcradata.dtos.Dolar;
import devgaf.bcradata.dtos.Icl;
import devgaf.bcradata.exceptions.GlobalExceptionHandler;
import devgaf.bcradata.exceptions.NoContentException;
import devgaf.bcradata.services.DataService;
import devgaf.bcradata.utils.Messages;

@ExtendWith(MockitoExtension.class)
class DataControllerTest {

    @Mock
    private DataService dataService;

    @Mock
    private Messages messages;

    @InjectMocks
    private DataController dataController;

    private MockMvc mockMvc;

    private static final java.time.LocalDate FIXED_DATE = java.time.LocalDate.of(2024, 1, 1);

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(dataController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
    }

    // Tests for /dolar-data endpoint
    @Nested
    class DolarDataEndpointTests {
        @Test
        void whenDataExists_thenReturnsOkWithData() throws Exception {
            // Arrange
            Dolar dolar = createTestDolar("Blue", 100.0, 105.0);
            when(dataService.getResponseDolar()).thenReturn(List.of(dolar));

            // Act & Assert
            mockMvc.perform(get("/dolar-data")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].name").value("Blue"))
                    .andExpect(jsonPath("$[0].purchase").value(100.0))
                    .andExpect(jsonPath("$[0].sale").value(105.0));
        }

        @Test
        void whenNoContent_thenReturnsNoContent() throws Exception {
            // Arrange
            when(dataService.getResponseDolar())
                .thenThrow(new NoContentException("No content available"));

            // Act & Assert
            mockMvc.perform(get("/dolar-data"))
                    .andExpect(status().isNoContent())
                    .andExpect(header().exists("Error"));
        }

        @Test
        void whenServiceFails_thenReturnsInternalError() throws Exception {
            // Arrange
            when(dataService.getResponseDolar())
                .thenThrow(new RuntimeException("Service error"));

            // Act & Assert
            mockMvc.perform(get("/dolar-data"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(header().exists("Error"));
        }
    }

    // Tests for /bcra-data-icl endpoint
    @Nested
    class BcraDataIclEndpointTests {
        @Test
        void whenDataExists_thenReturnsOkWithData() throws Exception {
            // Arrange
            Icl icl = createTestIcl(FIXED_DATE, 100.0);
            when(dataService.getResponseBcraIcl()).thenReturn(List.of(icl));

            // Act & Assert
            mockMvc.perform(get("/bcra-data-icl")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].date[0]").value(2024))
                    .andExpect(jsonPath("$[0].date[1]").value(1))
                    .andExpect(jsonPath("$[0].date[2]").value(1))
                    .andExpect(jsonPath("$[0].measurement").value(100.0));
        }

        @Test
        void whenNoContent_thenReturnsNoContent() throws Exception {
            // Arrange
            when(dataService.getResponseBcraIcl())
                .thenThrow(new NoContentException("No content available"));

            // Act & Assert
            mockMvc.perform(get("/bcra-data-icl"))
                    .andExpect(status().isNoContent())
                    .andExpect(header().exists("Error"));
        }
    }

    // Tests for /bcra-data-icl-from-date endpoint
    @Nested
    class BcraDataIclFromDateEndpointTests {
        @Test
        void whenValidDates_thenReturnsOkWithData() throws Exception {
            // Arrange
            Icl icl = createTestIcl(FIXED_DATE, 100.0);
            when(dataService.getResponseBcraIclFromDate(anyString(), anyString()))
                .thenReturn(List.of(icl));

            // Act & Assert
            mockMvc.perform(post("/bcra-data-icl-from-date")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"dateIni\":\"01/01/2024\",\"dateEnd\":\"31/01/2024\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].date[0]").value(2024))
                    .andExpect(jsonPath("$[0].date[1]").value(1))
                    .andExpect(jsonPath("$[0].date[2]").value(1))
                    .andExpect(jsonPath("$[0].measurement").value(100.0));
        }
    }

    // Helper methods
    private Dolar createTestDolar(String name, double purchase, double sale) {
        Dolar dolar = new Dolar();
        dolar.setName(name);
        dolar.setPurchase(purchase);
        dolar.setSale(sale);
        dolar.setLastUpdated(FIXED_DATE);
        return dolar;
    }

    private Icl createTestIcl(java.time.LocalDate date, double measurement) {
        Icl icl = new Icl();
        icl.setDate(date);
        icl.setMeasurement(measurement);
        return icl;
    }
}