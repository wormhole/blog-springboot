package net.stackoverflow.blog.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 评论VO类
 *
 * @author 凉衫薄
 */
public class CommentDTO implements Serializable {

    @NotNull(message = "缺少主键字段", groups = {ReviewGroup.class, DeleteGroup.class})
    private String id;

    @NotNull(message = "缺少昵称字段", groups = {InsertGroup.class})
    @Length(min = 1, max = 100, message = "昵称长度只能在1到100之间", groups = {InsertGroup.class})
    private String nickname;

    @NotNull(message = "缺少邮箱字段", groups = {InsertGroup.class})
    @Length(min = 1, max = 100, message = "邮箱长度只能在1到100之间", groups = {InsertGroup.class})
    @Email(message = "邮箱格式错误", groups = {InsertGroup.class})
    private String email;

    @Length(min = 1, max = 100, message = "网站地址只能在1到100之间", groups = {InsertGroup.class})
    @URL(message = "网址格式错误", groups = {InsertGroup.class})
    private String website;

    @NotNull(message = "缺少评论字段", groups = {InsertGroup.class})
    @Length(min = 1, max = 140, message = "评论内容长度只能在1到140之间", groups = {InsertGroup.class})
    private String content;

    @Length(min = 1, max = 100, message = "回复人的昵称长度只能在1到100之间", groups = {InsertGroup.class})
    private String replyTo;

    @DecimalMin(value = "0", message = "review值只能为0或1", groups = {ReviewGroup.class})
    @DecimalMax(value = "1", message = "review值只能为0或1", groups = {ReviewGroup.class})
    private Integer review;

    private String articleId;
    private Date date;

    //以下为扩展字段
    @NotNull(message = "文章url不能为空", groups = {InsertGroup.class})
    private String url;

    private String articleTitle;
    private String reviewTag;

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

    public String getReviewTag() {
        return reviewTag;
    }

    public void setReviewTag(String reviewTag) {
        this.reviewTag = reviewTag;
    }
}
