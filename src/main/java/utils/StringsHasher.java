/*
 *  ASTI Services (c) © 2013
 *  Consultoria de Software.
 */

package utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import org.apache.ws.commons.util.Base64;

/**
 *
 * @author Rene Vera Apale
 */
public class StringsHasher {
    
    /**
     * Hashes the specified {@code inputPassword} with the specified salt using SHA-256. 
     * Then it verifies that the resulting hash matches the specified {@code storedDigestion}
     * @param salt Stored salt originally used for the hashing
     * @param userInput  The password that should be hashed
     * @param storedDigestion  The password to match the hashed string against
     * @return {@code true} when the passwords match, {@code false} otherwise
     * @throws org.apache.ws.commons.util.Base64.DecodingException When the salt string
     * is corrupted
     * @throws NoSuchAlgorithmException When an unknown algorithm is specified
     * @throws java.io.UnsupportedEncodingException
     */
    public static boolean stringsMatch(String userInput, String salt, String storedDigestion) throws Base64.DecodingException, NoSuchAlgorithmException, 
            UnsupportedEncodingException {
        String digestedInput = doHashing(userInput, salt).digestedPassword;
        return digestedInput.equalsIgnoreCase(storedDigestion);
    }
    /**
     * Computes the hashing of the specified password with a salt of length 256, using SHA-256.
     * @param userInput  The password to hash
     * @return An instance of {@link HashedPair} with the processed data
     * @throws java.security.NoSuchAlgorithmException 
     * @throws org.apache.ws.commons.util.Base64.DecodingException 
     **/
    public static HashedPair digestString(String userInput) throws Base64.DecodingException, NoSuchAlgorithmException, UnsupportedEncodingException {
        return doHashing(userInput, null);
    }
    
    private static HashedPair doHashing(String userInput, String salt) throws Base64.DecodingException, NoSuchAlgorithmException, 
            UnsupportedEncodingException {
        // Get salt bytes
        byte[] saltBytes;
        if (salt != null)
            saltBytes = Base64.decode(salt);
        else {
            saltBytes = new byte[256];
            new Random().nextBytes(saltBytes);
        }
        // Digest input with salt, using SHA-256
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(saltBytes);
        byte[] passwordBytes = digest.digest(userInput.getBytes("UTF-8"));
        // Compose digested bytes into text representation
        StringBuilder bytesAsString = new StringBuilder();
        for (int i = 0; i < passwordBytes.length; i++) {
            // Flip negative values, positives stay the same. Also all values must be over 100
            int flippedInt = passwordBytes[i] & 0xff;
            flippedInt = flippedInt + 0x100;
            
            String hexRep = Integer.toString(flippedInt, 16);
            // Chop off first position as it's always 1
            bytesAsString.append(hexRep.substring(1));
        }
        return new HashedPair(bytesAsString.toString(), Base64.encode(saltBytes));
    }
    
    /**
     * Utility class to temporarily hold the digested credentials.
     */
    public static class HashedPair {
        
        /**
         * Password after beign digested by the specified algorithm
         */
        public String digestedPassword;
        /**
         * Base64 representation of the randomly generated bytes used to salt the digestion
         */
        public String b64Salt;
        
        public HashedPair(String password, String salt) {
            this.digestedPassword = password;
            this.b64Salt = salt;
        }
    }
}