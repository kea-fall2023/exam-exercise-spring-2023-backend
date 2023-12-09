package dat3.event.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventResponse {
  Long id;
  String name;
  //@JsonFormat(pattern = "dd-MM-yyyy (HH:mm)",shape = JsonFormat.Shape.STRING)
  LocalDateTime date;
  String description;
  Long LocationId;
  String LocationName;
  Integer capacity;
  Integer freeSeats;
  LocalDateTime created;
  LocalDateTime updated;

  //Use Builder or setters if more fields are needed
  public EventResponse(Long id, String name, LocalDateTime date,String description) {
    this.id = id;
    this.name = name;
    this.date = date;
    this.description = description;
  }
}
