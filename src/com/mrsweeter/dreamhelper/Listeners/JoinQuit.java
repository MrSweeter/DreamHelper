package com.mrsweeter.dreamhelper.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.mrsweeter.dreamhelper.DreamHelper;

public class JoinQuit implements Listener {

	DreamHelper pl;

	public JoinQuit(DreamHelper main) {
		pl = main;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {

		Player p = event.getPlayer();
		int nb = pl.getAConfig("submit").getStringList("submissions").size();

		if (p.hasPermission("dreamhelper.check") && nb > 0) {
			p.sendMessage("§c[§aDreamhelper§c] §e" + nb + "§a submission(s) waiting §e/dhsubmit read");
		}
	}
}
