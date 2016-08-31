delimiter $$
drop procedure if exists proc_new_shop $$
create procedure proc_new_shop()
begin
	set autocommit = 0;
	set @error_no = 0;
	set @not_found = 0;
	begin
		declare exit handler for sqlexception set @error_no = 1;
		declare continue handler for not found set @not_found = 1;
<#-- 渠道信息 -->
		/* tm_order_channel */
		${channel};
<#-- 渠道配置信息 -->
		<#if channelConfig??>
		/* tm_order_channel_config */
			<#list channelConfig as item>
		${item};
			</#list>
		</#if>
<#-- 短信配置信息 -->
		<#if sms??>
		/* tm_sms_config */
			<#list sms as item>
		${item};
			</#list>
		</#if>
<#-- 第三方配置信息 -->
		<#if thirdParty??>
		/* com_mt_third_party_config */
			<#list thirdParty as item>
		${item};
			</#list>
		</#if>
<#-- 快递配置信息 -->
		<#if carrier??>
		/* tm_carrier_channel */
			<#list carrier as item>
		${item};
			</#list>
		</#if>
<#-- 类型属性配置信息 -->
		<#if channelAttr??>
		/* com_mt_value_channel */
			<#list channelAttr as item>
		${item};
			</#list>
		</#if>
<#-- 仓库和配置信息 -->
		<#if store??>
			<#list store as item>
		/* wms_mt_store */
		${item.sql};
				<#if item.config??>
		/* ct_store_config */
		select last_insert_id() into @last_store_id;
					<#list item.config as config>
		${config};
					</#list>
				</#if>
			</#list>
		</#if>
<#-- 渠道Cart和配置信息 -->
		<#if cartShop??>
			<#list cartShop as item>
		/* tm_channel_shop */
		${item.sql};
				<#if item.config??>
		/* tm_channel_shop_config */
					<#list item.config as config>
		${config};
					</#list>
				</#if>
			</#list>
		</#if>
<#-- Cart物流信息 -->
		<#if cartTracking??>
		/* com_mt_tracking_info_config */
			<#list cartTracking as item>
		${item};
			</#list>
		</#if>
<#-- 任务信息 -->
		<#if task??>
			<#list task as item>
		/* com_mt_task */
		set @not_found = 0;
		${item.select};
		if @not_found = 1 then
			${item.insert};
		else
			${item.update};
		end if;
				<#if item.config??>
		/* tm_task_control */
					<#list item.config as config>
		set @not_found = 0;
		${config.select};
		if @not_found = 1 then
			${config.insert};
		end if;
					</#list>
				</#if>
			</#list>
		</#if>
	end;
	if @error_no = 1 then
		rollback;
	else
		commit;
	end if;
	show errors;
end$$
call proc_new_shop() $$
delimiter ;