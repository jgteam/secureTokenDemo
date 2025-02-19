/**
 * File: AppShell.java
 * Author: Jannis Günsche
 * Description: This class contains the main UI window of the application.
 */
package secureTokenDemo;

import logger.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class AppShell {

    private static Shell shell = null;

    // UI elements
    public static Combo combo;
    public static Text textExisting;
    public static Text textNewName;
    public static Text textNewToken;
    public static Combo comboCreateType;

    public static Button buttonReadTokenMeasure;

    private AppShell() {
        // Prevent instantiation
    }

    public static Shell getShell(Display display) {

        if (shell != null) {
            return shell;
        }

        shell = new Shell(display);
        shell.setText("Secure Token Storage Proof-of-Concept App");
        shell.setSize(550, 700);

        GridLayout layout = new GridLayout(1, false);
        shell.setLayout(layout);


        // Layouts
        //GridLayout groupLayout = new GridLayout(2, false);
        //GridData groupGridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        //GridData lastGroupGridData = new GridData(SWT.FILL, SWT.FILL, true, true);

        GridData buttonsGridData = new GridData(SWT.END, SWT.CENTER, true, true);
        buttonsGridData.widthHint = 220;
        GridData buttonsGridDataMeasure = new GridData(SWT.END, SWT.CENTER, true, true, 2, 1);
        buttonsGridDataMeasure.widthHint = 220;
        GridData lastButtonsGridData = new GridData(SWT.END, SWT.TOP, true, false);
        lastButtonsGridData.widthHint = 220;


        // READ TOKEN GROUP
        Group groupRead = new Group(shell, SWT.NONE);
        groupRead.setLayout(new GridLayout(2, false));
        groupRead.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        groupRead.setText("Reading out token from secure storage with credential-key");

        combo = new Combo(groupRead, SWT.READ_ONLY);
        GridData comboGridData = new GridData(SWT.BEGINNING, SWT.CENTER, false, false, 1, 1);
        comboGridData.widthHint = 280;
        combo.setLayoutData(comboGridData);

        Button buttonReadToken = new Button(groupRead, SWT.PUSH);
        buttonReadToken.setText("Request token");
        buttonReadToken.setLayoutData(buttonsGridData);
        buttonReadToken.setEnabled(false);

        buttonReadTokenMeasure = new Button(groupRead, SWT.PUSH);
        buttonReadTokenMeasure.setText("Request token (measure time)");
        buttonReadTokenMeasure.setLayoutData(buttonsGridDataMeasure);
        buttonReadTokenMeasure.setEnabled(false);


        // WRITE EXISTING CRED GROUP
        Group groupAddExisting = new Group(shell, SWT.NONE);
        groupAddExisting.setLayout(new GridLayout(2, false));
        groupAddExisting.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        groupAddExisting.setText("Adding existing credential-key for existing token");

        textExisting = new Text(groupAddExisting, SWT.BORDER);
        GridData textExistingGridData = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
        textExistingGridData.widthHint = 280;
        textExisting.setLayoutData(textExistingGridData);
        textExisting.setText("<Enter existing credential-key>");

        Button buttonAddExisting = new Button(groupAddExisting, SWT.PUSH);
        buttonAddExisting.setText("Add new credential-key");
        buttonAddExisting.setLayoutData(buttonsGridData);


        // WRITE NEW TOKEN CRED GROUP
        Group groupAddNew = new Group(shell, SWT.NONE);
        groupAddNew.setLayout(new GridLayout(2, false));
        groupAddNew.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        groupAddNew.setText("Adding new credential-key with new token");

        textNewName = new Text(groupAddNew, SWT.BORDER);
        GridData textNewNameGridData = new GridData(SWT.BEGINNING, SWT.TOP, false, false, 2, 1);
        textNewNameGridData.widthHint = 280;
        textNewName.setLayoutData(textNewNameGridData);
        textNewName.setText("<Enter new credential-key>");

        textNewToken = new Text(groupAddNew, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        GridData textNewTokenGridData = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
        textNewToken.setLayoutData(textNewTokenGridData);
        textNewToken.setText("<Enter new token>");

        comboCreateType = new Combo(groupAddNew, SWT.READ_ONLY);
        GridData comboCreateGridData = new GridData(SWT.BEGINNING, SWT.CENTER, false, false, 1, 1);
        comboCreateGridData.widthHint = 280;
        comboCreateType.add("As this Application");
        comboCreateType.add("As 'another' Application");
        comboCreateType.select(0);
        comboCreateType.setLayoutData(comboCreateGridData);

        Button buttonAddNew = new Button(groupAddNew, SWT.PUSH);
        buttonAddNew.setText("Add new credential-pair");
        buttonAddNew.setLayoutData(lastButtonsGridData);



        // MEASURE REPORT BUTTON
        Button buttonMeasureReport = new Button(shell, SWT.PUSH);
        buttonMeasureReport.setLayoutData(new GridData(SWT.END, SWT.TOP, true, false, 2, 1));
        buttonMeasureReport.setText("View Time Measurements");

        // LOG BUTTON
        Button buttonOpenLog = new Button(shell, SWT.PUSH);
        buttonOpenLog.setLayoutData(new GridData(SWT.END, SWT.TOP, true, false, 2, 1));
        buttonOpenLog.setText("Open Log Viewer");

        // INFO LABEL
        Label infoLabel = new Label(shell, SWT.WRAP);
        infoLabel.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
        infoLabel.setText("\n\nSecure Storage Demo Application - This application is part of a Bachelor's thesis project by Jannis Günsche. \n\nDisclaimer: This application demonstrates the use of the Secure Token Storage API. Only use this application for demonstration purposes.");




        // LISTENERS

        buttonReadToken.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                AppLogic.retrieveToken(combo.getText());
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // Do nothing
            }
        });

        buttonReadTokenMeasure.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                AppLogic.retrieveToken(combo.getText(), true, 100);
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // Do nothing
            }
        });

        combo.addModifyListener(e -> {
            buttonReadToken.setEnabled(!combo.getText().isEmpty());
            buttonReadTokenMeasure.setEnabled(!combo.getText().isEmpty());
        });

        buttonAddExisting.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                AppLogic.addCredKeyToDemoApp(textExisting.getText());
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // Do nothing
            }
        });

        textExisting.addModifyListener(e -> {
            buttonAddExisting.setEnabled(!textExisting.getText().isEmpty());
        });

        buttonAddNew.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                AppLogic.storeCreds(textNewName.getText(), textNewToken.getText());
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // Do nothing
            }
        });

        textNewName.addModifyListener(e -> {
            buttonAddNew.setEnabled(!textNewName.getText().isEmpty() && !textNewToken.getText().isEmpty());
        });

        textNewToken.addModifyListener(e -> {
            buttonAddNew.setEnabled(!textNewName.getText().isEmpty() && !textNewToken.getText().isEmpty());
        });

        buttonOpenLog.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                LogDialog.open();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // Do nothing
            }
        });

        buttonMeasureReport.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
                messageBox.setMessage(Logger.getTimeReport());
                messageBox.setText("Time Measurements");
                messageBox.open();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // Do nothing
            }
        });


        return shell;
    }

}
