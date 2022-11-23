package com.github.SoyDary.NekoTags.Managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.bukkit.entity.Player;

import com.github.SoyDary.NekoTags.NekoTags;
import com.github.SoyDary.NekoTags.Object.Gui;
import com.github.SoyDary.NekoTags.Object.Tag;
public class Manager {
	NekoTags plugin;
	private HashMap<String, Tag> tags;
	public HashMap<Player, Gui> guis;
	public ArrayList<Tag> guisWithOrder;
	private HashMap<Player, HashMap<String, Integer>> animationsCount;
	private HashMap<Player, HashMap<String, Integer>> repeatCount;
	
	public Manager(NekoTags plugin) {
		this.plugin = plugin;
		tags = new HashMap<String, Tag>();
		guis = new HashMap<Player, Gui>();
		guisWithOrder = new ArrayList<Tag>();
	    animationsCount = new HashMap<Player, HashMap<String, Integer>>();
	    repeatCount = new HashMap<Player, HashMap<String, Integer>>();
	}
	
	public void loadTags() {
		Set<String> keys = plugin.getConfig().getConfigurationSection("Tags").getKeys(false);
		for(String key : keys) {
			String name = plugin.getConfig().getString("Tags."+key+".name");
			String description = plugin.getConfig().getString("Tags."+key+".description");
			String tag = plugin.getConfig().getString("Tags."+key+".tag");
			String item = plugin.getConfig().getString("Tags."+key+".item");
			String selectedItem = plugin.getConfig().getString("Tags."+key+".selected_item");
			String animatedItem = plugin.getConfig().getString("Tags."+key+".animated_item");
			String extra = plugin.getConfig().getString("Tags."+key+".extra_lore");
			String condition = plugin.getConfig().getString("Tags."+key+".no_perm_action.condition");
			List<String> functions = plugin.getConfig().getStringList("Tags."+key+".no_perm_action.functions");
			List<String> denyFunctions = plugin.getConfig().getStringList("Tags."+key+".no_perm_action.deny_functions");
			Boolean hideNoPerm = plugin.getConfig().getBoolean("Tags."+key+".no_perm_hide");
			Tag t = new Tag(name, tag, description, item, selectedItem, key, animatedItem == null ? false : Boolean.valueOf(animatedItem), extra, condition, functions, denyFunctions, hideNoPerm);
			if(plugin.getConfig().contains("Tags."+key+".custom_data")) {
				for(String k : plugin.getConfig().getConfigurationSection("Tags."+key+".custom_data").getKeys(false)) {
					String v = plugin.getConfig().getString("Tags."+key+".custom_data."+k);
					t.setCustomData(k, v);
				}
			}
			
			tags.put(key, t);
			guisWithOrder.add(t);
		}
		plugin.getServer().getConsoleSender().sendMessage(plugin.getUtils().color("&8[&dNeko&eTags&8] &eSe han cargado &d"+keys.size()+"&e tags en el servidor!"));
		//startAnimationsTask(); XD
	}
	
	private String getTag(Player p, Tag t) {
		if(t.hasMultiTags()) {
			HashMap<String, Integer> cache = animationsCount.getOrDefault(p, new HashMap<String, Integer>());
			HashMap<String, Integer> repeatCache = repeatCount.getOrDefault(p, new HashMap<String, Integer>());
			int index = cache.getOrDefault(t.getKey(), 0);
			int count = repeatCache.getOrDefault(t.getKey(), t.getRepeatFrame());
			if(count > 1) {
				repeatCache.put(t.getKey(), count-1);
				repeatCount.put(p, repeatCache);
				return t.getTags().get(index);
			}else {
				if(index == t.getTags().size()-1) {
					cache.put(t.getKey(), 0);
				}else {
					cache.put(t.getKey(), index+1);
				}
				animationsCount.put(p, cache);
				repeatCache.remove(t.getKey());
				repeatCount.put(p, repeatCache);
				return t.getTags().get(index);
			}
			
		}else {
			return t.getTag();
		}
	}

	public HashMap<String, Tag> getTags(){
		return this.tags;
	}
	
	
}
