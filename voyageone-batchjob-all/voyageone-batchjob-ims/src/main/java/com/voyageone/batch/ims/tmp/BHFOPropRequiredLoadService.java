package com.voyageone.batch.ims.tmp;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.taobao.top.schema.depend.DependGroup;
import com.taobao.top.schema.enums.FieldTypeEnum;
import com.taobao.top.schema.factory.SchemaReader;
import com.taobao.top.schema.field.ComplexField;
import com.taobao.top.schema.field.Field;
import com.taobao.top.schema.field.MultiComplexField;
import com.taobao.top.schema.field.OptionsField;
import com.taobao.top.schema.option.Option;
import com.taobao.top.schema.rule.Rule;

@Service
public class BHFOPropRequiredLoadService {

	@Qualifier("masterJdbcTemplate")
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Transactional
	public void doLoad() throws Exception {
		List<Long> query = jdbcTemplate.query("select cid from bhfo_use_cid", new RowMapper<Long>() {

			@Override
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getLong("cid");
			}
		});

		String filePrefix = "/Users/sheppard/doc/xml";
		String item_rule = filePrefix + "/item_rule/";
		String product_rule = filePrefix + "/product_rule/";
		for (Long cid : query) {
			
			String item_rule_file_name = item_rule + cid + ".xml";
			String product_rule_file_name = product_rule + cid + ".xml";

			File item_rule_file = new File(item_rule_file_name);
			File product_rule_file = new File(product_rule_file_name);

			if (item_rule_file.exists()) {
				doProcess(cid, item_rule_file, Boolean.FALSE);
			}

			if (product_rule_file.exists()) {
				doProcess(cid, product_rule_file, Boolean.TRUE);
			}

		}
	}

	private static final String CHANNEL_ID = "006";

	private void doProcess(Long cid, File item_rule_file, boolean isProduct) throws Exception {
		List<Field> fs = SchemaReader.readXmlForList(FileUtils.readFileToString(item_rule_file));
		List<Field> fieldList = new ArrayList<Field>();
		for (Field field : fs) {
			fieldList.add(field);
			if (FieldTypeEnum.MULTICOMPLEX.equals(field.getType())) {
				MultiComplexField mf = (MultiComplexField) field;
				List<Field> fls = mf.getFieldList();
				for (Field mcf : fls) {
					fieldList.add(mcf);
					if (FieldTypeEnum.COMPLEX.equals(field.getType())) {
						ComplexField cf = (ComplexField) mcf;
						fieldList.addAll(cf.getFieldList());
					}
				}
			} else if (FieldTypeEnum.COMPLEX.equals(field.getType())) {
				ComplexField cf = (ComplexField) field;
				fieldList.addAll(cf.getFieldList());
			}

		}

		for (Field f : fieldList) {
			Rule requiredRule = f.getRuleByName("requiredRule");

			if (requiredRule == null) {
				continue;
			}

			String prop_id = f.getId();
			String prop_type = f.getType().name();
			String prop_name = f.getName();

			boolean hasDependGroup = Boolean.FALSE;
			for (Rule rule : f.getRules()) {
				DependGroup dependGroup = rule.getDependGroup();
				if (dependGroup != null) {
					hasDependGroup = Boolean.TRUE;
					break;
				}
			}
			if (hasDependGroup) {
				continue;
			}

			boolean saveFlag = Boolean.FALSE;

			if (!(f instanceof OptionsField)) {
				if (!saveFlag) {
					doSave(cid, prop_id, prop_type, prop_name, null, null, isProduct);
					saveFlag = Boolean.TRUE;
				}
				continue;
			}
			OptionsField field = (OptionsField) f;
			List<Option> options = field.getOptions();

			if (CollectionUtils.isEmpty(options)) {
				if (!saveFlag) {
					doSave(cid, prop_id, prop_type, prop_name, null, null, isProduct);
					saveFlag = Boolean.TRUE;
				}
				continue;
			} else {
				for (Option option : options) {
					String option_name = option.getDisplayName();
					String option_value = option.getValue();
					doSave(cid, prop_id, prop_type, prop_name, option_name, option_value, isProduct);
				}
			}

		}
	}

	private void doSave(Long cid, String prop_id, String prop_type, String prop_name, String option_name, String option_value, boolean isProduct) {
		Object[] args = { CHANNEL_ID, cid, prop_id, prop_type, prop_name, option_name, option_value, isProduct ? 1 : 0 };
		jdbcTemplate.update(
				"insert into bhfo_prop_required(channel_id,cid,prop_id,prop_type,prop_name,option_name,option_value,is_product) values(?,?,?,?,?,?,?,?)", args);
	}

	//@Scheduled(cron = "0/5 * *  * * ? ")
	public void execute() throws Exception {
		System.out.println("********");
		doLoad();
		System.out.println("========");
	}
}
