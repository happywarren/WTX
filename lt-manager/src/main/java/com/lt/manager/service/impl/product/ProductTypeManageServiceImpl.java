package com.lt.manager.service.impl.product;


import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.lt.manager.bean.product.ProductParamVO;
import com.lt.manager.dao.product.ProductManageDao;
import com.lt.manager.dao.product.ProductTypeManageDao;
import com.lt.manager.service.product.IProductTypeManageService;
import com.lt.model.product.ProductType;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.model.Response;

/**
 * 商品类型管理实现类
 *
 * @author jingwb
 */
@Service
public class ProductTypeManageServiceImpl implements IProductTypeManageService {

    @Autowired
    private ProductTypeManageDao productTypeManageDao;
    @Autowired
    private ProductManageDao productManageDao;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    @Transactional
    public void addProductType(ProductParamVO param) throws Exception {

        //修改已有类型排序号，均加一
        productTypeManageDao.updateSort();
        //添加类型
        productTypeManageDao.insertProductType(param);
    }

    @Override
    public void editProductType(ProductParamVO param) throws Exception {
        productTypeManageDao.updateProductType(param);
    }

    @Override
    @Transactional
    public void removeProductType(ProductParamVO param) throws Exception {
        //判断该商品类型是否已被商品占用，如果被占用则不允许删除
        Map<String, String> map = new HashMap<String, String>();
        map.put("typeIds", param.getIds());
        Integer count = productManageDao.selectProCountByParam(map);
        if (count > 0) {
            throw new LTException(LTResponseCode.PR00013);
        }
        //将该排序下的排序均-1
        String[] ids = param.getIds().split(",");
        ProductParamVO vo = new ProductParamVO();
        for (String id : ids) {

            vo.setId(id);
            ProductType type = productTypeManageDao.selectProductTypeOne(vo);
            if (type != null) {
                productTypeManageDao.updateSortToL(String.valueOf(type.getSortNum()));
            }
        }
        productTypeManageDao.deleteProductType(param);
    }

    @Override
    public Page<ProductType> queryProductTypePage(ProductParamVO param)
            throws Exception {
        Page<ProductType> page = new Page<ProductType>();
        page.setPageNum(param.getPage());
        page.setPageSize(param.getRows());
        page.addAll(productTypeManageDao.selectProductTypePage(param));
        page.setTotal(productTypeManageDao.selectProductTypeCount(param));
        return page;
    }

    @Override
    public List<ProductType> queryProductType(ProductParamVO param)
            throws Exception {
        param.setLimit(9999);
        return productTypeManageDao.selectProductTypePage(param);
    }

    @Override
    public Integer queryProductTypeCount(ProductParamVO param) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Transactional
    public void editProductTypeSort(ProductParamVO param) throws Exception {
        Map<String, Object> p = new HashMap<String, Object>();
        ProductType productType = productTypeManageDao.selectProductTypeOne(param);

        if (param.getSortNum() == productType.getSortNum()) {
            throw new LTException(LTResponseCode.PR00003);
        }
        //规则：如3挪到1，则更新原sort>=1&&sort<3 的类型排序加一
        //该修改类型排序改为1
        p.put("oldSort", productType.getSortNum());
        p.put("newSort", param.getSortNum());
        if (param.getSortNum() < productType.getSortNum()) {
            productTypeManageDao.updateSortForGTL(p);
        } else {
            productTypeManageDao.updateSortForLTG(p);
        }

        //修改该类型排序
        productTypeManageDao.updateProductType(param);
    }

    @Override
    public void test() throws Exception {
    /*	BoundHashOperations<String, String, String> proTimeCfgRedis = redisTemplate.boundHashOps("testk");
		proTimeCfgRedis.put("123456", "test");
		redisTemplate.expire("testk:*", 5, TimeUnit.SECONDS);*/

        String DEFAULT_TIME_HMS = "HHmm";
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_TIME_HMS);
        String HHmm = format.format(new Date());
        //String HHmmss = DateTools.parseToHMSTime(new Date());
		 /*BoundHashOperations<String, String, Set<Integer>> codeRedis = redisTemplate.boundHashOps("testk"+HHmm);
	     Set<Integer> set = codeRedis.get("testk"+HHmm);
	     if(set == null){
	    	 set = new HashSet<Integer>();	    		 
	    	 set.add(1);
		     set.add(2);
		     set.add(3);	     
		     set.add(4);
		     set.add(5);
		     codeRedis.put("testk"+HHmm, set);
		     redisTemplate.expire("testk"+HHmm, 1, TimeUnit.MINUTES);
		     
	     }else{
	    	 set.add(11);
		     set.add(12);
		     set.add(13);	     
		     set.add(14);
		     set.add(15);
		     codeRedis.put("testk"+HHmm, set);
	     }*/
		/*while(true){
			BoundHashOperations<String, String, String> pp = redisTemplate.boundHashOps("testk");
			System.out.println(pp.get("123456"));
		}*/
		/*int i = productTypeManageDao.test();
		System.out.println("==================i="+i+"");
		if(i == 0){
			throw new LTException("重复执行");
		}
		ProductParamVO param = new ProductParamVO();
		param.setName("test");
		param.setRemark("test");
		productTypeManageDao.insertProductType(param);*/

        if (!setSuccess(redisTemplate, "testk:" + HHmm, "123456")) {
            System.out.println("重复了");
        }
    }

    @SuppressWarnings("unchecked")
    public static boolean setSuccess(RedisTemplate redisTemplate, final String key, final String value) {
        boolean flag = false;
        flag = (boolean) redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                boolean flag = connection.setNX(key.getBytes(), value.getBytes());
                System.out.println("执行结果为：" + flag);
                return flag;
            }

        });
        if (flag) {
            redisTemplate.expire(key, 1, TimeUnit.MINUTES);
        }
        return flag;
    }

    @Override
    @Transactional
    public void test1() throws Exception {
        int i = productTypeManageDao.test1();
        System.out.println("==================i=" + i + "");
        if (i == 0) {
            throw new LTException("重复执行");
        }
    }
}
