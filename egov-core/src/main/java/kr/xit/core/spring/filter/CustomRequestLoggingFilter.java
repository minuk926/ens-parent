package kr.xit.core.spring.filter;

import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

/**
 * <pre>
 * description : body가 소모되지 않은 경우 강제로 로그 기록
 *               - 400 bad request 등으로 헤더 혹은 파라미터만 읽고 처리가 완료될 경우
 *               =>  body 가소모되지 않아 CommonsRequestLoggingFilter를 통한 log가 남지 않는다
 * packageName : kr.xit.core.spring.filter
 * fileName    : CustomRequestLoggingFilter
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 * @see CommonsRequestLoggingFilter
 */
public class CustomRequestLoggingFilter extends CommonsRequestLoggingFilter {
    CatchLocation catchLocation = CatchLocation.AFTER;

    MessageMaker messageMaker;

    enum CatchLocation {
        ALL, BEFORE, AFTER
    }

    @Override
    protected boolean shouldLog(HttpServletRequest request) {
        return logger.isInfoEnabled();
    }

    public CatchLocation getCatchLocation() {
        return catchLocation;
    }

    public void setCatchLocation(CatchLocation catchLocation) {
        this.catchLocation = catchLocation;
    }

    public void setMessageMaker(MessageMaker messageMaker) {
        this.messageMaker = messageMaker;
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        if(catchLocation == CatchLocation.BEFORE || catchLocation == CatchLocation.ALL) {
            logger.info(message);
        }
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        if(request instanceof ContentCachingRequestWrapper) {
            try (ServletInputStream is = request.getInputStream()) {
                if(is.available() >= 0) {
                    while(true) {
                        int read = is.read(new byte[8096]);
                        if (read < 8096) {
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                //throw new RuntimeException(e);
                logger.error(e.getMessage());
            }
        }
        if(catchLocation == CatchLocation.AFTER || catchLocation == CatchLocation.ALL) {
            logger.info(message);
        }
    }

    @Override
    protected String createMessage(HttpServletRequest request, String prefix, String suffix) {
        if(this.messageMaker != null) {
            return this.messageMaker.make(request, prefix, suffix);
        } else {
            return super.createMessage(request, prefix, suffix);
        }
    }

    public interface MessageMaker {
        String make(HttpServletRequest request, String prefix, String suffix);
    }

}
