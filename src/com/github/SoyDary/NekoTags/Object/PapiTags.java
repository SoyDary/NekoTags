package com.github.SoyDary.NekoTags.Object;


import java.util.HashMap;

import org.bukkit.entity.Player;

import com.github.SoyDary.NekoTags.NekoTags;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PapiTags extends PlaceholderExpansion {

    private NekoTags plugin;

    public PapiTags(NekoTags plugin) {
    	this.plugin = plugin;
    }
 
    @Override
    public boolean persist(){
        return true;
    }
   
    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public String getAuthor(){
        return "FavioMC19";
    }
   
    @Override
    public String getIdentifier(){
        return "nekotags";
    }

    @Override
    public String getVersion(){
        return plugin.getDescription().getVersion();
    }
    
    HashMap<Player, HashMap<String, Integer>> animationsCount = new HashMap<Player, HashMap<String, Integer>>();
    HashMap<Player, HashMap<String, Integer>> repeatCount = new HashMap<Player, HashMap<String, Integer>>();
    HashMap<String, Integer> animationsCountTag = new HashMap<String, Integer>();
    HashMap<String, Integer> repeatCountTag = new HashMap<String, Integer>();
    
	@Override
    public String onPlaceholderRequest(Player p, String id){
		String type = id.split("_")[0];
		String data = id.split("_")[1];
    	if(p != null) {
    		String pt = plugin.getData().getTag(p.getUniqueId().toString());
    		Tag t = null;
    		if(pt != null && plugin.getManager().getTags().containsKey(pt)) {
    			t = plugin.getManager().getTags().get(pt);
    		}
    		if(type.equalsIgnoreCase("active")) {
    			if(data.equalsIgnoreCase("tag")) {
    				if(t != null) {
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
    			}else if(data.equalsIgnoreCase("name")) {
    				if(t != null) {
    					return t.getName();
    				}
    			}else if(data.equalsIgnoreCase("desc")) {
    				if(t != null) {
    					return t.getDescription();
    				}
    			}
    			return "";
    		}
    	}
    	
    	if(type.equalsIgnoreCase("tag")) {
			if(data.equalsIgnoreCase("name")) {
				String name = id.split("_")[2];
				Tag t = plugin.getManager().getTags().getOrDefault(name, null);
				if(t != null) {
					return t.getName();
				}
			}else if(data.equalsIgnoreCase("desc")) {
				String name = id.split("_")[2];
				Tag t = plugin.getManager().getTags().getOrDefault(name, null);
				if(t != null) {
					return t.getDescription();
				}
			}else {
				Tag t = plugin.getManager().getTags().getOrDefault(data, null);
				if(t != null) {
					if(t.hasMultiTags()) {
						int index = animationsCountTag.getOrDefault(t.getKey(), 0);
						int count = repeatCountTag.getOrDefault(t.getKey(), t.getRepeatFrame());
						if(count > 1) {
							repeatCountTag.put(t.getKey(), count-1);
							return t.getTags().get(index);
						}else {
							if(index == t.getTags().size()-1) {
								animationsCountTag.put(t.getKey(), 0);
    						}else {
    							animationsCountTag.put(t.getKey(), index+1);
    						}
							repeatCountTag.remove(t.getKey());
							return t.getTags().get(index);
						}
						
					}else {
						return t.getTag();
					}
					
				}
			}
		}
       
        return "";
    }
}