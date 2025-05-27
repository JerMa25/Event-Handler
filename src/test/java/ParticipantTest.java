import com.example.eventhandler.models.personne.Participant;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de tests unitaires pour la classe Participant
 * Utilise JUnit 5 avec des tests paramétrés et des assertions modernes
 */
@DisplayName("Tests de la classe Participant")
class ParticipantTest {

    private Participant participant;
    private ObjectMapper objectMapper;

    // Constantes pour les tests
    private static final String VALID_ID = "user123";
    private static final String VALID_NOM = "Jean Dupont";
    private static final String VALID_EMAIL = "jean.dupont@example.com";
    private static final String VALID_PASSWORD = "motdepasse123";

    @BeforeEach
    @DisplayName("Initialisation avant chaque test")
    void setUp() {
        participant = new Participant();
        objectMapper = new ObjectMapper();
    }

    @AfterEach
    @DisplayName("Nettoyage après chaque test")
    void tearDown() {
        participant = null;
    }

    // ============== TESTS DES CONSTRUCTEURS ==============

    @Test
    @DisplayName("Test du constructeur par défaut")
    void testConstructeurParDefaut() {
        // Given & When
        Participant newParticipant = new Participant();

        // Then
        assertNotNull(newParticipant, "Le participant ne devrait pas être null");
        assertNull(newParticipant.getId(), "L'ID devrait être null par défaut");
        assertNull(newParticipant.getNom(), "Le nom devrait être null par défaut");
        assertNull(newParticipant.getEmail(), "L'email devrait être null par défaut");
        assertNull(newParticipant.getPassword(), "Le mot de passe devrait être null par défaut");
    }

    @Test
    @DisplayName("Test du constructeur avec paramètres valides")
    void testConstructeurAvecParametres() {
        // Given & When
        Participant newParticipant = new Participant(VALID_ID, VALID_NOM, VALID_EMAIL, VALID_PASSWORD);

        // Then
        assertAll("Vérification de tous les attributs",
                () -> assertNotNull(newParticipant, "Le participant ne devrait pas être null"),
                () -> assertEquals(VALID_ID, newParticipant.getId(), "L'ID devrait être correctement assigné"),
                () -> assertEquals(VALID_NOM, newParticipant.getNom(), "Le nom devrait être correctement assigné"),
                () -> assertEquals(VALID_EMAIL, newParticipant.getEmail(), "L'email devrait être correctement assigné"),
                () -> assertEquals(VALID_PASSWORD, newParticipant.getPassword(), "Le mot de passe devrait être correctement assigné")
        );
    }

    @Test
    @DisplayName("Test du constructeur avec paramètres null")
    void testConstructeurAvecParametresNull() {
        // Given & When
        Participant newParticipant = new Participant(null, null, null, null);

        // Then
        assertAll("Vérification que les valeurs null sont acceptées",
                () -> assertNotNull(newParticipant, "Le participant ne devrait pas être null"),
                () -> assertNull(newParticipant.getId(), "L'ID devrait être null"),
                () -> assertNull(newParticipant.getNom(), "Le nom devrait être null"),
                () -> assertNull(newParticipant.getEmail(), "L'email devrait être null"),
                () -> assertNull(newParticipant.getPassword(), "Le mot de passe devrait être null")
        );
    }

    // ============== TESTS DES GETTERS ==============

    @Test
    @DisplayName("Test des getters après initialisation par constructeur")
    void testGettersApresInitialisation() {
        // Given
        Participant testParticipant = new Participant(VALID_ID, VALID_NOM, VALID_EMAIL, VALID_PASSWORD);

        // When & Then
        assertAll("Vérification de tous les getters",
                () -> assertEquals(VALID_ID, testParticipant.getId(), "getId() devrait retourner l'ID correct"),
                () -> assertEquals(VALID_NOM, testParticipant.getNom(), "getNom() devrait retourner le nom correct"),
                () -> assertEquals(VALID_EMAIL, testParticipant.getEmail(), "getEmail() devrait retourner l'email correct"),
                () -> assertEquals(VALID_PASSWORD, testParticipant.getPassword(), "getPassword() devrait retourner le mot de passe correct")
        );
    }

    @Test
    @DisplayName("Test des getters avec constructeur par défaut")
    void testGettersAvecConstructeurDefaut() {
        // When & Then
        assertAll("Vérification que tous les getters retournent null",
                () -> assertNull(participant.getId(), "getId() devrait retourner null"),
                () -> assertNull(participant.getNom(), "getNom() devrait retourner null"),
                () -> assertNull(participant.getEmail(), "getEmail() devrait retourner null"),
                () -> assertNull(participant.getPassword(), "getPassword() devrait retourner null")
        );
    }

