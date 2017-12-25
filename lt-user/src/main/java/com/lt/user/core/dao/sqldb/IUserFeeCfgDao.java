package com.lt.user.core.dao.sqldb;

import java.util.List;

import com.lt.model.user.InvestorFeeCfg;
import org.apache.ibatis.annotations.Param;

public interface IUserFeeCfgDao {
	

	
	/** com.lt.user.core.dao.sqldb.IUserFeeCfgDao.selectInvestorFeeCfg
	 * 为资金提供获取用户产品费用数据
	 * @param tele 电话
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 * @param sms_type 短信类型
	 * @return    
	 * @return:       String    
	 * @throws 
	 * @author        licy
	 * @Date          2016年12月10日 下午8:37:37
	 */
	public List<InvestorFeeCfg> selectInvestorFeeCfg(@Param("userId") String userId, @Param("productId") Integer productId, @Param("plateType") Integer plateType, @Param("supportDirect") Integer supportDirect);
	
	public InvestorFeeCfg selectInvestorFeeCfgByProId(Integer productId, String investorId);

	public InvestorFeeCfg selectInvestorFeeCfgScore(@Param("investorId") String investorId, @Param("productId") Integer productId, @Param("plateType") Integer plateType, @Param("supportDirect") Integer supportDirect);

	public InvestorFeeCfg selectInvestorFeeCfgBySecurityCode(@Param("investorId") String investorId, @Param("productId") Integer productId, @Param("securityCode") String securityCode, @Param("supportDirect") Integer supportDirect);
}
