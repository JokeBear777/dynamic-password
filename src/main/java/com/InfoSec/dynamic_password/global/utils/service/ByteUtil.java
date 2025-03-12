package com.InfoSec.dynamic_password.global.utils.service;


import java.nio.charset.StandardCharsets;

public class ByteUtil {
    private ByteUtil() {
    }

    public static String toString(Object obj) {
        if (obj instanceof byte[]) {
            return new String((byte[]) obj, StandardCharsets.UTF_8);
        } else if (obj instanceof String) {
            return (String) obj;
        } else {
            throw new IllegalArgumentException("Unsupported type: " + obj.getClass().getName());
        }
    }

    public static byte[] toBytes(Object obj) {
        if (obj instanceof byte[]) {
            return (byte[]) obj;
        } else if (obj instanceof String) {
            return ((String) obj).getBytes(StandardCharsets.UTF_8);
        } else {
            throw new IllegalArgumentException("Unsupported type: " + obj.getClass().getName());
        }

    }


}