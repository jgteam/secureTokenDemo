/**
 * File: App.java
 * Author: Jannis Günsche
 * Description: This file is the starting point of a proof-of-concept demo application
 *              for secure token storage. This application is part of
 *              a Bachelor's thesis project.
 */

package secureTokenDemo;

import com.microsoft.credentialstorage.SecretStore;
import com.microsoft.credentialstorage.StorageProvider;
import com.microsoft.credentialstorage.model.StoredCredential;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.util.Arrays;


/**
 * The type App.
 */
public class App
{

    // Config
    public static final String USERNAME = "DEMOGUI_USER_SecureStorageDemo";
    public static final String CRED_KEY_PREFIX = "TOKENDEMO_";

    public static final String CREDENTIAL_STORAGE_FILE = "credentials.txt";
    public static final String CREDENTIAL_JSON_KEY = "credKeys";

    private static Display display;
    private static Shell shell;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main( String[] args )
    {
        display = new Display();
        shell = AppShell.getShell(display);

        shell.open();

        // Will initialize the CredentialTracker singleton to load credentials from file
        CredentialTracker.getInstance().loadCredentials();


        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        display.dispose();
    }

    public static Display getDisplay() {
        return display;
    }

    public static Shell getShell() {
        return shell;
    }

}