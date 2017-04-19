package com.shaw.util;

/**
 * Created by imn5100 on 2017/4/19.
 */
public class CodecUtils {
    public static final String PRE_ID_ENCODE_SALT = PropertiesUtil.getConfiguration().getString("pre_id_encode_salt");
    public static final String SUF_ENCODE_SALT = PropertiesUtil.getConfiguration().getString("suf_id_encode_salt");

    public static int getDecodeId(String encodeId) {
        String decodeId = new String(org.apache.commons.codec.binary.Base64.decodeBase64(encodeId));
        if (decodeId.length() > PRE_ID_ENCODE_SALT.length() && decodeId.length() > SUF_ENCODE_SALT.length()) {
            return NumberUtils.parseIntQuietly(decodeId.substring(PRE_ID_ENCODE_SALT.length(), decodeId.length() - SUF_ENCODE_SALT.length()), 0);
        } else {
            return 0;
        }
    }

    public static String getEncodeId(Integer id) {
        if (id != null && id > 0) {
            String encodeStr = PRE_ID_ENCODE_SALT + id + SUF_ENCODE_SALT;
            return org.apache.commons.codec.binary.Base64.encodeBase64String(encodeStr.getBytes());
        } else {
            return null;
        }
    }
}
