package net.pitan76.bedrocktools.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.pitan76.bedrocktools.item.BedrockPickaxeItem;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.midohra.block.BlockState;
import net.pitan76.mcpitanlib.midohra.block.MCBlocks;
import net.pitan76.mcpitanlib.midohra.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {
    @Inject(method = "calcBlockBreakingDelta", at = @At(value = "JUMP", opcode = Opcodes.IFNE, shift = At.Shift.AFTER), cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
    public void inject_calcBlockBreakingDelta(net.minecraft.block.BlockState _state, PlayerEntity playerEntity, BlockView world, BlockPos pos, CallbackInfoReturnable<Float> cir, float hardness) {
        Player player = new Player(playerEntity);
        BlockState state = BlockState.of(_state);
        ItemStack stack = ItemStack.of(player.getMainHandStack());

        if (hardness == -1.0F && stack.getItem().instanceOf(BedrockPickaxeItem.class)) {
            int effective = player.canHarvest(state.toMinecraft()) ? 30 : 100;
            if (state.getBlock().equals(MCBlocks.BEDROCK)) {
                cir.setReturnValue(playerEntity.getBlockBreakingSpeed(state.toMinecraft()) / 20F / effective);
                return;
            }
            if (state.getBlock().equals(MCBlocks.END_PORTAL_FRAME)) {
                cir.setReturnValue(playerEntity.getBlockBreakingSpeed(state.toMinecraft()) / 5F / effective);
                return;
            }

            cir.setReturnValue(playerEntity.getBlockBreakingSpeed(state.toMinecraft()) / 10F / effective);
        }
    }
}
