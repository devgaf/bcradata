package devgaf.bcradata.services.thirdparty;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import devgaf.bcradata.exceptions.SSLConfigurationException;
import devgaf.bcradata.models.Icl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class BcraService {
    private static final Logger logger = LoggerFactory.getLogger(BcraService.class);

    @Value("${urlBcraPrincipalesVariables}")
    private String urlBcraPrincipalesVariables;

    @Value("${urlBcraBase}")
    private String urlBcraBase;

    @Value("${bcraVersion}")
    private String bcraVersion;

    @Value("${bcraPathBase}")
    private String bcraPathBase;

    @Value("${urlBcraFullRecords}")
    private String urlBcraFullRecords;
    

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public BcraService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Mapea una respuesta JSON del BCRA a una lista de Icl.
     * 
     * @param jsonResponse la respuesta JSON del BCRA
     * @return una lista de Icl con los datos de la respuesta JSON
     * @throws IOException si hay un error parseando la respuesta JSON
     */
    private List<Icl> mapToIclList(String jsonResponse) throws IOException {
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        JsonNode resultsNode = rootNode.get("results");
        List<Icl> iclList = new ArrayList<>();
        if (resultsNode != null && resultsNode.isArray()) {
            for (JsonNode node : resultsNode) {
                if (node != null) {
                    JsonNode dateNode = node.get("fecha");
                    JsonNode valueNode = node.get("valor");
                    if (dateNode != null && valueNode != null) {
                        Icl icl = new Icl();
                        icl.setDate(LocalDate.parse(dateNode.asText()));
                        icl.setValue(valueNode.asDouble());
                        iclList.add(icl);
                    } else {
                        logger.warn("Missing 'fecha' or 'valor' in JSON node: {}", node.toString());
                    }
                } else {
                    logger.warn("Null JSON node encountered");
                }
            }
        } else {
            logger.warn("Missing 'results' array in JSON response");
        }
        return iclList;
    }

    /**
     * Consulta el BCRA y devuelve una lista del ICL historico
     * 
     * @return lista de Icl
     * @throws SSLConfigurationException si hay un error en la configuracion SSL
     * @throws IOException si hay un error parseando la respuesta JSON
     */
    public List<Icl> getResponseBcraIcl() throws SSLConfigurationException, IOException {
        String url = UriComponentsBuilder.fromUriString(urlBcraFullRecords)
                .queryParam("limit", "0")
                .toUriString();
        try {
            String response = restTemplate.getForObject(url, String.class);
            if (response != null) {
                return mapToIclList(response);
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
            logger.error("Error fetching data from BCRA: {}", ex.toString());
            ex.printStackTrace();
            throw new RuntimeException("Error fetching data from BCRA: " + ex.getMessage(), ex);
        }
    }

    /**
     * Consulta el BCRA y devuelve una lista del ICL desde una fecha dateIni hasta una fecha dateEnd
     * 
     * @param dateIni fecha de inicio en formato "dd/MM/yyyy"
     * @param dateEnd fecha de fin en formato "dd/MM/yyyy"
     * @return lista de Icl desde dateIni hasta dateEnd
     * @throws SSLConfigurationException si hay un error en la configuracion SSL
     * @throws IOException si hay un error parseando la respuesta JSON
     */
    public List<Icl> getResponseBcraIclFromDate(String dateIni, String dateEnd) throws SSLConfigurationException, IOException {
        String url = UriComponentsBuilder.fromUriString(urlBcraFullRecords)
                .queryParam("limit", "0")
                .queryParam("desde", dateIni)
                .queryParam("hasta", dateEnd)
                .toUriString();
        try {
            String response = restTemplate.getForObject(url, String.class);
            if (response != null) {
                return mapToIclList(response);
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
            logger.error("Error fetching data from BCRA: {}", ex.toString());
            ex.printStackTrace();
            throw new RuntimeException("Error fetching data from BCRA: " + ex.getMessage(), ex);
        }
    }
}
