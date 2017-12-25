package com.lt.constant.recharge;

import java.util.Map;

import com.lt.util.utils.StringTools;

import javolution.util.FastMap;

public class AggpayRechargeConstant {
	public final static Map<String, String> result = FastMap.newInstance();
	public final static String DEFAULT_CODE ="9999";
	public final static String DEFAULT_MSG = "通用错误";
	static{
		result.put("1015","没有找到出金流水的冻结资金操作日志");
		result.put("1014","没有找到转账流水的资金操作日志");
		result.put("1013","没有找到商户资金信息");
		result.put("1012","没有找到商户参数配置信息");
		result.put("1011","查询数据库失败");
		result.put("1010","构建统计记录数SQL语句失败");
		result.put("1009","SQL语句为空");
		result.put("1008","无效的SQL语句");
		result.put("1007","请求数据库连接失败");
		result.put("1006","响应消息打包失败");
		result.put("1005","获取单条纪录失败");
		result.put("1004","获取多条纪录失败");
		result.put("1003","分页处理失败");
		result.put("1002","构建sql语句失败");
		result.put("1001","执行SQL失败");
		result.put("9999","处理中，状态未知");
		result.put("1999","未知错误");
		result.put("0000","正确处理");
		result.put("2001","商户编号不存在");
		result.put("2002","支付密码不匹配");
		result.put("2003","商户编号无效");
		result.put("2004","报文格式有误，解析失败，表示json数据解析失败");
		result.put("2005","无效商户类型");
		result.put("2006","无效支付密码");
		result.put("2007","商户类型不匹配");
		result.put("2008","账号不在线");
		result.put("2009","操作权限不够");
		result.put("2010","会员无账户在线");
		result.put("2011","账户在别处登录");
		result.put("2012","值为空");
		result.put("2013","登录账户已经存在");
		result.put("2014","无效的证件类型");
		result.put("2015","无效的性别");
		result.put("2016","无效的会员类型");
		result.put("2017","商户编号为空");
		result.put("2018","银行账号为空");
		result.put("2019","银行账号无效");
		result.put("2020","银行账号错误");
		result.put("2021","银行账户状态异常");
		result.put("2022","银行账户名称为空");
		result.put("2023","银行账户名称不匹配");
		result.put("2024","银行账户名称错误");
		result.put("2025","交易金额无效");
		result.put("2026","订单号重复");
		result.put("2027","资金不足");
		result.put("2028","无效资金操作请求");
		result.put("2029","银行流水号重复");
		result.put("2030","订单号为空");
		result.put("2031","商户出金已关闭");
		result.put("2032","商户已被冻结");
		result.put("2033","单笔出金金额超过限额");
		result.put("2034","单日出金金额超过限额");
		result.put("2035","资金操作申请类型无效");
		result.put("2036","商户状态异常");
		result.put("2037","商户出金开关标志异常");
		result.put("2038","出金金额未达单笔最低金额");
		result.put("2039","出金金额超过单笔最大限额");
		result.put("2040","商户出金控制标志异常");
		result.put("2041","商户出金控制标志异常");
		result.put("2042","出入金转账记录不存在");
		result.put("2043","出金流水已解冻");
		result.put("2044","出金流水已扣钱");
		result.put("2045","入金流水已增加金额");
		result.put("2046","币种无效");
		result.put("2047","银行账户类型无效");
		result.put("3001","报文解密失败");
		result.put("3002","报文加密失败");
		result.put("3003","验证报文签名失败");
		result.put("3004","报文签名失败");
		result.put("3005","报文签名为空");
		result.put("3006","组装报文失败");
		result.put("3007","返回码为空");
		result.put("3008","连接支付平台主机异常");
		
	}
	
	/**
	 * 查询错误信息
	 * @param resultCode
	 * @return
	 */
	public String getResultMsg(String resultCode){
		if(StringTools.isNotEmpty(resultCode)){
			String resultMsg = result.get(resultCode);
			if(StringTools.isNotEmpty(resultMsg)){
				return resultMsg;
			}
			
		}
		return DEFAULT_MSG;
	}
	
	
	public static void main(String[] args)  throws Exception{
/*		File file = new File("c:/data.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String data = null;
		while((data=reader.readLine())!=null){
			System.out.println("result.put("+"\""+data.substring(0, 4)+"\"," +"\""+data.substring(4) +"\");");
			
		}*/
	}
}
