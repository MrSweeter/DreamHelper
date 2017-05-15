package com.mrsweeter.dreamhelper;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.mrsweeter.dreamhelper.Commands.Commands;
import com.mrsweeter.dreamhelper.Configuration.Loader;
import com.mrsweeter.dreamhelper.Configuration.PluginConfiguration;
import com.mrsweeter.dreamhelper.Listeners.ChatMessage;
import com.mrsweeter.dreamhelper.Listeners.JoinQuit;
import com.mrsweeter.dreamhelper.SQL.SQLConnection;

public class DreamHelper extends JavaPlugin	{
	
	static Logger log = Logger.getLogger("Minecraft");
	static PluginManager pm = Bukkit.getPluginManager();
	public static String color = "&";
	public static int nbDay= 7;
	public SQLConnection sql = null;
	
	Map<String, PluginConfiguration> configs = new HashMap<String, PluginConfiguration>();
	
	public void onEnable()	{
		
		configs.put("faq", new PluginConfiguration(this, "faq.yml"));
		configs.put("lang", new PluginConfiguration(this, "lang-message.yml", "lang-message.yml", null));
		configs.put("config", new PluginConfiguration(this, "configuration.yml", "configuration.yml", null));
		
		Loader.loadAllConfig(configs);
		color = configs.get("config").getString("color");
		nbDay = configs.get("config").getInt("purge-timeDay-expiration");
		Loader.loadLanguage(configs.get("lang"));
		Loader.loadTicket(this, configs.get("config"));
		
		pm.registerEvents(new ChatMessage(configs.get("faq"), configs.get("config"), this), this);
		pm.registerEvents(new JoinQuit(this), this);
		
		getCommand("dhreload").setExecutor(new Commands(this));
		getCommand("dhticket").setExecutor(new Commands(this));
		getCommand("dhtickets").setExecutor(new Commands(this));
		getCommand("dhtaketicket").setExecutor(new Commands(this));
		getCommand("dhcloseticket").setExecutor(new Commands(this));
		getCommand("dhdeleteticket").setExecutor(new Commands(this));
		getCommand("dhcheckticket").setExecutor(new Commands(this));
		getCommand("dhpurgetickets").setExecutor(new Commands(this));
		getCommand("dhreplyticket").setExecutor(new Commands(this));
		getCommand("dhthelp").setExecutor(new Commands(this));
		
		log.info(Color.GREEN + "=============== " + Color.YELLOW + "DreamHelper enable" + Color.GREEN + " ===============" + Color.RESET);
				
	}
	
	public void onDisable()	{
		
		if (this.getConfig().getBoolean("storage.sqlStorage.enable"))	{
			sql.disconnetion();
		}
		log.info(Color.GREEN + "=============== " + Color.YELLOW + "DreamHelper disable" + Color.GREEN + " ===============" + Color.RESET);
		
	}	
	
	public static String arraysToString(String[] arrays, int begin){
		String str = "";
		for(int i = begin; i < arrays.length; i++){
			str += arrays[i];
			str += " ";
		}
		return str.trim();
	}
	
	public PluginConfiguration getAConfig(String name)	{
		PluginConfiguration pc = configs.get(name);
		if (pc == null)	{
			String file = name + ".yml";
			pc = new PluginConfiguration(this, file);
			configs.put(name, pc);
		}
		return pc;
	}
	
	public Map<String, PluginConfiguration> getAllConfig()	{
		return configs;
	}
	public static Logger getLog()	{
		return log;
	}
	public Connection getSQL()	{
		if (sql != null)	{
			return sql.getLink();
		}
		return null;
	}
	public void setSQL(SQLConnection paramSql)	{
		sql = paramSql;
	}
}
