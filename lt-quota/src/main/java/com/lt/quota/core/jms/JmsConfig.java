package com.lt.quota.core.jms;

import com.lt.quota.core.constant.IConstants;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

/**
 * 创建：cndym
 * 时间：2017/5/3 7:15
 */
@Configuration
@EnableJms
public class JmsConfig {

    @Bean//配置一个消息通道
    public Queue userOnlineQueue() {
        return new ActiveMQQueue(IConstants.QUEUE_USER_ONLINE_LOG);
    }


    @Bean
    public JmsListenerContainerFactory<?> topicFactory(ConnectionFactory connectionFactory,
                                                       DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setPubSubDomain(true);
        return factory;
    }
}
