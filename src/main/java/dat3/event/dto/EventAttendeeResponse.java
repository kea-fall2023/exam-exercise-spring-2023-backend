package dat3.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import dat3.event.entity.EventAttendee;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventAttendeeResponse {
  Long eventId;
  Long reservationId;
  String eventName;
  String attendeeName;
  @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss",shape = JsonFormat.Shape.STRING)
  LocalDateTime eventDate;
  String eventDescription;
  Long LocationId;
  String LocationName;

  public EventAttendeeResponse(EventAttendee ea) {
    this.eventId = ea.getEvent().getId();
    this.reservationId = ea.getId();
    this.eventName = ea.getEvent().getName();
    this.attendeeName = ea.getAttendee().getUsername();
    this.eventDate = ea.getEvent().getDate();
    this.eventDescription = eventDescription;
    LocationId = ea.getEvent().getLocation().getId();
    LocationName = ea.getEvent().getLocation().getName();
  }
}