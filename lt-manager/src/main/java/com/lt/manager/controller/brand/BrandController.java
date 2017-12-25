package com.lt.manager.controller.brand;

import com.github.pagehelper.Page;
import com.lt.manager.bean.brand.BrandPage;
import com.lt.manager.bean.brand.BrandVo;
import com.lt.manager.service.brand.IBrandService;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.JqueryEasyUIData;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Description: 品牌管理
 * Created by yanzhenyu on 2017/8/25.
 */
@Controller
@RequestMapping("/brand")
public class BrandController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IBrandService brandService;

    @RequestMapping(value = "/getBrandListPage")
    @ResponseBody
    public String getBrandListPage(HttpServletRequest request, BrandVo brandVo) {
        Response response = null;
        try {
            Page<BrandPage> page = brandService.getBrandListPage(brandVo);
            return JqueryEasyUIData.init(page);
        } catch (LTException e) {
            e.printStackTrace();
            response = LTResponseCode.getCode(e.getMessage());
            return response.toJsonString();
        }
    }

    @RequestMapping(value = "/getBrandList")
    @ResponseBody
    public String getBrandList(HttpServletRequest request, BrandVo brandVo) {
        Response response = null;
        try {
            return LTResponseCode.getCode(
                    LTResponseCode.SUCCESS, brandService.getBrandList(brandVo)
            ).toJsonString();
        } catch (LTException e) {
            e.printStackTrace();
            response = LTResponseCode.getCode(e.getMessage());
            return response.toJsonString();
        }
    }

    @RequestMapping(value = "/addBrand")
    @ResponseBody
    public String addBrand(HttpServletRequest request, BrandVo brandVo) {
        Response response = null;
        try {
            if (StringTools.isEmpty(brandVo.getBrandName())) {
                throw new LTException(LTResponseCode.MA00004);
            }
            if (StringTools.isEmpty(brandVo.getBrandCode())) {
                throw new LTException(LTResponseCode.MA00004);
            }
            brandService.addBrand(brandVo);
            response = new Response(LTResponseCode.SUCCESS, "新增成功");
        } catch (LTException e) {
            e.printStackTrace();
            response = LTResponseCode.getCode(e.getMessage());
        }
        return response.toJsonString();
    }

    @RequestMapping(value = "/deleteBrand")
    @ResponseBody
    public String deleteBrand(HttpServletRequest request, String brandId) {
        Response response = null;
        try {

            if (StringTools.isEmpty(brandId)) {
                throw new LTException(LTResponseCode.MA00004);
            }

            brandService.deleteBrand(brandId);
            response = new Response(LTResponseCode.SUCCESS, "删除成功");
        } catch (LTException e) {
            e.printStackTrace();
            response = LTResponseCode.getCode(e.getMessage());
        }
        return response.toJsonString();
    }

    @RequestMapping(value = "/updateBrand")
    @ResponseBody
    public String updateBrand(HttpServletRequest request, BrandVo brandVo) {
        Response response = null;
        try {

            if (StringTools.isEmpty(brandVo.getBrandId())) {
                throw new LTException(LTResponseCode.MA00004);
            }

            if (StringTools.isEmpty(brandVo.getBrandName())) {
                throw new LTException(LTResponseCode.MA00004);
            }

            if (StringTools.isEmpty(brandVo.getBrandCode())) {
                throw new LTException(LTResponseCode.MA00004);
            }
            
            if (StringTools.isEmpty(brandVo.getInvestorId())) {
                throw new LTException(LTResponseCode.MA00004);
            }
            brandService.updateBrand(brandVo);
            response = new Response(LTResponseCode.SUCCESS, "修改成功");
        } catch (LTException e) {
            e.printStackTrace();
            response = LTResponseCode.getCode(e.getMessage());
        }
        return response.toJsonString();
    }
}
