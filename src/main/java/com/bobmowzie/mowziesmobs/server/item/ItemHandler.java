package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityDart;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.MaskType;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.ProjectileDispenseBehavior;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;


@Mod.EventBusSubscriber(modid = MowziesMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(MowziesMobs.MODID)
public final class ItemHandler {
    private ItemHandler() {}

    public static final ItemFoliaathSeed FOLIAATH_SEED = null;
    public static final ItemMobRemover MOB_REMOVER = null;
    public static final ItemWroughtAxe WROUGHT_AXE = null;
    public static final ItemWroughtHelm WROUGHT_HELMET = null;
    public static final ItemBarakoaMask BARAKOA_MASK_FURY = null;
    public static final ItemBarakoaMask BARAKOA_MASK_FEAR = null;
    public static final ItemBarakoaMask BARAKOA_MASK_RAGE = null;
    public static final ItemBarakoaMask BARAKOA_MASK_BLISS = null;
    public static final ItemBarakoaMask BARAKOA_MASK_MISERY = null;
    public static final ItemBarakoaMask BARAKOA_MASK_FAITH = null;
    public static final ItemBarakoMask BARAKO_MASK = null;
    public static final ItemDart DART = null;
    public static final ItemSpear SPEAR = null;
    public static final ItemSunblockStaff SUNBLOCK_STAFF = null;
    public static final ItemBlowgun BLOWGUN = null;
    public static final ItemGrantSunsBlessing GRANT_SUNS_BLESSING = null;
    public static final ItemIceCrystal ICE_CRYSTAL = null;
    public static final ItemEarthTalisman EARTH_TALISMAN = null;
    public static final ItemCapturedGrottol CAPTURED_GROTTOL = null;
    public static final ItemGlowingJelly GLOWING_JELLY = null;
    public static final ItemNagaFang NAGA_FANG = null;
    public static final ItemNagaFangDagger NAGA_FANG_DAGGER = null;
    public static final ItemLogo LOGO = null;
    public static final RecordItem PETIOLE_MUSIC_DISC = null;

    public static final ForgeSpawnEggItem FOLIAATH_SPAWN_EGG = null;
    public static final ForgeSpawnEggItem WROUGHTNAUT_SPAWN_EGG = null;
    public static final ForgeSpawnEggItem BARAKOA_SPAWN_EGG = null;
    public static final ForgeSpawnEggItem BARAKOANA_SPAWN_EGG = null;
    public static final ForgeSpawnEggItem BARAKOA_SUNBLOCKER_SPAWN_EGG = null;
    public static final ForgeSpawnEggItem BARAKO_SPAWN_EGG = null;
    public static final ForgeSpawnEggItem FROSTMAW_SPAWN_EGG = null;
    public static final ForgeSpawnEggItem GROTTOL_SPAWN_EGG = null;
    public static final ForgeSpawnEggItem LANTERN_SPAWN_EGG = null;
    public static final ForgeSpawnEggItem NAGA_SPAWN_EGG = null;
    public static final ForgeSpawnEggItem SCULPTOR_SPAWN_EGG = null;

    private static final int BARAKOA_GREEN_COLOR = 0x748C47;
    private static final int BARAKOA_PINK_COLOR = 0xBA6656;

    public static Style TOOLTIP_STYLE = Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.GRAY));

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
            new ItemFoliaathSeed(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("foliaath_seed"),
            new ItemMobRemover(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("mob_remover"),
            new ItemWroughtAxe(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab).rarity(Rarity.UNCOMMON)).setRegistryName("wrought_axe"),
            new ItemWroughtHelm(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab).rarity(Rarity.UNCOMMON)).setRegistryName("wrought_helmet"),
            new ItemBarakoaMask(MaskType.FURY, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("barakoa_mask_fury"),
            new ItemBarakoaMask(MaskType.FEAR, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("barakoa_mask_fear"),
            new ItemBarakoaMask(MaskType.RAGE, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("barakoa_mask_rage"),
            new ItemBarakoaMask(MaskType.BLISS, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("barakoa_mask_bliss"),
            new ItemBarakoaMask(MaskType.MISERY, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("barakoa_mask_misery"),
            new ItemBarakoaMask(MaskType.FAITH, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("barakoa_mask_faith"),
            new ItemBarakoMask(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab).rarity(Rarity.RARE)).setRegistryName("barako_mask"),
            new ItemDart(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("dart"),
            new ItemSpear(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab).stacksTo(1)).setRegistryName("spear"),
            new ItemSunblockStaff(new Item.Properties().stacksTo(1)).setRegistryName("sunblock_staff"),
            new ItemBlowgun(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab).stacksTo(1).maxDamage(300)).setRegistryName("blowgun"),
            new ItemGrantSunsBlessing(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab).stacksTo(1).rarity(Rarity.EPIC)).setRegistryName("grant_suns_blessing"),
            new ItemIceCrystal(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab).defaultMaxDamage(ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ICE_CRYSTAL.durability.get()).rarity(Rarity.RARE)).setRegistryName("ice_crystal"),
            new ItemEarthTalisman(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab).stacksTo(1).rarity(Rarity.EPIC)).setRegistryName("earth_talisman"),
            new ItemCapturedGrottol(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab).stacksTo(1)).setRegistryName("captured_grottol"),
            new ItemGlowingJelly( new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab).food(ItemGlowingJelly.GLOWING_JELLY_FOOD)).setRegistryName("glowing_jelly"),
            new ItemNagaFang(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("naga_fang"),
            new ItemNagaFangDagger(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("naga_fang_dagger"),
            new ItemLogo(new Item.Properties()).setRegistryName("logo"),
            new RecordItem(14, MMSounds.MUSIC_PETIOLE, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab).stacksTo(1).rarity(Rarity.RARE)).setRegistryName("music_disc_petiole"),
    
            new ForgeSpawnEggItem(EntityHandler.FOLIAATH, 0x47CC3B, 0xC03BCC, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("foliaath_spawn_egg"),
            new ForgeSpawnEggItem(EntityHandler.WROUGHTNAUT, 0x8C8C8C, 0xFFFFFF, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("wroughtnaut_spawn_egg"),
            new ForgeSpawnEggItem(EntityHandler.BARAKOA_VILLAGER, BARAKOA_GREEN_COLOR, 0xdbdbdb, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("barakoa_spawn_egg"),
            new ForgeSpawnEggItem(EntityHandler.BARAKOANA, BARAKOA_GREEN_COLOR, 0xFAFA78, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("barakoana_spawn_egg"),
            new ForgeSpawnEggItem(EntityHandler.BARAKOAYA, BARAKOA_GREEN_COLOR, 0xff8271, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("barakoa_sunblocker_spawn_egg"),
            new ForgeSpawnEggItem(EntityHandler.BARAKO, BARAKOA_GREEN_COLOR, 0xFFFF2F, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("barako_spawn_egg"),
            new ForgeSpawnEggItem(EntityHandler.FROSTMAW, 0xf7faff, 0xafcdff, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("frostmaw_spawn_egg"),
            new ForgeSpawnEggItem(EntityHandler.GROTTOL, 0x777777, 0xbce0ff, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("grottol_spawn_egg"),
            new ForgeSpawnEggItem(EntityHandler.LANTERN, 0x6dea00, 0x235a10, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("lantern_spawn_egg"),
            new ForgeSpawnEggItem(EntityHandler.NAGA, 0x154850, 0x8dd759, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("naga_spawn_egg"),
            new ForgeSpawnEggItem(EntityHandler.SCULPTOR, 0x154850, 0x8dd759, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("sculptor_spawn_egg"),

            new BlockItem(BlockHandler.PAINTED_ACACIA.get(), new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName(BlockHandler.PAINTED_ACACIA.get().getRegistryName()),
            new BlockItem(BlockHandler.PAINTED_ACACIA_SLAB.get(), new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName(BlockHandler.PAINTED_ACACIA_SLAB.get().getRegistryName()),
            new BlockItem(BlockHandler.THATCH.get(), new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName(BlockHandler.THATCH.get().getRegistryName())
        );
    }

    public static void initializeAttributes() {
        WROUGHT_AXE.getAttributesFromConfig();
        WROUGHT_HELMET.getAttributesFromConfig();
        BARAKOA_MASK_FURY.getAttributesFromConfig();
        BARAKOA_MASK_FEAR.getAttributesFromConfig();
        BARAKOA_MASK_RAGE.getAttributesFromConfig();
        BARAKOA_MASK_BLISS.getAttributesFromConfig();
        BARAKOA_MASK_MISERY.getAttributesFromConfig();
        BARAKOA_MASK_FAITH.getAttributesFromConfig();
        BARAKO_MASK.getAttributesFromConfig();
        SPEAR.getAttributesFromConfig();
        NAGA_FANG_DAGGER.getAttributesFromConfig();

        int barakoaColor = ConfigHandler.CLIENT.oldBarakoaTextures.get() ? BARAKOA_PINK_COLOR : BARAKOA_GREEN_COLOR;
        BARAKO_SPAWN_EGG.primaryColor = barakoaColor;
        BARAKOA_SPAWN_EGG.primaryColor = barakoaColor;
        BARAKOANA_SPAWN_EGG.primaryColor = barakoaColor;
        BARAKOA_SUNBLOCKER_SPAWN_EGG.primaryColor = barakoaColor;
    }

    public static void initializeDispenserBehaviors() {
        // Copied from IDispenseItemBehavior
        DefaultDispenseItemBehavior defaultdispenseitembehavior = new DefaultDispenseItemBehavior() {
            /**
             * Dispense the specified stack, play the dispense sound and spawn particles.
             */
            public ItemStack dispenseStack(BlockSource source, ItemStack stack) {
                Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
                EntityType<?> entitytype = ((ForgeSpawnEggItem)stack.getItem()).getType(stack.getTag());
                entitytype.spawn(source.getLevel(), stack, (Player)null, source.getPos().offset(direction.getNormal()), MobSpawnType.DISPENSER, direction != Direction.UP, false);
                stack.shrink(1);
                return stack;
            }
        };
        ForgeSpawnEggItem[] spawnEggItems = new ForgeSpawnEggItem[] {
                FOLIAATH_SPAWN_EGG,
                WROUGHTNAUT_SPAWN_EGG,
                BARAKOA_SPAWN_EGG,
                BARAKOANA_SPAWN_EGG,
                BARAKOA_SUNBLOCKER_SPAWN_EGG,
                BARAKO_SPAWN_EGG,
                FROSTMAW_SPAWN_EGG,
                GROTTOL_SPAWN_EGG,
                LANTERN_SPAWN_EGG,
                NAGA_SPAWN_EGG,
                SCULPTOR_SPAWN_EGG
        };
        for(ForgeSpawnEggItem spawneggitem : spawnEggItems) {
            DispenserBlock.registerBehavior(spawneggitem, defaultdispenseitembehavior);
        }
        DispenserBlock.registerBehavior(DART, new AbstractProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
                EntityDart dartentity = new EntityDart(EntityHandler.DART, worldIn, position.x(), position.y(), position.z());
                dartentity.pickup = AbstractArrow.Pickup.ALLOWED;
                return dartentity;
            }
        });
    }
}