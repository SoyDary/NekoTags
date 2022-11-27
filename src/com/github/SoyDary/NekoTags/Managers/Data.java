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
	public Map<String, FileConfiguration> configs;
		
	public Data(NekoTags plugin) {
		this.offhandItems = new HashMap<Player, ItemStack>();
		this.configs = new HashMap<String, FileConfiguration>();
		this.plugin = plugin;
	}
	
	public String getTag(String uuid) {
		FileConfiguration config = getConfiguration(uuid);
		if(config.getString("selected") != null)
			return config.getString("selected");
		
		return null;
	}
	
	public void setTag(String uuid, String tag) {
		FileConfiguration config = getConfiguration(uuid);
		config.set("selected", tag);
		saveConfig(uuid);
	}
	
	public void addTag(String uuid, String tag) {
		FileConfiguration config = getConfiguration(uuid);
		List<String> tags = getTags(uuid);
		if(!tags.contains(tag))
			tags.add(tag);
		config.set("Tags", tags);
		saveConfig(uuid);
	}
	
	public void removeTag(String uuid, String tag) {
		FileConfiguration config = getConfiguration(uuid);
		List<String> tags = getTags(uuid);
		if(tags.contains(tag))
			tags.remove(tag);
		config.set("Tags", tags);
		saveConfig(uuid);
	}
	
	public void saveConfig(String uuid) {
		File file = new File("plugins/NekoTags/Players/"+uuid+".yml");
		FileConfiguration config = configs.getOrDefault(uuid, YamlConfiguration.loadConfiguration(file));
		try {
			config.save(file);
	    }
	    catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public FileConfiguration getConfiguration(String uuid) {
		File file = new File("plugins/NekoTags/Players/"+uuid+".yml");
		FileConfiguration config = configs.getOrDefault(uuid, YamlConfiguration.loadConfiguration(file));
		configs.put(uuid, config);
		return config;
	}
	
	public Boolean hasTag(Player p, String tag) {
		if(p.hasPermission("nekotags.tag."+tag)) return true;
		List<String> tags = getTags(p.getUniqueId().toString());
		return tags.contains(tag);
	}
	
	public List<String> getTags(String uuid){
		FileConfiguration config = getConfiguration(uuid);
		List<String> tags = config.getStringList("Tags");
		if(tags == null)
			tags = new ArrayList<String>();
		return tags;
	}
	public void checkCurrentTag(Player p) {  	   	
		Bukkit.getScheduler().runTaskAsynchronously(plugin, (Runnable)new Runnable() {
            @Override
            public void run() {
            	String tag = plugin.getData().getTag(p.getUniqueId().toString());
            	if(tag == null) return;
            	if(!hasTag(p, tag)) {
            		removeTag(p.getUniqueId().toString(), tag);
            		setTag(p.getUniqueId().toString(), null);
            	}
            }
        });	
	}

}

