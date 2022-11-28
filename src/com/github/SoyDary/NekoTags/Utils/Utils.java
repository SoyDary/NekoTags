package com.github.SoyDary.NekoTags.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerTextures;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.github.SoyDary.NekoTags.NekoTags;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;

public class Utils {
	NekoTags plugin;

	public Utils(NekoTags plugin) {
		this.plugin = plugin;
	}
	
	public static int spigotv;
	public void sendMessage(CommandSender s, String message) {
		s.sendMessage(color(message));
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

	private final Pattern pattern = Pattern.compile("(?<!\\\\)(#[a-fA-F0-9]{6})");

	public String addChar(String str, String ch, int position) {
	    return str.substring(0, position) + ch + str.substring(position);
	}

	public String parseColor(String text) {
		if(text.length() == 0 | text == null || text.length() < 7) {
			return text;
		}
		
		String tedit = text;
		String text2 = text;
		for(int i = text2.length()-1; i > 0; i--) {
			String c = text2.charAt(i)+"";
			
			if(c.contains("#") && i-1 < 0 && text2.length() >= 7) {
				String color = c+text2.charAt(i+1)+text2.charAt(i+2)+text2.charAt(i+3)+text2.charAt(i+4)+text2.charAt(i+5)+text2.charAt(i+6);
				if(isColor(color)) {
					tedit = addChar(text2, "&", i);
				}
			}else {
				if(c.contains("#") && i-1 > 0 && i+6 <= text2.length()-1) {
					String color = c+text2.charAt(i+1)+text2.charAt(i+2)+text2.charAt(i+3)+text2.charAt(i+4)+text2.charAt(i+5)+text2.charAt(i+6);
					if(isColor(color) && !(text2.charAt(i-1)+"").contains("&")) {
						tedit = addChar(text2, "&", i);
					}
				}
			}
		}
		return tedit;
	}


	public String color(String text) {
		String end = "";
		if(text == null || text == "") {
			return "";
		}
		String text2 = parseColor(text);
		
		String[] words = text2.split(Pattern.quote("&#"));
		if(words.length != 0) {
			int count = 0;
			for(String t : words) {
				String more = "";
				if(count != 0) {
					more = "#";
				}
				String t2 = t;
				t2 = normalColor(t2);
				t2 = HexColor(more+t2);
				t2 = color2(t2);
				end += t2;
				count++;
			}
			return end;
		}else {
			return text;
		}
		
	}
	
	private String color2(String text) {
		String end = "";
		if(text == null || text == "") {
			return "";
		}
		String text2 = parseColor(text);
		
		String[] words = text2.split(Pattern.quote("#"));
		if(words.length != 0) {
			int count = 0;
			for(String t : words) {
				String more = "";
				if(count != 0) {
					more = "#";
				}
				String t2 = t;
				t2 = normalColor(t2);
				t2 = HexColor(more+t2);
				end += t2;
				count++;
			}
			return end;
		}else {
			return text;
		}
		
	}

	private ArrayList<String> normalColors = new ArrayList<String>();

	public ArrayList<String>  normalsColor(){
		return normalColors;
	}

	public boolean isColor(String text) {
		String text2 = text;
		if(text.startsWith("&")) {
			text2 = text.replaceFirst("&", "");
		}
	    try {
	  	  ChatColor.of(text2);
	  	  return true;
	    }catch(Exception ex) {
	  	  return false;
	    }
	}


	public List<String> color(List<String> lore) {
		List<String> endlore = new ArrayList<String>();
		if(lore == null) {
			return endlore;
		}
		for(String text : lore) {
			endlore.add(color(text));
		}
		return endlore;
	}
	
	public List<String> color(List<String> lore, Player p) {
		List<String> endlore = new ArrayList<String>();
		if(lore == null) {
			return endlore;
		}
		for(String text : lore) {
			endlore.add(PlaceholderAPI.setPlaceholders(p, color(text)));
		}
		return endlore;
	}

	public String getData(ItemStack item, String data) {
		NamespacedKey key = new NamespacedKey(plugin, data);
		ItemMeta itemMeta = item.getItemMeta();
		PersistentDataContainer tagContainer = itemMeta.getPersistentDataContainer();

		if(tagContainer.has(key , PersistentDataType.STRING)) {
			return tagContainer.get(key, PersistentDataType.STRING);
		}else {
			return null;
		}
	}

	public void setData(ItemStack item, String data, Object value) {
		ItemMeta im = item.getItemMeta();
		NamespacedKey spaceKey = new NamespacedKey(plugin, data);
		im.getPersistentDataContainer().set(spaceKey, PersistentDataType.STRING, value+"");
		item.setItemMeta(im);
	}


	private String HexColor(String message) {
	    Matcher matcher = pattern.matcher(message);
	    while (matcher.find()) {
	      String color = message.substring(matcher.start(), matcher.end());
	      Boolean isColor = false;
	      try {
	    	  ChatColor.of(color);
	    	  isColor = true;
	      }catch(Exception ex) {
	    	  
	      }
	      if(isColor) {
	    	  message = message.replace(color, "" + ChatColor.of(color));
	      }
	    } 
	    return message;
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

	private String normalColor(String message) {
	      message = ChatColor.translateAlternateColorCodes('&', message); 
	    return message;
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
