package telran.drones.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.drones.dto.State;
import telran.drones.model.Drone;
import telran.drones.model.DroneModel;
import telran.drones.projections.DroneNumber;

@Repository
public interface DronesRepo extends JpaRepository<Drone, String>{

	List<DroneNumber> findByStateAndBatteryCapacityGreaterThanEqual(State state, int capacityThreshold);
@Query("select batteryCapacity from Drone where number=:droneNumber")
	Integer findBatteryCapacity(String droneNumber);

}