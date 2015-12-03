package com.voyageone.bi.dao.mondrian;

public class MondrianConstantSalesTimeLine {
	/**
	 * 
	 * MEMBERS
	 * 
	 */
	public static String DAY_TIMELINE_MEMBERS = 
			"	--qty \n"+
			"	MEMBER Measures.qty_n       as IIF(IsEmpty([Measures].[qty]), 0, [Measures].[qty]), FORMAT_STRING = '#0' \n"+
			"	MEMBER Measures.qty_n_l     as IIF(IsEmpty(([Date].[Day].CurrentMember.PrevMember, [Measures].[qty])), 0, ([Date].[Day].CurrentMember.PrevMember, [Measures].[qty])), FORMAT_STRING = '#0' \n"+
			"	MEMBER Measures.qty_n_l_r   as  \n"+
			"		IIF ( [Measures].[qty_n_l] = 0 \n"+
			"		   OR [Measures].[qty_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[qty_n]/[Measures].[qty_n_l]) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.qty_n_l_r_u as  \n"+
			"		IIF ( [Measures].[qty_n_l_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[qty_n_l_r]-1) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.qty_n_y     as IIF(IsEmpty((ParallelPeriod([Date].[Day].CurrentMember.Parent.Parent.LEVEL, 1, [Date].[Day].CurrentMember) ,[Measures].[qty])),  \n"+
			"					0, (ParallelPeriod([Date].[Day].CurrentMember.Parent.Parent.LEVEL, 1, [Date].[Day].CurrentMember) ,[Measures].[qty])),  \n"+
			"					FORMAT_STRING = '#0' \n"+
			"	MEMBER Measures.qty_n_y_r   as \n"+
			"		IIF ( [Measures].[qty_n_y] = 0 \n"+
			"		   OR [Measures].[qty_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[qty_n] / [Measures].[qty_n_y])   \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.qty_n_y_r_u as \n"+
			"		IIF ( [Measures].[qty_n_y_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , [Measures].[qty_n_y_r]-1 \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	--amt \n"+
			"	MEMBER Measures.amt_n       as IIF(IsEmpty([Measures].[amt]), 0, [Measures].[amt]), FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.amt_n_l     as IIF(IsEmpty(([Date].[Day].CurrentMember.PrevMember, [Measures].[amt])), 0, ([Date].[Day].CurrentMember.PrevMember, [Measures].[amt])), FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.amt_n_l_r   as  \n"+
			"		IIF ( [Measures].[amt_n_l] = 0 \n"+
			"		   OR [Measures].[amt_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[amt_n]/[Measures].[amt_n_l]) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.amt_n_l_r_u as  \n"+
			"		IIF ( [Measures].[amt_n_l_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[amt_n_l_r]-1) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.amt_n_y     as IIF(IsEmpty((ParallelPeriod([Date].[Day].CurrentMember.Parent.Parent.LEVEL, 1, [Date].[Day].CurrentMember) ,[Measures].[amt])),  \n"+
			"					0, (ParallelPeriod([Date].[Day].CurrentMember.Parent.Parent.LEVEL, 1, [Date].[Day].CurrentMember) ,[Measures].[amt])),  \n"+
			"					FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.amt_n_y_r   as \n"+
			"		IIF ( [Measures].[amt_n_y] = 0 \n"+
			"		   OR [Measures].[amt_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[amt_n] / [Measures].[amt_n_y])   \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.amt_n_y_r_u as \n"+
			"		IIF ( [Measures].[amt_n_y_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , [Measures].[amt_n_y_r]-1 \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	--orders \n"+
			"	MEMBER Measures.order_n       as IIF(IsEmpty([Measures].[orders]), 0, [Measures].[orders]), FORMAT_STRING = '#0' \n"+
			"	MEMBER Measures.order_n_l     as IIF(IsEmpty(([Date].[Day].CurrentMember.PrevMember, [Measures].[orders])), 0, ([Date].[Day].CurrentMember.PrevMember, [Measures].[orders])), FORMAT_STRING = '#0' \n"+
			"	MEMBER Measures.order_n_l_r   as  \n"+
			"		IIF ( [Measures].[order_n_l] = 0 \n"+
			"		   OR [Measures].[order_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[order_n]/[Measures].[order_n_l]) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.order_n_l_r_u as  \n"+
			"		IIF ( [Measures].[order_n_l_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[order_n_l_r]-1) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.order_n_y     as IIF(IsEmpty((ParallelPeriod([Date].[Day].CurrentMember.Parent.Parent.LEVEL, 1, [Date].[Day].CurrentMember) ,[Measures].[orders])),  \n"+
			"					0, (ParallelPeriod([Date].[Day].CurrentMember.Parent.Parent.LEVEL, 1, [Date].[Day].CurrentMember) ,[Measures].[orders])),  \n"+
			"					FORMAT_STRING = '#0' \n"+
			"	MEMBER Measures.order_n_y_r   as \n"+
			"		IIF ( [Measures].[order_n_y] = 0 \n"+
			"		   OR [Measures].[order_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[order_n] / [Measures].[order_n_y])   \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.order_n_y_r_u as \n"+
			"		IIF ( [Measures].[order_n_y_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , [Measures].[order_n_y_r]-1 \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	--atv \n"+
			"	MEMBER Measures.atv_n       as IIF([Measures].[amt_n] = 0 OR [Measures].[order_n] = 0, 0, [Measures].[amt_n]/[Measures].[order_n]), FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.atv_n_l     as IIF([Measures].[amt_n_l] = 0 OR [Measures].[order_n_l] = 0, 0, [Measures].[amt_n_l]/[Measures].[order_n_l]), FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.atv_n_l_r   as  \n"+
			"		IIF ( [Measures].[atv_n_l] = 0 \n"+
			"		   OR [Measures].[atv_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[atv_n]/[Measures].[atv_n_l]) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.atv_n_l_r_u as  \n"+
			"		IIF ( [Measures].[atv_n_l_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[atv_n_l_r]-1) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.atv_n_y     as IIF([Measures].[amt_n_y] = 0 OR [Measures].[order_n_y] = 0, 0, [Measures].[amt_n_y]/[Measures].[order_n_y]), FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.atv_n_y_r   as \n"+
			"		IIF ( [Measures].[atv_n_y] = 0 \n"+
			"		   OR [Measures].[atv_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[atv_n] / [Measures].[atv_n_y])   \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.atv_n_y_r_u as \n"+
			"		IIF ( [Measures].[atv_n_y_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , [Measures].[atv_n_y_r]-1 \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n";

