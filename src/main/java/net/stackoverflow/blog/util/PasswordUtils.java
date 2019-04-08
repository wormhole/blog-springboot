package net.stackoverflow.blog.util;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * 密码工具类
 *
 * @author 凉衫薄
 */
public class PasswordUtils {
    private static RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
    private static final String algorithmName = "md5";
    private static final int hashIterations = 1;

    /**
     * 获取盐
     *
     * @return 返回获取的盐值
     */
    public static String getSalt() {
        return randomNumberGenerator.nextBytes().toHex();
    }

    /**
     * 加密密码
     *
     * @param salt     盐值
     * @param password 待加密的密码
     * @return 返回加密后的密码
     */
    public static String encryptPassword(String salt, String password) {
        String newPassword = new SimpleHash(algorithmName, password, ByteSource.Util.bytes(salt), hashIterations).toHex();
        return newPassword;
    }

}