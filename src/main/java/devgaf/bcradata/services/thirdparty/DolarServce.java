package devgaf.bcradata.services.thirdparty;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import devgaf.bcradata.exceptions.SSLConfigurationException;
import devgaf.bcradata.models.Dolar;

@Service
public class DolarServce {
    private static final Logger logger = LoggerFactory.getLogger(DolarServce.class);

    @Value("${urlDolarapi}")
    private String urlDolarapi;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    public DolarServce(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

/**
 * Deserializa una respuesta JSON que contiene información sobre el dólar a una lista de objetos Dolar.
 *
 * @param response la respuesta JSON en forma de cadena que contiene los datos del dólar.
 * @return una lista de objetos Dolar con los valores deserializados del JSON.
 * @throws IOException si ocurre un error al deserializar la respuesta JSON.
 */

    private List<Dolar> dollarSerializer(String response) throws IOException {
        try {
            List<JsonNode> nodes = objectMapper.readValue(response, new TypeReference<List<JsonNode>>() {});
            return nodes.stream().map(node -> {
                Dolar dolar = new Dolar();
                dolar.setName(node.get("nombre").asText());
                dolar.setPurchase(node.get("compra").asDouble());
                dolar.setSale(node.get("venta").asDouble());
                LocalDateTime dateTime = LocalDateTime.parse(node.get("fechaActualizacion").asText(), formatter);
                dolar.setLastUpdated(dateTime.toLocalDate());
                return dolar;
            }).collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("Error deserializing response: {}", e.toString());
            throw new IOException("Error deserializing response from dollarSerializer", e);
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
    public List<Dolar> getDolarValues() throws IOException, SSLConfigurationException, Exception {
        String url = UriComponentsBuilder.fromUriString(urlDolarapi)
                .toUriString();
        try {
            String responseUrl = restTemplate.getForObject(url, String.class);
            if (responseUrl != null) {
                return dollarSerializer(responseUrl);
            } else {
                throw new IOException("Response body is null");
            }
        } catch (SSLConfigurationException sslEx) {
            logger.error("SSL Configuration Error: {}", sslEx.toString());
            sslEx.printStackTrace();
            throw sslEx;
        } catch (IOException ioEx) {
            logger.error("IOException Error: {}", ioEx.toString());
            ioEx.printStackTrace();
            throw ioEx;
        } catch (Exception ex) {
            logger.error("Error fetching data from DolarApi: {}", ex.toString());
            ex.printStackTrace();
            throw ex;
        }
    }
}
