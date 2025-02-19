package secureTokenDemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.credentialstorage.model.StoredToken;
import com.microsoft.credentialstorage.model.StoredTokenType;
import logger.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class AppLogic {

    private AppLogic() {
        // Prevent instantiation
    }

    public static boolean retrieveToken(String credKey) {
        return retrieveToken(credKey, false, 1);
    }

    public static boolean retrieveToken(String credKey, boolean measureTime, int count) {
        if(count < 1) {
            Logger.log("AppLogic", "Invalid count.");
            return false;
        } else if(count == 1) {
            return retrieveToken(credKey, measureTime, true);
        } else {
            boolean lastResult = false;
            for(int i = 0; i < count; i++) {
                lastResult = retrieveToken(credKey, measureTime, false);
                Logger.log("AppLogic", "Retrieval " + (i + 1) + " of " + count + ": " + (lastResult ? "Success" : "Failed"));

                AppShell.buttonReadTokenMeasure.setText("Progress: " + (i + 1) + " of " + count);
                AppShell.buttonReadTokenMeasure.getParent().layout();
            }
            MessageBox messageBox = new MessageBox(App.getShell(), SWT.ICON_INFORMATION | SWT.OK);
            messageBox.setText("Measuring finished");
            messageBox.setMessage("The measurement is finished. Please check the log or report for details.");
            messageBox.open();
            AppShell.buttonReadTokenMeasure.setText("Request token (measure time)");
            return true;
        }
    }

    public static boolean retrieveToken(String credKey, boolean measureTime, boolean openDialog) {
        StoredToken storedCredential = null;
        Instant start = null;
        Instant end = null;

        try {
            if(measureTime) {
                start = Instant.now();
            }
            storedCredential = SecureStorageHandler.getStoredCredential(credKey);
            if(measureTime) {
                end = Instant.now();
            }
        } catch (Exception e) {
            Logger.log("AppLogic", "Error retrieving token: " + e.getMessage());
            return false;
        }

        if(measureTime && start != null && end != null) {
            long timeElapsed = Duration.between(start, end).toMillis();
            Logger.log("AppShell", "Decryption took " + timeElapsed + " ms.");
            Logger.logTime(timeElapsed);
            if(openDialog) {
                MessageBox messageBox = new MessageBox(App.getShell(), SWT.ICON_INFORMATION | SWT.OK);
                messageBox.setText("Decryption Time");
                messageBox.setMessage("Decryption took " + timeElapsed + " ms.");
                messageBox.open();
            }
        }

        if(storedCredential == null) {
            Logger.log("AppLogic", "No token found for credential key '" + credKey + "'.");
            showMessage("No credentials for key '" + credKey + "' retrieved.");
            return false;
        } else {
            if(openDialog) {
                TokenViewerDialog dialog = new TokenViewerDialog(credKey, storedCredential.getValue());
                dialog.open();
            }
            return true;
        }

    }

    public static void addCredKeyToDemoApp(String credKey) {
        CredentialTracker.getInstance().addCredential(credKey);

        syncComboBoxWithCredentialTracker();
        AppShell.textExisting.setText("");

        showMessage("Credential key '" + credKey + "' added successfully to the Demo App.");
    }


    // NOTICE: The passing of the token as a String is a security vulnerability!
    public static void storeCreds(String credKey, String token) {
        credKey = App.CRED_KEY_PREFIX + credKey;

        if(AppShell.comboCreateType.getSelectionIndex() == 0) {
            // Save as 'this'-Application

            StoredToken storedCredential = new StoredToken(token.toCharArray(), StoredTokenType.ACCESS);
            boolean success = SecureStorageHandler.storeCredential(credKey, storedCredential);

            if(success) {
                Logger.log("AppLogic", "Credential stored successfully for key '" + credKey + "'.");
                showMessage("Credential stored successfully for key '" + credKey + "'.");

                CredentialTracker.getInstance().addCredential(credKey);

                syncComboBoxWithCredentialTracker();
                AppShell.textNewName.setText("");
                AppShell.textNewToken.setText("");
            } else {
                Logger.log("AppLogic", "Failed to store credential for key '" + credKey + "'.");
                showMessage("Failed to store credential for key '" + credKey + "'.");
            }
        } else {
            // Save as 'another'-Application

            String command = "java -jar CredStoreCLI.jar " + credKey + " " + token;
            Logger.log("AppLogic", "Generated CLI command: " + command);
            CopyCommandDialog.open(command);

            CredentialTracker.getInstance().addCredential(credKey);

            syncComboBoxWithCredentialTracker();
            AppShell.textNewName.setText("");
            AppShell.textNewToken.setText("");
        }

    }

    private static void showMessage(String message) {
        Shell shell = AppShell.getShell(App.getDisplay());
        MessageBox messageBox = new MessageBox(shell, SWT.OK);
        messageBox.setMessage(message);
        messageBox.open();
    }

    public static void syncComboBoxWithCredentialTracker() {
        String selected = AppShell.combo.getText();
        AppShell.combo.removeAll();
        CredentialTracker.getInstance().getCredentials().forEach(AppShell.combo::add);
        AppShell.combo.setText(selected);
    }

    public static String decodeAndPrettifyToken(char[] token) {
        // Expecting a valid 3-Part JWT token

        try {
            // Split into 3 parts
            String tokenString = new String(token);
            String[] parts = tokenString.split("\\.");

            // decode base 64
            String[] decodedToken = new String[parts.length];
            for (int i = 0; i < 2; i++) {
                decodedToken[i] = new String(java.util.Base64.getDecoder().decode(parts[i]));
            }

            String decodedTokenPretty = "";
            // use jackson json parser to prettify
            for (int i = 0; i < 3; i++) {
                if(i == 2) {
                    // Signature-Case
                    decodedTokenPretty += "Signature: " + parts[i];
                    continue;
                }

                try {
                    ObjectMapper mapper = new ObjectMapper();
                    Object json = mapper.readValue(decodedToken[i], Object.class);
                    decodedTokenPretty += mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json) + "\n\n";
                } catch (IOException e) {
                    Logger.log("AppLogic", "Error decoding token: " + e.getMessage());
                }
            }


            return decodedTokenPretty;
        } catch (Exception e) {
            Logger.log("AppLogic", "Error decoding token: " + e.getMessage());
            return "[ Error decoding token ]";
        }
    }
}
