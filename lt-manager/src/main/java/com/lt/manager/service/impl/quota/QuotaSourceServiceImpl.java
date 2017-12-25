package com.lt.manager.service.impl.quota;

import com.lt.manager.bean.quota.QuotaSourceVo;
import com.lt.manager.dao.quota.QuotaSourceDao;
import com.lt.manager.service.quota.IQuotaSourceService;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuotaSourceServiceImpl implements IQuotaSourceService {

    @Autowired
    private QuotaSourceDao quotaSourceDao;

    @Override
    public List<QuotaSourceVo> listQuotaSource(String type) {
        return quotaSourceDao.listQuotaSource(type);
    }

    @Override
    public void insertQuotaSource(QuotaSourceVo param) {
        QuotaSourceVo quotaSourceVo = quotaSourceDao.getQuotaSource(param);
        if (StringTools.isNotEmpty(quotaSourceVo)){
            throw new LTException(LTResponseCode.US00001);
        }
        quotaSourceDao.insertQuotaSource(param);
    }

    @Override
    public QuotaSourceVo getQuotaSource(Long id) {
        return quotaSourceDao.getQuotaSourceById(id);
    }

    @Override
    public void startQuotaSource(Long id) {
        quotaSourceDao.startQuotaSource(id);
    }

    @Override
    public void endQuotaSource(Long id) {
        quotaSourceDao.endQuotaSource(id);
    }

    @Override
    public void deleteQuotaSource(Long id) {
        quotaSourceDao.deleteQuotaSource(id);
    }
}
