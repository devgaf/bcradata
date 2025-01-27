package devgaf.bcradata.collections;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import devgaf.bcradata.models.Icl;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IclCollection {
    private List<Icl> iclList= new ArrayList<>();

    /**
     * Setea la coleccion iclList con la lista de Icl pasada por parametro.
     * 
     * @param iclList lista de Icl a setear en la coleccion
     */
    public void setIclCollectionFromBCRA(List<Icl> iclList) {
        this.iclList = iclList;
    }

    /**
     * Ordena la lista iclList por la fecha de los Icl. Los mas antiguos primero.
     */
    public void sortIclListByDate() {
        iclList.sort((Icl icl1, Icl icl2) -> icl1.getDate().compareTo(icl2.getDate()));
    }

    /**
     * Devuelve la lista de Icl que se encuentra en la coleccion. Si no se ha
     * llamado a setIclCollectionFromBCRA() esta lista estara vacia.
     * 
     * @return lista de Icl
     */
    public List<Icl> getIclList() {
        return iclList;
    }

    /**
     * Devuelve un objeto de tipo Icl que coincida con la fecha pasada por parametro
     * 
     * @param date fecha a buscar en el formato yyyy-MM-dd
     * @return objeto Icl que coincide con la fecha pasada por parametro o null si
     *         no se encuentra.
     */
    public Icl getIclByDate(String date) {
        Icl response = null;
        for (Icl icl : iclList) {
            if (icl.getDate().equals(LocalDate.parse(date))) {
                response = icl;
            }
        }
        return response;
    }
}
