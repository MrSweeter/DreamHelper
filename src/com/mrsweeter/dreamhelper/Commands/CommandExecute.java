package com.mrsweeter.dreamhelper.Commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.mrsweeter.dreamhelper.DreamHelper;
import com.mrsweeter.dreamhelper.Language;
import com.mrsweeter.dreamhelper.Configuration.PluginConfiguration;

public class CommandExecute {

	public static void addQuestion(String[] args, DreamHelper pl) {
		
		String str = args[0];
		
		for (int i = 1; i < args.length; i++)	{
			str += " ";
			str += args[i];
		}
		PluginConfiguration config = pl.getAConfig("submit");
		
		List<String> submissions = config.getStringList("submissions");
		submissions.add(str);
		config.set("submissions", submissions);
		config.save();
	}

	public static void tellSubmissions(CommandSender sender, DreamHelper pl) {
		
		PluginConfiguration config = pl.getAConfig("submit");
		List<String> submissions = config.getStringList("submissions");
		
		sender.sendMessage("§a========== §eDreamHelper §a==========");
		for (int i = 0; i < submissions.size(); i++)	{
			if ((i % 2) == 0)	{
				sender.sendMessage("§e" + (i+1) + ". §b" + submissions.get(i));
			} else {
				sender.sendMessage("§e" + (i+1) + ". §d" + submissions.get(i));
			}
		}
		sender.sendMessage("§a================================");
		
	}

	public static void removeSubmission(CommandSender sender, DreamHelper pl, int num) {
		
		PluginConfiguration config = pl.getAConfig("submit");
		List<String> submissions = config.getStringList("submissions");
		
		if (num-1 >= submissions.size() || num-1 < 0)	{
			sender.sendMessage("§c[§aDreamhelper§c] " + Language.noSubmitNb + num);
		} else {
			submissions.remove(num-1);
			config.set("submissions", submissions);
			config.save();
			sender.sendMessage("§c[§aDreamhelper§c] " + Language.submitXClear + " (" + num + ")");
		}
	}
}
