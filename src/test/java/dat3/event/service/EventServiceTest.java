package dat3.event.service;

import dat3.event.dto.EventRequest;
import dat3.event.dto.EventResponse;
import dat3.event.entity.Event;
import dat3.event.entity.Location;
import dat3.event.repository.EventRepository;
import dat3.event.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


/**
 * Unit tests for the EventService class, using Mockito to mock dependencies.
 */
@ExtendWith(SpringExtension.class)
public class EventServiceTest {

  @Mock
  private EventRepository eventRepository;

  @Mock
  private LocationRepository locationRepository;

  @InjectMocks
  private EventService eventService;

  @Test
  public void testCreateEvent() {
    Long locationId = 0L;
    Location mockLocation = new Location();
    mockLocation.setId(locationId);
    mockLocation.setName("Royal Arena");
    mockLocation.setCapacity(100);
    EventRequest Event;
    EventRequest eventRequest = new EventRequest("Event A", LocalDateTime.now(),"Description",locationId,mockLocation.getCapacity());

    when(locationRepository.findById(locationId)).thenReturn(Optional.of(mockLocation));

    Event event = new Event(eventRequest);
    event.setId(100l);
    event.setCreated(LocalDateTime.now());
    event.setUpdated(LocalDateTime.now());
    when(eventRepository.save(any(Event.class))).thenReturn(event);

    EventResponse result = eventService.createEvent(eventRequest);

    // Assert
    assertNotNull(result);
    assertEquals(event.getName(), result.getName());
    assertEquals(event.getDate(), result.getDate());
    assertNotNull(result.getCreated());
    assertNotNull(result.getUpdated());
    assertNull(result.getLocationId());
  }
}