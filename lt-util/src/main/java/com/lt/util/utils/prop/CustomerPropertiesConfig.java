package com.lt.util.utils.prop;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class CustomerPropertiesConfig extends PropertyPlaceholderConfigurer {

	private Properties props;

	@Override
	protected void processProperties(
			ConfigurableListableBeanFactory beanFactoryToProcess,
			Properties props) throws BeansException {
		super.processProperties(beanFactoryToProcess, props);
		this.props = props;
	}

	public String getProperty(String key) {
		
		String value = props.getProperty(key);
		if(value != null && value.length() > 0){
			value = value.trim();
		}
		return value;
	}
	public void setPropertiesUpdate(Properties propUpdate){
		Iterator it=propUpdate.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry entry=(Map.Entry)it.next();
		    String key = (String) entry.getKey();
		    String value = (String) entry.getValue();
		    props.put(key, value);
		} 
	}
	
}
