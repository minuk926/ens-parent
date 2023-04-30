package kr.xit.core.support.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mybatis.spring.SqlSessionFactoryBean;

/**
 * <pre>
 * description : null or empty check utility
 * packageName : kr.xit.core.support.utils
 * fileName    : Checks
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 */
public class Checks {
	/**
	 * val이 null이 아니면 그대로 리턴, 아니면 defVal
	 * 
	 * @param val T
	 * @param defVal T
	 * @return T
	 */
	public static < T > T checkVal( T val, T defVal ) {
		return ( isNotNull( val ) ? val : defVal );
	}
	
	/**
	 * val이 Empty가 아니면 그대로 리턴, 아니면 defVal (Empty는 배열이나 Map도 가능)
	 * 
	 * @param val T
	 * @param defVal T
	 * @return T
	 */
	public static < T > T checkEmptyVal( T val, T defVal ) {
		return ( isNotEmpty( val ) ? val : defVal );
	}

	/**
	 * expression이 true이면 val, 아니면 defVal
	 * 
	 * @param expression boolean
	 * @param tVal T
	 * @param fVal T
	 * @return T
	 */
	public static < T > T checkVal( boolean expression, T tVal, T fVal ) {
		return ( expression ? tVal : fVal );
	}
	
	
	public static boolean isNull( Object checkValue ) {
		return !isNotNull(checkValue);
	}

	public static boolean isNotNull( Object checkValue ) {
		return checkValue != null;
	}

	public static boolean isEmpty( Object checkValue ) {
		return !isNotEmpty(checkValue);
	}

	public static boolean isNotEmpty( Object checkValue ) {
		if ( isNotNull( checkValue ) ) {

			if ( checkValue instanceof String ) {
				return !( ( String ) checkValue ).isEmpty();
			}
			else if ( checkValue instanceof Iterator< ? > ) {
				return ( ( Iterator< ? > ) checkValue ).hasNext();
			}
			else if ( checkValue instanceof Iterable< ? > ) {
				return ( ( Iterable< ? > ) checkValue ).iterator().hasNext();
			}
			else if ( checkValue instanceof Map< ?, ? > ) {
				return !( ( Map< ?, ? > ) checkValue ).isEmpty();
			}
//			else if ( checkValue instanceof DataSetMap ) {
//				return !( ( DataSetMap ) checkValue ).isEmpty();
//			}

			if ( checkValue.getClass().isArray() ) {
				return ( ( Object[] ) checkValue ).length != 0;
			}
			else {
				// 체크하는 항목이 아니면서 not null 이면 not empty로 간주한다.
				return true;
			}
		}

		return false;
	}

	public static boolean isNumeric( String str )
	{
		if ( Checks.isEmpty( str ) )
			return false;

		return str.matches( "-?\\d+(.\\d+)?" );
	}

	public static boolean isInstance( Object checkValue, Class< ? >... classes ) {
		if ( isNotNull( checkValue ) ) {
			for ( Class< ? > clazz : classes ) {
				if ( clazz.isInstance( checkValue ) ) {
					return true;
				}
			}
		}

		return false;
	}
	
	/**
	 * email형식 체크
	 * 
	 * @메소드 : isCheckEmail
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isCheckEmail( String str ) {
		if ( Checks.isEmpty( str ) )
			return false;
		
		String regex = "^([\\w-]+(?:\\.[\\w-]+)*)@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)$";
		Pattern pattern = Pattern.compile( regex, Pattern.CASE_INSENSITIVE );
		
		Matcher matcher = pattern.matcher( str );
		return matcher.find();
	}
	
	/**
	 * 전화형식 체크
	 * 
	 * @메소드 : isCheckTel
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isCheckTel( String str ) {
		if ( Checks.isEmpty( str ) )
			return false;
		
		String regex = "^\\d[\\d-]+\\d$";
		Pattern pattern = Pattern.compile( regex, Pattern.MULTILINE );
		
		Matcher matcher = pattern.matcher( str );
		return matcher.find();
	}
}
