# 전자정부 프레임워크 호환성 가이드
[호환성가이드 문서](./document/egov_compatibility_guide.pdf)

### Index
[1. 실행환경 변경 금지](#실행환경-변경-금지)   
[2. 설정파일 위치 규칙](#설정파일-위치-규칙)   
[3. 데이터 액세스 아키텍처 규칙](#데이터-액세스-아키텍처-규칙)   
[4. MVC 아키텍처 규칙](#MVC-아키텍처-규칙)   
[5. 서비스 아키텍처 규칙](#서비스-아키텍처-규칙)   
[6. 실행환경 확장 규칙](#실행환경-확장-규칙)   
[7. 표준프레임워크 활용 규칙](#표준프레임워크-활용-규칙)

#### 실행확경 변경 금지
```text
# 필수 dependency
egovframework.rte.ptl.mvc.jar
egovframework.rte.fdl.cmmn.jar
egovframework.rte.psl.dataaccess.jar
egovframework.rte.fdl.logging.jar


```

#### 설정파일 위치 규칙
```text
# 설정파일은 프로젝트 루트에 위히할 수 없다
# 설정파일은 특정 위치에 존재하여야 한다
# 설정파일들은 공통적인 상위 디렉토리를 가져야 한다
```

#### 데이터 액세스 아키텍처 규칙
```text
# ibatis - EgovAbstractDAO 상속
# mybatis - EgovAbstractMapper 상속
# mybatis Mapper Interface 방식 사용시
  - MapperConfigurer 설정시 프레임워크에서 제공하는 MapperConfigurer와 @Mapper 사용
  - 프로젝트에 부적합한 경우 해당 클래스를 상속받아 구현 가능
```

#### MVC 아키텍처 규칙
```text
# Ibatis SqlMapClientDaoSupport, Mybatis SqlSessionDaoSupport 클래스 메소드 호출 불가
```

#### 서비스 아키텍처 규칙
```text
# EgovAbstractServiceImpl 상속 받아야 한다
# 특정 인터페이스를 구현하여야 한다
```

#### 실행환경 확장 규칙
```text
# egovframework.rte 패키지에 속한 클래스를 상속받은 클래스는 모두 대상
# egovframework.rte 패키지 내에 정의될 수 없다
# Egov라는 이름으로 시작할 수 없다
```

#### 표준프레임워크 활용 규칙
```text
# 한 개 이상의 실행환경 라이브러리가 존재
# 한 개 이상의 DAO 클래스
# 한 개 이상의 Service 클래스
```
