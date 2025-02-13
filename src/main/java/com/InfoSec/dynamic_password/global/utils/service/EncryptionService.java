package com.InfoSec.dynamic_password.global.utils.service;

import java.security.PublicKey;

public interface EncryptionService {
    String encrypt(String plainText) throws Exception;
    String decrypt(String encryptedText) throws Exception;
    String encryptWithRsaPublicKey(String plainTest, String base64PublicKey);
    PublicKey base64Decode(String base64RsaPublicKey);
}
