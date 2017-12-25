package com.lt.manager.controller.fund;

import com.lt.api.business.product.IProductApiService;
import com.lt.enums.fund.FundCashOptCodeEnum;
import com.lt.enums.fund.FundIoWithdrawalEnum;
import com.lt.enums.fund.IFundOptCode;
import com.lt.enums.sys.CurrencyEnum;
import com.lt.manager.bean.sys.Staff;
import com.lt.manager.service.fund.IFundWithdrawService;
import com.lt.model.fund.FundIoCashWithdrawal;
import com.lt.model.fund.FundOptCode;
import com.lt.util.StaffUtil;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.CalendarTools;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.DoubleUtils;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.alipay.alipayTranfer.util.AlipayNotify;
import com.lt.util.utils.model.Response;
import org.jboss.netty.util.internal.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * 提现管理controller
 * 提现相关操作均在此处 便于管理
 * @author jingwb
 *
 */
@Controller
@RequestMapping(value="/fundWithdraw")
public class FundWithdrawController {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IFundWithdrawService fundWithDrawServiceImpl;
	
	@Autowired
	private IProductApiService productApiServiceImpl;
	
	public Map<String,Date> repairCurrentMap = new ConcurrentHashMap<String, Date>();

	/**
	 * 银生宝提现
	 * @param request
	 * @param ioList
	 * @return
	 */
	@RequestMapping("/withdrawForUnspay")
	@ResponseBody
	public String withdrawForUnspay(HttpServletRequest request,String ioList) {
		Response  resp = null;
		try{
			logger.info("银生宝提现");
			logger.info("REQUEST PARAMETERS:{}",ioList);
			
			if(StringTools.isEmpty(ioList)){
				return LTResponseCode.getCode(LTResponseCode.FU00003).toJsonString();
			}
			Integer transferUserId = 0;
			Staff staff = StaffUtil.getStaff(request);
			if(staff!=null && StringTools.isNotEmpty(staff.getId()+"")){
				transferUserId = staff.getId();
			}
			fundWithDrawServiceImpl.withdrawForUnspay(ioList.split(","),transferUserId);
			resp = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		}catch(Exception e){
			resp = LTResponseCode.getCode(e.getMessage());
			logger.info("======银生宝转账失败,e={}",e);
		}		
		return resp.toJsonString();
	}
	/**
	 * 浙江银生宝提现
	 * @param request
	 * @param ioList
	 * @return
	 */
	@RequestMapping("/withdrawForUnspayZJ")
	@ResponseBody
	public String withdrawForUnspayZJ(HttpServletRequest request,String ioList) {
		Response  resp = null;
		try{
			logger.info("银生宝提现");
			logger.info("REQUEST PARAMETERS:{}",ioList);

			if(StringTools.isEmpty(ioList)){
				return LTResponseCode.getCode(LTResponseCode.FU00003).toJsonString();
			}
			Integer transferUserId = 0;
			Staff staff = StaffUtil.getStaff(request);
			if(staff!=null && StringTools.isNotEmpty(staff.getId()+"")){
				transferUserId = staff.getId();
			}
			fundWithDrawServiceImpl.withdrawForUnspayZJ(ioList.split(","),transferUserId);
			resp = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		}catch(Exception e){
			resp = LTResponseCode.getCode(e.getMessage());
			logger.info("======银生宝转账失败,e={}",e);
		}
		return resp.toJsonString();
	}
	
	/**
	 * 银生宝提现回调接口
	 * @param request
	 */
	@RequestMapping("/callbackForUnspay")
	@ResponseBody
	public void callbackForUnspay(HttpServletRequest request) {
		try{
			logger.info("===银生宝提现回掉接口==执行");
			String result_code = request.getParameter("result_code");
			String result_msg = request.getParameter("result_msg");
			String amount = request.getParameter("amount");
			String orderId = request.getParameter("orderId");
			logger.info("银生宝提现返回信息,result_code={},result_msg={},amount={},orderId={}",result_code,result_msg,amount,orderId);
			if(StringTools.isEmpty(orderId)){
				logger.info("银生宝返回参数缺失");
				return;
			}
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("result_code", result_code);
			map.put("result_msg", result_msg);
			map.put("amount", amount);
			map.put("orderId", orderId);
			fundWithDrawServiceImpl.callbackForUnspay(map);
			logger.info("===银生宝提现回掉接口==结束");
		}catch(Exception e){
			logger.info("银生宝提现回调接口异常,e={}",e);
		}		
	}

