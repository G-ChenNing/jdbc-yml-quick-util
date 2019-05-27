package com.github.wangchenning.leaf.util.sql;

import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.github.wangchenning.leaf.util.json.JsonObj;

public interface MQuery {
	default String select(EntityManager em, JsonObj args) throws JsonProcessingException {
		throw new UnsupportedOperationException("不支持");
	}
	@Deprecated
	default void delete(EntityManager em, JsonObj args) {
		throw new UnsupportedOperationException("不支持");
	}
	@Deprecated
	default void update(EntityManager em, JsonObj args) {
		throw new UnsupportedOperationException("不支持");
	}
	@Deprecated
	default String create(EntityManager em, JsonObj args) {
		throw new UnsupportedOperationException("不支持");
	}
	
	default void delete(DataSource dataSource, JsonObj args) throws SQLException {
		throw new UnsupportedOperationException("不支持");
	}
	default void update(DataSource dataSource, JsonObj args) throws SQLException {
		throw new UnsupportedOperationException("不支持");
	}
	default String create(DataSource dataSource, JsonObj args) throws SQLException {
		throw new UnsupportedOperationException("不支持");
	}
	
	default void batchInsert(DataSource dataSource, JsonObj args) throws SQLException {
		throw new UnsupportedOperationException("不支持");
	}
	default void batchUpdate(DataSource dataSource, JsonObj args) throws SQLException {
		throw new UnsupportedOperationException("不支持");
	}
	default SQLGroupResult select(DataSource dataSource, JsonObj args) throws SQLException {
		throw new UnsupportedOperationException("不支持");
	}
}
