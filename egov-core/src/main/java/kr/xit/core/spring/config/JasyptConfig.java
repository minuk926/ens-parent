package kr.xit.core.spring.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptConfig {
    @Value("${app.jasypt.secretKey}")
    private String SECRET_KEY;

    @Value("${app.jasypt.alg}")
    private String SECRET_ALG;

    @Value("${app.jasypt.type}")
    private String STRING_TYPE;

    @Bean(name = "jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        // 암/복호화 키
        config.setPassword(SECRET_KEY);
        // 암/복호화 알고리즘
        config.setAlgorithm(SECRET_ALG);
        // 암/복호화 인스턴스 : 0보다 커야
        config.setPoolSize("1");
        config.setStringOutputType(STRING_TYPE);

        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setConfig(config);
        return encryptor;
    }


    public static void main(String[] args) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword("xit5811807!@");
        encryptor.setStringOutputType("base64");
        String url = encryptor.encrypt("jdbc:log4jdbc:mariadb://211.119.124.122:3306/xplatform?useUnicode=true&characterEncoding=utf8serverTimezone=Asia/Seoul&useSSL=false");
        String id = encryptor.encrypt("root");
        String passwd = encryptor.encrypt("xit5811807");
        String decryptedPass = encryptor.decrypt(url);
        System.out.println(url);
        System.out.println(id);
        System.out.println(passwd);
        System.out.println(decryptedPass);
    }
}
