package net.stackoverflow.blog.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 菜单VO
 *
 * @author 凉衫薄
 */
@ApiModel(value = "菜单")
public class MenuVO {

    @ApiModelProperty(value = "主键")
    @NotNull(message = "缺少主键字段", groups = {MenuVO.DeleteGroup.class, MenuVO.UpdateGroup.class})
    private String id;

    @ApiModelProperty(value = "菜单名称")
    @NotNull(message = "缺少菜单名字段", groups = {MenuVO.InsertGroup.class, MenuVO.UpdateGroup.class})
    @Size(min = 1, max = 100, message = "菜单名的长度必须在1到100之间", groups = {MenuVO.InsertGroup.class, MenuVO.UpdateGroup.class})
    private String name;

    @ApiModelProperty(value = "菜单URL")
    @NotNull(message = "缺少URL字段", groups = {MenuVO.InsertGroup.class, MenuVO.UpdateGroup.class})
    @Size(min = 1, max = 100, message = "URL的长度必须在1到100之间", groups = {MenuVO.InsertGroup.class, MenuVO.UpdateGroup.class})
    @Pattern(regexp = "(^(http://|https://)([a-zA-Z0-9\\-]+\\.)+[a-zA-Z]+/([a-zA-Z0-9\\-]+/)*[a-zA-Z0-9]*$)|(^/([a-zA-Z0-9\\-]+/)*[a-zA-Z0-9]*$)", message = "URL格式错误", groups = {MenuVO.InsertGroup.class, MenuVO.UpdateGroup.class})
    private String url;

    @ApiModelProperty(value = "是否可删除标志")
    private Integer deleteAble;
    @ApiModelProperty(value = "创建时间")
    private Date date;

    //以下为扩展字段
    @ApiModelProperty(value = "可删除标志字符串")
    private String deleteStr;

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

    public String getDeleteStr() {
        return deleteStr;
    }

    public void setDeleteStr(String deleteStr) {
        this.deleteStr = deleteStr;
    }
}
