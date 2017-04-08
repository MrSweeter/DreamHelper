package com.mrsweeter.dreamhelper.Commands;

import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.mrsweeter.dreamhelper.DreamHelper;
import com.mrsweeter.dreamhelper.PluginConfiguration;

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
					
					Map<String, PluginConfiguration> configs = pl.getAllConfig();
					for (String str : configs.keySet()){
						configs.get(str).reload();
					}
					
					sender.sendMessage("§c[§aDreamhelper§c] §7Reload complete");
				} else	{
					sender.sendMessage("§cYou aren't allow do to this");
				}
				return true;
			}
		} else if (args.length >= 5)	{
			switch (commandLabel)	{
			case "dhsubmit":
				if (sender.hasPermission("dreamhelper.submit"))	{
					CommandExecute.addQuestion(args, pl);
					sender.sendMessage("§c[§aDreamhelper§c] §7Question send, thank you for helping");
				} else	{
					sender.sendMessage("§cYou aren't allow do to this");
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
						
						sender.sendMessage("§c[§aDreamhelper§c] §7submit.yml clear");
						return true;
					}
				} else	{
					sender.sendMessage("§cYou aren't allow do to this");
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
					sender.sendMessage("§cYou aren't allow do to this");
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
