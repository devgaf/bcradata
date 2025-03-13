package devgaf.ngrok;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import devgaf.ngrok.dtos.Icl;
import devgaf.ngrok.models.IclEntity;
import devgaf.ngrok.exceptions.NoContentException;
import devgaf.ngrok.repositories.IclRepository;
import devgaf.ngrok.services.thirdparty.BcraService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@MockitoSettings
class BcraServiceTest {
    @Autowired
    @InjectMocks
    BcraService bcraService;

    @Mock
    IclRepository iclRepository;

    List<Icl> historicalIcl = new ArrayList<>();
    List<Icl> historicalIclBetweenDate = new ArrayList<>();

    String pattern = "yyyy-MM-dd";
    LocalDate primerDiaMesActual = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
    LocalDate ultimoDiaMesActual = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());

    @BeforeAll
    void setUp() throws Exception {
        historicalIcl = bcraService.getResponseBcraIcl();
        historicalIclBetweenDate = bcraService.getResponseBcraIclFromDate(primerDiaMesActual.format(DateTimeFormatter.ofPattern(pattern)), ultimoDiaMesActual.format(DateTimeFormatter.ofPattern(pattern)));
    }

    @Test
    void testGetResponseBcraIcl() {
        Assertions.assertNotNull(historicalIcl);
        Assertions.assertTrue(historicalIcl.size() > 0);
    }

    @Test
    void testGetResponseBcraIclBetweenDate() {
        Assertions.assertNotNull(historicalIclBetweenDate);
        Assertions.assertTrue(historicalIclBetweenDate.size() > 0);
    }

    @Test
    void testGetResponseBcraIclBetweenDateNoContentException() {
        String primerDiaMesStr = primerDiaMesActual.plusYears(25).format(DateTimeFormatter.ofPattern(pattern));
        String ultimoDiaMesStr = ultimoDiaMesActual.plusYears(25).format(DateTimeFormatter.ofPattern(pattern));
        Assertions.assertThrows(NoContentException.class,() -> bcraService.getResponseBcraIclFromDate(primerDiaMesStr, ultimoDiaMesStr));
    }
/*
    @Test
    void testGetResponseBcraIcl_Success() throws Exception {
        // Arrange
        String jsonResponse = "{\"results\":[{\"fecha\":\"2025-03-10\",\"valor\":10.0}]}";
        Mockito.when(bcraService.getRestTemplate().getForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(jsonResponse);

        // Act
        List<Icl> result = bcraService.getResponseBcraIcl();

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(LocalDate.parse("2025-03-10"), result.get(0).getDate());
        Assertions.assertEquals(10.0, result.get(0).getMeasurement());
        Mockito.verify(iclRepository, Mockito.times(1)).saveAll(Mockito.anyList());
    }

    @Test
    void testGetResponseBcraIcl_NullResponse() {
        // Arrange
        Mockito.when(bcraService.getRestTemplate().getForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(null);

        // Act & Assert
        Assertions.assertThrows(NoContentException.class, () -> bcraService.getResponseBcraIcl());
    }

    @Test
    void testGetResponseBcraIcl_SSLConfigurationError() throws Exception {
        // Arrange
        Mockito.when(bcraService.getRestTemplate().getForObject(Mockito.anyString(), Mockito.eq(String.class))).thenThrow(new Exception("SSL error"));

        // Act & Assert
        Assertions.assertThrows(Exception.class, () -> bcraService.getResponseBcraIcl());
    }

    @Test
    void testGetResponseBcraIcl_IOException() throws Exception {
        // Arrange
        Mockito.when(bcraService.getRestTemplate().getForObject(Mockito.anyString(), Mockito.eq(String.class))).thenThrow(new Exception("IO error"));

        // Act & Assert
        Assertions.assertThrows(Exception.class, () -> bcraService.getResponseBcraIcl());
    }

    @Test
    void testGetResponseBcraIcl_NoContent() throws Exception {
        // Arrange
        String jsonResponse = "{\"results\":[]}";
        Mockito.when(bcraService.getRestTemplate().getForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(jsonResponse);

        // Act & Assert
        Assertions.assertThrows(NoContentException.class, () -> bcraService.getResponseBcraIcl());
    }

    @Test
    void testSaveIcl_NewEntity() {
        // Arrange
        IclEntity newIcl = new IclEntity(LocalDate.now(), 10.0);
        Mockito.when(iclRepository.findByDate(newIcl.getDate())).thenReturn(null);

        // Act
        bcraService.saveIcl(newIcl);

        // Assert
        Mockito.verify(iclRepository, Mockito.times(1)).save(newIcl);
    }

    @Test
    void testSaveIcl_ExistingEntityWithDifferentValue() {
        // Arrange
        LocalDate date = LocalDate.now();
        IclEntity existingIcl = new IclEntity(date, 10.0);
        IclEntity updatedIcl = new IclEntity(date, 20.0);
        Mockito.when(iclRepository.findByDate(date)).thenReturn(existingIcl);

        // Act
        bcraService.saveIcl(updatedIcl);

        // Assert
        Mockito.verify(existingIcl, Mockito.times(1)).setMeasurement(updatedIcl.getMeasurement());
        Mockito.verify(iclRepository, Mockito.times(1)).save(existingIcl);
    }

    @Test
    void testSaveIcl_ExistingEntityWithSameValue() {
        // Arrange
        LocalDate date = LocalDate.now();
        IclEntity existingIcl = new IclEntity(date, 10.0);
        IclEntity sameIcl = new IclEntity(date, 10.0);
        Mockito.when(iclRepository.findByDate(date)).thenReturn(existingIcl);

        // Act
        bcraService.saveIcl(sameIcl);

        // Assert
        Mockito.verify(iclRepository, Mockito.never()).save(Mockito.any());
    }
    */
}
