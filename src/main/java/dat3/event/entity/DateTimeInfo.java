package dat3.event.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public class DateTimeInfo {
  @CreationTimestamp
  LocalDateTime created;
  @UpdateTimestamp
  LocalDateTime updated;
}
