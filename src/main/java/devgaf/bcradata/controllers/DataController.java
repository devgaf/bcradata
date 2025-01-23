package devgaf.bcradata.controllers;

import org.springframework.web.bind.annotation.RestController;

import devgaf.bcradata.services.DataService;
import devgaf.bcradata.exceptions.SSLConfigurationException;
import devgaf.bcradata.models.Dolar;
import devgaf.bcradata.models.Icl;
import devgaf.bcradata.exceptions.NoContentException;
import devgaf.bcradata.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class DataController {
    private final DataService dataService;

    @PostMapping("/bcra-data-icl-from-date")
    public ResponseEntity<ApiResponse<List<Icl>>> getBcraDataIclFromDate(@RequestBody Map<String, String> dateRange) {
        try {
            String dateIni = dateRange.get("dateIni");
            String dateEnd = dateRange.get("dateEnd");
            List<Icl> data = dataService.getResponseBcraIclFromDate(dateIni, dateEnd);
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "Success", data), HttpStatus.OK);
        } catch (SSLConfigurationException e) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error de configuración SSL: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NoContentException e) {
            return new ResponseEntity<>(
                    new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "No content available: " + e.getMessage(), null),
                    HttpStatus.NO_CONTENT);
        } catch (IOException e) {
            return new ResponseEntity<>(
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "IO Error: " + e.getMessage(), null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error: " + e.getMessage(), null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/bcra-data-icl")
    public ResponseEntity<ApiResponse<List<Icl>>> getBcraDataIcl() {
        try {
            List<Icl> data = dataService.getResponseBcraIcl();
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "Success", data), HttpStatus.OK);
        } catch (SSLConfigurationException e) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error de configuración SSL: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NoContentException e) {
            return new ResponseEntity<>(
                    new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "No content available: " + e.getMessage(), null),
                    HttpStatus.NO_CONTENT);
        } catch (IOException e) {
            return new ResponseEntity<>(
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "IO Error: " + e.getMessage(), null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error: " + e.getMessage(), null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/dolar-data")
    public ResponseEntity<ApiResponse<List<Dolar>>> getDolarData() {
        try {
            List<Dolar> data = dataService.getResponseDolar();
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "Success", data), HttpStatus.OK);
        } catch (SSLConfigurationException e) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error de configuración SSL: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            return new ResponseEntity<>(
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "IO Error: " + e.getMessage(), null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error: " + e.getMessage(), null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
