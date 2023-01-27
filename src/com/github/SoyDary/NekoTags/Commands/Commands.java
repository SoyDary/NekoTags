package com.github.SoyDary.NekoTags.Commands;

import java.util.List;

import org.bukkit.OfflinePlayer;
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
			case "select": {
				return select(s, a);
			}
			case "give": {
				return giveTag(s, a);
			}
			case "remove": {
				return removeTag(s, a);
			}
			case "list": {
				return listTags(s, a);
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
	
	private boolean giveTag(CommandSender s, String[] a) {
		if(!s.hasPermission("nekotags.admin")) {
			s.sendMessage("§cError: Permisos insuficientes.");
			return true;
		}
		if(a.length == 3) {
			
			OfflinePlayer op = plugin.getUtils().getUser(a[1]);
			if(op == null) {
				s.sendMessage(plugin.getUtils().color(plugin.prefix+" &fJugador inválido!"));
				return true;
			}
			Tag tag = plugin.getManager().getTags().get(a[2]);
			if(tag == null) {
				s.sendMessage(plugin.getUtils().color(plugin.prefix+" &fEtiqueta inválida!"));
				return true;
			}
			if(!plugin.getData().hasExactTag(op.getUniqueId(), tag.getKey())) {
				s.sendMessage(plugin.getUtils().color(plugin.prefix+" &fOtorgada la etiqueta &f"+tag.getTag()+"&f a &n"+op.getName()+"&f."));
				plugin.getData().addTag(op.getUniqueId().toString(), tag.getKey());
				
			} else {
				s.sendMessage(plugin.getUtils().color(plugin.prefix+" &f&n"+op.getName()+"&r ya tiene esta etiqueta."));				
			}
			return true;		
		} else {
			s.sendMessage(plugin.getUtils().color(plugin.prefix+" &fUso: /tags give <usuario> <tag>"));
		}
		return true;
	}
	private boolean removeTag(CommandSender s, String[] a) {
		if(!s.hasPermission("nekotags.admin")) {
			s.sendMessage("§cError: Permisos insuficientes.");
			return true;
		}
		if(a.length == 3) {
			OfflinePlayer op = plugin.getUtils().getUser(a[1]);
			if(op == null) {
				s.sendMessage(plugin.getUtils().color(plugin.prefix+" &fJugador inválido!"));
				return true;
			}
			Tag tag = plugin.getManager().getTags().get(a[2]);
			if(tag == null) {
				s.sendMessage(plugin.getUtils().color(plugin.prefix+" &fEtiqueta inválida!"));
				return true;
			}
			if(plugin.getData().hasExactTag(op.getUniqueId(), tag.getKey())) {
				s.sendMessage(plugin.getUtils().color(plugin.prefix+" &fEliminada la etiqueta &f"+tag.getTag()+"&f a &n"+op.getName()+"&f."));
				plugin.getData().removeTag(op.getUniqueId().toString(), tag.getKey());
				
			} else {
				s.sendMessage(plugin.getUtils().color(plugin.prefix+" &f&n"+op.getName()+"&r no tiene esta etiqueta."));				
			}
			return true;		
		} else {
			s.sendMessage(plugin.getUtils().color(plugin.prefix+" &fUso: /tags remove <usuario> <tag>"));
		}
		return true;
	}
	private boolean listTags(CommandSender s, String[] a) {
		if(!s.hasPermission("nekotags.admin")) {
			s.sendMessage("§cError: Permisos insuficientes.");
			return true;
		}
		if(a.length < 2) {
			s.sendMessage(plugin.getUtils().color(plugin.prefix+" &fIntroduce el nombre de un jugador."));
		} else {
			OfflinePlayer op = plugin.getUtils().getUser(a[1]);
			if(op == null) {
				s.sendMessage(plugin.getUtils().color(plugin.prefix+" &fJugador inválido!"));
				return true;
			}
			List<String> tags = plugin.getData().getTags(op.getUniqueId().toString());
			if(tags.isEmpty()) {
				s.sendMessage(plugin.getUtils().color(plugin.prefix+" &f"+op.getName()+" &7no tiene etiquetas."));
				return true;
			}
			s.sendMessage(plugin.getUtils().color("&3Etiquetas de "+op.getName()+":"));
			for(String t : tags) {
				Tag tag = plugin.getManager().getTags().get(t);
				s.sendMessage(plugin.getUtils().color("&7"+tag.getKey()+": &f"+tag.getTag()));		
			}
		}
		return true;
	}
	
	
	private boolean reload(CommandSender s, String[] a) {
		if(!s.hasPermission("nekotags.admin")) {
			s.sendMessage("§cError: Permisos insuficientes.");
			return true;
		}
		s.sendMessage(plugin.getUtils().color(plugin.prefix+" &#ff99ffPlugin recargado!"));
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
			if(plugin.getData().hasTag(p, tag.getKey())) {
				p.sendMessage(plugin.getUtils().color("Etiqueta seleccionada: "+tag.getTag()));
				plugin.getData().setTag(p.getUniqueId().toString(), tag.getKey());
			} else {
				p.sendMessage(plugin.getUtils().color("&fNo tienes permiso de usar esta etiqueta."));
			}
		} 
		return true;
	}
}
