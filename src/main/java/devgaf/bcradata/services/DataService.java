package devgaf.bcradata.services;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import devgaf.bcradata.services.thirdparty.BcraService;
import devgaf.bcradata.services.thirdparty.DolarServce;
import devgaf.bcradata.collections.DolarCollection;
import devgaf.bcradata.collections.IclCollection;
import devgaf.bcradata.dtos.Dolar;
import devgaf.bcradata.dtos.Icl;
import devgaf.bcradata.models.DolarEntity;
import devgaf.bcradata.models.IclEntity;
import devgaf.bcradata.exceptions.SSLConfigurationException;
import devgaf.bcradata.repositories.IclRepository;
import devgaf.bcradata.repositories.DolarRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DataService {
    private final BcraService bcraService;
    private final DolarServce dolarService;
    private final IclRepository iclRepository;
    private final DolarRepository dolarRepository;

    private final IclCollection iclCollection = new IclCollection();
    private final DolarCollection dolarCollection = new DolarCollection();

    /**
     * Consulta el BCRA y devuelve una lista del ICL desde una fecha dateIni hasta una fecha dateEnd
     * @param dateIni fecha de inicio en formato "dd/MM/yyyy"
     * @param dateEnd fecha de fin en formato "dd/MM/yyyy"
     * @return lista de Icl desde dateIni hasta dateEnd
     * @throws SSLConfigurationException si hay un error en la configuración SSL
     * @throws IOException si hay un error parseando la respuesta JSON
     */
    public List<Icl> getResponseBcraIclFromDate(String dateIni, String dateEnd) throws SSLConfigurationException, IOException {
        try{
            return bcraService.getResponseBcraIclFromDate(dateIni, dateEnd);
        } catch (SSLConfigurationException e) {
            throw new SSLConfigurationException("Error de configuración SSL: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new IOException("Error parsing JSON response: " + e.getMessage(), e);
        }
    }

    /**
     * Consulta el BCRA y devuelve una lista del ICL historico
     * @return lista de Icl
     * @throws SSLConfigurationException si hay un error en la configuración SSL
     * @throws IOException si hay un error parseando la respuesta JSON
     */
    public List<Icl> getResponseBcraIcl() throws SSLConfigurationException, IOException {
        iclCollection.setIclCollectionFromBCRA(iclRepository.findAll().stream().map(this::toIclDto).collect(Collectors.toList()));
        if (iclCollection.getIclList().isEmpty()) {
            iclCollection.setIclCollectionFromBCRA(bcraService.getResponseBcraIcl());
        }
        iclCollection.sortIclListByDate();
        return iclCollection.getIclList();
    }
    
/**
 * Consulta la API de DolarSi y devuelve una lista de valores de dólares.
 * 
 * Si la lista de dólares en la colección local está vacía, consulta la API
 * externa para obtener los valores actuales. La lista obtenida se ordena 
 * por el nombre de cada tipo de dólar.
 * 
 * @return lista de Dolar con los valores de los dólares Oficial, Blue, 
 *         Bolsa, CCL, Mayorista, Cripto y Tarjeta/Turista
 * @throws SSLConfigurationException si hay un error en la configuración SSL
 * @throws IOException si hay un error parseando la respuesta JSON
 * @throws Exception si hay un error general
 */

    public List<Dolar> getResponseDolar() throws SSLConfigurationException, IOException, Exception {
        dolarCollection.setDolarCollectionFromDolarApi(dolarRepository.findAll().stream().map(this::toDolarDto).collect(Collectors.toList()));
        if (dolarCollection.getDolarList().isEmpty()) {
            dolarCollection.setDolarCollectionFromDolarApi(dolarService.getDolarValues());
        }
        dolarCollection.sortDolarListByName();
        return dolarCollection.getDolarList();
    }

    private Icl toIclDto(IclEntity entity) {
        Icl dto = new Icl();
        dto.setDate(entity.getDate());
        dto.setValue(entity.getValue());
        return dto;
    }

    private Dolar toDolarDto(DolarEntity entity) {
        Dolar dto = new Dolar();
        dto.setName(entity.getName());
        dto.setPurchase(entity.getPurchase());
        dto.setSale(entity.getSale());
        dto.setLastUpdated(entity.getLastUpdated());
        return dto;
    }
}
