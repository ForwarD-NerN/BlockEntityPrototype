package ru.nern.becraft;

import dev.crmodders.cosmicquilt.api.entrypoint.ModInitializer;
import dev.crmodders.flux.api.events.GameEvents;
import dev.crmodders.flux.registry.FluxRegistries;
import dev.crmodders.flux.tags.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nern.becraft.bed.BlockEntityRegistries;
import ru.nern.becraft.bed.api.BlockEntityType;
import ru.nern.becraft.bed.utils.BEUtils;
import ru.nern.becraft.block.BETestBlock;
import ru.nern.becraft.block.CustomBlockEntity;
import ru.nern.becraft.block.client.CustomBlockEntityRenderer;

public class BECraft implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("BE Craft");
	public static final String MOD_ID = "becraft";
	public static final Identifier TEST_BLOCK_IDENTIFIER = new Identifier(MOD_ID, "be_test");

	public static BlockEntityType<CustomBlockEntity> CUSTOM_BE_TYPE = new BlockEntityType<>(new Identifier(BECraft.MOD_ID, "custom_block_entity"),
			CustomBlockEntity::new, TEST_BLOCK_IDENTIFIER.toString());

	@Override
	public void onInitialize(ModContainer mod) {
		FluxRegistries.BLOCKS.register(TEST_BLOCK_IDENTIFIER, new BETestBlock());
		BlockEntityRegistries.register(CUSTOM_BE_TYPE);

		GameEvents.ON_INIT.register(() -> {
			//You don't need to put this line in your mod
			BlockEntityRegistries.BLOCK_ENTITIES.freeze();

			//Registering custom block entity renderer
			BEUtils.renderDispatcher.registerRender(BECraft.CUSTOM_BE_TYPE, new CustomBlockEntityRenderer());
		});
	}
}

