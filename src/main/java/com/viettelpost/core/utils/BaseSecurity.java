package com.viettelpost.core.utils;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import com.viettelpost.core.services.domains.UserInfo;
import net.minidev.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;

public class BaseSecurity {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    //    final static String rootFolder = "/home/luannt/app/cfg/";
    final static String rootFolder = "/home/hieubv/projects/viettel/crm_v2/";

    public static String md5(String input) throws Exception {
        return DigestUtils.md5Hex(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String sha256Hex(String input) throws Exception {
        return DigestUtils.sha256Hex(input);
    }


    public static UserInfo validateToken(String token) {
        SignedJWT signedJWT;
        try {
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(parseBase64Binary(getKey(rootFolder + "KeyPair/vtman.public", false)));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKey bobPubKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
            signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new RSASSAVerifier(bobPubKey);
            if (signedJWT.verify(verifier)) {
                JSONObject objs = signedJWT.getPayload().toJSONObject();
                if (new UserInfo(objs).verify())
                    return new UserInfo(objs);
            }
        } catch (Exception e) {
            System.out.println("token error: " + e.getLocalizedMessage());
        }
        return null;
    }

    public static String getKey(String filename, boolean isPrivateKey) throws Exception {
        File f = new File(filename);
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int) f.length()];
        dis.readFully(keyBytes);
        dis.close();

        String temp = new String(keyBytes);
        String pem = null;
        if (isPrivateKey) {
            pem = temp.replace("-----BEGIN RSA PRIVATE KEY-----", "");
            pem = pem.replace("-----END RSA PRIVATE KEY-----", "");
        } else {
            pem = temp.replace("-----BEGIN PUBLIC KEY-----", "");
            pem = pem.replace("-----END PUBLIC KEY-----", "");
        }
        return pem;
    }

}
