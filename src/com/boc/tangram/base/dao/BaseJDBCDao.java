package com.boc.tangram.base.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

/**
 * JDBC DAO层基类
 * 
 * 主要功能：
 * 1.支持分页；
 * 2.查询结果集的列信息；
 */
public class BaseJDBCDao extends JdbcDaoSupport {
	/**
	 * 支持分页
	 * 
	 * @param sql
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public List<Map<String, Object>> queryForList(String sql, int currentPage, int pageSize) {
		int end = currentPage * pageSize;
		int start = end - pageSize + 1;
		StringBuffer strbuf = new StringBuffer("SELECT * FROM (SELECT T.*,ROWNUM AS ROWINDEX FROM (");
		strbuf.append(sql).append(") T WHERE ROWNUM <= ").append(end).append(") WHERE  ROWINDEX >= ").append(start);

		return super.getJdbcTemplate().queryForList(strbuf.toString());
	}

	/**
	 * 查询结果集的列信息
	 * 
	 * @param sql
	 *            SELECT语句
	 * @return String[] 结果集信息
	 */
	public String[] getResultColum(String sql) {
		StringBuffer strbuf = new StringBuffer("SELECT * FROM (" + sql + ") WHERE ROWNUM <= 0");
		SqlRowSet sqlrowset = super.getJdbcTemplate().queryForRowSet(strbuf.toString());
		SqlRowSetMetaData rsmd = sqlrowset.getMetaData();
		int colcumnCount = rsmd.getColumnCount();
		String[] columnName = new String[colcumnCount];
		for (int i = 0; i < colcumnCount; i++) {
			columnName[i] = rsmd.getColumnName(i + 1);
		}

		return columnName;
	}
}