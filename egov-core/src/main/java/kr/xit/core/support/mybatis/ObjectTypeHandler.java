package kr.xit.core.support.mybatis;

import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class ObjectTypeHandler extends BaseTypeHandler<Object> {
	
	@Override
	  public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType)
	      throws SQLException {
	    ps.setObject(i, parameter);
	  }

	  @Override
	  public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
		  Object value = "";
		  Object object = rs.getObject(columnName);
		  value = object;
		  if(object instanceof Clob) {
			  
			  Clob clob = (Clob) object;
				
				if (clob != null) {
					try {
						int size = (int) clob.length();
						value = clob.getSubString(1, size);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
		  } 
		  else if (object instanceof java.sql.Date ) {
  			  Timestamp sqlTimestamp = rs.getTimestamp(columnName);
			  if (sqlTimestamp != null) {
				  value = new Date(sqlTimestamp.getTime());
			  }
		  }
		  
	    return value;
	  }

	  @Override
	  public Object getNullableResult(ResultSet rs, int columnIndex)
	      throws SQLException {
		  Object value = "";
		  Object object = rs.getObject(columnIndex);
		  value = object;
		  if(object instanceof Clob) {
			  
			  Clob clob = (Clob) object;
				
				if (clob != null) {
					try {
						int size = (int) clob.length();
						value = clob.getSubString(1, size);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
		  }
		  else if (object instanceof java.sql.Date ) {
  			  Timestamp sqlTimestamp = rs.getTimestamp(columnIndex);
			  if (sqlTimestamp != null) {
				  value = new Date(sqlTimestamp.getTime());
			  }
		  }
	    return value;
	  }

	  @Override
	  public Object getNullableResult(CallableStatement cs, int columnIndex)
	      throws SQLException {
	    return cs.getObject(columnIndex);
	  }
}
