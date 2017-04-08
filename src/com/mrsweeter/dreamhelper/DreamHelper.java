package com.mrsweeter.dreamhelper;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.mrsweeter.dreamhelper.Commands.Commands;
import com.mrsweeter.dreamhelper.Listeners.ChatMessage;
import com.mrsweeter.dreamhelper.Listeners.JoinQuit;

public class DreamHelper extends JavaPlugin	{
	
	static Logger log = Logger.getLogger("Minecraft");
	static PluginManager pm = Bukkit.getPluginManager();
	public static String color = "&";
	
	Map<String, PluginConfiguration> configs = new HashMap<String, PluginConfiguration>();

	public void onEnable()	{
		
		configs.put("faq", new PluginConfiguration(this, "faq.yml", "faq.yml"));
		configs.put("config", new PluginConfiguration(this, "preferences.yml", "preferences.yml"));
		configs.put("submit", new PluginConfiguration(this, "submit.yml"));
		
		for (String str : configs.keySet()){
			configs.get(str).reload();
		}
		
		color = configs.get("config").getString("color");
		
		pm.registerEvents(new JoinQuit(this), this);
		pm.registerEvents(new ChatMessage(configs.get("faq"), configs.get("config"), this), this);
		
		getCommand("dhreload").setExecutor(new Commands(this));
		getCommand("dhsubmit").setExecutor(new Commands(this));
		
		log.info(Color.GREEN + "=============== " + Color.YELLOW + "DreamHelper enable" + Color.GREEN + " ===============" + Color.RESET);
//		Updater updater = new Updater(0);
//		if (updater.checkUpdate(this.getDescription().getVersion()) && Updater.updateAvailable())	{
//			log.warning(Color.RED + "=============== A newest version of DreamSkull is available ===============" + Color.RESET);
//		}
		
	}
	
	public void onDisable()	{
		
		log.info(Color.GREEN + "=============== " + Color.YELLOW + "DreamHelper disable" + Color.GREEN + " ===============" + Color.RESET);
		
	}
	
	public PluginConfiguration getAConfig(String name)	{
		return configs.get(name);
	}
	public Map<String, PluginConfiguration> getAllConfig()	{
		return configs;
	}
}
