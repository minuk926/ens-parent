package kr.xit.core.support.mybatis;

import java.util.HashMap;

import org.springframework.jdbc.support.JdbcUtils;

public class CamelCaseMap extends HashMap {
	
	@Override
	public Object put(Object key, Object value){
		if(key != null && key.toString().contains("_"))		return super.put(JdbcUtils.convertUnderscoreNameToPropertyName((String)key), value);
	    else                                                return super.put(key, value);
	}
}
