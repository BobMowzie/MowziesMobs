package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemHandler {

    public static Style TOOLTIP_STYLE = Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.GRAY));

    public static final DeferredRegister<Item> REG = DeferredRegister.create(ForgeRegistries.ITEMS, MowziesMobs.MODID);

    public static final RegistryObject<ItemFoliaathSeed> FOLIAATH_SEED = REG.register("foliaath_seed", () -> new ItemFoliaathSeed(new Item.Properties()));
    public static final RegistryObject<ItemMobRemover> MOB_REMOVER = REG.register("mob_remover", () -> new ItemMobRemover(new Item.Properties()));
    public static final RegistryObject<ItemWroughtAxe> WROUGHT_AXE = REG.register("wrought_axe", () -> new ItemWroughtAxe(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ItemWroughtHelm> WROUGHT_HELMET = REG.register("wrought_helmet", () -> new ItemWroughtHelm(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ItemUmvuthanaMask> UMVUTHANA_MASK_FURY = REG.register("umvuthana_mask_fury", () -> new ItemUmvuthanaMask(MaskType.FURY, new Item.Properties()));
    public static final RegistryObject<ItemUmvuthanaMask> UMVUTHANA_MASK_FEAR = REG.register("umvuthana_mask_fear", () -> new ItemUmvuthanaMask(MaskType.FEAR, new Item.Properties()));
    public static final RegistryObject<ItemUmvuthanaMask> UMVUTHANA_MASK_RAGE = REG.register("umvuthana_mask_rage", () -> new ItemUmvuthanaMask(MaskType.RAGE, new Item.Properties()));
    public static final RegistryObject<ItemUmvuthanaMask> UMVUTHANA_MASK_BLISS = REG.register("umvuthana_mask_bliss", () -> new ItemUmvuthanaMask(MaskType.BLISS, new Item.Properties()));
    public static final RegistryObject<ItemUmvuthanaMask> UMVUTHANA_MASK_MISERY = REG.register("umvuthana_mask_misery", () -> new ItemUmvuthanaMask(MaskType.MISERY, new Item.Properties()));
    public static final RegistryObject<ItemUmvuthanaMask> UMVUTHANA_MASK_FAITH = REG.register("umvuthana_mask_faith", () -> new ItemUmvuthanaMask(MaskType.FAITH, new Item.Properties()));
    public static final RegistryObject<ItemSolVisage> SOL_VISAGE = REG.register("sol_visage", () -> new ItemSolVisage(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<ItemDart> DART = REG.register("dart", () -> new ItemDart(new Item.Properties()));
    public static final RegistryObject<ItemSpear> SPEAR = REG.register("spear", () -> new ItemSpear(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<ItemBlowgun> BLOWGUN = REG.register("blowgun", () -> new ItemBlowgun(new Item.Properties().stacksTo(1).durability(300)));
    public static final RegistryObject<ItemGrantSunsBlessing> GRANT_SUNS_BLESSING = REG.register("grant_suns_blessing", () -> new ItemGrantSunsBlessing(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final RegistryObject<ItemIceCrystal> ICE_CRYSTAL = REG.register("ice_crystal", () -> new ItemIceCrystal(new Item.Properties().defaultDurability(ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ICE_CRYSTAL.durabilityValue).rarity(Rarity.RARE)));
    public static final RegistryObject<ItemEarthTalisman> EARTH_TALISMAN = REG.register("earth_talisman", () -> new ItemEarthTalisman(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final RegistryObject<ItemCapturedGrottol> CAPTURED_GROTTOL = REG.register("captured_grottol", () -> new ItemCapturedGrottol(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<ItemGlowingJelly> GLOWING_JELLY = REG.register("glowing_jelly", () -> new ItemGlowingJelly(new Item.Properties().food(ItemGlowingJelly.GLOWING_JELLY_FOOD)));
    public static final RegistryObject<ItemNagaFang> NAGA_FANG = REG.register("naga_fang", () -> new ItemNagaFang(new Item.Properties()));
    public static final RegistryObject<ItemNagaFangDagger> NAGA_FANG_DAGGER = REG.register("naga_fang_dagger", () -> new ItemNagaFangDagger(new Item.Properties()));
    public static final RegistryObject<ItemEarthboreGauntlet> EARTHBORE_GAUNTLET = REG.register("earthbore_gauntlet", () -> new ItemEarthboreGauntlet(new Item.Properties().defaultDurability(ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHBORE_GAUNTLET.durabilityValue).rarity(Rarity.RARE)));
    public static final RegistryObject<ItemSculptorStaff> SCULPTOR_STAFF = REG.register("sculptor_staff", () -> new ItemSculptorStaff(new Item.Properties().defaultDurability(1000).rarity(Rarity.RARE)));
    public static final RegistryObject<ItemSandRake> SAND_RAKE = REG.register("sand_rake", () -> new ItemSandRake(new Item.Properties().defaultDurability(64)));
    public static final RegistryObject<Item> LOGO = REG.register("logo", () -> new Item(new Item.Properties()));
    public static final RegistryObject<RecordItem> PETIOLE_MUSIC_DISC = REG.register("music_disc_petiole", () -> new RecordItem(14, MMSounds.MUSIC_PETIOLE, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 2800));

    public static final RegistryObject<ForgeSpawnEggItem> FOLIAATH_SPAWN_EGG = REG.register("foliaath_spawn_egg", () -> new ForgeSpawnEggItem(EntityHandler.FOLIAATH, 0x47CC3B, 0xC03BCC, new Item.Properties()));
    public static final RegistryObject<ForgeSpawnEggItem> WROUGHTNAUT_SPAWN_EGG = REG.register("wroughtnaut_spawn_egg", () -> new ForgeSpawnEggItem(EntityHandler.WROUGHTNAUT, 0x8C8C8C, 0xFFFFFF, new Item.Properties()));
    public static final RegistryObject<ForgeSpawnEggItem> UMVUTHANA_SPAWN_EGG = REG.register("umvuthana_spawn_egg", () -> new ForgeSpawnEggItem(EntityHandler.UMVUTHANA_MINION, 0xba5f1e, 0x3a2f2f, new Item.Properties()));
    public static final RegistryObject<ForgeSpawnEggItem> UMVUTHANA_RAPTOR_SPAWN_EGG = REG.register("umvuthana_raptor_spawn_egg", () -> new ForgeSpawnEggItem(EntityHandler.UMVUTHANA_RAPTOR, 0xba5f1e, 0xf6f2f1, new Item.Properties()));
    public static final RegistryObject<ForgeSpawnEggItem> UMVUTHANA_CRANE_SPAWN_EGG = REG.register("umvuthana_crane_spawn_egg", () -> new ForgeSpawnEggItem(EntityHandler.UMVUTHANA_CRANE, 0xba5f1e, 0xfddc76, new Item.Properties()));
    public static final RegistryObject<ForgeSpawnEggItem> UMVUTHI_SPAWN_EGG = REG.register("umvuthi_spawn_egg", () -> new ForgeSpawnEggItem(EntityHandler.UMVUTHI, 0xf6f2f1, 0xba5f1e, new Item.Properties()));
    public static final RegistryObject<ForgeSpawnEggItem> FROSTMAW_SPAWN_EGG = REG.register("frostmaw_spawn_egg", () -> new ForgeSpawnEggItem(EntityHandler.FROSTMAW, 0xf7faff, 0xafcdff, new Item.Properties()));
    public static final RegistryObject<ForgeSpawnEggItem> GROTTOL_SPAWN_EGG = REG.register("grottol_spawn_egg", () -> new ForgeSpawnEggItem(EntityHandler.GROTTOL, 0x777777, 0xbce0ff, new Item.Properties()));
    public static final RegistryObject<ForgeSpawnEggItem> LANTERN_SPAWN_EGG = REG.register("lantern_spawn_egg", () -> new ForgeSpawnEggItem(EntityHandler.LANTERN, 0x6dea00, 0x235a10, new Item.Properties()));
    public static final RegistryObject<ForgeSpawnEggItem> NAGA_SPAWN_EGG = REG.register("naga_spawn_egg", () -> new ForgeSpawnEggItem(EntityHandler.NAGA, 0x154850, 0x8dd759, new Item.Properties()));
    public static final RegistryObject<ForgeSpawnEggItem> SCULPTOR_SPAWN_EGG = REG.register("sculptor_spawn_egg", () -> new ForgeSpawnEggItem(EntityHandler.SCULPTOR, 0xc4a137, 0xfff5e7, new Item.Properties()));

    public static void initializeAttributes() {
        WROUGHT_AXE.get().getAttributesFromConfig();
        WROUGHT_HELMET.get().getAttributesFromConfig();
        UMVUTHANA_MASK_FURY.get().getAttributesFromConfig();
        UMVUTHANA_MASK_FEAR.get().getAttributesFromConfig();
        UMVUTHANA_MASK_RAGE.get().getAttributesFromConfig();
        UMVUTHANA_MASK_BLISS.get().getAttributesFromConfig();
        UMVUTHANA_MASK_MISERY.get().getAttributesFromConfig();
        UMVUTHANA_MASK_FAITH.get().getAttributesFromConfig();
        SOL_VISAGE.get().getAttributesFromConfig();
        SPEAR.get().getAttributesFromConfig();
        NAGA_FANG_DAGGER.get().getAttributesFromConfig();
        EARTHBORE_GAUNTLET.get().getAttributesFromConfig();
    }

    public static void initializeDispenserBehaviors() {
        DispenserBlock.registerBehavior(DART.get(), new AbstractProjectileDispenseBehavior() {
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