package com.yjiang.test;

import org.junit.Test;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Base64Test {
    @Test
    public void getAuth() throws Exception
    {
        String aeskey="VqJQKvLe4tiLC80R"; // 固定的密码（必须16位）
        byte[] raw=aeskey.getBytes("utf-8");
        SecretKeySpec skeySpec=new SecretKeySpec(raw,"AES");
        Cipher cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv=new IvParameterSpec("2018040217359546".getBytes("utf-8"));
        cipher.init(Cipher.ENCRYPT_MODE,skeySpec,iv);
//        String now=(new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date());
        String now = "20230228152900";
        byte[] encrypted=cipher.doFinal(now.getBytes("utf-8"));
        System.out.println( new BASE64Encoder().encode(encrypted));
    }
}
