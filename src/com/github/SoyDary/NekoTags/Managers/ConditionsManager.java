package com.github.SoyDary.NekoTags.Managers;

import java.util.ArrayList;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.github.SoyDary.NekoTags.NekoTags;
import com.github.SoyDary.NekoTags.Object.Gui;

public class ConditionsManager {
	public NekoTags plugin;
		
		public ConditionsManager(NekoTags plugin) {
		this.plugin = plugin;
	}
		
		public boolean parseConditions(String text) {
			text = text.contains(" || ") ? text.replaceAll(" \\|\\| ", "||") : text;
			text = text.contains(" && ") ? text.replaceAll(" && ", "&&") : text;
			Boolean or = false;
			ArrayList<Boolean> counts = new ArrayList<Boolean>();
			if(text.contains("||")) {
				or = true;
			}
			if(or) {
				for(String t : text.split("\\|\\|")) {
					counts.add(checkBool(t));
				}
				Boolean end = false;
				for(Boolean b : counts) {
					if(b) {
						return true;
					}
				}
				return end;
			}else {
				for(String t : text.split(Pattern.quote("&&"))) {
					counts.add(checkBool(t));
				}
				Boolean end = true;
				for(Boolean b : counts) {
					if(!b) {
						end = false;
						break;
					}
				}
				return end;
			}
		}
		
		public boolean checkBool(String text) {
			text = text.contains(" >= ") ? text.replaceAll(" >= ", ">=") : text;
			text = text.contains(" <= ") ? text.replaceAll(" <= ", "<=") : text;
			text = text.contains(" == ") ? text.replaceAll(" == ", "==") : text;
			text = text.contains(" > ") ? text.replaceAll(" > ", ">") : text;
			text = text.contains(" < ") ? text.replaceAll(" < ", "<") : text;
			
			if(text.contains(">=")) {
				String p1 = text.split(">=")[0];
				String p2 = text.split(">=")[1];
				if(plugin.getUtils().isInt(p1) && plugin.getUtils().isInt(p2)) {
					if(Integer.parseInt(p1) >= Integer.parseInt(p2)) {
						return true;
					}else {
						return false;
					}
				}else {
					return false;
				}
			}else if(text.contains("<=")) {
				String p1 = text.split("<=")[0];
				String p2 = text.split("<=")[1];
				if(plugin.getUtils().isInt(p1) && plugin.getUtils().isInt(p2)) {
					if(Integer.parseInt(p1) <= Integer.parseInt(p2)) {
						return true;
					}else {
						return false;
					}
				}else {
					return false;
				}
			}else if(text.contains("==")) {
				String p1 = text.split("==")[0];
				String p2 = text.split("==")[1];
				if(p1 == p2 || p1.equals(p2)) {
					return true;
				}else {
					return false;
				}
			}else if(text.contains(">")) {
				String p1 = text.split(">")[0];
				String p2 = text.split(">")[1];
				if(plugin.getUtils().isInt(p1) && plugin.getUtils().isInt(p2)) {
					if(Integer.parseInt(p1) > Integer.parseInt(p2)) {
						return true;
					}else {
						return false;
					}
				}else {
					return false;
				}
			}else if(text.contains("<")) {
				String p1 = text.split("<")[0];
				String p2 = text.split("<")[1];
				if(plugin.getUtils().isInt(p1) && plugin.getUtils().isInt(p2)) {
					if(Integer.parseInt(p1) < Integer.parseInt(p2)) {
						return true;
					}else {
						return false;
					}
				}else {
					return false;
				}
			}
			return false;
		}
		
		public void sendFunctions(String text, Player p) {
			String action = text.split(" ")[0];
			String value = text.replaceFirst(Pattern.quote(action)+" ", "");
			switch (action.toLowerCase()) {
			case "[console]": {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), value);
				return;
			}
			case "[message]": {
				p.sendMessage(value);
				return;
			}
			case "[chat]": {
				p.chat(value);
				return;
			}
			case "[player]": {
				Bukkit.dispatchCommand(p, value);
				return;
			}
			case "[sound]": {
				p.playSound(p.getLocation(), Sound.valueOf(value.toUpperCase()), 1f, 1f);
				return;
			}
			case "[closeinventory]": {
				closeInventory(p);
				return;
			}
			case "[update]": {
				Gui gui = new Gui(p, 0);
				plugin.getManager().guis.put(p, gui);
			}
			default:
				throw new IllegalArgumentException(ChatColor.RED+"Funcion no registrada: " + action);
			}
		}
		private void closeInventory(Player p) {
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {

				@Override
				public void run() {
					p.closeInventory();
				}
				
			}, 2L);
		}
}
