package devgaf.bcradata.services;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import devgaf.bcradata.services.thirdparty.BcraService;
import devgaf.bcradata.services.thirdparty.DolarServce;
import devgaf.bcradata.exceptions.SSLConfigurationException;
import devgaf.bcradata.models.Dolar;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DataService {
    private final BcraService bcraService;
    private final DolarServce dolarService;

    public String getResponseBcraIclFromDate(String dateIni, String dateEnd) throws SSLConfigurationException {
        try {
            return bcraService.getResponseBcraIclFromDate(dateIni, dateEnd);
        } catch (SSLConfigurationException e) {
            throw new SSLConfigurationException("Error de configuración SSL: " + e.getMessage(), e);
        }
    }

    public String getResponseBcraIcl() throws SSLConfigurationException {
        try {
            return bcraService.getResponseBcraIcl();
        } catch (SSLConfigurationException e) {
            throw new SSLConfigurationException("Error de configuración SSL: " + e.getMessage(), e);
        }
    }

    public List<Dolar> getResponseDolar() throws SSLConfigurationException, IOException, Exception {
        return dolarService.getDolarValues();
    }
}
