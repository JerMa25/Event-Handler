import com.example.eventhandler.models.evenement.Concert;
import com.example.eventhandler.models.evenement.Evenement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Concert Tests")
class ConcertTest {

    private Concert concert;
    private LocalDateTime testDate;
    private Duration testDuration;

    @BeforeEach
    void setUp() {
        testDate = LocalDateTime.of(2024, 8, 15, 20, 0);
        testDuration = Duration.ofHours(3);

        concert = new Concert(
                1,
                "Summer Music Festival",
                testDate,
                "Madison Square Garden",
                20000,
                testDuration,
                "The Beatles",
                "Rock"
        );
    }

    @Test
    @DisplayName("Test default constructor")
    void testDefaultConstructor() {
        Concert defaultConcert = new Concert();

        assertNotNull(defaultConcert);
        assertEquals(0, defaultConcert.getId());
        assertNull(defaultConcert.getNom());
        assertNull(defaultConcert.getDate());
        assertNull(defaultConcert.getLieu());
        assertEquals(0, defaultConcert.getCapaciteMax());
        assertNull(defaultConcert.getDuration());
        assertNull(defaultConcert.getArtiste());
        assertNull(defaultConcert.getGenreMusical());
    }

    @Test
    @DisplayName("Test parameterized constructor")
    void testParameterizedConstructor() {
        assertNotNull(concert);

        // Test inherited fields from Evenement
        assertEquals(1, concert.getId());
        assertEquals("Summer Music Festival", concert.getNom());
        assertEquals(testDate, concert.getDate());
        assertEquals("Madison Square Garden", concert.getLieu());
        assertEquals(20000, concert.getCapaciteMax());
        assertEquals(testDuration, concert.getDuration());

        // Test Concert-specific fields
        assertEquals("The Beatles", concert.getArtiste());
        assertEquals("Rock", concert.getGenreMusical());
    }

    @Test
    @DisplayName("Test getArtiste and setArtiste")
    void testArtisteGetterAndSetter() {
        assertEquals("The Beatles", concert.getArtiste());

        concert.setArtiste("Rolling Stones");
        assertEquals("Rolling Stones", concert.getArtiste());

        concert.setArtiste("Beyoncé");
        assertEquals("Beyoncé", concert.getArtiste());

        concert.setArtiste("");
        assertEquals("", concert.getArtiste());

        concert.setArtiste(null);
        assertNull(concert.getArtiste());
    }

    @Test
    @DisplayName("Test getGenreMusical and setGenreMusical")
    void testGenreMusicalGetterAndSetter() {
        assertEquals("Rock", concert.getGenreMusical());

        concert.setGenreMusical("Pop");
        assertEquals("Pop", concert.getGenreMusical());

        concert.setGenreMusical("Jazz");
        assertEquals("Jazz", concert.getGenreMusical());

        concert.setGenreMusical("Classical");
        assertEquals("Classical", concert.getGenreMusical());

        concert.setGenreMusical("");
        assertEquals("", concert.getGenreMusical());

        concert.setGenreMusical(null);
        assertNull(concert.getGenreMusical());
    }

    @Test
    @DisplayName("Test constructor with null Concert-specific values")
    void testConstructorWithNullConcertValues() {
        Concert concertWithNulls = new Concert(
                2,
                "Unnamed Concert",
                testDate,
                "Unknown Venue",
                1000,
                testDuration,
                null,
                null
        );

        assertNotNull(concertWithNulls);
        assertEquals(2, concertWithNulls.getId());
        assertEquals("Unnamed Concert", concertWithNulls.getNom());
        assertNull(concertWithNulls.getArtiste());
        assertNull(concertWithNulls.getGenreMusical());
    }

    @Test
    @DisplayName("Test constructor with empty Concert-specific values")
    void testConstructorWithEmptyConcertValues() {
        Concert concertWithEmptyValues = new Concert(
                3,
                "Empty Details Concert",
                testDate,
                "Concert Hall",
                500,
                testDuration,
                "",
                ""
        );

        assertNotNull(concertWithEmptyValues);
        assertEquals(3, concertWithEmptyValues.getId());
        assertEquals("Empty Details Concert", concertWithEmptyValues.getNom());
        assertEquals("", concertWithEmptyValues.getArtiste());
        assertEquals("", concertWithEmptyValues.getGenreMusical());
    }

    @Test
    @DisplayName("Test inheritance from Evenement")
    void testInheritanceFromEvenement() {
        assertTrue(concert instanceof Evenement);
        assertTrue(concert instanceof Concert);
        assertEquals(Concert.class, concert.getClass());
    }

