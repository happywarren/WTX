package com.lt.manager.controller.product;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.lt.api.trade.IOrderApiService;
import com.lt.api.trade.IOrderScoreApiService;
import com.lt.enums.product.ProductTypeEnum;
import com.lt.manager.bean.product.ProductParamVO;
import com.lt.manager.bean.product.ProductParamVO.ProductCode;
import com.lt.manager.bean.product.ProductRiskConfigVO;
import com.lt.manager.bean.sys.Staff;
import com.lt.manager.service.product.IProductManageService;
import com.lt.manager.service.product.IProductRiskConfigManageService;
import com.lt.model.product.Product;
import com.lt.util.LoggerTools;
import com.lt.util.StaffUtil;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.JqueryEasyUIData;
import com.lt.util.utils.OSSObjectSample;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import com.lt.util.utils.speedycloud.SpeedyCloudUtils;
import com.lt.vo.product.ProductVo;

import javolution.util.FastMap;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商品管理控制器
 *
 * @author jingwb
 */
@Controller
@RequestMapping(value = "product")
public class ProductManageController {
    private Logger logger = LoggerTools.getInstance(getClass());

    @Autowired
    private IProductManageService productManageServiceImpl;

    @Autowired
    private IOrderApiService orderServiceImpl;

    @Autowired
    private IOrderScoreApiService orderScoreServiceImpl;

    @Autowired
    private IProductRiskConfigManageService productRiskConfigManageService;

    /**
     * 加载商品信息和时间配置到缓存
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/loadProAndTimeToRedis")
    @ResponseBody
    public String loadProAndTimeToRedis(HttpServletRequest request) {
        Response response = null;
        try {
            //刷新缓存中的商品信息
            productManageServiceImpl.loadProAndTimeToRedis();
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
        } catch (Exception e) {
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("添加商品,刷新缓存异常，e={}", e);
        }
        return response.toJsonString();
    }

    /**
     * 添加商品
     *
     * @param request
     * @param product
     * @return
     */
    @RequestMapping(value = "/addProduct")
    @ResponseBody
    public String addProduct(HttpServletRequest request, ProductParamVO param) {
        Response response = null;
        try {

			/*
			 * 如果是差价合约
			 */
            if (ProductTypeEnum.CONTRACT.getCode().equals(param.getProductTypeCode())) {
                ProductCode productCode=new ProductCode();
                productCode.setProductCode(param.getCode());
                productCode.setExpirationBeginTime("2017-10-23 11:17:49.0");
                productCode.setExpirationEndTime("2117-10-23 11:17:49.0");
                List<ProductCode> codes=new ArrayList<>();
                codes.add(productCode);
                param.setJsonProductCodeList(JSONObject.toJSONString(codes));
            }

            if (StringTools.isEmpty(param.getName()) || StringTools.isEmpty(param.getJsonProductCodeList())
                    || StringTools.isEmpty(param.getShortCode())) {

                return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
            }

            //商品合约和交割日list
            String jsonProductCodeList = param.getJsonProductCodeList();
            List<ProductCode> productCodeList = JSONObject.parseArray(jsonProductCodeList, ProductCode.class);
            if (productCodeList.size() == 0) {
                return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
            }

            for (ProductCode productCode : productCodeList) {
                param.setCode(productCode.getProductCode());//商品code
                param.setExpirationBeginTime(productCode.getExpirationBeginTime());//合约开始时间
                param.setExpirationTime(productCode.getExpirationEndTime());//合约到期时间
                productManageServiceImpl.addProduct(param);
            }

            productManageServiceImpl.loadProAndTimeToRedis();
            //加载时间配置
            orderServiceImpl.callOrderClearTime();
            orderScoreServiceImpl.callOrderClearTime();
            productManageServiceImpl.pushSpreadModifyMsg(param.getSpreadModProductList());
            orderServiceImpl.updateDeferTimeConfig();
            orderScoreServiceImpl.updateDeferTimeConfig();
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("添加商品系统异常，e={}", e);
        }
        return response.toJsonString();
    }


