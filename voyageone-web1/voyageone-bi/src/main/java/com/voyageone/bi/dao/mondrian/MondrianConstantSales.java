package com.voyageone.bi.dao.mondrian;

public class MondrianConstantSales {


	// 各各维度SQL_MEMBERS
	public static String SQL_MEMBERS = 
			"	--qty \n"+
			"	MEMBER Measures.qty_n       as IIF(IsEmpty([Measures].[qty]), 0, [Measures].[qty]), FORMAT_STRING = '#0' \n"+
			"	--amt \n"+
			"	MEMBER Measures.amt_n     as IIF(IsEmpty([Measures].[amt]), 0, [Measures].[amt]), FORMAT_STRING = '#0.00' \n"+
			"	--orders \n"+
			"	MEMBER Measures.order_n   as IIF(IsEmpty([Measures].[orders]), 0, [Measures].[orders]), FORMAT_STRING = '#0' \n"+
			"	--atv \n"+
			"	MEMBER Measures.atv_n       as IIF([Measures].[amt_n]=0 OR [Measures].[order_n]=0, 0, [Measures].[amt_n]/[Measures].[order_n]), FORMAT_STRING = '#0.00' \n";
	
	public static String SQL_MEMBER_COLUMNS = 
			"	[Measures].[qty_n], \n"+
			"	[Measures].[amt_n], \n"+
			"	[Measures].[order_n], \n"+
			"	[Measures].[atv_n] \n";
	
	// 各各维度SQL_MEMBERS
	public static String EXTEND_SQL_MEMBERS = 
			"	--qty \n"+
			"	MEMBER Measures.qty_n       as IIF(IsEmpty([Measures].[qty]), 0, [Measures].[qty]), FORMAT_STRING = '#0' \n"+
			"	--amt \n"+
			"	MEMBER Measures.amt_n     as IIF(IsEmpty([Measures].[amt]), 0, [Measures].[amt]), FORMAT_STRING = '#0.00' \n"+
			"	--orders \n"+
			"	MEMBER Measures.order_n   as IIF(IsEmpty([Measures].[orders]), 0, [Measures].[orders]), FORMAT_STRING = '#0' \n"+
			"	--atv \n"+
			"	MEMBER Measures.atv_n       as IIF([Measures].[amt_n]=0 OR [Measures].[order_n]=0, 0, [Measures].[amt_n]/[Measures].[order_n]), FORMAT_STRING = '#0.00' \n" +
			"	--pv \n"+
			"	MEMBER Measures.pv_n        as IIF(IsEmpty([Measures].[pv]), 0, [Measures].[pv]), FORMAT_STRING = '#0' \n"+
			"	--uv \n"+
			"	MEMBER Measures.uv_n        as IIF(IsEmpty([Measures].[uv]), 0, [Measures].[uv]), FORMAT_STRING = '#0.00' \n" +
			"	--tr \n"+
			"	MEMBER Measures.tr_n         as IIF(IsEmpty([Measures].[tr]), 0, [Measures].[tr]), FORMAT_STRING = '#0.00' \n";
	
	public static String EXTEND_SQL_MEMBER_COLUMNS = 
			"	[Measures].[qty_n], \n"+
			"	[Measures].[amt_n], \n"+
			"	[Measures].[order_n], \n"+
			"	[Measures].[atv_n], \n" +
			"	[Measures].[pv_n], \n"+
			"	[Measures].[uv_n], \n" +
			"	[Measures].[tr_n] \n";
}
