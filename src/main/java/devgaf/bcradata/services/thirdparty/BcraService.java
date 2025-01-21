package devgaf.bcradata.services.thirdparty;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import devgaf.bcradata.exceptions.SSLConfigurationException;

@Service
public class BcraService {
    private static final Logger logger = LoggerFactory.getLogger(BcraService.class);
    private static final ObjectMapper mapper = new ObjectMapper();

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

    @Value("${urlDolarapi}")
    private String urlDolarapi;

    private String responseBcra;
    private final RestTemplate restTemplate;

    public BcraService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getResponseBcraIcl() {
        String url = UriComponentsBuilder.fromUriString(urlBcraFullRecords)
                .queryParam("limit", "0")
                .toUriString();
        logger.info("BcraService linea 141 url: {}", url);
        try {
            String response = restTemplate.getForObject(url, String.class);
            if (response != null) {
                logger.info("BcraService linea 171: Response responseData: {}", response);
                return response;
            } else {
                throw new IOException("Response body is null");
            }
        } catch (SSLConfigurationException sslEx) {
            logger.error("SSL Configuration Error: {}", sslEx.toString());
            return null;
        } catch (IOException ioEx) {
            logger.error("IOException Error: {}", ioEx.toString());
            ioEx.printStackTrace();
            return null;
        } catch (Exception ex) {
            logger.error("Error fetching data from BCRA: {}", ex.toString());
            ex.printStackTrace();
            return null;
        }
    }

    public String getResponseBcraIclFromDate(String dateIni, String dateEnd) {
        String url = UriComponentsBuilder.fromUriString(urlBcraFullRecords)
                .queryParam("limit", "0")
                .queryParam("desde", dateIni)
                .queryParam("hasta", dateEnd)
                .toUriString();
        logger.info("BcraService linea 191 url: {}", url);
        try {
            String response = restTemplate.getForObject(url, String.class);
            if (response != null) {
                logger.info("BcraService linea 227: Response responseData: {}", response);
                return response;
            } else {
                throw new IOException("Response body is null");
            }
        } catch (SSLConfigurationException sslEx) {
            logger.error("SSL Configuration Error: {}", sslEx.toString());
            sslEx.printStackTrace();
            return null;
        } catch (IOException ioEx) {
            logger.error("IOException Error: {}", ioEx.toString());
            ioEx.printStackTrace();
            return null;
        } catch (Exception ex) {
            logger.error("Error fetching data from BCRA: {}", ex.toString());
            ex.printStackTrace();
            return null;
        }
    }

    public void test() {
        try {
            List<Map<String, Object>> list = mapper.readValue(responseBcra,
                    new TypeReference<List<Map<String, Object>>>() {
                    });

            logger.info("BcraService Linea 252 List: {}", list); // imprime la lista de objetos
        } catch (JsonProcessingException e) {
            logger.error("Error parsing JSON response: {}", e.getMessage());
        }
    }
}
