package dat3.event.repository;

import dat3.event.entity.Event;
import dat3.event.entity.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class EventRepositoryTest {

  @Autowired
  EventRepository eventRepository;

  @Autowired
  LocationRepository locationRepository;

  private boolean setUpIsDone = false;

  @BeforeEach
  void setUp() {
    if (setUpIsDone) {
      return;
    }
    Location location = new Location("Test location", 100);
    LocalDateTime date = LocalDateTime.of(2025,5,1,20,0);
    List<Event> events = new ArrayList<>();
    events.add(new Event("Event A", date,"Description", location.getCapacity()));
    events.add(new Event("Event A", date.plusDays(1),"Description", location.getCapacity()));
    events.add(new Event("An event WITH this name", date.plusDays(0),"Description", location.getCapacity()));
    locationRepository.save(location);
    eventRepository.saveAll(events);
    setUpIsDone = true;
  }

  @Test
  void findByName() {
    List<Event> events = eventRepository.findByName("Event A");
    assertEquals(2, events.size(),"Should find 2 events with name 'Event A'");
  }

  @Test
  void findByNameContainingIgnoreCase() {
    List<Event> events = eventRepository.findByNameContainingIgnoreCase("event with");
    assertEquals(1, events.size(),"Should find 1 event");
    assertEquals("An event WITH this name", events.get(0).getName(),"Should find 1 events with name 'An event WITH this name'");
  }
}