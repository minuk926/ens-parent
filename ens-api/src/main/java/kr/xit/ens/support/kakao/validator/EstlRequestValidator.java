package kr.xit.ens.support.kakao.validator;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.xit.ens.support.kakao.model.KkoPayEltrDocDTO;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class EstlRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return KkoPayEltrDocDTO.EstlRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        KkoPayEltrDocDTO.EstlRequest dto = (KkoPayEltrDocDTO.EstlRequest) target;

        if (!StringUtils.hasText(dto.getDocument_binder_uuid())
            ||dto.getDocument_binder_uuid().length() < 10
            ||dto.getDocument_binder_uuid().length() > 40) {
            errors.rejectValue("document_binder_uuid", "required", "카카오페이 문서식별번호(max:40)");
        }

        if (!StringUtils.hasText(dto.getToken())
                ||dto.getToken().length() < 10
                ||dto.getToken().length() > 50) {
            errors.rejectValue("token", "required", "카카오페이 전자문서 서버로 부터 받은 토큰(max:50)");
        }

        if (StringUtils.hasText(dto.getExternal_document_uuid()) && dto.getExternal_document_uuid().length() > 40){
            errors.rejectValue("external_document_uuid", "", "외부 문서 식별 번호(max:40)");
        }
    }
}
