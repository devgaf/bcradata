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

    /**
     * Consulta el BCRA y devuelve una lista del ICL desde una fecha dateIni hasta una fecha dateEnd
     * 
     * @param dateRange un objeto Map con dos claves: "dateIni" y "dateEnd", que representan la fecha 
     *                  de inicio y fin de la consulta respectivamente, en formato "dd/MM/yyyy"
     * @return lista de Icl desde dateIni hasta dateEnd
     * @throws SSLConfigurationException si hay un error en la configuracion SSL
     * @throws IOException                si hay un error al obtener los datos del BCRA
     */
    @PostMapping("/bcra-data-icl-from-date")
    public ResponseEntity<ApiResponse<List<Icl>>> getBcraDataIclFromDate(@RequestBody Map<String, String> dateRange) {
        try {
            String dateIni = dateRange.get("dateIni");
            String dateEnd = dateRange.get("dateEnd");
            List<Icl> data = dataService.getResponseBcraIclFromDate(dateIni, dateEnd);
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "Success", data), HttpStatus.OK);
        } catch (SSLConfigurationException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error de configuración SSL: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NoContentException e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "No content available: " + e.getMessage(), null),
                    HttpStatus.NO_CONTENT);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "IO Error: " + e.getMessage(), null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error: " + e.getMessage(), null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Consulta el BCRA y devuelve una lista del ICL historico
     * 
     * @return lista de Icl
     * @throws SSLConfigurationException si hay un error en la configuracion SSL
     * @throws IOException                si hay un error al obtener los datos del BCRA
     */
    @GetMapping("/bcra-data-icl")
    public ResponseEntity<ApiResponse<List<Icl>>> getBcraDataIcl() {
        try {
            List<Icl> data = dataService.getResponseBcraIcl();
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "Success", data), HttpStatus.OK);
        } catch (SSLConfigurationException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error de configuración SSL: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NoContentException e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "No content available: " + e.getMessage(), null),
                    HttpStatus.NO_CONTENT);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "IO Error: " + e.getMessage(), null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error: " + e.getMessage(), null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Consulta la API de DolarSi y devuelve una lista de valores de dolares
     * 
     * @return lista de Dolar con los valores de los dolares Oficial, Blue, Bolsa, CCL, Mayorista, 
     *          Cripto y Tarjeta/Turista
     * @throws SSLConfigurationException si hay un error en la configuracion SSL
     * @throws IOException si hay un error al obtener los datos de DolarSi
     * @throws Exception si hay un error general
     */
    @GetMapping("/dolar-data")
    public ResponseEntity<ApiResponse<List<Dolar>>> getDolarData() {
        try {
            List<Dolar> data = dataService.getResponseDolar();
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "Success", data), HttpStatus.OK);
        } catch (SSLConfigurationException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error de configuración SSL: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "IO Error: " + e.getMessage(), null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error: " + e.getMessage(), null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
