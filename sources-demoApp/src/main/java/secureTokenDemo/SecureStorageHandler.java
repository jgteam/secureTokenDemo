/**
 * File: SecureStorageHandler.java
 * Author: Jannis Günsche
 * Description: This class is a singleton that provides access to the secure storage.
 */
package secureTokenDemo;

import com.microsoft.credentialstorage.SecretStore;
import com.microsoft.credentialstorage.StorageProvider;
import com.microsoft.credentialstorage.model.StoredToken;
import logger.Logger;

/**
 * The type Secure storage handler.
 */
public class SecureStorageHandler {

    private static SecureStorageHandler instance = null;

    // Secure storage for credentials
    private final SecretStore<StoredToken> credentialStorage;

    private SecureStorageHandler() {
        // Initialize secure storage
        credentialStorage = StorageProvider.getTokenStorage(true, StorageProvider.SecureOption.REQUIRED);
    }

    private static SecureStorageHandler getInstance() {
        if (instance == null) {
            Logger.log("SecureStorageHandler", "Creating new instance...");
            instance = new SecureStorageHandler();
        }
        return instance;
    }

    /**
     * Gets stored credential.
     *
     * @param credKey the cred key
     * @return the stored credential
     */
    public static StoredToken getStoredCredential(String credKey) {
        Logger.log("SecureStorageHandler", "Retrieving credential with key: " + credKey);
        try {
            return getInstance().credentialStorage.get(credKey);
        } catch (java.lang.Error e) {
            Logger.log("SecureStorageHandler", "Error retrieving credential: " + e.getMessage());
            return null;
        } catch (Exception e) {
            Logger.log("SecureStorageHandler", "Exception retrieving credential: " + e.getMessage());
            return null;
        }
    }

    /**
     * Store credential
     *
     * @param credKey    the cred key
     * @param credential the credential
     * @return the boolean
     */
    public static boolean storeCredential(String credKey, StoredToken credential) {
        Logger.log("SecureStorageHandler", "Storing credential with key: " + credKey);
        return getInstance().credentialStorage.add(credKey, credential);
    }
}
