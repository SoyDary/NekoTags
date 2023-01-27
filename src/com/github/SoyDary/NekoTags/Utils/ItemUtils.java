package com.github.SoyDary.NekoTags.Utils;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.github.SoyDary.NekoTags.NekoTags;

public class ItemUtils {

	private static NekoTags plugin = NekoTags.getInstance();
	
	public static String getData(ItemStack item, Object data) {
		if(item == null || !item.hasItemMeta()) return "null";
		NamespacedKey key = data instanceof NamespacedKey ? (NamespacedKey)data : new NamespacedKey(plugin, (String)data);
		ItemMeta itemMeta = item.getItemMeta();
		PersistentDataContainer tagContainer = itemMeta.getPersistentDataContainer();
		if(tagContainer.has(key, PersistentDataType.STRING)) {
			return tagContainer.get(key, PersistentDataType.STRING);
		} else {
			return "null";
		}
	}
	
	public static void setData(ItemStack item, String data, Object value) {
		ItemMeta im = item.getItemMeta();
		NamespacedKey spaceKey = new NamespacedKey(plugin, data);
		im.getPersistentDataContainer().set(spaceKey, PersistentDataType.STRING, value+"");	
		item.setItemMeta(im);
	}
}
