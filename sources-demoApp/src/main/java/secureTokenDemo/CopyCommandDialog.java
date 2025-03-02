/**
 * File: CopyCommandDialog.java
 * Author: Jannis Günsche
 * Description: This class contains the dialog for copying a command.
 */
package secureTokenDemo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

/**
 * The class CopyCommandDialog contains the dialog for copying a command.
 */
public class CopyCommandDialog {

    private CopyCommandDialog() {
        // Prevent instantiation
    }

    /**
     * Open the dialog.
     *
     * @param cmd the command to copy
     */
    public static void open(String cmd) {

        Display display = App.getDisplay();
        Shell shell = new Shell(display, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE);
        shell.setText("Copy Command Dialog");
        shell.setSize(400, 300);
        shell.setLayout(new GridLayout(1, false));

        Group group = new Group(shell, SWT.NONE);
        group.setText("Command to copy");
        group.setLayout(new GridLayout(1, false));
        group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        Text command = new Text(group, SWT.WRAP | SWT.READ_ONLY);
        command.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        command.setText(cmd);

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }

        }

    }

}
