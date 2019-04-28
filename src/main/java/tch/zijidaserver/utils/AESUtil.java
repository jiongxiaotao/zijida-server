package tch.zijidaserver.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
public class AESUtil {
	private String ALGO = "AES";
    private String ALGO_MODE = "AES/CBC/NoPadding";
    //aes解密模式为PKCS7：encryptedData-密文	key:密钥	iv：偏移量
    public String decrypt(String encryptedData,String key,String iv) throws Exception {
        try {
            byte[] encrypted1 = Base64.decodeBase64(encryptedData);
            byte[] ivBase64 = Base64.decodeBase64(iv); 
            Cipher cipher = Cipher.getInstance(ALGO_MODE);
            SecretKeySpec keyspec = new SecretKeySpec(Base64.decodeBase64(key), ALGO);
            IvParameterSpec ivspec = new IvParameterSpec(ivBase64);
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            return originalString.trim();//此处添加trim（）是为了去除多余的填充字符，就不用去填充了，具体有什么问题我还没有遇到，有强迫症的同学可以自己写一个PKCS7UnPadding函数
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
