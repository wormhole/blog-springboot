package net.stackoverflow.blog.validator;

import net.stackoverflow.blog.pojo.vo.SettingVO;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义校验器
 *
 * @author 凉衫薄
 */
public class SettingVOValidator {

    Pattern numPattern = Pattern.compile("^[0-9]+$");

    /**
     * 校验title
     *
     * @param title
     * @return
     */
    private boolean validateTitle(String title) {
        if (0 < title.length() && title.length() <= 100) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 校验keywords
     *
     * @param keywords
     * @return
     */
    private boolean validateKeywords(String keywords) {
        if (0 < keywords.length() && keywords.length() <= 100) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 校验description
     *
     * @param description
     * @return
     */
    private boolean validateDescription(String description) {
        if (0 < description.length() && description.length() <= 100) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 校验版权copyright
     *
     * @param copyright
     * @return
     */
    private boolean validateCopyright(String copyright) {
        if (0 < copyright.length() && copyright.length() <= 100) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 校验昵称nickname
     *
     * @param nickname
     * @return
     */
    private boolean validateNickname(String nickname) {
        if (0 < nickname.length() && nickname.length() <= 100) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 校验签名signature
     *
     * @param signature
     * @return
     */
    private boolean validateSignature(String signature) {
        if (0 < signature.length() && signature.length() <= 100) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 校验每页显示的文章数是否为数字
     *
     * @param items
     * @return
     */
    private boolean validateItems(String items) {
        Matcher matcher = numPattern.matcher(items);
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }

    public Map<String, String> validate(SettingVO[] settingVOs) {
        Map<String, String> errMap = new HashMap<>(16);
        for (SettingVO settingVO : settingVOs) {
            switch (settingVO.getName()) {
                case "title":
                    if (!validateTitle(settingVO.getValue())) {
                        errMap.put("title", "标题长度应该在1到100之间");
                    }
                    break;
                case "keywords":
                    if (!validateKeywords(settingVO.getValue())) {
                        errMap.put("keywords", "关键字长度应该在1到100之间");
                    }
                    break;
                case "description":
                    if (!validateDescription(settingVO.getValue())) {
                        errMap.put("description", "描述长度应该在1到100之间");
                    }
                    break;
                case "copyright":
                    if (!validateCopyright(settingVO.getValue())) {
                        errMap.put("copyright", "版权长度应该在1到100之间");
                    }
                    break;
                case "name":
                    if (!validateNickname(settingVO.getValue())) {
                        errMap.put("name", "网站名长度应该在1到100之间");
                    }
                    break;
                case "signature":
                    if (!validateSignature(settingVO.getValue())) {
                        errMap.put("signature", "签名长度应该在1到100之间");
                    }
                    break;
                case "limit":
                    if (!validateItems(settingVO.getValue())) {
                        errMap.put("limit", "每页显示的文章数只能为数字");
                    }
                    break;
                default:
                    break;
            }
        }
        if (errMap.size() == 0) {
            return null;
        } else {
            return errMap;
        }
    }
}
