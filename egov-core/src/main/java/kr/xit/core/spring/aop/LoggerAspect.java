package kr.xit.core.spring.aop;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
public class LoggerAspect {
    @Value("${app.result.log.trace:false}")
    private boolean isRsltLog;

    @Pointcut("execution(public * egovframework..web.*.*(..)) || execution(public * kr.xit..web.*.*(..))")
    public void loggerPointCut() {
    }

    @Around("loggerPointCut()")
    public Object methodLogger(ProceedingJoinPoint pjp) throws Throwable {
        try {
            HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
            requestLog(request);

            Object result = pjp.proceed();
            if(isRsltLog) {
                StringBuilder sb = new StringBuilder();
                sb.append("\n//==============================Result====================================").append("\n");
                sb.append("Execution : ").append(pjp.getSignature()).append("\n");
                sb.append("return : ").append(result).append("\n");
                sb.append("=========================================================================//");
                log.debug(sb.toString());
            }else {
            }

            // FIXME:: 페이징 정보 set
            // Object pageObject = RequestContextHolder.getRequestAttributes().getAttribute(PagingConstants.Session.PAGE_INFO.getCode(), RequestAttributes.SCOPE_REQUEST);
            // if(pageObject != null && result != null){
            //     return setPaginator(pageObject, result);
            // }

            return result;

        } catch (Throwable throwable) {
            throw throwable;
        }
    }

    private void requestLog(HttpServletRequest request) {

        if (log.isDebugEnabled()) {
            String method = request.getMethod();
            StringBuilder sb = new StringBuilder("\n");
            sb.append("//=========================================================================\n");
            //sb.append("Ajax Call : " + "XMLHttpRequest".equals(request.getHeader(Globals.AJAX_HEADER))).append("\n");
            sb.append("URI : " + request.getRequestURI()).append("\n");
            sb.append("URL : " + request.getRequestURL()).append("\n");
            sb.append("IP : " + request.getRemoteAddr()).append("\n");
            sb.append("Referer URI : " + request.getHeader("referer")).append("\n");
            sb.append("Method : " + request.getMethod()).append("\n");
            sb.append("User Agent : " + request.getHeader("User-Agent")).append("\n");
            sb.append("Session : " + request.getSession().getId()).append("\n");
            sb.append("Locale : " + request.getLocale().getCountry()).append("\n");
            sb.append("ContentType : " + request.getContentType()).append("\n");

            sb.append("----- Parameters ----- \n");

            Enumeration<?> e = request.getParameterNames();
            if (e.hasMoreElements()) {
                String pName = "";
                String pValue = "";
                do {
                    pName = (String) e.nextElement();
                    pValue = request.getParameter(pName);
                    sb.append(pName + ": " + pValue + "\n");
                } while (e.hasMoreElements());
            } else {
                sb.append(" is Empty \n");
            }
            sb.append("=========================================================================//");

            log.debug(sb.toString());
            sb = null;
        }
    }

    // private Object setPaginator(Object pageObject, Object result){
    //     Paginator paginator = (Paginator)pageObject;
    //
    //     log.debug("###################################{}",result.getClass());
    //
    //     //if(rtnObj instanceof Map){
    //     if(Map.class.isAssignableFrom(result.getClass())){
    //         Map map = (Map)result;
    //         map.putAll(getPageInfoMap(paginator));
    //         return map;
    //
    //         //}else if(rtnObj instanceof List){
    //     }else if(List.class.isAssignableFrom(result.getClass())){
    //         Map<String,Object> map = new HashMap<String,Object>();
    //         map.put("rows", result);
    //         map.putAll(getPageInfoMap(paginator));
    //         return map;
    //
    //     }else if(ModelAndView.class.isAssignableFrom(result.getClass())){
    //         ModelAndView mav = (ModelAndView)result;
    //         ((Map)mav.getModelMap().get("data")).put("pagination", getPageInfoMap(paginator));
    //         return mav;
    //
    //     }else if(Model.class.isAssignableFrom(result.getClass())){
    //         Model mav = (Model)result;
    //         mav.addAttribute("pagination", getPageInfoMap(paginator));
    //         return mav;
    //
    //     }else if(String.class.isAssignableFrom(result.getClass())){
    //         return result;
    //
    //     }else{
    //         throw new IllegalArgumentException("Paging works Illegal argument type, must be : ModelAndView, Map, List:" + rtnObj.getClass());
    //     }
    // }
    //
    //
    // private Map<String,Object> getPageInfoMap(Paginator paginator){
    //     Map<String, Object> map = new HashMap<String, Object>();
    //     map.put("totalPage", paginator.getTotalPages());
    //     map.put("totalCount", paginator.getTotalCount());
    //     map.put("page", paginator.getPage());
    //     return map;
    // }

}
