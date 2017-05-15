package com.mrsweeter.dreamhelper.SQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mrsweeter.dreamhelper.Color;
import com.mrsweeter.dreamhelper.DreamHelper;
import com.mrsweeter.dreamhelper.Language;

public class Tickets {
	
	public static ResultSet seenTickets(Connection sql, String status)	{
		
		String request = "SELECT * FROM dreamhelper_tickets WHERE ticket_status IN (" + status + ")";
		ResultSet results = null;
		
		try	{
			PreparedStatement r = sql.prepareStatement(request);
			results = r.executeQuery();
			
		} catch (SQLException e)	{
			e.printStackTrace();
		}
		return results;
	}
	
	public static ResultSet seenPlayerTickets(Connection sql, String player)	{
		
		String request = "SELECT * FROM dreamhelper_tickets WHERE ticket_player = '" + player + "'";
		ResultSet results = null;
		
		try	{
			PreparedStatement r = sql.prepareStatement(request);
			results = r.executeQuery();
			
		} catch (SQLException e)	{
			e.printStackTrace();
		}
		return results;
	}

	public static void createTicket(Player p, String[] args, Connection sql) {
		
		String request = "INSERT INTO dreamhelper_tickets (ticket_msg, ticket_x, ticket_y, ticket_z, ticket_yaw, ticket_pitch, ticket_world, ticket_player, ticket_date)"
				+ " VALUES ('"
				+ DreamHelper.arraysToString(args, 0) + "',"
				+ p.getLocation().getX() + ","
				+ p.getLocation().getY() + ","
				+ p.getLocation().getZ() + ","
				+ p.getLocation().getYaw() + ","
				+ p.getLocation().getPitch() + ",'"
				+ p.getLocation().getWorld().getName() + "','"
				+ p.getName() + "',"
				+ "CURRENT_TIME)";
		
		try {
			PreparedStatement r = sql.prepareStatement(request);
			r.execute();
			r.close();
			
			p.sendMessage(Language.prefix + Language.ticketSend);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Map<String, String> takeTicket(Player p, String id, Connection sql)	{
		
		String request = "UPDATE dreamhelper_tickets SET ticket_pAssigned = '" + p.getName() + "', ticket_status = 'assigned' "
				+ "WHERE ticket_id = " + Integer.parseInt(id) + " AND ticket_status IN ('assigned', 'submit')";
		Map<String, String> ticket = new HashMap<String, String>();
		
		try	{
			PreparedStatement r = sql.prepareStatement(request);
			r.execute();
			r.close();
			
			request = "SELECT * FROM dreamhelper_tickets WHERE ticket_id = " + Integer.parseInt(id)+ " AND ticket_status IN ('assigned', 'submit')";
			
			try {
				r = sql.prepareStatement(request);
				ResultSet result = r.executeQuery();
				
				result.last();
				if (result.getRow() == 1)	{
					ResultSetMetaData metadata = result.getMetaData();
					
					for (int i = 1; i <= metadata.getColumnCount(); i++)	{
						String key = metadata.getColumnName(i);
						String value = result.getString(key);
						ticket.put(key, value);
					}
				} else if (result.getRow() > 1)	{
					DreamHelper.getLog().warning(Color.RED + "More than one result -- Check id of ticket" + Color.RESET);
				}
				r.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ticket;
	}

	public static void closeTicket(CommandSender sender, String id, Connection sql) {
		
		String request = "UPDATE dreamhelper_tickets SET ticket_status = 'closed' "
				+ "WHERE ticket_id = " + Integer.parseInt(id);
		
		try {
			PreparedStatement r = sql.prepareStatement(request);
			r.execute();
			r.close();
			
			sender.sendMessage(Language.prefix + Language.ticketClose.replace("{ID}", id));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public static void deleteTicket(CommandSender sender, String id, Connection sql) {
		
		String request = "DELETE FROM dreamhelper_tickets WHERE ticket_id = " + Integer.parseInt(id);
		
		try {
			PreparedStatement r = sql.prepareStatement(request);
			r.execute();
			r.close();
			
			sender.sendMessage(Language.prefix + Language.ticketDelete.replace("{ID}", id));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public static Map<String, String> checkTicket(Player p, String id, Connection sql) {
		
		String request = "UPDATE dreamhelper_tickets SET ticket_pAssigned = '" + p.getName() + "', ticket_status = 'assigned' "
				+ "WHERE ticket_id = " + Integer.parseInt(id);
		Map<String, String> ticket = new HashMap<String, String>();
		
		try	{
			PreparedStatement r = sql.prepareStatement(request);
			r.execute();
			r.close();
			
			request = "SELECT * FROM dreamhelper_tickets WHERE ticket_id = " + Integer.parseInt(id);
			
			try {
				r = sql.prepareStatement(request);
				ResultSet result = r.executeQuery();
				
				result.last();
				if (result.getRow() == 1)	{
					ResultSetMetaData metadata = result.getMetaData();
					
					for (int i = 1; i <= metadata.getColumnCount(); i++)	{
						String key = metadata.getColumnName(i);
						String value = result.getString(key);
						ticket.put(key, value);
					}
				} else if (result.getRow() > 1)	{
					DreamHelper.getLog().warning(Color.RED + "More than one result -- Check id of ticket" + Color.RESET);
				}
				r.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ticket;
		
	}

	public static void purgeTickets(Connection sql, String status) {
		
		String request = "DELETE FROM dreamhelper_tickets WHERE ticket_status IN (" + status + ")";
		
		try {
			PreparedStatement r = sql.prepareStatement(request);
			r.execute();
			r.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public static void purgeTimedTickets(Connection sql) {
		
		String request = "DELETE FROM dreamhelper_tickets WHERE DATEDIFF(CURRENT_DATE(), DATE(ticket_date)) > " + DreamHelper.nbDay;
		
		try {
			PreparedStatement r = sql.prepareStatement(request);
			r.execute();
			r.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public static void replyTicket(CommandSender sender, String id, String reply, Connection sql) {
		
		String request = "UPDATE dreamhelper_tickets SET ticket_reply = '" + reply + "', ticket_replyStatus = 0 "
				+ "WHERE ticket_id = " + Integer.parseInt(id);
		
		try {
			PreparedStatement r = sql.prepareStatement(request);
			r.execute();
			r.close();
			sender.sendMessage(Language.prefix + Language.ticketReply.replace("{ID}", id));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public static void resetAITickets(Connection sql) {
		String request = "ALTER TABLE dreamhelper_tickets AUTO_INCREMENT = 1";
		
		try	{
			PreparedStatement r = sql.prepareStatement(request);
			r.execute();
			r.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public static void updateReplyStatus(Connection sql, int id, int value) {
		String request = "UPDATE dreamhelper_tickets SET ticket_replyStatus = " + value + " WHERE ticket_id = " + id;
		
		try	{
			PreparedStatement r = sql.prepareStatement(request);
			r.execute();
			r.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}