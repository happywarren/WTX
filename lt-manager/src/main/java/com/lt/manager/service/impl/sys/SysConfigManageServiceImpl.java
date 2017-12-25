package com.lt.manager.service.impl.sys;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lt.manager.dao.sys.SysConfigManageDao;
import com.lt.manager.service.product.IProductManageService;
import com.lt.manager.service.sys.ISysConfigManageService;
import com.lt.model.sys.SysConfig;
import com.lt.util.error.LTException;
import com.lt.constant.redis.RedisUtil;

@Service
public class SysConfigManageServiceImpl implements ISysConfigManageService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private SysConfigManageDao SysConfigManageDao;
	@Autowired
	private IProductManageService productManageServiceImpl;


    @Override
    public void loadSysCfgToRedis() throws Exception {

        Logger logger = LoggerFactory.getLogger(getClass());
        //系统配置
        BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);

        //获取所有系统配置
        List<SysConfig> list = SysConfigManageDao.selectSysConfigs();

        for (SysConfig cfg : list) {
            logger.debug("加载配置:name={};key={};value={}", cfg.getReleaseName(), cfg.getCfgCode(), cfg.getCfgValue());
            sysCfgRedis.put(cfg.getCfgCode(), cfg.getCfgValue());
        }
    }


	@Override
	public SysConfig querySysConfigByCode(String cfgCode) throws LTException {
		try{
			SysConfig sysConfig = this.SysConfigManageDao.selectSysConfigByCode(cfgCode);
			if(sysConfig==null){
				throw new LTException("当前配置信息不存在!");
			}
			return sysConfig;
		}catch(Exception exception){
			exception.printStackTrace();
			throw new LTException("查询配置信息失败异常");
		}
	}


	@Override
	public void updateSysConfig(SysConfig sysConfig) throws LTException {
		try{
			if(this.SysConfigManageDao.updateSysConfig(sysConfig)<1){
				throw new LTException("更新配置信息失败");
			}
			BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
			sysCfgRedis.put(sysConfig.getCfgCode(), sysConfig.getCfgValue()+"-"+sysConfig.getReleaseName());
		}catch(Exception exception){
			exception.printStackTrace();
			throw new LTException("更新配置信息异常");
		}
	}



	@Override
	public List<SysConfig> fuzzyQuerySysConfigByCfgCode(List<String> cfgCodes) throws LTException {
		List<SysConfig> sysConfigs = null;
		try{
			sysConfigs = this.SysConfigManageDao.fuzzySelectSysConfig(cfgCodes);
			BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
			if(CollectionUtils.isNotEmpty(sysConfigs)){
				for(SysConfig sysConfig:sysConfigs){
					sysCfgRedis.put(sysConfig.getCfgCode(), sysConfig.getCfgValue());	
				}
			}else{
				throw new LTException("查询配置信息异常");
			}
		}catch(Exception exception){
			exception.printStackTrace();
			throw new LTException("查询配置信息异常");
		}
		return sysConfigs;

	}


	@Override
	public void updateSysConfigList(List<SysConfig> sysConfigs) throws LTException {
		try{
			if(this.SysConfigManageDao.updateSysConfigList(sysConfigs)<1){
				throw new LTException("更新配置信息失败");
			}
			BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
			for(SysConfig sysConfig:sysConfigs){
				sysCfgRedis.put(sysConfig.getCfgCode(), sysConfig.getCfgValue());	
			}			
		}catch(Exception exception){
			exception.printStackTrace();
			throw new LTException("更新配置信息异常");
		}
		
	}


	@Override
	@Transactional
	public void modifyWinterSummerChange(String value) {
		SysConfig config = new SysConfig();
		config.setCfgCode("winter_summer_change");
		config.setCfgValue(value);		
		SysConfigManageDao.updateSysConfig(config);
		if(value.equals("1")){//夏令时
			productManageServiceImpl.updateTimeSummer();
		}else{//冬令时
			productManageServiceImpl.updateTimeWinter();
		
		}
		BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
		sysCfgRedis.put(config.getCfgCode(), config.getCfgValue());
		
	}

}
