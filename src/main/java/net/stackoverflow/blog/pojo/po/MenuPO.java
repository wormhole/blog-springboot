package net.stackoverflow.blog.pojo.po;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 菜单实体类
 *
 * @author 凉衫薄
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuPO implements Serializable {

    private String id;
    private String name;
    private String url;
    private Integer deleteAble;
    private Date date;

}
