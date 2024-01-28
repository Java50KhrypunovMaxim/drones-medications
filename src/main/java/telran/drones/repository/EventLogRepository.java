package telran.drones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import telran.drones.model.EventLog;

@Repository
public interface EventLogRepository extends JpaRepository<EventLog, Long> {
}