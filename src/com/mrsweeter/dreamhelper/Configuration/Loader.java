package com.mrsweeter.dreamhelper.Configuration;

import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;

import com.mrsweeter.dreamhelper.DreamHelper;
import com.mrsweeter.dreamhelper.Language;
import com.mrsweeter.dreamhelper.SQL.SQLConnection;

public class Loader {

	public static void loadAllConfig(Map<String, PluginConfiguration> configs)	{
		
		for (String str : configs.keySet()){
			configs.get(str).reload();
		}
	}
	
	public static void loadLanguage(PluginConfiguration lang)	{
		
		Language.prefix = lang.getString("prefix").replace(DreamHelper.color, "§");
		Language.noPerm = lang.getString("no-permission").replace(DreamHelper.color, "§");
		Language.suggestQuestion = lang.getString("suggest-question").replace(DreamHelper.color, "§");
		Language.answer = lang.getString("answer").replace(DreamHelper.color, "§");
		Language.reload = lang.getString("reload").replace(DreamHelper.color, "§");
		Language.onlyPlayer = lang.getString("onlyPlayer").replace(DreamHelper.color, "§");
		Language.noTickets = lang.getString("noTickets").replace(DreamHelper.color, "§");
		Language.ticketSend = lang.getString("ticketSend").replace(DreamHelper.color, "§");
		Language.ticketClose = lang.getString("ticketClose").replace(DreamHelper.color, "§");
		Language.ticketDelete = lang.getString("ticketDelete").replace(DreamHelper.color, "§");
		Language.unknowID = lang.getString("unknow-id").replace(DreamHelper.color, "§");
		Language.closedTicket = lang.getString("closedTicket").replace(DreamHelper.color, "§");
		Language.ticketTakedBy = lang.getString("ticketTakeBy").replace(DreamHelper.color, "§");
		
	}

	public static void loadTicket(DreamHelper pl, FileConfiguration config) {
		
		pl.setSQL(null);
		if (config.getBoolean("storage.sqlStorage.enable"))	{
			
			SQLConnection sql = new SQLConnection(config.getString("storage.sqlStorage.url"), config.getString("storage.sqlStorage.host"), config.getString("storage.sqlStorage.database"),
					config.getString("storage.sqlStorage.user"), config.getString("storage.sqlStorage.password"));
			sql.connection();
			pl.setSQL(sql);
			
		} else {
			pl.getAllConfig().put("tickets", new PluginConfiguration(pl, "tickets.yml"));
		}
	}
}
