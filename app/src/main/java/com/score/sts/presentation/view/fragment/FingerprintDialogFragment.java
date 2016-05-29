package com.score.sts.presentation.view.fragment;


import android.Manifest;
import android.app.DialogFragment;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.score.sts.R;
import com.score.sts.presentation.FingerprintHandler;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Enumeration;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * A simple {@link Fragment} subclass.
 */
/**
 * NOTE: All contexts needed are reached from application context vs activity context.
 * Activity context was only used for system services access.
 * This code was taken from the sample activity where all context was the current activity context - this
 * The purpose behind this type of context access is to reduce and possibly eliminate memory leaks
 */
public class FingerprintDialogFragment extends DialogFragment {

    private static final String TAG = FingerprintDialogFragment.class.getSimpleName();
    private KeyguardManager keyguardManager;
    private FingerprintManager fingerprintManager;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private Cipher cipher;
    private FingerprintManager.CryptoObject cryptoObject;
    private CancellationSignal cancellationSignal;

    private static final String ANDROID_KEYSTORE = "AndroidKeyStore";
    private static final String KEY_NAME = "STS";

    public FingerprintDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fingerprint_dialog_fragment, container, false);
        // TODO uncomment this if you don't want to have the title area. be aware that you will have
        // to adjust the sytles for the buttons because the text on the cancel button goes to the next line
        /*if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }*/

        // TODO create a listener for the fingerprint icon
        View.OnClickListener fingerprintListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // get access to the FingerprintManager and KeyguardManager
                    keyguardManager = (KeyguardManager) getActivity().getSystemService(Context.KEYGUARD_SERVICE);
                    fingerprintManager = (FingerprintManager) getActivity().getSystemService(Context.FINGERPRINT_SERVICE);
                }else{
                    // TODO inform the user that their device is not equipped with fingerprint technology
                    // TODO and tell them to use their password and/or disable fingerprint image
                    Toast.makeText(getActivity().getApplicationContext(), "Your device is not equipped with fingerpring technology", Toast.LENGTH_LONG).show();
                    return;
                }

                // check to see if lockscreen is secured by pin, password or pattern
                if(!keyguardManager.isKeyguardSecure()){
                    Toast.makeText(getActivity().getApplicationContext(), "Lock screen security is not enabled", Toast.LENGTH_LONG).show();
                    return;
                }

                // check to see if Fingerprint permission is granted
                if(ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getActivity().getApplicationContext(), "Fingerprint permission is not granted", Toast.LENGTH_LONG).show();
                    return;
                }

                // check to see if there is a registered fingerprint on the device
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!fingerprintManager.hasEnrolledFingerprints()) {
                        Log.d(TAG, "Fingerprint Enrolled? " + fingerprintManager.hasEnrolledFingerprints());
                        Toast.makeText(getActivity().getApplicationContext(), "No registered fingerprints on your device", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                generateKey();
                if(cipherInit()){
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        cryptoObject = new FingerprintManager.CryptoObject(cipher);
                        FingerprintHandler fingerprintHandler = new FingerprintHandler(getActivity().getApplicationContext());
                        fingerprintHandler.startAuth(fingerprintManager, cryptoObject);
                        // for versions < 23  and not in the original example
                        startAuth(fingerprintManager, cryptoObject, getAuthenticationCallback(getActivity().getApplicationContext()));
                    }
                }
            }
        };

        ImageView imageFingerprint = (ImageView) view.findViewById(R.id.image_fingerprint);
        imageFingerprint.setOnClickListener(fingerprintListener);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // method to access the android Keystore
    protected void generateKey(){
        try {
            keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        }

        // generate a key that will be used in generating a cipher
        try {
            keyStore.load(null);
            if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // version check
                keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setUserAuthenticationRequired(true)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .build()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | CertificateException e) {
            Log.e(TAG, e.getMessage());
//            throw new RuntimeException(e);
        }
    } // end method generateKey

    // TODO create a method to initialize a cipher to be used with the CryptoObject
    protected boolean cipherInit(){
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" +
                    KeyProperties.BLOCK_MODE_CBC + "/" +
                    KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            //---debugging---//
            Enumeration<String> storedKeys = keyStore.aliases();
            while(storedKeys.hasMoreElements()){
                Log.d(TAG, "Stored key: " + storedKeys.nextElement());
            }

            SecretKey secretKey = (SecretKey) keyStore.getKey(KEY_NAME, null);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return true;
        }catch (InvalidKeyException e){
            Log.e(TAG, e.getMessage());
            return false;
        } catch (IOException | KeyStoreException | UnrecoverableKeyException | NoSuchAlgorithmException | CertificateException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }

    } // end method cipherInit


    //---TODO this method is an attempt to substitute the FingerprintHandler class. version 23 is required for these callback methods.
    protected static FingerprintManager.AuthenticationCallback getAuthenticationCallback(final Context context){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FingerprintManager.AuthenticationCallback callback = new FingerprintManager.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    Toast.makeText(context, "Authentication error\n" + errString, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                    super.onAuthenticationHelp(helpCode, helpString);
                    Toast.makeText(context, "Authentication help\n" + helpString, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Toast.makeText(context, "Authentication failed", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    Toast.makeText(context, "Authentication succeeded", Toast.LENGTH_LONG).show();
                }
            };
            return callback;
        } // end if
        return null;
    }  // end method

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject, FingerprintManager.AuthenticationCallback callback){
        cancellationSignal = new CancellationSignal();
        // updated the context from this(in the sample activity) to getApplicationContext
        if(ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED){
            return;
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.authenticate(cryptoObject, cancellationSignal, 0, callback, null);
        }
    }
}
