package com.lt.util.utils.redis;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class RedisJsonSerializer<T> implements RedisSerializer<T>{

	/**
	 * 用于simple-spring-memached的序列化
	 */
	@SuppressWarnings("unused")
	private static final int FAST_JSON_SERIALIZED_FLAG = 16;

	private static final SerializerFeature[] features = {
			SerializerFeature.WriteEnumUsingToString,
			SerializerFeature.SortField, SerializerFeature.SkipTransientField,
			SerializerFeature.WriteClassName };

	private static final Feature[] DEFAULT_PARSER_FEATURE = {
			Feature.AutoCloseSource, Feature.InternFieldNames,
			Feature.AllowUnQuotedFieldNames, Feature.AllowSingleQuotes,
			Feature.AllowArbitraryCommas, Feature.SortFeidFastMatch,
			Feature.IgnoreNotMatch };

	@Override
	public byte[] serialize(Object t) throws SerializationException {
		return JSON.toJSONBytes(t, features);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T deserialize(byte[] bytes) throws SerializationException {
		if (bytes == null)
			return null;
		return (T) JSON.parse(bytes, DEFAULT_PARSER_FEATURE);
	}

}
