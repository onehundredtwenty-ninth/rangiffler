package com.onehundredtwentyninth.rangiffler.db;

import com.onehundredtwentyninth.rangiffler.config.Config;
import com.p6spy.engine.spy.P6DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;
import org.postgresql.ds.PGSimpleDataSource;

public enum DataSourceProvider {

  INSTANCE;

  private static final Config CFG = Config.getInstance();
  private final Map<JdbcUrl, DataSource> store = new ConcurrentHashMap<>();

  public DataSource dataSource(JdbcUrl jdbcUrl) {
    return store.computeIfAbsent(jdbcUrl, k -> {
      var ds = new PGSimpleDataSource();
      ds.setURL(k.getUrl());
      ds.setUser(CFG.jdbcUser());
      ds.setPassword(CFG.jdbcPassword());
      return new P6DataSource(ds);
    });
  }
}
