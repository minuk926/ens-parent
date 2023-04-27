package kr.xit.core.support.utils;

import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ConvertHelper {
	private static final Logger log = LoggerFactory.getLogger(ConvertHelper.class);
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	private static final JsonFactory JSON_FACTORY = new JsonFactory();
	
	public static String jsonToObject(Object o){
		JsonGenerator generator = null;
		StringWriter writer = new StringWriter();
		
		try{
			OBJECT_MAPPER.registerModule(new JodaModule());
			generator = JSON_FACTORY.createGenerator(writer);
			OBJECT_MAPPER.writeValue(generator, o);
			generator.flush();
			return writer.toString();
		}catch(Exception e){
			log.error("ConvertHelper::jsonToObject", e);
			return null;
		}finally{
			if(generator != null){
				try{
					generator.close();
				}catch(Exception e){
					log.error("InternalServerError: {}", e.getLocalizedMessage());
				}
			}
		}
	}
	
	public static <T> T objectToJson(String json, Class<T> clazz){
		try{
			OBJECT_MAPPER.registerModule(new JodaModule());
			return OBJECT_MAPPER.readValue(json, clazz);
		}catch(Exception e){
			log.error("ConvertHelper::objectToJson", e);
			return null;
		}
	}
}
