package devgaf.bcradata.dtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que se utiliza para almacenar los datos de la cotizacion del ICL
 * Se utiliza anotaciones de Lombok para generar los metodos getter, 
 * setter, equals, hashcode, toString(@Data), 
 * para generar un constructor sin argumentos(@NoArgsConstructor) 
 * y otro con todos los argumentos(@AllArgsConstructor)
 * 
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Icl {
    public static final String NAME = "Índice para Contratos de Locación";
    public static final String ID_VARIABLE = "40";
    private double value;
    private LocalDate date;
}
