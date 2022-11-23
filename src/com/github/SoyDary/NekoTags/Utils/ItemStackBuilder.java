package com.github.SoyDary.NekoTags.Utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;



public class ItemStackBuilder {

    private ItemOption option;
    private ItemStack is;
    
    public ItemStackBuilder(Material mat) {
        this(mat, (byte) 0);
    }

    @SuppressWarnings("deprecation")
	public ItemStackBuilder(Material mat, byte data) {
        this(new ItemStack(mat, 1, data));
    }

    public ItemStackBuilder(ItemStack stack) {
        from(stack);
    }

    public ItemStackBuilder from(ItemStack stack) {
        this.is = stack.clone();
        this.option = new ItemOption(this);
        return this;
    }

    public ItemStackBuilder material(Material mat, byte data) {
        material(mat);
        data(data);
        return this;
    }

    public ItemStackBuilder material(Material mat) {
        if (is == null) {
            is = new ItemStack(mat);
            return this;
        }

        is.setType(mat);
        return this;
    }

    @SuppressWarnings("deprecation")
	public ItemStackBuilder data(byte data) {
        is.getData().setData(data);
        return this;
    }

    public ItemStackBuilder amount(int amount) {
        is.setAmount(amount);
        return this;
    }

    public ItemOption option() {
        return option;
    }

    public ItemStack build() {
        return is;
    }
}
