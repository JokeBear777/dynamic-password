package com.InfoSec.dynamic_password.global.utils.validator;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RsaKeyValidator {

    public static boolean isValidPublicKey(String publicKey) {
        try{
            byte[] keyBytes = Base64.getDecoder().decode(publicKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            PublicKey pubKey = keyFactory.generatePublic(keySpec);
            return pubKey != null;

        } catch (Exception e) {
            return false;
        }
    }
}
