package dat3.event.service;

import dat3.event.dto.EventResponse;
import dat3.event.entity.Attendee;
import dat3.event.entity.Event;
import dat3.event.entity.Location;
import dat3.event.repository.AttendeeRepository;
import dat3.event.repository.EventAttendeeRepository;
import dat3.event.repository.EventRepository;
import dat3.event.repository.LocationRepository;
import dat3.security.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
Unit tests for the EventService class, using H2 to mock repositories .
 */
@DataJpaTest
class EventServiceTest_H2 {

  @Autowired
  LocationRepository locationRepository;
  @Autowired
  EventRepository eventRepository;
  @Autowired
  AttendeeRepository attendeeRepository;
  @Autowired
  EventAttendeeRepository eventAttendeeRepository;

  EventService eventService;

  private long eventId1;
  private boolean setUpIsDone = false;
  @BeforeEach
  void setUp() {
    if (setUpIsDone) {
      return;
    }
    Attendee attendee = new Attendee();
    attendee.setUsername("Kurt");
    attendee.setEmail("kurt@a.dk");
    attendee.setPhoneNumber("1234567890");
    attendee.setPassword("test12");
    attendee.addRole(Role.USER);
    attendeeRepository.save(attendee);

    Location location = new Location("Royal Arena", 100);
    LocalDateTime date = LocalDateTime.of(2025,5,1,20,0);
    Event event1 = new Event("Event A", date,"Description", location.getCapacity());
    event1.setLocation(location);
    locationRepository.save(location);
    eventId1 = eventRepository.save(event1).getId();

    eventService = new EventService(eventRepository, locationRepository, attendeeRepository, eventAttendeeRepository);

    setUpIsDone = true;
  }

  @Test
  void registerEventForAttendeeReturnsCorrectDTO() {
    EventResponse response = eventService.registerEventForAttendee(eventId1,"Kurt",false);
    assertEquals(eventId1, response.getId());
    assertEquals("Event A", response.getName());
    assertEquals("Royal Arena", response.getLocationName());
    assertEquals(100-1, response.getFreeSeats());
  }

  @Test
  void registerEventCorrectlyUpdatesEvent() {
    eventService.registerEventForAttendee(eventId1,"Kurt",false);
    eventService.registerEventForAttendee(eventId1,"Kurt",false);
    eventService.registerEventForAttendee(eventId1,"Kurt",false);

    Event event = eventRepository.findById(eventId1).get();
    int freeSeats = event.getCapacity() -event.getEventAttendees().size();
    assertEquals(100-3, freeSeats);

  }
}