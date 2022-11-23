package com.github.SoyDary.NekoTags.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.SoyDary.NekoTags.NekoTags;
import com.github.SoyDary.NekoTags.Object.Gui;
import com.github.SoyDary.NekoTags.Object.Tag;

public class Commands implements CommandExecutor {
	NekoTags plugin;
	
	public Commands(NekoTags plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
		if(a.length > 0) {
			switch(a[0].toLowerCase()) {
			case "reload":{
				return reload(s, a);
			}
			case "select":{
				return select(s, a);
			}
			}
		}	
		if(s instanceof Player) {
			Player p = (Player)s;
			Gui gui = new Gui(p, 0);
			plugin.getManager().guis.put(p, gui);
			return true;
		}
		return true;
	}
	
	private boolean reload(CommandSender s, String[] a) {
		if(!s.hasPermission("nekotags.admin")) {
			s.sendMessage("§cError: Permisos insuficientes.");
			return true;
		}
		s.sendMessage(plugin.getUtils().color("&8[&dNeko&eTags&8] &aPlugin recargado!"));
		plugin.loadPlugin();
		return true;
	}
	private boolean select(CommandSender s, String[] a) {
		if(!(s instanceof Player)) return true;
		Player p = (Player) s;
		if(a.length < 2) {
			p.sendMessage("§cUso: /tags select <tag>");
			return true;
		}
		Tag tag = plugin.getManager().getTags().get(a[1]);
		if(tag != null) {
			if(p.hasPermission("nekotags.tag."+tag.getName())) {
				p.sendMessage(plugin.getUtils().color("Etiqueta seleccionada: "+tag.getTag()));
				plugin.getData().setTag(p.getUniqueId().toString(), tag.getKey());
			} else {
				p.sendMessage(plugin.getUtils().color("&fNo tienes permiso de usar esta etiqueta."));
			}
		} 
		return true;
	}
	
}
