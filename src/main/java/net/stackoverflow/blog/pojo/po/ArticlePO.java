package net.stackoverflow.blog.pojo.po;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 文章实体类
 *
 * @author 凉衫薄
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArticlePO implements Serializable {

    private String id;
    private String userId;
    private String title;
    private String articleMd;
    private String articleHtml;
    private String categoryId;
    private Date createDate;
    private Date modifyDate;
    private Integer hits;
    private Integer likes;
    private String url;
    private Integer visible;

}
