package kr.xit.core.spring.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.MediaType;

import lombok.extern.slf4j.Slf4j;

/**
 * POST request parameter logging
 *
 */
@Slf4j
public class ReadableRequestWrapper extends HttpServletRequestWrapper {
    private final Charset encoding;
    private byte[] rawData;
    private Map<String, String[]> params = new HashMap<>();

    public ReadableRequestWrapper(HttpServletRequest request) {
        super(request);
        // 원래의 파라미터를 저장
        this.params.putAll(request.getParameterMap());

        // 인코딩 설정
        String charEncoding = request.getCharacterEncoding();
        this.encoding = StringUtils.isBlank(charEncoding) ? StandardCharsets.UTF_8 : Charset.forName(charEncoding);

        try {
            InputStream is = request.getInputStream();
            // InputStream 을 별도로 저장한 다음 getReader() 에서 새 스트림으로 생성
            this.rawData = IOUtils.toByteArray(is);

            // body 파싱
            String collect = this.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

            // body 가 없을경우 로깅 제외
            if (StringUtils.isEmpty(collect)) {
                return;
            }

            // 파일 업로드시 로깅제외
            if (request.getContentType() != null && request.getContentType().contains(
                MediaType.MULTIPART_FORM_DATA_VALUE)) {
                return;
            }
            JSONParser jsonParser = new JSONParser();
            Object parse = jsonParser.parse(collect);
            if (parse instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray)jsonParser.parse(collect);
                setParameter("requestBody", jsonArray.toJSONString());
            } else {
                JSONObject jsonObject = (JSONObject)jsonParser.parse(collect);
                Iterator iterator = jsonObject.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = (String)iterator.next();
                    setParameter(key, jsonObject.get(key).toString().replace("\"", "\\\""));
                }
            }
        } catch (Exception e) {
            log.error("ReadableRequestWrapper init error", e);
        }
    }

    @Override
    public String getParameter(String name) {
        String[] paramArray = getParameterValues(name);
        if (paramArray != null && paramArray.length > 0) {
            return paramArray[0];
        } else {
            return null;
        }
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return Collections.unmodifiableMap(params);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(params.keySet());
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] result = null;
        String[] dummyParamValue = params.get(name);

        if (dummyParamValue != null) {
            result = new String[dummyParamValue.length];
            System.arraycopy(dummyParamValue, 0, result, 0, dummyParamValue.length);
        }
        return result;
    }

    public void setParameter(String name, String value) {
        String[] param = {value};
        setParameter(name, param);
    }

    public void setParameter(String name, String[] values) {
        params.put(name, values);
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.rawData);

        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                // Do nothing
            }

            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream(), this.encoding));
    }
}
