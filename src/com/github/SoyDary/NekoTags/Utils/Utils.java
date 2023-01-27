package com.github.SoyDary.NekoTags.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.earth2me.essentials.User;
import com.github.SoyDary.NekoTags.NekoTags;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class Utils {
	NekoTags plugin;
	LegacyComponentSerializer lcs;
	TextReplacementConfig italic;

	public Utils(NekoTags plugin) {
		this.lcs = LegacyComponentSerializer.builder().character('&').hexCharacter('#').extractUrls().build();
		this.italic = TextReplacementConfig.builder().match("&<ITALITC> ").replacement("").build();
		this.plugin = plugin;
	}
	

	public Component color(String text) {
		if(text == null) return Component.text("");
		text = fixColors(text);
		if(text.split(" ").length > 1 && text.split(" ")[0].contains("&o")) text = text.replaceAll("&o", "&<ITALITC> &o");
		return lcs.deserialize(text).decoration(TextDecoration.ITALIC, false).replaceText(italic);

	}
	
	public OfflinePlayer getUser(String str) {	
		if(str == null || str.equals("")) return null;
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(p.getName().equalsIgnoreCase(str)) return Bukkit.getOfflinePlayer(p.getUniqueId());
		}
		OfflinePlayer of = Bukkit.getOfflinePlayer(str);
		if(plugin.essentials == null && !of.hasPlayedBefore()) {
			return of;
		}
		
		if(str.length() == 36) {
			User user = plugin.essentials.getUser(UUID.fromString(str));
			if(user != null) {
				return Bukkit.getPlayer(user.getUUID());
			}
		}
		User user = plugin.essentials.getUser(str);
		if(user != null) {
			return Bukkit.getOfflinePlayer(user.getUUID());
		}
		return null;
		
	}
	
	
	
	public String fixColors(String text) {
		return 	text = text
					.replaceAll("&A", "&a")
					.replaceAll("&B", "&b")
					.replaceAll("&C", "&c")
					.replaceAll("&D", "&d")
					.replaceAll("&E", "&e")
					.replaceAll("&F", "&f")
					.replaceAll("&L", "&l")
					.replaceAll("&M", "&m")
					.replaceAll("&N", "&n")
					.replaceAll("&O", "&o")
					.replaceAll("&K", "&K");
	}
	
	public List<Component> color(List<String> list) {
		List<Component> components = new ArrayList<Component>();
		for(String str : list) {
			components.add(color(str));
		}
		return components;	
	}
	
	public String getSkinID(Player p) {
		PlayerProfile profile = p.getPlayerProfile(); 
		if(!profile.hasTextures()) return null;
        String id = new String(profile.getTextures().getSkin().toString()).split("texture/")[1];
        return id;    
    }
	
	public String itemTo64(ItemStack item) {
		ItemStack[] items = {item};
		return itemsToBase64(items);
	}

	public ItemStack base64ToItem(String text){
		try {
			return base64ToItems(text)[0];
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private  String itemsToBase64(final ItemStack[] items) throws IllegalStateException {
	    try {
	        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        final BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
	        dataOutput.writeInt(items.length);

	        for (ItemStack item : items) {
	            dataOutput.writeObject(item);
	        }

	        dataOutput.close();
	        return Base64Coder.encodeLines(outputStream.toByteArray());
	    }
	    catch (Exception e) {
	        throw new IllegalStateException("Error en pasar el item a base64.", e);
	    }
	}

	private  ItemStack[] base64ToItems(final String data) throws IOException {
	        final ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
	        final BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
	        final ItemStack[] items = new ItemStack[dataInput.readInt()];

	        for (int i = 0; i < items.length; ++i) {
	            try {
					items[i] = (ItemStack) dataInput.readObject();
				} catch (ClassNotFoundException e) {
					throw new IOException("Error en pasar el base64 a item.", e);
				} catch (IOException e) {
				}
	        }

	        dataInput.close();
	        return items;
	        
	}

	public Boolean isInt(String text) {
		try {
			Integer.parseInt(text);
			return true;
		}catch(Exception ex) {
			return false;
		}
	}

	public String addChar(String str, String ch, int position) {
	    return str.substring(0, position) + ch + str.substring(position);
	}
                                  
	public ItemStack getHead(String texture, UUID uuid) {
		ItemStack head = new ItemStack(Material.PLAYER_HEAD);
	    SkullMeta headMeta = (SkullMeta) head.getItemMeta();
	    PlayerProfile profile = Bukkit.createProfile(uuid);
	    String encodedData = Base64Coder.encodeString(String.format("{textures:{SKIN:{url:\"%s\"}}}", texture));
	    profile.setProperty(new ProfileProperty("textures", encodedData));
	    headMeta.setPlayerProfile(profile);
	    head.setItemMeta(headMeta);    
	    return head;
	}
	
	public ItemStack getHeadfromUrl(String url) {
	    ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
	    if(url.isEmpty()) return head;	   
	    SkullMeta headMeta = (SkullMeta) head.getItemMeta();
	    PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
	    String encodedData = Base64Coder.encodeString(String.format("{textures:{SKIN:{url:\"%s\"}}}", url));
	    profile.setProperty(new ProfileProperty("textures", encodedData));
	    headMeta.setPlayerProfile(profile);
	    head.setItemMeta(headMeta);    
	    return head;
	}

	public ItemStack createItem(String textitem, Player p) {
		String text = PlaceholderAPI.setPlaceholders(p, textitem);
		ItemStack item = new ItemStack(Material.AIR);
		if(text.toLowerCase().startsWith("head-")) {
			String texture = text.substring(5, text.length());
			if(!texture.contains("-") && texture.length() > 20) {
				item = plugin.getUtils().getHeadfromUrl("http://textures.minecraft.net/texture/"+texture);
			}else if(texture.equalsIgnoreCase("owner")) {
				texture = plugin.getUtils().getSkinID(p); 
			      item = this.plugin.getUtils().getHeadfromUrl("http://textures.minecraft.net/texture/" + texture);
			}else {
				if(texture.contains("-")) {
					item = new ItemStack(Material.PLAYER_HEAD);
					SkullMeta sm = (SkullMeta) item.getItemMeta();
					sm.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(texture)));
					item.setItemMeta(sm);
				}else {
					item = new ItemStack(Material.PLAYER_HEAD);
					SkullMeta sm = (SkullMeta) item.getItemMeta();
					sm.setOwningPlayer(Bukkit.getOfflinePlayer(texture));
					item.setItemMeta(sm);
				}
				
			}
		}else {
			Material material = Material.valueOf(text.toUpperCase());
			item = new ItemStack(material);
		}
		return item;
	}
	
	}
