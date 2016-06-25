package com.android.cows.fahrgemeinschaft.cryptography;

import android.util.Base64;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by david on 13.06.2016.
 */
public class AsymmetricEncryptionClient {
    //new new new new new
    private PublicKey publicKey;
    private SecretKey symmetricKey;

    private void addProviderBC() {
        try {
            System.out.println("PROVIDER ADDED: " + Security.getProvider("BC"));
        } catch(Exception exception) {
            System.err.println(exception.toString());
        }
    }

    private SecretKey createSymmetricKey() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        SecretKey secretKey = keyGenerator.generateKey();
        this.symmetricKey = secretKey;
//        System.out.println("SECRET KEY: " + secretKey.toString());
//        String encodedKey = Base64.encodeToString(secretKey.getEncoded(), Base64.DEFAULT);
//        System.out.println("KEY ENCODED SEND: " + encodedKey);
//        byte[] decodedKey = Base64.decode(encodedKey, Base64.DEFAULT);
//        secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
//        System.out.println("KEY RECEIVED: " + secretKey.toString());
        return secretKey;
    }

    public void setPublicKey(String publicKey) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decode(publicKey, Base64.DEFAULT));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
        this.publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
    }

    public String encryptSymmetricKey(String message) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, this.symmetricKey);
        return Base64.encodeToString(cipher.doFinal(message.getBytes()), Base64.DEFAULT);
    }

    public String decryptSymmetricKey(String encodedMessage) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException  {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, this.symmetricKey);
        return new String(cipher.doFinal(Base64.decode(encodedMessage, Base64.DEFAULT)));
    }

    public String encryptPublicKey() throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, this.publicKey);
        System.out.println("SYMMETRIC KEY STRING: " + new String(this.symmetricKey.getEncoded()));
        return Base64.encodeToString(cipher.doFinal(this.symmetricKey.getEncoded()), Base64.NO_WRAP);
    }

    public boolean checkKeys() {
        return (this.publicKey != null && this.symmetricKey != null);
    }

    private AsymmetricEncryptionClient() throws NoSuchAlgorithmException, NoSuchProviderException {
        addProviderBC();
        createSymmetricKey();
    }

    private static AsymmetricEncryptionClient asymmetricEncryptionClient = null;

    public static AsymmetricEncryptionClient getInstance() throws NoSuchProviderException, NoSuchAlgorithmException {
        if(asymmetricEncryptionClient == null) {
            asymmetricEncryptionClient = new AsymmetricEncryptionClient();
        }
        return asymmetricEncryptionClient;
    }
}
