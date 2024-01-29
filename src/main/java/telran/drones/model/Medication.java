package telran.drones.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name="medication")
@NoArgsConstructor
public class Medication {

    @Id
    @Column(name = "medication_code")
    private String code;
    
    @Column(name = "weigth")
    private int weight;


    public Medication(String code, int weight ) {
        this.code = code;
        this.weight= weight;
    }
}