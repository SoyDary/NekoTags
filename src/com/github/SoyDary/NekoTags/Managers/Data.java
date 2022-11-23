package com.github.SoyDary.NekoTags.Managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.SoyDary.NekoTags.NekoTags;

public class Data {
	NekoTags plugin;
	public Map<Player, ItemStack> offhandItems;
	
	public Data(NekoTags plugin) {
		this.plugin = plugin;
		this.offhandItems = new HashMap<Player, ItemStack>();
	}
	
	public Map<String, FileConfiguration> configs = new HashMap<String, FileConfiguration>();
	
	public String getTag(String uuid) {
		FileConfiguration config = getConfiguration(uuid);
		if(config.getString(uuid) != null)
			return config.getString(uuid);
		
		return null;
	}
	
	public void setTag(String uuid, String tag) {
		FileConfiguration config = getConfiguration(uuid);
		config.set(uuid, tag);
		saveConfig(uuid);
	}
	
	public void saveConfig(String uuid) {
		File file = new File("plugins/NekoTags/data.yml");
		FileConfiguration config = configs.getOrDefault(uuid, YamlConfiguration.loadConfiguration(file));
		try {
			config.save(file);
	    }
	    catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public FileConfiguration getConfiguration(String uuid) {
		File file = new File("plugins/NekoTags/data.yml");
		FileConfiguration config = configs.getOrDefault(uuid, YamlConfiguration.loadConfiguration(file));
		configs.put(uuid, config);
		return config;
	}
	public List<String> getTags(Player p) {
		List<String> tags = new ArrayList<String>();
		for(String str : plugin.getManager().getTags().keySet()) {
			if(!p.hasPermission("nekotags.tag."+str)) continue;
			tags.add(str);
		}
		return tags;
		
	}
	public void checkCurrentTag(Player p) {  	   	
		Bukkit.getScheduler().runTaskAsynchronously(plugin, (Runnable)new Runnable() {
            @Override
            public void run() {
            	String tag = plugin.getData().getTag(p.getUniqueId().toString());
            	if(tag == null) return;
            	if(!p.hasPermission("nekotags.tag."+tag)) {
            		plugin.getData().setTag(p.getUniqueId().toString(), null);
            	}
            }
        });	
	}
	

}
