package com.mrsweeter.dreamhelper.Commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.mrsweeter.dreamhelper.DreamHelper;
import com.mrsweeter.dreamhelper.Language;
import com.mrsweeter.dreamhelper.Configuration.PluginConfiguration;
import com.mrsweeter.dreamhelper.SQL.Tickets;

public class TicketCommand {

	public static boolean submit(DreamHelper pl, CommandSender sender, String[] args) {
		
		if (sender instanceof Player)	{
			Player p = (Player) sender;
			List<String> blacklist = pl.getAConfig("config").getStringList("ticket-prefix");
			
			if ((args.length > 0 && !blacklist.contains(args[0]))|| (args.length > 1) && blacklist.contains(args[0]))	{
				
				String msg = DreamHelper.arraysToString(args, 0);
				double x = p.getLocation().getX(), y = p.getLocation().getY(), z = p.getLocation().getZ(), yaw = p.getLocation().getYaw(), pitch = p.getLocation().getPitch();
				String world = p.getLocation().getWorld().getName();
				String player = p.getName();
				
				if (pl.getSQL() == null)	{
					
					PluginConfiguration config = pl.getAConfig("tickets");
					int id = lastTicket(pl)+1;
					
					SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yy H:mm:ss");
					Date date = new Date();
					
					config.set(id + ".message", msg);
					config.set(id + ".x", x);
					config.set(id + ".y", y);
					config.set(id + ".z", z);
					config.set(id + ".yaw", yaw);
					config.set(id + ".pitch", pitch);
					config.set(id + ".world", world);
					config.set(id + ".player", player);
					config.set(id + ".playerAssigned", "");
					config.set(id + ".status", "submit");
					config.set(id + ".date", formater.format(date));
					config.set(id + ".reply", "");
					config.set(id + ".replyStatus", "no");
					
					config.save();
					p.sendMessage(Language.prefix + Language.ticketSend);
					
				} else {
					Tickets.createTicket(p, args, pl.getSQL());
				}
				
			} else {
				return false;
			}
		} else {
			sender.sendMessage(Language.prefix + Language.onlyPlayer);
		}
		return true;
	}

	public static boolean seen(DreamHelper pl, CommandSender sender, String[] args) {
		
		if (args.length == 0)	{
			args = new String[1];
			args[0] = "-s";
		}
			
			
		if (args.length == 1 && (args[0].equals("-a") || args[0].equals("-c") || args[0].equals("-s")))	{
			
			char param = args[0].charAt(1);
			
			if (pl.getSQL() == null)	{

				PluginConfiguration ti = pl.getAConfig("tickets");
				
				switch (param)	{
				case 'a':
					sender.sendMessage("§6---------- " + Language.prefix + "§9: All Tickets§6----------");
					break;
				case 'c':
					sender.sendMessage("§6---------- " + Language.prefix + "§9: Closed Tickets§6----------");
					break;
				default:
					sender.sendMessage("§6---------- " + Language.prefix + "§9: Tickets§6----------");
				}
				
				
				for (String key : ti.getKeys(false)) {

					ConfigurationSection section = ti.getConfigurationSection(key);
					String ticket;
					
					switch (param)	{
					case 'a':
						ticket = "§6" + key + ". §8" + section.getString("player") + ": §7" + section.getString("message");
						sender.sendMessage(ticket);
						break;
					case 'c':
						if (section.getString("status").equalsIgnoreCase("closed")) {
							ticket = "§6" + key + ". §8" + section.getString("player") + ": §7" + section.getString("message");
							sender.sendMessage(ticket);
						}
						break;
					default:
						if (section.getString("status").equalsIgnoreCase("submit") || section.getString("status").equalsIgnoreCase("assigned")) {
							ticket = "§6" + key + ". §9" + section.getString("player") + "§6: §a" + section.getString("message");
							sender.sendMessage(ticket);
						}
					}
				}
				if (ti.getKeys(false).size() == 0) {
					sender.sendMessage(Language.noTickets);
				}

				sender.sendMessage("§6------------- ------------- -------------");
				
			} else {
				ResultSet results;
				try	{
					switch (param)	{
					case 'a':
						sender.sendMessage("§6---------- " + Language.prefix + "§9: All Tickets§6----------");
						results = Tickets.seenTickets(pl.getSQL(), "'submit', 'assigned', 'closed'");
						if (results != null)	{
							while (results.next())	{
								String ticket = "§6" + results.getInt("ticket_id") + ". §8" + results.getString("ticket_player") + ": §7" + results.getString("ticket_msg");
								sender.sendMessage(ticket);
							}
						}
						break;
					case 'c':
						sender.sendMessage("§6---------- " + Language.prefix + "§9: Closed Tickets§6----------");
						results = Tickets.seenTickets(pl.getSQL(), "'closed'");
						if (results != null)	{
							while (results.next())	{
								String ticket = "§6" + results.getInt("ticket_id") + ". §8" + results.getString("ticket_player") + ": §7" + results.getString("ticket_msg");
								sender.sendMessage(ticket);
							}
						}
						break;
					default:
						sender.sendMessage("§6---------- " + Language.prefix + "§9: Tickets§6----------");
						results = Tickets.seenTickets(pl.getSQL(), "'submit', 'assigned'");
						if (results != null)	{
							while (results.next())	{
								String ticket = "§6" + results.getInt("ticket_id") + ". §8" + results.getString("ticket_player") + ": §7" + results.getString("ticket_msg");
								sender.sendMessage(ticket);
							}
						}
					}
				} catch (SQLException exception)	{
					DreamHelper.getLog().warning("SQL error");
					exception.printStackTrace();
				}
				sender.sendMessage("§6------------- ------------- -------------");
			}
		} else {
			return false;
		}
		return true;
	}

