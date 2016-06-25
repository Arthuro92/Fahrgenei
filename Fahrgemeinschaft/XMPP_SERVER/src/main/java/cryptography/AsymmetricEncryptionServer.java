package cryptography;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by david on 14.06.2016.
 */
public class AsymmetricEncryptionServer {
    //new new new new new
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private SecretKey symmetricKey;

    private void addProviderBC() {
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            System.out.println("PROVIDER ADDED: " + Security.getProvider("BC"));
        } catch(Exception exception) {
            System.err.println(exception.toString());
        }
    }

    private SecureRandom createSecureRandom() throws NoSuchAlgorithmException, NoSuchProviderException {
        return SecureRandom.getInstance("SHA1PRNG");
    }

    private KeyPair createKeyPair(SecureRandom secureRandom) throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
        keyPairGenerator.initialize(2048, secureRandom);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }

    private void setKeys(KeyPair keyPair) {
        this.publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();
    }

    public String getPublicKeyString() {
        return Base64.getEncoder().encodeToString(this.publicKey.getEncoded());
    }

    public byte[] decryptPrivateKey(String encryptedKey) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException {
        Cipher cipher = Cipher.getInstance("RSA", "BC");
        cipher.init(Cipher.DECRYPT_MODE, this.privateKey);
        System.out.println("SYMMETRIC KEY STRING: "+ new String(cipher.doFinal(Base64.getDecoder().decode(encryptedKey))));
        return cipher.doFinal(Base64.getDecoder().decode(encryptedKey));
    }

    public String encryptSymmetricKey(String message) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, this.symmetricKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes()));
    }

    public String decryptSymmetricKey(String encodedMessage) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException  {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, this.symmetricKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(encodedMessage)));
    }

    public void setSymmetricKey(byte[] decodedKey) {
        this.symmetricKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    private AsymmetricEncryptionServer() throws NoSuchProviderException, NoSuchAlgorithmException {
        addProviderBC();
        setKeys(createKeyPair(createSecureRandom()));
    }

    private static AsymmetricEncryptionServer asymmetricEncryptionServer;

    public static AsymmetricEncryptionServer getInstance() throws NoSuchProviderException, NoSuchAlgorithmException {
        if(asymmetricEncryptionServer == null) {
            asymmetricEncryptionServer = new AsymmetricEncryptionServer();
        }
        return asymmetricEncryptionServer;
    }
}
