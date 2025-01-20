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

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main( String[] args )
    {
        display = new Display();
        Shell shell = AppShell.getShell(display);

        // CODE TO EXECUTE ON START

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

    private static void securestorage_test() {

        SecretStore<StoredCredential> credentialStorage = StorageProvider.getCredentialStorage(true, StorageProvider.SecureOption.REQUIRED);

        //final String CREDENTIALS_KEY = "TestCredentials";
        final String CREDENTIALS_KEY = "TestJavaDemo";

        /*final StoredCredential credential = new StoredCredential("userName", "password123$$".toCharArray());

        try {
            // Save the credential to the store.
            credentialStorage.add(CREDENTIALS_KEY, credential);

        } finally {
            // clear password value.
            credential.clear();
        }*/

        // Retrieve the credential from the store.

        StoredCredential storedCredential = credentialStorage.get(CREDENTIALS_KEY);

        System.out.println("Stored credential: " + storedCredential.getUsername() + " " + Arrays.toString(storedCredential.getPassword()));

    }

    //private static void tpm_test() {
    //    // The secret data we want to seal.
    //    String secretString = "Hello TPM! This is my secret string.";
    //    byte[] secretData = secretString.getBytes(StandardCharsets.UTF_8);
//
    //    // 1. Connect to the TPM
    //    // For a simulator:
    //    // Tpm tpm = TpmFactory.localTpmSimulator();
    //    // For a physical TPM on Windows:
    //    Tpm tpm = TpmFactory.platformTpm();
    //    // Adjust according to your environment.
    //    //Tpm tpm = TpmFactory.localTpmSimulator();
//
    //    // TPM may require Startup if it's a simulator just started.
    //    tpm._allowErrors().Startup(TPM_SU.CLEAR); // Ignore error if it's already started.
//
//
    //    // Output TPM details
    //    //byte[] tpmName = tpm.ReadPublic(TPM_HANDLE.from(TPM_RH.OWNER)).name;
    //    //System.out.println("TPM Name: " + new String(tpmName, StandardCharsets.UTF_8));
    //    System.out.println("TPM Manufacturer: " + tpm.GetCapability(TPM_CAP.TPM_PROPERTIES, TPM_PT.MANUFACTURER.toInt(), 1).toString());
    //    System.out.println("TPM Version: " + tpm.GetCapability(TPM_CAP.TPM_PROPERTIES, TPM_PT.FIRMWARE_VERSION_1.toInt(), 1).toString());
//
    //    // 2. Create a primary key under the Owner hierarchy
    //    //    This will be a restricted decryption key to serve as a parent for our sealed object.
    //    TPMT_PUBLIC primaryTemplate = new TPMT_PUBLIC(
    //            TPM_ALG_ID.SHA1, // Change to SHA-1
    //            new TPMA_OBJECT(TPMA_OBJECT.fixedTPM, TPMA_OBJECT.fixedParent, TPMA_OBJECT.sensitiveDataOrigin, TPMA_OBJECT.userWithAuth, TPMA_OBJECT.restricted, TPMA_OBJECT.decrypt),
    //            null, // No auth policy
    //            new TPMS_RSA_PARMS(
    //                    new TPMT_SYM_DEF_OBJECT(TPM_ALG_ID.AES, 128, TPM_ALG_ID.CFB),
    //                    new TPMS_NULL_ASYM_SCHEME(),
    //                    2048,
    //                    65537
    //            ),
    //            new TPM2B_PUBLIC_KEY_RSA(new byte[0])
    //    );
//
    //    /*
    //    PMT_PUBLIC primaryTemplate = new TPMT_PUBLIC(
    //            TPM_ALG_ID.SHA256,
    //            new TPMA_OBJECT(TPMA_OBJECT.fixedTPM, TPMA_OBJECT.fixedParent, TPMA_OBJECT.restricted,
    //                    TPMA_OBJECT.decrypt, TPMA_OBJECT.userWithAuth, TPMA_OBJECT.sensitiveDataOrigin),
    //            null, // No auth policy
    //            new TPMS_RSA_PARMS(
    //                    new TPMT_SYM_DEF_OBJECT(TPM_ALG_ID.AES, 128, TPM_ALG_ID.CFB),
    //                    new TPMS_NULL_ASYM_SCHEME(),
    //                    2048, 65537
    //            ),
    //            new TPM2B_PUBLIC_KEY_RSA(new byte[0])
    //    );*/
//
    //    TPMS_PCR_SELECTION[] pcrSelectionsPrim = { new TPMS_PCR_SELECTION() };
//
    //    CreatePrimaryResponse primaryResp;
    //    try {
    //        primaryResp = tpm.CreatePrimary(
    //                TPM_HANDLE.from(TPM_RH.OWNER),
    //                new TPMS_SENSITIVE_CREATE(), // no special auth
    //                primaryTemplate, // Use the primaryTemplate defined earlier
    //                new byte[0],
    //                pcrSelectionsPrim
    //        );
    //    } catch (TpmException e) {
    //        System.err.println("Failed to create primary key: " + e.getMessage());
    //        try {
    //            tpm.close();
    //        } catch (IOException e1) {
    //            // Ignore if the TPM is already shut down.
    //        }
    //        return;
    //    }
    //    TPM_HANDLE primaryHandle = primaryResp.handle;
//
    //    // 3. Create a sealed object to store the secret
    //    // KeyedHash object with no signing/decryption, just data storage.
    //    TPMT_PUBLIC sealTemplate = new TPMT_PUBLIC(
    //            TPM_ALG_ID.SHA256,
    //            new TPMA_OBJECT(TPMA_OBJECT.fixedTPM, TPMA_OBJECT.fixedParent, TPMA_OBJECT.userWithAuth,
    //                    TPMA_OBJECT.noDA, TPMA_OBJECT.sensitiveDataOrigin),
    //            null, // No auth policy
    //            new TPMS_KEYEDHASH_PARMS(
    //                    new TPMS_KEY_SCHEME_ECDH(TPM_ALG_ID.NULL)
    //            ),
    //            new TPM2B_DIGEST(new byte[0])
    //    );
//
    //    // Put the secret data into the sensitive area
    //    TPMS_SENSITIVE_CREATE inSensitive =
    //            new TPMS_SENSITIVE_CREATE(
    //                    new byte[0],           // No extra auth
    //                    secretData
    //            )
    //            ;
//
    //    TPMS_PCR_SELECTION[] pcrSelectionsRsp = { new TPMS_PCR_SELECTION() };
//
    //    CreateResponse createResp = tpm.Create(
    //            primaryHandle,
    //            inSensitive,
    //            new TPMT_PUBLIC(),
    //            new byte[0],
    //            pcrSelectionsRsp
    //    );
//
    //    TPM2B_PRIVATE sealedPrivate = createResp.outPrivate;
    //    TPMT_PUBLIC sealedPublic = createResp.outPublic;
//
    //    // Now sealedPrivate and sealedPublic are your sealed object blobs.
    //    // You can store these offline and later load them back for unsealing.
//
    //    // 4. "Decode" (Unseal) operation
    //    // Load the sealed object again:
    //    TPM_HANDLE /*loadResp*/ sealedHandle = tpm.Load(primaryHandle, sealedPrivate, sealedPublic);
//
    //    // Unseal the data from the TPM
    //    TPM2B_SENSITIVE_DATA unsealedData = TPM2B_SENSITIVE_DATA.fromBytes(tpm.Unseal(sealedHandle));
    //    String retrievedSecret = new String(unsealedData.buffer, StandardCharsets.UTF_8);
    //    System.out.println("Retrieved secret: " + retrievedSecret);
//
    //    // Cleanup
    //    tpm.FlushContext(sealedHandle);
    //    tpm.FlushContext(primaryHandle);
    //    try {
    //        tpm.close();
    //    } catch (IOException e) {
    //        // Ignore if the TPM is already shut down.
    //    }
    //}

    public static Display getDisplay() {
        return display;
    }

}