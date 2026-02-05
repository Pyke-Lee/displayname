package kr.pyke.displayname.registry.creativetab;

import kr.pyke.displayname.DisplayName;
import kr.pyke.displayname.registry.item.ModItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeTab {
    private ModCreativeTab() { }

    public static final ResourceKey<CreativeModeTab> DISPLAYNAME_TAB_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(DisplayName.MOD_ID, "creative_tab_displayname"));

    public static final CreativeModeTab CREATIVE_TAB = FabricItemGroup.builder()
        .icon(() -> new ItemStack(ModItem.ID_CARD))
        .title(Component.translatable("itemGroup.displayname.creative_tab"))
        .displayItems((parameters, output) -> output.accept(ModItem.ID_CARD))
        .build();

    public static void register() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, DISPLAYNAME_TAB_KEY, CREATIVE_TAB);
    }
}