    // ============== TESTS DES SETTERS ==============

    @Test
    @DisplayName("Test des setters avec valeurs valides")
    void testSettersAvecValeursValides() {
        // When
        participant.setId(VALID_ID);
        participant.setNom(VALID_NOM);
        participant.setEmail(VALID_EMAIL);
        participant.setPassword(VALID_PASSWORD);

        // Then
        assertAll("Vérification que tous les setters fonctionnent",
                () -> assertEquals(VALID_ID, participant.getId(), "setId() devrait définir l'ID correctement"),
                () -> assertEquals(VALID_NOM, participant.getNom(), "setNom() devrait définir le nom correctement"),
                () -> assertEquals(VALID_EMAIL, participant.getEmail(), "setEmail() devrait définir l'email correctement"),
                () -> assertEquals(VALID_PASSWORD, participant.getPassword(), "setPassword() devrait définir le mot de passe correctement")
        );
    }

    @Test
    @DisplayName("Test des setters avec valeurs null")
    void testSettersAvecValeursNull() {
        // Given - Initialiser avec des valeurs
        participant.setId(VALID_ID);
        participant.setNom(VALID_NOM);
        participant.setEmail(VALID_EMAIL);
        participant.setPassword(VALID_PASSWORD);

        // When - Remettre à null
        participant.setId(null);
        participant.setNom(null);
        participant.setEmail(null);
        participant.setPassword(null);

        // Then
        assertAll("Vérification que les setters acceptent null",
                () -> assertNull(participant.getId(), "setId(null) devrait définir l'ID à null"),
                () -> assertNull(participant.getNom(), "setNom(null) devrait définir le nom à null"),
                () -> assertNull(participant.getEmail(), "setEmail(null) devrait définir l'email à null"),
                () -> assertNull(participant.getPassword(), "setPassword(null) devrait définir le mot de passe à null")
        );
    }

    @ParameterizedTest
    @DisplayName("Test du setter setId avec différentes valeurs")
    @ValueSource(strings = {"user1", "admin", "test_user", "123", "user@domain"})
    void testSetIdAvecDifferentesValeurs(String id) {
        // When
        participant.setId(id);

        // Then
        assertEquals(id, participant.getId(), "setId() devrait définir correctement l'ID : " + id);
    }

    @ParameterizedTest
    @DisplayName("Test du setter setEmail avec différents formats")
    @ValueSource(strings = {
            "test@example.com",
            "user.name@domain.org",
            "admin123@company.co.uk",
            "simple@test.fr"
    })
    void testSetEmailAvecDifferentsFormats(String email) {
        // When
        participant.setEmail(email);

        // Then
        assertEquals(email, participant.getEmail(), "setEmail() devrait définir correctement l'email : " + email);
    }

    @ParameterizedTest
    @DisplayName("Test des setters avec chaînes vides")
    @NullAndEmptySource
    void testSettersAvecChainesVides(String valeur) {
        // When
        participant.setId(valeur);
        participant.setNom(valeur);
        participant.setEmail(valeur);
        participant.setPassword(valeur);

        // Then
        assertAll("Vérification que les setters acceptent les chaînes vides et null",
                () -> assertEquals(valeur, participant.getId(), "setId() devrait accepter : " + valeur),
                () -> assertEquals(valeur, participant.getNom(), "setNom() devrait accepter : " + valeur),
                () -> assertEquals(valeur, participant.getEmail(), "setEmail() devrait accepter : " + valeur),
                () -> assertEquals(valeur, participant.getPassword(), "setPassword() devrait accepter : " + valeur)
        );
    }

    // ============== TESTS DE LA MÉTHODE toString() ==============

    @Test
    @DisplayName("Test de toString() avec toutes les valeurs définies")
    void testToStringAvecValeursDefinies() {
        // Given
        participant.setId(VALID_ID);
        participant.setNom(VALID_NOM);
        participant.setEmail(VALID_EMAIL);
        participant.setPassword(VALID_PASSWORD);

        // When
        String result = participant.toString();

        // Then
        assertAll("Vérification du contenu de toString()",
                () -> assertNotNull(result, "toString() ne devrait pas retourner null"),
                () -> assertTrue(result.contains(VALID_ID), "toString() devrait contenir l'ID"),
                () -> assertTrue(result.contains(VALID_NOM), "toString() devrait contenir le nom"),
                () -> assertTrue(result.contains(VALID_EMAIL), "toString() devrait contenir l'email"),
                () -> assertTrue(result.contains("Participant"), "toString() devrait contenir 'Participant'"),
                () -> assertFalse(result.contains(VALID_PASSWORD), "toString() ne devrait PAS contenir le mot de passe pour la sécurité")
        );
    }

