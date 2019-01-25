package com.zilker.onlinejobsearch.utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.zilker.onlinejobsearch.constants.QueryConstants;

/*
 * class for establishing connection with database and closes connection
 */
public class DButils {

	private static final Logger logger = Logger.getLogger(DButils.class.getName());

	/*
	 * establishes connection
	 */
	public static Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(QueryConstants.CONNECTIONSTRING,QueryConstants.USERNAME,QueryConstants.PASSWORD);
			//logger.log(Level.INFO, "connected");
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error connecting with SQL Driver");
		}
		return conn;
	}
	/*
	 * closes connection
	 */

	public static void closeConnection(Connection conn, PreparedStatement pst, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (pst != null) {
				pst.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error closing the connection variables");
		}
	}

}
