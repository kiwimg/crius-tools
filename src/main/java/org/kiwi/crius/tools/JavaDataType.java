package org.kiwi.crius.tools;

public enum JavaDataType {
	 	ARRAY           (java.sql.Array.class, SqlDataType.ARRAY),
	    BIGDECIMAL      (java.math.BigDecimal.class, SqlDataType.DOUBLE),
	    BOOLEAN         (java.lang.Boolean.class, SqlDataType.BOOLEAN),
	    BYTEARRAY       (java.lang.Byte[].class, SqlDataType.BINARY),
	    BLOB            (java.sql.Blob.class, SqlDataType.BLOB),
	    CHAR            (java.lang.Character.class, SqlDataType.CHAR),
	    CLOB            (java.sql.Clob.class, SqlDataType.CLOB),
	    DATE            (java.util.Date.class, SqlDataType.TIMESTAMP),
	    DOUBLE          (java.lang.Double.class, SqlDataType.DOUBLE),
	    FLOAT           (java.lang.Float.class, SqlDataType.REAL),
	    INTEGER         (java.lang.Integer.class, SqlDataType.INTEGER),
	    LONG            (java.lang.Long.class, SqlDataType.BIGINT),
	    REF             (java.lang.ref.Reference.class, SqlDataType.REF),
	    STRING          (java.lang.String.class, SqlDataType.VARCHAR),
	    SHORT           (java.lang.Short.class, SqlDataType.TINYINT),
	    SQL_DATE        (java.util.Date.class, SqlDataType.DATE),
	    SQL_TIME        (java.sql.Time.class, SqlDataType.TIME),
	    SQL_TIMESTAMP   (java.sql.Timestamp.class, SqlDataType.TIMESTAMP),
	    URL             (java.net.URL.class, SqlDataType.DATALINK),
	    OBJECT          (java.lang.Object.class, SqlDataType.OTHER)
	    ;
	    
	    @SuppressWarnings("unchecked")
		private Class cls;
	    private SqlDataType sqlDataType;
	    
	    @SuppressWarnings("unchecked")
		private JavaDataType(Class cls, SqlDataType sqlDataType) {
	        this.cls = cls;
	        this.sqlDataType = sqlDataType;
	    }
	    
	    @SuppressWarnings("unchecked")
		public Class getCls() {
	        return cls;
	    }
	    
	    public SqlDataType getSqlDataType() {
	        return sqlDataType;
	    }
}
