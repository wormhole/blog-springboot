package net.stackoverflow.blog.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * 文章VO类
 *
 * @author 凉衫薄
 */
@ApiModel(value = "文章VO类")
public class ArticleVO {

    @ApiModelProperty(value = "文章主键")
    @NotNull(message = "缺少主键字段", groups = {ArticleVO.UpdateGroup.class, ArticleVO.VisibleGroup.class})
    private String id;

    @ApiModelProperty(value = "文章标题")
    @NotNull(message = "缺少标题字段", groups = {ArticleVO.UpdateGroup.class, ArticleVO.InsertGroup.class})
    @Length(min = 1, max = 100, message = "标题长度只能在1到100之内", groups = {ArticleVO.UpdateGroup.class, ArticleVO.InsertGroup.class})
    private String title;

    @ApiModelProperty(value = "文章md")
    @NotNull(message = "缺少文章md字段", groups = {ArticleVO.UpdateGroup.class, ArticleVO.InsertGroup.class})
    @Length(min = 1, message = "文章长度要大于等于1", groups = {ArticleVO.UpdateGroup.class, ArticleVO.InsertGroup.class})
    private String articleMd;

    @ApiModelProperty(value = "文章html")
    @NotNull(message = "缺少文章html字段", groups = {ArticleVO.UpdateGroup.class, ArticleVO.InsertGroup.class})
    @Length(min = 1, message = "文章长度要大于等于1", groups = {ArticleVO.UpdateGroup.class, ArticleVO.InsertGroup.class})
    private String articleHtml;

    @ApiModelProperty(value = "文章分类主键")
    @NotNull(message = "缺少分类字段", groups = {ArticleVO.UpdateGroup.class, ArticleVO.InsertGroup.class})
    private String categoryId;

    @ApiModelProperty(value = "文章可视化标志，0-不可见，1-可见")
    @NotNull(message = "缺少可视化标志字段", groups = {ArticleVO.VisibleGroup.class})
    @DecimalMax(value = "1", message = "可视化标志只能为1或0", groups = {ArticleVO.VisibleGroup.class})
    @DecimalMin(value = "0", message = "可视化标志只能为1或0", groups = {ArticleVO.VisibleGroup.class})
    private Integer visible;

    @ApiModelProperty(value = "文章URL")
    @NotNull(message = "缺少url字段", groups = {ArticleVO.LikeGroup.class})
    private String url;

    @ApiModelProperty(value = "文章创建时间")
    private Date createDate;
    @ApiModelProperty(value = "文章修改时间")
    private Date modifyDate;
    @ApiModelProperty(value = "文章点击量")
    private Integer hits;
    @ApiModelProperty(value = "文章点赞数")
    private Integer likes;

    //以下为扩展字段
    @ApiModelProperty(value = "文章编码")
    @NotNull(message = "缺少文章编码字段", groups = {ArticleVO.UpdateGroup.class, ArticleVO.InsertGroup.class})
    @Length(min = 1, max = 100, message = "编码长度只能在1到100之间", groups = {ArticleVO.UpdateGroup.class, ArticleVO.InsertGroup.class})
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "编码只能为字母数字下划线", groups = {ArticleVO.UpdateGroup.class, ArticleVO.InsertGroup.class})
    private String articleCode;

    @ApiModelProperty(value = "文章预览")
    private String preview;
    @ApiModelProperty(value = "文章作者")
    private String author;
    @ApiModelProperty(value = "文章分类名")
    private String categoryName;
    @ApiModelProperty(value = "文章评论数")
    private Integer commentCount;
    private String visibleStr;

    public interface UpdateGroup {
    }

    public interface InsertGroup {
    }

    public interface VisibleGroup {
    }

    public interface LikeGroup {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArticleMd() {
        return articleMd;
    }

    public void setArticleMd(String articleMd) {
        this.articleMd = articleMd;
    }

    public String getArticleHtml() {
        return articleHtml;
    }

    public void setArticleHtml(String articleHtml) {
        this.articleHtml = articleHtml;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Integer getHits() {
        return hits;
    }

    public void setHits(Integer hits) {
        this.hits = hits;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getVisible() {
        return visible;
    }

    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    public String getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(String articleCode) {
        this.articleCode = articleCode;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public String getVisibleStr() {
        return visibleStr;
    }

    public void setVisibleStr(String visibleStr) {
        this.visibleStr = visibleStr;
    }
}