	public static String MONTH_TIMELINE_MEMBERS = 
			"	--qty \n"+
			"	MEMBER Measures.qty_n       as IIF(IsEmpty([Measures].[qty]), 0, [Measures].[qty]), FORMAT_STRING = '#0' \n"+
			"	MEMBER Measures.qty_n_l     as IIF(IsEmpty(([Date].[Month].CurrentMember.PrevMember, [Measures].[qty])), 0, ([Date].[Month].CurrentMember.PrevMember, [Measures].[qty])), FORMAT_STRING = '#0' \n"+
			"	MEMBER Measures.qty_n_l_r   as  \n"+
			"		IIF ( [Measures].[qty_n_l] = 0 \n"+
			"		   OR [Measures].[qty_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[qty_n]/[Measures].[qty_n_l]) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.qty_n_l_r_u as  \n"+
			"		IIF ( [Measures].[qty_n_l_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[qty_n_l_r]-1) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.qty_n_y     as IIF(IsEmpty((ParallelPeriod([Date].[Month].CurrentMember.Parent.LEVEL, 1, [Date].[Month].CurrentMember) ,[Measures].[qty])),  \n"+
			"					0, (ParallelPeriod([Date].[Month].CurrentMember.Parent.LEVEL, 1, [Date].[Month].CurrentMember) ,[Measures].[qty])),  \n"+
			"					FORMAT_STRING = '#0' \n"+
			"	MEMBER Measures.qty_n_y_r   as \n"+
			"		IIF ( [Measures].[qty_n_y] = 0 \n"+
			"		   OR [Measures].[qty_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[qty_n] / [Measures].[qty_n_y])   \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.qty_n_y_r_u as \n"+
			"		IIF ( [Measures].[qty_n_y_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , [Measures].[qty_n_y_r]-1 \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	--amt \n"+
			"	MEMBER Measures.amt_n       as IIF(IsEmpty([Measures].[amt]), 0, [Measures].[amt]), FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.amt_n_l     as IIF(IsEmpty(([Date].[Month].CurrentMember.PrevMember, [Measures].[amt])), 0, ([Date].[Month].CurrentMember.PrevMember, [Measures].[amt])), FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.amt_n_l_r   as  \n"+
			"		IIF ( [Measures].[amt_n_l] = 0 \n"+
			"		   OR [Measures].[amt_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[amt_n]/[Measures].[amt_n_l]) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.amt_n_l_r_u as  \n"+
			"		IIF ( [Measures].[amt_n_l_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[amt_n_l_r]-1) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.amt_n_y     as IIF(IsEmpty((ParallelPeriod([Date].[Month].CurrentMember.Parent.LEVEL, 1, [Date].[Month].CurrentMember) ,[Measures].[amt])),  \n"+
			"					0, (ParallelPeriod([Date].[Month].CurrentMember.Parent.LEVEL, 1, [Date].[Month].CurrentMember) ,[Measures].[amt])),  \n"+
			"					FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.amt_n_y_r   as \n"+
			"		IIF ( [Measures].[amt_n_y] = 0 \n"+
			"		   OR [Measures].[amt_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[amt_n] / [Measures].[amt_n_y])   \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.amt_n_y_r_u as \n"+
			"		IIF ( [Measures].[amt_n_y_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , [Measures].[amt_n_y_r]-1 \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	--orders \n"+
			"	MEMBER Measures.order_n       as IIF(IsEmpty([Measures].[orders]), 0, [Measures].[orders]), FORMAT_STRING = '#0' \n"+
			"	MEMBER Measures.order_n_l     as IIF(IsEmpty(([Date].[Month].CurrentMember.PrevMember, [Measures].[orders])), 0, ([Date].[Month].CurrentMember.PrevMember, [Measures].[orders])), FORMAT_STRING = '#0' \n"+
			"	MEMBER Measures.order_n_l_r   as  \n"+
			"		IIF ( [Measures].[order_n_l] = 0 \n"+
			"		   OR [Measures].[order_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[order_n]/[Measures].[order_n_l]) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.order_n_l_r_u as  \n"+
			"		IIF ( [Measures].[order_n_l_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[order_n_l_r]-1) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.order_n_y     as IIF(IsEmpty((ParallelPeriod([Date].[Month].CurrentMember.Parent.LEVEL, 1, [Date].[Month].CurrentMember) ,[Measures].[orders])),  \n"+
			"					0, (ParallelPeriod([Date].[Month].CurrentMember.Parent.LEVEL, 1, [Date].[Month].CurrentMember) ,[Measures].[orders])),  \n"+
			"					FORMAT_STRING = '#0' \n"+
			"	MEMBER Measures.order_n_y_r   as \n"+
			"		IIF ( [Measures].[order_n_y] = 0 \n"+
			"		   OR [Measures].[order_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[order_n] / [Measures].[order_n_y])   \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.order_n_y_r_u as \n"+
			"		IIF ( [Measures].[order_n_y_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , [Measures].[order_n_y_r]-1 \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	--atv \n"+
			"	MEMBER Measures.atv_n       as IIF([Measures].[amt_n] = 0 OR [Measures].[order_n] = 0, 0, [Measures].[amt_n]/[Measures].[order_n]), FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.atv_n_l     as IIF([Measures].[amt_n_l] = 0 OR [Measures].[order_n_l] = 0, 0, [Measures].[amt_n_l]/[Measures].[order_n_l]), FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.atv_n_l_r   as  \n"+
			"		IIF ( [Measures].[atv_n_l] = 0 \n"+
			"		   OR [Measures].[atv_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[atv_n]/[Measures].[atv_n_l]) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.atv_n_l_r_u as  \n"+
			"		IIF ( [Measures].[atv_n_l_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[atv_n_l_r]-1) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.atv_n_y     as IIF([Measures].[amt_n_y] = 0 OR [Measures].[order_n_y] = 0, 0, [Measures].[amt_n_y]/[Measures].[order_n_y]), FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.atv_n_y_r   as \n"+
			"		IIF ( [Measures].[atv_n_y] = 0 \n"+
			"		   OR [Measures].[atv_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[atv_n] / [Measures].[atv_n_y])   \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.atv_n_y_r_u as \n"+
			"		IIF ( [Measures].[atv_n_y_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , [Measures].[atv_n_y_r]-1 \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n";
			
