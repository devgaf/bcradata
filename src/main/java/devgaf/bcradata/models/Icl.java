package devgaf.bcradata.models;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Icl {
    public static final String NAME = "Índice para Contratos de Locación";
    public static final String ID_VARIABLE = "40";
    private double value;
    private LocalDate date;
}
