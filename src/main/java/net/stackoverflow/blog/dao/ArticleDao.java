package net.stackoverflow.blog.dao;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.entity.Article;
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

    List<Article> selectByPage(Page page);

    List<Article> selectByCondition(Map<String, Object> searchMap);

    Article selectById(String id);

    Article selectByUrl(String url);

    int insert(Article article);

    int batchInsert(List<Article> list);

    int delete(String id);

    int batchDelete(List<String> list);

    int update(Article article);

    int batchUpdate(List<Article> list);

}
