package devgaf.bcradata.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;

import devgaf.bcradata.dtos.Dolar;
import devgaf.bcradata.exceptions.NoContentException;
import devgaf.bcradata.repositories.DolarRepository;
import devgaf.bcradata.services.thirdparty.DolarService;

@ExtendWith(MockitoExtension.class)
class DataServiceDolarTest {
    
    private static final java.time.LocalDate FIXED_DATE = java.time.LocalDate.of(2024, 1, 1);
    
    @Mock
    private DolarService dolarService;

    @Mock 
    private DolarRepository dolarRepository;

    @InjectMocks
    private DataService dataService;

    private Dolar createDolar(String name, double purchase, double sale) {
        Dolar dolar = new Dolar();
        dolar.setName(name);
        dolar.setPurchase(purchase);
        dolar.setSale(sale);
        dolar.setLastUpdated(FIXED_DATE);
        return dolar;
    }

    @Test
    void testGetDolarValuesEmptyList() throws Exception {
        // Arrange
        when(dolarRepository.findAll())
            .thenReturn(Collections.emptyList());
        when(dolarService.getDolarValues())
            .thenReturn(Collections.emptyList());

        // Act & Assert
        NoContentException exception = assertThrows(
            NoContentException.class,
            () -> dataService.getResponseDolar()
        );

        // Assert message
        assertEquals("No content available", exception.getMessage());

        // Verify interactions in order
        var inOrder = inOrder(dolarRepository, dolarService);
        inOrder.verify(dolarRepository).findAll();
        inOrder.verify(dolarService).getDolarValues();
        verifyNoMoreInteractions(dolarRepository, dolarService);
    }

    @Test
    void testGetDolarValuesSingleValue() throws Exception {
        // Arrange
        when(dolarRepository.findAll()).thenReturn(Collections.emptyList());
        Dolar dolar = createDolar("Oficial", 100.0, 105.0);
        when(dolarService.getDolarValues()).thenReturn(List.of(dolar));

        // Act
        List<Dolar> result = dataService.getResponseDolar();

        // Assert & Verify
        assertAll(
            () -> assertEquals(1, result.size(), "Debería retornar 1 elemento"),
            () -> assertEquals("Oficial", result.get(0).getName(), "El nombre no coincide"),
            () -> assertEquals(100.0, result.get(0).getPurchase(), 0.001, "Purchase no coincide"),
            () -> assertEquals(105.0, result.get(0).getSale(), 0.001, "Sale no coincide"),
            () -> verify(dolarRepository).findAll(),
            () -> verify(dolarService).getDolarValues(),
            () -> verify(dolarService).saveDolar(any()),
            () -> verifyNoMoreInteractions(dolarRepository, dolarService)
        );
    }

    @Test
    void testGetDolarValuesMultipleValues() throws Exception {
        // Arrange
        when(dolarRepository.findAll()).thenReturn(Collections.emptyList());
        List<Dolar> dolarList = List.of(
            createDolar("Blue", 200.0, 205.0),
            createDolar("Oficial", 100.0, 105.0)
        );
        when(dolarService.getDolarValues()).thenReturn(dolarList);

        // Act
        List<Dolar> result = dataService.getResponseDolar();

        // Assert & Verify
        assertAll(
            () -> assertEquals(2, result.size(), "Debería retornar 2 elementos"),
            () -> assertEquals("Blue", result.get(0).getName(), "First element should be Blue"),
            () -> assertEquals("Oficial", result.get(1).getName(), "Second element should be Oficial"),
            () -> verify(dolarRepository).findAll(),
            () -> verify(dolarService).getDolarValues(),
            () -> verify(dolarService, times(2)).saveDolar(any())
        );
    }
}