	public static boolean take(DreamHelper pl, CommandSender sender, String[] args) {
		
		if (sender instanceof Player)	{
			Player p = (Player) sender;
			
			if (args.length == 1)	{
				
				try {
					Integer.parseInt(args[0]);
				} catch (NumberFormatException e){
					return false;
				}
				
				int id; double x, y, z; float yaw, pitch; World world;
				String msg, player, playerAssigned, status, date, reply;
				if (pl.getSQL() == null)	{
					
					PluginConfiguration ticketInfo = pl.getAConfig("tickets");
					
					if (ticketInfo.getKeys(false).contains(args[0]))	{
						
						ConfigurationSection ticket = ticketInfo.getConfigurationSection(args[0]);
						if (ticket.getString("status").equalsIgnoreCase("submit") || ticket.getString("status").equalsIgnoreCase("assigned"))	{
							ticket.set("playerAssigned", p.getName());
							ticket.set("status", "assigned");
							ticketInfo.save();
							
							id = Integer.parseInt(args[0]);
							msg = ticket.getString("message");
							x = ticket.getInt("x");
							y = ticket.getInt("y");
							z = ticket.getInt("z");
							yaw = (float) ticket.getDouble("yaw");
							pitch = (float) ticket.getDouble("pitch");
							world = pl.getServer().getWorld(ticket.getString("world"));
							player = ticket.getString("player");
							playerAssigned = ticket.getString("playerAssigned");
							status = ticket.getString("status");
							date = ticket.getString("date");
							reply = ticket.getString("reply");
						} else {
							p.sendMessage(Language.prefix + Language.closedTicket);
							return true;
						}
					} else {
						p.sendMessage(Language.prefix + Language.unknowID);
						return true;
					}
					
				} else {
					Map<String, String> ticket = Tickets.takeTicket((Player) sender, args[0], pl.getSQL());
					
					if (ticket.size() != 0)	{
						id = Integer.parseInt(ticket.get("ticket_id"));
						msg = ticket.get("ticket_msg");
						x = Double.parseDouble(ticket.get("ticket_x"));
						y = Double.parseDouble(ticket.get("ticket_y"));
						z = Double.parseDouble(ticket.get("ticket_z"));
						yaw = Float.parseFloat(ticket.get("ticket_yaw"));
						pitch = Float.parseFloat(ticket.get("ticket_pitch"));
						world = pl.getServer().getWorld(ticket.get("ticket_world"));
						player = ticket.get("ticket_player");
						playerAssigned = ticket.get("ticket_pAssigned");
						status = ticket.get("ticket_status");
						date = ticket.get("ticket_date");
						reply = ticket.get("ticket_reply");
					} else {
						p.sendMessage(Language.prefix + Language.closedTicket);
						return true;
					}
				}
				Location loc = new Location(world, x, y, z, yaw, pitch);
				p.teleport(loc);
				p.sendMessage("§6---------- §9Ticket: "+ id + " §6----------");
				p.sendMessage("§9Message: §a" + msg);
				p.sendMessage("§9Player: §a" + player);
				p.sendMessage("§9Date: §a" + date);
				p.sendMessage("§9Status: §a" + status);
				p.sendMessage("§9Assigned: §a" + playerAssigned);
				p.sendMessage("§9Reply: §a" + reply);
				p.sendMessage("§6--------- --------- ---------");
				
				Player ticketP = Bukkit.getPlayer(player);
				if (ticketP instanceof Player)	{
					ticketP.sendMessage(Language.ticketTakedBy + playerAssigned);
				}
				
			} else {
				return false;
			}
		} else {
			sender.sendMessage(Language.prefix + Language.onlyPlayer);
		}
		return true;
	}

