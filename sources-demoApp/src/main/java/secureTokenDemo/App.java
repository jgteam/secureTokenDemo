/**
 * File: App.java
 * Author: Jannis Günsche
 * Description: This file is the starting point of a proof-of-concept demo application
 *              for secure token storage. This application is part of
 *              a Bachelor's thesis project.
 */

package secureTokenDemo;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * The type App.
 */
public class App
{

    /**
     * The constant CRED_KEY_PREFIX.
     */
    public static final String CRED_KEY_PREFIX = "TOKENDEMO_";

    /**
     * The constant CREDENTIAL_STORAGE_FILE.
     */
    public static final String CREDENTIAL_STORAGE_FILE = "credentials.txt";
    /**
     * The constant CREDENTIAL_JSON_KEY.
     */
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

    /**
     * Gets display.
     *
     * @return the display
     */
    public static Display getDisplay() {
        return display;
    }

    /**
     * Gets shell.
     *
     * @return the shell
     */
    public static Shell getShell() {
        return shell;
    }

}