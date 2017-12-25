package com.lt.manager.service.impl.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.lt.manager.bean.user.Channel;
import com.lt.manager.bean.user.ChargeChannelVo;
import com.lt.manager.bean.user.PayAndChannelRelation;
import com.lt.manager.dao.user.ChannelDao;
import com.lt.manager.service.user.IChannelService;
import com.lt.model.user.charge.UserChargeMapper;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;

import javolution.util.FastList;


/**
 * 
 * <br>
 * <b>功能：</b>ChannelService<br>
 * @author yanzhenyu
 */
@Service("channelService")                   
public class  ChannelServiceImpl 	implements IChannelService {

	private final Logger logger = LoggerFactory.getLogger(ChannelServiceImpl.class.getName());
		
	@Autowired
	private ChannelDao channelDao;
	
	
	@Override
	public Page<Channel> queryChannelPage(Channel param)
			throws Exception {
		
		Page<Channel> page = new Page<Channel>();
		page.setPageNum(param.getPage());
		page.setPageSize(param.getRows());
		List<Channel> channels = channelDao.selectChannelPage(param);
		StringBuffer payChannelIds = null;
		StringBuffer payChannelNames = null;
		Integer registerCount = 0;
		Integer rechargeCount = 0;
		Integer traderCount=0;
		//Double rechargeAmount = 0.0;
		
		for(Channel channel:channels){
			Channel rechargeInfo = null;
			payChannelIds = new StringBuffer();
			payChannelNames = new StringBuffer();
			
			for(ChargeChannelVo channelVo:this.channelDao.selectPayChannelRelation(channel.getCode())){
				if(payChannelIds.toString().length()>0){
					payChannelIds.append(",");
				}
				
				if(payChannelNames.toString().length()>0){
					payChannelNames.append(",");
				}
				
				payChannelIds.append(channelVo.getChannelId());
				payChannelNames.append(channelVo.getChannelName());
			}
			registerCount = this.channelDao.selectRegisterCount(channel.getCode(),channel.getBrandId());
			rechargeInfo = this.channelDao.selectRechargeCount(channel.getCode());
			traderCount = this.channelDao.selectTraderCount(channel.getCode());
			logger.info("========rechargeInfo={}========",JSONObject.toJSONString(rechargeInfo));
			rechargeCount = rechargeInfo.getRechargeCount();
			channel.setPayChannelIds(payChannelIds.toString());
			channel.setPayChannelNames(payChannelNames.toString());
			channel.setRegisterCount(registerCount);
			channel.setRechargeCount(rechargeCount);
			channel.setRechargeAmount(rechargeInfo.getRechargeAmount()==null?0.0:rechargeInfo.getRechargeAmount());
			channel.setTraderCount(traderCount);
		}
		page.addAll(channels);
		page.setTotal(channelDao.selectChannelCount(param));
		
		return page;
	}

	@Override
	public List<Channel> queryChannel(Channel param)
			throws Exception {
		param.setLimit(9999);
		return channelDao.selectChannelPage(param);
	}

	@Override
	public Integer queryChannelCount(Channel param) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void addChannel(Channel param) throws Exception {
		Channel channel =new Channel();
		channel.setCode(param.getCode());
		List<Channel> channels = channelDao.selectChannelPage(channel);
		if(!channels.isEmpty()){
			throw new LTException(LTResponseCode.MA00032);
		}
		channelDao.insertChannel(param);
	}

	@Override
	public void editChannel(Channel param) throws Exception {
		channelDao.updateChannel(param);
	}

	@Override
	public void removeChannel(Integer id) throws Exception {
		channelDao.deleteChannel(id);
	}

	@Override
	public List<Channel> findChannel(Channel param) {
		return channelDao.selectChannel(param);
	}
	
	@Override
	public Channel getChannel(Integer id) {
		return channelDao.selectChannelById(id);
	}

	@Override
	public void addPayChannelRelation(String channelId, String payChannels) throws LTException {
		try{
			//this.channelDao.deletePayChannelRelation(channelId);
			String[] payChannelArr = payChannels.split(",");
			List<PayAndChannelRelation> channelRelations = FastList.newInstance();
			PayAndChannelRelation channelRelation = null;
			for(String payChannel:payChannelArr){
				channelRelation = new PayAndChannelRelation(channelId,payChannel);
				channelRelations.add(channelRelation);
			}
			this.channelDao.insertPayChannelRelation(channelRelations);
			
		}catch(Exception e){
			e.printStackTrace();
			throw new LTException(LTResponseCode.ER400);
		}
		
	}

	@Override
	public void editPayChannelRelation(String channelId, String payChannels) throws LTException {
		try{
			this.channelDao.deletePayChannelRelation(channelId);
			logger.info("支付渠道ids:{}",payChannels);
			String[] payChannelArr = payChannels.split(",");
			List<PayAndChannelRelation> channelRelations = FastList.newInstance();
			PayAndChannelRelation channelRelation = null;
			for(String payChannel:payChannelArr){
				channelRelation = new PayAndChannelRelation(channelId,payChannel);
				channelRelations.add(channelRelation);
			}
			this.channelDao.insertPayChannelRelation(channelRelations);
			
			/**查询用户id列表**/
			List<String> userIds = this.channelDao.selectUserIds(channelId);
			/**查询已经存在的用户渠道列表**/
			List<UserChargeMapper> chargeMappers = this.channelDao.selectUserChargeMappers(channelId);
			
			/**封装新的用户渠道列表**/
			List<UserChargeMapper> chargeMappers2 = FastList.newInstance();
			for(String payChannel:payChannelArr){
				for(int i=0;i<userIds.size();i++){
					chargeMappers2.add(new UserChargeMapper(userIds.get(i), payChannel));
				}
			}
			chargeMappers2.removeAll(chargeMappers);
			if(CollectionUtils.isNotEmpty(chargeMappers2)){
				this.channelDao.insertUserChargeMapperMutil(chargeMappers2);
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new LTException(LTResponseCode.ER400);
		}		
		
	}
	public List<UserChargeMapper> removeDuplicate(List<UserChargeMapper> list){  
        List<UserChargeMapper> listTemp = new ArrayList<UserChargeMapper>();  
        for(int i=0;i<list.size();i++){  
            if(!listTemp.contains(list.get(i))){  
                listTemp.add(list.get(i));  
            }  
        }  
        return listTemp;  
    }  
	
	@Override
	public void removePayChannelRelation(String channelId) throws LTException {
		try{
			if(StringTools.isNotEmpty(channelId)){
				this.channelDao.deletePayChannelRelation(channelId);				
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new LTException(LTResponseCode.ER400);
		}			
	}
	public static void main(String[] args) {
		List<UserChargeMapper> list1 = FastList.newInstance();
		list1.add(new UserChargeMapper("1", "100"));
		list1.add(new UserChargeMapper("1", "101"));
		list1.add(new UserChargeMapper("1", "102"));
		list1.add(new UserChargeMapper("1", "103"));
		
		List<UserChargeMapper> list2 = FastList.newInstance();
		list2.add(new UserChargeMapper("2", "100"));
		list2.add(new UserChargeMapper("2", "100"));
		list2.add(new UserChargeMapper("1", "101"));
		list2.add(new UserChargeMapper("1", "102"));
		
		list1.removeAll(list2);
		for(UserChargeMapper chargeMapper:list1){
			System.out.println(chargeMapper.getUserId()+","+chargeMapper.getChannelId());
		}
		
	}
}
