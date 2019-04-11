package net.stackoverflow.blog.dao;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.po.ArticlePO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 文章表DAO
 *
 * @author 凉衫薄
 */
@Mapper
public interface ArticleDao {

    List<ArticlePO> selectByPage(Page page);

    List<ArticlePO> selectByCondition(Map<String, Object> searchMap);

    ArticlePO selectById(String id);

    ArticlePO selectByUrl(String url);

    int insert(ArticlePO article);

    int batchInsert(List<ArticlePO> list);

    int deleteById(String id);

    int batchDeleteById(List<String> list);

    int update(ArticlePO article);

    int batchUpdate(List<ArticlePO> list);

}
