import com.example.eventhandler.exceptions.EvenementDejaExistantException;
import com.example.eventhandler.exceptions.ParticipantDejaExistantException;
import com.example.eventhandler.models.evenement.Evenement;
import com.example.eventhandler.models.personne.Organisateur;
import com.example.eventhandler.models.personne.Participant;
import com.example.eventhandler.persistence.Serialization;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SerializationTest {

    @TempDir
    static Path tempDir;

    private static String originalUserDir;
    private static ObjectMapper mapper;

    private Participant testParticipant1;
    private Participant testParticipant2;
    private Organisateur testOrganisateur1;
    private Organisateur testOrganisateur2;
    private ConcreteEvenement testEvenement1;
    private ConcreteEvenement testEvenement2;
    private ConcreteEvenement conflictingEvenement;

    // Concrete implementation of abstract Evenement for testing
    private static class ConcreteEvenement extends Evenement {
        public ConcreteEvenement(int id, String nom, String lieu, LocalDateTime date, int capaciteMax, Duration duration) {
            super(id, nom, date, lieu, capaciteMax, duration);
        }

        // Implement any abstract methods from Evenement here
        // Add any required abstract method implementations based on your Evenement class
    }

    @BeforeAll
    static void setUpClass() {
        // Save original user.dir and set it to temp directory
        originalUserDir = System.getProperty("user.dir");
        System.setProperty("user.dir", tempDir.toString());

        // Initialize ObjectMapper
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @AfterAll
    static void tearDownClass() {
        // Restore original user.dir
        System.setProperty("user.dir", originalUserDir);
    }

    @BeforeEach
    void setUp() {
        // Create test participants
        testParticipant1 = new Participant("user1", "John", "Doe", "john.doe@email.com");
        testParticipant2 = new Participant("user2", "Jane", "Smith", "jane.smith@email.com");

        // Create test organisateurs
        testOrganisateur1 = new Organisateur();
        testOrganisateur2 = new Organisateur();

        // Create test evenements
        testEvenement1 = new ConcreteEvenement(1, "Concert", "Music Hall", LocalDateTime.of(2024, 6, 15, 20, 0), 300, Duration.ofHours(3));
        testEvenement2 = new ConcreteEvenement(2, "Conference", "Convention Center", LocalDateTime.of(2024, 6, 16, 9, 0), 258, Duration.ofHours(8));

        // Create conflicting event (same location and overlapping time)
        conflictingEvenement = new ConcreteEvenement(3, "Conflicting Event", "Music Hall", LocalDateTime.of(2024, 6, 15, 21, 0), 137, Duration.ofHours(2));
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up test files after each test
        deleteFileIfExists("ParticipantData.json");
        deleteFileIfExists("EvenementData.json");
        deleteFileIfExists("ParticipantsAuxEvenements.json");
        deleteFileIfExists("OrganisateurData.json");
    }

    private void deleteFileIfExists(String filename) throws IOException {
        Path filePath = Paths.get(tempDir.toString(), filename);
        Files.deleteIfExists(filePath);
    }

    // PARTICIPANT TESTS

    @Test
    @Order(1)
    @DisplayName("Add participant successfully")
    void testAddParticipantSuccess() throws ParticipantDejaExistantException {
        boolean result = Serialization.addParticipant(testParticipant1);

        assertTrue(result, "Participant should be added successfully");

        // Verify file was created and contains the participant
        File file = new File(tempDir.toString(), "ParticipantData.json");
        assertTrue(file.exists(), "Participant data file should be created");

        // Verify content
        List<Participant> participants = readParticipantsFromFile();
        assertEquals(1, participants.size(), "File should contain exactly one participant");
        assertEquals(testParticipant1.getId(), participants.get(0).getId(), "Participant ID should match");
    }

    @Test
    @Order(2)
    @DisplayName("Add multiple participants successfully")
    void testAddMultipleParticipants() throws ParticipantDejaExistantException {
        Serialization.addParticipant(testParticipant1);
        Serialization.addParticipant(testParticipant2);

        List<Participant> participants = readParticipantsFromFile();
        assertEquals(2, participants.size(), "File should contain two participants");
    }

    @Test
    @Order(3)
    @DisplayName("Throw exception when adding duplicate participant by ID")
    void testAddDuplicateParticipantById() throws ParticipantDejaExistantException {
        Serialization.addParticipant(testParticipant1);

        Participant duplicateById = new Participant("user1", "Different", "Name", "different@email.com");

        assertThrows(ParticipantDejaExistantException.class,
                () -> Serialization.addParticipant(duplicateById),
                "Should throw exception when adding participant with duplicate ID");
    }

    @Test
    @Order(4)
    @DisplayName("Throw exception when adding duplicate participant by email")
    void testAddDuplicateParticipantByEmail() throws ParticipantDejaExistantException {
        Serialization.addParticipant(testParticipant1);

        Participant duplicateByEmail = new Participant("differentUser", "Different", "Name", "john.doe@email.com");

        assertThrows(ParticipantDejaExistantException.class,
                () -> Serialization.addParticipant(duplicateByEmail),
                "Should throw exception when adding participant with duplicate email");
    }

    @Test
    @Order(5)
    @DisplayName("Save all participants successfully")
    void testSaveAllParticipants() {
        List<Participant> participants = List.of(testParticipant1, testParticipant2);

        assertDoesNotThrow(() -> Serialization.saveAllParticipant(participants),
                "Should not throw exception when saving participants");

        List<Participant> savedParticipants = readParticipantsFromFile();
        assertEquals(2, savedParticipants.size(), "Should save all participants");
    }

    // ORGANISATEUR TESTS

    @Test
    @Order(6)
    @DisplayName("Add organisateur successfully")
    void testAddOrganisateurSuccess() {
        boolean result = Serialization.addOrganisateur(testOrganisateur1);

        assertTrue(result, "Organisateur should be added successfully");

        File file = new File(tempDir.toString(), "OrganisateurData.json");
        assertTrue(file.exists(), "Organisateur data file should be created");

        List<Organisateur> organisateurs = readOrganisateursFromFile();
        assertEquals(1, organisateurs.size(), "File should contain exactly one organisateur");
        assertEquals(testOrganisateur1.getId(), organisateurs.get(0).getId(), "Organisateur ID should match");
    }

    @Test
    @Order(7)
    @DisplayName("Reject duplicate organisateur")
    void testAddDuplicateOrganisateur() {
        Serialization.addOrganisateur(testOrganisateur1);

        Organisateur duplicate = new Organisateur();
        boolean result = Serialization.addOrganisateur(duplicate);

        assertFalse(result, "Should reject duplicate organisateur");

        List<Organisateur> organisateurs = readOrganisateursFromFile();
        assertEquals(1, organisateurs.size(), "Should still have only one organisateur");
    }

    @Test
    @Order(8)
    @DisplayName("Save all organisateurs successfully")
    void testSaveAllOrganisateurs() {
        List<Organisateur> organisateurs = List.of(testOrganisateur1, testOrganisateur2);

        assertDoesNotThrow(() -> Serialization.saveAllOrganisateur(organisateurs),
                "Should not throw exception when saving organisateurs");

        List<Organisateur> savedOrganisateurs = readOrganisateursFromFile();
        assertEquals(2, savedOrganisateurs.size(), "Should save all organisateurs");
    }

    // EVENEMENT TESTS

    @Test
    @Order(9)
    @DisplayName("Add evenement successfully")
    void testAddEvenementSuccess() throws EvenementDejaExistantException {
        boolean result = Serialization.addEvenement(testEvenement1);

        assertTrue(result, "Evenement should be added successfully");

        File file = new File(tempDir.toString(), "EvenementData.json");
        assertTrue(file.exists(), "Evenement data file should be created");

        List<Evenement> evenements = readEvenementsFromFile();
        assertEquals(1, evenements.size(), "File should contain exactly one evenement");
        assertEquals(testEvenement1.getId(), evenements.get(0).getId(), "Evenement ID should match");
    }

    @Test
    @Order(10)
    @DisplayName("Add multiple non-conflicting evenements")
    void testAddMultipleNonConflictingEvenements() throws EvenementDejaExistantException {
        Serialization.addEvenement(testEvenement1);
        Serialization.addEvenement(testEvenement2);

        List<Evenement> evenements = readEvenementsFromFile();
        assertEquals(2, evenements.size(), "Should have two evenements");
    }

    @Test
    @Order(11)
    @DisplayName("Throw exception for conflicting evenements")
    void testAddConflictingEvenement() throws EvenementDejaExistantException {
        Serialization.addEvenement(testEvenement1);

        assertThrows(EvenementDejaExistantException.class,
                () -> Serialization.addEvenement(conflictingEvenement),
                "Should throw exception for conflicting evenement");
    }

    @Test
    @Order(12)
    @DisplayName("Allow evenements at same location but different dates")
    void testSameLocationDifferentDate() throws EvenementDejaExistantException {
        Serialization.addEvenement(testEvenement1);

        Evenement differentDate = new ConcreteEvenement(4, "Different Date Event", "Music Hall",
                LocalDateTime.of(2024, 6, 17, 20, 0), 483, Duration.ofHours(3));

        boolean result = Serialization.addEvenement(differentDate);
        assertTrue(result, "Should allow same location on different date");

        List<Evenement> evenements = readEvenementsFromFile();
        assertEquals(2, evenements.size(), "Should have two evenements");
    }

    // PARTICIPANT-EVENEMENT REGISTRATION TESTS

    @Test
    @Order(13)
    @DisplayName("Register participant to evenement successfully")
    void testAddParticipantsAuxEvenementsSuccess() throws EvenementDejaExistantException {
        boolean result = Serialization.addParticipantsAuxEvenements(testParticipant1, testEvenement1);

        assertTrue(result, "Registration should be successful");

        File file = new File(tempDir.toString(), "ParticipantsAuxEvenements.json");
        assertTrue(file.exists(), "Registration file should be created");

        Map<String, List<Integer>> registrations = readRegistrationsFromFile();
        assertTrue(registrations.containsKey(testParticipant1.getId()), "Should contain participant");
        assertTrue(registrations.get(testParticipant1.getId()).contains(testEvenement1.getId()),
                "Should contain event registration");
    }

    @Test
    @Order(14)
    @DisplayName("Register same participant to multiple evenements")
    void testRegisterParticipantToMultipleEvents() throws EvenementDejaExistantException {
        Serialization.addParticipantsAuxEvenements(testParticipant1, testEvenement1);
        Serialization.addParticipantsAuxEvenements(testParticipant1, testEvenement2);

        Map<String, List<Integer>> registrations = readRegistrationsFromFile();
        List<Integer> events = registrations.get(testParticipant1.getId());

        assertEquals(2, events.size(), "Participant should be registered to two events");
        assertTrue(events.contains(testEvenement1.getId()), "Should contain first event");
        assertTrue(events.contains(testEvenement2.getId()), "Should contain second event");
    }

    @Test
    @Order(15)
    @DisplayName("Reject duplicate registration")
    void testRejectDuplicateRegistration() throws EvenementDejaExistantException {
        Serialization.addParticipantsAuxEvenements(testParticipant1, testEvenement1);
        boolean result = Serialization.addParticipantsAuxEvenements(testParticipant1, testEvenement1);

        assertFalse(result, "Should reject duplicate registration");

        Map<String, List<Integer>> registrations = readRegistrationsFromFile();
        List<Integer> events = registrations.get(testParticipant1.getId());
        assertEquals(1, events.size(), "Should still have only one registration");
    }

    @Test
    @Order(16)
    @DisplayName("Register multiple participants to same evenement")
    void testRegisterMultipleParticipantsToSameEvent() throws EvenementDejaExistantException {
        Serialization.addParticipantsAuxEvenements(testParticipant1, testEvenement1);
        Serialization.addParticipantsAuxEvenements(testParticipant2, testEvenement1);

        Map<String, List<Integer>> registrations = readRegistrationsFromFile();

        assertEquals(2, registrations.size(), "Should have two participants registered");
        assertTrue(registrations.get(testParticipant1.getId()).contains(testEvenement1.getId()));
        assertTrue(registrations.get(testParticipant2.getId()).contains(testEvenement1.getId()));
    }

    // EDGE CASE TESTS

    @Test
    @Order(17)
    @DisplayName("Handle corrupted JSON file gracefully")
    void testHandleCorruptedFile() throws IOException, ParticipantDejaExistantException {
        // Create a corrupted JSON file
        Path corruptedFile = Paths.get(tempDir.toString(), "ParticipantData.json");
        Files.writeString(corruptedFile, "{ invalid json content }");

        // Should handle gracefully and start fresh
        boolean result = Serialization.addParticipant(testParticipant1);
        assertTrue(result, "Should handle corrupted file and add participant");

        List<Participant> participants = readParticipantsFromFile();
        assertEquals(1, participants.size(), "Should have one participant after recovery");
    }

    @Test
    @Order(18)
    @DisplayName("Handle empty file gracefully")
    void testHandleEmptyFile() throws IOException, ParticipantDejaExistantException {
        // Create an empty file
        Path emptyFile = Paths.get(tempDir.toString(), "ParticipantData.json");
        Files.createFile(emptyFile);

        boolean result = Serialization.addParticipant(testParticipant1);
        assertTrue(result, "Should handle empty file and add participant");

        List<Participant> participants = readParticipantsFromFile();
        assertEquals(1, participants.size(), "Should have one participant");
    }

    // HELPER METHODS

    private List<Participant> readParticipantsFromFile() {
        try {
            File file = new File(tempDir.toString(), "ParticipantData.json");
            if (!file.exists()) return new ArrayList<>();

            String content = Files.readString(file.toPath());
            return mapper.readValue(content, new TypeReference<List<Participant>>() {});
        } catch (Exception e) {
            fail("Failed to read participants from file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<Organisateur> readOrganisateursFromFile() {
        try {
            File file = new File(tempDir.toString(), "OrganisateurData.json");
            if (!file.exists()) return new ArrayList<>();

            String content = Files.readString(file.toPath());
            return mapper.readValue(content, new TypeReference<List<Organisateur>>() {});
        } catch (Exception e) {
            fail("Failed to read organisateurs from file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<Evenement> readEvenementsFromFile() {
        try {
            File file = new File(tempDir.toString(), "EvenementData.json");
            if (!file.exists()) return new ArrayList<>();

            String content = Files.readString(file.toPath());
            return mapper.readValue(content, new TypeReference<List<Evenement>>() {});
        } catch (Exception e) {
            fail("Failed to read evenements from file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private Map<String, List<Integer>> readRegistrationsFromFile() {
        try {
            File file = new File(tempDir.toString(), "ParticipantsAuxEvenements.json");
            if (!file.exists()) return new HashMap<>();

            String content = Files.readString(file.toPath());
            return mapper.readValue(content, new TypeReference<HashMap<String, List<Integer>>>() {});
        } catch (Exception e) {
            fail("Failed to read registrations from file: " + e.getMessage());
            return new HashMap<>();
        }
    }
}