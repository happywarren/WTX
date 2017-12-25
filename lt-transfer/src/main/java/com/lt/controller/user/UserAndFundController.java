package com.lt.controller.user;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lt.api.business.product.IProductApiService;
import com.lt.api.sms.ISmsApiService;
import com.lt.controller.tools.CommonTools;
import com.lt.controller.user.bean.UserOldBankInfo;
import com.lt.controller.user.bean.FundCashFlow;
import com.lt.controller.user.bean.FundScoreFlow;
import com.lt.controller.user.bean.UserBase;
import com.lt.controller.user.bean.UserOldBaseInfo;
import com.lt.controller.user.bean.UserProductChoose;
import com.lt.controller.user.bean.UserServiceMapperTransfer;
import com.lt.controller.user.bean.UserFundMsgBean;
import com.lt.controller.user.bean.UserMainCash;
import com.lt.controller.user.bean.UserMainScore;
import com.lt.controller.user.service.IUserAndFundService;
import com.lt.enums.fund.FundCashOptCodeEnum;
import com.lt.enums.fund.FundFlowTypeEnum;
import com.lt.enums.fund.FundScoreOptCodeEnum;
import com.lt.enums.fund.FundThirdOptCodeEnum;
import com.lt.enums.sys.CurrencyEnum;
import com.lt.model.fund.FundFlow;
import com.lt.model.sms.SystemMessage;
import com.lt.model.sms.SystemMessageContant;
import com.lt.model.user.UserContant;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.DoubleTools;
import com.lt.util.utils.model.Response;
import com.lt.vo.user.UserProductSelectListVo;

/**   
* 项目名称：lt-transfer   
* 类名称：UserAndFundController   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年5月12日 上午9:53:33      
*/
@Controller
@RequestMapping(value="/userAndFund")
public class UserAndFundController {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IUserAndFundService userAndFundServiceImpl ;
	
	@Autowired
	private ISmsApiService smsServiceImpl ;
	
	@Autowired
	private IProductApiService productApiServiceImpl;
	
	@RequestMapping(value="/sendMsgChangeFundToUser")
	@ResponseBody
	public String sendMsgChangeFundToUser(){
		
		String msgContet = "由于平台品牌升级，货币单位统一为美元。您当前的可用余额(cny)元折算后为(usd)美元，敬请知晓！【LT证券】";
		
		Double rate = productApiServiceImpl.getRate(CurrencyEnum.DEFAULT_CURRENCY.getCurrencyCode());
		
		List<UserFundMsgBean> userFundList = new ArrayList<>();
		userFundList = userAndFundServiceImpl.getUserFundInfo(msgContet,rate);
		
		for(UserFundMsgBean bean : userFundList){
			SystemMessage ann = new SystemMessage();
			ann.setUserId("-999"); // 注册时不存在用户信息，赋默认值
			ann.setDestination(bean.getTele());
			ann.setContent(bean.getContent());
			ann.setCause(UserContant.APP_CHANGE_FUND_MARK);
			ann.setType(UserContant.SMS_SHORT_TYPE);
			ann.setSmsType(Integer.parseInt(UserContant.APP_CHANGE_FUND_INFO));
			ann.setPriority(0);
			ann.setStatus(SystemMessageContant.SEND_STATUS_SUCCESS); // 默认为发送成功
			ann.setUserType(0);
			ann.setIp(null);
			ann.setCreateDate(new Date());
			
			try {
				smsServiceImpl.sendUserFundMsg(ann);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.info("短信通知失败，电话为：{},金额为：{}",bean.getTele(),bean.getRmbAmt());
				e.printStackTrace();
			}
		}
		
		Response response = new Response(LTResponseCode.SUCCESS, "短信发送完成");
		return  response.toJsonString();
	}
	
