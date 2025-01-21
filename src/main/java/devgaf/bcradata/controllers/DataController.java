package devgaf.bcradata.controllers;

import org.springframework.web.bind.annotation.RestController;

import devgaf.bcradata.services.DataService;
import devgaf.bcradata.exceptions.SSLConfigurationException;
import devgaf.bcradata.exceptions.NoContentException;
import devgaf.bcradata.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequiredArgsConstructor
public class DataController {
    private final DataService dataService;
    private final ObjectMapper objectMapper;

    @PostMapping("/bcra-data-icl-from-date")
    public ResponseEntity<ApiResponse<Object>> getBcraDataIclFromDate(@RequestBody Map<String, String> dateRange) {
        try {
            String dateIni = dateRange.get("dateIni");
            String dateEnd = dateRange.get("dateEnd");
            String data = dataService.getResponseBcraIclFromDate(dateIni, dateEnd);
            Object jsonData = objectMapper.readValue(data, Object.class);
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "Success", jsonData), HttpStatus.OK);
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
    public ResponseEntity<ApiResponse<Object>> getBcraDataIcl() {
        try {
            String data = dataService.getResponseBcraIcl();
            Object jsonData = objectMapper.readValue(data, Object.class);
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "Success", jsonData), HttpStatus.OK);
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
}
