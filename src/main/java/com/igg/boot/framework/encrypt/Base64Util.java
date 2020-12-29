package com.igg.boot.framework.encrypt;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Util {
    public static String decode(String src) {
        return decode(src,StandardCharsets.UTF_8);
    }
    
    public static String decode(String src,Charset charset) {
        return new String(Base64.getDecoder().decode(src),charset);
    }
    
    public static String encode(String src) {
        return Base64.getEncoder().encodeToString(src.getBytes(StandardCharsets.UTF_8));
    }
}
