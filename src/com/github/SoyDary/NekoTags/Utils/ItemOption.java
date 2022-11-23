package com.github.SoyDary.NekoTags.Utils;

import com.github.SoyDary.NekoTags.NekoTags;
import com.google.common.collect.Lists;

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
    
    protected ItemOption(ItemStackBuilder builder) {
        this.builder = builder;
        this.is = builder.build();
        if(is.getItemMeta() == null) {
        	is.setItemMeta(Bukkit.getItemFactory().getItemMeta(is.getType()));
        }
        this.meta = is.getItemMeta();
    }

    public ItemOption setTitle(String s) {
        meta.setDisplayName(plugin.getUtils().color(s));
        verify();
        return this;
    }
    
    public ItemOption hideFlag(ItemFlag flag) {
    	meta.addItemFlags(flag);
    	verify();
    	return this;
    }

    public ItemOption setLore(String... s) {
        setLore(Arrays.asList(s));
        return this;
    }

    public ItemOption setLore(List<String> lores) {
        meta.setLore(plugin.getUtils().color(lores));
        verify();
        return this;
    }

    public ItemOption addLoreLine(String s) {
        List<String> lore = meta.getLore() == null ? Lists.newArrayList() : meta.getLore();
        lore.add(plugin.getUtils().color(s));
        setLore(lore);
        return this;
    }

    public ItemOption clearLoreLine(int index) {
        List<String> lore = meta.getLore();
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
