package telran.drones.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.drones.model.Drone;
import telran.drones.model.DroneModel;

@Repository
public interface DroneRepository extends JpaRepository<Drone, String> {

	 Optional<Drone> findByNumber(String number);
	 boolean existsByNumber(String number);
	    
	 Optional<Drone> findByModel(DroneModel model);
	 boolean existsByModel(DroneModel model);
}
