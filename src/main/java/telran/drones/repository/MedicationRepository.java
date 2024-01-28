package telran.drones.repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import telran.drones.model.Medication;

@Repository
public interface MedicationRepository extends JpaRepository <Medication, String> {
    Optional<Medication> findByCode(String code);
}
