package com.mrsweeter.dreamhelper.SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.mrsweeter.dreamhelper.Color;
import com.mrsweeter.dreamhelper.DreamHelper;

public class SQLConnection {
	
	private Connection link;
	private String url;
	private String host;
	private String db;
	private String user;
	private String password;
	
	public SQLConnection(String paramUrl, String paramHost, String paramDb, String paramUser, String paramPassword)	{
		
		url = paramUrl;
		host = paramHost;
		db = paramDb;
		user = paramUser;
		password = paramPassword;
	}
	
	public void connection()	{
		
		if(!isConnected())	{
			
			try	{
				link = DriverManager.getConnection(url + host + "/" + db, user, password);
				createTicketsTable();
				DreamHelper.getLog().info(Color.GREEN + "Connection DB complete" + Color.RESET);
			} catch (SQLException e)	{
				e.printStackTrace();
			}
		}
	}
	
	public void disconnetion()	{
		
		if(isConnected())	{
			
			try	{
				link.close();
				DreamHelper.getLog().info(Color.GREEN + "Disconnection DB complete" + Color.RESET);
			} catch (SQLException e)	{
				e.printStackTrace();
			}
		}
	}
	
	public void createTicketsTable()	{
		
		String request = "CREATE TABLE IF NOT EXISTS dreamhelper_tickets ("
				+ "ticket_id int(11) PRIMARY KEY AUTO_INCREMENT,"
				+ "ticket_msg varchar(600) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,"
				+ "ticket_x double(10,3) NOT NULL,"
				+ "ticket_y double(10,3) NOT NULL,"
				+ "ticket_z double(10,3) NOT NULL,"
				+ "ticket_yaw double(10,3) NOT NULL,"
				+ "ticket_pitch double(10,3) NOT NULL,"
				+ "ticket_world varchar(300) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,"
				+ "ticket_player varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,"
				+ "ticket_pAssigned varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',"
				+ "ticket_status varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT 'submit',"
				+ "ticket_date datetime NOT NULL,"
				+ "ticket_reply varchar(300) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',"
				+ "ticket_replyStatus tinyint(1) NOT NULL DEFAULT '-1'"
		+ ")";
		
		try	{
			
			PreparedStatement r = link.prepareStatement(request);
			r.execute();
			r.close();
			
		} catch (SQLException e)	{
			e.printStackTrace();
		}
		
	}
	
	public boolean isConnected(){
        return link != null;
    }
	public Connection getLink()	{
		return link;
	}
}
