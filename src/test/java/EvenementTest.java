import com.example.eventhandler.models.evenement.Evenement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Evenement Tests")
class EvenementTest {

    private ConcreteEvenement evenement;
    private LocalDateTime testDate;
    private Duration testDuration;

    // Concrete implementation of abstract Evenement for testing
    private static class ConcreteEvenement extends Evenement {
        public ConcreteEvenement() {
            super();
        }

        public ConcreteEvenement(int id, String nom, LocalDateTime date, String lieu, int capaciteMax, Duration duration) {
            super(id, nom, date, lieu, capaciteMax, duration);
        }
    }

    @BeforeEach
    void setUp() {
        testDate = LocalDateTime.of(2024, 6, 15, 14, 30);
        testDuration = Duration.ofHours(2);

        evenement = new ConcreteEvenement(
                1,
                "Test Event",
                testDate,
                "Convention Center",
                500,
                testDuration
        );
    }

    @Test
    @DisplayName("Test default constructor")
    void testDefaultConstructor() {
        ConcreteEvenement defaultEvenement = new ConcreteEvenement();

        assertNotNull(defaultEvenement);
        assertEquals(0, defaultEvenement.getId());
        assertNull(defaultEvenement.getNom());
        assertNull(defaultEvenement.getDate());
        assertNull(defaultEvenement.getLieu());
        assertEquals(0, defaultEvenement.getCapaciteMax());
        assertNull(defaultEvenement.getDuration());
    }

    @Test
    @DisplayName("Test parameterized constructor")
    void testParameterizedConstructor() {
        assertNotNull(evenement);
        assertEquals(1, evenement.getId());
        assertEquals("Test Event", evenement.getNom());
        assertEquals(testDate, evenement.getDate());
        assertEquals("Convention Center", evenement.getLieu());
        assertEquals(500, evenement.getCapaciteMax());
        assertEquals(testDuration, evenement.getDuration());
    }

    @Test
    @DisplayName("Test getId and setId")
    void testIdGetterAndSetter() {
        assertEquals(1, evenement.getId());

        evenement.setId(99);
        assertEquals(99, evenement.getId());

        evenement.setId(0);
        assertEquals(0, evenement.getId());

        evenement.setId(-1);
        assertEquals(-1, evenement.getId());
    }

    @Test
    @DisplayName("Test getNom and setNom")
    void testNomGetterAndSetter() {
        assertEquals("Test Event", evenement.getNom());

        evenement.setNom("Updated Event Name");
        assertEquals("Updated Event Name", evenement.getNom());

        evenement.setNom("");
        assertEquals("", evenement.getNom());

        evenement.setNom(null);
        assertNull(evenement.getNom());
    }

    @Test
    @DisplayName("Test getDate and setDate")
    void testDateGetterAndSetter() {
        assertEquals(testDate, evenement.getDate());

        LocalDateTime newDate = LocalDateTime.of(2024, 12, 25, 10, 0);
        evenement.setDate(newDate);
        assertEquals(newDate, evenement.getDate());

        evenement.setDate(null);
        assertNull(evenement.getDate());
    }

    @Test
    @DisplayName("Test getLieu and setLieu")
    void testLieuGetterAndSetter() {
        assertEquals("Convention Center", evenement.getLieu());

        evenement.setLieu("Stadium");
        assertEquals("Stadium", evenement.getLieu());

        evenement.setLieu("");
        assertEquals("", evenement.getLieu());

        evenement.setLieu(null);
        assertNull(evenement.getLieu());
    }

    @Test
    @DisplayName("Test getCapaciteMax and setCapaciteMax")
    void testCapaciteMaxGetterAndSetter() {
        assertEquals(500, evenement.getCapaciteMax());

        evenement.setCapaciteMax(1000);
        assertEquals(1000, evenement.getCapaciteMax());

        evenement.setCapaciteMax(0);
        assertEquals(0, evenement.getCapaciteMax());

        evenement.setCapaciteMax(-1);
        assertEquals(-1, evenement.getCapaciteMax());
    }

