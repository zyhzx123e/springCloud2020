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
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

public class CryptoUtil {
    
    @Autowired
    public Environment ev;

    public static void main(String[] args) throws Exception{

        String encryptedKeyFromFile = "KQ7hemiIT7z/Ye4JPouHyHOlGr6H9BE0uQBUm4FErlppGZQQDYhnwMSGxe2iIC/+jPHwWf/kue8qtoLYE/ppgHJoh/UAw2MQ9poZvo1JxiKB2pyk1SE1/a7v9B+EVgCuG5Wn";
        //1. use class hashcode to decrypt the encrypted key in file
        String shashCode = String.valueOf(CryptoUtil.class.hashCode());//1297685781

        //String encrypt = encrypt(masterKey, shashCode);
        String masterKey = decrypt(encryptedKeyFromFile, shashCode);
        //2. use the decrypted key from file as master key
        
        //3. use the master key to decrypt further
        String encryptedKey = encrypt("abcd1234", masterKey);
        System.out.println("encryptedKey :" + encryptedKey);
        String decryptedText = decrypt(encryptedKey, masterKey);
        System.out.println("decryptedText :" + decryptedText);
    }

    public static final String masterKey = "123";
    public static final int GCM_TAG_LENGTH = 16;
    public static final String AES_GCM_NO_PADDING = "AES/GCM/NoPadding";
    public static final String ALGO_AES = "AES";


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
