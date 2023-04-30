package egovframework.com.cmm;

import org.egovframe.rte.fdl.cmmn.exception.handler.ExceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EgovComOthersExcepHndlr implements ExceptionHandler {

    public void occur(Exception e, String packageName) {
    	//log.debug(" EgovServiceExceptionHandler run...............");
    	//LOGGER.error(packageName, e);
    	log.error("{} error: {}", packageName, e.getMessage());
    }
}
