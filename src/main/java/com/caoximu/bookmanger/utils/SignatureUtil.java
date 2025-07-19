package com.caoximu.bookmanger.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class SignatureUtil {
    private static final String ALGORITHM_RSA = "SHA256withRSA";
    private static final String ALGORITHM_HMAC = "HmacSHA256";

    public static String signWithRsa(String data, String privateKeyStr) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            byte[] keyBytes = decodeBase64(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(data.getBytes());
            byte[] signed = signature.sign();
            return Base64.getEncoder().encodeToString(signed);
        } catch (Exception e) {
            throw new RuntimeException("Error generating RSA signature", e);
        }
    }

    public static String signWithHmac(String data, String key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hmacData = mac.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(hmacData);
        } catch (Exception e) {
            throw new RuntimeException("Error generating HMAC signature", e);
        }
    }

    public static boolean verifyWithRsa(String data, String publicKeyStr, String signature) {
        try {
            byte[] keyBytes = decodeBase64(publicKeyStr);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(publicKey);
            sig.update(data.getBytes());
            return sig.verify(Base64.getDecoder().decode(signature));
        } catch (Exception e) {
            throw new RuntimeException("Error verifying RSA signature", e);
        }
    }

    public static boolean verifyWithHmac(String data, String key, String signature) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hmacData = mac.doFinal(data.getBytes());
            byte[] signatureBytes = Base64.getDecoder().decode(signature);
            return Arrays.equals(hmacData, signatureBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error verifying HMAC signature", e);
        }
    }

    public static String encryptWithRsa(String data, String publicKeyStr) {
        try {
            byte[] keyBytes = decodeBase64(publicKeyStr);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(1, publicKey);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting data with RSA", e);
        }
    }

    public static String decryptWithRsa(String encryptedData, String privateKeyStr) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            byte[] keyBytes = decodeBase64(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(2, privateKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting data with RSA", e);
        }
    }

    private static byte[] decodeBase64(String str) {
        return Base64.getDecoder().decode(str.replaceAll("^(-----BEGIN.*KEY-----\\n?)|(\\n?-----END.*KEY-----)", "").replaceAll("\\s", ""));
    }

    public static void main(String[] args) {
        String pulk = "-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAl9egxbiu4UJvIUUIHNm9\nLt6s1yR0cRampGQDb4c+FGcdImuTzyrecT2CWsqXZQ4ZBIFTlKItGFleJ7qveFkT\ni4Ud76/2INkcrQoUYu47uKeAJAmXjujOgCIh+QpV1lD44zGp6ZAJ6quq9dlNpCV4\n/GQT9hkIYyvpS6SbIS1/lj1wijYkSF9dghghzj4xXPeEE7khRFxr4niFY2UTmKji\nnMNhkfmnpvidaz2UjqKpSeOpPste1+gjnp5CnfXvvLWi669qIrRIjBSk0JAbryq9\nC13IpxV6yrXjxPSXXQQ3ASMl3XA2fiVMunVko2lo+ztqSQU7tyCkB8bVhuhBMGUW\nNwIDAQAB\n-----END PUBLIC KEY-----";
        String prik = "-----BEGIN RSA PRIVATE KEY-----\n      MIIEowIBAAKCAQEAl9egxbiu4UJvIUUIHNm9Lt6s1yR0cRampGQDb4c+FGcdImuT\n      zyrecT2CWsqXZQ4ZBIFTlKItGFleJ7qveFkTi4Ud76/2INkcrQoUYu47uKeAJAmX\n      jujOgCIh+QpV1lD44zGp6ZAJ6quq9dlNpCV4/GQT9hkIYyvpS6SbIS1/lj1wijYk\n      SF9dghghzj4xXPeEE7khRFxr4niFY2UTmKjinMNhkfmnpvidaz2UjqKpSeOpPste\n      1+gjnp5CnfXvvLWi669qIrRIjBSk0JAbryq9C13IpxV6yrXjxPSXXQQ3ASMl3XA2\n      fiVMunVko2lo+ztqSQU7tyCkB8bVhuhBMGUWNwIDAQABAoIBAAYqG5LWDp4tOPRx\n      fa9+Ki9kRuwXxMpv5aVi52jNus16lN+Nh9tZ8tycvKq8SC0yx1KRgDfWkFzJXrM0\n      4bl8aaNjwiALABkRTMK8WAY0LySDWFvI2pL+DJ7TGUH9E223V9i8GW6BXzBcdn0I\n      rMhEj9w0oULVKAFZX7KnJM0nWw4EXIJ5x8Vg5+IjtSvGW+HERhyFSqQfwu8L0PeG\n      +mgzqwxVdaQ1DDcd6l/zb+mO6xV6RQfGVhnxY6gCLEIFPJZPqZagOMTpfUFdsBdw\n      qMt5UKIXVLh/ohodTl9lGvI25BwP8TPoV9cwNDdl4QLKOidsMnX3NuzBXTdAgMAo\n      w6jYaBECgYEAu25YILSFxJVcw5hDGfcb8W0zbWAfl1T9UrZhd6tA2LjeT6A/2RBX\n      GxIgsV5G2QbRb9Ms4dsnp2b4Cey9uOV/7uAcxFp0qrbjuTCSNzrn9FLfU0vtN2Q+\n      LfFaS3/me2sZjsMuhMhalDukhEMjwyr8+fJ8fJg8RySMBlQ1FNRaZEsCgYEAz2RC\n      HpldjJKVltYAgzZgPCvvjq4PopIXc8gGLmI0uy9YBOQPl2iQC/NVedUQxFtxr7LA\n      fyQvflP0ESknagHXfCXTfdbppaPm0l8yqRuaVK01QXrPfm/0KuL7w01Bjun/VW7o\n      G9mUgwmNCV0zyfXc4vLBv9RDryAhNy0qf8jjakUCgYAHIfXcmMIWbxCuqNa04ECB\n      +YPhf6z0F1nkkeg3o56+IJrlMNX9LrAB6biwx0Ou8qF5vfGRTsO9jR+XURkPNjye\n      HI07s62I6ZjU7VJDI8y78YNqKhOG7hunp+2lveJJhBPe6PKWPwGjbnmeRufPf41M\n      /btbCngQxQVHIGE8v7AI7QKBgQCDfo0Ru6n95D8miP+AMmzoGe9lJuXQ7RIjnhKV\n      PD005frH9xYPqQOUIC/09GwSI+y8OvkkY/wc+/wHGXHdG9fV5mHnKumv1XEXt7Z2\n      VIMQ51WZ89U11KQawRpiJUzI4YK8V3qhld49C0Q6SLb6eYIY/1RrXZmgox2MT33f\n      0a6nhQKBgBYiXhzNoZoYe4bkjlGLM5S2SczfHpCwaW1ZhDJPtXuKbUT690fe2YBy\n      NUKkuDoBLvFEd9EmxFmHoHnpVaGyYDADiF5vaB0s5TAPAqaYCbnBSjP9pY/oS0wy\n      j6y4qbjNloDgd07dkJt/J6LGCmjDLiSwh8vdhftC+pCE/0COhcDd\n      -----END RSA PRIVATE KEY-----";
        String s = pulk.replaceAll("^(-----BEGIN.*KEY-----\\n?)|(\\n?-----END.*KEY-----)", "").replaceAll("\\s", "");
        System.out.println(s);
        String data = "123456";
        String encryptedData = encryptWithRsa(data, pulk);
        System.out.println("Encrypted: \n" + encryptedData);
        String decryptedData = decryptWithRsa("PKoNbaXpj7keY5hmgFxvP46XJ/XEPrHUQeoJBJG89vPWTtXfRutAYYF5rtntUr58Qy3hwUqDLU5EbMKlSUmm4OJBCpxnJFyYF4KnDrrYnWrSmtvb+8/j2s1GAJFq0CtDmkjKE/anQ1hsoPyqcGIKW30eUWvWPZv7nd1NqLoul3zoMxyQ1TsMdtKmRnUjKpGpm3BMunjF5xUpROMCHlD4CSXDKk3N7AA4R1YBiH3tkUd4k/dwikcF/Xju05SRT1PxhDKDgBYBcvknCsGeD0iNYWDIAYoGAQywRTiiktMpNDypyw7oU6NYskxOBdz5++Kir1ramP7q3QFIHjcv4kio9Q==", prik);
        System.out.println("Decrypted: \n" + decryptedData);
    }
}