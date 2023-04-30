package kr.xit.core.support.mybatis;

import java.util.LinkedHashMap;

import org.springframework.jdbc.support.JdbcUtils;

import ch.qos.logback.core.filter.Filter;

/**
 * <pre>
 * description : CamelCase LinkedHashMap
 * packageName : kr.xit.core.support.mybatis
 * fileName    : CamelCaseLinkedMap
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 * @see LinkedHashMap
 */
public class CamelCaseLinkedMap extends LinkedHashMap {
	
	@Override
	public Object put(Object key, Object value){
		if(key != null && key.toString().contains("_"))		return super.put(JdbcUtils.convertUnderscoreNameToPropertyName((String)key), value);
		else                                                return super.put(key, value);
	}
}
