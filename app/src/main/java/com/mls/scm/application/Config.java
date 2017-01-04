package com.mls.scm.application;

import android.os.Environment;


public class Config {
    public static final String BASE_PATH = Environment.getExternalStorageDirectory() + "/hbt";
    public static final String IMAGE_CACHE_PATH = BASE_PATH + "/img_cache";
    public static final String DOC_PATH = BASE_PATH + "/document/";
    public static final String PIC_PATH = BASE_PATH + "/pic";
    public static final String LOG_PATH = BASE_PATH + "/log";
}
