package dat3.event.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Location extends DateTimeInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer capacity;

    @OneToMany(mappedBy = "location")
    private List<Event> events = new ArrayList<>();

    public void addEvent(Event event) {
        events.add(event);
        event.setLocation(this);
    }
    public void removeEvent(Event event) {
        events.remove(event);
        event.setLocation(null);
    }

    public Location(String name, Integer capacity) {
        this.name = name;
        this.capacity = capacity;
    }
}
