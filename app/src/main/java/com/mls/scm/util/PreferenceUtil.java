package com.mls.scm.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Map;


public class PreferenceUtil {
    
    private SharedPreferences mShared;
    
    private Editor mEditor;
    

    public PreferenceUtil(Context context, String kvName, int mode) {
        mShared = context.getSharedPreferences(kvName, mode);
        mEditor = mShared.edit();
    }
    

    public boolean getBoolean(String key, boolean defValue) {
        return mShared.getBoolean(key, defValue);
    }
    
 
    public int getInt(String key, int defValue) {
        return mShared.getInt(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return mShared.getLong(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        return mShared.getFloat(key, defValue);
    }
    
 
    public String getString(String key, String defValue) {
        return mShared.getString(key, defValue);
    }
    
 
    public Map<String, ?> getAll() {
        return mShared.getAll();
    }
    
 
    public PreferenceUtil put(String key, Object value) {
        if (value instanceof Boolean) {
            mEditor.putBoolean(key, (Boolean)value).commit();
        } else if (value instanceof Integer || value instanceof Byte) {
            mEditor.putInt(key, (Integer)value).commit();
        } else if (value instanceof Long) {
            mEditor.putLong(key, (Long)value).commit();
        } else if (value instanceof Float) {
            mEditor.putFloat(key, (Float)value).commit();
        } else if (value instanceof Double) {
            mEditor.putFloat(key, Float.parseFloat(value.toString())).commit();
        } else if (value instanceof String) {
            mEditor.putString(key, (String)value).commit();
        } else {
            if (null == value) {
                value = "";
            }
            mEditor.putString(key, value.toString()).commit();
        }
        return this;
    }
    

    public PreferenceUtil remove(String key) {
        mEditor.remove(key).commit();
        return this;
    }

    public PreferenceUtil clear() {
        mEditor.clear().commit();
        return this;
    }
    

    public boolean contains(String key) {
        return mShared.contains(key);
    }
}
