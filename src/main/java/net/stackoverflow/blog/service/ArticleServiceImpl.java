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
    @Transactional(rollbackFor = Exception.class)
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
    @Transactional(rollbackFor = Exception.class)
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
    @Transactional(rollbackFor = Exception.class)
    public Article selectById(String id) {
        //先查询缓存，如果不为空，则直接返回，如果为空则查询数据库
        Article articlePO = (Article) RedisCacheUtils.get("article:" + id);
        if (articlePO != null) {
            return articlePO;
        } else {
            articlePO = articleDao.selectById(id);
            //如果查询数据库不为空，则设置缓存
            if (articlePO != null) {
                RedisCacheUtils.set("article:" + id, articlePO);
                RedisCacheUtils.set("article:" + articlePO.getUrl(), articlePO);
            }
            return articlePO;
        }
    }

    /**
     * 根据url查询
     *
     * @param url 需要查询的url
     * @return 返回结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Article selectByUrl(String url) {
        //先查询缓存，如果不为空，则直接返回，如果为空则查询数据库
        Article articlePO = (Article) RedisCacheUtils.get("article:" + url);
        if (articlePO != null) {
            return articlePO;
        } else {
            articlePO = articleDao.selectByUrl(url);
            //如果查询数据库不为空，则设置缓存
            if (articlePO != null) {
                RedisCacheUtils.set("article:" + articlePO.getId(), articlePO);
                RedisCacheUtils.set("article:" + url, articlePO);
            }
            return articlePO;
        }
    }

    /**
     * 新增文章
     *
     * @param articlePO 新增的文章
     * @return 返回新增文章po对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Article insert(Article articlePO) {
        articleDao.insert(articlePO);
        RedisCacheUtils.set("article:" + articlePO.getId(), articlePO);
        RedisCacheUtils.set("article:" + articlePO.getUrl(), articlePO);
        return articlePO;
    }

    /**
     * 批量新增
     *
     * @param articlePOs 新增的文章列表
     * @return 返回新增的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<Article> articlePOs) {
        return articleDao.batchInsert(articlePOs);
    }

    /**
     * 根据id删除
     *
     * @param id 需要删除的文章id
     * @return 返回被删除的文章
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Article deleteById(String id) {
        Article articlePO = articleDao.selectById(id);

        //查询所有评论
        List<Comment> commentPOs = commentDao.selectByCondition(new HashMap<String, Object>() {{
            put("articleId", id);
        }});

        //删除文章所有评论
        if (commentPOs != null && commentPOs.size() != 0) {
            List<String> ids = new ArrayList<>();
            for (Comment commentPO : commentPOs) {
                ids.add(commentPO.getId());
                RedisCacheUtils.del("comment:" + commentPO.getId());
            }
            commentDao.batchDeleteById(ids);
        }

        //删除文章及缓存
        articleDao.deleteById(id);
        if (articlePO != null) {
            RedisCacheUtils.del("article:" + articlePO.getUrl());
            RedisCacheUtils.del("article:" + id);
        }
        return articlePO;
    }

    /**
     * 根据主键批量删除文章
     *
     * @param ids 主键列表
     * @return 返回被删除的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteById(List<String> ids) {
        for (String id : ids) {
            //查询评论并删除
            List<Comment> commentPOs = commentDao.selectByCondition(new HashMap<String, Object>() {{
                put("articleId", id);
            }});

            if (commentPOs != null && commentPOs.size() != 0) {
                List<String> commentIds = new ArrayList<>();
                for (Comment commentPO : commentPOs) {
                    commentIds.add(commentPO.getId());
                    RedisCacheUtils.del("comment:" + commentPO.getId());
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
        return articleDao.batchDeleteById(ids);
    }

    /**
     * 更新文章
     *
     * @param articlePO 被更新的文章PO
     * @return 返回更新后的po对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Article update(Article articlePO) {
        //删除之前的缓存
        RedisCacheUtils.del("article:" + articleDao.selectById(articlePO.getId()).getUrl());
        articleDao.update(articlePO);
        //重新设置缓存
        Article newArticlePO = articleDao.selectById(articlePO.getId());
        RedisCacheUtils.set("article:" + newArticlePO.getId(), newArticlePO);
        RedisCacheUtils.set("article:" + newArticlePO.getUrl(), newArticlePO);
        return newArticlePO;
    }

    /**
     * 批量更新文章
     *
     * @param articlePOs 被更新的文章列表
     * @return 返回更新的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<Article> articlePOs) {
        for (Article articlePO : articlePOs) {
            RedisCacheUtils.del("article:" + articleDao.selectById(articlePO.getId()).getUrl());
            RedisCacheUtils.del("article:" + articlePO.getId());
        }
        return articleDao.batchUpdate(articlePOs);
    }

}
