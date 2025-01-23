package devgaf.bcradata.services.thirdparty;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import devgaf.bcradata.exceptions.SSLConfigurationException;

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

    public BcraService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getResponseBcraIcl() {
        String url = UriComponentsBuilder.fromUriString(urlBcraFullRecords)
                .queryParam("limit", "0")
                .toUriString();
        try {
            String response = restTemplate.getForObject(url, String.class);
            if (response != null) {
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
        try {
            String response = restTemplate.getForObject(url, String.class);
            if (response != null) {
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
}