	public static boolean close(DreamHelper pl, CommandSender sender, String[] args) {
		
		if (args.length == 1)	{
			
			try	{
				Integer.parseInt(args[0]);
			} catch (Exception e){
				return false;
			}
			
			if (pl.getSQL() == null)	{
				
				PluginConfiguration config = pl.getAConfig("tickets");
				
				if (config.isSet(args[0]))	{
					if (config.getString(args[0] + ".status").equals("closed"))	{
						sender.sendMessage(Language.prefix + Language.closedTicket);
					} else {
						
						config.set(args[0] + ".status", "closed");
						config.save();
						sender.sendMessage(Language.prefix + Language.ticketClose.replace("{ID}", args[0]));
					}
				} else {
					sender.sendMessage(Language.prefix + Language.unknowID);
				}
			} else {
				Tickets.closeTicket(sender, args[0], pl.getSQL());
			}
			
		} else {
			return false;
		}
		return true;
	}
	
	public static boolean reply(DreamHelper pl, CommandSender sender, String[] args) {
		
		if (args.length > 1)	{
			
			try	{
				Integer.parseInt(args[0]);
			} catch (Exception e){
				return false;
			}
			
			if (pl.getSQL() == null)	{
				
				PluginConfiguration config = pl.getAConfig("tickets");
				
				if (config.isSet(args[0]))	{
					
					config.set(args[0] + ".reply", DreamHelper.arraysToString(args, 1));
					config.set(args[0] + ".replyStatus", "on");
					config.save();
					sender.sendMessage(Language.prefix + Language.ticketReply.replace("{ID}", args[0]));
					
				} else {
					sender.sendMessage(Language.prefix + Language.unknowID);
				}
			} else {
				Tickets.replyTicket(sender, args[0], DreamHelper.arraysToString(args, 1), pl.getSQL());
			}
			
		} else {
			return false;
		}
		return true;
	}

	public static boolean delete(DreamHelper pl, CommandSender sender, String[] args) {
		
		if (args.length == 1)	{
			
			try	{
				Integer.parseInt(args[0]);
			} catch (Exception e){
				return false;
			}
			
			if (pl.getSQL() == null)	{
				
				PluginConfiguration config = pl.getAConfig("tickets");
				
				if (config.isSet(args[0]))	{
					config.set(args[0], null);
					config.save();
					sender.sendMessage(Language.prefix + Language.ticketDelete.replace("{ID}", args[0]));
				} else {
					sender.sendMessage(Language.prefix + Language.unknowID);
				}
				
			} else {
				Tickets.deleteTicket(sender, args[0], pl.getSQL());
			}
			
		} else	{
			return false;
		}
		return true;
	}
	
	public static boolean purge(DreamHelper pl, CommandSender sender, String[] args) {
		
		if (args.length == 0)	{
			args = new String[1];
			args[0] = "-t";
		}
			
			
		if (args.length == 1 && (args[0].equals("-a") || args[0].equals("-c") || args[0].equals("-t")))	{
			
			char param = args[0].charAt(1);
			
			if (pl.getSQL() == null)	{

				PluginConfiguration ti = pl.getAConfig("tickets");

				for (String key : ti.getKeys(false)) {

					ConfigurationSection section = ti.getConfigurationSection(key);
					
					if (ti.getKeys(false).size() == 0) {
						sender.sendMessage(Language.prefix + Language.noTickets);
					}
					
					switch (param)	{
					case 'a':
						ti.set(key, null);
						break;
					case 'c':
						if (section.getString("status").equalsIgnoreCase("closed")) {
							ti.set(key, null);
						}
						break;
					default:
						
						String dateTicket = section.getString("date");
						SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yy H:mm:ss");
						Date date = null;
						Date today = new Date();
						
						try {
							date = formater.parse(dateTicket);
							long delay = (today.getTime() - date.getTime()) / 86400000;//MilliSeconds in one day
							
							if (delay > DreamHelper.nbDay) {
								ti.set(key, null);
							}
							
						} catch (ParseException e) {
							DreamHelper.getLog().warning("Ticket " + key + ": Error in date format, check your ticket.yml");
						}
					}
				}
				ti.save();
				switch (param)	{
				case 'a':
					sender.sendMessage("§aPurge of all tickets complete");
					break;
				case 'c':
					sender.sendMessage("§aPurge of closed tickets complete");
					break;
				default:
					sender.sendMessage("§aPurge of oldest tickets complete");
				}
				
			} else {
				
				switch (param)	{
				case 'a':
					Tickets.purgeTickets(pl.getSQL(), "'submit', 'assigned', 'closed'");
					Tickets.resetAITickets(pl.getSQL());
					sender.sendMessage("§aPurge of all tickets complete");
					break;
				case 'c':
					Tickets.purgeTickets(pl.getSQL(), "'closed'");
					sender.sendMessage("§aPurge of closed tickets complete");
					break;
				default:					
					Tickets.purgeTimedTickets(pl.getSQL());
					sender.sendMessage("§aPurge of oldest tickets complete");
				}
			}
		} else {
			return false;
		}
		return true;
	}
	
