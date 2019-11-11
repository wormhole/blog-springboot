package net.stackoverflow.blog.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.util.Date;

/**
 * 评论VO
 *
 * @author 凉衫薄
 */
@ApiModel(value = "评论")
public class CommentVO {

    @ApiModelProperty(value = "主键")
    @NotNull(message = "缺少主键字段", groups = {CommentVO.ReviewGroup.class, CommentVO.DeleteGroup.class})
    private String id;

    @ApiModelProperty(value = "昵称")
    @NotNull(message = "缺少昵称字段", groups = {CommentVO.InsertGroup.class})
    @Size(min = 1, max = 100, message = "昵称长度只能在1到100之间", groups = {CommentVO.InsertGroup.class})
    private String nickname;

    @ApiModelProperty(value = "邮箱")
    @NotNull(message = "缺少邮箱字段", groups = {CommentVO.InsertGroup.class})
    @Size(min = 1, max = 100, message = "邮箱长度只能在1到100之间", groups = {CommentVO.InsertGroup.class})
    @Email(message = "邮箱格式错误", groups = {CommentVO.InsertGroup.class})
    private String email;

    @ApiModelProperty(value = "个人主页")
    @Size(min = 1, max = 100, message = "网站地址只能在1到100之间", groups = {CommentVO.InsertGroup.class})
    @URL(message = "网址格式错误", groups = {CommentVO.InsertGroup.class})
    private String website;

    @ApiModelProperty(value = "评论内容")
    @NotNull(message = "缺少评论字段", groups = {CommentVO.InsertGroup.class})
    @Size(min = 1, max = 140, message = "评论内容长度只能在1到140之间", groups = {CommentVO.InsertGroup.class})
    private String content;

    @ApiModelProperty(value = "回复谁")
    @Size(min = 1, max = 100, message = "回复人的昵称长度只能在1到100之间", groups = {CommentVO.InsertGroup.class})
    private String replyTo;

    @ApiModelProperty(value = "是否审核")
    @DecimalMin(value = "0", message = "review值只能为0或1", groups = {CommentVO.ReviewGroup.class})
    @DecimalMax(value = "1", message = "review值只能为0或1", groups = {CommentVO.ReviewGroup.class})
    private Integer review;

    @ApiModelProperty(value = "文章主键")
    private String articleId;
    @ApiModelProperty(value = "回复日期")
    private Date date;

    //以下为扩展字段
    @ApiModelProperty(value = "文章url")
    @NotNull(message = "文章url不能为空", groups = {CommentVO.InsertGroup.class})
    private String url;

    @ApiModelProperty(value = "文章标题")
    private String articleTitle;
    @ApiModelProperty(value = "审核标志字符串")
    private String reviewStr;

    public interface InsertGroup {
    }

    public interface DeleteGroup {
    }

    public interface ReviewGroup {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public Integer getReview() {
        return review;
    }

    public void setReview(Integer review) {
        this.review = review;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getReviewStr() {
        return reviewStr;
    }

    public void setReviewStr(String reviewStr) {
        this.reviewStr = reviewStr;
    }
}
