package com.github.SoyDary.NekoTags.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.github.SoyDary.NekoTags.NekoTags;

public class CommandCompleter implements TabCompleter {
	
	NekoTags plugin;
	public CommandCompleter(NekoTags plugin) {
		this.plugin = plugin;
	}
	@Override
	public List<String> onTabComplete(CommandSender s, Command c, String l, String[] a) {
		if (a.length == 1) {
	        List<String> commandsList = new ArrayList<>();
	        List<String> preCommands = new ArrayList<>();
	        if(s.hasPermission("nekotags.admin")) {
	        	commandsList.add("reload");
	        	commandsList.add("give");
	        	commandsList.add("remove");
	        	commandsList.add("list");
	        }
	        commandsList.add("select");
	        for (String text : commandsList) {
	          if (text.toLowerCase().startsWith(a[0].toLowerCase()))
	            preCommands.add(text); 
	        } 
	        return preCommands;
			
		}
		if (a.length == 2) {
			if(!(s instanceof Player)) return null;
			Player p = (Player) s;
			if(a[0].equalsIgnoreCase("select")) {
		        List<String> commandsList = new ArrayList<>();
		        List<String> preCommands = new ArrayList<>();
		        preCommands = plugin.getData().getTags(p.getUniqueId().toString());
		        for (String text : commandsList) {
		          if (text.toLowerCase().startsWith(a[1].toLowerCase()))
		            preCommands.add(text); 
		        } 
		        return preCommands;
			}			
		}
		if (a.length == 3) {
			if(!(s instanceof Player)) return null;
			Player p = (Player) s;
			if(a[0].equalsIgnoreCase("give")) {
		        List<String> commandsList = new ArrayList<>();
		        List<String> preCommands = new ArrayList<>();
		        List<String > tags = plugin.getData().getTags(p.getUniqueId().toString());
		        for(String t : plugin.getManager().getTags().keySet()) {
		        	if(!tags.contains(t)) commandsList.add(t);
		        }
		        for (String text : commandsList) {
		          if (text.toLowerCase().contains(a[2].toLowerCase()))
		            preCommands.add(text); 
		        } 
		        return preCommands;
			}
			if(a[0].equalsIgnoreCase("remove")) {
		        List<String> commandsList = new ArrayList<>();
		        List<String> preCommands = new ArrayList<>();
		        List<String > tags = plugin.getData().getTags(p.getUniqueId().toString());
		        for(String t : plugin.getManager().getTags().keySet()) {
		        	if(tags.contains(t)) commandsList.add(t);
		        }
		        for (String text : commandsList) {
		          if (text.toLowerCase().contains(a[2].toLowerCase()))
		            preCommands.add(text); 
		        } 
		        return preCommands;
			}
		}
		return null;		
	}
}
