package devgaf.bcradata.controllers;

import org.springframework.web.bind.annotation.RestController;

import devgaf.bcradata.services.DataService;
import devgaf.bcradata.exceptions.SSLConfigurationException;
import devgaf.bcradata.dtos.Dolar;
import devgaf.bcradata.dtos.Icl;
import devgaf.bcradata.exceptions.NoContentException;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Map;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class DataController {
    private final DataService dataService;
    private final HttpHeaders headers = new HttpHeaders();

    {
        headers.add("Content-Type", "application/json; charset=UTF-8");
    }

    /**
     * Consulta el BCRA y devuelve una lista del ICL desde una fecha dateIni hasta una fecha dateEnd
     * 
     * @param dateRange un objeto Map con claves "dateIni" y "dateEnd" que contienen la fecha de
     *  inicio y fin en formato "dd/MM/yyyy"
     * @return una lista de Icl con los datos del ICL desde dateIni hasta dateEnd
     * @throws SSLConfigurationException si hay un error en la configuracion SSL
     * @throws NoContentException si no hay contenido disponible
     * @throws IOException si hay un error parseando la respuesta JSON
     * @throws Exception si hay un error general
     */
    @PostMapping("/bcra-data-icl-from-date")
    public ResponseEntity<List<Icl>> getBcraDataIclFromDate(@RequestBody Map<String, String> dateRange) {
        try {
            String dateIni = dateRange.get("dateIni");
            String dateEnd = dateRange.get("dateEnd");
            List<Icl> data = dataService.getResponseBcraIclFromDate(dateIni, dateEnd);
            return new ResponseEntity<>(data, headers, HttpStatus.OK);
        } catch (SSLConfigurationException e) {
            e.printStackTrace();
            headers.add("error", "SSL Configuration Error " + e.getMessage());
            return new ResponseEntity<>(null, headers, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NoContentException e) {
            e.printStackTrace();
            headers.add("error", "No content available: " + e.getMessage());
            return new ResponseEntity<>(null, headers,
                    HttpStatus.NO_CONTENT);
        } catch (IOException e) {
            e.printStackTrace();
            headers.add("error", "IO Error: " + e.getMessage());
            return new ResponseEntity<>(null, headers,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            headers.add("error", "Error general: " + e.toString());
            return new ResponseEntity<>(null, headers,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Consulta el BCRA y devuelve una lista del ICL historico
     * 
     * @return una lista de Icl con los datos del ICL historico
     * @throws SSLConfigurationException si hay un error en la configuracion SSL
     * @throws NoContentException si no hay contenido disponible
     * @throws IOException si hay un error parseando la respuesta JSON
     * @throws Exception si hay un error general
     */
    @GetMapping("/bcra-data-icl")
    public ResponseEntity<List<Icl>> getBcraDataIcl() {
        try {
            List<Icl> data = dataService.getResponseBcraIcl();
            return new ResponseEntity<>(data, headers, HttpStatus.OK);
        } catch (SSLConfigurationException e) {
            e.printStackTrace();
            headers.add("error", "SSL Configuration Error " + e.getMessage());
            return new ResponseEntity<>(null, headers, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NoContentException e) {
            e.printStackTrace();
            headers.add("error", "No content available: " + e.getMessage());
            return new ResponseEntity<>(null, headers,
                    HttpStatus.NO_CONTENT);
        } catch (IOException e) {
            e.printStackTrace();
            headers.add("error", "IO Error: " + e.getMessage());
            return new ResponseEntity<>(null, headers,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            headers.add("error", "Error general: " + e.toString());
            return new ResponseEntity<>(null, headers,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Consulta la API de DolarSi y devuelve una lista de valores de dolares
     * 
     * @return lista de Dolar con los valores de los dolares Oficial, Blue, Bolsa, CCL, Mayorista, Cripto y Tarjeta/Turista
     * @throws SSLConfigurationException si hay un error en la configuracion SSL
     * @throws IOException si hay un error parseando la respuesta JSON
     * @throws Exception si hay un error general
     */
    @GetMapping("/dolar-data")
    public ResponseEntity<List<Dolar>> getDolarData() {

        try {
            List<Dolar> data = dataService.getResponseDolar();
            return new ResponseEntity<>(data, headers, HttpStatus.OK);
        } catch (SSLConfigurationException e) {
            e.printStackTrace();
            headers.add("error", "SSL Configuration Error " + e.getMessage());
            return new ResponseEntity<>(null, headers, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            e.printStackTrace();
            headers.add("error", "IO Error: " + e.getMessage());
            return new ResponseEntity<>(null, headers,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            headers.add("error", "Error general: " + e.toString());
            return new ResponseEntity<>(null, headers,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
