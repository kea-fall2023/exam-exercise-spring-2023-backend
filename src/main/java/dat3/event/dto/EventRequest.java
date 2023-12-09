package dat3.event.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventRequest {
    String name;
    LocalDateTime date;
    String description;
    int capacity;
    Long locationId;

    public EventRequest(String name, LocalDateTime date, String description, Long locationId, int capacity) {
        this.name = name;
        this.date = date;
        this.description = description;
        this.locationId = locationId;
        this.capacity = capacity;
    }
}