	public static String YEAR_TIMELINE_MEMBERS = 
			"	--qty \n"+
			"	MEMBER Measures.qty_n       as IIF(IsEmpty([Measures].[qty]), 0, [Measures].[qty]), FORMAT_STRING = '#0' \n"+
			"	MEMBER Measures.qty_n_l     as IIF(IsEmpty(([Date].[Year].CurrentMember.PrevMember, [Measures].[qty])), 0, ([Date].[Year].CurrentMember.PrevMember, [Measures].[qty])), FORMAT_STRING = '#0' \n"+
			"	MEMBER Measures.qty_n_l_r   as  \n"+
			"		IIF ( [Measures].[qty_n_l] = 0 \n"+
			"		   OR [Measures].[qty_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[qty_n]/[Measures].[qty_n_l]) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.qty_n_l_r_u as  \n"+
			"		IIF ( [Measures].[qty_n_l_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[qty_n_l_r]-1) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.qty_n_y     as IIF(IsEmpty((ParallelPeriod([Date].[Year].CurrentMember.LEVEL, 1, [Date].[Year].CurrentMember) ,[Measures].[qty])),  \n"+
			"					0, (ParallelPeriod([Date].[Year].CurrentMember.LEVEL, 1, [Date].[Month].CurrentMember) ,[Measures].[qty])),  \n"+
			"					FORMAT_STRING = '#0' \n"+
			"	MEMBER Measures.qty_n_y_r   as \n"+
			"		IIF ( [Measures].[qty_n_y] = 0 \n"+
			"		   OR [Measures].[qty_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[qty_n] / [Measures].[qty_n_y])   \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.qty_n_y_r_u as \n"+
			"		IIF ( [Measures].[qty_n_y_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , [Measures].[qty_n_y_r]-1 \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	--amt \n"+
			"	MEMBER Measures.amt_n       as IIF(IsEmpty([Measures].[amt]), 0, [Measures].[amt]), FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.amt_n_l     as IIF(IsEmpty(([Date].[Year].CurrentMember.PrevMember, [Measures].[amt])), 0, ([Date].[Year].CurrentMember.PrevMember, [Measures].[amt])), FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.amt_n_l_r   as  \n"+
			"		IIF ( [Measures].[amt_n_l] = 0 \n"+
			"		   OR [Measures].[amt_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[amt_n]/[Measures].[amt_n_l]) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.amt_n_l_r_u as  \n"+
			"		IIF ( [Measures].[amt_n_l_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[amt_n_l_r]-1) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.amt_n_y     as IIF(IsEmpty((ParallelPeriod([Date].[Year].CurrentMember.LEVEL, 1, [Date].[Year].CurrentMember) ,[Measures].[amt])),  \n"+
			"					0, (ParallelPeriod([Date].[Year].CurrentMember.LEVEL, 1, [Date].[Year].CurrentMember) ,[Measures].[amt])),  \n"+
			"					FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.amt_n_y_r   as \n"+
			"		IIF ( [Measures].[amt_n_y] = 0 \n"+
			"		   OR [Measures].[amt_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[amt_n] / [Measures].[amt_n_y])   \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.amt_n_y_r_u as \n"+
			"		IIF ( [Measures].[amt_n_y_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , [Measures].[amt_n_y_r]-1 \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	--orders \n"+
			"	MEMBER Measures.order_n       as IIF(IsEmpty([Measures].[orders]), 0, [Measures].[orders]), FORMAT_STRING = '#0' \n"+
			"	MEMBER Measures.order_n_l     as IIF(IsEmpty(([Date].[Year].CurrentMember.PrevMember, [Measures].[orders])), 0, ([Date].[Year].CurrentMember.PrevMember, [Measures].[orders])), FORMAT_STRING = '#0' \n"+
			"	MEMBER Measures.order_n_l_r   as  \n"+
			"		IIF ( [Measures].[order_n_l] = 0 \n"+
			"		   OR [Measures].[order_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[order_n]/[Measures].[order_n_l]) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.order_n_l_r_u as  \n"+
			"		IIF ( [Measures].[order_n_l_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[order_n_l_r]-1) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.order_n_y     as IIF(IsEmpty((ParallelPeriod([Date].[Month].CurrentMember.LEVEL, 1, [Date].[Month].CurrentMember) ,[Measures].[orders])),  \n"+
			"					0, (ParallelPeriod([Date].[Year].CurrentMember.LEVEL, 1, [Date].[Year].CurrentMember) ,[Measures].[orders])),  \n"+
			"					FORMAT_STRING = '#0' \n"+
			"	MEMBER Measures.order_n_y_r   as \n"+
			"		IIF ( [Measures].[order_n_y] = 0 \n"+
			"		   OR [Measures].[order_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[order_n] / [Measures].[order_n_y])   \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.order_n_y_r_u as \n"+
			"		IIF ( [Measures].[order_n_y_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , [Measures].[order_n_y_r]-1 \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	--atv \n"+
			"	MEMBER Measures.atv_n       as IIF([Measures].[amt_n] = 0 OR [Measures].[order_n] = 0, 0, [Measures].[amt_n]/[Measures].[order_n]), FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.atv_n_l     as IIF([Measures].[amt_n_l] = 0 OR [Measures].[order_n_l] = 0, 0, [Measures].[amt_n_l]/[Measures].[order_n_l]), FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.atv_n_l_r   as  \n"+
			"		IIF ( [Measures].[atv_n_l] = 0 \n"+
			"		   OR [Measures].[atv_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[atv_n]/[Measures].[atv_n_l]) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.atv_n_l_r_u as  \n"+
			"		IIF ( [Measures].[atv_n_l_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[atv_n_l_r]-1) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.atv_n_y     as IIF([Measures].[amt_n_y] = 0 OR [Measures].[order_n_y] = 0, 0, [Measures].[amt_n_y]/[Measures].[order_n_y]), FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.atv_n_y_r   as \n"+
			"		IIF ( [Measures].[atv_n_y] = 0 \n"+
			"		   OR [Measures].[atv_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[atv_n] / [Measures].[atv_n_y])   \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.atv_n_y_r_u as \n"+
			"		IIF ( [Measures].[atv_n_y_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , [Measures].[atv_n_y_r]-1 \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n";
	
