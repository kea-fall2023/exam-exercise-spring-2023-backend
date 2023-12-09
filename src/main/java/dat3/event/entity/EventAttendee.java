package dat3.event.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class EventAttendee extends DateTimeInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "attendee_id")
    private Attendee attendee;

    public EventAttendee(Event event, Attendee attendee) {
        this.event = event;
        this.attendee = attendee;
        this.event.addEventAttendee(this);
        this.attendee.addEventAttendee(this);
    }
}
