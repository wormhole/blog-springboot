package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.entity.Article;

import java.util.List;
import java.util.Map;

/**
 * 文章服务接口
 *
 * @author 凉衫薄
 */
public interface ArticleService {

    List<Article> selectByPage(Page page);

    List<Article> selectByCondition(Map<String, Object> searchMap);

    Article selectById(String id);

    Article selectByUrl(String url);

    Article insert(Article article);

    int batchInsert(List<Article> list);

    Article deleteById(String id);

    int batchDeleteById(List<String> list);

    Article update(Article article);

    int batchUpdate(List<Article> list);

}
