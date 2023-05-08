package kr.xit.core.support.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.xit.core.exception.BizRuntimeException;

/**
 * JSON Util Class
 */
public class JsonUtils {

	/**
	 * Object 를 json string으로 변환
	 * @return String
	 * @param obj Object
	 */
	public static String toJson(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
//		mapper.setSerializationInclusion(Include.NON_EMPTY);
		try {
			return obj != null ? mapper.writeValueAsString(obj) : null;
		} catch (JsonProcessingException e) {
			throw BizRuntimeException.create(e.getLocalizedMessage());
		}
	}

	/**
	 * Json string을 지정된 class로 변환
	 * @return T
	 * @param str String
	 * @param cls Class
	 */
	public static <T> T toObject(String str, Class<T> cls) {
		ObjectMapper om = new ObjectMapper();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			return str != null ? om.readValue(str, cls) : null;
		} catch (JsonProcessingException e) {
			throw BizRuntimeException.create(e.getLocalizedMessage());
		}
	}

	/**
	 * Json string을 지정된 class로 변환
	 * @param obj Object
	 * @param cls Class
	 * @return T
	 */
	public static <T> T toObjByObj(Object obj, Class<T> cls) {
		String	str	= toJson(obj);
		ObjectMapper om = new ObjectMapper();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			return str != null ? om.readValue(str, cls) : null;
		} catch (JsonProcessingException e) {
			throw BizRuntimeException.create(e.getLocalizedMessage());
		}
	}

	/**
	 * Json string을 지정된 class list로 변환
	 * @return		T
	 * @param		str
	 * @param		cls
	 * @throws IOException
	 */
	public static <T> List<T> toObjectList(String str, Class<T> cls) {
		ObjectMapper om = new ObjectMapper();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		if(str != null){
			try {
				return om.readValue(str, om.getTypeFactory().constructCollectionType(List.class, cls));
			} catch (JsonProcessingException e) {
				throw BizRuntimeException.create(e.getLocalizedMessage());
			}
		}else {
			return null;
		}
	}

	/**
	 * JSON 문자열을 Map 구조체로 변환
	 * @param str String	str
	 * @return Map
	 */
	public static Map<String, Object> toMap(String str) {
		Map<String, Object>	map	= null;
		ObjectMapper om = new ObjectMapper();
		try {
			map	= om.readValue(str, new TypeReference<Map<String, Object>>(){});
		} catch (JsonProcessingException e) {
			throw BizRuntimeException.create(e.getLocalizedMessage());
		}
		return map;
	}

	/**
	 * Json 데이터 보기 좋게 변환.
	 * @param obj Object	json
	 * @return String
	 */
	public static String jsonEnterConvert(Object obj) {
		String	rltStr;

		try {
			rltStr	= jsonEnterConvert((JsonUtils.toJson(obj)));
		} catch(Exception e) {
			rltStr	= "";
		}

		return rltStr;
	}

	/**
	 * Json 데이터 보기 좋게 변환.
	 * @param json String	json
	 * @return	String
	 */
	private static String jsonEnterConvert(String json) {

		if( json == null || json.length() < 2 )
			return json;

		final int len = json.length();
		final StringBuilder sb = new StringBuilder();
		char c;
		String tab = "";
		boolean beginEnd = true;
		for( int i=0 ; i<len ; i++ ) {
			c = json.charAt(i);
			switch( c ) {
				case '{': case '[':{
					sb.append( c );
					if( beginEnd ){
						tab += "\t";
						sb.append("\n");
						sb.append( tab );
					}
					break;
				}
				case '}': case ']':{
					if( beginEnd ){
						tab = tab.substring(0, tab.length()-1);
						sb.append("\n");
						sb.append( tab );
					}
					sb.append( c );
					break;
				}
				case '"':{
					if( json.charAt(i-1)!='\\' )
						beginEnd = ! beginEnd;
					sb.append( c );
					break;
				}
				case ',':{
					sb.append( c );
					if( beginEnd ){
						sb.append("\n");
						sb.append( tab );
					}
					break;
				}
				default :{
					sb.append( c );
				}
			} // switch end

		}
		if( sb.length() > 0 )
			sb.insert(0, '\n');
		return sb.toString();
	}
}