    /**
     * 上传图片
     *
     * @param request
     * @param photoFile
     * @return
     */
    @RequestMapping(value = "/uploadImg")
    @ResponseBody
    public String uploadImg(HttpServletRequest request, MultipartFile photoFile) {
        Response response = null;
        if (photoFile == null) {
            return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
        }
        String iconUrl = updPhoto(photoFile, System.currentTimeMillis() + "");
        logger.info("=========iconUrl={}", iconUrl);
        response = LTResponseCode.getCode(LTResponseCode.SUCCESS, iconUrl);
        return response.toJsonString();
    }


    /**
     * 获取商品相关详情
     *
     * @param request
     * @param param
     * @return
     */
    @RequestMapping(value = "/queryProductDetail")
    @ResponseBody
    public String queryProductDetail(HttpServletRequest request, ProductParamVO param) {
        Response response = null;
        try {
            if (StringTools.isEmpty(param.getIds())) {
                return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
            }
            Map<String, Object> map = productManageServiceImpl.queryProductDetail(param);
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS, map);
        } catch (Exception e) {
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("获取商品相关详情异常，e={}", e);
        }
        return response.toJsonString();
    }

    /**
     * 批量编辑商品
     *
     * @param request
     * @param product
     * @return
     */
    @RequestMapping(value = "/editProduct")
    @ResponseBody
    public String editProduct(HttpServletRequest request, ProductParamVO param) {
        Response response = null;
        try {

            /*
			 * 如果是差价合约
			 */
            if (ProductTypeEnum.CONTRACT.getCode().equals(param.getProductTypeCode())) {
                ProductCode productCode=new ProductCode();
                productCode.setId(Integer.parseInt(param.getIds()));
                productCode.setProductCode(param.getCode());
                productCode.setExpirationBeginTime("2017-10-23 11:17:49.0");
                productCode.setExpirationEndTime("2117-10-23 11:17:49.0");
                List<ProductCode> codes=new ArrayList<>();
                codes.add(productCode);
                param.setJsonProductCodeList(JSONObject.toJSONString(codes));
            }

            if (StringTools.isEmpty(param.getIds()) || StringTools.isEmpty(param.getJsonProductCodeList())
                    || StringTools.isEmpty(param.getShortCode())) {
                return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
            }

            boolean b = productManageServiceImpl.editOrAddProduct(param);
            productManageServiceImpl.loadProAndTimeToRedis();
            if(b){
                //加载时间配置
                orderServiceImpl.callOrderClearTime();
                orderScoreServiceImpl.callOrderClearTime();
            }

            productManageServiceImpl.pushSpreadModifyMsg(param.getSpreadModProductList());
            orderServiceImpl.updateDeferTimeConfig();
            orderScoreServiceImpl.updateDeferTimeConfig();
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
        } catch (Exception e) {
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("编辑商品系统异常，e={}", e);
        }
        return response.toJsonString();
    }

    /**
     * 删除商品
     *
     * @param request
     * @param product
     * @return
     */
    @RequestMapping(value = "/removeProducts")
    @ResponseBody
    public String removeProducts(HttpServletRequest request, ProductParamVO param) {
        //TODO 删除
        Response response = null;
        try {
            if (StringTools.isEmpty(param.getIds())) {
                return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
            }
            productManageServiceImpl.removeProducts(param);
            productManageServiceImpl.loadProAndTimeToRedis();
            orderServiceImpl.updateDeferTimeConfig();
            orderScoreServiceImpl.updateDeferTimeConfig();
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
        } catch (Exception e) {
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("删除商品系统异常，e={}", e);
        }
        return response.toJsonString();
    }

    /**
     * 编辑商品基本信息
     *
     * @param request
     * @param param
     * @return
     */
    @RequestMapping(value = "/editProductInfo")
    @ResponseBody
    public String editProductInfo(HttpServletRequest request, ProductParamVO param) {
        Response response = null;
        try {
            if (StringTools.isEmpty(param.getIds())) {
                return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
            }
            boolean b = productManageServiceImpl.editProductInfo(param);
            productManageServiceImpl.loadProAndTimeToRedis();
            if(b){
                //加载时间配置
                orderServiceImpl.callOrderClearTime();
                orderScoreServiceImpl.callOrderClearTime();
            }
            orderServiceImpl.updateDeferTimeConfig();
            orderScoreServiceImpl.updateDeferTimeConfig();
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
        } catch (Exception e) {
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("编辑商品基本信息系统异常，e={}", e);
        }
        return response.toJsonString();
    }

    /**
     * 修改商品排序
     *
     * @param request
     * @param param
     * @return
     */
    @RequestMapping(value = "/editProductSort")
    @ResponseBody
    public String editProductSort(HttpServletRequest request, ProductParamVO param) {
        Response response = null;
        try {
            if (param.getId() == null || param.getSortNum() == null) {
                return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
            }
            productManageServiceImpl.editProductSort(param);
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
        } catch (Exception e) {
            response = LTResponseCode.getCode(e.getMessage());
        }
        return response.toJsonString();
    }

    /**
     * 商品置顶
     *
     * @param request
     * @param param
     * @return
     */
    @RequestMapping(value = "/topProducts")
    @ResponseBody
    public String topProducts(HttpServletRequest request, ProductParamVO param) {
        Response response = null;
        try {
            if (StringTools.isEmpty(param.getIds())) {
                return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
            }
            productManageServiceImpl.topProducts(param);
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
        } catch (Exception e) {
            response = LTResponseCode.getCode(e.getMessage());
        }
        return response.toJsonString();
    }

    /**
     * 查询商品信息--分页
     *
     * @param request
     * @param param
     * @return
     */
    @RequestMapping(value = "/queryProductPage")
    @ResponseBody
    public String queryProductPage(HttpServletRequest request, ProductParamVO param) {
        Response response = null;
        try {
            Page<Product> page = productManageServiceImpl.queryProductPage(param);
            return JqueryEasyUIData.init(page);
        } catch (Exception e) {
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("查询商品分页系统异常，e={}", e);
        }
        return response.toJsonString();
    }

    /**
     * 查询商品list
     *
     * @param request
     * @param param
     * @return
     */
    @RequestMapping(value = "/queryProductList")
    @ResponseBody
    public String queryProductList(HttpServletRequest request, ProductParamVO param) {
        Response response = null;
        try {
            List<ProductVo> list = productManageServiceImpl.queryProductList(param);
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS, list);
        } catch (Exception e) {
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("查询商品list异常，e={}", e);
        }
        return response.toJsonString();
    }

    /**
     * 查询商品品种编码
     *
     * @param request
     * @param param
     * @return
     */
    @RequestMapping(value = "/queryProShortCode")
    @ResponseBody
    public String queryProShortCode(HttpServletRequest request, ProductParamVO param) {
        Response response = null;
        try {
            List<Product> list = productManageServiceImpl.getProShortCode(new Product());
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS, list);
        } catch (Exception e) {
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("查询商品品种系统异常，e={}", e);
        }
        return response.toJsonString();
    }

    /**
     * 上传图片
     *
     * @param photoFile
     * @param name
     * @return
     */
