package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.dao.ArticleDao;
import net.stackoverflow.blog.dao.CommentDao;
import net.stackoverflow.blog.pojo.entity.Article;
import net.stackoverflow.blog.pojo.entity.Comment;
import net.stackoverflow.blog.util.RedisCacheUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文章服务实现
 *
 * @author 凉衫薄
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private CommentDao commentDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Article> selectByPage(Page page) {
        return articleDao.selectByPage(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Article> selectByCondition(Map<String, Object> searchMap) {
        return articleDao.selectByCondition(searchMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Cacheable(value = "article", key = "'article:'+#id", unless = "#result == null")
    public Article selectById(String id) {
        return articleDao.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Cacheable(value = "article", key = "'article:'+#url", unless = "#result == null")
    public Article selectByUrl(String url) {
        return articleDao.selectByUrl(url);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CachePut(value = "article", key = "'article:'+#result.url", condition = "#result!=null")
    public Article insert(Article article) {
        articleDao.insert(article);
        RedisCacheUtils.set("article:" + article.getId(), article);
        return articleDao.selectById(article.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<Article> list) {
        for (Article article : list) {
            RedisCacheUtils.set("article:" + article.getId(), article);
            RedisCacheUtils.set("article:" + article.getUrl(), article);
        }
        return articleDao.batchInsert(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "article", key = "'article:'+#result.id", condition = "#result!=null", beforeInvocation = false)
    public Article deleteById(String id) {
        Article article = articleDao.selectById(id);

        List<Comment> commentList = commentDao.selectByCondition(new HashMap<String, Object>() {{
            put("articleId", id);
        }});

        if (commentList.size() != 0) {
            List<String> ids = new ArrayList<String>();
            for (Comment comment : commentList) {
                ids.add(comment.getId());
            }
            commentDao.batchDeleteById(ids);
        }

        articleDao.deleteById(id);
        RedisCacheUtils.del("article:" + article.getUrl());
        return article;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteById(List<String> list) {
        for (String id : list) {
            List<Comment> commentList = commentDao.selectByCondition(new HashMap<String, Object>() {{
                put("articleId", id);
            }});

            if (commentList.size() != 0) {
                List<String> ids = new ArrayList<String>();
                for (Comment comment : commentList) {
                    ids.add(comment.getId());
                }
                commentDao.batchDeleteById(ids);
            }

            Article article = articleDao.selectById(id);
            RedisCacheUtils.del("article:" + article.getUrl());
            RedisCacheUtils.del("article:" + id);
        }
        return articleDao.batchDeleteById(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CachePut(value = "article", key = "'article:'+#result.url", condition = "#result!=null")
    public Article update(Article article) {
        articleDao.update(article);
        RedisCacheUtils.set("article:" + article.getId(), article);
        return articleDao.selectById(article.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<Article> list) {
        for (Article article : list) {
            RedisCacheUtils.set("article:" + article.getId(), article);
            RedisCacheUtils.set("article:" + article.getUrl(), article);
        }
        return articleDao.batchUpdate(list);
    }

}
