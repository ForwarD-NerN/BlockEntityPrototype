package ru.nern.becraft;

import dev.crmodders.flux.registry.StableRegistries;
import dev.crmodders.flux.tags.Identifier;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nern.becraft.bed.BEUtils;
import ru.nern.becraft.bed.BlockEntityRegistries;
import ru.nern.becraft.bed.api.BlockEntityType;
import ru.nern.becraft.block.BETestBlock;
import ru.nern.becraft.block.CustomBlockEntity;
import ru.nern.becraft.block.client.CustomBlockEntityRenderer;

public class BECraft implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("BE Craft");
	public static final String MOD_ID = "becraft";
	public static final Identifier TEST_BLOCK_ID = new Identifier(MOD_ID, "be_test");
	public static final Identifier CUSTOM_BE_IDENTIFIER = new Identifier(BECraft.MOD_ID, "custom_block_entity");

	public static BlockEntityType<CustomBlockEntity> CUSTOM_BE_TYPE = new BlockEntityType<>(CUSTOM_BE_IDENTIFIER,
			CustomBlockEntity::new, TEST_BLOCK_ID.toString());

	@Override
	public void onInitialize() {
		StableRegistries.BLOCKS.register(TEST_BLOCK_ID, new BETestBlock());

		BlockEntityRegistries.BLOCK_ENTITIES.register(CUSTOM_BE_IDENTIFIER, CUSTOM_BE_TYPE);
	}

	public static void initRenderers() {
		BEUtils.renderDispatcher.registerRender(BECraft.CUSTOM_BE_TYPE, new CustomBlockEntityRenderer());
	}
}

