package com.lt.manager.service.impl.brand;

import com.alibaba.dubbo.common.utils.NamedThreadFactory;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.lt.manager.bean.brand.BrandBean;
import com.lt.manager.bean.brand.BrandPage;
import com.lt.manager.bean.brand.BrandVo;
import com.lt.manager.bean.news.NewsArticleVo;
import com.lt.manager.dao.brand.BrandDao;
import com.lt.manager.dao.brand.BrandPayDao;
import com.lt.manager.dao.news.NewsArticleBrandDao;
import com.lt.manager.dao.news.NewsArticleMainDao;
import com.lt.manager.dao.user.ChannelDao;
import com.lt.manager.service.brand.IBrandService;
import com.lt.model.brand.BrandInfo;
import com.lt.model.brand.BrandPayInfo;
import com.lt.model.news.NewsArticleBrand;
import com.lt.model.user.charge.ChargeChannelInfo;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Description: 品牌管理
 * Created by yanzhenyu on 2017/8/25.
 */
@Service
public class BrandServiceImpl implements IBrandService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BrandDao brandDao;

    @Autowired
    private BrandPayDao brandPayDao;

    @Autowired
    private ChannelDao channelDao;

    @Autowired
    private NewsArticleBrandDao newsArticleBrandDao;

    @Autowired
    private NewsArticleMainDao newsArticleMainDao;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    ExecutorService service = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());

    @Override
    public Page<BrandPage> getBrandListPage(BrandVo brandVo) {
        Page<BrandPage> page = new Page<>();
        page.setPageNum(brandVo.getPage());
        page.setPageSize(brandVo.getRows());

        List<BrandPage> brandPages = brandDao.getBrandListPage(brandVo);
        setPayChannelRelation(brandPages);

        page.addAll(brandPages);
        page.setTotal(brandDao.getBrandCount(brandVo));
        return page;
    }

    @Override
    public List<BrandPage> getBrandList(BrandVo brandVo) {
        brandVo.setLimit(9999);
        return brandDao.getBrandListPage(brandVo);
    }

    @Override
    public Integer getBrandCount(BrandVo brandVo) {
        return brandDao.getBrandCount(brandVo);
    }

    @Override
    @Transactional(rollbackFor = LTException.class)
    public void addBrand(BrandVo brand) {
        //防止品牌重复添加
    	BrandVo tmp = new BrandVo();
    	tmp.setBrandCode(brand.getBrandCode());
        if (brandDao.getBrandCount(tmp) > 0) {
            throw new LTException(LTResponseCode.MA00027);
        }

        //添加品牌
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        brand.setBrandId(sdf.format(new Date()) + StringTools.generateString(3));
        brandDao.addBrand(brand);

        try {
            //先清空之前的关系
            brandPayDao.deleteByBrandId(brand.getBrandId());

            //判断是否设置支付渠道
            if (StringTools.isEmpty(brand.getPayChannels())) {
                return;
            }

            //建立品牌-支付渠道对应关系
            List<BrandPayInfo> list = new ArrayList<>();
            buildBrandPayRelation(brand, list);
            brandPayDao.insertList(list);
            //保存品牌文章关联关系
            saveNewsArticleBrand(brand.getBrandId());
        } catch (Exception e) {
            throw new LTException(LTResponseCode.MA00031);
        }
    }

    @Override
    @Transactional(rollbackFor =  LTException.class)
    public void deleteBrand(String brandId) {
        BrandVo brand = new BrandVo();
        brand.setBrandId(brandId);
        if (brandDao.getBrandCount(brand) == 0) {
            throw new LTException(LTResponseCode.MA00028);
        }

        if (!channelDao.findChannelByBrandId(brandId).isEmpty()) {
            throw new LTException(LTResponseCode.MA00029);
        }

        try {
            //先清空之前的关系
            brandPayDao.deleteByBrandId(brand.getBrandId());
            brandDao.deleteBrand(brand);

            deleteNewsArticleBrand(brandId);
        } catch (Exception e) {
            throw new LTException(LTResponseCode.MA00031);
        }

    }


    /**
     * 保存品牌文章关联关系
     * @param brandId
     */
    private void saveNewsArticleBrand(final String brandId){
        service.execute(new Runnable() {
            @Override
            public void run() {
                logger.info(Thread.currentThread().getName()+"保存品牌文章关联关系线程正处理");
                List<NewsArticleVo>  newsList = newsArticleMainDao.qryNewsPageList(null);
                for (int j = 0; j < newsList.size(); j++) {
                    NewsArticleVo newsArticle = newsList.get(j);
                    NewsArticleBrand brand = new NewsArticleBrand();
                    brand.setBrandId(brandId);
                    brand.setBrandName(brandDao.findBrandNameByBrandId(brandId));
                    brand.setCreater("其他");
                    brand.setNewsArticleId(newsArticle.getNewsId()+"");
                    newsArticleBrandDao.saveNewsArticleBrand(brand);
                }
                logger.info(Thread.currentThread().getName()+"保存品牌文章关联关系线程处理完成");
            }
        });

    }

    /**
     * 删除品牌文章关联关系
     * @param brandId
     */
    private void deleteNewsArticleBrand(final String brandId){
        newsArticleBrandDao.deleteNewsArticleBrandByBrandId(brandId);
    }

    @Override
    @Transactional(rollbackFor = LTException.class)
    public void updateBrand(BrandVo brand) {
        BrandVo temp = new BrandVo();
        temp.setBrandId(brand.getBrandId());
        if (brandDao.getBrandCount(temp) == 0) {
            throw new LTException(LTResponseCode.MA00028);
        }

        try {
            brandDao.updateBrand(brand);

            //先清空之前的关系
            brandPayDao.deleteByBrandId(brand.getBrandId());
            deleteNewsArticleBrand(brand.getBrandId());
            //判断是否设置支付渠道
            if (StringTools.isEmpty(brand.getPayChannels())) {
                return;
            }

            //建立品牌-支付渠道对应关系
            List<BrandPayInfo> list = new ArrayList<>();

            buildBrandPayRelation(brand, list);

            brandPayDao.insertList(list);
            saveNewsArticleBrand(brand.getBrandId());
        } catch (Exception e) {
            throw new LTException(LTResponseCode.MA00031);
        }
    }

    /**
     * 获取支付渠道，封装入返回对象
     */
    private void setPayChannelRelation(List<BrandPage> brandPages) {
        for (BrandPage brandPage : brandPages) {
            List<ChargeChannelInfo> list = brandDao.findPayChannelByBrandId(brandPage.getBrandId());

            StringBuilder idBuilder = new StringBuilder();
            StringBuilder nameBuilder = new StringBuilder();
            int i = 1;
            for (ChargeChannelInfo info : list) {
                idBuilder.append(info.getChannelId());
                if (i != list.size()){
                    idBuilder.append(',');
                }
                nameBuilder.append(info.getChannelName());
                if (i != list.size()) {
                    nameBuilder.append(',');
                }
                i++;
            }
            brandPage.setPayChannelIds(idBuilder.toString());
            brandPage.setPayChannelNames(nameBuilder.toString());
        }
    }


    /**
     * 建立品牌-支付渠道对应关系
     *
     * @param brand
     * @param list
     */
    private void buildBrandPayRelation(BrandVo brand, List<BrandPayInfo> list) {
        for (String payIdChannel : brand.getPayChannels().split(",")) {
            BrandPayInfo temp = new BrandPayInfo();
            temp.setBrandId(brand.getBrandId());
            temp.setPayId(payIdChannel);
            list.add(temp);
        }
    }

}

