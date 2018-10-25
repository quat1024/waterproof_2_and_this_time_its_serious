package quaternary.waterproof2;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Mod(modid = Waterproof2.MODID, name = Waterproof2.NAME, version = Waterproof2.VERSION)
@Mod.EventBusSubscriber
public class Waterproof2 {
	public static final String MODID = "waterproof2";
	public static final String NAME = "Waterproof 2: And This Time It's Serious";
	public static final String VERSION = "GRADLE:VERSION";
	
	private static final Collection<Block> waterproofBlocks = new ArrayList<>();
	
	public static boolean canFlowInto_hook(boolean vanillaResult, IBlockState state) {
		//This hook injects right before BlockDynamicLiquid#canFlowInto returns.
		//Of course I don't want to totally stomp the vanilla handling.
		return vanillaResult && !waterproofBlocks.contains(state.getBlock());
	}
	
	private static void refreshConfig() {
		waterproofBlocks.clear();
		for(String id : ModConfig.waterproofBlocks) {
			Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(id));
			if(b != null) waterproofBlocks.add(b);
		}
	}
	
	@SubscribeEvent
	public static void configChange(ConfigChangedEvent.OnConfigChangedEvent e) {
		if(e.getModID().equals(MODID)) {
			ConfigManager.sync(MODID, Config.Type.INSTANCE);
			refreshConfig();
		}
	}
	
	@Mod.EventHandler
	public static void postinit(FMLPostInitializationEvent e) {
		refreshConfig();
		
		//Forge fluids use a different system that's easier to add blocks to.
		//It takes a little reflection, though.
		Collection<Fluid> fluids = FluidRegistry.getRegisteredFluids().values();
		for(Fluid f : fluids) {
			Block b = f.getBlock();
			if(b instanceof BlockFluidBase) {
				BlockFluidBase fluidBlock = (BlockFluidBase) b;
				Map<Block, Boolean> displacements = ReflectionHelper.getPrivateValue(BlockFluidBase.class, fluidBlock, "displacements");
				waterproofBlocks.forEach(waterproof -> displacements.put(waterproof, false));
			}
		}
	}
	
	@Config(modid = MODID)
	public static class ModConfig {
		@Config.Name("WaterproofBlocks")
		@Config.Comment({
						"Block IDs that liquids will not be able to flow into.",
						"Note that some modded fluids will require a game restart for changes here to take effect!"
		})
		public static String[] waterproofBlocks = new String[] {
						"minecraft:redstone_wire",
						"minecraft:redstone_torch",
						"minecraft:unlit_redstone_torch",
						"minecraft:powered_repeater",
						"minecraft:unpowered_repeater",
						"minecraft:powered_comparator",
						"minecraft:unpowered_comparator",
						"minecraft:rail",
						"minecraft:golden_rail",
						"minecraft:detector_rail",
						"minecraft:activator_rail",
						//below this line are blocks that didn't exist by default in vazgoo waterproof
						//but i wanted them :v
						"minecraft:stone_button",
						"minecraft:wooden_button",
						"minecraft:lever"
		};
	}
}
