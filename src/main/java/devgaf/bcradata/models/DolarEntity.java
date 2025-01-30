package devgaf.bcradata.models;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DolarEntity {
    private String name;
    private double purchase;
    private double sale;
    private LocalDate lastUpdated;
}
