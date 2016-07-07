package com.liangmayong.androidplugin.management.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.liangmayong.androidplugin.management.APluginInfo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * APTable
 * 
 * @author LiangMaYong
 * @version 1.0
 */
public class APTable extends APDatabaseTable {

	private static final String DATABASE_NAME = "ANDROID_PLUGINS.DB";
	private static final String TABLE_NAME = "plugins";
	private static final int DATABASE_VERSION = 1;
	// value map
	private static final Map<String, String> valueMap = new HashMap<String, String>();

	public APTable(Context context) {
		super(context, TABLE_NAME, DATABASE_NAME, DATABASE_VERSION);
	}

	@Override
	public Map<String, APDatabaseType> getFields() {
		Map<String, APDatabaseType> map = new HashMap<String, APDatabaseType>();
		map.put("name", APDatabaseType.TEXT);
		map.put("path", APDatabaseType.TEXT);
		return map;
	}

	private boolean del(String name) {
		if (valueMap.containsKey(name)) {
			valueMap.remove(name);
		}
		return delete("name='" + name + "'") > 0;
	}

	/**
	 * getList
	 * 
	 * @return info list
	 */
	public List<APluginInfo> getList() {
		List<APluginInfo> list = new ArrayList<APluginInfo>();
		List<DatabaseModel> models = collection(null).sync().getList();
		for (int i = 0; i < models.size(); i++) {
			DatabaseModel databaseModel = models.get(i);
			APluginInfo info = new APluginInfo();
			info.setPackageName(databaseModel.getString("name"));
			info.setPluginPath(databaseModel.getString("path"));
			list.add(info);
		}
		return list;
	}

	/**
	 * get
	 * 
	 * @param name
	 *            name
	 * @return path
	 */
	public String get(String name) {
		if (valueMap.containsKey(name)) {
			return valueMap.get(name);
		}
		DatabaseModel model = getModel("name='" + name + "'");
		if (model != null) {
			String value = model.getString("path");
			valueMap.put(name, value);
			return value;
		}
		return "";
	}

	/**
	 * isExists
	 * 
	 * @param name
	 *            name
	 * @return true or false
	 */
	public boolean isExists(String name) {
		return getCount("name='" + name + "'") > 0;
	}

	/**
	 * add
	 * 
	 * @param name
	 *            name
	 * @param path
	 *            path
	 * @return true or false
	 */
	public boolean set(String name, String path) {
		if (path == null) {
			del(name);
			return true;
		}
		if (isExists(name)) {
			return false;
		}
		valueMap.put(name, path);
		boolean flag = inser().put("name", name).put("path", path).commit() > 0;
		return flag;
	}

	@Override
	protected void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
