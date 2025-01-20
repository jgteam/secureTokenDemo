package secureTokenDemo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import java.util.Arrays;
import java.util.Base64;

public class TokenViewerDialog {

    private String credKey;
    private String username;
    private char[] token;

    public TokenViewerDialog(String credKey, String username, char[] token) {
        this.credKey = credKey;
        this.username = username;
        this.token = token;
    }

    public void open() {
        Display display = App.getDisplay();
        Shell shell = new Shell(display);
        shell.setText("Token Viewer");
        shell.setSize(400, 500);
        shell.setLayout(new GridLayout(1, false));

        Group group = new Group(shell, SWT.NONE);
        group.setText("Token Information");
        group.setLayout(new GridLayout(2, false));
        group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));


        Label credKeyLabelHeading = new Label(group, SWT.NONE);
        credKeyLabelHeading.setLayoutData(new GridData(SWT.BEGINNING, SWT.TOP, false, false));
        credKeyLabelHeading.setText("Credential Key:");

        Label credKeyLabel = new Label(group, SWT.WRAP);
        credKeyLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        credKeyLabel.setText(credKey);


        Label usernameLabelHeading = new Label(group, SWT.NONE);
        usernameLabelHeading.setLayoutData(new GridData(SWT.BEGINNING, SWT.TOP, false, false));
        usernameLabelHeading.setText("Username:");

        Label usernameLabel = new Label(group, SWT.WRAP);
        usernameLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        usernameLabel.setText(username);

        Label tokenLabelHeading = new Label(group, SWT.NONE);
        tokenLabelHeading.setLayoutData(new GridData(SWT.BEGINNING, SWT.TOP, false, false));
        tokenLabelHeading.setText("Token:");

        Label tokenLabel = new Label(group, SWT.WRAP);
        tokenLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        tokenLabel.setText(new String(token));

        Label tokenDecodedLabelHeading = new Label(group, SWT.NONE);
        tokenDecodedLabelHeading.setLayoutData(new GridData(SWT.BEGINNING, SWT.TOP, false, false));
        tokenDecodedLabelHeading.setText("Token (Decoded):");

        Label tokenDecodedLabel = new Label(group, SWT.WRAP);
        tokenDecodedLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        tokenDecodedLabel.setText(AppLogic.decodeAndPrettifyToken(token));

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        // CLEAR TOKEN
        for (int i = 0; i < token.length; i++) {
            token[i] = 0;
        }
    }
}