    @Test
    @DisplayName("Test de toString() avec valeurs null")
    void testToStringAvecValeursNull() {
        // When
        String result = participant.toString();

        // Then
        assertAll("Vérification de toString() avec valeurs null",
                () -> assertNotNull(result, "toString() ne devrait pas retourner null même avec des valeurs null"),
                () -> assertTrue(result.contains("Participant"), "toString() devrait toujours contenir 'Participant'"),
                () -> assertTrue(result.contains("null"), "toString() devrait contenir 'null' pour les valeurs non définies")
        );
    }

    @Test
    @DisplayName("Test du format exact de toString()")
    void testFormatExactToString() {
        // Given
        participant.setId("test123");
        participant.setNom("Test User");
        participant.setEmail("test@example.com");

        // When
        String result = participant.toString();

        // Then
        String expectedFormat = "Participant [id=test123, nom=Test User email=test@example.com]";
        assertEquals(expectedFormat, result, "toString() devrait respecter le format exact attendu");
    }

    // ============== TESTS DE SÉRIALISATION JSON ==============

    @Test
    @DisplayName("Test de sérialisation JSON")
    void testSerialisationJSON() throws Exception {
        // Given
        Participant testParticipant = new Participant(VALID_ID, VALID_NOM, VALID_EMAIL, VALID_PASSWORD);

        // When
        String json = objectMapper.writeValueAsString(testParticipant);

        // Then
        assertAll("Vérification de la sérialisation JSON",
                () -> assertNotNull(json, "La sérialisation ne devrait pas retourner null"),
                () -> assertTrue(json.contains(VALID_ID), "Le JSON devrait contenir l'ID"),
                () -> assertTrue(json.contains(VALID_NOM), "Le JSON devrait contenir le nom"),
                () -> assertTrue(json.contains(VALID_EMAIL), "Le JSON devrait contenir l'email"),
                () -> assertTrue(json.contains(VALID_PASSWORD), "Le JSON devrait contenir le mot de passe")
        );
    }

    @Test
    @DisplayName("Test de désérialisation JSON")
    void testDeserialisationJSON() throws Exception {
        // Given
        String json = "{\"username\":\"" + VALID_ID + "\",\"nom\":\"" + VALID_NOM +
                "\",\"email\":\"" + VALID_EMAIL + "\",\"password\":\"" + VALID_PASSWORD + "\"}";

        // When
        Participant deserializedParticipant = objectMapper.readValue(json, Participant.class);

        // Then
        assertAll("Vérification de la désérialisation JSON",
                () -> assertNotNull(deserializedParticipant, "La désérialisation ne devrait pas retourner null"),
                () -> assertEquals(VALID_ID, deserializedParticipant.getId(), "L'ID devrait être correctement désérialisé"),
                () -> assertEquals(VALID_NOM, deserializedParticipant.getNom(), "Le nom devrait être correctement désérialisé"),
                () -> assertEquals(VALID_EMAIL, deserializedParticipant.getEmail(), "L'email devrait être correctement désérialisé"),
                () -> assertEquals(VALID_PASSWORD, deserializedParticipant.getPassword(), "Le mot de passe devrait être correctement désérialisé")
        );
    }

    @Test
    @DisplayName("Test de cycle sérialisation/désérialisation")
    void testCycleSerialisationDeserialisation() throws Exception {
        // Given
        Participant original = new Participant(VALID_ID, VALID_NOM, VALID_EMAIL, VALID_PASSWORD);

        // When
        String json = objectMapper.writeValueAsString(original);
        Participant restored = objectMapper.readValue(json, Participant.class);

        // Then
        assertAll("Vérification du cycle complet",
                () -> assertEquals(original.getId(), restored.getId(), "L'ID devrait être préservé"),
                () -> assertEquals(original.getNom(), restored.getNom(), "Le nom devrait être préservé"),
                () -> assertEquals(original.getEmail(), restored.getEmail(), "L'email devrait être préservé"),
                () -> assertEquals(original.getPassword(), restored.getPassword(), "Le mot de passe devrait être préservé")
        );
    }

    // ============== TESTS D'ÉGALITÉ ET COMPARAISON ==============

