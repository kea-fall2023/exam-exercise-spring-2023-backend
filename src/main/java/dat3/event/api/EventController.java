package dat3.event.api;

import dat3.event.dto.EventAttendeeResponse;
import dat3.event.dto.EventRequest;
import dat3.event.dto.EventResponse;
import dat3.event.service.EventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private EventService eventService;
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@RequestBody EventRequest eventRequest) {
        EventResponse createdEvent = eventService.createEvent(eventRequest);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public String deleteEvent(@PathVariable long id){
        return eventService.deleteEvent(id);
    }

    @PutMapping("/{id}")
    public EventResponse editEvent(@RequestBody EventRequest eventRequest,@PathVariable Long id) {
       return eventService.editEvent(eventRequest,id);
    }

    @GetMapping(value = "/count" ,produces = "application/json")
    ResponseEntity<String> count() {
        return eventService.count();
    }

    @PostMapping("/register/{eventId}")
    public EventResponse register(@PathVariable long eventId, Principal principal){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        boolean isAdmin = roles.contains("ADMIN");
        return eventService.registerAttendee(eventId, principal.getName(),isAdmin);
    }

    @DeleteMapping("/unregister/{eventId}")
    public String unregister(@PathVariable long eventId, Principal principal){
        return eventService.unregisterEvent(eventId, principal.getName());
    }

    @GetMapping("/myevents")
    public List<EventAttendeeResponse> getMyEvents(Principal principal){
        return eventService.getRegisteredEventsForPrincipal(principal.getName());
    }

    @GetMapping("/{id}")
    public EventResponse getEvent(@PathVariable long id){
        return eventService.getEvent(id,false);
    }


    @GetMapping("/namelike/{name}")
    public List<EventResponse> getEventByNameLike(@PathVariable String name){
        return eventService.getEventByNameLike(name);
    }

    @GetMapping()
    public Page<EventResponse> getAllEvents(Pageable pageable) {
        return eventService.getAllEvents(pageable);
    }
}