    @Test
    @DisplayName("Test inherited methods from Evenement")
    void testInheritedMethods() {
        // Test that we can use inherited setters
        concert.setId(99);
        concert.setNom("Updated Concert Name");
        concert.setLieu("New Venue");
        concert.setCapaciteMax(50000);

        assertEquals(99, concert.getId());
        assertEquals("Updated Concert Name", concert.getNom());
        assertEquals("New Venue", concert.getLieu());
        assertEquals(50000, concert.getCapaciteMax());
    }

    @Test
    @DisplayName("Test with various music genres")
    void testWithVariousMusicGenres() {
        String[] genres = {
                "Rock", "Pop", "Jazz", "Classical", "Hip-Hop", "Electronic",
                "Country", "R&B", "Reggae", "Blues", "Folk", "Punk",
                "Heavy Metal", "Alternative", "Indie", "World Music"
        };

        for (String genre : genres) {
            concert.setGenreMusical(genre);
            assertEquals(genre, concert.getGenreMusical());
        }
    }

    @Test
    @DisplayName("Test with various artist names")
    void testWithVariousArtistNames() {
        String[] artists = {
                "Taylor Swift",
                "Ed Sheeran",
                "Adele",
                "Bruno Mars",
                "Lady Gaga",
                "The Weeknd",
                "Billie Eilish",
                "Drake",
                "Ariana Grande",
                "Post Malone"
        };

        for (String artist : artists) {
            concert.setArtiste(artist);
            assertEquals(artist, concert.getArtiste());
        }
    }

    @Test
    @DisplayName("Test with special characters in artist and genre")
    void testWithSpecialCharacters() {
        concert.setArtiste("Céline Dion & René Angélil");
        assertEquals("Céline Dion & René Angélil", concert.getArtiste());

        concert.setArtiste("AC/DC");
        assertEquals("AC/DC", concert.getArtiste());

        concert.setGenreMusical("Rock & Roll");
        assertEquals("Rock & Roll", concert.getGenreMusical());

        concert.setGenreMusical("R&B/Soul");
        assertEquals("R&B/Soul", concert.getGenreMusical());

        concert.setGenreMusical("K-Pop");
        assertEquals("K-Pop", concert.getGenreMusical());
    }

    @Test
    @DisplayName("Test with long artist and genre names")
    void testWithLongNames() {
        String longArtistName = "The Red Hot Chili Peppers featuring John Frusciante and Special Guest Artists";
        String longGenreName = "Progressive Alternative Rock with Electronic Elements and World Music Influences";

        concert.setArtiste(longArtistName);
        concert.setGenreMusical(longGenreName);

        assertEquals(longArtistName, concert.getArtiste());
        assertEquals(longGenreName, concert.getGenreMusical());
    }

    @Test
    @DisplayName("Test concert with realistic scenarios")
    void testRealisticConcertScenarios() {
        // Outdoor music festival
        Concert festival = new Concert(
                10,
                "Coachella 2024",
                LocalDateTime.of(2024, 4, 15, 18, 0),
                "Empire Polo Club, Indio",
                75000,
                Duration.ofHours(8),
                "Various Artists",
                "Multi-Genre"
        );

        assertEquals("Coachella 2024", festival.getNom());
        assertEquals("Various Artists", festival.getArtiste());
        assertEquals("Multi-Genre", festival.getGenreMusical());
        assertEquals(75000, festival.getCapaciteMax());

        // Intimate acoustic concert
        Concert acoustic = new Concert(
                11,
                "Acoustic Night",
                LocalDateTime.of(2024, 3, 20, 19, 30),
                "Blue Note Jazz Club",
                150,
                Duration.ofMinutes(90),
                "Norah Jones",
                "Jazz/Folk"
        );

        assertEquals("Acoustic Night", acoustic.getNom());
        assertEquals("Norah Jones", acoustic.getArtiste());
        assertEquals("Jazz/Folk", acoustic.getGenreMusical());
        assertEquals(150, acoustic.getCapaciteMax());
        assertEquals(90, acoustic.getDuration().toMinutes());
    }

    @Test
    @DisplayName("Test updating concert details")
    void testUpdatingConcertDetails() {
        // Original concert details
        assertEquals("The Beatles", concert.getArtiste());
        assertEquals("Rock", concert.getGenreMusical());

        // Update concert details (e.g., tribute band performance)
        concert.setArtiste("The Fab Four (Beatles Tribute)");
        concert.setGenreMusical("Classic Rock Tribute");

        assertEquals("The Fab Four (Beatles Tribute)", concert.getArtiste());
        assertEquals("Classic Rock Tribute", concert.getGenreMusical());

        // Verify inherited fields remain unchanged
        assertEquals("Summer Music Festival", concert.getNom());
        assertEquals("Madison Square Garden", concert.getLieu());
        assertEquals(20000, concert.getCapaciteMax());
    }
}