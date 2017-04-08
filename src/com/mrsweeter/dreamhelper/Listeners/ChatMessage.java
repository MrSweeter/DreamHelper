package com.mrsweeter.dreamhelper.Listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.mrsweeter.dreamhelper.DreamHelper;
import com.mrsweeter.dreamhelper.PluginConfiguration;

public class ChatMessage implements Listener	{
	
	PluginConfiguration faq;
	PluginConfiguration config;
	DreamHelper pl;
	
	public ChatMessage(PluginConfiguration faq, PluginConfiguration config, DreamHelper main)	{
		
		this.faq = faq;
		this.config = config;
		pl = main;
		
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event)	{
		
		String msg = event.getMessage().toLowerCase();
		Player p = event.getPlayer();
		
		if (msg.contains("?") || config.getBoolean("check-all-message"))	{
			Map<String, String> info = findAnswer(msg);
			
			if (info != null)	{
				if (config.getBoolean("conversation-mode")){
					
					new BukkitRunnable() {
				        
			            @Override
			            public void run() {
			            	//p.sendMessage("§c[§aDreamhelper§c] §7Question suggérée: §8" + info.get("q"));
			                pl.getServer().broadcastMessage("§4" + config.getString("bot-name").replace(DreamHelper.color, "§") + " " + info.get("a"));
			            }
			            
			        }.runTaskLater(pl, 1);
					
				} else {
					tellAnswer(info, p);
					event.setCancelled(true);
				}
			}
		}
	}
	
	private void tellAnswer(Map<String, String> info, Player player)	{
		
		player.sendMessage("§a========== §eDreamHelper §a==========");
		player.sendMessage("§aVous avez suggérer la question:");
		player.sendMessage("§b" + info.get("q"));
		player.sendMessage("§aRéponse:");
		player.sendMessage("§b" + info.get("a"));
		player.sendMessage("§a================================");
		
	}
	
	private Map<String, String> findAnswer(String msg)	{
		
		Map<String, String> info = new HashMap<String, String>();
		
		String question = "";
		String answer = "";
		List<String> keywords = toList3CharMin(msg);
		int counter = 0;
		
		for (String key : faq.getKeys(false))	{
			ConfigurationSection section = faq.getConfigurationSection(key);
			question = section.getString("question").replace(DreamHelper.color, "§");
			answer = section.getString("answer").replace(DreamHelper.color, "§");
			counter = 0;
			
			for (String s : keywords)	{
				if (section.getString("keywords").contains(s))	{
					counter += 1;
				}
				if (counter >= 3)	{
					info.put("q", question);
					info.put("a", answer);
					return info;
				}
			}
		}
		return null;
	}
	
	private List<String> toList3CharMin(String str)	{
		
		List<String> keys = new ArrayList<String>();
		String[] strTab = str.split(" ");
		
		for (String s : strTab)	{
			if (s.length() >= 3)	{
				keys.add(s);
			}
		}
		return keys;
	}
}
