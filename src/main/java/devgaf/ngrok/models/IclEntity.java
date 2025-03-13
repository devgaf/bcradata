package devgaf.ngrok.models;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad para almacenar los datos del ICL en la tabla icl
 * Se utiliza anotaciones de Lombok para generar los metodos getter,
 * setter, equals, hashcode, toString(@Data),
 * para generar un constructor sin argumentos(@NoArgsConstructor)
 * y otro con todos los argumentos(@AllArgsConstructor)
 * 
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "icl")
public class IclEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "measurement")
    private double measurement;
    private LocalDate date;
}
