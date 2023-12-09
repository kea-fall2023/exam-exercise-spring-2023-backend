package dat3.event.configuration;

import dat3.event.entity.Attendee;
import dat3.event.entity.Event;
import dat3.event.entity.Location;
import dat3.event.repository.AttendeeRepository;
import dat3.event.repository.EventRepository;
import dat3.event.repository.LocationRepository;
import dat3.security.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/*
Added as a service to solve the LazyInitializationException problem
createTestEvents are marked as transactional which is what solver the problem
 */
@Service
class TestDataSetupService {

  private EventRepository eventRepository;
  private LocationRepository locationRepository;
  private AttendeeRepository attendeeRepository;

  public TestDataSetupService(EventRepository eventRepository, LocationRepository locationRepository, AttendeeRepository attendeeRepository) {
    this.eventRepository = eventRepository;
    this.locationRepository = locationRepository;
    this.attendeeRepository = attendeeRepository;
  }

  private static final List<String> LOCATIONS = Arrays.asList(
          "Kongelige Teater", "DR Koncerthuset", "Skuespilhuset", "ICC Theatre",
          "Nørrebro Teater", "Østre Gasværk Teater", "Operaen Christiania", "Bremen Teater",
          "Folketeatret", "Posthus Teatret");

  private static final List<String> PLAYS = Arrays.asList(
          "Jean de France", "Jeppe på Bjerget", "Hakon Jarl", "Et Dukkehjem", "Ole Lukøje",
          "Ordet", "En Gæst", "I Adams verden", "Louises hus", "Verdens Ende", "Morgen og Aften",
          "Cyrano", "Klokkeren fra Notre Dame", "Matador", "Melodien der blev væk");

  public void setupTestData() {
    createLocations();
    createEvents();
    createTestUsers();
  }

  void createTestUsers() {
    IntStream.rangeClosed(1, 10).forEach(i -> {
      Attendee attendee = new Attendee();
      attendee.setUsername("A" + i);
      attendee.setEmail("A" + i + "@test.com");
      attendee.setPhoneNumber("1234567890" + i);
      attendee.setPassword("test12");
      attendee.addRole(Role.USER);
      attendeeRepository.save(attendee);
    });
  }

  private void createLocations() {
    LOCATIONS.forEach(locationName -> {
      Location location = new Location();
      location.setName(locationName);
      location.setCapacity(new Random().nextInt(497) + 4); // Random capacity between 4 and 500
      locationRepository.save(location);
    });
  }

  @Transactional
  void createEvents() {
    List<Location> locations = locationRepository.findAll();
    Random random = new Random();
    int totalEvents = 50;
    int eventsPerPlay = totalEvents / PLAYS.size();

    for (String play : PLAYS) {
      Location location = locations.get(random.nextInt(locations.size()));
      LocalDate startDate = LocalDate.now();

      for (int i = 0; i < eventsPerPlay; i++) {
        Event event = new Event();
        event.setName(play);
        event.setDate(LocalDateTime.of(startDate.plusDays(i), LocalTime.of(20, 0)));
        event.setDescription("Description for " + play);
        //event.setCapacity(random.nextInt(497) + 4);
        event.setCapacity(location.getCapacity());
        event.setLocation(location);

        eventRepository.save(event);
      }
    }
  }

}

@Component
public class DeveloperTestData implements CommandLineRunner {

  @Autowired
  TestDataSetupService dataSetupService;

  @Override
  public void run(String... args) {
    dataSetupService.setupTestData();
  }
}


