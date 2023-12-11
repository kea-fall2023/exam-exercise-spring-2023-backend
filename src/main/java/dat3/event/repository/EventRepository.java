package dat3.event.repository;

import dat3.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

  List<Event> findByName(String name);
  List<Event> findByNameContainingIgnoreCase(String name);
}