	/**
	 * 用户基本信息转移
	 * @return    
	 * @return:       String    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年5月16日 下午5:43:54
	 */
	@RequestMapping(value="/transferUserInfo")
	@ResponseBody
	public String transferUserInfo(){
		Double rate = productApiServiceImpl.getRate(CurrencyEnum.DEFAULT_CURRENCY.getCurrencyCode());
		List<UserOldBaseInfo> userList = userAndFundServiceImpl.qryUserBaseInfo();

		Set<Integer> productIds = new HashSet<Integer>(); 
		List<UserProductSelectListVo> list = productApiServiceImpl.findAttentionList(null,-1);
		if(CollectionUtils.isNotEmpty(list)){
			for(UserProductSelectListVo userProduct : list){
				productIds.add(userProduct.getId());
			}
		}
		
		boolean isProduct = productIds.isEmpty() ;
		
		/** 用户基本信息表*/
		List<UserBase> userBaseList = new ArrayList<UserBase>();
		List<UserOldBankInfo> userBankList = new ArrayList<UserOldBankInfo>();
		List<UserMainCash> userMainCashList = new ArrayList<UserMainCash>();
		
		List<FundCashFlow> fundCashFlowList = new ArrayList<FundCashFlow>();
		List<FundScoreFlow> fundScoreFlowList = new ArrayList<FundScoreFlow>();
		
		List<UserProductChoose> userProductChooseList = new ArrayList<UserProductChoose>();
		
		List<UserMainScore> userMainScoreList = new ArrayList<UserMainScore>();
		
		List<UserServiceMapperTransfer> serviceMapperList = new ArrayList<UserServiceMapperTransfer>();
		
		List<String> userId = new ArrayList<String>();
		
		
		if(CollectionUtils.isNotEmpty(userList)){
			for(UserOldBaseInfo userBaseInfo : userList){
				
				Integer orderCount = userAndFundServiceImpl.qryUserOrderCount(userBaseInfo.getUserId());
				if(orderCount == null){
					orderCount = 0 ;
				}
				
				UserBase usebase = new UserBase(userBaseInfo);
				if(orderCount > 0 ){
					usebase.setReal_name_status(2);
				}
				
				if(userBaseInfo.getTele_status() != 1){
					usebase.setStatus(-10);
				}
				
				userBaseList.add(usebase);
				
				if(!isProduct){
					for(Integer productId : productIds){
						UserProductChoose userProductChoose = new UserProductChoose(userBaseInfo.getUserId(), productId);
						userProductChooseList.add(userProductChoose);
					}
				}
				
				if(userBaseInfo.getBank_num()!= null && !userBaseInfo.getBank_num().equals("")){
					UserOldBankInfo bankInfo = new UserOldBankInfo(userBaseInfo);
					bankInfo.setBank_code(CommonTools.getBankCode(userBaseInfo.getBank_name()));
					// 原系统状态值与新系统 相反
					bankInfo.setIs_default(bankInfo.getIs_default() == 0 ? 1 : 0);
					userBankList.add(bankInfo);
				}
				
				userId.add(userBaseInfo.getUserId());
			}
		}
		
		if(CollectionUtils.isNotEmpty(userId)){
			for(String userIds : userId){
				UserMainCash userMainCash = userAndFundServiceImpl.qryUserMainCash(userIds);
				if(userMainCash != null && userMainCash.getTotal_recharge_amount() != 0 ){
					userMainCash.setBalance(DoubleTools.mul(userMainCash.getBalance() , rate, 2));
//					userMainCash.setTotal_benefit_amount(DoubleTools.rund(userMainCash.getTotal_benefit_amount() * rate, 2));
//					userMainCash.setTotal_counter_fee(DoubleTools.rund(userMainCash.getTotal_counter_fee() * rate, 2));
//					userMainCash.setTotal_draw_amount(DoubleTools.rund(userMainCash.getTotal_draw_amount() * rate, 2));
					userMainCash.setTotal_recharge_amount(0.0);
//					userMainCash.setTotal_interest_amount(DoubleTools.rund(userMainCash.getTotal_interest_amount() * rate, 2));
					userMainCash.setTotal_present_amount(userMainCash.getBalance());
					userMainCashList.add(userMainCash);
					
					FundCashFlow cashFlow = new FundCashFlow(userIds, FundFlowTypeEnum.INCOME.getValue(), FundCashOptCodeEnum.TRANSFER.getFirstLevelCode(), FundCashOptCodeEnum.TRANSFER.getCode(), 
							FundThirdOptCodeEnum.XTQY.getThirdLevelCode(), userMainCash.getBalance(), userMainCash.getBalance(), FundThirdOptCodeEnum.XTQY.getThirdLevelname(), DateTools.formatDate(new Date(), DateTools.FORMAT_LONG));
					fundCashFlowList.add(cashFlow);
					
					UserServiceMapperTransfer mapperTransfer = new UserServiceMapperTransfer(userIds, "1001", 1, DateTools.formatDate(new Date(), DateTools.FORMAT_LONG), DateTools.formatDate(new Date(), DateTools.FORMAT_LONG));
					serviceMapperList.add(mapperTransfer);
				}
				

				UserMainScore userMainScore = new UserMainScore();
				userMainScore.setUser_id(userIds);
				userMainScore.setBalance(10000.0);
				userMainScore.setTotal_present_amount(10000.0);
				
				FundScoreFlow scoreFlow = new FundScoreFlow(userIds, FundFlowTypeEnum.INCOME.getValue(), FundScoreOptCodeEnum.PRESENT.getFirstLevelCode(), FundScoreOptCodeEnum.PRESENT.getCode(), 
						FundThirdOptCodeEnum.ZSTYJ.getThirdLevelCode(), 10000.0, 10000.0, FundThirdOptCodeEnum.ZSTYJ.getThirdLevelname(), DateTools.formatDate(new Date(), DateTools.FORMAT_LONG));
				fundScoreFlowList.add(scoreFlow);
				
				userMainScoreList.add(userMainScore);
				
				UserServiceMapperTransfer mapperTransferS = new UserServiceMapperTransfer(userIds, "1000", 1, DateTools.formatDate(new Date(), DateTools.FORMAT_LONG), DateTools.formatDate(new Date(), DateTools.FORMAT_LONG));
				serviceMapperList.add(mapperTransferS);
			}
		}
		
		try {
			CommonTools.createFile("1_user_base", CommonTools.createInsertSql("user_base", userBaseList));
			CommonTools.createFile("2_user_bankcard", CommonTools.createInsertSql("user_bankcard", userBankList));
			CommonTools.createFile("3_fund_main_cash", CommonTools.createInsertSql("fund_main_cash", userMainCashList));
			
			CommonTools.createFile("5_fund_main_score", CommonTools.createInsertSql("fund_main_score", userMainScoreList));
			CommonTools.createFile("4_fund_flow_cash", CommonTools.createInsertSql("fund_flow_cash", fundCashFlowList));
			CommonTools.createFile("6_fund_flow_score", CommonTools.createInsertSql("fund_flow_score", fundScoreFlowList));
			
			CommonTools.createFile("14_user_service_mapper", CommonTools.createInsertSql("user_service_mapper", serviceMapperList));
			CommonTools.createFile("15_user_product_select", CommonTools.createInsertSql("user_product_select", userProductChooseList));
			
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return LTResponseCode.getCode(LTResponseCode.SUCCESS).toJsonString() ;
	}
	
	
	
}
