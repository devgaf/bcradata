package devgaf.ngrok.services.thirdparty;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import devgaf.ngrok.dtos.Dolar;
import devgaf.ngrok.models.DolarEntity;
import devgaf.ngrok.exceptions.SSLConfigurationException;
import devgaf.ngrok.repositories.DolarRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio que se encarga de obtener los valores del dólar de la API de
 * DolarSi.
 */
@Slf4j
@Service
public class DolarService {

    @Value("${urlDolarapi}")
    private String urlDolarapi;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
    private final DolarRepository dolarRepository;

    /**
     * Constructor de la clase DolarService.
     * 
     * @param restTemplate    RestTemplate para realizar las solicitudes HTTP.
     * @param dolarRepository Repositorio de la entidad DolarEntity.
     */
    public DolarService(RestTemplate restTemplate, DolarRepository dolarRepository) {
        this.restTemplate = restTemplate;
        this.dolarRepository = dolarRepository;
    }


    /**
     * Guarda un valor de dólar en la base de datos. Si ya existe un registro
     * con la misma fecha de actualización y el mismo nombre, pero con valores
     * de compra o venta diferentes, actualiza esos valores en el registro
     * existente. Si no existe un registro con la misma fecha de actualización,
     * guarda el nuevo registro.
     * 
     * @param dolarEntity la entidad DolarEntity que contiene los valores del 
     *                    dólar a guardar o actualizar
     */
    public void saveDolar(DolarEntity dolarEntity) {
        DolarEntity existingDolar = dolarRepository.findByLastUpdated(dolarEntity.getLastUpdated());
        if (existingDolar != null && existingDolar.getLastUpdated().equals(dolarEntity.getLastUpdated()) && Double.compare(existingDolar.getPurchase(), dolarEntity.getPurchase()) != 0 && Double.compare(existingDolar.getSale(), dolarEntity.getSale()) != 0 && existingDolar.getName().equals(dolarEntity.getName())) {

            existingDolar.setPurchase(dolarEntity.getPurchase());
            existingDolar.setSale(dolarEntity.getSale());
            dolarRepository.save(existingDolar);

        } else if (existingDolar == null) {

            dolarRepository.save(dolarEntity);
        }
    }

    /**
     * Deserialize the response from DolarSi API into a list of Dolar objects.
     * 
     * @param response string containing the response from the API
     * @return a list of Dolar objects
     * @throws IOException if there is an error deserializing the response
     */
    private List<Dolar> dollarSerializer(String response) throws IOException {
        try {
            List<JsonNode> nodes = objectMapper.readValue(response, new TypeReference<List<JsonNode>>() {
            });
            return nodes.stream().map(node -> {
                Dolar dolar = new Dolar();
                dolar.setName(node.get("nombre").asText());
                dolar.setPurchase(node.get("compra").asDouble());
                dolar.setSale(node.get("venta").asDouble());
                LocalDateTime dateTime = LocalDateTime.parse(node.get("fechaActualizacion").asText(), formatter);
                dolar.setLastUpdated(dateTime.toLocalDate());
                return dolar;
            }).toList();
        } catch (IOException e) {
            log.error("Error deserializing response: {}", e.toString());
            throw new IOException("Error deserializing response from dollarSerializer", e);
        }
    }

    /**
     * Convierte un objeto Dolar en un objeto DolarEntity.
     *
     * @param dolar el objeto Dolar a convertir
     * @return el objeto DolarEntity con los datos del objeto Dolar
     */
    private DolarEntity toEntity(Dolar dolar) {
        DolarEntity entity = new DolarEntity();
        entity.setName(dolar.getName());
        entity.setPurchase(dolar.getPurchase());
        entity.setSale(dolar.getSale());
        entity.setLastUpdated(dolar.getLastUpdated());
        return entity;
    }

    /**
     * Consulta la API de DolarSi y devuelve una lista de valores de dolares
     * 
     * @return lista de Dolar con los valores de los dolares Oficial, Blue, Bolsa,
     *         CCL, Mayorista, Cripto y Tarjeta/Turista
     * @throws SSLConfigurationException si hay un error en la configuracion SSL
     * @throws IOException               si hay un error parseando la respuesta JSON
     * @throws Exception                 si hay un error general
     */
    public List<Dolar> getDolarValues() throws SSLConfigurationException, Exception {
        String url = UriComponentsBuilder.fromUriString(urlDolarapi)
                .toUriString();
        try {
            String responseUrl = restTemplate.getForObject(url, String.class);
            if (responseUrl != null) {
                List<Dolar> dolarList = dollarSerializer(responseUrl);
                dolarRepository.saveAll(dolarList.stream().map(this::toEntity).toList());
                return dolarList;
            } else {
                throw new IOException("Response body is null");
            }
        } catch (SSLConfigurationException sslEx) {
            log.error("SSL Configuration Error: {}", sslEx.toString());
            sslEx.printStackTrace();
            throw sslEx;
        } catch (IOException ioEx) {
            log.error("IOException Error: {}", ioEx.toString());
            ioEx.printStackTrace();
            throw ioEx;
        } catch (Exception ex) {
            log.error("Error fetching data from DolarApi: {}", ex.toString());
            ex.printStackTrace();
            throw ex;
        }
    }
}
