package devgaf.ngrok.dtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Clase que se utiliza para almacenar los datos de la cotizacion del dolar
 * Se utiliza anotaciones de Lombok para generar los metodos getter, 
 * setter, equals, hashcode, toString(@Data), 
 * para generar un constructor sin argumentos(@NoArgsConstructor) 
 * y otro con todos los argumentos(@AllArgsConstructor)
 * 
 * DTO para representar los valores del dólar.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Dolar {
    private String name;
    private double purchase;
    private double sale;
    private LocalDate lastUpdated;
}
