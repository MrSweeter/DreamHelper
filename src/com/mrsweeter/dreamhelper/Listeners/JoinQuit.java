package com.mrsweeter.dreamhelper.Listeners;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.mrsweeter.dreamhelper.DreamHelper;
import com.mrsweeter.dreamhelper.Configuration.PluginConfiguration;
import com.mrsweeter.dreamhelper.SQL.Tickets;

public class JoinQuit implements Listener	{
	
	DreamHelper pl;
	
	public JoinQuit(DreamHelper main)	{
		pl = main;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event)	{
		
		Player p = event.getPlayer();
		String name = p.getName();
		
		if (pl.getSQL() == null)	{
			
			PluginConfiguration config = pl.getAConfig("tickets");
			
			for (String key : config.getKeys(false)) {

				ConfigurationSection section = config.getConfigurationSection(key);
				
				if (section.getString("replyStatus").equals("on"))	{
					
					String ticket = "§6Ticket " + key + ":§a " + section.getString("message");
					String reply = "§9Reply:§a " + section.getString("reply");
					section.set("replyStatus", "off");
					config.save();
					
					new BukkitRunnable() {
				        
			            @Override
			            public void run() {
			            	p.sendMessage(ticket);
			            	p.sendMessage(reply);
			            }
			            
			        }.runTaskLater(pl, 5);	
				}
			}
			
		} else {
			
			ResultSet results = Tickets.seenPlayerTickets(pl.getSQL(), name);
			
			if (results != null)	{
				
				try {
					while (results.next())	{
						
						if (results.getString("ticket_replyStatus").equals("0"))	{
							
							String ticket = "§6Ticket " + results.getString("ticket_id") + ":§a " + results.getString("ticket_msg");
							String reply = "§9Reply:§a " + results.getString("ticket_reply");
							Tickets.updateReplyStatus(pl.getSQL(), results.getInt("ticket_id"), 1);
							new BukkitRunnable() {
						        
					            @Override
					            public void run() {
					            	p.sendMessage(ticket);
					            	p.sendMessage(reply);
					            }
					            
					        }.runTaskLater(pl, 5);
							
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		}		
	}
}
