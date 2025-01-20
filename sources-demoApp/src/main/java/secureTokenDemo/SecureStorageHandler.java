package secureTokenDemo;

import com.microsoft.credentialstorage.SecretStore;
import com.microsoft.credentialstorage.StorageProvider;
import com.microsoft.credentialstorage.model.StoredToken;
import logger.Logger;

public class SecureStorageHandler {

    private static SecureStorageHandler instance = null;

    private final SecretStore<StoredToken> credentialStorage;

    private SecureStorageHandler() {
        credentialStorage = StorageProvider.getTokenStorage(true, StorageProvider.SecureOption.REQUIRED);
    }

    private static SecureStorageHandler getInstance() {
        if (instance == null) {
            Logger.log("SecureStorageHandler", "Creating new instance...");
            instance = new SecureStorageHandler();
        }
        return instance;
    }

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

    public static boolean storeCredential(String credKey, StoredToken credential) {
        Logger.log("SecureStorageHandler", "Storing credential with key: " + credKey);
        return getInstance().credentialStorage.add(credKey, credential);
    }
}
