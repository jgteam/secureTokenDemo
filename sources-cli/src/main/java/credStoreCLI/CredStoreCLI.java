package credStoreCLI;

import com.microsoft.credentialstorage.SecretStore;
import com.microsoft.credentialstorage.StorageProvider;
import com.microsoft.credentialstorage.model.StoredToken;
import com.microsoft.credentialstorage.model.StoredTokenType;

import java.util.List;

import static java.lang.System.exit;

public class CredStoreCLI {

    // Config
    private static final String USERNAME = "CLI_USER_SecureStorageDemo";

    public static void main(String[] args) {
        System.out.println("Secure Token Storage CLI");
        System.out.println("Disclaimer: This application demonstrates the use of the Secure Token Storage API. Only use this application for demonstration purposes.");


        // Check if the number of arguments is correct
        if (args.length != 2) {
            System.out.println("Usage: java -jar credStoreCLI.jar <credential-key> <token>");
            exit(1);
        }

        // Check if the credential-key is valid
        if (args[0].isEmpty()) {
            System.out.println("Invalid credential-key");
            exit(1);
        }

        // Check if the token is valid
        if (args[1].isEmpty()) {
            System.out.println("Invalid token");
            exit(1);
        }

        final String credKey = args[0];
        final char[] token = args[1].toCharArray();

        SecretStore<StoredToken> credentialStorage = StorageProvider.getTokenStorage(true, StorageProvider.SecureOption.REQUIRED);

        final StoredToken credential = new StoredToken(token, StoredTokenType.ACCESS);

        try {
            credentialStorage.add(credKey, credential);
        } finally {
            credential.clear();
        }

        System.out.println("Credential stored successfully! \n\n Key:  " + credKey + " \n User: " + USERNAME);
        exit(0);
    }
}
