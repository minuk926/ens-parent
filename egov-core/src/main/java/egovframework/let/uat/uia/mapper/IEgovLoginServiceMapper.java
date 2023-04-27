package egovframework.let.uat.uia.mapper;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

import egovframework.com.cmm.LoginVO;

@Mapper
public interface IEgovLoginServiceMapper {
    LoginVO actionLogin(LoginVO vo);
    // LoginVO searchId(LoginVO vo);
    // LoginVO searchPassword(LoginVO vo);
    // void updatePassword(LoginVO vo);
}
