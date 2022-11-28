package com.atguigu.springcloud.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

public class CryptoUtil {

    public static String hashkeyPart2Encrypted = "CXjThjwR1mm37shCa0Qd6Uajy2MQJvNmPZ0AajWZt7e+Jo83e/6V15+fhSluUm9wvSwScz5n7areTIrBRguJ0DMF/pj/HR0O7ULD0BuZxJuYAFGURstr/f3Xz4h6kIyPyBpoVcxxAA==";

    public static void main(String[] args) throws Exception{

        String encryptedKeyFromFile = "Ut1MCbKjFRTKg0AbGTJ8W/sU8NZ9Ilnjs64t4SLss5h5lqg0Xtcy0M5IjqmsXCPtNWs9LxbHXPEuo360vD6ga2gziAbYkvGxOEXrodGz/LUvBEDqQ+UxDv5hhjXeeghz9p8d";
        //1. use class name hash
        String shash = hashStr(CryptoUtil.class.getName());//Y9r7kNqQjPqgBTRLeM31AiVtUDEt88v9TQMhqitRsE4=

        //2 use class name hash to decrypt the hashkeyPart2
        hashKeyPart2 = decrypt(hashkeyPart2Encrypted, shash);

        //String hashPart2 = encrypt(hashKeyPart2, shash);
        //String encrypt = encrypt(masterKey, shash+"."+hashKeyPart2);

        //3. concatenate shash & hashKeyPart2 in 1 string to decrypt the encrypted masterKey in file
        String masterKey = decrypt(encryptedKeyFromFile, shash + "." + hashKeyPart2);//if it fails, exception will throw -> javax.crypto.AEADBadTagException: Tag mismatch!

        //4. use the master key to encrypt/decrypt further
        String encryptedKey = encrypt("abcd1234", masterKey);
        System.out.println("encryptedKey :" + encryptedKey);
        String decryptedText = decrypt(encryptedKey, masterKey);
        System.out.println("decryptedText :" + decryptedText);
    }
    public  static String hashKeyPart2;// = "abcdefg";//this should store in a file or env
    public  static String masterKey;// = "123";//this should store in a file or env
    public static final int GCM_TAG_LENGTH = 16;
    public static final String AES_GCM_NO_PADDING = "AES/GCM/NoPadding";
    public static final String ALGO_AES = "AES";


    public static String hashStr(String textToHash) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] byteOfTextToHash = textToHash.getBytes(StandardCharsets.UTF_8);
            byte[] hashedByetArray = digest.digest(byteOfTextToHash);
            return Base64.getEncoder().encodeToString(hashedByetArray);
        } catch (Exception e) {
            return textToHash;
        }
    }

    private enum SingletonSecureRandom {
        INSTANCE;
        private SecureRandom singleton;
        // JVM ensure this only execute once
        SingletonSecureRandom() {
            singleton = new SecureRandom();
        }
        public SecureRandom getInstance() {
            return singleton;
        }
    }

    public static byte[] generateIv(int ivLen) {
        byte[] iv = new byte[ivLen];
        SingletonSecureRandom.INSTANCE.getInstance().nextBytes(iv);
        return iv;
    }


    public static SecretKey getKeyFromPassword(String masterKey, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        KeySpec spec = new PBEKeySpec(masterKey.toCharArray(), salt, 2145, 256);
        return new SecretKeySpec(factory.generateSecret(spec)
                .getEncoded(), ALGO_AES);
    }


    public static byte[] encryptHelper(String algorithm, String input, SecretKey key,
                                       GCMParameterSpec gcmParameterSpec) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, gcmParameterSpec);
        return cipher.doFinal(input.getBytes());
    }

    public static String decryptHelper(String algorithm, byte[] cipherText, byte[] tag,SecretKey key,
                                       GCMParameterSpec gcmParameterSpec) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, gcmParameterSpec);
        cipher.update(cipherText);
        byte[] plainText = cipher.doFinal(tag);
        return new String(plainText, StandardCharsets.UTF_8);
    }


    public static String encrypt(String text, String masterKey) throws NoSuchAlgorithmException,
            InvalidKeySpecException, IOException, InvalidAlgorithmParameterException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        byte[] iv = generateIv(16);
        byte[] salt = generateIv(64);
        SecretKey key = getKeyFromPassword(masterKey, salt);
        byte[] cipher = encryptHelper(AES_GCM_NO_PADDING, text, key,  new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] ciphertext = Arrays.copyOfRange(cipher, 0, text.length());
        byte[] tag = Arrays.copyOfRange(cipher, text.length(), cipher.length);
        outputStream.write(salt);
        outputStream.write(iv);
        outputStream.write(tag);
        outputStream.write(ciphertext);
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }


    public static String decrypt(String encData, String masterKey) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        byte[] cipherText = Base64.getDecoder().decode(encData.getBytes(StandardCharsets.UTF_8));
        byte[] salt = Arrays.copyOfRange(cipherText, 0, 64);
        byte[] iv = Arrays.copyOfRange(cipherText, 64, 80);
        byte[] tag = Arrays.copyOfRange(cipherText, 80, 96);
        byte[] ciphertext = Arrays.copyOfRange(cipherText, 96, cipherText.length);
        SecretKey key = getKeyFromPassword(masterKey, salt);
        return decryptHelper(AES_GCM_NO_PADDING, ciphertext, tag, key, new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv));
    }
}
