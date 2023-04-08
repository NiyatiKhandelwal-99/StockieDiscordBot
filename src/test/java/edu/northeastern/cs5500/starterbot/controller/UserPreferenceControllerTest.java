package edu.northeastern.cs5500.starterbot.controller;

import static com.google.common.truth.Truth.assertThat;

import edu.northeastern.cs5500.starterbot.model.UserPreference;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import org.junit.jupiter.api.Test;

class UserPreferenceControllerTest {
    // TODO: replace with discord IDs that are more real
    static final String USER_ID_1 = "23h5ikoqaehokljhaoe";
    static final String USER_ID_2 = "2kjfksdjdkhokljhaoe";
    static final String PREFERRED_NAME_1 = "Joe";
    static final String PREFERRED_NAME_2 = "Penny";

    private UserPreferenceController getUserPreferenceController() {
        UserPreferenceController userPreferenceController =
                new UserPreferenceController(new InMemoryRepository<>());
        return userPreferenceController;
    }

    @Test
    void testSetNameActuallySetsName() {
        // setup
        UserPreferenceController userPreferenceController = getUserPreferenceController();

        // precondition
        assertThat(userPreferenceController.getPreferredNameForUser(USER_ID_1))
                .isNotEqualTo(PREFERRED_NAME_1);

        // mutation
        userPreferenceController.setPreferredNameForUser(USER_ID_1, PREFERRED_NAME_1);

        // postcondition
        assertThat(userPreferenceController.getPreferredNameForUser(USER_ID_1))
                .isEqualTo(PREFERRED_NAME_1);
    }

    @Test
    void testSetNameOverwritesOldName() {
        UserPreferenceController userPreferenceController = getUserPreferenceController();
        userPreferenceController.setPreferredNameForUser(USER_ID_1, PREFERRED_NAME_1);
        assertThat(userPreferenceController.getPreferredNameForUser(USER_ID_1))
                .isEqualTo(PREFERRED_NAME_1);

        userPreferenceController.setPreferredNameForUser(USER_ID_1, PREFERRED_NAME_2);
        assertThat(userPreferenceController.getPreferredNameForUser(USER_ID_1))
                .isEqualTo(PREFERRED_NAME_2);
    }

    @Test
    void testSetNameOnlyOverwritesTargetUser() {
        UserPreferenceController userPreferenceController = getUserPreferenceController();

        userPreferenceController.setPreferredNameForUser(USER_ID_1, PREFERRED_NAME_1);
        userPreferenceController.setPreferredNameForUser(USER_ID_2, PREFERRED_NAME_2);

        assertThat(userPreferenceController.getPreferredNameForUser(USER_ID_1))
                .isEqualTo(PREFERRED_NAME_1);
        assertThat(userPreferenceController.getPreferredNameForUser(USER_ID_2))
                .isEqualTo(PREFERRED_NAME_2);
    }

    @Test
    public void testUserPreferenceEqualHashCode() {
        UserPreference sourceUserPreference = new UserPreference();
        sourceUserPreference.setDiscordUserId(USER_ID_1);
        sourceUserPreference.setPreferredName(PREFERRED_NAME_1);
        UserPreference sameUserPreference = new UserPreference();
        sameUserPreference.setDiscordUserId(USER_ID_1);
        sameUserPreference.setPreferredName(PREFERRED_NAME_1);

        UserPreference targetUserPreference = new UserPreference();
        targetUserPreference.setDiscordUserId(USER_ID_2);
        targetUserPreference.setPreferredName(PREFERRED_NAME_2);
        assertThat(sourceUserPreference.equals(sameUserPreference)).isTrue();
        assertThat(
                        sourceUserPreference
                                .getDiscordUserId()
                                .equals(sameUserPreference.getDiscordUserId()))
                .isTrue();
        assertThat(sourceUserPreference.hashCode() == sameUserPreference.hashCode()).isTrue();
        assertThat(sourceUserPreference.equals(sourceUserPreference)).isTrue();
        assertThat(sourceUserPreference.hashCode() == sourceUserPreference.hashCode()).isTrue();
        assertThat(sourceUserPreference.toString().equals(sourceUserPreference.toString()))
                .isTrue();
    }

    @Test
    public void testUserPreferenceNotEqualHashCode() {
        UserPreference sourceUserPreference = new UserPreference();
        sourceUserPreference.setDiscordUserId(USER_ID_1);
        sourceUserPreference.setPreferredName(PREFERRED_NAME_1);

        UserPreference targetUserPreference = new UserPreference();
        targetUserPreference.setDiscordUserId(USER_ID_2);
        targetUserPreference.setPreferredName(PREFERRED_NAME_2);
        assertThat(sourceUserPreference.equals(targetUserPreference)).isFalse();
        assertThat(
                        sourceUserPreference
                                .getDiscordUserId()
                                .equals(targetUserPreference.getDiscordUserId()))
                .isFalse();
        assertThat(sourceUserPreference.getDiscordUserId().equals("test")).isFalse();
        assertThat(sourceUserPreference.hashCode() == targetUserPreference.hashCode()).isFalse();
        assertThat(sourceUserPreference.equals(sourceUserPreference)).isTrue();
        assertThat(sourceUserPreference.hashCode() == sourceUserPreference.hashCode()).isTrue();
        assertThat(sourceUserPreference.toString().equals(targetUserPreference.toString()))
                .isFalse();
    }
}
