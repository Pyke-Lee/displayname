package kr.pyke.displayname.registry.item.idcard;

import kr.pyke.displayname.client.DisplayNameClient;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public final class IDCard extends Item {
    public IDCard(Item.Properties props) { super(props); }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);
        if (level.isClientSide) { DisplayNameClient.openChageDisplayNameScreen(); }

        return InteractionResultHolder.sidedSuccess(item, level.isClientSide);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        if (context.getLevel().isClientSide) { DisplayNameClient.openChageDisplayNameScreen(); }

        return InteractionResult.sidedSuccess(context.getLevel().isClientSide);
    }
}