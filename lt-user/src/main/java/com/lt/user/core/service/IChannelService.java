package com.lt.user.core.service;

import com.lt.model.user.Channel;

public interface IChannelService {

    Channel getChannelByCode(String code);

    String getChannelCode(String code,String deviceType);

}
