package com.lt.manager.service.impl.news;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.lt.enums.news.NewsArticleEnum;
import com.lt.manager.bean.brand.BrandPage;
import com.lt.manager.bean.news.*;
import com.lt.manager.dao.brand.BrandDao;
import com.lt.manager.dao.news.NewArticleInfoDao;
import com.lt.manager.dao.news.NewsArticleBrandDao;
import com.lt.manager.dao.news.NewsArticleMainDao;
import com.lt.manager.service.news.NewsArticleMainService;
import com.lt.model.news.*;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.StringTools;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 项目名称：lt-manager
 * 类名称：NewsArticleMainServiceImpl
 * 类描述：  文章评论处理类
 * 创建人：yuanxin
 * 创建时间：2017年2月7日 上午9:52:31
 */
@Service
public class NewsArticleMainServiceImpl implements NewsArticleMainService {


    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private NewsArticleMainDao newMainDao;
    @Autowired
    private NewArticleInfoDao newArticleInfoDao;
    @Autowired
    private NewsArticleBrandDao newsArticleBrandDao;
    @Autowired
    private BrandDao brandDao;

    ExecutorService service = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());


    @Override
    public Page<NewsArticleVo> qryNewsInfoPage(NewsArticleVo newsArticleVo) throws LTException {
        List<NewsArticleVo> list = newMainDao.qryNewsPageList(newsArticleVo);
        if (StringTools.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {

                List<NewsArticleBrand> brandList = newsArticleBrandDao.findBrandNameByNewsArticleId(list.get(i).getNewsId() + "");
                StringBuilder name = new StringBuilder();
                StringBuilder creater = new StringBuilder();
                for (int j = 0; j < brandList.size(); j++) {
                    if (j != 0) {
                        name.append(",");
                        creater.append(",");
                    }
                    name.append(brandList.get(j).getBrandName());
                    creater.append(brandList.get(j).getCreater());
                }
                list.get(i).setBrandName(name.toString());
                list.get(i).setCreater(creater.toString());
            }
        }
        logger.info("----------list={}", JSONObject.toJSON(list));
        Page<NewsArticleVo> page = new Page<NewsArticleVo>();
        page.setPageNum(newsArticleVo.getPage());
        page.setPageSize(newsArticleVo.getRows());
        page.addAll(list);
        page.setTotal(newMainDao.qryNewsPageListCount(newsArticleVo));
        return page;
    }

    @Override
    public NewsArticleDetail qryNewsDetailById(Integer newsArticleId) throws LTException {
        NewsArticleDetail detail = newMainDao.qryNewsDetailById(newsArticleId);
        List<NewsArticleBrand> brandList = newsArticleBrandDao.findBrandNameByNewsArticleId(newsArticleId + "");
        StringBuilder brandIds = new StringBuilder();
        StringBuilder creater = new StringBuilder();
        for (int j = 0; j < brandList.size(); j++) {
            if (j != 0) {
                brandIds.append(",");
                creater.append(",");
            }
            brandIds.append(brandList.get(j).getBrandId());
            creater.append(brandList.get(j).getCreater());
        }
        detail.setBrandId(brandIds.toString());
        detail.setCreater(creater.toString());
        return detail;
    }

    @Override
    @Transactional(rollbackFor = LTException.class, propagation = Propagation.REQUIRED)
    public void addNewsArticle(String plateIds, NewsArticleDetail articleDetail) throws LTException {
        try {
            newMainDao.insertNewsArticle(articleDetail);

            List<NewsPlateRelation> plateList = new ArrayList<NewsPlateRelation>();
            if (plateIds != null && !plateIds.equals("")) {
                NewsPlateRelation newsPlateRelation = null;
                if (plateIds.contains(",")) {
                    for (String plateId : plateIds.split(",")) {
                        newsPlateRelation = new NewsPlateRelation();
                        newsPlateRelation.setNewsId(articleDetail.getNewsArticleId());
                        newsPlateRelation.setPlateId(Integer.parseInt(plateId));
                        plateList.add(newsPlateRelation);
                    }
                } else {
                    newsPlateRelation = new NewsPlateRelation();
                    newsPlateRelation.setNewsId(articleDetail.getNewsArticleId());
                    newsPlateRelation.setPlateId(Integer.parseInt(plateIds));
                    plateList.add(newsPlateRelation);
                }
            }

            NewsActInfo actInfo = new NewsActInfo(0);
            actInfo.setTargetId(articleDetail.getNewsArticleId());
            actInfo.setInitLikeCount(articleDetail.getInitLikeCount() == null ? 0 : articleDetail.getInitLikeCount());
            actInfo.setInitReadCount(articleDetail.getInitReadCount() == null ? 0 : articleDetail.getInitReadCount());
            newArticleInfoDao.insertArticleInfo(actInfo);

            if (CollectionUtils.isNotEmpty(plateList)) {
                newArticleInfoDao.insetPlateRalation(plateList);
            }

            //保存策略品牌发布人关联关系
            saveNewsArticleBrand(articleDetail);
        } catch (Exception e) {
            e.printStackTrace();
            throw new LTException(LTResponseCode.US00001);
        }


    }

    @Override
    @Transactional(rollbackFor = LTException.class)
    public void deleteNewsArticle(String plateIds) throws LTException {
        if (plateIds == null || plateIds.equals("")) {
            throw new LTException(LTResponseCode.FU00003);
        } else {
            try {
                List<String> ids = new ArrayList<String>();
                if (plateIds.contains(",")) {
                    for (String id : plateIds.split(",")) {
                        ids.add(id);
                    }
                } else {
                    ids.add(plateIds);
                }

                newMainDao.deleteNewsArticle(ids);
                newArticleInfoDao.deletePlateRelation(ids);
                newsArticleBrandDao.deleteNewsArticleBrandByNewsArticleId(ids);
            } catch (Exception e) {
                e.printStackTrace();
                throw new LTException(LTResponseCode.US00001);
            }

        }

    }

    @Override
    public void topNewsArticle(String plateIds, Integer status) throws LTException {
        if (plateIds == null || plateIds.equals("")) {
            throw new LTException(LTResponseCode.FU00003);
        } else {
            List<String> ids = new ArrayList<String>();
            if (plateIds.contains(",")) {
                for (String id : plateIds.split(",")) {
                    ids.add(id);
                }
            } else {
                ids.add(plateIds);
            }

            int len = ids.size();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("list", ids);
            map.put("status", status);
            int result = newMainDao.topNewsArticle(map);

            if (len != result) {
                throw new LTException(LTResponseCode.FU00000);
            }
        }

    }

    @Override
    public void checkedNewsArticle(String plateIds, String status) throws LTException {
        if (plateIds == null || plateIds.equals("")) {
            throw new LTException(LTResponseCode.FU00003);
        } else {
            List<String> ids = new ArrayList<String>();
            if (plateIds.contains(",")) {
                for (String id : plateIds.split(",")) {
                    ids.add(id);
                }
            } else {
                ids.add(plateIds);
            }

            int len = ids.size();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("idList", ids);
            map.put("status", status);

            int result = 0;
            if (status.equals(NewsArticleEnum.NEWS_STATUS_SHOW.getValue().toString())) {
                result = newMainDao.checkedNewsArticleShow(map);
            } else {
                result = newMainDao.checkedNewsArticle(map);
            }

            if (len != result) {
                throw new LTException(LTResponseCode.FU00000);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = LTException.class)
    public void updateNewsArticle(String plateIds, NewsArticleDetail articleDetail) throws LTException {
        try {
            newMainDao.updateNewsArticle(articleDetail);

            List<NewsPlateRelation> plateList = new ArrayList<NewsPlateRelation>();
            if (plateIds != null && !plateIds.equals("")) {
                NewsPlateRelation newsPlateRelation = null;
                if (plateIds.contains(",")) {
                    for (String plateId : plateIds.split(",")) {
                        newsPlateRelation = new NewsPlateRelation();
                        newsPlateRelation.setNewsId(articleDetail.getNewsArticleId());
                        newsPlateRelation.setPlateId(Integer.parseInt(plateId));
                        plateList.add(newsPlateRelation);
                    }
                } else {
                    newsPlateRelation = new NewsPlateRelation();
                    newsPlateRelation.setNewsId(articleDetail.getNewsArticleId());
                    newsPlateRelation.setPlateId(Integer.parseInt(plateIds));
                    plateList.add(newsPlateRelation);
                }
            }

            List<String> list = new ArrayList<String>();
            list.add(articleDetail.getNewsArticleId().toString());
            newArticleInfoDao.deletePlateRelation(list);
            if (CollectionUtils.isNotEmpty(plateList)) {
                newArticleInfoDao.insetPlateRalation(plateList);
            }

            NewsActInfo actInfo = new NewsActInfo();
            actInfo.setTargetId(articleDetail.getNewsArticleId());
            actInfo.setInitLikeCount(articleDetail.getInitLikeCount());
            actInfo.setInitReadCount(articleDetail.getInitReadCount());
            newArticleInfoDao.addNewsActInfo(actInfo);


            //删除策略品牌发布人关联关系
            newsArticleBrandDao.deleteNewsArticleBrandByNewsArticleId(list);

            //保存策略品牌发布人关联关系
            saveNewsArticleBrand(articleDetail);

        } catch (Exception e) {
            e.printStackTrace();
            throw new LTException(LTResponseCode.US00001);
        }


    }

    @Override
    public Page<NewsArticleCmtReplyVo> qryNewsInfoCmtRlyPage(NewsArticleVo newsArticleVo) throws LTException {
        Page<NewsArticleCmtReplyVo> page = new Page<NewsArticleCmtReplyVo>();
        page.setPageNum(newsArticleVo.getPage());
        page.setPageSize(newsArticleVo.getRows());
        page.addAll(newMainDao.qryNewsCmtRlyPageList(newsArticleVo));
        page.setTotal(newMainDao.qryNewsPageListCount(newsArticleVo));
        return page;
    }

    @Override
    public List<NewsArticleCmtLikeRlyCount> qryCmtLikeRlyCountByDate(Date date) throws LTException {
        return newMainDao.qryRlyCmtLikeCount(DateTools.formatDate(date, DateTools.FORMAT_LONG));
    }

    @Override
    public List<NewsUserComment> qryNewsComment(String newsId, Integer status) throws LTException {
        return newMainDao.qryNewsCommentByNewsId(newsId, status);
    }

    @Override
    public List<NewsUserCommentReply> qryNewsCommentReply(String commentId, Integer status) throws LTException {
        return newMainDao.qryNewsCommentReplyByComment(commentId, status);
    }

    @Override
    public void addNewsComment(NewsComment newsComment) throws LTException {
        NewsArticleDetail newsArticleDetail = qryNewsDetailById(newsComment.getNewsId());
        newsComment.setNewsName(newsArticleDetail.getTitle());
        newArticleInfoDao.addComment(newsComment);

        NewsActInfoLog actInfoLog = new NewsActInfoLog();
        actInfoLog.setLogType(4);
        actInfoLog.setNewsArticleId(newsComment.getNewsId());
        actInfoLog.setUserId(newsComment.getUserId());

        newArticleInfoDao.addNewsActLog(actInfoLog);

        NewsActInfo actInfo = new NewsActInfo();
        actInfo.setRealCommentCount(1);
        actInfo.setTargetId(newsComment.getNewsId());

        newArticleInfoDao.addNewsActInfo(actInfo);
    }


    @Override
    public void addNewsReply(NewsCmtReply newsCmtReply) throws LTException {
        // TODO Auto-generated method stub
        NewsArticleDetail newsArticleDetail = qryNewsDetailById(newsCmtReply.getNewsId());
        newsCmtReply.setNewsName(newsArticleDetail.getTitle());
        newArticleInfoDao.addNewCmtReply(newsCmtReply);

        NewsActInfoLog actInfoLog = new NewsActInfoLog();
        actInfoLog.setLogType(5);
        actInfoLog.setNewsArticleId(newsCmtReply.getNewsId());
        actInfoLog.setUserId(newsCmtReply.getReplyUserId());

        newArticleInfoDao.addNewsActLog(actInfoLog);

        NewsActInfo actInfo = new NewsActInfo();
        actInfo.setRealReplyCount(1);
        actInfo.setTargetId(newsCmtReply.getNewsId());

        newArticleInfoDao.addNewsActInfo(actInfo);
    }

    @Override
    public void checkNewsComment(String commentId, String status) throws LTException {
        newArticleInfoDao.checkNewsComment(commentId, status);
    }

    @Override
    public void checkNewsReply(String commentId, String status) throws LTException {
        newArticleInfoDao.checkNewsReply(commentId, status);
    }

    @Override
    public void updateNewsReply(NewsCmtReply newsCmtReply) throws LTException {
        newArticleInfoDao.updateNewsReply(newsCmtReply);
    }

    @Override
    public void updateNewsComment(NewsComment newsComment) throws LTException {
        newArticleInfoDao.updateNewsComment(newsComment);
    }

    @Override
    public void chkCommentReply(String commentId, String replyId, String status) throws LTException {
        try {
            Integer statu = Integer.parseInt(status);

            List<String> content = new ArrayList<String>();
            List<String> repliys = new ArrayList<String>();

            if (commentId != null && !commentId.equals("")) {
                for (String comment : commentId.split(",")) {
                    content.add(comment);
                }

                newArticleInfoDao.checkCommentMutil(content, statu);
            }

            if (replyId != null && !replyId.equals("")) {
                for (String comment : replyId.split(",")) {
                    repliys.add(comment);
                }

                newArticleInfoDao.checkReplyMutil(repliys, statu);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            throw new LTException(LTResponseCode.FU00000);
        }

    }

    /**
     * 保存策略 品牌 发布人关联关系
     *
     * @param articleDetail
     */
    private void saveNewsArticleBrand(NewsArticleDetail articleDetail) {
        if (articleDetail.getBrandId().contains(",")) {
            String[] strs = articleDetail.getBrandId().split(",");
            String[] creaters = articleDetail.getCreater().split(",");
            for (int i = 0; i < strs.length; i++) {
                String brandId = strs[i];
                String creater = creaters[i];
                saveNewsArticleBrand(brandId, creater, articleDetail.getNewsArticleId() + "");
            }
        } else {
            saveNewsArticleBrand(articleDetail.getBrandId(), articleDetail.getCreater(), articleDetail.getNewsArticleId() + "");
        }
    }

    /**
     * 保存策略 品牌 发布人关联关系
     *
     * @param brandId
     * @param creater
     * @param newsArticleId
     */
    private void saveNewsArticleBrand(String brandId, String creater, String newsArticleId) {
        NewsArticleBrand brand = new NewsArticleBrand();
        brand.setBrandId(brandId);
        brand.setBrandName(brandDao.findBrandNameByBrandId(brandId));
        brand.setCreater(creater);
        brand.setNewsArticleId(newsArticleId);
        newsArticleBrandDao.saveNewsArticleBrand(brand);
    }

    @Override
    public void init() {
        service.execute(new Runnable() {
            @Override
            public void run() {
                logger.info(Thread.currentThread().getName()+"初始化品牌文章关联关系线程正处理");
                List<NewsArticleVo> newsList = newMainDao.qryNewsPageList(null);
                logger.info(Thread.currentThread().getName()+"---------newsList : {}", JSONObject.toJSON(newsList));
                List<BrandPage> list = brandDao.getBrandListPage(null);
                logger.info(Thread.currentThread().getName()+"---------list : {}", JSONObject.toJSON(list));

                for (int i = 0; i < list.size(); i++) {
                    BrandPage brandPage = list.get(i);
                    for (int j = 0; j < newsList.size(); j++) {
                        NewsArticleVo newsArticle = newsList.get(j);
                        saveNewsArticleBrand(brandPage.getBrandId(), "其他", newsArticle.getNewsId() + "");
                    }

                }
                logger.info(Thread.currentThread().getName()+"初始化品牌文章关联关系线程结束");
            }
        });

    }
}
