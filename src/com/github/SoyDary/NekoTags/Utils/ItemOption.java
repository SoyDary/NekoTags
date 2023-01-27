package com.github.SoyDary.NekoTags.Utils;

import com.github.SoyDary.NekoTags.NekoTags;
import com.google.common.collect.Lists;

import net.kyori.adventure.text.Component;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class ItemOption {

    private final ItemStackBuilder builder;
    private final ItemStack is;
    private final ItemMeta meta;
    public NekoTags plugin = NekoTags.getInstance();
    
    public ItemOption(ItemStackBuilder builder) {
        this.builder = builder;
        this.is = builder.build();
        if(is.getItemMeta() == null) {
        	is.setItemMeta(Bukkit.getItemFactory().getItemMeta(is.getType()));
        }
        this.meta = is.getItemMeta();
    }

    public ItemOption setTitle(String s) {
        meta.displayName(plugin.getUtils().color(s));
        verify();
        return this;
    }
    
    public ItemOption hideFlag(ItemFlag flag) {
    	meta.addItemFlags(flag);
    	verify();
    	return this;
    }

    public ItemOption setLore(Component... s) {
        setLore(Arrays.asList(s));
        return this;
    }

    public ItemOption setLore(List<Component> lores) {
        meta.lore(lores);
        verify();
        return this;
    }

    public ItemOption addLoreLine(String s) {
        List<Component> lore = meta.lore() == null ? Lists.newArrayList() : meta.lore();
        lore.add(plugin.getUtils().color(s));
        setLore(lore);
        return this;
    }

    public ItemOption clearLoreLine(int index) {
        List<Component> lore = meta.lore();
        lore.remove(index);
        setLore(lore);
        return this;
    }

    public void verify() {
        is.setItemMeta(meta);
        builder.from(is);
    }

    public ItemStackBuilder apply() {
        return builder;
    }

	
	public ItemOption addEnchantment(Enchantment enchant, int level) {
		meta.addEnchant(enchant, level, true);
		verify();
		return this;
	}


}
