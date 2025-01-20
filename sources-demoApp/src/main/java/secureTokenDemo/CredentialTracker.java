/**
 * File: CredentialTracker.java
 * Author: Jannis Günsche
 * Description: This class is a singleton that keeps track of all credentials
 *              that have been used with the application.
 */
package secureTokenDemo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import logger.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CredentialTracker {

    private static CredentialTracker instance = null;
    private List<String> credentials;


    private CredentialTracker() {
        credentials = new ArrayList<>();
    }

    public static CredentialTracker getInstance() {
        if (instance == null) {
            instance = new CredentialTracker();
        }
        return instance;
    }

    public void loadCredentials() {
        Logger.log("CredentialTracker", "Loading credentials from file... (File: " + App.CREDENTIAL_STORAGE_FILE + ")");

        ObjectMapper mapper = new ObjectMapper();
        File file = new File(App.CREDENTIAL_STORAGE_FILE);

        if (file.exists()) {
            try {
                JsonNode rootNode = mapper.readTree(file);
                JsonNode credKeysNode = rootNode.path(App.CREDENTIAL_JSON_KEY);
                if (credKeysNode.isArray()) {
                    for (JsonNode node : credKeysNode) {
                        credentials.add(node.asText());
                    }
                }
            } catch (IOException e) {
                Logger.log("CredentialTracker", "Error loading credentials: " + e.getMessage());
            }
        }

        AppLogic.syncComboBoxWithCredentialTracker();
    }

    private void saveCredentials() {
        Logger.log("CredentialTracker", "Saving credentials to file... (File: " + App.CREDENTIAL_STORAGE_FILE + ")");

        ObjectMapper mapper = new ObjectMapper();

        // Remove duplicates
        Set<String> uniqueCredentials = new HashSet<>(credentials);

        ArrayNode credArray = mapper.createArrayNode();
        uniqueCredentials.forEach(credArray::add);

        try {
            File file = new File(App.CREDENTIAL_STORAGE_FILE);
            if (!file.exists()) {
                Files.createFile(Paths.get(App.CREDENTIAL_STORAGE_FILE));
            }
            mapper.writeValue(file, mapper.createObjectNode().set(App.CREDENTIAL_JSON_KEY, credArray));
        } catch (IOException e) {
            Logger.log("CredentialTracker", "Error saving credentials: " + e.getMessage());
        }
    }

    public void addCredential(String credential) {
        Logger.log("CredentialTracker", "Adding credential: " + credential);
        credentials.add(credential);
        saveCredentials();
    }

    public List<String> getCredentials() {
        Logger.log("CredentialTracker", "Returning all credentials.");
        return new ArrayList<>(credentials);
    }
}