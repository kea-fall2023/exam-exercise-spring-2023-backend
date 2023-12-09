package dat3.event.repository;

import dat3.event.entity.EventAttendee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventAttendeeRepository extends JpaRepository<EventAttendee, Long> {
  //Long countByEventId(Long eventId);
}
