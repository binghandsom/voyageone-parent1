package com.voyageone.service.dao;

import com.mongodb.*;
import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * [name]<br>
 * MongoService<br><br>
 * [function]<br>
 * 实现Mongo db基本数据库操作<br><br>
 * [history]<br>
 * 2014/04/08 ver1.00 JiangJusheng<br>
 */
@Repository
public class MongoNativeDao {

	private static Logger logger = Logger.getLogger(MongoNativeDao.class);
	@Resource
	private MongoTemplate mongoTemplate;
	/**
	 * 缺省DB对象
	 */
	private DB db = null;

	/**
	 * 取得指定名称的集合对象
	 *
	 * @param collName 集合名
	 *
	 * @return DBCollection 集合对象
	 */
	private DBCollection getCollection(String collName) {
		if (db == null) {
			db = mongoTemplate.getDb();
		}
		return db.getCollection(collName);
	}

	/**
	 * 根据指定条件查询数据<br>
	 *
	 * @param collName 数据库表名
	 * @param map 查询条件列
	 * @param exckey 输出列
	 *
	 * @return ArrayList<DBObject> 查询结果
	 */
	public ArrayList<DBObject> find(String collName, DBObject map, DBObject exckey) {
		return find(collName, map, exckey, null, 0, 0);
	}

	/**
	 * 根据指定条件查询数据<br>
	 *
	 * @param collName 数据库表名
	 * @param map 查询条件列
	 * @param exckey 输出列
	 * @param sortkey 排序条件
	 * @param limit 翻页用每页显示件数
	 * @param skip 翻页用计数
	 *
	 * @return ArrayList<DBObject> 查询结果
	 */
	public ArrayList<DBObject> find(String collName, DBObject map, DBObject exckey, DBObject sortkey, int limit, int skip) {
		DBCursor cur = getCollection(collName).find(map, exckey).sort(sortkey).limit(limit).skip(skip);
		if (cur == null) {
			return new ArrayList<DBObject>(0);
		}

		ArrayList<DBObject> list = new ArrayList<DBObject>(cur.count());
		while (cur.hasNext()) {
			DBObject obj = cur.next();
			list.add(obj);
		}
		cur.close();
		return list;
	}

	/**
	 * 查询数据
	 *
	 * @param collName 数据库表名
	 * @param map 查询条件列
	 * @param exckey 输出列
	 * @param sortkey 排序条件
	 *
	 * @return DBObject 查询结果
	 */
	public DBObject findOne(String collName, DBObject map, DBObject exckey, DBObject sortkey) {
		return getCollection(collName).findOne(map, exckey, sortkey);
	}

	/**
	 * 插入多条数据
	 *
	 * @param tblName 数据库表名
	 * @param listDB 数据对象列表
	 */
	public void insert(String tblName, List<DBObject> listDB) {
		getCollection(tblName).insert(listDB);
	}

	/**
	 * 更新数据
	 *
	 * @param tblName 数据库表名
	 * @param paraObj 更新条件
	 * @param rsObj  更新对象
	 */
	public void update(String tblName, DBObject paraObj, DBObject rsObj) {
		getCollection(tblName).update(paraObj, new BasicDBObject("$set", rsObj));
	}

	/**
	 * 更新数据
	 *
	 * @param tblName 数据库表名
	 * @param paraObj 更新条件
	 * @param rsObj  更新对象
	 */
	public void upsert(String tblName, DBObject paraObj, DBObject rsObj) {
		getCollection(tblName).update(paraObj, new BasicDBObject("$set", rsObj), true, false);
	}

	/**
	 * 插入一条数据
	 *
	 * @param tblName 数据库表名
	 * @param rsObj 数据对象
	 */
	public void save(String tblName, DBObject rsObj) {
		getCollection(tblName).save(rsObj);
	}

	/**
	 * 删除数据
	 *
	 * @param tblName 数据库表名
	 * @param paraObj 删除条件
	 */
	public void remove(String tblName, DBObject paraObj) {
		getCollection(tblName).remove(paraObj);
	}
}
