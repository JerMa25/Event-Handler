import com.example.eventhandler.models.evenement.Evenement;
import com.example.eventhandler.models.personne.Organisateur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Organisateur Tests")
class OrganisateurTest {

    private Organisateur organisateur;
    private List<Evenement> evenements;

    @Mock
    private Evenement evenement1;

    @Mock
    private Evenement evenement2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create list with mock events
        evenements = new ArrayList<>(Arrays.asList(evenement1, evenement2));

        organisateur = new Organisateur(
                "testuser123",
                "John Doe",
                "john.doe@example.com",
                "password123",
                evenements
        );
    }

    @Test
    @DisplayName("Test default constructor")
    void testDefaultConstructor() {
        Organisateur defaultOrganisateur = new Organisateur();

        assertNotNull(defaultOrganisateur);
        assertNull(defaultOrganisateur.getEvenementsOrganises());
    }

    @Test
    @DisplayName("Test parameterized constructor")
    void testParameterizedConstructor() {
        assertNotNull(organisateur);
        assertEquals("testuser123", organisateur.getId()); // Assuming getUsername() from Participant
        assertEquals("John Doe", organisateur.getNom()); // Assuming getNom() from Participant
        assertEquals("john.doe@example.com", organisateur.getEmail()); // Assuming getEmail() from Participant
        assertEquals("password123", organisateur.getPassword()); // Assuming getPassword() from Participant
        assertEquals(evenements, organisateur.getEvenementsOrganises());
        assertEquals(2, organisateur.getEvenementsOrganises().size());
    }

    @Test
    @DisplayName("Test getEvenementsOrganises")
    void testGetEvenementsOrganises() {
        List<Evenement> retrievedEvents = organisateur.getEvenementsOrganises();

        assertNotNull(retrievedEvents);
        assertEquals(2, retrievedEvents.size());
        assertTrue(retrievedEvents.contains(evenement1));
        assertTrue(retrievedEvents.contains(evenement2));
    }

    @Test
    @DisplayName("Test setEvenementsOrganises")
    void testSetEvenementsOrganises() {
        Evenement newEvent = evenement1; // Reuse existing mock
        List<Evenement> newEvents = Arrays.asList(newEvent);

        organisateur.setEvenementsOrganises(newEvents);
        assertEquals(newEvents, organisateur.getEvenementsOrganises());
        assertEquals(1, organisateur.getEvenementsOrganises().size());
        assertTrue(organisateur.getEvenementsOrganises().contains(newEvent));
    }

    @Test
    @DisplayName("Test setEvenementsOrganises with null")
    void testSetEvenementsOrganisesWithNull() {
        organisateur.setEvenementsOrganises(null);

        assertNull(organisateur.getEvenementsOrganises());
    }

    @Test
    @DisplayName("Test setEvenementsOrganises with empty list")
    void testSetEvenementsOrganisesWithEmptyList() {
        List<Evenement> emptyList = new ArrayList<>();

        organisateur.setEvenementsOrganises(emptyList);

        assertNotNull(organisateur.getEvenementsOrganises());
        assertTrue(organisateur.getEvenementsOrganises().isEmpty());
        assertEquals(0, organisateur.getEvenementsOrganises().size());
    }

    @Test
    @DisplayName("Test constructor with null events list")
    void testConstructorWithNullEventsList() {
        Organisateur organisateurWithNullEvents = new Organisateur(
                "testuser456",
                "Jane Smith",
                "jane.smith@example.com",
                "password456",
                null
        );

        assertNotNull(organisateurWithNullEvents);
        assertNull(organisateurWithNullEvents.getEvenementsOrganises());
    }

    @Test
    @DisplayName("Test constructor with empty events list")
    void testConstructorWithEmptyEventsList() {
        List<Evenement> emptyEvents = new ArrayList<>();
        Organisateur organisateurWithEmptyEvents = new Organisateur(
                "testuser789",
                "Bob Johnson",
                "bob.johnson@example.com",
                "password789",
                emptyEvents
        );

        assertNotNull(organisateurWithEmptyEvents);
        assertNotNull(organisateurWithEmptyEvents.getEvenementsOrganises());
        assertTrue(organisateurWithEmptyEvents.getEvenementsOrganises().isEmpty());
    }

    @Test
    @DisplayName("Test inheritance from Participant")
    void testInheritanceFromParticipant() {
        assertTrue(organisateur != null);
    }
}