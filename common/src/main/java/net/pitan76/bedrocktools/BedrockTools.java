package net.pitan76.bedrocktools;

import net.minecraft.world.InteractionHand;
import net.pitan76.bedrocktools.cmd.BedrockToolsCommand;
import net.pitan76.bedrocktools.item.BedrockPickaxeItem;
import net.pitan76.bedrocktools.item.CreativeShotKillItem;
import net.pitan76.bedrocktools.item.CreativeTabs;
import net.pitan76.mcpitanlib.api.command.CommandRegistry;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.block.result.BlockBreakResult;
import net.pitan76.mcpitanlib.api.event.v1.AttackEntityEventRegistry;
import net.pitan76.mcpitanlib.api.event.v2.BlockEventRegistry;
import net.pitan76.mcpitanlib.api.registry.v2.CompatRegistryV2;
import net.pitan76.mcpitanlib.api.util.CompatActionResult;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.mcpitanlib.api.util.EntityUtil;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.midohra.block.BlockState;
import net.pitan76.mcpitanlib.midohra.block.MCBlocks;
import net.pitan76.mcpitanlib.midohra.item.ItemStack;
import net.pitan76.mcpitanlib.midohra.util.math.BlockPos;
import net.pitan76.mcpitanlib.midohra.world.World;

public class BedrockTools {
    public static final String MOD_ID = "bedrocktools76";

    public static final CompatRegistryV2 registry = CompatRegistryV2.create(MOD_ID);

    public static void init() {
        registry.registerItemGroup(_id("tools"), CreativeTabs.BEDROCK_TOOLS_GROUP);

        registry.registerItem(_id("obsidian_sword"), () -> Items.OBSIDIAN_SWORD);
        registry.registerItem(_id("obsidian_axe"), () -> Items.OBSIDIAN_AXE);
        registry.registerItem(_id("obsidian_pickaxe"), () -> Items.OBSIDIAN_PICKAXE);
        registry.registerItem(_id("obsidian_shovel"), () -> Items.OBSIDIAN_SHOVEL);
        registry.registerItem(_id("obsidian_hoe"), () -> Items.OBSIDIAN_HOE);
        registry.registerItem(_id("bedrock_sword"), () -> Items.BEDROCK_SWORD);
        registry.registerItem(_id("bedrock_axe"), () -> Items.BEDROCK_AXE);
        registry.registerItem(_id("bedrock_pickaxe"), () -> Items.BEDROCK_PICKAXE);
        registry.registerItem(_id("bedrock_shovel"), () -> Items.BEDROCK_SHOVEL);
        registry.registerItem(_id("bedrock_hoe"), () -> Items.BEDROCK_HOE);

        CommandRegistry.register("bedrocktools76", new BedrockToolsCommand());

        registry.allRegister();

        AttackEntityEventRegistry.register(e -> {
                    if (!e.player.isCreative()) return CompatActionResult.PASS;
                    if (!e.getItemWrapperInPlayer().instanceOf(CreativeShotKillItem.class))
                        return CompatActionResult.PASS;

                    EntityUtil.kill(e.target);
                    return CompatActionResult.SUCCESS;
                }
        );

        BlockEventRegistry.ON_BREAK.register(e -> {
            Player player = e.player;
            ItemStack stack = player.getMidohraStackInHand(InteractionHand.MAIN_HAND);
            if (!stack.instanceOf(BedrockPickaxeItem.class) || player.isCreative())
                return new BlockBreakResult(e.state);

            BlockState state = e.getMidohraState();
            World world = e.getMidohraWorld();
            BlockPos pos = e.getMidohraPos();

            if (state.getBlock().equals(MCBlocks.BEDROCK))
                world.dropStackOnBlock(pos, MCBlocks.BEDROCK.asItem().createStack());
            if (state.getBlock().equals(MCBlocks.END_PORTAL_FRAME))
                world.dropStackOnBlock(pos, MCBlocks.BEDROCK.asItem().createStack());

            if (stack.getItem().equals(Items.OBSIDIAN_PICKAXE.getWrapper()) && !player.isClient())
                ItemStackUtil.damage(stack.toMinecraft(), 999, player.getServerPlayer().get());

            return new BlockBreakResult(e.state);
        });
    }

    public static CompatIdentifier _id(String name) {
        return CompatIdentifier.of(MOD_ID, name);
    }
}