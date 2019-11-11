package net.stackoverflow.blog.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 访问记录VO
 *
 * @author 凉衫薄
 */
@ApiModel(value = "访问记录")
public class VisitVO {

    @ApiModelProperty(value = "主键")
    private String id;
    @ApiModelProperty(value = "URL")
    private String url;
    @ApiModelProperty(value = "状态码")
    private Integer status;
    @ApiModelProperty(value = "访问ip")
    private String ip;
    @ApiModelProperty(value = "客户端")
    private String agent;
    @ApiModelProperty(value = "referer字段")
    private String referer;
    @ApiModelProperty(value = "访问时间")
    private Date date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
