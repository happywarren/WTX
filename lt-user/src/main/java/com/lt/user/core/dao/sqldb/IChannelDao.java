package com.lt.user.core.dao.sqldb;

import com.lt.model.user.Channel;
import org.springframework.data.repository.query.Param;

public interface IChannelDao {

    Channel getChannelByCode(@Param("code") String code);
}
