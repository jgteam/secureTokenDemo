package secureTokenDemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.credentialstorage.model.StoredCredential;
import logger.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AppLogic {

    private AppLogic() {
        // Prevent instantiation
    }

    public static void openTokenDialog(String credKey) {
        StoredCredential storedCredential = null;
        try {
            storedCredential = SecureStorageHandler.getStoredCredential(credKey);
        } catch (Exception e) {
            Logger.log("AppLogic", "Error retrieving token: " + e.getMessage());
            return;
        }

        if(storedCredential == null) {
            Logger.log("AppLogic", "No token found for credential key '" + credKey + "'.");
            showMessage("No credentials for key '" + credKey + "' retrieved.");
        } else {
            TokenViewerDialog dialog = new TokenViewerDialog(credKey, storedCredential.getUsername(), storedCredential.getPassword());
            dialog.open();
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

            StoredCredential storedCredential = new StoredCredential(App.USERNAME, token.toCharArray());
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

            //try {
                String command = "java -jar CredStoreCLI.jar " + credKey + " " + token;
                Logger.log("AppLogic", "Executing command: " + command);
                /*Process p = Runtime.getRuntime().exec(command);
                logCredStoreCLIOutput(p);
                p.waitFor();
                if(p.exitValue() == 0) {
                    Logger.log("AppLogic", "Credential stored successfully for key '" + credKey + "'.");
                    showMessage("Credential stored successfully for key '" + credKey + "'.");

                    CredentialTracker.getInstance().addCredential(credKey);

                    syncComboBoxWithCredentialTracker();
                    AppShell.textNewName.setText("");
                    AppShell.textNewToken.setText("");
                } else {
                    Logger.log("AppLogic", "Failed to store credential for key '" + credKey + "'.");
                    showMessage("Failed to store credential for key '" + credKey + "'.");
                }*/
            //} catch (IOException | InterruptedException e) {
            //    Logger.log("AppLogic", "Error storing credential: " + e.getMessage());
            //    showMessage("Error storing credential: " + e.getMessage());
            //}
        }

    }

    private static void logCredStoreCLIOutput(Process process) throws IOException {
        try (BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
             BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

            String s;
            while ((s = stdInput.readLine()) != null) {
                Logger.log("Process: CredStoreCLI.jar", "stdOut: " + s);
            }
            while ((s = stdError.readLine()) != null) {
                Logger.log("Process: CredStoreCLI.jar", "stdErr: " + s);
            }
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
