package com.softwarelma.epe.p3.generic;

import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.softwarelma.epe.p1.app.EpeAppConstants;
import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalDecrypt extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "decrypt, expected the key, the encrypted value and optionally the init vector and the key suffix.";
        String key = getStringAt(listExecResult, 0, postMessage);
        String encrypted = getStringAt(listExecResult, 1, postMessage);
        String initVector = getStringAt(listExecResult, 2, postMessage, null);
        String keySuffix = getStringAt(listExecResult, 3, postMessage, null);
        String str = decrypt(key, encrypted, initVector, keySuffix);
        log(execParams, str);
        return createResult(str);
    }

    /**
     * @param key
     *            128 bit key
     */
    public static String decrypt(String key, String encrypted, String initVector, String keySuffix)
            throws EpeAppException {
        EpeAppUtils.checkNull("key", key);
        EpeAppUtils.checkNull("encrypted", encrypted);
        initVector = EpeAppUtils.isEmpty(initVector) ? EpeAppConstants.CIPHER_INIT_VECTOR : initVector;
        keySuffix = EpeAppUtils.isEmpty(keySuffix) ? EpeAppConstants.CIPHER_KEY_SUFFIX : keySuffix;

        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            key += keySuffix;
            key = key.substring(0, 16);
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));
            return new String(original);
        } catch (Exception e) {
            throw new EpeAppException("decrypt", e);
        }
    }

}
