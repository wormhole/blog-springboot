package net.stackoverflow.blog.pojo.po;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 评论实体类
 *
 * @author 凉衫薄
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentPO implements Serializable {

    private String id;
    private String nickname;
    private String email;
    private String website;
    private String content;
    private String articleId;
    private Date date;
    private String replyTo;
    private Integer review;

}
