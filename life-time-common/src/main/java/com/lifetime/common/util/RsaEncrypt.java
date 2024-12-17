package com.lifetime.common.util;



import io.micrometer.core.lang.Nullable;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.*;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @ClassName RsaEncrypt
 * @Author Liwx
 * @Description
 * @Date 2024/4/30$ 11:13
 */
public class RsaEncrypt {
    private static final int FRAGMENT_LENGTH = 245;

    /**
     * 加密方法.
     *
     * @param plainText 要加密的文本
     * @param customPublicKey 公钥，可以从插件配置页面获取
     * @return base64编码的加密字符串
     */
    public static String encrypt(String plainText, String customPublicKey) {
        return encrypt(plainText, string2PublicKey(customPublicKey));
    }

    public static byte[] encrypt(byte[] plainTextData, Key publicKey) {

        if (plainTextData.length == 0) {
            return plainTextData;
        }
        try {
            Cipher c1 = Cipher.getInstance("RSA");
            c1.init(Cipher.ENCRYPT_MODE, publicKey);
            return dealEncryptFragment(plainTextData, c1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encrypt(String plainText, Key publicKey) {
        if (plainText == null || "".equals(plainText)) {
            return plainText;
        }
        byte[] publicEncrypt = encrypt(plainText.getBytes(StandardCharsets.UTF_8), publicKey);
        return Base64.getEncoder().encodeToString(publicEncrypt);
    }

    public static PublicKey string2PublicKey(String pubStr) {
        try {
            byte[] keyBytes = base642Byte(pubStr);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] base642Byte(String base64Key) throws Exception {
        BASE64Decoder decoder = new BASE64Decoder();
        return decoder.decodeBuffer(base64Key);
    }

    private static byte[] dealEncryptFragment(byte[] data, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException {

        byte[] result = new byte[]{};
        int i;
        for (i = 0; i < data.length; i += FRAGMENT_LENGTH) {
            byte[] fragment = subarray(data, i, i + FRAGMENT_LENGTH);
            byte[] update = cipher.doFinal(fragment);
            result = addAll(result, update);
        }
        return result;
    }

    public static byte[] subarray(@Nullable byte[] array, int startIndexInclusive, int endIndexExclusive) {
        if (array == null) {
            return null;
        } else {
            if (startIndexInclusive < 0) {
                startIndexInclusive = 0;
            }

            if (endIndexExclusive > array.length) {
                endIndexExclusive = array.length;
            }

            int newSize = endIndexExclusive - startIndexInclusive;
            if (newSize <= 0) {
                return new byte[0];
            } else {
                byte[] subarray = new byte[newSize];
                System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
                return subarray;
            }
        }
    }

    public static byte[] addAll(@Nullable byte[] array1, @Nullable byte... array2) {
        if (array1 == null) {
            return clone(array2);
        } else if (array2 == null) {
            return clone(array1);
        } else {
            byte[] joinedArray = new byte[array1.length + array2.length];
            System.arraycopy(array1, 0, joinedArray, 0, array1.length);
            System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
            return joinedArray;
        }
    }

    public static byte[] clone(@Nullable byte[] array) {
        return array == null ? null : (byte[]) array.clone();
    }

    static abstract class CharacterDecoder {
        public CharacterDecoder() {
        }

        protected abstract int bytesPerAtom();

        protected abstract int bytesPerLine();

        protected void decodeBufferPrefix(PushbackInputStream var1, OutputStream var2) throws Exception {
        }

        protected void decodeBufferSuffix(PushbackInputStream var1, OutputStream var2) throws Exception {
        }

        protected int decodeLinePrefix(PushbackInputStream var1, OutputStream var2) throws Exception {
            return this.bytesPerLine();
        }

        protected void decodeLineSuffix(PushbackInputStream var1, OutputStream var2) throws Exception {
        }

        protected void decodeAtom(PushbackInputStream var1, OutputStream var2, int var3) throws Exception {
            throw new Exception();
        }

        protected int readFully(InputStream var1, byte[] var2, int var3, int var4) throws Exception {
            for(int var5 = 0; var5 < var4; ++var5) {
                int var6 = var1.read();
                if (var6 == -1) {
                    return var5 == 0 ? -1 : var5;
                }

                var2[var5 + var3] = (byte)var6;
            }

            return var4;
        }

        public void decodeBuffer(InputStream var1, OutputStream var2) throws Exception {
            int var4 = 0;
            PushbackInputStream var5 = new PushbackInputStream(var1);
            this.decodeBufferPrefix(var5, var2);

            while(true) {
                try {
                    int var6 = this.decodeLinePrefix(var5, var2);

                    int var3;
                    for(var3 = 0; var3 + this.bytesPerAtom() < var6; var3 += this.bytesPerAtom()) {
                        this.decodeAtom(var5, var2, this.bytesPerAtom());
                        var4 += this.bytesPerAtom();
                    }

                    if (var3 + this.bytesPerAtom() == var6) {
                        this.decodeAtom(var5, var2, this.bytesPerAtom());
                        var4 += this.bytesPerAtom();
                    } else {
                        this.decodeAtom(var5, var2, var6 - var3);
                        var4 += var6 - var3;
                    }

                    this.decodeLineSuffix(var5, var2);
                } catch (Exception var8) {
                    this.decodeBufferSuffix(var5, var2);
                    return;
                }
            }
        }

        public byte[] decodeBuffer(String var1) throws Exception {
            byte[] var2 = new byte[var1.length()];
            var1.getBytes(0, var1.length(), var2, 0);
            ByteArrayInputStream var3 = new ByteArrayInputStream(var2);
            ByteArrayOutputStream var4 = new ByteArrayOutputStream();
            this.decodeBuffer(var3, var4);
            return var4.toByteArray();
        }

        public byte[] decodeBuffer(InputStream var1) throws Exception {
            ByteArrayOutputStream var2 = new ByteArrayOutputStream();
            this.decodeBuffer(var1, var2);
            return var2.toByteArray();
        }

        public ByteBuffer decodeBufferToByteBuffer(String var1) throws Exception {
            return ByteBuffer.wrap(this.decodeBuffer(var1));
        }

        public ByteBuffer decodeBufferToByteBuffer(InputStream var1) throws Exception {
            return ByteBuffer.wrap(this.decodeBuffer(var1));
        }
    }


    static class BASE64Decoder extends CharacterDecoder {
        private static final char[] pem_array = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
        private static final byte[] pem_convert_array = new byte[256];
        byte[] decode_buffer = new byte[4];

        public BASE64Decoder() {
        }

        protected int bytesPerAtom() {
            return 4;
        }

        protected int bytesPerLine() {
            return 72;
        }

        protected void decodeAtom(PushbackInputStream var1, OutputStream var2, int var3) throws Exception {
            byte var5 = -1;
            byte var6 = -1;
            byte var7 = -1;
            byte var8 = -1;
            if (var3 < 2) {
                throw new Exception("BASE64Decoder: Not enough bytes for an atom.");
            } else {
                int var4;
                do {
                    var4 = var1.read();
                    if (var4 == -1) {
                        throw new Exception();
                    }
                } while(var4 == 10 || var4 == 13);

                this.decode_buffer[0] = (byte)var4;
                var4 = this.readFully(var1, this.decode_buffer, 1, var3 - 1);
                if (var4 == -1) {
                    throw new Exception();
                } else {
                    if (var3 > 3 && this.decode_buffer[3] == 61) {
                        var3 = 3;
                    }

                    if (var3 > 2 && this.decode_buffer[2] == 61) {
                        var3 = 2;
                    }

                    switch(var3) {
                        case 4:
                            var8 = pem_convert_array[this.decode_buffer[3] & 255];
                        case 3:
                            var7 = pem_convert_array[this.decode_buffer[2] & 255];
                        case 2:
                            var6 = pem_convert_array[this.decode_buffer[1] & 255];
                            var5 = pem_convert_array[this.decode_buffer[0] & 255];
                        default:
                            switch(var3) {
                                case 2:
                                    var2.write((byte)(var5 << 2 & 252 | var6 >>> 4 & 3));
                                    break;
                                case 3:
                                    var2.write((byte)(var5 << 2 & 252 | var6 >>> 4 & 3));
                                    var2.write((byte)(var6 << 4 & 240 | var7 >>> 2 & 15));
                                    break;
                                case 4:
                                    var2.write((byte)(var5 << 2 & 252 | var6 >>> 4 & 3));
                                    var2.write((byte)(var6 << 4 & 240 | var7 >>> 2 & 15));
                                    var2.write((byte)(var7 << 6 & 192 | var8 & 63));
                            }

                    }
                }
            }
        }

        static {
            int var0;
            for(var0 = 0; var0 < 255; ++var0) {
                pem_convert_array[var0] = -1;
            }

            for(var0 = 0; var0 < pem_array.length; ++var0) {
                pem_convert_array[pem_array[var0]] = (byte)var0;
            }

        }
    }
}