/*	public String updPhoto(MultipartFile photoFile,String name) {
		try {
			final InputStream input = photoFile.getInputStream();
			final String ex = OSSObjectSample.getFileNameNoEx(photoFile.getOriginalFilename());
			if (null != photoFile && photoFile.getSize() > 0) {
	            System.out.println("正在上传...");
	            String key = DateTools.parseToDefaultDateString(new Date())+"_market"+name+ex;
	            OSSObjectSample.uploadFile(OSSObjectSample.client, OSSObjectSample.bucketName, key, photoFile.getSize(),input);
		        String path = "http://"+OSSObjectSample.bucketName+OSSObjectSample.url+key;
				return path;
			} else {
				logger.error("图片上传失败,头像文件不存在");
				return null;
			}

		} catch (Exception e) {
			logger.error("图片上传失败,Exception = {}", e);
			return null;
		}
	}*/
    public String updPhoto(MultipartFile photoFile, String name) {
        try {
            final InputStream input = photoFile.getInputStream();
            final String ex = OSSObjectSample.getFileNameNoEx(photoFile.getOriginalFilename());
            if (null != photoFile && photoFile.getSize() > 0) {
                String key = DateTools.parseToDefaultDateString(new Date()) + "_market" + name + ex;
                //OSSObjectSample.uploadFile(OSSObjectSample.client, OSSObjectSample.bucketName, key, photoFile.getSize(),input);
                //String path = "http://"+OSSObjectSample.bucketName+OSSObjectSample.url+key;
                Map<String, Object> result = FastMap.newInstance();
                result = SpeedyCloudUtils.getSpeedyCloudUtils().uploadImage(key, input);
                if ("SUCCESS".equals((String) result.get("resultId"))) {
                    return (String) result.get("filePath");
                }
                return null;
            } else {
                logger.error("图片上传失败,头像文件不存在");
                return null;
            }

        } catch (Exception e) {
            logger.error("图片上传失败,Exception = {}", e);
            return null;
        }
    }

    /**
     * 获取美金人民币汇率
     *
     * @return
     */
    @RequestMapping(value = "/getRate")
    @ResponseBody
    public String getRate() {
        Response response = LTResponseCode.getCode(LTResponseCode.SUCCESS, productManageServiceImpl.selectRate("CNY"));
        return response.toJsonString();
    }

    /**
     * 新增商品风险杠杆配置记录
     *
     * @return
     */
    @RequestMapping(value = "/addProductRiskConfigRecord")
    @ResponseBody
    public String addProductRiskConfigRecord(HttpServletRequest request, ProductRiskConfigVO param) {
        Response response = null;
        try {
            if (StringTools.isEmpty(param.getProductId())) {
                return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
            }
            /**获取当前操作人**/
            String userId = "0";
            Staff staff = StaffUtil.getStaff(request);
            if (staff != null && StringTools.isNotEmpty(staff.getId() + "")) {
                userId = staff.getId() + "";
            }
            param.setCreateUserId(userId);
            param.setModifyUserId(userId);
            productRiskConfigManageService.insert(param);
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS, null);
        } catch (Exception e) {
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("新增商品风险杠杆配置记录异常，e={}", e);
        }
        return response.toJsonString();
    }

    /**
     * 修改商品风险杠杆配置记录
     *
     * @return
     */
    @RequestMapping(value = "/updateProductRiskConfigRecord")
    @ResponseBody
    public String updateProductRiskConfigRecord(HttpServletRequest request, ProductRiskConfigVO param) {
        Response response = null;
        try {
            if (StringTools.isEmpty(param.getId())) {
                return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
            }
            /**获取当前操作人**/
            String userId = "0";
            Staff staff = StaffUtil.getStaff(request);
            if (staff != null && StringTools.isNotEmpty(staff.getId() + "")) {
                userId = staff.getId() + "";
            }
            param.setModifyUserId(userId);
            productRiskConfigManageService.update(param);
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS, null);
        } catch (Exception e) {
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("修改商品风险杠杆配置记录异常，e={}", e);
        }
        return response.toJsonString();
    }

    /**
     * 一键清仓
     *
     * @return
     */
    @RequestMapping(value = "/reloadClearTime")
    @ResponseBody
    public String reloadClearTime(HttpServletRequest request, String productCodes) {
        Response response = null;
        try {
            String[] products = productCodes.split(",");
            if (products.length > 0) {
                productManageServiceImpl.reloadProdClearTime(products);
            }
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS, null);
        } catch (Exception e) {
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("重载清仓时间错误，e={}", e);
        }
        return response.toJsonString();
    }
}
