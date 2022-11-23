package com.github.SoyDary.NekoTags;

import java.io.File;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.SoyDary.NekoTags.Commands.CommandCompleter;
import com.github.SoyDary.NekoTags.Commands.Commands;
import com.github.SoyDary.NekoTags.Listeners.InventoryListener;
import com.github.SoyDary.NekoTags.Managers.ConditionsManager;
import com.github.SoyDary.NekoTags.Managers.Data;
import com.github.SoyDary.NekoTags.Managers.Manager;
import com.github.SoyDary.NekoTags.Object.PapiTags;
import com.github.SoyDary.NekoTags.Utils.Utils;

public class NekoTags extends JavaPlugin{
	
	Manager manager;
	Utils utils;
	Commands commands;
	Data data;
	ConditionsManager conditionsmanager;
	
	public void onEnable() {
		getServer().getConsoleSender().sendMessage("[NekoTags] plugin activado!");
		loadPlugin();
		getServer().getPluginCommand("NekoTags").setExecutor(this.commands);
		getServer().getPluginCommand("NekoTags").setTabCompleter(new CommandCompleter(this));
		getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
		new PapiTags(this).register();
	}
	
	public void onDisable() {
		for(Player p : getData().offhandItems.keySet()) p.closeInventory();
		getServer().getConsoleSender().sendMessage("[NekoTags] plugin desactivado!");
	}
	
	public void loadPlugin() {
		loadOrCreateConfig();
		this.manager = new Manager(this);
		this.data = new Data(this);
		this.utils = new Utils(this);
		this.commands = new Commands(this);
		this.conditionsmanager = new ConditionsManager(this);
		manager.loadTags();
	}
	
	public Manager getManager() {
		return this.manager;
	}
	
	public Utils getUtils() {
		return this.utils;
	}
	
	public Data getData() {
		return this.data;
	}
	
	public ConditionsManager getConditions() {
		return this.conditionsmanager;
	}
	
	public static NekoTags getInstance() {
	    return JavaPlugin.getPlugin(NekoTags.class);
	}
	
	private void loadOrCreateConfig() {	
		final File config = new File(this.getDataFolder() + File.separator + "config.yml");
		 if (!config.exists()) {
	           this.reloadConfig();
	           this.getConfig().options().copyDefaults(true);
	           this.saveConfig();
	       } else {
	    	   reloadConfig();
	       }
	}
}
