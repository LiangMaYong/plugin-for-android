package com.liangmayong.androidplugin.management.db;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * APDatabaseTable
 *
 * @author LiangMaYong
 * @version 1.0
 */
public abstract class APDatabaseTable {
    private String tableName;
    private String databaseName;
    private Helper helper;
    private Database kit;
    private Inser inser;

    private DatabaseModel newModel() {
        return new DatabaseModel(this);
    }

    private Database getKit() {
        return kit;
    }

    public DatabaseModel getModel(long id) {
        return getKit().getModel(id);
    }

    public DatabaseModel getModel(String where) {
        return getKit().getModel(where);
    }

    /**
     * get model
     *
     * @param where   where
     * @param orderBy orderBy
     * @return DatabaseModel
     */
    public DatabaseModel getModel(String where, String orderBy) {
        return getKit().getModel(where, orderBy);
    }

    /**
     * DatabaseCollection
     *
     * @param pager pager
     * @return DatabaseCollection
     */
    public DatabaseCollection collection(APDatabasePager pager) {
        return new DatabaseCollection(pager, this);
    }

    /**
     * inser
     *
     * @return inser
     */
    public Inser inser() {
        if (inser == null) {
            inser = new Inser(this);
        }
        return inser;
    }

    private abstract class Helper extends SQLiteOpenHelper {
        public Helper(Context context, String name, int version) {
            super(context, name, null, version);
        }

        @Override
        public abstract void onCreate(SQLiteDatabase db);

        @Override
        public abstract void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
    }

    /**
     * Inser
     *
     * @author LiangMaYong
     * @version 1.0
     */
    public static class Inser {
        private DatabaseModel dataModel;
        private APDatabaseTable table;

        private Inser(APDatabaseTable table) {
            this.table = table;
            this.dataModel = new DatabaseModel(table);
        }

        public Inser put(String key, Object value) {
            this.dataModel.put(key, value);
            return this;
        }

        public Inser putAll(ContentValues contentValues) {
            this.dataModel.putAll(contentValues);
            return this;
        }

        public long commit() {
            long count = table.getKit().add(dataModel);
            this.dataModel.removeAll();
            return count;
        }
    }

    /**
     * query
     *
     * @param distinct      distinct
     * @param columns       columns
     * @param whereString   whereString
     * @param selectionArgs selectionArgs
     * @param groupBy       groupBy
     * @param having        having
     * @param orderBy       orderBy
     * @param limit         limit
     * @return cursor
     */
    public Cursor query(boolean distinct, String[] columns, String whereString, String[] selectionArgs, String groupBy,
                        String having, String orderBy, String limit) {
        return kit.query(distinct, columns, whereString, selectionArgs, groupBy, having, orderBy, limit);
    }

    /**
     * delete
     *
     * @param where where
     * @return lines
     */
    public int delete(String where) {
        return kit.delete(where);
    }

    /**
     * delete all
     *
     * @return lines
     */
    public int deleteAll() {
        return kit.deleteAll();
    }

    /**
     * delete
     *
     * @param id id
     * @return lines
     */
    public int delete(long id) {
        return kit.delete(id);
    }

    /**
     * delete
     *
     * @param whereClause whereClause
     * @param whereArgs   whereArgs
     * @return lines
     */
    public int delete(String whereClause, String[] whereArgs) {
        return kit.delete(whereClause, whereArgs);
    }

    /**
     * updata
     *
     * @param contentValues contentValues
     * @param where         where
     * @param whereArgs     whereArgs
     * @return lines
     */
    public int updata(ContentValues contentValues, String where, String[] whereArgs) {
        return kit.updata(contentValues, where, whereArgs);
    }

    /**
     * updata
     *
     * @param contentValues contentValues
     * @param where         where
     * @return lines
     */
    public int updata(ContentValues contentValues, String where) {
        return kit.updata(contentValues, where);
    }

    private SQLiteDatabase getReadableDatabase() {
        return helper.getReadableDatabase();
    }

    private SQLiteDatabase getWritableDatabase() {
        return helper.getWritableDatabase();
    }

