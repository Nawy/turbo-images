package com.turbo.util;

import com.turbo.model.exception.InternalServerErrorHttpException;
import org.apache.commons.lang3.StringUtils;
import org.hashids.Hashids;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * Created by rakhmetov on 01.06.17.
 */
public class EncryptionService {

    private static final Logger LOG = LoggerFactory.getLogger(EncryptionService.class);

    //make immutable from properties becose if someone change sault everything will goes to hell :D
    private static final Hashids hashids = new Hashids("Kibana_ZAuruzsxca_12437r!#^");
    // DON'T CHANGE!!!
    private static final String AES_ENCRYPT_ALGORITHM = "AES";
    // STRING FOR ENCRYPTION SHOULD HAVE STRICT SIZE OF 16 SYMBOLS, after change run tests!
    private static final byte[] keyValue = "VA$A 13K! V*KA V".getBytes();

    public static String encodeHashId(Long id) {
        if (id == null) return null;
        if(id > 9007199254740992L) {
            final long[] values = getTwoLongFromOne(id);
            return hashids.encode(values[0], values[1]);
        }
        return hashids.encode(id);
    }

    public static long decodeHashId(String id) {
        final long[] results = hashids.decode(id);
        if(results.length > 1) {
            return Long.valueOf(String.valueOf(results[0]) + String.valueOf(results[1]));
        }
        return hashids.decode(id)[0];
    }

    public static String encryptAES(String valueToEncrypt) {
        return encodeDecodeAES(Cipher.ENCRYPT_MODE, valueToEncrypt);
    }

    public static String decryptAES(String encryptedValue) {
        return encodeDecodeAES(Cipher.DECRYPT_MODE, encryptedValue);
    }

    private static String encodeDecodeAES(int encryptMode, String value) {
        if (StringUtils.isBlank(value)) throw new InternalServerErrorHttpException("encrypt/decrypt value is blank");
        Key key = new SecretKeySpec(keyValue, AES_ENCRYPT_ALGORITHM);
        try {
            byte[] modValue = encryptMode == Cipher.DECRYPT_MODE ?
                    new BASE64Decoder().decodeBuffer(value) :
                    value.getBytes();
            Cipher c = Cipher.getInstance(AES_ENCRYPT_ALGORITHM);
            c.init(encryptMode, key);
            byte[] encValue = c.doFinal(modValue);
            return encryptMode == Cipher.ENCRYPT_MODE ?
                    new BASE64Encoder().encode(encValue) :
                    new String(encValue);

        } catch (IllegalBlockSizeException e) {
            LOG.error("Illegal encryption block size", e);
        } catch (BadPaddingException e) {
            LOG.error("Bad padding of encryption", e);
        } catch (NoSuchPaddingException e) {
            LOG.error("No such padding", e);
        } catch (NoSuchAlgorithmException e) {
            LOG.error("No such algorithm", e);
        } catch (InvalidKeyException e) {
            LOG.error("Invalid encryption key", e);
        } catch (IOException e) {
            LOG.error("IOE exception occured during encryption", e);
        }
        throw new InternalServerErrorHttpException("can't encrypt/decrypt value");
    }

    private static long[] getTwoLongFromOne(final long value) {
        final long divider = 100000000L;
        final long val1 = (long)Math.floor((double)value / (double)divider);
        final long val2 = value % divider;
        return new long[]{val1, val2};
    }
}
