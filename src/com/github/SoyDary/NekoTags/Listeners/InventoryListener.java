package com.github.SoyDary.NekoTags.Listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.github.SoyDary.NekoTags.NekoTags;
import com.github.SoyDary.NekoTags.Object.Gui;
import com.github.SoyDary.NekoTags.Object.Tag;

import me.clip.placeholderapi.PlaceholderAPI;

public class InventoryListener implements Listener{
	NekoTags plugin;
	public InventoryListener(NekoTags plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerClickedInventory(InventoryClickEvent e) {
		Player p = (Player)e.getWhoClicked();		
		Boolean open = true;
		if(!(e.getInventory().getHolder() instanceof Gui)) return;
		e.setCancelled(true);	
		if(e.getView().getTopInventory().equals(e.getClickedInventory())) {
			ItemStack item = e.getCurrentItem();
			if(item == null || item.getType().equals(Material.AIR)) {
				return;
			}
			String tag = plugin.getData().getTag(p.getUniqueId().toString());
			if(item.getItemMeta() != null && item.getItemMeta().getLocalizedName() != null) {
				String loc = item.getItemMeta().getLocalizedName();
				if(loc.equals("MainMenu")) {
					Bukkit.dispatchCommand(p, "menu");
					return;
				}
				if(loc.equalsIgnoreCase("RemoveTag")) {
					if(tag == null) {
						p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 5, 1);
					}else {
						plugin.getData().setTag(p.getUniqueId().toString(), null);
						p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 5, 1);
					}
					
				}else if(plugin.getManager().getTags().containsKey(loc)){
					if(plugin.getData().hasTag(p, loc)) {
						if(!loc.equals(tag)) {
							plugin.getData().setTag(p.getUniqueId().toString(), loc);
							p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 5, 1);
						}else {
							p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 5, 1);
						}
					}else {
						Tag t = plugin.getManager().getTags().getOrDefault(loc, null);
						if(t != null && t.getCondition() != null) {
							String conditions = PlaceholderAPI.setPlaceholders(p, t.getCondition());
							List<String> ExecutableFunctions = plugin.getConditions().parseConditions(conditions) ? plugin.getUtils().color(t.getFunctions(), p) : plugin.getUtils().color(t.getDenyFunctions(), p);
							for(String value : ExecutableFunctions) {
								plugin.getConditions().sendFunctions(value, p);
							}
						}
					}
					
				}else {
					if(item.getItemMeta().getLocalizedName().equalsIgnoreCase("nextpage")) {
						Gui gui = new Gui(p, plugin.getManager().guis.get(p).page_num+1);
						plugin.getManager().guis.put(p, gui);
						open = false;
					}else if(item.getItemMeta().getLocalizedName().equalsIgnoreCase("prevpage")) {
						Gui gui = new Gui(p, plugin.getManager().guis.get(p).page_num-1);
						plugin.getManager().guis.put(p, gui);
						open = false;
					}
				}
				if(open) {
					Gui gui = new Gui(p, plugin.getManager().guis.get(p).page_num);
					plugin.getManager().guis.put(p, gui);
				}
			}
		}
	}
	
	
	@EventHandler
	public void swapItem(InventoryClickEvent e) {
		if(!isMenu(e.getInventory().getHolder())) return;
		if(e.getClick() != ClickType.SWAP_OFFHAND) return;		
		e.setCancelled(true);	
	}
	
	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent e) {		
		if(!isMenu(e.getInventory().getHolder())) return;
		plugin.getData().offhandItems.put((Player) e.getPlayer(), e.getPlayer().getInventory().getItem(40));
		
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {	
		if(!isMenu(e.getInventory().getHolder())) return;
		if(!plugin.getData().offhandItems.containsKey(e.getPlayer())) return;
		e.getPlayer().getInventory().setItem(40, plugin.getData().offhandItems.get(e.getPlayer()));
		plugin.getData().offhandItems.remove(e.getPlayer());
	}
	
	public boolean isMenu(InventoryHolder holder) {
		if(holder instanceof Gui) return true;
		return false;		
	}

	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		plugin.getData().checkCurrentTag(event.getPlayer());
	}
	
	
}
