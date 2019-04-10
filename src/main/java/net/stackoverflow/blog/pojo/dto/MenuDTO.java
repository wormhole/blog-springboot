package net.stackoverflow.blog.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * 菜单VO
 *
 * @author 凉衫薄
 */
public class MenuDTO implements Serializable {

    @NotNull(message = "缺少主键字段", groups = {DeleteGroup.class, UpdateGroup.class})
    private String id;

    @NotNull(message = "缺少菜单名字段", groups = {InsertGroup.class, UpdateGroup.class})
    @Length(min = 1, max = 100, message = "菜单名的长度必须在1到100之间", groups = {InsertGroup.class, UpdateGroup.class})
    private String name;

    @NotNull(message = "缺少URL字段", groups = {InsertGroup.class, UpdateGroup.class})
    @Length(min = 1, max = 100, message = "URL的长度必须在1到100之间", groups = {InsertGroup.class, UpdateGroup.class})
    @Pattern(regexp = "(^(http://|https://)([a-zA-Z0-9\\-]+\\.)+[a-zA-Z]+$)|(^/([a-zA-Z0-9\\-]+/)*[a-zA-Z0-9]*$)", message = "URL格式错误", groups = {InsertGroup.class, UpdateGroup.class})
    private String url;

    private Integer deleteAble;
    private Date date;

    //以下为扩展字段
    private String deleteTag;

    public interface DeleteGroup {
    }

    public interface InsertGroup {
    }

    public interface UpdateGroup {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getDeleteAble() {
        return deleteAble;
    }

    public void setDeleteAble(Integer deleteAble) {
        this.deleteAble = deleteAble;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDeleteTag() {
        return deleteTag;
    }

    public void setDeleteTag(String deleteTag) {
        this.deleteTag = deleteTag;
    }
}
