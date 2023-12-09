package dat3.event.entity;

import dat3.security.entity.UserWithRoles;
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
public class Attendee extends UserWithRoles {

    private String phoneNumber;

    @OneToMany(mappedBy = "attendee")
    private List<EventAttendee> eventAttendees = new ArrayList<>();

    public void addEventAttendee(EventAttendee eventAttendee){
        eventAttendees.add(eventAttendee);
    }
    public Attendee(String name, String email, String password, String phoneNumber) {
        super(name, password, email);
        this.phoneNumber = phoneNumber;
    }
}
