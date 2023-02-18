package com.github.SoyDary.NekoTags.Object;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.SoyDary.NekoTags.NekoTags;
import com.github.SoyDary.NekoTags.Utils.ItemStackBuilder;
import com.github.SoyDary.NekoTags.Utils.ItemUtils;
import com.google.common.collect.Lists;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;

public class Gui implements InventoryHolder {
	
	public ArrayList<Inventory> pages = new ArrayList<>();
	
	public int page_num;
	Inventory inv;
	
	ArrayList<Integer> multiTagsSlots = new ArrayList<Integer>();
	NekoTags plugin = NekoTags.getInstance();
	
	public Gui(Player p, int page_num) {
		setGui(p, page_num);
		plugin.getData().checkCurrentTag(p);
	}
	
	public ArrayList<Integer> getMultiTagsSlots() {
		return this.multiTagsSlots;
	}
	
	private void setLineGlass(Inventory inv) {
		ItemStack light_gray = new ItemStackBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).option().setTitle("&b").apply().build();
		ItemStack gray = new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).option().setTitle("&b").apply().build();
		for(int i = 36; i < 54; i++) {
			if(i < 45) {
				inv.setItem(i, light_gray);
				continue;
			}
			
			inv.setItem(i, gray);
		}
	}
	
	
	public void setPage(int page_num) {
		this.page_num = page_num;
		this.inv = pages.get(page_num);
	}
	
	private Inventory blankPage(int current, int total) {
	    Inventory page = Bukkit.createInventory(this, 54, plugin.getUtils().color(plugin.getConfig().getString("GUI.gui_name")));
	    setLineGlass(page);
	    ItemStack nextpage = new ItemStack(Material.ARROW);
	    ItemUtils.setData(nextpage, "gui_item", "nextpage");
	    ItemMeta meta = nextpage.getItemMeta();
	    meta.displayName(this.plugin.getUtils().color("&cSiguiente pagina"));
	    nextpage.setItemMeta(meta);
	    ItemStack prevpage = new ItemStack(Material.ARROW);
	    ItemUtils.setData(prevpage, "gui_item", "nextpage");
	    meta = prevpage.getItemMeta();
	    meta.displayName(this.plugin.getUtils().color("&cPagina anterior"));
	    prevpage.setItemMeta(meta);
	    if (total > current + 1)
	      page.setItem(50, nextpage); 
	    if (current >= 1)
	      page.setItem(48, prevpage); 
	    return page;
	  }
	
	public void setGui(Player p, int page_num) {
		this.page_num = page_num;
		List<List<Tag>> sections = Lists.partition(plugin.getManager().guisWithOrder, 36);
		for(List<Tag> section : sections) {
			Inventory page = blankPage(page_num, sections.size());
			int start = 0;
			for(Tag tag : section) {
				String key = tag.getKey();
				String name = tag.getName();
				Boolean selected = hasSelected(tag.getKey(), p);
				ItemStack item = tag.createItem(p, selected);
				ItemUtils.setData(item, "gui_item", key);
				ItemMeta meta = item.getItemMeta();
				meta.displayName(plugin.getUtils().color(plugin.getConfig().getString("GUI.item_name").replaceAll("%tag_name%", name).replaceAll("%tag%", tag.hasMultiTags() ? tag.getTags().get(0) : tag.getTag(p))));
				List<Component> lore = new ArrayList<Component>();
				List<String> defaultLore = plugin.getConfig().getStringList("GUI.default_lore");
				for(String text : defaultLore) {
					if(text.equalsIgnoreCase("%description%")) {
						String[] desc = tag.getDescription().split("\\n");
						for(String text2 : desc) {
							lore.add(plugin.getUtils().color(PlaceholderAPI.setPlaceholders(p, text2)));
						}
					}else {
						lore.add(plugin.getUtils().color(PlaceholderAPI.setPlaceholders(p, text)));
					}
				}
				if(selected) {
					for(String text : plugin.getConfig().getStringList("GUI.selected_lore")) {
						lore.add(plugin.getUtils().color(PlaceholderAPI.setPlaceholders(p, text)));
					}
				}else {
					if(plugin.getData().hasTag(p, tag.getKey())) { 
						for(String text : plugin.getConfig().getStringList("GUI.allowed_lore")) {
							lore.add(plugin.getUtils().color(PlaceholderAPI.setPlaceholders(p, text)));
						}
					}else {
						for(String text : plugin.getConfig().getStringList("GUI.not_allowed_lore")) {
							if(text.contains("%extra_lore%")) {
								if(tag.getExtra() != null) {
									String[] args = text.split("%extra_lore%");
									String p1 = args[0];
									if(p1 != null) {
										lore.add(plugin.getUtils().color(PlaceholderAPI.setPlaceholders(p, p1)));
									}
									String[] ex = tag.getExtra().split("\\n");
									for(String text2 : ex) {
										lore.add(plugin.getUtils().color(PlaceholderAPI.setPlaceholders(p, text2)));
									}
									if(args.length > 1) {
										String p2 = args[1];
										lore.add(plugin.getUtils().color(PlaceholderAPI.setPlaceholders(p, p2)));
									}
								}else {
									lore.add(plugin.getUtils().color(PlaceholderAPI.setPlaceholders(p, text.replaceAll("%extra_lore%", ""))));
								}
								
							}else {
								lore.add(plugin.getUtils().color(PlaceholderAPI.setPlaceholders(p, (text))));
							}
							
						}
					}
				}
				meta.lore(lore);
				item.setItemMeta(meta);
				if(tag.hideNoPerm() && (!plugin.getData().hasTag(p, tag.getKey()))) {
					continue;
				}
				page.setItem(start, item);
				if(tag.hasMultiTags()) {
					multiTagsSlots.add(start);
				}
				start++;
			}
			ItemStack info = plugin.getUtils().createItem(plugin.getConfig().getString("GUI.info_item.item"), p);
			ItemUtils.setData(info, "gui_item", "RemoveTag");
			ItemMeta infom = info.getItemMeta();
			
			infom.displayName(plugin.getUtils().color(plugin.getConfig().getString("GUI.info_item.item_name")));
			List<Component> lore = new ArrayList<Component>();
			String tagname = plugin.getData().getTag(p.getUniqueId().toString());
			if(tagname != null && tagname != "") {
				Tag t = plugin.getManager().getTags().get(tagname);
				if(t == null) {
					plugin.getData().setTag(p.getUniqueId().toString(), null);
					setGui(p, page_num);
					return;
				}
				for(String text : plugin.getConfig().getStringList("GUI.info_item.lore")) {
					lore.add(plugin.getUtils().color(PlaceholderAPI.setPlaceholders(p, text.replaceAll("%tag%", t.hasMultiTags() ? t.getTags().get(0) : t.getTag(p)).replaceAll("%tag_name%", t.getName()))));
				}
			}else {
				for(String text : plugin.getConfig().getStringList("GUI.info_item.no_tag_lore")) {
					lore.add(plugin.getUtils().color(PlaceholderAPI.setPlaceholders(p, text)));
				}
			}
			infom.lore(lore);
			info.setItemMeta(infom);
			int infoSlot = plugin.getConfig().getInt("GUI.info_item.slot");
			page.setItem(infoSlot, info);		
			ItemStack main = plugin.getUtils().getHead("http://textures.minecraft.net/texture/69ea1d86247f4af351ed1866bca6a3040a06c68177c78e42316a1098e60fb7d3", UUID.fromString("3c4e86be-57f4-4f45-8158-c6547211a498"));
			ItemUtils.setData(main, "gui_item", "MainMenu");
			ItemMeta mainMeta = main.getItemMeta();
			mainMeta.displayName(plugin.getUtils().color("&c&lREGRESAR"));
			List<Component> mainlore = new ArrayList<Component>(); mainlore.add(plugin.getUtils().color("&fRegresar al menú principal."));
			mainMeta.lore(mainlore);
			main.setItemMeta(mainMeta);
			page.setItem(49, main);		
			pages.add(page);
		}
		this.inv = pages.get(page_num);
		p.openInventory(pages.get(page_num));
	}
	


	
	public boolean hasSelected(String tag, Player p) {
		String ptag = plugin.getData().getTag(p.getUniqueId().toString());
		if(ptag != null && ptag.equalsIgnoreCase(tag)) {
			return true;
		}
		return false;
	}
	
	public Inventory getInventory() {
		return inv;
	}

}
