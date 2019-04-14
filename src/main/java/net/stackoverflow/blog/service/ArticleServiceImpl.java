package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.dao.ArticleDao;
import net.stackoverflow.blog.dao.CommentDao;
import net.stackoverflow.blog.pojo.po.ArticlePO;
import net.stackoverflow.blog.pojo.po.CommentPO;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ArticlePO> selectByPage(Page page) {
        return articleDao.selectByPage(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ArticlePO> selectByCondition(Map<String, Object> searchMap) {
        return articleDao.selectByCondition(searchMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ArticlePO selectById(String id) {
        ArticlePO articlePO = (ArticlePO) RedisCacheUtils.get("article:" + id);
        if (articlePO != null) {
            return articlePO;
        } else {
            articlePO = articleDao.selectById(id);
            if (articlePO != null) {
                RedisCacheUtils.set("article:" + id, articlePO);
                RedisCacheUtils.set("article:" + articlePO.getUrl(), articlePO);
            }
            return articlePO;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ArticlePO selectByUrl(String url) {
        ArticlePO articlePO = (ArticlePO) RedisCacheUtils.get("article:" + url);
        if (articlePO != null) {
            return articlePO;
        } else {
            articlePO = articleDao.selectById(url);
            if (articlePO != null) {
                RedisCacheUtils.set("article:" + articlePO.getId(), articlePO);
                RedisCacheUtils.set("article:" + url, articlePO);
            }
            return articlePO;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ArticlePO insert(ArticlePO article) {
        articleDao.insert(article);
        RedisCacheUtils.set("article:" + article.getId(), article);
        RedisCacheUtils.set("article:" + article.getUrl(), article);
        return article;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<ArticlePO> articles) {
        return articleDao.batchInsert(articles);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ArticlePO deleteById(String id) {
        ArticlePO article = articleDao.selectById(id);

        List<CommentPO> commentList = commentDao.selectByCondition(new HashMap<String, Object>() {{
            put("articleId", id);
        }});

        if (commentList.size() != 0) {
            List<String> ids = new ArrayList<String>();
            for (CommentPO comment : commentList) {
                ids.add(comment.getId());
            }
            commentDao.batchDeleteById(ids);
        }

        articleDao.deleteById(id);
        if (article != null) {
            RedisCacheUtils.del("article:" + article.getUrl());
            RedisCacheUtils.del("article:" + id);
        }
        return article;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteById(List<String> ids) {
        for (String id : ids) {
            List<CommentPO> commentList = commentDao.selectByCondition(new HashMap<String, Object>() {{
                put("articleId", id);
            }});

            if (commentList.size() != 0) {
                List<String> commentIds = new ArrayList<>();
                for (CommentPO comment : commentList) {
                    commentIds.add(comment.getId());
                }
                commentDao.batchDeleteById(commentIds);
            }

            ArticlePO article = articleDao.selectById(id);
            if (article != null) {
                RedisCacheUtils.del("article:" + article.getUrl());
                RedisCacheUtils.del("article:" + id);
            }
        }
        return articleDao.batchDeleteById(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ArticlePO update(ArticlePO article) {
        RedisCacheUtils.del("article:" + articleDao.selectById(article.getId()).getUrl());
        articleDao.update(article);
        RedisCacheUtils.set("article:" + article.getId(), article);
        RedisCacheUtils.set("article:" + article.getUrl(), article);
        return articleDao.selectById(article.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<ArticlePO> articles) {
        int result = articleDao.batchUpdate(articles);
        for (ArticlePO article : articles) {
            RedisCacheUtils.del("article:" + articleDao.selectById(article.getId()).getUrl());
            RedisCacheUtils.set("article:" + article.getId(), article);
            RedisCacheUtils.set("article:" + article.getUrl(), article);
        }
        return result;
    }

}
