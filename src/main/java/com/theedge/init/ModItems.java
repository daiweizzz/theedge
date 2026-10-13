package com.theedge.init;

import com.theedge.TheEdgeMod;
import com.theedge.item.SandevistanItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    private ModItems() {
    }

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TheEdgeMod.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TheEdgeMod.MODID);

    public static final DeferredItem<Item> SANDEVISTAN = ITEMS.register("sandevistan",
            () -> new SandevistanItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> THEEDGE_TAB =
            CREATIVE_MODE_TABS.register("theedge", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.theedge"))
                    .icon(() -> new ItemStack(SANDEVISTAN.get()))
                    .displayItems((parameters, output) -> output.accept(SANDEVISTAN.get()))
                    .build());
}
