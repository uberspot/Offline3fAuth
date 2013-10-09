package utils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.google.gson.Gson;

/** Class contains static methods for encrypting and decrypting serializable objects
 */
public class ObjCrypter {
	
	/** The algorithm used for encryption */
	private static String AES_ALG = "PBEWITHSHA256AND128BITAES-CBC-BC";
	
	/** The password used by default if a pass is not provided in the arguments */
	private static final char[] defaultPass = "s0Methin6!!".toCharArray();
	
    private static byte[] salt = "a9v5n38s".getBytes();
    
    /** Encrypts the Serializable object with AES
     * @param plaintext the Serializable object to encrypt
     * @param password the password to use for encryption, if it's null or empty the default pass will be used instead
     * @return an encrypter String formatted as json containing the used cipher and the encrypted object
     */
    public static String encryptAES(Serializable plaintext, String password) {
		try{
			final PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 20);
			final PBEKeySpec pbeKeySpec = new PBEKeySpec( 
		    		(password==null || password.equalsIgnoreCase(""))?defaultPass:password.toCharArray()  );
			final SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(AES_ALG);
			final SecretKey secretKey = secretKeyFactory.generateSecret(pbeKeySpec);
	
			final Cipher cipher = Cipher.getInstance(AES_ALG);
		    cipher.init(Cipher.ENCRYPT_MODE,secretKey,pbeParamSpec);
	
		    return gson.toJson(new SealedObject(plaintext,cipher));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
    /** Decrypts an AES encrypted String
     * @param encString the String to decrypt, formatted as json containing the used cipher and the encrypted object
     * @param password the password to use for decryption, if it's null or empty the default pass will be used instead
     * @return a Serializable decrypted object 
     */
	public static Serializable decryptAES(String encString, String password) {
		try{
			final PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 20);
			final PBEKeySpec pbeKeySpec = new PBEKeySpec( 
		    		(password==null || password.equalsIgnoreCase(""))?defaultPass:password.toCharArray()  );
			final SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(AES_ALG);
			final SecretKey secretKey = secretKeyFactory.generateSecret(pbeKeySpec);
	
			final Cipher cipher = Cipher.getInstance(AES_ALG);
		    cipher.init(Cipher.DECRYPT_MODE,secretKey,pbeParamSpec);
		    return (Serializable) (gson.fromJson(encString, SealedObject.class)).getObject(cipher);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** A Gson object used for serialization and deserialization */
	private static final Gson gson = new Gson();
	
	/** Returns the hashed SHA-1 representation of the given text 
	 * @param text the text to hash
	 * @return the SHA-1 representation of that text
	 */
	public static String toSHA1(String text) {
		try {
			final MessageDigest md = MessageDigest.getInstance("SHA-1");
		
	        md.update(text.getBytes("UTF-8"));
	        
	        final byte byteData[] = md.digest();
	
	        //convert the byte to hex format method 1
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < byteData.length; i++) {
	        	sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        return sb.toString();
		} catch (NoSuchAlgorithmException e) { 
			System.out.println("No sha1 algorithm: " + e);
		} catch (UnsupportedEncodingException e) { 
			System.out.println("Wrong encoding in string: " + e);
		}
		return text;
	}

	public static byte[] encrypt3DES(byte[] message, String password) 
				throws NoSuchAlgorithmException, UnsupportedEncodingException, 
				NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, 
				IllegalBlockSizeException, BadPaddingException {
	    	final byte[] digestOfPassword = MessageDigest.getInstance("md5")
	    									.digest(password.getBytes("ISO-8859-1"));//"utf-8"
	    	final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
	    	for (int j = 0, k = 16; j < 8;) {
	    		keyBytes[k++] = keyBytes[j++];
	    	}
	
	    	final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
	    	final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
	    	cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(new byte[8]));

	    	return cipher.doFinal(message);
    }

    public static byte[] decrypt3DES(byte[] message, String password) 
    			throws NoSuchAlgorithmException, UnsupportedEncodingException, 
    				InvalidKeyException, InvalidAlgorithmParameterException, 
    				NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
	    	final byte[] digestOfPassword = MessageDigest.getInstance("md5")
	    										.digest(password.getBytes("ISO-8859-1")); //"utf-8"
	    	final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
	    	for (int j = 0, k = 16; j < 8;) {
	    		keyBytes[k++] = keyBytes[j++];
	    	}
	
	    	final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
	    	final Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
	    	decipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(new byte[8]));
	
	    	return decipher.doFinal(message);
    }
}
