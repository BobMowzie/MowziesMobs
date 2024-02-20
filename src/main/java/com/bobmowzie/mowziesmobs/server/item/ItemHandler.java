package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityDart;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.MaskType;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ItemHandler {
    private ItemHandler() {}

    public static final ItemFoliaathSeed FOLIAATH_SEED = null;
    public static final ItemMobRemover MOB_REMOVER = null;
    public static final ItemWroughtAxe WROUGHT_AXE = null;
    public static final ItemWroughtHelm WROUGHT_HELMET = null;
    public static final ItemUmvuthanaMask UMVUTHANA_MASK_FURY = null;
    public static final ItemUmvuthanaMask UMVUTHANA_MASK_FEAR = null;
    public static final ItemUmvuthanaMask UMVUTHANA_MASK_RAGE = null;
    public static final ItemUmvuthanaMask UMVUTHANA_MASK_BLISS = null;
    public static final ItemUmvuthanaMask UMVUTHANA_MASK_MISERY = null;
    public static final ItemUmvuthanaMask UMVUTHANA_MASK_FAITH = null;
    public static final ItemSolVisage SOL_VISAGE = null;
    public static final ItemDart DART = null;
    public static final ItemSpear SPEAR = null;
    public static final ItemBlowgun BLOWGUN = null;
    public static final ItemGrantSunsBlessing GRANT_SUNS_BLESSING = null;
    public static final ItemIceCrystal ICE_CRYSTAL = null;
    public static final ItemEarthTalisman EARTH_TALISMAN = null;
    public static final ItemCapturedGrottol CAPTURED_GROTTOL = null;
    public static final ItemGlowingJelly GLOWING_JELLY = null;
    public static final ItemNagaFang NAGA_FANG = null;
    public static final ItemNagaFangDagger NAGA_FANG_DAGGER = null;
    public static final ItemEarthboreGauntlet EARTHBORE_GAUNTLET = null;
//    public static final ItemSculptorStaff SCULPTOR_STAFF = null;
    public static final Item LOGO = null;
    public static final RecordItem PETIOLE_MUSIC_DISC = null;

    public static final ForgeSpawnEggItem FOLIAATH_SPAWN_EGG = null;
    public static final ForgeSpawnEggItem WROUGHTNAUT_SPAWN_EGG = null;
    public static final ForgeSpawnEggItem UMVUTHANA_SPAWN_EGG = null;
    public static final ForgeSpawnEggItem UMVUTHANA_RAPTOR_SPAWN_EGG = null;
    public static final ForgeSpawnEggItem UMVUTHANA_CRANE_SPAWN_EGG = null;
    public static final ForgeSpawnEggItem UMVUTHI_SPAWN_EGG = null;
    public static final ForgeSpawnEggItem FROSTMAW_SPAWN_EGG = null;
    public static final ForgeSpawnEggItem GROTTOL_SPAWN_EGG = null;
    public static final ForgeSpawnEggItem LANTERN_SPAWN_EGG = null;
    public static final ForgeSpawnEggItem NAGA_SPAWN_EGG = null;
//    public static final ForgeSpawnEggItem SCULPTOR_SPAWN_EGG = null;

    public static Style TOOLTIP_STYLE = Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.GRAY));

    @SubscribeEvent
    public static void register(RegisterEvent event) {
    	event.register(ForgeRegistries.Keys.ITEMS,
    			helper -> {
    	            helper.register("foliaath_seed", new ItemFoliaathSeed(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
    	            helper.register("mob_remover", new ItemMobRemover(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
    	            helper.register("wrought_axe", new ItemWroughtAxe(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab).rarity(Rarity.UNCOMMON)));
    	            helper.register("wrought_helmet", new ItemWroughtHelm(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab).rarity(Rarity.UNCOMMON)));
    	            helper.register("umvuthana_mask_fury", new ItemUmvuthanaMask(MaskType.FURY, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
    	            helper.register("umvuthana_mask_fear", new ItemUmvuthanaMask(MaskType.FEAR, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
    	            helper.register("umvuthana_mask_rage", new ItemUmvuthanaMask(MaskType.RAGE, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
    	            helper.register("umvuthana_mask_bliss", new ItemUmvuthanaMask(MaskType.BLISS, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
    	            helper.register("umvuthana_mask_misery", new ItemUmvuthanaMask(MaskType.MISERY, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
    	            helper.register("umvuthana_mask_faith", new ItemUmvuthanaMask(MaskType.FAITH, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
    	            helper.register("sol_visage", new ItemSolVisage(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab).rarity(Rarity.RARE)));
    	            helper.register("dart", new ItemDart(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
    	            helper.register("spear", new ItemSpear(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab).stacksTo(1)));
    	            helper.register("blowgun", new ItemBlowgun(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab).stacksTo(1).durability(300)));
    	            helper.register("grant_suns_blessing", new ItemGrantSunsBlessing(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab).stacksTo(1).rarity(Rarity.EPIC)));
    	            helper.register("ice_crystal", new ItemIceCrystal(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab).defaultDurability(ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ICE_CRYSTAL.durability.get()).rarity(Rarity.RARE)));
    	            helper.register("earth_talisman", new ItemEarthTalisman(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab).stacksTo(1).rarity(Rarity.EPIC)));
    	            helper.register("captured_grottol", new ItemCapturedGrottol(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab).stacksTo(1)));
    	            helper.register("glowing_jelly", new ItemGlowingJelly( new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab).food(ItemGlowingJelly.GLOWING_JELLY_FOOD)));
    	            helper.register("naga_fang", new ItemNagaFang(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
    	            helper.register("naga_fang_dagger", new ItemNagaFangDagger(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
    	            helper.register("earthbore_gauntlet", new ItemEarthboreGauntlet(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab).defaultDurability(ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHBORE_GAUNTLET.durability.get()).rarity(Rarity.RARE)));
//    	            helper.register("sculptor_staff", new ItemSculptorStaff(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab).defaultDurability(1000).rarity(Rarity.RARE)));
//    	            helper.register("sand_rake", new ItemSandRake(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab).defaultDurability(64)));
    	            helper.register("logo", new Item(new Item.Properties()));
    	            helper.register("music_disc_petiole", new RecordItem(14, MMSounds.MUSIC_PETIOLE, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab).stacksTo(1).rarity(Rarity.RARE), 0));
    	    
    	            helper.register("foliaath_spawn_egg", new ForgeSpawnEggItem(EntityHandler.FOLIAATH, 0x47CC3B, 0xC03BCC, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
    	            helper.register("wroughtnaut_spawn_egg", new ForgeSpawnEggItem(EntityHandler.WROUGHTNAUT, 0x8C8C8C, 0xFFFFFF, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
    	            helper.register("umvuthana_spawn_egg", new ForgeSpawnEggItem(EntityHandler.UMVUTHANA_MINION, 0xba5f1e, 0x3a2f2f, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
    	            helper.register("umvuthana_raptor_spawn_egg", new ForgeSpawnEggItem(EntityHandler.UMVUTHANA_RAPTOR, 0xba5f1e, 0xf6f2f1, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
    	            helper.register("umvuthana_crane_spawn_egg", new ForgeSpawnEggItem(EntityHandler.UMVUTHANA_CRANE, 0xba5f1e, 0xfddc76, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
    	            helper.register("umvuthi_spawn_egg", new ForgeSpawnEggItem(EntityHandler.UMVUTHI, 0xf6f2f1, 0xba5f1e, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
    	            helper.register("frostmaw_spawn_egg", new ForgeSpawnEggItem(EntityHandler.FROSTMAW, 0xf7faff, 0xafcdff, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
    	            helper.register("grottol_spawn_egg", new ForgeSpawnEggItem(EntityHandler.GROTTOL, 0x777777, 0xbce0ff, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
    	            helper.register("lantern_spawn_egg", new ForgeSpawnEggItem(EntityHandler.LANTERN, 0x6dea00, 0x235a10, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
    	            helper.register("naga_spawn_egg", new ForgeSpawnEggItem(EntityHandler.NAGA, 0x154850, 0x8dd759, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
//    	            helper.register("sculptor_spawn_egg", new ForgeSpawnEggItem(EntityHandler.SCULPTOR, 0xc4a137, 0xfff5e7, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));

    	            helper.register("painted_acacia", new BlockItem(BlockHandler.PAINTED_ACACIA.get(), new Item.Properties()));
    	            helper.register("painted_acacia_slab", new BlockItem(BlockHandler.PAINTED_ACACIA_SLAB.get(), new Item.Properties()));
    	            helper.register("thatch_block", new BlockItem(BlockHandler.THATCH.get(), new Item.Properties()));
//    	            helper.register("gong", new BlockItem(BlockHandler.GONG.get(), new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
//    	            helper.register("raked_sand", new BlockItem(BlockHandler.RAKED_SAND.get(), new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
    	            helper.register("clawed_log", new BlockItem(BlockHandler.CLAWED_LOG.get(), new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
    			});
    }

    public static void initializeAttributes() {
        WROUGHT_AXE.getAttributesFromConfig();
        WROUGHT_HELMET.getAttributesFromConfig();
        UMVUTHANA_MASK_FURY.getAttributesFromConfig();
        UMVUTHANA_MASK_FEAR.getAttributesFromConfig();
        UMVUTHANA_MASK_RAGE.getAttributesFromConfig();
        UMVUTHANA_MASK_BLISS.getAttributesFromConfig();
        UMVUTHANA_MASK_MISERY.getAttributesFromConfig();
        UMVUTHANA_MASK_FAITH.getAttributesFromConfig();
        SOL_VISAGE.getAttributesFromConfig();
        SPEAR.getAttributesFromConfig();
        NAGA_FANG_DAGGER.getAttributesFromConfig();
        EARTHBORE_GAUNTLET.getAttributesFromConfig();
    }

    public static void initializeDispenserBehaviors() {
        DispenserBlock.registerBehavior(DART, new AbstractProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
                EntityDart dartentity = new EntityDart(EntityHandler.DART.get(), worldIn, position.x(), position.y(), position.z());
                dartentity.pickup = AbstractArrow.Pickup.ALLOWED;
                return dartentity;
            }
        });
    }
}