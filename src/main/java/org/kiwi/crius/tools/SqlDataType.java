package org.kiwi.crius.tools;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;


/**
 * SQL data type mapping to java data  type except primitives type.
 *this dependency <code>Cfg2JavaTool</code>
 * 
 * @author xujun
 *
 */
public enum SqlDataType {
	 	ARRAY           (Types.ARRAY, JavaDataType.ARRAY),
	    BIGINT          (Types.BIGINT, JavaDataType.LONG),
	    BINARY          (Types.BINARY, JavaDataType.BYTEARRAY),
	    BIT             (Types.BIT, JavaDataType.BOOLEAN),
	    BLOB            (Types.BLOB, JavaDataType.BLOB),
	    BOOLEAN         (Types.BOOLEAN, JavaDataType.BOOLEAN),
	    CHAR            (Types.CHAR, JavaDataType.CHAR),
	    CLOB            (Types.CLOB, JavaDataType.CLOB),
	    DATALINK        (Types.DATALINK, JavaDataType.URL),
	    DATE            (Types.DATE, JavaDataType.SQL_DATE),
	    DECIMAL         (Types.DECIMAL, JavaDataType.DOUBLE),
	    DISTINCT        (Types.DISTINCT, JavaDataType.OBJECT),
	    DOUBLE          (Types.DOUBLE, JavaDataType.DOUBLE),
	    FLOAT           (Types.FLOAT, JavaDataType.DOUBLE),
	    INTEGER         (Types.INTEGER, JavaDataType.INTEGER),
	    JAVA_OBJECT     (Types.JAVA_OBJECT, JavaDataType.OBJECT),
	    LONGVARBINARY   (Types.LONGVARBINARY, JavaDataType.BYTEARRAY),
	    LONGVARCHAR     (Types.LONGVARCHAR, JavaDataType.STRING),
	    NUMERIC         (Types.NUMERIC, JavaDataType.DOUBLE),
	    OTHER           (Types.OTHER, JavaDataType.OBJECT),
	    REAL            (Types.REAL, JavaDataType.FLOAT),
	    REF             (Types.REF, JavaDataType.REF),
	    SMALLINT        (Types.SMALLINT, JavaDataType.INTEGER),
	    STRUCT          (Types.STRUCT, JavaDataType.OBJECT),
	    TIME            (Types.TIME, JavaDataType.SQL_TIME),
	    TIMESTAMP       (Types.TIMESTAMP, JavaDataType.DATE),
	    TINYINT         (Types.TINYINT, JavaDataType.SHORT),
	    VARBINARY       (Types.VARBINARY, JavaDataType.BYTEARRAY),
        NUMBER       	(Types.VARBINARY, JavaDataType.LONG),
	    VARCHAR         (Types.VARCHAR, JavaDataType.STRING),
	    NVARCHAR2        (Types.VARCHAR, JavaDataType.STRING),
	    TEXT        (Types.VARCHAR, JavaDataType.STRING),
         DATETIME        (Types.VARCHAR, JavaDataType.DATE)
	    ;
	    
	    int id;
	    JavaDataType javaDataType;
	    static Map<String,String> typeLookupMap =new HashMap<String,String>();
	    static{
	    	for(SqlDataType type:SqlDataType.values()){
	    		typeLookupMap.put(type.toString().toLowerCase(), type.getJavaDataType().getCls().getName());
	    	}    	
	    }
	   
	    
	    private SqlDataType(int id, JavaDataType javaDataType) {
	        this.id = id;
	        this.javaDataType = javaDataType;
	    }
	    
	    public int getId() {
	        return id;
	    }
	    
	    public JavaDataType getJavaDataType() {
	        return javaDataType;
	    }
	    
	    public static SqlDataType valueOf(int id) {
	        for (SqlDataType type : SqlDataType.values())
	            if (type.getId() == id)
	                return type;
	        
	        return SqlDataType.OTHER;
	    }
	    
	    public static String forType(String dataType){
	    	if(Cfg2JavaTool.isPrimitiveTypeName(dataType)) return dataType;
	    	String javaType =typeLookupMap.get(dataType==null?null:dataType.toLowerCase());
	    	if(javaType ==null || "".equals(javaType))
	    		return null;
	    	return javaType;
	    }
}
