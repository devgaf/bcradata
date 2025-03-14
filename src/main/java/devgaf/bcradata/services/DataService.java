package devgaf.bcradata.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import devgaf.bcradata.services.thirdparty.BcraService;
import devgaf.bcradata.services.thirdparty.DolarService;
import devgaf.bcradata.collections.DolarCollection;
import devgaf.bcradata.collections.IclCollection;
import devgaf.bcradata.dtos.Dolar;
import devgaf.bcradata.dtos.Icl;
import devgaf.bcradata.models.DolarEntity;
import devgaf.bcradata.models.IclEntity;
import devgaf.bcradata.exceptions.NoContentException;
import devgaf.bcradata.exceptions.SSLConfigurationException;
import devgaf.bcradata.repositories.IclRepository;
import devgaf.bcradata.repositories.DolarRepository;
import lombok.RequiredArgsConstructor;

/**
 * Clase que se encarga de manejar los datos de la aplicacion. Se encarga de
 * consultar los datos del BCRA y de DolarSi, y de almacenarlos en la base de
 * datos.
 */
@Service
@RequiredArgsConstructor
public class DataService {
    private final BcraService bcraService;
    private final DolarService dolarService;
    private final IclRepository iclRepository;
    private final DolarRepository dolarRepository;

    private final IclCollection iclCollection = new IclCollection();
    private final DolarCollection dolarCollection = new DolarCollection();

    /**
     * Consulta el BCRA y devuelve una lista del ICL desde una fecha dateIni hasta
     * una fecha dateEnd
     * 
     * @param dateIni fecha de inicio en formato "dd/MM/yyyy"
     * @param dateEnd fecha de fin en formato "dd/MM/yyyy"
     * @return lista de Icl desde dateIni hasta dateEnd
     * @throws SSLConfigurationException si hay un error en la configuracion SSL
     * @throws IOException               si hay un error parseando la respuesta JSON
     * @throws NoContentException        si no hay contenido disponible
     */
    public List<Icl> getResponseBcraIclFromDate(String dateIni, String dateEnd)
            throws SSLConfigurationException, IOException, NoContentException {
        try {
            List<Icl> response = bcraService.getResponseBcraIclFromDate(dateIni, dateEnd);
            if (response.isEmpty()) {
                throw new NoContentException("No content available");
            }
            return response;
        } catch (SSLConfigurationException e) {
            throw new SSLConfigurationException("Error de configuración SSL: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new IOException("Error parsing JSON response: " + e.getMessage(), e);
        }
    }

    /**
     * Consulta el BCRA y devuelve una lista del ICL historico. Si ya se ha
     * consultado
     * previamente el BCRA, devuelve la lista almacenada en la base de datos.
     * 
     * @return lista de Icl con los datos del ICL historico
     * @throws SSLConfigurationException si hay un error en la configuracion SSL
     * @throws IOException               si hay un error parseando la respuesta JSON
     */
    public List<Icl> getResponseBcraIcl() throws SSLConfigurationException, IOException {
        iclCollection.setIclCollectionFromBCRA(iclRepository.findAll().stream().map(this::toIclDto).toList());
        if (iclCollection.getIclList().isEmpty()) {
            List<Icl> iclList = bcraService.getResponseBcraIcl();
            iclList.forEach(icl -> bcraService.saveIcl(toIclEntity(icl)));
            iclCollection.setIclCollectionFromBCRA(iclList);
        }
        iclCollection.sortIclListByDate();
        return iclCollection.getIclList();
    }

    /**
     * Consulta la API de DolarSi y devuelve una lista de valores de dolares. Si ya
     * se ha consultado
     * previamente la API, devuelve la lista almacenada en la base de datos.
     * 
     * @return lista de Dolar con los valores de los dolares Oficial, Blue, Bolsa,
     *         CCL, Mayorista, Cripto y Tarjeta/Turista
          * @throws Exception 
          */
         public List<Dolar> getResponseDolar() throws Exception {
        try {
            List<Dolar> dolarList = new ArrayList<>(dolarRepository.findAll().stream().map(this::toDolarDto).toList());
            dolarCollection.setDolarCollectionFromDolarApi(dolarList);
            if (dolarCollection.getDolarList().isEmpty()) {
                dolarList = dolarService.getDolarValues();
                if (dolarList.isEmpty()) {
                    throw new NoContentException("No content available");
                }
                dolarList.forEach(dolar -> dolarService.saveDolar(toDolarEntity(dolar)));
                dolarCollection.setDolarCollectionFromDolarApi(dolarList);
            }
            dolarCollection.sortDolarListByName();
            return dolarCollection.getDolarList();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Convierte un objeto IclEntity a un objeto IclDto.
     * 
     * @param entity el objeto IclEntity a convertir
     * @return el objeto IclDto con los datos de la entidad
     */
    private Icl toIclDto(IclEntity entity) {
        Icl dto = new Icl();
        dto.setDate(entity.getDate());
        dto.setMeasurement(entity.getMeasurement());
        return dto;
    }

    /**
     * Convierte un objeto DolarEntity a un objeto DolarDto.
     * 
     * @param entity el objeto DolarEntity a convertir
     * @return el objeto DolarDto con los datos de la entidad
     */
    private Dolar toDolarDto(DolarEntity entity) {
        Dolar dto = new Dolar();
        dto.setName(entity.getName());
        dto.setPurchase(entity.getPurchase());
        dto.setSale(entity.getSale());
        dto.setLastUpdated(entity.getLastUpdated());
        return dto;
    }

    private IclEntity toIclEntity(Icl dto) {
        IclEntity entity = new IclEntity();
        entity.setDate(dto.getDate());
        entity.setMeasurement(dto.getMeasurement());
        return entity;
    }

    private DolarEntity toDolarEntity(Dolar dto) {
        DolarEntity entity = new DolarEntity();
        entity.setName(dto.getName());
        entity.setPurchase(dto.getPurchase());
        entity.setSale(dto.getSale());
        entity.setLastUpdated(dto.getLastUpdated());
        return entity;
    }
}
