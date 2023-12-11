package dat3.event.service;

import dat3.event.dto.EventAttendeeResponse;
import dat3.event.dto.EventRequest;
import dat3.event.dto.EventResponse;
import dat3.event.entity.Attendee;
import dat3.event.entity.Event;
import dat3.event.entity.EventAttendee;
import dat3.event.entity.Location;
import dat3.event.repository.AttendeeRepository;
import dat3.event.repository.EventAttendeeRepository;
import dat3.event.repository.EventRepository;
import dat3.event.repository.LocationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EventService {


    private EventRepository eventRepository;
    private LocationRepository locationRepository;
    private AttendeeRepository attendeeRepository;
    private final EventAttendeeRepository eventAttendeeRepository;

    public EventService(EventRepository eventRepository, LocationRepository locationRepository, AttendeeRepository attendeeRepository,
                        EventAttendeeRepository eventAttendeeRepository) {
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
        this.attendeeRepository = attendeeRepository;
        this.eventAttendeeRepository = eventAttendeeRepository;
    }

    public EventResponse createEvent(EventRequest eventRequest) {
        Event event = new Event(eventRequest);
        if(eventRequest.getLocationId() != null) {
            Location location = locationRepository.findById(eventRequest.getLocationId())
                    .orElseThrow(() -> new RuntimeException("Location not found"));
            event.setLocation(location);
        }
        Event savedEvent = eventRepository.save(event);
        //return toEventResponse(savedEvent,false,false,true);
        return toEventResponse(savedEvent,true,false,true);
    }

    public EventResponse editEvent(EventRequest eventRequest,Long id) {
        Event event = eventRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Event not found"));
        event.setName(eventRequest.getName());
        event.setDate(eventRequest.getDate());
        event.setCapacity(eventRequest.getCapacity());
        event.setDescription(eventRequest.getDescription());
        //New request has a location
        Location newLocation = eventRequest.getLocationId() != null ? locationRepository.findById(eventRequest.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found")) : null;

        Location locationOrg = event.getLocation();
        if(locationOrg != null && newLocation == null){
                locationOrg.removeEvent(event);
         }
        if(locationOrg != null && newLocation != null){
                locationOrg.removeEvent(event);
                locationRepository.save(locationOrg);
                newLocation.addEvent(event);
         }
        if(locationOrg == null && newLocation != null){
                newLocation.addEvent(event);
         }
        eventRepository.save(event);
        return toEventResponse(event,true,false,true);
    }

    //Events as requested in part2
    public Page<EventResponse> getAllEvents(Pageable pageable) {
        Page<Event> events = eventRepository.findAll(pageable);
        List<EventResponse> eventDtos = events.stream().map(e -> toEventResponse(e,false,true,false)).toList();
        //This is necessary to return a Page<EventResponse> when the type is not a JPA entity
        return new PageImpl<>(eventDtos, pageable, events.getTotalElements());
    }

    public EventResponse getEvent(Long id,boolean includeDates) {
        Event event = eventRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Event not found"));
        return toEventResponse(event,false,false,includeDates);
    }

    public List<EventResponse> getEventByNameLike(String name) {
        List<Event> eventsFound = eventRepository.findByNameContainingIgnoreCase(name);
        return eventsFound.stream().map(e -> toEventResponse(e,false,false,false)).toList();
    }

    public ResponseEntity<String> count() {
        return ResponseEntity.ok("{\"count\":" + eventRepository.count()+"}");
    }

    public List<EventResponse> getEventByName(String name,boolean includeDates) {
        List<Event> events = eventRepository.findByName(name);
        return events.stream().map(e -> toEventResponse(e,false,false,includeDates)).toList();
    }

    public EventResponse registerEventForAttendee(Long eventId, String attendeeId, boolean isAdmin) {
        Event event = eventRepository.findById(eventId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Event not found"));
        Attendee attendee = attendeeRepository.findById(attendeeId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Attendee not found"));
        if(event.getCapacity() <= event.getEventAttendees().size()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Event is fully booked");
        }
        EventAttendee eventAttendee = new EventAttendee(event,attendee);
        eventAttendeeRepository.save(eventAttendee);
        return toEventResponse(event,true,true,isAdmin);
    }

    public String unregisterEvent(Long EventAttendeeId, String username){
        Attendee attendee = attendeeRepository.findById(username).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Attendee not found"));
        EventAttendee eventAttendee = eventAttendeeRepository.findById(EventAttendeeId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"EventAttendee not found"));
        if(!eventAttendee.getAttendee().getUsername().equals(username)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"EventAttendee does not belong to attendee");
        }
        eventAttendee.getEvent().getEventAttendees().remove(eventAttendee);
        eventAttendee.getAttendee().getEventAttendees().remove(eventAttendee);
        eventAttendeeRepository.delete(eventAttendee);
        return "{\"message\":\"Event unregistered\"}";
    }

    public List<EventAttendeeResponse> getRegisteredEventsForPrincipal(String username){
        Attendee attendee = attendeeRepository.findById(username).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Attendee not found"));
        return attendee.getEventAttendees().stream().map(ea -> new EventAttendeeResponse(ea)).toList();
    }

    public String deleteEvent(long id){
        Event event = eventRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Event not found"));
        try {
            eventRepository.delete(event);
            return "{\"message\":\"Event deleted\"}";
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Event has relations to other entities");
        }
    }

    private EventResponse toEventResponse(Event event,boolean includeLocation, boolean includeFreeSeats, boolean includeDates) {
        EventResponse er= new EventResponse();
        er.setId(event.getId());
        er.setName(event.getName());
        er.setDate(event.getDate());
        er.setDescription(event.getDescription());
        er.setCapacity(event.getCapacity());
        if (includeLocation && event.getLocation() != null) {
            er.setLocationId(event.getLocation().getId());
            er.setLocationName(event.getLocation().getName());
        }
        if(includeFreeSeats){
            er.setFreeSeats(event.getCapacity() - event.getEventAttendees().size());
        }
        if(includeDates){
            er.setCreated(event.getCreated());
            er.setUpdated(event.getUpdated());
        }
        return er;
    }
}
