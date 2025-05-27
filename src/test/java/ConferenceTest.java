import com.example.eventhandler.models.evenement.Conference;
import com.example.eventhandler.models.evenement.Evenement;
import com.example.eventhandler.models.personne.Intervenant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Conference Tests")
class ConferenceTest {

    private Conference conference;
    private LocalDateTime testDate;
    private Duration testDuration;
    private List<Intervenant> intervenants;

    @Mock
    private Intervenant intervenant1;

    @Mock
    private Intervenant intervenant2;

    @Mock
    private Intervenant intervenant3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testDate = LocalDateTime.of(2024, 9, 20, 9, 0);
        testDuration = Duration.ofHours(6);

        // Set up mock intervenants with basic behavior
        when(intervenant1.getNom()).thenReturn("Dr. John Smith");
        when(intervenant2.getNom()).thenReturn("Prof. Jane Doe");
        when(intervenant3.getNom()).thenReturn("Dr. Bob Johnson");

        intervenants = new ArrayList<>(Arrays.asList(intervenant1, intervenant2));

        conference = new Conference(
                1,
                "Tech Innovation Summit 2024",
                testDate,
                "Silicon Valley Convention Center",
                1000,
                testDuration,
                "Artificial Intelligence and Future Technologies",
                intervenants
        );
    }

    @Test
    @DisplayName("Test default constructor")
    void testDefaultConstructor() {
        Conference defaultConference = new Conference();

        assertNotNull(defaultConference);
        assertEquals(0, defaultConference.getId());
        assertNull(defaultConference.getNom());
        assertNull(defaultConference.getDate());
        assertNull(defaultConference.getLieu());
        assertEquals(0, defaultConference.getCapaciteMax());
        assertNull(defaultConference.getDuration());
        assertNull(defaultConference.getTheme());
        assertNull(defaultConference.getIntervenants());
    }

    @Test
    @DisplayName("Test parameterized constructor")
    void testParameterizedConstructor() {
        assertNotNull(conference);

        // Test inherited fields from Evenement
        assertEquals(1, conference.getId());
        assertEquals("Tech Innovation Summit 2024", conference.getNom());
        assertEquals(testDate, conference.getDate());
        assertEquals("Silicon Valley Convention Center", conference.getLieu());
        assertEquals(1000, conference.getCapaciteMax());
        assertEquals(testDuration, conference.getDuration());

        // Test Conference-specific fields
        assertEquals("Artificial Intelligence and Future Technologies", conference.getTheme());
        assertEquals(intervenants, conference.getIntervenants());
        assertEquals(2, conference.getIntervenants().size());
    }

    @Test
    @DisplayName("Test getTheme and setTheme")
    void testThemeGetterAndSetter() {
        assertEquals("Artificial Intelligence and Future Technologies", conference.getTheme());

        conference.setTheme("Climate Change and Sustainability");
        assertEquals("Climate Change and Sustainability", conference.getTheme());

        conference.setTheme("Digital Transformation in Healthcare");
        assertEquals("Digital Transformation in Healthcare", conference.getTheme());

        conference.setTheme("");
        assertEquals("", conference.getTheme());

        conference.setTheme(null);
        assertNull(conference.getTheme());
    }

    @Test
    @DisplayName("Test getIntervenants and setIntervenants")
    void testIntervenantsGetterAndSetter() {
        List<Intervenant> retrievedIntervenants = conference.getIntervenants();

        assertNotNull(retrievedIntervenants);
        assertEquals(2, retrievedIntervenants.size());
        assertTrue(retrievedIntervenants.contains(intervenant1));
        assertTrue(retrievedIntervenants.contains(intervenant2));

        // Test setting new list
        List<Intervenant> newIntervenants = Arrays.asList(intervenant3);
        conference.setIntervenants(newIntervenants);

        assertEquals(newIntervenants, conference.getIntervenants());
        assertEquals(1, conference.getIntervenants().size());
        assertTrue(conference.getIntervenants().contains(intervenant3));
    }

    @Test
    @DisplayName("Test setIntervenants with null")
    void testSetIntervenantsWithNull() {
        conference.setIntervenants(null);
        assertNull(conference.getIntervenants());
    }

    @Test
    @DisplayName("Test setIntervenants with empty list")
    void testSetIntervenantsWithEmptyList() {
        List<Intervenant> emptyList = new ArrayList<>();
        conference.setIntervenants(emptyList);

        assertNotNull(conference.getIntervenants());
        assertTrue(conference.getIntervenants().isEmpty());
        assertEquals(0, conference.getIntervenants().size());
    }

    @Test
    @DisplayName("Test constructor with null Conference-specific values")
    void testConstructorWithNullConferenceValues() {
        Conference conferenceWithNulls = new Conference(
                2,
                "Conference with Missing Details",
                testDate,
                "Unknown Venue",
                500,
                testDuration,
                null,
                null
        );

        assertNotNull(conferenceWithNulls);
        assertEquals(2, conferenceWithNulls.getId());
        assertEquals("Conference with Missing Details", conferenceWithNulls.getNom());
        assertNull(conferenceWithNulls.getTheme());
        assertNull(conferenceWithNulls.getIntervenants());
    }

    @Test
    @DisplayName("Test constructor with empty Conference-specific values")
    void testConstructorWithEmptyConferenceValues() {
        List<Intervenant> emptyIntervenants = new ArrayList<>();
        Conference conferenceWithEmptyValues = new Conference(
                3,
                "Empty Details Conference",
                testDate,
                "Conference Hall",
                300,
                testDuration,
                "",
                emptyIntervenants
        );

        assertNotNull(conferenceWithEmptyValues);
        assertEquals(3, conferenceWithEmptyValues.getId());
        assertEquals("Empty Details Conference", conferenceWithEmptyValues.getNom());
        assertEquals("", conferenceWithEmptyValues.getTheme());
        assertNotNull(conferenceWithEmptyValues.getIntervenants());
        assertTrue(conferenceWithEmptyValues.getIntervenants().isEmpty());
    }

    @Test
    @DisplayName("Test inheritance from Evenement")
    void testInheritanceFromEvenement() {
        assertTrue(conference instanceof Evenement);
        assertTrue(conference instanceof Conference);
        assertEquals(Conference.class, conference.getClass());
    }

    @Test
    @DisplayName("Test inherited methods from Evenement")
    void testInheritedMethods() {
        // Test that we can use inherited setters
        conference.setId(99);
        conference.setNom("Updated Conference Name");
        conference.setLieu("New Convention Center");
        conference.setCapaciteMax(2000);

        assertEquals(99, conference.getId());
        assertEquals("Updated Conference Name", conference.getNom());
        assertEquals("New Convention Center", conference.getLieu());
        assertEquals(2000, conference.getCapaciteMax());
    }

    @Test
    @DisplayName("Test with various conference themes")
    void testWithVariousConferenceThemes() {
        String[] themes = {
                "Artificial Intelligence and Machine Learning",
                "Sustainable Development and Green Technology",
                "Digital Health and Telemedicine",
                "Cybersecurity and Data Privacy",
                "Blockchain and Cryptocurrency",
                "Space Exploration and Astronomy",
                "Renewable Energy Solutions",
                "Educational Technology and E-Learning",
                "Urban Planning and Smart Cities",
                "Biotechnology and Genetic Engineering"
        };

        for (String theme : themes) {
            conference.setTheme(theme);
            assertEquals(theme, conference.getTheme());
        }
    }

    @Test
    @DisplayName("Test with special characters in theme")
    void testWithSpecialCharactersInTheme() {
        conference.setTheme("AI & Machine Learning: The Future of Technology");
        assertEquals("AI & Machine Learning: The Future of Technology", conference.getTheme());

        conference.setTheme("Développement Durable & Écologie");
        assertEquals("Développement Durable & Écologie", conference.getTheme());

        conference.setTheme("Healthcare 4.0: IoT, AI & Big Data");
        assertEquals("Healthcare 4.0: IoT, AI & Big Data", conference.getTheme());
    }

    @Test
    @DisplayName("Test adding and removing intervenants")
    void testAddingAndRemovingIntervenants() {
        List<Intervenant> currentIntervenants = new ArrayList<>(conference.getIntervenants());
        assertEquals(2, currentIntervenants.size());

        // Add a new intervenant
        currentIntervenants.add(intervenant3);
        conference.setIntervenants(currentIntervenants);

        assertEquals(3, conference.getIntervenants().size());
        assertTrue(conference.getIntervenants().contains(intervenant3));

        // Remove an intervenant
        currentIntervenants.remove(intervenant1);
        conference.setIntervenants(currentIntervenants);

        assertEquals(2, conference.getIntervenants().size());
        assertFalse(conference.getIntervenants().contains(intervenant1));
        assertTrue(conference.getIntervenants().contains(intervenant2));
        assertTrue(conference.getIntervenants().contains(intervenant3));
    }

    @Test
    @DisplayName("Test with large number of intervenants")
    void testWithLargeNumberOfIntervenants() {
        List<Intervenant> manyIntervenants = new ArrayList<>();

        // Create a large list of mock intervenants
        for (int i = 0; i < 50; i++) {
            Intervenant mockIntervenant = mock(Intervenant.class);
            when(mockIntervenant.getNom()).thenReturn("Speaker " + (i + 1));
            manyIntervenants.add(mockIntervenant);
        }

        conference.setIntervenants(manyIntervenants);

        assertEquals(50, conference.getIntervenants().size());
        assertNotNull(conference.getIntervenants());
    }

    @Test
    @DisplayName("Test realistic conference scenarios")
    void testRealisticConferenceScenarios() {
        // Medical conference
        Conference medicalConf = new Conference(
                10,
                "International Medical Research Symposium",
                LocalDateTime.of(2024, 5, 15, 8, 30),
                "Medical University Auditorium",
                500,
                Duration.ofHours(8),
                "Advances in Cancer Treatment and Research",
                Arrays.asList(intervenant1, intervenant2)
        );

        assertEquals("International Medical Research Symposium", medicalConf.getNom());
        assertEquals("Advances in Cancer Treatment and Research", medicalConf.getTheme());
        assertEquals(500, medicalConf.getCapaciteMax());
        assertEquals(8, medicalConf.getDuration().toHours());

        // Tech startup conference
        Conference startupConf = new Conference(
                11,
                "Startup Pitch Competition 2024",
                LocalDateTime.of(2024, 7, 10, 13, 0),
                "Innovation Hub",
                200,
                Duration.ofHours(4),
                "Emerging Technologies and Entrepreneurship",
                Arrays.asList(intervenant1)
        );

        assertEquals("Startup Pitch Competition 2024", startupConf.getNom());
        assertEquals("Emerging Technologies and Entrepreneurship", startupConf.getTheme());
        assertEquals(200, startupConf.getCapaciteMax());
        assertEquals(1, startupConf.getIntervenants().size());
    }

    @Test
    @DisplayName("Test updating conference details")
    void testUpdatingConferenceDetails() {
        // Original conference details
        assertEquals("Artificial Intelligence and Future Technologies", conference.getTheme());
        assertEquals(2, conference.getIntervenants().size());

        // Update conference details
        conference.setTheme("Machine Learning and Data Science Applications");
        List<Intervenant> updatedIntervenants = Arrays.asList(intervenant1, intervenant2, intervenant3);
        conference.setIntervenants(updatedIntervenants);

        assertEquals("Machine Learning and Data Science Applications", conference.getTheme());
        assertEquals(3, conference.getIntervenants().size());
        assertTrue(conference.getIntervenants().contains(intervenant3));

        // Verify inherited fields remain unchanged
        assertEquals("Tech Innovation Summit 2024", conference.getNom());
        assertEquals("Silicon Valley Convention Center", conference.getLieu());
        assertEquals(1000, conference.getCapaciteMax());
    }

    @Test
    @DisplayName("Test conference with single intervenant")
    void testConferenceWithSingleIntervenant() {
        Conference singleSpeakerConf = new Conference(
                12,
                "Keynote Presentation",
                testDate,
                "Main Auditorium",
                800,
                Duration.ofMinutes(90),
                "The Future of Work in the Digital Age",
                Arrays.asList(intervenant1)
        );

        assertEquals(1, singleSpeakerConf.getIntervenants().size());
        assertEquals(intervenant1, singleSpeakerConf.getIntervenants().get(0));
        assertEquals("The Future of Work in the Digital Age", singleSpeakerConf.getTheme());
        assertEquals(90, singleSpeakerConf.getDuration().toMinutes());
    }

    @Test
    @DisplayName("Test conference theme with long description")
    void testConferenceWithLongTheme() {
        String longTheme = "Comprehensive Analysis of Emerging Technologies in Healthcare: " +
                "From Artificial Intelligence and Machine Learning Applications " +
                "to Telemedicine, IoT Devices, and Blockchain Integration in " +
                "Modern Medical Practice and Patient Care Management Systems";

        conference.setTheme(longTheme);
        assertEquals(longTheme, conference.getTheme());
    }
}