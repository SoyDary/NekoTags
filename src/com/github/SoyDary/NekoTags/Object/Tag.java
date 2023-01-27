package com.github.SoyDary.NekoTags.Object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.SoyDary.NekoTags.NekoTags;
import com.github.SoyDary.NekoTags.Utils.ItemStackBuilder;

import me.clip.placeholderapi.PlaceholderAPI;

public class Tag {
	NekoTags plugin = NekoTags.getInstance();
	private String name;
	private String tag;
	private String description;
	private String itemText;
	private String selectedItemText;
	private String key;
	private String extra;
	private String condition;
	private Boolean animatedItem;
	private Boolean multiTags = false;
	private List<String> tags;
	private List<String> functions;
	private List<String> denyFunctions;
	private int repeatingFrames = 0;
	private Boolean hideNoPerm;
	private Map<String, String> customData = new HashMap<String, String>();
	
	public Tag(String name, String tag, String description, String itemText, String selectedItemText, String key, Boolean animatedItem, String extra, String condition, List<String> functions, List<String> denyFunctions, Boolean hideNoPerm) {
		this.name = name;
		this.tag = tag;
		this.description = description;
		this.itemText = itemText;
		this.selectedItemText = selectedItemText;
		this.key = key;
		this.extra = extra;
		this.animatedItem = animatedItem;
		this.condition = condition;
		this.functions = functions;
		this.denyFunctions = denyFunctions;
		this.hideNoPerm = hideNoPerm;
		if(tag.contains("|")) {
			parseTags();
		}
	}
	
	private void parseTags() {
		multiTags = true;
		this.tags = new ArrayList<String>();
		if(tag.contains(";")) {
			String ntag = tag.split(Pattern.quote(";"))[0];
			int frames = Integer.parseInt(tag.split(Pattern.quote(";"))[1]);
			this.tag = ntag;
			this.repeatingFrames = frames;
			if(frames == 0) {
				this.repeatingFrames = 1;
			}
		}
		for(String t : tag.split(Pattern.quote("|"))) {
			tags.add(t);
		}
	}

	
	public ItemStack createItem(Player player, Boolean selected) {
		String text = selected ? this.selectedItemText : this.itemText;
		ItemStack item = new ItemStack(Material.AIR);
		
		String defaultItemText = plugin.getConfig().getString("GUI.default_item");
		defaultItemText = PlaceholderAPI.setPlaceholders(player, defaultItemText);
		if(text == null && !selected) {
			text = PlaceholderAPI.setPlaceholders(player, defaultItemText);
		}
		if(selected && text == null) {
			if(this.itemText == null) {
				text = PlaceholderAPI.setPlaceholders(player, defaultItemText);
			}else {
				text = PlaceholderAPI.setPlaceholders(player, this.itemText);
			}
		}
		
		if(text.toLowerCase().startsWith("head-")) {
			item = plugin.getUtils().createItem(text, player);
		}else {
			Material material = Material.valueOf(text.toUpperCase());
			item = new ItemStackBuilder(material).option().hideFlag(ItemFlag.HIDE_ATTRIBUTES).apply().build();
		}
		if(selected) {
			ItemMeta itemm = item.getItemMeta();
			itemm.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			itemm.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			item.setItemMeta(itemm);
			item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		}
		return item;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getTag() {
		return tag;
	}
	/*
	public String getTag(Boolean color) {
		return color ? plugin.getUtils().color(this.tag) : tag;
	}
	*/
	
	public List<String> getTags(){
		return tags;
	}
	
	/*
	public List<String> getTags(Boolean color){
		return color ?  plugin.getUtils().color(tags) : tags;
	}
	*/
	
	public List<String> getFunctions(){
		return functions;
	}
	
	public List<String> getDenyFunctions(){
		return denyFunctions;
	}
	
	public String getCondition() {
		return this.condition;
	}
	
	public String getExtra() {
		return this.extra;
	}
	
	public Boolean hasMultiTags() {
		return this.multiTags;
	}
	
	public Boolean hasAnimatedItem() {
		return this.animatedItem;
	}
	
	public String getKey() {
		return this.key;
	}
	
	public String getItemText() {
		return this.itemText;
	}
	
	public String getSelectedItemText() {
		return this.selectedItemText;
	}
	
	public int getRepeatFrame() {
		return this.repeatingFrames;
	}
	
	public Boolean hideNoPerm() {
		return this.hideNoPerm;
	}
	
	public String getCustomData(String key) {
		return customData.getOrDefault(key, null);
	}
	
	public void setCustomData(String key, String value) {
		customData.put(key, value);
	}
	
}
