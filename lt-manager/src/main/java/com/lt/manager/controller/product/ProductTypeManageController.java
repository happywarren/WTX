package com.lt.manager.controller.product;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.lt.manager.bean.product.ProductParamVO;
import com.lt.manager.service.product.IProductTypeManageService;
import com.lt.model.product.ProductType;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.JqueryEasyUIData;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**
 * 商品类型控制器
 *
 * @author jingwb
 */
@Controller
@RequestMapping(value = "product")
public class ProductTypeManageController {
    private Logger logger = LoggerTools.getInstance(getClass());

    @Autowired
    private IProductTypeManageService productTypeManageServiceImpl;

    /**
     * 添加商品类型
     *
     * @param request
     * @param param
     * @return
     */
    @RequestMapping(value = "/type/addProdutType")
    @ResponseBody
    public String addProdutType(HttpServletRequest request, ProductParamVO param) {
        Response response = null;
        try {
            if (StringTools.isEmpty(param.getName())) {
                return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
            }
            productTypeManageServiceImpl.addProductType(param);
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
        } catch (Exception e) {
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("添加商品类型异常，e={}", e);
        }
        return response.toJsonString();
    }

    /**
     * 编辑商品类型
     *
     * @param request
     * @param param
     * @return
     */
    @RequestMapping(value = "/type/editProdutType")
    @ResponseBody
    public String editProdutType(HttpServletRequest request, ProductParamVO param) {
        Response response = null;
        try {
            if (param.getId() == null || StringTools.isEmpty(param.getName())) {
                return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
            }
            productTypeManageServiceImpl.editProductType(param);
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
        } catch (Exception e) {
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("编辑商品类型异常，e={}", e);
        }
        return response.toJsonString();
    }

    /**
     * 修改排序
     *
     * @param request
     * @param param
     * @return
     */
    @RequestMapping(value = "/type/editProductTypeSort")
    @ResponseBody
    public String editProductTypeSort(HttpServletRequest request, ProductParamVO param) {
        Response response = null;
        try {
            if (param.getId() == null || param.getSortNum() == null) {
                return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
            }
            productTypeManageServiceImpl.editProductTypeSort(param);
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
        } catch (Exception e) {
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("修改排序异常，e={}", e);
        }
        return response.toJsonString();
    }

    /**
     * 删除商品类型
     *
     * @param request
     * @param param
     * @return
     */
    @RequestMapping(value = "/type/removeProdutType")
    @ResponseBody
    public String removeProdutType(HttpServletRequest request, ProductParamVO param) {
        Response response = null;
        try {
            if (StringTools.isEmpty(param.getIds())) {
                return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
            }
            productTypeManageServiceImpl.removeProductType(param);
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
        } catch (Exception e) {
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("删除商品类型异常，e={}", e);
        }
        return response.toJsonString();
    }

    /**
     * 查询商品类型--分页
     *
     * @param request
     * @param param
     * @return
     */
    @RequestMapping(value = "/type/queryProdutTypePage")
    @ResponseBody
    public String queryProdutTypePage(HttpServletRequest request, ProductParamVO param) {
        Response response = null;
        try {
            Page<ProductType> page = productTypeManageServiceImpl.queryProductTypePage(param);
            return JqueryEasyUIData.init(page);
        } catch (Exception e) {
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("删除商品类型异常，e={}", e);
        }
        return response.toJsonString();
    }

    /**
     * 查询商品类型
     *
     * @param request
     * @param param
     * @return
     */
    @RequestMapping(value = "/type/queryProductType")
    @ResponseBody
    public String queryProductType(HttpServletRequest request, ProductParamVO param) {
        Response response = null;
        try {
            return LTResponseCode.getCode(
                    LTResponseCode.SUCCESS, productTypeManageServiceImpl.queryProductType(param)
            ).toJsonString();
        } catch (Exception e) {
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("删除商品类型异常，e={}", e);
        }
        return response.toJsonString();
    }

    @RequestMapping(value = "/type/test")
    @ResponseBody
    public String test(HttpServletRequest request, ProductParamVO param) {
        Response response = null;
        try {
            productTypeManageServiceImpl.test();
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
        } catch (Exception e) {
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("删除商品类型异常，e={}", e);
        }
        return response.toJsonString();
    }

    @RequestMapping(value = "/type/test1")
    @ResponseBody
    public String test1(HttpServletRequest request, ProductParamVO param) {
        Response response = null;
        try {
            productTypeManageServiceImpl.test1();
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
        } catch (Exception e) {
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("删除商品类型异常，e={}", e);
        }
        return response.toJsonString();
    }
}
