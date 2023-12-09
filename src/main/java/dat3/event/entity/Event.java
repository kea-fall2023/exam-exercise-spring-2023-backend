package dat3.event.entity;

import dat3.event.dto.EventRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Event extends DateTimeInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Column(unique = true)
    private String name;
    private LocalDateTime date;
    private String description;

    private int capacity;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToMany(mappedBy = "event")
    private List<EventAttendee> eventAttendees = new ArrayList<>();

    public void addEventAttendee(EventAttendee eventAttendee){
        eventAttendees.add(eventAttendee);
    }

    public Event(EventRequest eventRequest){
        this.name = eventRequest.getName();
        this.date = eventRequest.getDate();
        this.description = eventRequest.getDescription();
        this.capacity = eventRequest.getCapacity();
    }
    public Event(String name, LocalDateTime date, String description, int capacity) {
        this.name = name;
        this.date = date;
        this.description = description;
        this.capacity = capacity;
    }
}
