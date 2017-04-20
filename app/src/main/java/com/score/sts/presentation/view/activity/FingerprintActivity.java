package com.score.sts.presentation.view.activity;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

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

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class FingerprintActivity extends AppCompatActivity {

    private KeyguardManager keyguardManager;
    private FingerprintManager fingerprintManager;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private Cipher cipher;
    private FingerprintManager.CryptoObject cryptoObject;
    private CancellationSignal cancellationSignal;

    private static final String ANDROID_KEYSTORE = "AndroidKeyStore";
    private static final String KEY_NAME = "STS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get access to the FingerprintManager and KeyguardManager
        keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

        // check to see if lockscreen is secured by pin, password or pattern
        if(!keyguardManager.isKeyguardSecure()){
            Toast.makeText(this, "Lock screen security is not enabled", Toast.LENGTH_LONG).show();
            return;
        }

        // check to see if Fingerprint permission is granted
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Fingerprint permission is not granted", Toast.LENGTH_LONG).show();
            return;
        }

        // check to see if there is a registered fingerprint on the device
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!fingerprintManager.hasEnrolledFingerprints()) {
                Toast.makeText(this, "No registered fingerprints on your device", Toast.LENGTH_LONG).show();
                return;
            }
        }

        generateKey();

        if(cipherInit()){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cryptoObject = new FingerprintManager.CryptoObject(cipher);
                FingerprintHandler fingerprintHandler = new FingerprintHandler(this);
                fingerprintHandler.startAuth(fingerprintManager, cryptoObject);
                // for versions < 23  and not in the original example
                startAuth(fingerprintManager, cryptoObject, getAuthenticationCallback(this));
            }
        }
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
            throw new RuntimeException(e);
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
            SecretKey secretKey = (SecretKey) keyStore.getKey(KEY_NAME, null);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return true;
        }catch (InvalidKeyException e){
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

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED){
            return;
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.authenticate(cryptoObject, cancellationSignal, 0, callback, null);
        }
    }
} // end activity FingerprintActivity