	public static String SQL_MEMBER_COLUMNS = 
			"	[Measures].[qty_n], \n"+
			"	[Measures].[qty_n_l], \n"+
			"	[Measures].[qty_n_l_r], \n"+
			"	[Measures].[qty_n_l_r_u], \n"+
			"	[Measures].[qty_n_y], \n"+
			"	[Measures].[qty_n_y_r], \n"+
			"	[Measures].[qty_n_y_r_u], \n"+
			"	[Measures].[amt_n], \n"+
			"	[Measures].[amt_n_l], \n"+
			"	[Measures].[amt_n_l_r], \n"+
			"	[Measures].[amt_n_l_r_u], \n"+
			"	[Measures].[amt_n_y], \n"+
			"	[Measures].[amt_n_y_r], \n"+
			"	[Measures].[amt_n_y_r_u], \n"+
			"	[Measures].[order_n], \n"+
			"	[Measures].[order_n_l], \n"+
			"	[Measures].[order_n_l_r], \n"+
			"	[Measures].[order_n_l_r_u], \n"+
			"	[Measures].[order_n_y], \n"+
			"	[Measures].[order_n_y_r], \n"+
			"	[Measures].[order_n_y_r_u], \n"+
			"	[Measures].[atv_n], \n"+
			"	[Measures].[atv_n_l], \n"+
			"	[Measures].[atv_n_l_r], \n"+
			"	[Measures].[atv_n_l_r_u], \n"+
			"	[Measures].[atv_n_y], \n"+
			"	[Measures].[atv_n_y_r], \n"+
			"	[Measures].[atv_n_y_r_u] \n";
	
	
	/**
	 * 
	 * EXTEND_MEMBERS
	 * 
	 */
	public static String DAY_TIMELINE_EXTEND_MEMBERS = 
			"	--pv \n"+
			"	MEMBER Measures.pv_n       as IIF(IsEmpty([Measures].[pv]), 0, [Measures].[pv]), FORMAT_STRING = '#0' \n"+
			"	MEMBER Measures.pv_n_l     as IIF(IsEmpty(([Date].[Day].CurrentMember.PrevMember, [Measures].[pv])), 0, ([Date].[Day].CurrentMember.PrevMember, [Measures].[pv])), FORMAT_STRING = '#0' \n"+
			"	MEMBER Measures.pv_n_l_r   as  \n"+
			"		IIF ( [Measures].[pv_n_l] = 0 \n"+
			"		   OR [Measures].[pv_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[pv_n]/[Measures].[pv_n_l]) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.pv_n_l_r_u as  \n"+
			"		IIF ( [Measures].[pv_n_l_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[pv_n_l_r]-1) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.pv_n_y     as IIF(IsEmpty((ParallelPeriod([Date].[Day].CurrentMember.Parent.Parent.LEVEL, 1, [Date].[Day].CurrentMember) ,[Measures].[pv])),  \n"+
			"					0, (ParallelPeriod([Date].[Day].CurrentMember.Parent.Parent.LEVEL, 1, [Date].[Day].CurrentMember) ,[Measures].[pv])),  \n"+
			"					FORMAT_STRING = '#0' \n"+
			"	MEMBER Measures.pv_n_y_r   as \n"+
			"		IIF ( [Measures].[pv_n_y] = 0 \n"+
			"		   OR [Measures].[pv_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[pv_n] / [Measures].[pv_n_y])   \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.pv_n_y_r_u as \n"+
			"		IIF ( [Measures].[pv_n_y_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , [Measures].[pv_n_y_r]-1 \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	--uv \n"+
			"	MEMBER Measures.uv_n       as IIF(IsEmpty([Measures].[uv]), 0, [Measures].[uv]), FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.uv_n_l     as IIF(IsEmpty(([Date].[Day].CurrentMember.PrevMember, [Measures].[uv])), 0, ([Date].[Day].CurrentMember.PrevMember, [Measures].[uv])), FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.uv_n_l_r   as  \n"+
			"		IIF ( [Measures].[uv_n_l] = 0 \n"+
			"		   OR [Measures].[uv_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[uv_n]/[Measures].[uv_n_l]) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.uv_n_l_r_u as  \n"+
			"		IIF ( [Measures].[uv_n_l_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[uv_n_l_r]-1) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.uv_n_y     as IIF(IsEmpty((ParallelPeriod([Date].[Day].CurrentMember.Parent.Parent.LEVEL, 1, [Date].[Day].CurrentMember) ,[Measures].[uv])),  \n"+
			"					0, (ParallelPeriod([Date].[Day].CurrentMember.Parent.Parent.LEVEL, 1, [Date].[Day].CurrentMember) ,[Measures].[uv])),  \n"+
			"					FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.uv_n_y_r   as \n"+
			"		IIF ( [Measures].[uv_n_y] = 0 \n"+
			"		   OR [Measures].[uv_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[uv_n] / [Measures].[uv_n_y])   \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.uv_n_y_r_u as \n"+
			"		IIF ( [Measures].[uv_n_y_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , [Measures].[uv_n_y_r]-1 \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n" +
			"	--tr \n"+
			"	MEMBER Measures.tr_n       as IIF(IsEmpty([Measures].[tr]), 0, [Measures].[tr]), FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.tr_n_l     as IIF(IsEmpty(([Date].[Day].CurrentMember.PrevMember, [Measures].[tr])), 0, ([Date].[Day].CurrentMember.PrevMember, [Measures].[tr])), FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.tr_n_l_r   as  \n"+
			"		IIF ( [Measures].[tr_n_l] = 0 \n"+
			"		   OR [Measures].[tr_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[tr_n]/[Measures].[tr_n_l]) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.tr_n_l_r_u as  \n"+
			"		IIF ( [Measures].[tr_n_l_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[tr_n_l_r]-1) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.tr_n_y     as IIF(IsEmpty((ParallelPeriod([Date].[Day].CurrentMember.Parent.Parent.LEVEL, 1, [Date].[Day].CurrentMember) ,[Measures].[tr])),  \n"+
			"					0, (ParallelPeriod([Date].[Day].CurrentMember.Parent.Parent.LEVEL, 1, [Date].[Day].CurrentMember) ,[Measures].[tr])),  \n"+
			"					FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.tr_n_y_r   as \n"+
			"		IIF ( [Measures].[tr_n_y] = 0 \n"+
			"		   OR [Measures].[tr_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[tr_n] / [Measures].[tr_n_y])   \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.tr_n_y_r_u as \n"+
			"		IIF ( [Measures].[tr_n_y_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , [Measures].[tr_n_y_r]-1 \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n";
	
