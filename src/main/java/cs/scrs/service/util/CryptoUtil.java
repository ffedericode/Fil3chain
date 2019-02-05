package cs.scrs.service.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class CryptoUtil {
    
    // genera una coppia di chiavi RSA a 2048 bit
    public static KeyPair generaChiave()throws Exception{
        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
        keyGenerator.initialize(2048); // genera chiavi RSA a 2048 bit
        KeyPair keys = keyGenerator.generateKeyPair();
        return keys;
    }
    
    public static String getPrKey(KeyPair keys){
        PrivateKey prKey = keys.getPrivate();
        byte[] encodedKey = prKey.getEncoded();
        return Base64.encodeBase64String(encodedKey);    
    }
    
     public static String getPuKey(KeyPair keys){
        PublicKey puKey = keys.getPublic();
        byte[] encodedKey = puKey.getEncoded();
        return Base64.encodeBase64String(encodedKey);
    }
     
    public static String cifra(String key,String msg) throws Exception{
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE,rebuildPrK(key));
        byte[] encVal = cipher.doFinal(msg.getBytes());
        String encryptedValue = Base64.encodeBase64String(encVal);
        return encryptedValue;
    }
    
     public static String decifra(String key,String msg) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE,rebuildPuK(key));
        byte[] decordedValue = Base64.decodeBase64(msg);
        byte[] decValue = cipher.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }
     
    public static String sign(String msg,String prKey)throws Exception{
        String hash = org.apache.commons.codec.digest.DigestUtils.sha256Hex(msg);
        String signature = cifra(prKey,hash);
        return signature;
    }
    
    public static Boolean verifySignature(String msg,String signature, String puK) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException{
        String hashMsg = org.apache.commons.codec.digest.DigestUtils.sha256Hex(msg);
        String decryptedSignature = decifra(puK,signature);
        return hashMsg.equals(decryptedSignature);
    }
 
    private static PublicKey rebuildPuK(String keyEncoding) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] encoded = Base64.decodeBase64(keyEncoding);
        PublicKey puK = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(encoded));
        return puK;
    }
    //NUll
    private static PrivateKey rebuildPrK(String keyEncoding)throws Exception{
        byte[] encoded = Base64.decodeBase64(keyEncoding);
        PrivateKey prK = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(encoded));
        return prK;
    }
}