    /**
     * APDatabaseTable
     *
     * @param context      context
     * @param tableName    tableName
     * @param databaseName databaseName
     * @param version      version
     */
    public APDatabaseTable(final Context context, String tableName, String databaseName, int version) {
        this.helper = new Helper(context, databaseName, version) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL(getCreateSQL());
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                APDatabaseTable.this.onUpgrade(db, oldVersion, newVersion);
            }
        };
        this.databaseName = databaseName;
        this.tableName = tableName;
        this.kit = Database.newInstance(this);
    }

    /**
     * get DatabaseName
     *
     * @return databaseName
     */
    public String getDatabaseName() {
        return databaseName;
    }

    /**
     * get TableName
     *
     * @return tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * get count
     *
     * @return count
     */
    public int getCount() {
        return kit.getCount();
    }

    /**
     * get count
     *
     * @param where where
     * @return count
     */
    public int getCount(String where) {
        return kit.getCount(where);
    }

    /**
     * get count
     *
     * @param where    where
     * @param distinct distinct
     * @return count
     */
    public int getCount(String where, boolean distinct) {
        return kit.getCount(where, distinct);
    }

    /**
     * get createSQL
     *
     * @return createSQL
     */
    private final String getCreateSQL() {
        if (getFields() == null) {
            return "";
        }
        String start = "create table IF NOT EXISTS " + tableName + " (_id" + " INTEGER PRIMARY KEY AUTOINCREMENT,";
        String end = ");";
        String content = "";
        for (Entry<String, APDatabaseType> entry : getFields().entrySet()) {
            content += entry.getKey();
            content += " " + entry.getValue() + "";
            content += ",";
        }
        if (getFields().size() > 0) {
            content = content.substring(0, content.length() - 1);
        } else {
            return "";
        }
        return start + content + end;
    }

    @Override
    public String toString() {
        return "[tableName=" + tableName + ", databaseName=" + databaseName + "]";
    }

    /**
     * get fields
     *
     * @return fields
     */
    public abstract Map<String, APDatabaseType> getFields();

    /**
     * onUpgrade
     *
     * @param db         db
     * @param oldVersion oldVersion
     * @param newVersion newVersion
     */
    protected abstract void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

    /**
     * DatabaseCollection
     *
     * @author LiangMaYong
     * @version 1.0
     */
    public static final class DatabaseCollection {
        private String tableName;
        private String databaseName;
        private Database kit;
        private APDatabasePager dataPager;
        private List<String> fields = new ArrayList<String>();
        private List<DatabaseModel> list = new ArrayList<DatabaseModel>();

        /**
         * remove all
         */
        public void removeAll() {
            list.removeAll(list);
        }

        @Override
        public String toString() {
            return list.toString();
        }

        public void setDataPager(APDatabasePager dataPager) {
            this.dataPager = dataPager;
        }

        /**
         * get list
         *
         * @return list
         */
        public List<DatabaseModel> getList() {
            return list;
        }

        public APDatabasePager getDataPager() {
            return dataPager;
        }

        /**
         * get databaseName
         *
         * @return databaseName
         */
        public String getDatabaseName() {
            return databaseName;
        }

        /**
         * get tableName
         *
         * @return tableName
         */
        public String getTableName() {
            return tableName;
        }

        void add(DatabaseModel model) {
            if (model != null) {
                list.add(model);
            }
        }

        /**
         * DatabaseCollection
         *
         * @param pager pager
         * @param table table
         */
        @SuppressLint("NewApi")
        private DatabaseCollection(APDatabasePager pager, APDatabaseTable table) {
            this.tableName = table.getTableName();
            this.databaseName = table.getDatabaseName();
            this.kit = table.getKit();
            this.dataPager = pager;
            List<String> strings = this.kit.getFields();
            for (int i = 0; i < strings.size(); i++) {
                if (!fields.contains(strings.get(i))) {
                    fields.add(strings.get(i));
                }
            }
        }

        /**
         * sync
         *
         * @return DatabaseCollection
         */
        public DatabaseCollection sync() {
            kit.sync(this);
            return this;
        }

        /**
         * sync
         *
         * @param where where
         * @return DatabaseCollection
         */
        public DatabaseCollection sync(String where) {
            kit.sync(this, where);
            return this;
        }

        /**
         * sync
         *
         * @param where    where
         * @param distinct distinct
         * @return DatabaseCollection
         */
        public DatabaseCollection sync(String where, boolean distinct) {
            kit.sync(this, distinct, where);
            return this;
        }

        /**
         * sync
         *
         * @param where    where
         * @param distinct distinct
         * @param orderBy  orderBy
         * @return DatabaseCollection
         */
        public DatabaseCollection sync(String where, boolean distinct, String orderBy) {
            kit.sync(this, distinct, null, where, null, null, null, orderBy, null);
            return this;
        }
    }

    /**
     * DatabaseModel
     *
     * @author LiangMaYong
     * @version 1.0
     */
    public static final class DatabaseModel implements Serializable {

        private static final long serialVersionUID = 1L;
        private ContentValues values = new ContentValues();
        private List<String> fields = new ArrayList<String>();
        private APDatabaseTable table;

        /**
         * get fields
         *
         * @return fields
         */
        public List<String> getFields() {
            return fields;
        }

        @Override
        public String toString() {
            return "[" + values + "]";
        }

        /**
         * init
         *
         * @param table table
         */
        @SuppressLint("NewApi")
        private DatabaseModel(APDatabaseTable table) {
            this.table = table;
            List<String> strings = table.getKit().getFields();
            for (int i = 0; i < strings.size(); i++) {
                if (!fields.contains(strings.get(i))) {
                    fields.add(strings.get(i));
                }
            }
        }

        /**
         * update
         *
         * @return lines
         */
        public int update() {
            try {
                return table.getKit().updata(this);
            } catch (Exception e) {
                return 0;
            }
        }

        /**
         * delete
         *
         * @return lines
         */
        public int delete() {
            try {
                int del = table.getKit().delete(this);
                if (del > 0) {
                    removeAll();
                }
                return del;
            } catch (Exception e) {
                return 0;
            }
        }

        /**
         * get values
         *
         * @return values
         */
        public ContentValues getValues() {
            return values;
        }

        /**
         * put all
         *
         * @param contentValues values
         */
        private void putAll(ContentValues contentValues) {
            this.values.putAll(contentValues);
        }

        /**
         * remove all
         */
        private void removeAll() {
            this.values.clear();
        }

        /**
         * put
         *
         * @param key   key
         * @param value value
         * @return DatabaseModel
         */
        public DatabaseModel put(String key, Object value) {
            if (!fields.contains(key)) {
                return this;
            }
            if (value instanceof Integer) {
                values.put(key, ((Integer) value).intValue());
            } else if (value instanceof String) {
                values.put(key, (String) value);
            } else if (value instanceof Double) {
                double d = ((Double) value).doubleValue();
                values.put(key, d);
            } else if (value instanceof Float) {
                float f = ((Float) value).floatValue();
                values.put(key, f);
            } else if (value instanceof Long) {
                long l = ((Long) value).longValue();
                values.put(key, l);
            } else if (value instanceof Boolean) {
                boolean b = ((Boolean) value).booleanValue();
                values.put(key, b);
            } else if (value instanceof byte[]) {
                byte[] b = (byte[]) value;
                values.put(key, b);
            } else if (value instanceof Byte) {
                Byte b = (Byte) value;
                values.put(key, b);
            } else if (value instanceof Short) {
                Short b = (Short) value;
                values.put(key, b);
            } else if (value instanceof Long) {
                Long l = (Long) value;
                values.put(key, l);
            } else {
                values.putNull(key);
            }
            return this;
        }

        /**
         * get boolean
         *
         * @param key key
         * @return true or false
         */
        public boolean getBoolean(String key) {
            boolean value = false;
            if (values.containsKey(key)) {
                try {
                    value = Boolean.valueOf(values.get(key).toString());
                } catch (Exception e) {
                }
            }
            return value;
        }

        private byte[] toByteArray(Object obj) {
            byte[] bytes = null;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(obj);
                oos.flush();
                bytes = bos.toByteArray();
                oos.close();
                bos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return bytes;
        }

        /**
         * get byte[]
         *
         * @param key key
         * @return byte[]
         */
        public byte[] getBlob(String key) {
            if (values.containsKey(key)) {
                try {
                    return toByteArray(values.get(key));
                } catch (Exception e) {
                }
            }
            return null;
        }

        /**
         * get string
         *
         * @param key key
         * @return string
         */
        public String getString(String key) {
            if (values.containsKey(key)) {
                try {
                    return values.get(key).toString();
                } catch (Exception e) {
                }
            }
            return null;
        }

        /**
         * get float
         *
         * @param key key
         * @return float
         */
        public Float getFloat(String key) {
            if (values.containsKey(key)) {
                try {
                    return Float.parseFloat(values.get(key).toString());
                } catch (Exception e) {
                }
            }
            return null;
        }

        /**
         * get integer
         *
         * @param key key
         * @return integer
         */
        public Integer getInteger(String key) {
            if (values.containsKey(key)) {
                try {
                    return Integer.parseInt(values.get(key).toString());
                } catch (Exception e) {
                }
            }
            return null;
        }

        /**
         * get
         *
         * @param key key
         * @return object
         */
        public Object get(String key) {
            if (values.containsKey(key)) {
                return values.get(key);
            } else {
                return null;
            }
        }

        /**
         * get id
         *
         * @return id
         */
        public int getId() {
            if (values.containsKey("_id")) {
                return values.getAsInteger("_id");
            } else {
                return 0;
            }
        }
    }

    /**
     * Database
     *
     * @author LiangMaYong
     * @version 1.0
     */
    @SuppressLint("NewApi")
    private static class Database {

        @Override
        public String toString() {
            return "DatabaseKit [tableName=" + _tableName + ",databaseName=" + _databaseName + "]";
        }

        private String _tableName;
        private String _databaseName;
        private APDatabaseTable _table;

        private Database(final APDatabaseTable table) {
            this._table = table;
            this._tableName = table.getTableName();
            this._databaseName = table.getDatabaseName();
        }

        public static Database newInstance(APDatabaseTable table) {
            return new Database(table);
        }

        final int getCount() {
            int count = 0;
            SQLiteDatabase db = _table.getReadableDatabase();
            String sql = "select count(*) from " + _tableName + ";";
            Cursor cursor = db.rawQuery(sql, null);
            try {
                if (cursor.moveToNext()) {
                    count = cursor.getInt(0);
                }
            } finally {
                if (!cursor.isClosed()) {
                    cursor.close();
                }
                db.close();
                db = null;
            }
            return count;
        }

        final int getCount(String where, boolean distinct) {
            return getCount(distinct, null, where, null, null, null, null, null);
        }

        final int getCount(String where) {
            return getCount(false, null, where, null, null, null, null, null);
        }

        private final int getCount(boolean distinct, String[] columns, String whereString, String[] selectionArgs,
                                   String groupBy, String having, String orderBy, String limit) {
            int count = 0;
            SQLiteDatabase db = _table.getReadableDatabase();
            Cursor cursor = db.query(distinct, _tableName, columns, whereString, selectionArgs, groupBy, having,
                    orderBy, limit);
            try {
                count = cursor.getCount();
            } finally {
                if (!cursor.isClosed()) {
                    cursor.close();
                }
                db.close();
                db = null;
            }
            return count;
        }

        /********************
         * List
         ***********************/
        void sync(DatabaseCollection models) {
            String limit = null;
            if (models.getDataPager() != null) {
                limit = models.getDataPager().getLimit();
                models.getDataPager().setTotal(getCount());
            }
            sync(models, false, null, null, null, null, null, null, limit);
        }

        void sync(DatabaseCollection models, String where) {
            String limit = null;
            if (models.getDataPager() != null) {
                limit = models.getDataPager().getLimit();
                models.getDataPager().setTotal(getCount(where));
            }
            sync(models, false, null, where, null, null, null, null, limit);
        }

        void sync(DatabaseCollection models, boolean distinct, String where) {
            String limit = null;
            if (models.getDataPager() != null) {
                limit = models.getDataPager().getLimit();
                models.getDataPager().setTotal(getCount(where, distinct));
            }
            sync(models, distinct, null, where, null, null, null, null, limit);
        }

        private final void sync(DatabaseCollection models, boolean distinct, String[] columns, String whereString,
                                String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
            SQLiteDatabase db = _table.getReadableDatabase();
            Cursor cursor = db.query(distinct, _tableName, columns, whereString, selectionArgs, groupBy, having,
                    orderBy, limit);
            try {
                models.removeAll();
                while (cursor.moveToNext()) {
                    models.add(toItem(cursor));
                }
            } finally {
                if (!cursor.isClosed()) {
                    cursor.close();
                }
                db.close();
                db = null;
            }
        }

        final Cursor query(boolean distinct, String[] columns, String whereString, String[] selectionArgs,
                           String groupBy, String having, String orderBy, String limit) {
            SQLiteDatabase db = _table.getReadableDatabase();
            Cursor cursor = db.query(distinct, _tableName, columns, whereString, selectionArgs, groupBy, having,
                    orderBy, limit);
            db.close();
            return cursor;
        }

        /***********************
         * Item
         **********************/

        DatabaseModel getModel(long id) {
            return getModel("_id = '" + id + "'");
        }

        DatabaseModel getModel(String where) {
            return getModel(false, null, where, null, null, null, null, null);
        }

        DatabaseModel getModel(String where, String orderBy) {
            return getModel(false, null, where, null, null, null, orderBy, null);
        }

        private final DatabaseModel getModel(boolean distinct, String[] columns, String selection,
                                             String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
            SQLiteDatabase db = _table.getReadableDatabase();
            Cursor cursor = db.query(distinct, _tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            DatabaseModel model = null;
            try {
                if (cursor.moveToNext()) {
                    model = toItem(cursor);
                }
            } finally {
                if (!cursor.isClosed()) {
                    cursor.close();
                }
                db.close();
                db = null;
            }
            return model;
        }

        /*********************************************/

        /**
         * add
         *
         * @param contentValues contentValues
         * @return id
         */
        private final long add(ContentValues contentValues) {
            long rowid = 0;
            if (contentValues != null) {
                contentValues.remove("_id");
                SQLiteDatabase db = _table.getWritableDatabase();
                try {
                    rowid = db.insert(_tableName, null, contentValues);
                } finally {
                    db.close();
                    db = null;
                }
            }
            return rowid;
        }

        final long add(DatabaseModel item) {
            return add(item.getValues());
        }

        /******************************************************************/
        final int updata(ContentValues contentValues, String whereClause, String[] whereArgs) {
            int count = 0;
            SQLiteDatabase db = _table.getWritableDatabase();
            try {
                count = db.update(_tableName, contentValues, whereClause, whereArgs);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
                db = null;
            }
            return count;
        }

        final int updata(ContentValues contentValues, String where) {
            return updata(contentValues, where, null);
        }

        final int updata(DatabaseModel item) {
            return updata(item.getValues(), "_id = '" + item.getId() + "'");
        }

        final int delete(String where) {
            int count = 0;
            SQLiteDatabase db = _table.getWritableDatabase();
            try {
                count = db.delete(_tableName, where, null);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
                db = null;
            }
            return count;
        }

        final int delete(String whereClause, String[] whereArgs) {
            int count = 0;
            SQLiteDatabase db = _table.getWritableDatabase();
            try {
                count = db.delete(_tableName, whereClause, whereArgs);
            } finally {
                db.close();
                db = null;
            }
            return count;
        }

        final int deleteAll() {
            int count = 0;
            try {
                count = delete("", new String[]{});
            } catch (Exception e) {
            }
            return count;
        }

        final int delete(long id) {
            return delete("_id = '" + id + "'");
        }

        final int delete(DatabaseModel item) {
            int del = delete("_id = '" + item.getId() + "'");
            if (del > 0) {
                item.getValues().remove("_id");
            }
            return del;
        }

        /**********************************************/

        private DatabaseModel toItem(Cursor cursor) {
            String[] name = cursor.getColumnNames();
            DatabaseModel model = _table.newModel();
            for (int i = 0; i < name.length; i++) {
                int columnIndex = cursor.getColumnIndex(name[i]);
                int columnType = cursor.getType(columnIndex);
                if (columnType == Cursor.FIELD_TYPE_BLOB) {
                    model.put(name[i], cursor.getBlob(columnIndex));
                } else if (columnType == Cursor.FIELD_TYPE_FLOAT) {
                    model.put(name[i], cursor.getDouble(columnIndex));
                } else if (columnType == Cursor.FIELD_TYPE_INTEGER) {
                    model.put(name[i], cursor.getLong(columnIndex));
                } else if (columnType == Cursor.FIELD_TYPE_NULL) {
                    model.put(name[i], null);
                } else if (columnType == Cursor.FIELD_TYPE_STRING) {
                    model.put(name[i], cursor.getString(columnIndex));
                }
            }
            return model;
        }

        @SuppressLint("SdCardPath")
        private static String getDatabaseDir(Context context) {
            return "/data/data/" + context.getPackageName() + "/databases/";
        }

        List<String> getFields() {
            List<String> strings = new ArrayList<String>();
            SQLiteDatabase db = _table.getReadableDatabase();
            Cursor cursor = db.query(_tableName, null, null, null, null, null, null);
            try {
                String[] strs = cursor.getColumnNames();
                for (int i = 0; i < strs.length; i++) {
                    strings.add(strs[i]);
                }
            } finally {
                if (!cursor.isClosed()) {
                    cursor.close();
                }
                db.close();
                db = null;
            }
            return strings;
        }
    }
}
