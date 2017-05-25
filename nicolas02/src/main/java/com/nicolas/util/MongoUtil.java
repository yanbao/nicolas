package com.nicolas.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;


import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
/**
 * Created by 大泥藕 on 2017/5/17.
 */

@Repository("dBObjectRepository")
public class MongoUtil {
	
	private MongoClient mongoClient;
	
	private static final String DB_NAME = "dbname";
	
	public MongoUtil(){
		init();
	}
	
	public void init(){

		//------------------------------------------------------------------------
		try {
			//连接到MongoDB服务 如果是远程连接可以替换“localhost”为服务器所在IP地址
			//ServerAddress()两个参数分别为 服务器地址 和 端口
			ServerAddress serverAddress = new ServerAddress(MongoConfig.MONGODB_IP,27017);
			List<ServerAddress> addrs = new ArrayList<ServerAddress>();
			addrs.add(serverAddress);

			//MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码
			MongoCredential credential = MongoCredential.createScramSha1Credential(
					"username",
					"dbname",
					"dbpwd".toCharArray());
			List<MongoCredential> credentials = new ArrayList<MongoCredential>();
			credentials.add(credential);

			//通过连接认证获取MongoDB连接
			 mongoClient = new MongoClient(addrs,credentials);

			//连接到数据库
			MongoDatabase mongoDatabase = mongoClient.getDatabase("databaseName");
			System.out.println("Connect to database successfully");
		} catch (Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}
		//------------------------------------------------------------------------
	}
	
	@SuppressWarnings("finally")
	public boolean insert(String collectionName,BasicDBObject object){
		MongoDatabase db = mongoClient.getDatabase(DB_NAME);
		MongoCollection<BasicDBObject> collection = db.getCollection(collectionName, BasicDBObject.class);
		boolean result = true;
		try{
			collection.insertOne(object);
		}catch(Exception e){
			e.printStackTrace();
			result = false;
		}finally{
			return result;
		}
	}
	
	public List<BasicDBObject> find(String collectionName,Map<String, Object> queryMap){
		List<BasicDBObject> result = new ArrayList<BasicDBObject>();
		MongoDatabase db = mongoClient.getDatabase(DB_NAME);
		MongoCollection<BasicDBObject> collection = db.getCollection(collectionName, BasicDBObject.class);
		Document queryDocument = new Document(queryMap);
		collection.find(queryDocument).into(result);
		return result;
	}
	
	public List<BasicDBObject> find(String collectionName,BasicDBObject condition){
		List<BasicDBObject> result = new ArrayList<BasicDBObject>();
		MongoDatabase db = mongoClient.getDatabase(DB_NAME);
		MongoCollection<BasicDBObject> collection = db.getCollection(collectionName, BasicDBObject.class);
		collection.find(condition).into(result);
		return result;
	}
	
	public List<BasicDBObject> findAndSort(String collectionName,BasicDBObject condition,BasicDBObject sort){
		List<BasicDBObject> result = new ArrayList<BasicDBObject>();
		MongoDatabase db = mongoClient.getDatabase(DB_NAME);
		MongoCollection<BasicDBObject> collection = db.getCollection(collectionName, BasicDBObject.class);
		collection.find(condition).sort(sort).into(result);
		return result;
	}
	
	public BasicDBObject get(String collectionName,BasicDBObject condition){
		List<BasicDBObject> result = find(collectionName, condition);
		if(result != null && result.size() == 1){
			return result.get(0);
		}
		return null;
	}
	
	public BasicDBObject get(String collectionName,Map<String, Object> queryMap){
		List<BasicDBObject> result = find(collectionName, queryMap);
		if(result != null && result.size() == 1){
			return result.get(0);
		}
		return null;
	}
	
	public boolean update(String collectionName,Map<String, Object> queryMap,BasicDBObject object){
		MongoDatabase db = mongoClient.getDatabase(DB_NAME);
		MongoCollection<BasicDBObject> collection = db.getCollection(collectionName, BasicDBObject.class);
		Document queryDocument = new Document(queryMap);
		UpdateResult result = collection.replaceOne(queryDocument, object);
		return result.wasAcknowledged();
	}
	
	public boolean update(String collectionName,BasicDBObject query,BasicDBObject object){
		MongoDatabase db = mongoClient.getDatabase(DB_NAME);
		MongoCollection<BasicDBObject> collection = db.getCollection(collectionName, BasicDBObject.class);
		BasicDBObject innerQuery = (BasicDBObject) query.clone();
		innerQuery.put("_id", object.get("_id"));
		UpdateResult result = collection.replaceOne(innerQuery, object);
		return result.wasAcknowledged();
	}
	public boolean update(String collectionName,Document query,BasicDBObject newDoc){
		 MongoDatabase db = mongoClient.getDatabase(DB_NAME);
		 MongoCollection<BasicDBObject> table = db.getCollection(collectionName, BasicDBObject.class);
		 UpdateOptions options = new UpdateOptions();
	     //如果这里是true，当查不到结果的时候会添加一条newDoc,默认为false
	     options.upsert(true);
	     BasicDBObject updateObj = new BasicDBObject();
	     updateObj.put("$set", newDoc);
	     UpdateResult result = table.updateMany(query, updateObj, options);
		return ((result.getModifiedCount()>0)?true:false);
		
	}
	public boolean delete(String collectionName,BasicDBObject query){
		MongoDatabase db = mongoClient.getDatabase(DB_NAME);
		MongoCollection<BasicDBObject> collection = db.getCollection(collectionName, BasicDBObject.class);
		DeleteResult result = collection.deleteMany(query);
		return result.wasAcknowledged();
	}
	public static boolean isEmpty(Collection collection) {
		return (collection == null || collection.isEmpty());
	}
}
