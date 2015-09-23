<%-- 
    Document   : clearCache
    Created on : 26 juil. 2009, 15:14:28
    Author     : spopoff
--%>

<%@ page session="true" contentType="text/html; charset=ISO-8859-1" %>

<%
String rep = "";
java.util.List<mondrian.rolap.RolapSchema> schemas = mondrian.rolap.RolapSchema.getRolapSchemas();
for (int i=0; i<schemas.size(); i++) {
	mondrian.rolap.RolapSchema schema = schemas.get(i);
	rep+="Schema="+schema.getName()+"<br/>";
	mondrian.olap.CacheControl cacheControl = schema.getInternalConnection().getCacheControl(null);
	for (mondrian.olap.Cube cube : schema.getCubes()) {
		rep+="&nbsp;&nbsp;&nbsp;cube="+cube.getName()+"<br/>";
		cacheControl.flush(cacheControl.createMeasuresRegion(cube));
	}
	cacheControl.flushSchema(schema);
	cacheControl.flushSchemaCache();
}

com.voyageone.bi.dao.cache.CacheContext.getInstance().evictCache();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>clear cache Mondrian</title>
    </head>
    <body>
    	<h1>Clear List:</h1>
        <p><%=rep%>
        <a href="index.html">back to index</a>
    </body>
</html>