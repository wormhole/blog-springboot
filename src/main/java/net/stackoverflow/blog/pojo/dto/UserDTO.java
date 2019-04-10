package net.stackoverflow.blog.pojo.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 用户VO
 *
 * @author 凉衫薄
 */
public class UserDTO implements Serializable {

    private String id;

    @NotNull(message = "缺少邮箱字段", groups = {RegisterGroup.class, UpdateBaseGroup.class})
    @Length(min = 1, max = 100, message = "邮箱长度只能在1到100之间", groups = {RegisterGroup.class, UpdateBaseGroup.class})
    @Email(message = "邮箱格式错误", groups = {RegisterGroup.class, UpdateBaseGroup.class})
    private String email;

    @NotNull(message = "缺少密码字段", groups = {RegisterGroup.class, UpdatePasswordGroup.class})
    @Length(min = 6, message = "密码长度必须大于等于6", groups = {RegisterGroup.class, UpdatePasswordGroup.class})
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "密码只能为数字字母下划线", groups = {RegisterGroup.class, UpdatePasswordGroup.class})
    private String password;

    @NotNull(message = "缺少昵称字段", groups = {RegisterGroup.class, UpdateBaseGroup.class})
    @Length(min = 1, max = 100, message = "昵称长度只能在1到100之间", groups = {RegisterGroup.class, UpdateBaseGroup.class})
    private String nickname;

    private String salt;
    private Integer deleteAble;

    //以下为扩展字段
    @NotNull(message = "缺少旧密码字段", groups = {UpdatePasswordGroup.class})
    @NotBlank(message = "旧密码不能为空", groups = {UpdatePasswordGroup.class})
    private String oldPassword;

    @NotNull(message = "缺少验证码字段", groups = {RegisterGroup.class})
    @NotBlank(message = "验证码不能为空", groups = {RegisterGroup.class})
    private String vcode;

    public interface RegisterGroup {
    }

    public interface UpdateBaseGroup {
    }

    public interface UpdatePasswordGroup {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Integer getDeleteAble() {
        return deleteAble;
    }

    public void setDeleteAble(Integer deleteAble) {
        this.deleteAble = deleteAble;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getVcode() {
        return vcode;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }
}
