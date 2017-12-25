package com.lt.manager.service.impl.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.manager.bean.user.BankCard;
import com.lt.manager.bean.user.BankVo;
import com.lt.manager.dao.user.BankCardManageDao;
import com.lt.manager.dao.user.BankManageDao;
import com.lt.manager.service.user.IBankCardManageService;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;

/**
 * 银行卡管理实现类
 * @author licy
 *
 */
@Service
public class BankCardManageServiceImpl implements IBankCardManageService{

	@Autowired
	private BankCardManageDao bankCardManageDao;
	@Autowired
	private BankManageDao bankManageDao;
	
	
	@Override
	public void addBankCard(BankCard param) throws Exception {
				
		BankVo vo = bankManageDao.selectBankVo(param.getBankCode());
		//判断是否为默认
		List<BankCard> list = bankCardManageDao.selectBankCardByUserId(param.getUserId());
		if(list != null && list.size() > 0){
			//查询是否有银行卡
			for(BankCard bankCard:list){
				if(param.getBankCardNum().equals(bankCard.getBankCardNum())){
					throw new LTException(LTResponseCode.US05002);
				}
			}			
			param.setIsDefault(0);//非默认
		}else{
			param.setIsDefault(1);//默认
		}
		
		param.setBankName(vo.getBankName());
		param.setBankStatus(1);
		bankCardManageDao.insertBankCard(param);
	}

	@Override
	public void editBankCard(BankCard param) throws Exception {
		List<BankCard> list = bankCardManageDao.selectBankCardByUserId(param.getUserId());
		if(list != null && list.size() > 0){
			//查询是否有银行卡
			for(BankCard bankCard:list){
				if(StringTools.isNotEmpty(param.getNewBankCardNum()) && !param.getBankCardNum().equals(param.getNewBankCardNum())){
					if(param.getNewBankCardNum().equals(bankCard.getBankCardNum())){
						throw new LTException(LTResponseCode.US05002);
					}
				}
			}			
		}
		if(StringTools.isNotEmpty(param.getNewBankCardNum()) && !param.getBankCardNum().equals(param.getNewBankCardNum())){
			param.setBankCardNum(param.getNewBankCardNum());
		}
		if(param.getIsDefault()==1){
			this.bankCardManageDao.updateBankCardDefaultByUserId(param.getUserId());
		}
		bankCardManageDao.updateBankCard(param);
	}

	@Override
	public void removeBankCard(BankCard param) throws Exception {
		//判断该商品类型是否已被商品占用，如果被占用则不允许删除
		// TODO
		bankCardManageDao.deleteBankCard(param);
	}

	@Override
	public List<BankCard> findBankCardByUserId(String userId) {
		List <BankCard> list = bankCardManageDao.selectBankCardByUserId(userId);
		if(StringTools.isNotEmpty(list)&& list.size()>0)
		for (BankCard bankCard : list) {
			bankCard.setBankCardNum(StringTools.fuzzy(bankCard.getBankCardNum(), 4, 3));
		}
		return list;
	}
	
	@Override
	public List<BankCard> findBankInfo() {
		return bankCardManageDao.selectBankInfo();
	}
	
	@Override
	public List<BankCard> findProInfo() {
		return bankCardManageDao.selectProv();
	}
	
	@Override
	public List<BankCard> findCityInfo(String provinceId) {
		return bankCardManageDao.selectCity(provinceId);
	}
	@Override
	public List<BankCard> findBranchBank(String cityId,String bankCode) {
		return bankCardManageDao.selectBranchBank(cityId,bankCode);
	}
	
}