    @Test
    @DisplayName("Test de comparaison d'objets identiques")
    void testComparaisonObjetsIdentiques() {
        // Given
        Participant p1 = new Participant(VALID_ID, VALID_NOM, VALID_EMAIL, VALID_PASSWORD);
        Participant p2 = new Participant(VALID_ID, VALID_NOM, VALID_EMAIL, VALID_PASSWORD);

        // When & Then
        // Note: La classe ne surcharge pas equals(), donc on teste l'identité des attributs
        assertAll("Vérification que les objets ont les mêmes attributs",
                () -> assertEquals(p1.getId(), p2.getId(), "Les IDs devraient être égaux"),
                () -> assertEquals(p1.getNom(), p2.getNom(), "Les noms devraient être égaux"),
                () -> assertEquals(p1.getEmail(), p2.getEmail(), "Les emails devraient être égaux"),
                () -> assertEquals(p1.getPassword(), p2.getPassword(), "Les mots de passe devraient être égaux")
        );
    }

    // ============== TESTS DE CAS LIMITES ==============

    @Test
    @DisplayName("Test avec des chaînes très longues")
    void testAvecChainesTresLongues() {
        // Given
        String longString = "a".repeat(1000);

        // When
        participant.setId(longString);
        participant.setNom(longString);
        participant.setEmail(longString);
        participant.setPassword(longString);

        // Then
        assertAll("Vérification que les chaînes longues sont acceptées",
                () -> assertEquals(longString, participant.getId(), "setId() devrait accepter les chaînes longues"),
                () -> assertEquals(longString, participant.getNom(), "setNom() devrait accepter les chaînes longues"),
                () -> assertEquals(longString, participant.getEmail(), "setEmail() devrait accepter les chaînes longues"),
                () -> assertEquals(longString, participant.getPassword(), "setPassword() devrait accepter les chaînes longues")
        );
    }

    @Test
    @DisplayName("Test avec des caractères spéciaux")
    void testAvecCaracteresSpeciaux() {
        // Given
        String specialChars = "àáâãäåæçèéêëìíîïñòóôõöøùúûüý!@#$%^&*()";

        // When
        participant.setId(specialChars);
        participant.setNom(specialChars);
        participant.setEmail(specialChars);
        participant.setPassword(specialChars);

        // Then
        assertAll("Vérification que les caractères spéciaux sont acceptés",
                () -> assertEquals(specialChars, participant.getId(), "setId() devrait accepter les caractères spéciaux"),
                () -> assertEquals(specialChars, participant.getNom(), "setNom() devrait accepter les caractères spéciaux"),
                () -> assertEquals(specialChars, participant.getEmail(), "setEmail() devrait accepter les caractères spéciaux"),
                () -> assertEquals(specialChars, participant.getPassword(), "setPassword() devrait accepter les caractères spéciaux")
        );
    }

    // ============== TESTS DE PERFORMANCE ==============

    @Test
    @DisplayName("Test de performance - création de multiples instances")
    void testPerformanceCreationMultiplesInstances() {
        // Given
        int nombreInstances = 1000;
        long startTime = System.currentTimeMillis();

        // When
        for (int i = 0; i < nombreInstances; i++) {
            new Participant("id" + i, "nom" + i, "email" + i + "@test.com", "password" + i);
        }

        // Then
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        assertTrue(duration < 1000,
                "La création de " + nombreInstances + " instances devrait prendre moins de 1 seconde (actuel: " + duration + "ms)");
    }

    // ============== TEST GLOBAL D'INTÉGRATION ==============

    @Test
    @DisplayName("Test d'intégration complet")
    void testIntegrationComplet() {
        // Given
        String id = "integration_test";
        String nom = "Utilisateur Test";
        String email = "integration@test.com";
        String password = "testPassword123";

        // When - Test du cycle de vie complet
        Participant testParticipant = new Participant();

        // Définition des valeurs
        testParticipant.setId(id);
        testParticipant.setNom(nom);
        testParticipant.setEmail(email);
        testParticipant.setPassword(password);

        // Vérification des valeurs
        String toStringResult = testParticipant.toString();

        // Modification des valeurs
        testParticipant.setNom("Nouveau Nom");
        testParticipant.setEmail("nouveau@email.com");

        // Then
        assertAll("Test d'intégration complet",
                () -> assertEquals(id, testParticipant.getId(), "L'ID devrait être préservé"),
                () -> assertEquals("Nouveau Nom", testParticipant.getNom(), "Le nom devrait être mis à jour"),
                () -> assertEquals("nouveau@email.com", testParticipant.getEmail(), "L'email devrait être mis à jour"),
                () -> assertEquals(password, testParticipant.getPassword(), "Le mot de passe devrait être préservé"),
                () -> assertTrue(toStringResult.contains(nom), "toString() devrait avoir contenu l'ancien nom"),
                () -> assertTrue(toStringResult.contains(email), "toString() devrait avoir contenu l'ancien email")
        );
    }
}