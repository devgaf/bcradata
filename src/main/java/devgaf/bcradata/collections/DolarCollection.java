package devgaf.bcradata.collections;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import devgaf.bcradata.exceptions.SSLConfigurationException;
import devgaf.bcradata.models.Dolar;
import devgaf.bcradata.services.DataService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DolarCollection {

    List<Dolar> dolarList = new ArrayList<>();
    private DataService dataService;

    /**
     * Setea la coleccion dolarList con los datos obtenidos desde DolarSi.
     * 
     * @throws SSLConfigurationException si hay un error en la configuracion SSL
     * @throws IOException               si hay un error al obtener los datos de DolarSi
     * @throws Exception                 si hay un error general
     */
    public void setDolarCollectionFromDolarApi() throws SSLConfigurationException, IOException, Exception {
        this.dolarList = dataService.getResponseDolar();
    }

    /**
     * Setea la coleccion dolarList con la lista de Dolar pasada por parametro.
     * 
     * @param dolarList lista de Dolar a setear en la coleccion
     */
    public void setDolarCollectionFromDolarApi(List<Dolar> dolarList) {
        this.dolarList = dolarList;
    }
     
    /**
     * Ordena la lista de Dolar por nombre de cada tipo de Dolar.
     */
    public void sortDolarListByName() {
        dolarList.sort((Dolar dolar1, Dolar dolar2) -> dolar1.getName().compareTo(dolar2.getName()));
    }

    public List<Dolar> getDolarList() {
        return dolarList;
    }

    /**
     * Devuelve un objeto de tipo Dolar que coincida con el nombre pasado por parametro
     * 
     * @param name nombre a buscar
     * @return objeto Dolar que coincide con el nombre pasado por parametro o null si no se encuentra.
     */
    public Dolar getDolarByName(String name) {
        Dolar response = null;
        for (Dolar dolar : dolarList) {
            if (dolar.getName().equals(name)) {
                response = dolar;
            }
        }
        return response;
    }
}
