package com.igg.boot.framework.autoconfigure.db;

public class DataSourceHolder {
	private static ThreadLocal<String> DATASOURCES = new ThreadLocal<>();
    
    public static void setDatasource(String datasource) {
        DATASOURCES.set(datasource);
    }

    public static String getDatasource() {
        return DATASOURCES.get();
    }

    public static void removeDatasource() {
        DATASOURCES.remove();
    }
}
