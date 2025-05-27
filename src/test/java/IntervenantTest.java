import com.example.eventhandler.models.personne.Intervenant;
import com.example.eventhandler.models.personne.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Intervenant Tests")
class IntervenantTest {

    private Intervenant intervenant;

    @BeforeEach
    void setUp() {
        intervenant = new Intervenant(
                "speaker123",
                "Alice Johnson",
                "alice.johnson@example.com",
                "password456"
        );
    }

    @Test
    @DisplayName("Test default constructor")
    void testDefaultConstructor() {
        Intervenant defaultIntervenant = new Intervenant();

        assertNotNull(defaultIntervenant);
        // Assuming parent class Participant initializes fields to null
        assertNull(defaultIntervenant.getId());
        assertNull(defaultIntervenant.getNom());
        assertNull(defaultIntervenant.getEmail());
        assertNull(defaultIntervenant.getPassword());
    }

    @Test
    @DisplayName("Test parameterized constructor")
    void testParameterizedConstructor() {
        assertNotNull(intervenant);
        assertEquals("speaker123", intervenant.getId());
        assertEquals("Alice Johnson", intervenant.getNom());
        assertEquals("alice.johnson@example.com", intervenant.getEmail());
        assertEquals("password456", intervenant.getPassword());
    }

    @Test
    @DisplayName("Test constructor with null values")
    void testConstructorWithNullValues() {
        Intervenant intervenantWithNulls = new Intervenant(null, null, null, null);

        assertNotNull(intervenantWithNulls);
        assertNull(intervenantWithNulls.getId());
        assertNull(intervenantWithNulls.getNom());
        assertNull(intervenantWithNulls.getEmail());
        assertNull(intervenantWithNulls.getPassword());
    }

    @Test
    @DisplayName("Test constructor with empty strings")
    void testConstructorWithEmptyStrings() {
        Intervenant intervenantWithEmptyStrings = new Intervenant("", "", "", "");

        assertNotNull(intervenantWithEmptyStrings);
        assertEquals("", intervenantWithEmptyStrings.getId());
        assertEquals("", intervenantWithEmptyStrings.getNom());
        assertEquals("", intervenantWithEmptyStrings.getEmail());
        assertEquals("", intervenantWithEmptyStrings.getPassword());
    }

    @Test
    @DisplayName("Test inheritance from Participant")
    void testInheritanceFromParticipant() {
        assertTrue(intervenant instanceof Participant);
    }

    @Test
    @DisplayName("Test setters inherited from Participant")
    void testInheritedSetters() {
        // Test that setters work properly (assuming they exist in Participant)
        intervenant.setId("newusername");
        intervenant.setNom("New Name");
        intervenant.setEmail("newemail@example.com");
        intervenant.setPassword("newpassword");

        assertEquals("newusername", intervenant.getId());
        assertEquals("New Name", intervenant.getNom());
        assertEquals("newemail@example.com", intervenant.getEmail());
        assertEquals("newpassword", intervenant.getPassword());
    }

    @Test
    @DisplayName("Test equals and hashCode consistency")
    void testEqualsAndHashCode() {
        Intervenant intervenant1 = new Intervenant("user1", "John Doe", "john@example.com", "pass123");
        Intervenant intervenant2 = new Intervenant("user1", "John Doe", "john@example.com", "pass123");
        Intervenant intervenant3 = new Intervenant("user2", "Jane Doe", "jane@example.com", "pass456");

        // Test reflexivity (assuming equals is properly implemented in Participant)
        assertEquals(intervenant1, intervenant1);

        // Test symmetry
        if (intervenant1.equals(intervenant2)) {
            assertEquals(intervenant2, intervenant1);
            assertEquals(intervenant1.hashCode(), intervenant2.hashCode());
        }

        // Test with different objects
        assertNotEquals(intervenant1, intervenant3);
    }

    @Test
    @DisplayName("Test toString method")
    void testToString() {
        String toStringResult = intervenant.toString();

        assertNotNull(toStringResult);
        assertFalse(toStringResult.isEmpty());
        // Assuming toString includes class name
        assertTrue(toStringResult.contains("Intervenant") || toStringResult.contains("Participant"));
    }

    @Test
    @DisplayName("Test object creation with special characters")
    void testConstructorWithSpecialCharacters() {
        Intervenant specialIntervenant = new Intervenant(
                "user@#$%",
                "Jean-Pierre O'Connor",
                "jean.pierre@université.fr",
                "pässwörd123!"
        );

        assertNotNull(specialIntervenant);
        assertEquals("user@#$%", specialIntervenant.getId());
        assertEquals("Jean-Pierre O'Connor", specialIntervenant.getNom());
        assertEquals("jean.pierre@université.fr", specialIntervenant.getEmail());
        assertEquals("pässwörd123!", specialIntervenant.getPassword());
    }
}