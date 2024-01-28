package telran.drones.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name="medication")
@NoArgsConstructor
public class Medication {

    @Id
    @Column(name = "medication_code")
    private String code;


    public Medication(String code) {
        this.code = code;
    }
}