	public static String MONTH_TIMELINE_EXTEND_MEMBERS = 
			"	--pv \n"+
			"	MEMBER Measures.pv_n       as IIF(IsEmpty([Measures].[pv]), 0, [Measures].[pv]), FORMAT_STRING = '#0' \n"+
			"	MEMBER Measures.pv_n_l     as IIF(IsEmpty(([Date].[Month].CurrentMember.PrevMember, [Measures].[pv])), 0, ([Date].[Month].CurrentMember.PrevMember, [Measures].[pv])), FORMAT_STRING = '#0' \n"+
			"	MEMBER Measures.pv_n_l_r   as  \n"+
			"		IIF ( [Measures].[pv_n_l] = 0 \n"+
			"		   OR [Measures].[pv_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[pv_n]/[Measures].[pv_n_l]) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.pv_n_l_r_u as  \n"+
			"		IIF ( [Measures].[pv_n_l_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[pv_n_l_r]-1) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.pv_n_y     as IIF(IsEmpty((ParallelPeriod([Date].[Month].CurrentMember.Parent.LEVEL, 1, [Date].[Month].CurrentMember) ,[Measures].[pv])),  \n"+
			"					0, (ParallelPeriod([Date].[Month].CurrentMember.Parent.LEVEL, 1, [Date].[Month].CurrentMember) ,[Measures].[pv])),  \n"+
			"					FORMAT_STRING = '#0' \n"+
			"	MEMBER Measures.pv_n_y_r   as \n"+
			"		IIF ( [Measures].[pv_n_y] = 0 \n"+
			"		   OR [Measures].[pv_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[pv_n] / [Measures].[pv_n_y])   \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.pv_n_y_r_u as \n"+
			"		IIF ( [Measures].[pv_n_y_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , [Measures].[pv_n_y_r]-1 \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	--uv \n"+
			"	MEMBER Measures.uv_n       as IIF(IsEmpty([Measures].[uv]), 0, [Measures].[uv]), FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.uv_n_l     as IIF(IsEmpty(([Date].[Month].CurrentMember.PrevMember, [Measures].[uv])), 0, ([Date].[Month].CurrentMember.PrevMember, [Measures].[uv])), FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.uv_n_l_r   as  \n"+
			"		IIF ( [Measures].[uv_n_l] = 0 \n"+
			"		   OR [Measures].[uv_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[uv_n]/[Measures].[uv_n_l]) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.uv_n_l_r_u as  \n"+
			"		IIF ( [Measures].[uv_n_l_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[uv_n_l_r]-1) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.uv_n_y     as IIF(IsEmpty((ParallelPeriod([Date].[Month].CurrentMember.Parent.LEVEL, 1, [Date].[Month].CurrentMember) ,[Measures].[uv])),  \n"+
			"					0, (ParallelPeriod([Date].[Month].CurrentMember.Parent.LEVEL, 1, [Date].[Month].CurrentMember) ,[Measures].[uv])),  \n"+
			"					FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.uv_n_y_r   as \n"+
			"		IIF ( [Measures].[uv_n_y] = 0 \n"+
			"		   OR [Measures].[uv_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[uv_n] / [Measures].[uv_n_y])   \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.uv_n_y_r_u as \n"+
			"		IIF ( [Measures].[uv_n_y_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , [Measures].[uv_n_y_r]-1 \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n" + 
			"	--tr \n"+
			"	MEMBER Measures.tr_n       as IIF(IsEmpty([Measures].[tr]), 0, [Measures].[tr]), FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.tr_n_l     as IIF(IsEmpty(([Date].[Month].CurrentMember.PrevMember, [Measures].[tr])), 0, ([Date].[Month].CurrentMember.PrevMember, [Measures].[tr])), FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.tr_n_l_r   as  \n"+
			"		IIF ( [Measures].[tr_n_l] = 0 \n"+
			"		   OR [Measures].[tr_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[tr_n]/[Measures].[tr_n_l]) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.tr_n_l_r_u as  \n"+
			"		IIF ( [Measures].[tr_n_l_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[tr_n_l_r]-1) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.tr_n_y     as IIF(IsEmpty((ParallelPeriod([Date].[Month].CurrentMember.Parent.LEVEL, 1, [Date].[Month].CurrentMember) ,[Measures].[tr])),  \n"+
			"					0, (ParallelPeriod([Date].[Month].CurrentMember.Parent.LEVEL, 1, [Date].[Month].CurrentMember) ,[Measures].[tr])),  \n"+
			"					FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.tr_n_y_r   as \n"+
			"		IIF ( [Measures].[tr_n_y] = 0 \n"+
			"		   OR [Measures].[tr_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[tr_n] / [Measures].[tr_n_y])   \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.tr_n_y_r_u as \n"+
			"		IIF ( [Measures].[tr_n_y_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , [Measures].[tr_n_y_r]-1 \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n";
	
