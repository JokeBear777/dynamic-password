package com.InfoSec.dynamic_password.global.utils.service;

public interface EncryptionService {
    String encrypt(String plainText) throws Exception;
    String decrypt(String encryptedText) throws Exception;
}