    @Test
    @DisplayName("Test getDuration and setDuration")
    void testDurationGetterAndSetter() {
        assertEquals(testDuration, evenement.getDuration());

        Duration newDuration = Duration.ofMinutes(90);
        evenement.setDuration(newDuration);
        assertEquals(newDuration, evenement.getDuration());

        Duration zeroDuration = Duration.ZERO;
        evenement.setDuration(zeroDuration);
        assertEquals(zeroDuration, evenement.getDuration());

        evenement.setDuration(null);
        assertNull(evenement.getDuration());
    }

    @Test
    @DisplayName("Test constructor with null values")
    void testConstructorWithNullValues() {
        ConcreteEvenement nullEvenement = new ConcreteEvenement(
                0, null, null, null, 0, null
        );

        assertNotNull(nullEvenement);
        assertEquals(0, nullEvenement.getId());
        assertNull(nullEvenement.getNom());
        assertNull(nullEvenement.getDate());
        assertNull(nullEvenement.getLieu());
        assertEquals(0, nullEvenement.getCapaciteMax());
        assertNull(nullEvenement.getDuration());
    }

    @Test
    @DisplayName("Test constructor with edge case values")
    void testConstructorWithEdgeCaseValues() {
        LocalDateTime pastDate = LocalDateTime.of(2020, 1, 1, 0, 0);
        Duration longDuration = Duration.ofDays(30);

        ConcreteEvenement edgeCaseEvenement = new ConcreteEvenement(
                Integer.MAX_VALUE,
                "Very Long Event Name That Could Be Problematic",
                pastDate,
                "Venue with Special Characters: Café & Théâtre #1",
                Integer.MAX_VALUE,
                longDuration
        );

        assertNotNull(edgeCaseEvenement);
        assertEquals(Integer.MAX_VALUE, edgeCaseEvenement.getId());
        assertEquals("Very Long Event Name That Could Be Problematic", edgeCaseEvenement.getNom());
        assertEquals(pastDate, edgeCaseEvenement.getDate());
        assertEquals("Venue with Special Characters: Café & Théâtre #1", edgeCaseEvenement.getLieu());
        assertEquals(Integer.MAX_VALUE, edgeCaseEvenement.getCapaciteMax());
        assertEquals(longDuration, edgeCaseEvenement.getDuration());
    }

    @Test
    @DisplayName("Test duration with different time units")
    void testDurationWithDifferentTimeUnits() {
        Duration minutes30 = Duration.ofMinutes(30);
        Duration hours3 = Duration.ofHours(3);
        Duration days2 = Duration.ofDays(2);

        evenement.setDuration(minutes30);
        assertEquals(minutes30, evenement.getDuration());
        assertEquals(30, evenement.getDuration().toMinutes());

        evenement.setDuration(hours3);
        assertEquals(hours3, evenement.getDuration());
        assertEquals(3, evenement.getDuration().toHours());

        evenement.setDuration(days2);
        assertEquals(days2, evenement.getDuration());
        assertEquals(2, evenement.getDuration().toDays());
    }

    @Test
    @DisplayName("Test date with different time zones")
    void testDateWithDifferentTimes() {
        LocalDateTime earlyMorning = LocalDateTime.of(2024, 1, 1, 6, 0);
        LocalDateTime lateMorning = LocalDateTime.of(2024, 1, 1, 11, 30);
        LocalDateTime afternoon = LocalDateTime.of(2024, 1, 1, 15, 45);
        LocalDateTime evening = LocalDateTime.of(2024, 1, 1, 20, 15);
        LocalDateTime lateNight = LocalDateTime.of(2024, 1, 1, 23, 59);

        evenement.setDate(earlyMorning);
        assertEquals(earlyMorning, evenement.getDate());

        evenement.setDate(lateMorning);
        assertEquals(lateMorning, evenement.getDate());

        evenement.setDate(afternoon);
        assertEquals(afternoon, evenement.getDate());

        evenement.setDate(evening);
        assertEquals(evening, evenement.getDate());

        evenement.setDate(lateNight);
        assertEquals(lateNight, evenement.getDate());
    }

    @Test
    @DisplayName("Test abstract class behavior")
    void testAbstractClassBehavior() {
        // Verify that we cannot instantiate Evenement directly
        // This is more of a compile-time check, but we can verify inheritance
        assertTrue(evenement instanceof Evenement);
        assertEquals(ConcreteEvenement.class, evenement.getClass());
    }
}