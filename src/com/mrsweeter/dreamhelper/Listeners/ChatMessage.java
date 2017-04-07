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

import com.mrsweeter.dreamhelper.PluginConfiguration;

public class ChatMessage implements Listener	{
	
	PluginConfiguration faq;
	
	public ChatMessage(PluginConfiguration faq)	{
		
		this.faq = faq;
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event)	{
		
		String msg = event.getMessage().toLowerCase();
		
		if (msg.contains("?"))	{
			Map<String, String> info = findAnswer(msg);
			
			if (info != null)	{
				event.setCancelled(true);
				tellAnswer(info, event.getPlayer());
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
			question = section.getString("question").replace("&", "§");
			answer = section.getString("answer").replace("&", "§");
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
