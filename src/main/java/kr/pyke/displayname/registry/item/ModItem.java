package kr.pyke.displayname.registry.item;

import kr.pyke.displayname.DisplayName;
import kr.pyke.displayname.registry.item.idcard.IDCard;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ModItem {
    private ModItem() { }

    public static final Item ID_CARD = new IDCard(new Item.Properties().stacksTo(99));

    public static void register() {
        Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(DisplayName.MOD_ID, "id_card"), ID_CARD);
    }
}
