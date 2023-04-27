package kr.xit.core.api;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.google.gson.GsonBuilder;

import egovframework.com.cmm.ResponseCode;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.xit.core.support.utils.Checks;
import kr.xit.core.support.utils.ConvertHelper;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "RestApiResult", description = "Restful API 결과", implementation = IRestApiResponse.class)
@Getter
@Setter
@EqualsAndHashCode
public class RestApiResponse<T> implements IRestApiResponse, Serializable {
    private static final long SerialVersionUID = 1L;

    @Schema(name = "true: 성공, false:실패", example = "true", description = "에러인 경우 false", requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean success = true;
    private String code = String.valueOf(ResponseCode.SUCCESS.getCode());

    @Schema(name = "메세지", description = "오류 발생시 오류 메세지", example = " ", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message = ResponseCode.SUCCESS.getMessage();

    @Schema(name = "결과값", description = "오류시 null", example = " ")
    private T data;

    @Schema(name = "데이타 수", description = "API 실행 결과 데이타 수")
    private int count;

    @Schema(name = "페이징 정보", description = "결과값이 Collection type인 경우 사용됨", example = " ")
    private PaginationInfo paginationInfo;

    private RestApiResponse(){}

    private RestApiResponse(T data) {
        this.data = data;

        if(data == null){
            this.count = 0;

        }else {

            if (Collection.class.isAssignableFrom(data.getClass())) {
                this.count = (((Collection<?>) data).size());

            } else {
                this.count =  1;
            }
        }
    }

    public static <T> ResponseEntity<? extends IRestApiResponse> of(){
        return ResponseEntity.ok().body(new RestApiResponse<>());
    }

    public static <T> ResponseEntity<? extends IRestApiResponse> of(T data){
        return of(data, null, null);
    }

    public static <T> ResponseEntity<? extends IRestApiResponse> of(T data, String code){
        return of(data, code, getResponseCode(code).getMessage());
    }

    public static <T> ResponseEntity<? extends IRestApiResponse> of(String code){
        return of(null, code, getResponseCode(code).getMessage());
    }

    public static <T> ResponseEntity<? extends IRestApiResponse> of(String code, String message){
        return of(null, code, message);
    }

    public static <T> ResponseEntity<? extends IRestApiResponse> of(T data, String code, String message){
        RestApiResponse result = new RestApiResponse(data);
        if(Checks.isNotEmpty(code))     result.setCode(code);
        if(Checks.isNotEmpty(message))  result.setMessage(message);
        return ResponseEntity.ok().body(result);
    }

    public static <T> ResponseEntity<? extends IRestApiResponse> of(String name, T data){
        Map<String, T> map = new HashMap<>();
        map.put(name, data);
        return ResponseEntity.ok().body(new RestApiResponse<>(map));
    }

    public static <T> ResponseEntity<? extends IRestApiResponse> of(HttpStatus httpStatus){
        RestApiResponse result = new RestApiResponse<>();
        return new ResponseEntity<>(result, httpStatus);
    }

    public static <T extends IRestApiResponse> RestApiResponse result(){
        return new RestApiResponse<>();
    }

    public static <T extends IRestApiResponse> RestApiResponse result(T data){
         return new RestApiResponse<>(data);
    }

    @Override
    public String toString() {
        // value가 null값인 경우도 생성
        GsonBuilder builder = new GsonBuilder().serializeNulls();
        builder.disableHtmlEscaping();
        return builder.setPrettyPrinting().create().toJson(this);
    }

    public String convertToJson() {
        return ConvertHelper.jsonToObject(this);
    }

    public String asToString(RestApiResponse<T> t) {
        GsonBuilder builder = new GsonBuilder().serializeNulls();       // value가 null값인 경우도 생성
        builder.disableHtmlEscaping();
        return builder.setPrettyPrinting().create().toJson(t);
    }

    private static ResponseCode getResponseCode(String code){
        return Arrays.stream(ResponseCode.values())
            .filter(responseCode -> String.valueOf(responseCode.getCode()).equals(code))
            .findAny()
            .orElseGet(() -> ResponseCode.EMPTY_MESSAGE);
    }
}