	public static boolean check(DreamHelper pl, CommandSender p, String[] args) {
		
		if (args.length == 1)	{
			
			try	{
				Integer.parseInt(args[0]);
			} catch (Exception e){
				return false;
			}
			
			int id; double x, y, z;
			World world;
			String msg, player, playerAssigned, status, date, reply;
			
			if (pl.getSQL() == null)	{
				
				PluginConfiguration ticketInfo = pl.getAConfig("tickets");
				
				if (ticketInfo.getKeys(false).contains(args[0]))	{
					
					ConfigurationSection ticket = ticketInfo.getConfigurationSection(args[0]);
						
					id = Integer.parseInt(args[0]);
					msg = ticket.getString("message");
					x = ticket.getInt("x");
					y = ticket.getInt("y");
					z = ticket.getInt("z");
					world = pl.getServer().getWorld(ticket.getString("world"));
					player = ticket.getString("player");
					playerAssigned = ticket.getString("playerAssigned");
					status = ticket.getString("status");
					date = ticket.getString("date");
					reply = ticket.getString("reply");
					
				} else {
					p.sendMessage(Language.prefix + Language.unknowID);
					return true;
				}
				
			} else {
				Map<String, String> ticket = Tickets.checkTicket((Player) p, args[0], pl.getSQL());
				
				if (ticket.size() != 0)	{
					id = Integer.parseInt(ticket.get("ticket_id"));
					msg = ticket.get("ticket_msg");
					x = Double.parseDouble(ticket.get("ticket_x"));
					y = Double.parseDouble(ticket.get("ticket_y"));
					z = Double.parseDouble(ticket.get("ticket_z"));
					world = pl.getServer().getWorld(ticket.get("ticket_world"));
					player = ticket.get("ticket_player");
					playerAssigned = ticket.get("ticket_pAssigned");
					status = ticket.get("ticket_status");
					date = ticket.get("ticket_date");
					reply = ticket.get("ticket_reply");
				} else {
					p.sendMessage(Language.prefix + Language.unknowID);
					return true;
				}
			}
			p.sendMessage("§6---------- §9Ticket: "+ id + " §6----------");
			p.sendMessage("§9Message: §a" + msg);
			p.sendMessage("§9Player: §a" + player);
			p.sendMessage("§9Coordinates: §a" + (int)x + " " + (int)y + " " + (int)z + " in " + world.getName());
			p.sendMessage("§9Date: §a" + date);
			p.sendMessage("§9Status: §a" + status);
			p.sendMessage("§9Assigned: §a" + playerAssigned);
			p.sendMessage("§9Reply: §a" + reply);
			p.sendMessage("§6--------- --------- ---------");
			
		} else	{
			return false;
		}
		return true;
		
	}
	
	private static int lastTicket(DreamHelper pl)	{
		
		PluginConfiguration config = pl.getAConfig("tickets");
		Object[] keys = config.getKeys(false).toArray();
		
		if (keys.length >= 1)	{
			return Integer.parseInt((String) keys[keys.length-1]);
		}
		return 0;
	}

	public static boolean help(DreamHelper pl, CommandSender sender) {
		
		List<String> helpText = pl.getAConfig("config").getStringList("help-staff");
		sender.sendMessage("§6---------- §9Help Staff Ticket §6----------");
		for (String str : helpText)	{
			sender.sendMessage(str.replace(DreamHelper.color, "§"));
		}
		return true;
		
	}


}
