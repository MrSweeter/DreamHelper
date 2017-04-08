package com.mrsweeter.dreamhelper.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.mrsweeter.dreamhelper.DreamHelper;
import com.mrsweeter.dreamhelper.Language;
import com.mrsweeter.dreamhelper.Configuration.Loader;
import com.mrsweeter.dreamhelper.Configuration.PluginConfiguration;

public class Commands implements CommandExecutor	{
	
	private DreamHelper pl;

	public Commands(DreamHelper dreamHelper) {
		pl = dreamHelper;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		
		if (args.length == 0)	{
			switch (commandLabel)	{
			case "dhreload":
				if (sender.hasPermission("dreamhelper.reload"))	{
					
					Loader.loadAllConfig(pl.getAllConfig());
					Loader.loadLanguage(pl.getAllConfig().get("lang"));
					
					sender.sendMessage("§c[§aDreamhelper§c] " + Language.reload);
				} else	{
					sender.sendMessage(Language.noPerm);
				}
				return true;
			}
		} else if (args.length >= 5)	{
			switch (commandLabel)	{
			case "dhsubmit":
				if (sender.hasPermission("dreamhelper.submit"))	{
					CommandExecute.addQuestion(args, pl);
					sender.sendMessage("§c[§aDreamhelper§c] " + Language.sendQuestion);
				} else	{
					sender.sendMessage(Language.noPerm);
				}
				return true;
			}
		} else if (args.length == 1)	{
			switch (commandLabel)	{
			case "dhsubmit":
				if (sender.hasPermission("dreamhelper.submit"))	{
					if (args[0].equalsIgnoreCase("read"))	{
						CommandExecute.tellSubmissions(sender, pl);
						return true;
					} else if (args[0].equalsIgnoreCase("clear"))	{
						
						PluginConfiguration config = pl.getAConfig("submit");
						config.set("submissions", null);
						config.save();
						
						sender.sendMessage("§c[§aDreamhelper§c] " + Language.clearAllSubmit);
						return true;
					}
				} else	{
					sender.sendMessage(Language.noPerm);
				}
			}
		} else if (args.length == 2)	{
			switch (commandLabel)	{
			case "dhsubmit":
				if (sender.hasPermission("dreamhelper.submit"))	{
					if (args[0].equalsIgnoreCase("clear") && isInteger(args[1]))	{
						
						CommandExecute.removeSubmission(sender, pl, Integer.parseInt(args[1]));
						return true;
					}
				} else	{
					sender.sendMessage(Language.noPerm);
				}
			}
		}
		return false;
		
	}
	
	private boolean isInteger(String str) {
		try {
			Integer.parseInt(str);
		} catch (NumberFormatException e){
			return false;
		}
 
		return true;
	}
}
