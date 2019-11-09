package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.dao.ArticleDao;
import net.stackoverflow.blog.dao.CommentDao;
import net.stackoverflow.blog.pojo.entity.Article;
import net.stackoverflow.blog.pojo.entity.Comment;
import net.stackoverflow.blog.util.RedisCacheUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * 分页查询
     *
     * @param page 分页参数
     * @return 返回结果集
     */
    @Override
    public List<Article> selectByPage(Page page) {
        return articleDao.selectByPage(page);
    }

    /**
     * 条件查询
     *
     * @param searchMap 查询参数
     * @return 返回结果集
     */
    @Override
    public List<Article> selectByCondition(Map<String, Object> searchMap) {
        return articleDao.selectByCondition(searchMap);
    }

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 返回结果
     */
    @Override
    public Article selectById(String id) {
        //先查询缓存，如果不为空，则直接返回，如果为空则查询数据库
        Article article = (Article) RedisCacheUtils.get("article:" + id);
        if (article != null) {
            return article;
        } else {
            article = articleDao.selectById(id);
            //如果查询数据库不为空，则设置缓存
            if (article != null) {
                RedisCacheUtils.set("article:" + id, article, 1800);
                RedisCacheUtils.set("article:" + article.getUrl(), article, 1800);
            }
            return article;
        }
    }

    /**
     * 根据url查询
     *
     * @param url 需要查询的url
     * @return 返回结果
     */
    @Override
    public Article selectByUrl(String url) {
        //先查询缓存，如果不为空，则直接返回，如果为空则查询数据库
        Article article = (Article) RedisCacheUtils.get("article:" + url);
        if (article != null) {
            return article;
        } else {
            article = articleDao.selectByUrl(url);
            //如果查询数据库不为空，则设置缓存
            if (article != null) {
                RedisCacheUtils.set("article:" + article.getId(), article, 1800);
                RedisCacheUtils.set("article:" + url, article, 1800);
            }
            return article;
        }
    }

    /**
     * 新增文章
     *
     * @param article 新增的文章
     * @return 返回新增文章实体对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Article insert(Article article) {
        articleDao.insert(article);
        RedisCacheUtils.set("article:" + article.getId(), article, 1800);
        RedisCacheUtils.set("article:" + article.getUrl(), article, 1800);
        return article;
    }

    /**
     * 批量新增
     *
     * @param articles 新增的文章列表
     * @return 返回新增的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<Article> articles) {
        return articleDao.batchInsert(articles);
    }

    /**
     * 根据id删除
     *
     * @param id 需要删除的文章id
     * @return 返回被删除的文章
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Article delete(String id) {
        Article article = articleDao.selectById(id);

        //查询所有评论
        List<Comment> comments = commentDao.selectByCondition(new HashMap<String, Object>(16) {{
            put("articleId", id);
        }});

        //删除文章所有评论
        if (comments != null && comments.size() != 0) {
            List<String> ids = new ArrayList<>();
            for (Comment comment : comments) {
                ids.add(comment.getId());
                RedisCacheUtils.del("comment:" + comment.getId());
            }
            commentDao.batchDeleteById(ids);
        }

        //删除文章及缓存
        articleDao.delete(id);
        if (article != null) {
            RedisCacheUtils.del("article:" + article.getUrl());
            RedisCacheUtils.del("article:" + id);
        }
        return article;
    }

    /**
     * 根据主键批量删除文章
     *
     * @param ids 主键列表
     * @return 返回被删除的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(List<String> ids) {
        for (String id : ids) {
            //查询评论并删除
            List<Comment> comments = commentDao.selectByCondition(new HashMap<String, Object>(16) {{
                put("articleId", id);
            }});

            if (comments != null && comments.size() != 0) {
                List<String> commentIds = new ArrayList<>();
                for (Comment comment : comments) {
                    commentIds.add(comment.getId());
                    RedisCacheUtils.del("comment:" + comment.getId());
                }
                commentDao.batchDeleteById(commentIds);
            }

            //清楚缓存
            Article articlePO = articleDao.selectById(id);
            if (articlePO != null) {
                RedisCacheUtils.del("article:" + articlePO.getUrl());
                RedisCacheUtils.del("article:" + id);
            }
        }
        return articleDao.batchDelete(ids);
    }

    /**
     * 更新文章
     *
     * @param article 被更新的文章实体对象
     * @return 返回更新后的实体对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Article update(Article article) {
        //删除之前的缓存
        RedisCacheUtils.del("article:" + articleDao.selectById(article.getId()).getUrl());
        articleDao.update(article);
        //重新设置缓存
        Article newArticle = articleDao.selectById(article.getId());
        RedisCacheUtils.set("article:" + newArticle.getId(), newArticle, 1800);
        RedisCacheUtils.set("article:" + newArticle.getUrl(), newArticle, 1800);
        return newArticle;
    }

    /**
     * 批量更新文章
     *
     * @param articles 被更新的文章列表
     * @return 返回更新的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<Article> articles) {
        for (Article article : articles) {
            RedisCacheUtils.del("article:" + articleDao.selectById(article.getId()).getUrl());
            RedisCacheUtils.del("article:" + article.getId());
        }
        return articleDao.batchUpdate(articles);
    }

}
