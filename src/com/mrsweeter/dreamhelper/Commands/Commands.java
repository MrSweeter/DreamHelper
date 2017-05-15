package com.mrsweeter.dreamhelper.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.mrsweeter.dreamhelper.DreamHelper;
import com.mrsweeter.dreamhelper.Language;
import com.mrsweeter.dreamhelper.Configuration.Loader;

public class Commands implements CommandExecutor, TabCompleter	{
	
	private DreamHelper pl;

	public Commands(DreamHelper dreamHelper) {
		pl = dreamHelper;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		
		if (sender.hasPermission(command.getPermission()))	{
			commandLabel = command.getLabel();
			switch (commandLabel)	{
			case "dhreload":
				
				Loader.loadAllConfig(pl.getAllConfig());
				Loader.loadLanguage(pl.getAConfig("lang"));
				DreamHelper.color = pl.getAConfig("config").getString("color");
				DreamHelper.nbDay = pl.getAConfig("config").getInt("purge-timeDay-expiration");
				Loader.loadTicket(pl, pl.getAConfig("config"));
				sender.sendMessage(Language.prefix + Language.reload);
				
				break;
			case "dhticket":
				return TicketCommand.submit(pl, sender, args);
			case "dhtickets":
				return TicketCommand.seen(pl, sender, args);
			case "dhtaketicket":
				return TicketCommand.take(pl, sender, args);
			case "dhcloseticket":
				return TicketCommand.close(pl, sender, args);
			case "dhreplyticket":
				return TicketCommand.reply(pl, sender, args);
			case "dhdeleteticket":
				return TicketCommand.delete(pl, sender, args);
			case "dhpurgetickets":
				return TicketCommand.purge(pl, sender, args);
			case "dhcheckticket":
				return TicketCommand.check(pl, sender, args);
			case "dhthelp":
				return TicketCommand.help(pl, sender);
			}
		} else {
			sender.sendMessage(Language.noPerm);
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String commandLabel, String[] args) {
		
		List<String> complete = new ArrayList<>();
		
		switch (commandLabel)	{
		case "dhticket":
			complete =  pl.getAConfig("config").getStringList("ticket-prefix");
			break;
		case "dhtickets":
		case "dhpurgetickets":
			complete.add("-a");
			complete.add("-c");
			break;
		}
		
		return complete;
		
	}
}
