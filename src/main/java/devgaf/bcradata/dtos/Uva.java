package devgaf.bcradata.dtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que se utiliza para almacenar los datos de la cotizacion del UVA
 * Se utiliza anotaciones de Lombok para generar los metodos getter, 
 * setter, equals, hashcode, toString(@Data), 
 * para generar un constructor sin argumentos(@NoArgsConstructor) 
 * y otro con todos los argumentos(@AllArgsConstructor)
 * 
 */

/**
 * DTO para representar los valores del UVA.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Uva {
    /**
     * Nombre del UVA.
     */
    public static final String NAME = "Unidad de Valor Adquisitivo (UVA) (en pesos -con dos decimales-, base 31.3.2016=14.05)";

    /**
     * Variable ID del UVA.
     */
    public static final String ID_VARIABLE = "31";
    private double measurement;
    private LocalDate date;
}
