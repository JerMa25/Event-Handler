/*package com.example.eventhandler.test.persistence;

import com.example.eventhandler.models.personne.Participant;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DeserializationTest {

    @TempDir
    static Path tempDir;

    private static File testDataFile;
    private static String originalDataFilePath;
    private ObjectMapper mapper;

    @BeforeAll
    @SuppressWarnings("unchecked")
    static void setUpClass() throws Exception {
        // Create a temporary test file
        testDataFile = tempDir.resolve("TestData.json").toFile();

        // Store original DATA_FILE_PATH and replace it with test file path
        Field dataFilePathField = Deserialization.class.getDeclaredField("DATA_FILE_PATH");
        dataFilePathField.setAccessible(true);
        originalDataFilePath = (String) dataFilePathField.get(null);
        dataFilePathField.set(null, testDataFile.getAbsolutePath());
    }

    @AfterAll
    @SuppressWarnings("unchecked")
    static void tearDownClass() throws Exception {
        // Restore original DATA_FILE_PATH
        Field dataFilePathField = Deserialization.class.getDeclaredField("DATA_FILE_PATH");
        dataFilePathField.setAccessible(true);
        dataFilePathField.set(null, originalDataFilePath);
    }

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        // Clean up test file before each test
        if (testDataFile.exists()) {
            testDataFile.delete();
        }
    }

    @Test
    @Order(1)
    @DisplayName("Test getAllParticipants with empty/non-existent file")
    void testGetAllParticipants_EmptyFile() {
        // Test with non-existent file
        List<Participant> participants = Deserialization.getAllParticipants();

        assertNotNull(participants);
        assertTrue(participants.isEmpty());
        assertEquals(0, participants.size());
    }

    @Test
    @Order(2)
    @DisplayName("Test getAllParticipants with valid data")
    void testGetAllParticipants_ValidData() throws IOException {
        // Create test data
        String jsonData = """
            [
                {
                    "id": "user1",
                    "nom": "John Doe",
                    "email": "john@example.com",
                    "password": "pass123"
                },
                {
                    "id": "user2",
                    "nom": "Jane Smith",
                    "email": "jane@example.com",
                    "password": "pass456"
                }
            ]
            """;

        // Write test data to file
        try (FileWriter writer = new FileWriter(testDataFile)) {
            writer.write(jsonData);
        }

        // Test method
        List<Participant> participants = Deserialization.getAllParticipants();

        assertNotNull(participants);
        assertEquals(2, participants.size());
        assertEquals("user1", participants.get(0).getId());
        assertEquals("John Doe", participants.get(0).getNom());
        assertEquals("john@example.com", participants.get(0).getEmail());
        assertEquals("user2", participants.get(1).getId());
        assertEquals("Jane Smith", participants.get(1).getNom());
    }

    @Test
    @Order(3)
    @DisplayName("Test getAllParticipants with invalid JSON")
    void testGetAllParticipants_InvalidJSON() throws IOException {
        // Create invalid JSON data
        String invalidJsonData = "{ invalid json content }";

        try (FileWriter writer = new FileWriter(testDataFile)) {
            writer.write(invalidJsonData);
        }

        // Test method - should return empty list on error
        List<Participant> participants = Deserialization.getAllParticipants();

        assertNotNull(participants);
        assertTrue(participants.isEmpty());
    }

    @Test
    @Order(4)
    @DisplayName("Test getParticipantByUsername - participant exists")
    void testGetParticipantByUsername_Exists() throws IOException {
        // Create test data
        String jsonData = """
            [
                {
                    "id": "testUser",
                    "nom": "Test User",
                    "email": "test@example.com",
                    "password": "testpass"
                },
                {
                    "id": "anotherUser",
                    "nom": "Another User",
                    "email": "another@example.com",
                    "password": "anotherpass"
                }
            ]
            """;

        try (FileWriter writer = new FileWriter(testDataFile)) {
            writer.write(jsonData);
        }

        // Test method
        Optional<Participant> result = Deserialization.getParticipantByUsername("testUser");

        assertTrue(result.isPresent());
        assertEquals("testUser", result.get().getId());
        assertEquals("Test User", result.get().getNom());
        assertEquals("test@example.com", result.get().getEmail());
    }

    @Test
    @Order(5)
    @DisplayName("Test getParticipantByUsername - participant does not exist")
    void testGetParticipantByUsername_NotExists() throws IOException {
        // Create test data
        String jsonData = """
            [
                {
                    "id": "user1",
                    "nom": "User One",
                    "email": "user1@example.com",
                    "password": "pass1"
                }
            ]
            """;

        try (FileWriter writer = new FileWriter(testDataFile)) {
            writer.write(jsonData);
        }

        // Test method with non-existent username
        Optional<Participant> result = Deserialization.getParticipantByUsername("nonExistentUser");

        assertFalse(result.isPresent());
        assertTrue(result.isEmpty());
    }

    @Test
    @Order(6)
    @DisplayName("Test getParticipantByUsername with empty file")
    void testGetParticipantByUsername_EmptyFile() {
        // Test with non-existent file
        Optional<Participant> result = Deserialization.getParticipantByUsername("anyUser");

        assertFalse(result.isPresent());
        assertTrue(result.isEmpty());
    }

    @Test
    @Order(7)
    @DisplayName("Test participantExists - true case")
    void testParticipantExists_True() throws IOException {
        // Create test data
        String jsonData = """
            [
                {
                    "id": "existingUser",
                    "nom": "Existing User",
                    "email": "existing@example.com",
                    "password": "existingpass"
                }
            ]
            """;

        try (FileWriter writer = new FileWriter(testDataFile)) {
            writer.write(jsonData);
        }

        // Test method
        boolean exists = Deserialization.participantExists("existingUser");

        assertTrue(exists);
    }

    @Test
    @Order(8)
    @DisplayName("Test participantExists - false case")
    void testParticipantExists_False() throws IOException {
        // Create test data
        String jsonData = """
            [
                {
                    "id": "someUser",
                    "nom": "Some User",
                    "email": "some@example.com",
                    "password": "somepass"
                }
            ]
            """;

        try (FileWriter writer = new FileWriter(testDataFile)) {
            writer.write(jsonData);
        }

        // Test method with non-existent username
        boolean exists = Deserialization.participantExists("nonExistentUser");

        assertFalse(exists);
    }

    @Test
    @Order(9)
    @DisplayName("Test getParticipantCount with data")
    void testGetParticipantCount_WithData() throws IOException {
        // Create test data with 3 participants
        String jsonData = """
            [
                {
                    "id": "user1",
                    "nom": "User One",
                    "email": "user1@example.com",
                    "password": "pass1"
                },
                {
                    "id": "user2",
                    "nom": "User Two",
                    "email": "user2@example.com",
                    "password": "pass2"
                },
                {
                    "id": "user3",
                    "nom": "User Three",
                    "email": "user3@example.com",
                    "password": "pass3"
                }
            ]
            """;

        try (FileWriter writer = new FileWriter(testDataFile)) {
            writer.write(jsonData);
        }

        // Test method
        int count = Deserialization.getParticipantCount();

        assertEquals(3, count);
    }

    @Test
    @Order(10)
    @DisplayName("Test getParticipantCount with empty file")
    void testGetParticipantCount_EmptyFile() {
        // Test with non-existent file
        int count = Deserialization.getParticipantCount();

        assertEquals(0, count);
    }

    @Test
    @Order(11)
    @DisplayName("Test case sensitivity in username search")
    void testUsernameCaseSensitivity() throws IOException {
        // Create test data
        String jsonData = """
            [
                {
                    "id": "TestUser",
                    "nom": "Test User",
                    "email": "test@example.com",
                    "password": "testpass"
                }
            ]
            """;

        try (FileWriter writer = new FileWriter(testDataFile)) {
            writer.write(jsonData);
        }

        // Test case sensitivity
        assertTrue(Deserialization.participantExists("TestUser"));
        assertFalse(Deserialization.participantExists("testuser"));
        assertFalse(Deserialization.participantExists("TESTUSER"));
    }

    @Test
    @Order(12)
    @DisplayName("Test with null and empty username")
    void testNullAndEmptyUsername() throws IOException {
        // Create test data
        String jsonData = """
            [
                {
                    "id": "validUser",
                    "nom": "Valid User",
                    "email": "valid@example.com",
                    "password": "validpass"
                }
            ]
            """;

        try (FileWriter writer = new FileWriter(testDataFile)) {
            writer.write(jsonData);
        }

        // Test null username
        assertFalse(Deserialization.participantExists(null));
        Optional<Participant> nullResult = Deserialization.getParticipantByUsername(null);
        assertTrue(nullResult.isEmpty());

        // Test empty username
        assertFalse(Deserialization.participantExists(""));
        Optional<Participant> emptyResult = Deserialization.getParticipantByUsername("");
        assertTrue(emptyResult.isEmpty());
    }
}*/