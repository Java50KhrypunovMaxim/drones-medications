package telran.drones.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import telran.drones.dto.ModelType;
import telran.drones.model.*;

public interface DronesModelRepo extends JpaRepository<DroneModel, ModelType>{

}