	/**
	 * 智付提现
	 * @param request
	 * @param ioList
	 * @return
	 */
	@RequestMapping("/withdrawForDinpay")
	@ResponseBody
	public String withdrawForDinpay(HttpServletRequest request, String ioList, Integer tranType) {
		Response  resp = null;
		try{
			if(StringTools.isEmpty(ioList) || tranType == null){
				return LTResponseCode.getCode(LTResponseCode.FU00003).toJsonString();
			}
			Integer transferUserId = 0;
			Staff staff = StaffUtil.getStaff(request);
			if(staff!=null && StringTools.isNotEmpty(staff.getId()+"")){
				transferUserId = staff.getId();
			}
			fundWithDrawServiceImpl.withdrawForDinpay(ioList.split(","), transferUserId, tranType);
			resp = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		}catch(Exception e){
			resp = LTResponseCode.getCode(e.getMessage());
			logger.info("======智付转账失败,e={}",e);
		}
		return resp.toJsonString();
	}

	/**
	 * 提现审核（1，审核通过，2审核拒绝,4 转账失败）
	 * @param request
	 * @return    
	 * @return:       String    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月6日 下午1:45:54
	 */
	@RequestMapping(value="/auditWithdrawMutil")
	@ResponseBody
	public String auditWithdrawMutil(HttpServletRequest request){
		Response response = null ;
		try{
			// 批量操作id
			String ioIds = request.getParameter("ioIds");
			// 操作 2  提现拒绝 1  审核通过
			String operate = request.getParameter("operate");
//			 HttpSession session = request.getSession();
//		        
//			Staff staff = session.getAttribute("staff") != null ? (Staff)  session.getAttribute("staff") : null ;
//			
			String modifyId = "0";// staff == null ?"0":staff.getName();
			/**获取当前操作人**/
			Staff staff = StaffUtil.getStaff(request);
			if(staff!=null && StringTools.isNotEmpty(staff.getId()+"")){
				modifyId = staff.getId()+"";
			}
			logger.info("当前操作员："+modifyId);
			// 备注
			String remark = request.getParameter("remark");
			
			if(ioIds == null || ioIds.trim().equals("")
					|| operate == null || operate.trim().equals("")){
				throw new LTException(LTResponseCode.FU00003);
			}
			
			int result = Integer.parseInt(operate);
			IFundOptCode fundCode = null ;
			if(result != FundIoWithdrawalEnum.WAIT.getValue() && result != FundIoWithdrawalEnum.REPULSE.getValue() &&
					result != FundIoWithdrawalEnum.TREPULSE.getValue()){
				throw new LTException(LTResponseCode.FU00003);
			}else{
				if(result == FundIoWithdrawalEnum.WAIT.getValue()){
					fundCode = FundCashOptCodeEnum.WITHDRAW;
				}else if(result == FundIoWithdrawalEnum.REPULSE.getValue() || result == FundIoWithdrawalEnum.TREPULSE.getValue()){
					fundCode = FundCashOptCodeEnum.REJECT_WITHDRAW;
				}
			}
			fundWithDrawServiceImpl.auditWithdraw(ioIds, result, modifyId,remark,fundCode);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		}catch (Exception e) {
			// TODO: handle exception
			logger.debug("程序异常：",e);
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 * 提现审核（1，审核通过，2审核拒绝,4 转账失败）
	 * @param request
	 * @return    
	 * @return:       String    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月6日 下午1:45:54
	 */
	@RequestMapping(value="/auditWithdraw")
	@ResponseBody
	public String auditWithdraw(HttpServletRequest request){
		Response response = null ;
		try{
			// 批量操作id
			String ioIds = request.getParameter("ioId");
			// 操作 2  提现拒绝 1  审核通过
			String operate = request.getParameter("operate");
			// 修改用户暂定 1 
			String modify_name = "1";
			// 备注
			String remark = request.getParameter("remark");
			// 实际收取手续费
			String fact_tax = request.getParameter("fact_tax");
			
			if(ioIds == null || ioIds.trim().equals("")
					|| operate == null || operate.trim().equals("") || !StringTools.isNumberic(fact_tax,false,true,true)){
				throw new LTException(LTResponseCode.FU00003);
			}
			
			int result = Integer.parseInt(operate);
			IFundOptCode fundCode = null ;
			if(result != FundIoWithdrawalEnum.WAIT.getValue() && result != FundIoWithdrawalEnum.REPULSE.getValue()
					&& result != FundIoWithdrawalEnum.TREPULSE.getValue()){
				throw new LTException(LTResponseCode.FU00003);
			}else{
			
				if(result == FundIoWithdrawalEnum.WAIT.getValue()){
					fundCode = FundCashOptCodeEnum.WITHDRAW;
				}else if(result == FundIoWithdrawalEnum.REPULSE.getValue()){
					fundCode = FundCashOptCodeEnum.REJECT_WITHDRAW;
				}else if(result == FundIoWithdrawalEnum.TREPULSE.getValue()){
					fundCode = FundCashOptCodeEnum.REJECT_WITHDRAW;
				}
				
			}
			fundWithDrawServiceImpl.auditWithdrawSingle(ioIds, result, modify_name,Double.valueOf(fact_tax),remark,fundCode);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		}catch (Exception e) {
			// TODO: handle exception
			logger.debug("程序异常：",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	
	
	/**
	 * 提现转账（支付宝）
	 * @param request
	 * @return    
	 * @return:       String    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月6日 下午2:31:21
	 */
	@RequestMapping(value="/alipayTransferToBank")
	@ResponseBody
	public String alipayTransferToBank(HttpServletRequest request){
		Response response = null ;
		try{
			String ioList = request.getParameter("ioList");
			String modifyId = "1";
			
			if(ioList == null || ioList.equals("")){
				throw new LTException(LTResponseCode.FU00003);
			}
			
			fundWithDrawServiceImpl.alipayToBank(ioList, modifyId);
			
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}

	/**
	 * 支付宝转账，接收支付宝返回结果
	 * @param request
	 * @param response    
	 * @return:       void    
	 * @throws IOException 
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月9日 下午4:36:32
	 */
	@RequestMapping(value="/alipayTransferReceive")
	@ResponseBody
	public void alipayTransferReceive(HttpServletRequest request,HttpServletResponse response) throws IOException {

		logger.info("============alipayAutoReceive============");
		String result = "fail";
		String message = "";
		String out_biz_no = "";
		
		try{
			String status = new String(request.getParameter("status").getBytes("ISO-8859-1"),"UTF-8");//交易状态
			out_biz_no = new String(request.getParameter("out_biz_no").getBytes("ISO-8859-1"),"UTF-8");//商户订单号
			
			Map<String,String> params = new HashMap<String,String>();
			Map<String, String[]> requestParams = request.getParameterMap();
			String requestUrl = request.getRequestURL()+"?";
			for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i]
							: valueStr + values[i] + ",";
				}
				requestUrl += name + "=" + valueStr + "&";
				//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
				params.put(name, valueStr);
				logger.info("{}:{}",name,valueStr);
			}
			logger.info("FULL REQUEST URL::{}",requestUrl);
			if (AlipayNotify.verify(params)) {//验证成功
				FundIoCashWithdrawal fundIoCashWithdrawal = new FundIoCashWithdrawal();
				fundIoCashWithdrawal.setPayId(out_biz_no);
				fundIoCashWithdrawal = fundWithDrawServiceImpl.getFundCashDrawInfo(fundIoCashWithdrawal);
				
				Date doneDate = new Date();
				
				if (status.equals("SUCCESS")) {//转账成功处理
					doneDate = CalendarTools.parseDateTime(params.get("pay_date"), CalendarTools.DATETIME);
					if (fundIoCashWithdrawal.getStatus().equals(FundIoWithdrawalEnum.PROCESS.getValue())) {//待转帐
						fundWithDrawServiceImpl.alipayTransferResiveResult(fundIoCashWithdrawal, true);
					}
				} else if (status.equals("FAIL") || status.equals("REFUND")){//转账失败或退票处理
					String error_code = new String(org.apache.commons.lang3.StringUtils.defaultIfBlank(request.getParameter("error_code"), status));//交易状态
					String fail_reason = new String(org.apache.commons.lang3.StringUtils.defaultIfBlank(request.getParameter("fail_reason"), "业务繁忙,处理失败"));//交易状态
					fundIoCashWithdrawal.setRemark(fail_reason);
					fundWithDrawServiceImpl.alipayTransferResiveResult(fundIoCashWithdrawal, false);
					message = "==>转账失败原因alipayAutoReceive "+ status + ":---" + error_code + ":" + fail_reason;
					logger.error(message);
				}
			} else {//验证失败
				result = "fail";
			}
		}catch(Exception e){
			message = "==>alipayAutoReceive error:"+ result + "---" + out_biz_no;
			logger.error("错误堆栈：{}",e);
			response.getWriter().print("fail");
		}
		message = "==>alipayAutoReceive end:"+ result + "---" + out_biz_no;
		logger.info(message);
		response.getWriter().print(result);
	
	}
	/**
	 * 银生宝提现查询接口
	 * @param request
	 */
	@RequestMapping("/queryWithdrawResultForUnspay")
	@ResponseBody
	public void queryWithdrawResultForUnspay(HttpServletRequest request) {
		try {
			fundWithDrawServiceImpl.queryWithdrawResultForUnspay();
		} catch (Exception e) {
			logger.info("定时查询银生宝提现结果,异常e={}",e);
		}
	}

	/**
	 * 智付转账查询接口
	 * @param request
	 */
	@RequestMapping("/queryWithdrawResultForDinpay")
	@ResponseBody
	public void queryWithdrawResultForDinpay(HttpServletRequest request) {
		try {
			fundWithDrawServiceImpl.queryWithdrawResultForDinpay();
		} catch (Exception e) {
			logger.info("定时查询智付提现结果,异常e={}",e);
		}
	}

	/**
	 * 钱通提现查询接口
	 * @param request
	 */
	@RequestMapping("/queryWithdrawResultForQtong")
	@ResponseBody
	public void queryWithdrawResultForQtong(HttpServletRequest request) {
		try {
			fundWithDrawServiceImpl.queryWithdrawResultForQtong();
		} catch (Exception e) {
			logger.info("定时查询钱通提现结果,异常e={}",e);
		}
	}
	/**
	 * 后台补单操作
	 * @param request    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月16日 下午1:35:09
	 */
	@RequestMapping("/repairFinancyIo")
	@ResponseBody
	public String repairFinancyIo(HttpServletRequest request,FundOptCode fundOptCode){
		Response  response = null ;
		//前台传递的金额改成人民币
		String amt = request.getParameter("amt");//美元
		String rmbAmt = request.getParameter("rmbAmt");//人民币
		String id = request.getParameter("id");
		try{
			if(repairCurrentMap.containsKey(id)){
				Date date = new Date();
				Date operateDate = repairCurrentMap.get(id);
				// 设定五分钟账号处理时间，防止系统异常需要重启服务
				if(!DateTools.addMinute(operateDate, 5).before(date)){
					throw new  LTException(LTResponseCode.US01112);
				}
			}
			
			repairCurrentMap.put(id, new Date());
			
			if(amt == null || amt.equals("") || id == null || id.equals("")){
				throw new LTException(LTResponseCode.FUY00003);
			}
			Integer modifyId = 0;
			Staff staff = StaffUtil.getStaff(request);
			if(staff!=null && StringTools.isNotEmpty(staff.getId()+"")){
				modifyId = staff.getId();
			}
			
			
			Double rate = productApiServiceImpl.getRate(CurrencyEnum.DEFAULT_CURRENCY.getCurrencyCode());
			
			// 前端没正确运算
			if(DoubleUtils.scaleFormatEnd(DoubleUtils.mul(Double.valueOf(rmbAmt), rate), 2) != Double.valueOf(amt)){
				throw new LTException(LTResponseCode.FUY00014);
			}
			
			//fundWithDrawServiceImpl.repairFinancyIo(id, amt,rmbAmt,fundOptCode);
			fundWithDrawServiceImpl.repairFinancyIo(id, amt,rmbAmt,fundOptCode,modifyId);
			response = new Response(LTResponseCode.SUCCESS, "补单成功");
		}catch (Exception e) {
			// TODO: handle exception
			response = LTResponseCode.getCode(e.getMessage());
		}
		
		repairCurrentMap.remove(id);
		return response.toJsonString();
	}
	
	/**
	 * 补单批量拒绝
	 * @param request
	 * @return    
	 * @return:       String    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月17日 下午1:19:08
	 */
	@RequestMapping("/financyIoReject")
	@ResponseBody
	public String financyIoReject(HttpServletRequest request){
		Response  response = null ;
		try{
			String ids = request.getParameter("ids");
			
			if(ids == null || ids.equals("")){
				throw new LTException(LTResponseCode.FUY00003);
			}
			
			fundWithDrawServiceImpl.financyIoReject(ids);
			response = new Response(LTResponseCode.SUCCESS, "拒绝充值操作成功");
		}catch (Exception e) {
			// TODO: handle exception
			response = LTResponseCode.getCode(e.getMessage());
		}
		
		return response.toJsonString();
	}
	
	/**
	 * 人工转账
	 * @param request
	 * @return    
	 * @return:       String    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年5月3日 下午3:09:57
	 */
	@RequestMapping("/manualTransfer")
	@ResponseBody
	public String manualTransfer(HttpServletRequest request){
		Response  response = null ;
		try{
			String ids = request.getParameter("ids");
			
			if(ids == null || ids.equals("")){
				throw new LTException(LTResponseCode.FU00003);
			}
			Integer transferUserId = 0;
			Staff staff = StaffUtil.getStaff(request);
			if(staff!=null && StringTools.isNotEmpty(staff.getId()+"")){
				transferUserId = staff.getId();
			}			
			fundWithDrawServiceImpl.manualTransfer(ids,transferUserId);
			response = new Response(LTResponseCode.SUCCESS, "处理成功");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 * 强制处理转账信息
	 * @param request
	 * @return    
	 * @return:       String    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年5月3日 下午4:22:13
	 */
	@RequestMapping("/transferForceStatus")
	@ResponseBody
	public String transferForceStatus(HttpServletRequest request){
		
		Response  response = null ;
		/** 操作数据ID*/
		String id = request.getParameter("flowId");
		/** 操作 1：成功 2 失败*/
		String operate = request.getParameter("operate");
		/** 失败原因 */
		String remark = request.getParameter("remark");
		
		try{
			if(id == null || id.equals("") || operate == null || !StringTools.isNumeric(operate)){
				throw new LTException(LTResponseCode.FU00003);
			}
			
			fundWithDrawServiceImpl.transferForce(id, Integer.parseInt(operate),remark);
			response = new Response(LTResponseCode.SUCCESS, "操作成功");
		}catch (Exception e) {
			// TODO: handle exception
			response = LTResponseCode.getCode(e.getMessage());
		}
		
		return response.toJsonString();
	}

	/**
	 * 爸爸付提现
	 * @param request
	 * @param ioList
	 * @return
	 */
	@RequestMapping("/withdrawForDaddypay")
	@ResponseBody
	public String withdrawForDaddypay(HttpServletRequest request, String ioList) {
		Response  resp = null;
		try{
			if(StringTools.isEmpty(ioList)){
				return LTResponseCode.getCode(LTResponseCode.FU00003).toJsonString();
			}
			Integer transferUserId = 0;
			Staff staff = StaffUtil.getStaff(request);
			if(staff!=null && StringTools.isNotEmpty(staff.getId()+"")){
				transferUserId = staff.getId();
			}
			fundWithDrawServiceImpl.withdrawForDaddypay(ioList.split(","), transferUserId);
			resp = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		}catch(Exception e){
			resp = LTResponseCode.getCode(e.getMessage());
			logger.info("======daddypay转账失败,e={}",e);
		}
		return resp.toJsonString();
	}

	/**
	 * 钱通提现
	 * @param request
	 * @param ioList
	 * @return
	 */
	@RequestMapping("/withdrawForQtongPay")
	@ResponseBody
	public String withdrawForQtongPay(HttpServletRequest request, String ioList) {
		Response  resp = null;
		try{
			if(StringTools.isEmpty(ioList)){
				return LTResponseCode.getCode(LTResponseCode.FU00003).toJsonString();
			}
			Integer transferUserId = 0;
			Staff staff = StaffUtil.getStaff(request);
			if(staff!=null && StringTools.isNotEmpty(staff.getId()+"")){
				transferUserId = staff.getId();
			}
			fundWithDrawServiceImpl.withdrawForQtongPay(ioList.split(","), transferUserId);
			resp = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		}catch(Exception e){
			resp = LTResponseCode.getCode(e.getMessage());
			logger.info("======钱通提现失败,e={}",e);
		}
		return resp.toJsonString();
	}

	@RequestMapping("/withdrawForJiuPai")
	@ResponseBody
	public String withdrawForJiuPai(HttpServletRequest request,String ioList){
		Response resp = null;
		try{
			if(StringTools.isEmpty(ioList)){
				return LTResponseCode.getCode(LTResponseCode.FU00003).toJsonString();
			}
			Integer transferUserId = 0;
			Staff staff = StaffUtil.getStaff(request);
			if(staff!=null && StringTools.isNotEmpty(staff.getId()+"")){
				transferUserId = staff.getId();
			}
			fundWithDrawServiceImpl.withdrawForJiuPaiPay(ioList.split(","), transferUserId,request);
			resp = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		}catch(Exception e){
			resp = LTResponseCode.getCode(e.getMessage());
			logger.info("======九派提现失败,e={}",e);
		}
		return resp.toString();
	}





}