	public static String YEAR_TIMELINE_EXTEND_MEMBERS = 
			"	--pv \n"+
			"	MEMBER Measures.pv_n       as IIF(IsEmpty([Measures].[pv]), 0, [Measures].[pv]), FORMAT_STRING = '#0' \n"+
			"	MEMBER Measures.pv_n_l     as IIF(IsEmpty(([Date].[Year].CurrentMember.PrevMember, [Measures].[pv])), 0, ([Date].[Year].CurrentMember.PrevMember, [Measures].[pv])), FORMAT_STRING = '#0' \n"+
			"	MEMBER Measures.pv_n_l_r   as  \n"+
			"		IIF ( [Measures].[pv_n_l] = 0 \n"+
			"		   OR [Measures].[pv_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[pv_n]/[Measures].[pv_n_l]) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.pv_n_l_r_u as  \n"+
			"		IIF ( [Measures].[pv_n_l_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[pv_n_l_r]-1) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.pv_n_y     as IIF(IsEmpty((ParallelPeriod([Date].[Year].CurrentMember.LEVEL, 1, [Date].[Year].CurrentMember) ,[Measures].[pv])),  \n"+
			"					0, (ParallelPeriod([Date].[Year].CurrentMember.LEVEL, 1, [Date].[Year].CurrentMember) ,[Measures].[pv])),  \n"+
			"					FORMAT_STRING = '#0' \n"+
			"	MEMBER Measures.pv_n_y_r   as \n"+
			"		IIF ( [Measures].[pv_n_y] = 0 \n"+
			"		   OR [Measures].[pv_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[pv_n] / [Measures].[pv_n_y])   \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.pv_n_y_r_u as \n"+
			"		IIF ( [Measures].[pv_n_y_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , [Measures].[pv_n_y_r]-1 \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	--uv \n"+
			"	MEMBER Measures.uv_n       as IIF(IsEmpty([Measures].[uv]), 0, [Measures].[uv]), FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.uv_n_l     as IIF(IsEmpty(([Date].[Year].CurrentMember.PrevMember, [Measures].[uv])), 0, ([Date].[Year].CurrentMember.PrevMember, [Measures].[uv])), FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.uv_n_l_r   as  \n"+
			"		IIF ( [Measures].[uv_n_l] = 0 \n"+
			"		   OR [Measures].[uv_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[uv_n]/[Measures].[uv_n_l]) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.uv_n_l_r_u as  \n"+
			"		IIF ( [Measures].[uv_n_l_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[uv_n_l_r]-1) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.uv_n_y     as IIF(IsEmpty((ParallelPeriod([Date].[Year].CurrentMember.LEVEL, 1, [Date].[Year].CurrentMember) ,[Measures].[uv])),  \n"+
			"					0, (ParallelPeriod([Date].[Year].CurrentMember.LEVEL, 1, [Date].[Year].CurrentMember) ,[Measures].[uv])),  \n"+
			"					FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.uv_n_y_r   as \n"+
			"		IIF ( [Measures].[uv_n_y] = 0 \n"+
			"		   OR [Measures].[uv_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[uv_n] / [Measures].[uv_n_y])   \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.uv_n_y_r_u as \n"+
			"		IIF ( [Measures].[uv_n_y_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , [Measures].[uv_n_y_r]-1 \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n" + 
			"	--tr \n"+
			"	MEMBER Measures.tr_n       as IIF(IsEmpty([Measures].[tr]), 0, [Measures].[tr]), FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.tr_n_l     as IIF(IsEmpty(([Date].[Year].CurrentMember.PrevMember, [Measures].[tr])), 0, ([Date].[Year].CurrentMember.PrevMember, [Measures].[tr])), FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.tr_n_l_r   as  \n"+
			"		IIF ( [Measures].[tr_n_l] = 0 \n"+
			"		   OR [Measures].[tr_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[tr_n]/[Measures].[tr_n_l]) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.tr_n_l_r_u as  \n"+
			"		IIF ( [Measures].[tr_n_l_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[tr_n_l_r]-1) \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.tr_n_y     as IIF(IsEmpty((ParallelPeriod([Date].[Year].CurrentMember.LEVEL, 1, [Date].[Year].CurrentMember) ,[Measures].[tr])),  \n"+
			"					0, (ParallelPeriod([Date].[Year].CurrentMember.LEVEL, 1, [Date].[Year].CurrentMember) ,[Measures].[tr])),  \n"+
			"					FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.tr_n_y_r   as \n"+
			"		IIF ( [Measures].[tr_n_y] = 0 \n"+
			"		   OR [Measures].[tr_n] = 0 \n"+
			"		   , 0   \n"+
			"		   , ([Measures].[tr_n] / [Measures].[tr_n_y])   \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n"+
			"	MEMBER Measures.tr_n_y_r_u as \n"+
			"		IIF ( [Measures].[tr_n_y_r] = 0 \n"+
			"		   , 0   \n"+
			"		   , [Measures].[tr_n_y_r]-1 \n"+
			"		 ) , FORMAT_STRING = '#0.00' \n";

	public static String SQL_MEMBER_EXTEND_COLUMNS = 
			"	[Measures].[pv_n], \n"+
			"	[Measures].[pv_n_l], \n"+
			"	[Measures].[pv_n_l_r], \n"+
			"	[Measures].[pv_n_l_r_u], \n"+
			"	[Measures].[pv_n_y], \n"+
			"	[Measures].[pv_n_y_r], \n"+
			"	[Measures].[pv_n_y_r_u], \n"+
			"	[Measures].[uv_n], \n"+
			"	[Measures].[uv_n_l], \n"+
			"	[Measures].[uv_n_l_r], \n"+
			"	[Measures].[uv_n_l_r_u], \n"+
			"	[Measures].[uv_n_y], \n"+
			"	[Measures].[uv_n_y_r], \n"+
			"	[Measures].[uv_n_y_r_u], \n" +
			"	[Measures].[tr_n], \n"+
			"	[Measures].[tr_n_l], \n"+
			"	[Measures].[tr_n_l_r], \n"+
			"	[Measures].[tr_n_l_r_u], \n"+
			"	[Measures].[tr_n_y], \n"+
			"	[Measures].[tr_n_y_r], \n"+
			"	[Measures].[tr_n_y_r_u] \n";

}
