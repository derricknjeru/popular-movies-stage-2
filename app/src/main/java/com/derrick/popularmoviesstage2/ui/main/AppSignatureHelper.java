package com.derrick.popularmoviesstage2.ui.main;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

public class AppSignatureHelper extends ContextWrapper {
    public static final String TAG = AppSignatureHelper.class.getSimpleName();

    private static final String HASH_TYPE = "SHA-256";
    public static final int NUM_HASHED_BYTES = 9;
    public static final int NUM_BASE64_CHAR = 11;
    private String packageName = "com.dtb.app";
    private String signature = "6b6579746f6f6c206572726f723a206a6176612e6c616e672e457863657074696f6e3a204b657973746f72652066696c6520646f6573206e6f742065786973743a204454422e6a6b730a";

    public AppSignatureHelper(Context context) {
        super(context);
    }

    /**
     * Get all the app signatures for the current package
     *
     * @return
     */
    public ArrayList<String> getAppSignatures() {
        ArrayList<String> appCodes = new ArrayList<>();

        // Get all package signatures for the current package
        // String packageName = getPackageName();
        //PackageManager packageManager = getPackageManager();
        // Signature[] signatures = packageManager.getPackageInfo(packageName,
        //  PackageManager.GET_SIGNATURES).signatures;

        // For each signature create a compatible hash
        //for (Signature signature : signatures) {
        String hash = hash(packageName, signature);
        if (hash != null) {
            appCodes.add(String.format("%s", hash));
        }
        //}

        return appCodes;
    }

    private static String hash(String packageName, String signature) {
        String appInfo = packageName + " " + signature;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(HASH_TYPE);
            messageDigest.update(appInfo.getBytes(StandardCharsets.UTF_8));
            byte[] hashSignature = messageDigest.digest();

            // truncated into NUM_HASHED_BYTES
            hashSignature = Arrays.copyOfRange(hashSignature, 0, NUM_HASHED_BYTES);
            // encode into Base64
            String base64Hash = Base64.encodeToString(hashSignature, Base64.NO_PADDING | Base64.NO_WRAP);
            base64Hash = base64Hash.substring(0, NUM_BASE64_CHAR);

            Log.d(TAG, String.format("pkg: %s -- hash: %s", packageName, base64Hash));
            return base64Hash;
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "hash:NoSuchAlgorithm", e);
        }
        return null;
    }
}
