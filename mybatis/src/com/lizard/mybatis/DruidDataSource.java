package com.lizard.mybatis;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.DataSourceFactory;
import org.apache.ibatis.datasource.pooled.PooledDataSource;

public class DruidDataSource implements DataSourceFactory {

	@Override
	public void setProperties(Properties arg0) {
	}

	@Override
	public DataSource getDataSource() {

		return new PooledDataSource();
	}

}
