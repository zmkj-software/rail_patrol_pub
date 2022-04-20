package cn.zhima.flame_project.wx.tools;

import java.util.Arrays;

/**
 *
 * token验证类
 * @author 冫Soul丶
 */
public class CheckUtil {
    private static final String token = "zhimakeji";
    public static boolean checkSignature(String signature, String timestamp, String nonce) {
        String[] str = new String[]{token, timestamp, nonce};
        Arrays.sort(str);
        StringBuilder buffer = new StringBuilder();
        for (String s : str) {
            buffer.append(s);
        }
        String temp = SHA1.encode(buffer.toString());
        return signature.equals(temp);
    }
}