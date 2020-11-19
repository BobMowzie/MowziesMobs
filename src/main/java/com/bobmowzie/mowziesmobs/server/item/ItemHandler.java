package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.MaskType;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = MowziesMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ItemHandler {
    private ItemHandler() {}

    public static final ItemFoliaathSeed FOLIAATH_SEED = (ItemFoliaathSeed) new ItemFoliaathSeed(new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("foliaath_seed");
//    public static final ItemTestStructure TEST_STRUCTURE = new ItemTestStructure(new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab));
    public static final ItemMobRemover MOB_REMOVER = (ItemMobRemover) new ItemMobRemover(new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("mob_remover");
    public static final ItemWroughtAxe WROUGHT_AXE = (ItemWroughtAxe) new ItemWroughtAxe(new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("wrought_axe");
    public static final ItemWroughtHelm WROUGHT_HELMET = (ItemWroughtHelm) new ItemWroughtHelm(new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("wrought_helmet");
    public static final ItemBarakoaMask BARAKOA_MASK_FURY = (ItemBarakoaMask) new ItemBarakoaMask(MaskType.FURY, new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("barakoa_mask_fury");
    public static final ItemBarakoaMask BARAKOA_MASK_FEAR = (ItemBarakoaMask) new ItemBarakoaMask(MaskType.FEAR, new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("barakoa_mask_fear");
    public static final ItemBarakoaMask BARAKOA_MASK_RAGE = (ItemBarakoaMask) new ItemBarakoaMask(MaskType.RAGE, new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("barakoa_mask_rage");
    public static final ItemBarakoaMask BARAKOA_MASK_BLISS = (ItemBarakoaMask) new ItemBarakoaMask(MaskType.BLISS, new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("barakoa_mask_bliss");
    public static final ItemBarakoaMask BARAKOA_MASK_MISERY = (ItemBarakoaMask) new ItemBarakoaMask(MaskType.MISERY, new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("barakoa_mask_misery");
    public static final ItemBarakoMask BARAKO_MASK = (ItemBarakoMask) new ItemBarakoMask(new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("barako_mask");
    public static final ItemDart DART = (ItemDart) new ItemDart(new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("dart");
    public static final ItemSpear SPEAR = (ItemSpear) new ItemSpear(new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("spear");
    public static final ItemBlowgun BLOWGUN = (ItemBlowgun) new ItemBlowgun(new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("blowgun");
    public static final ItemGrantSunsBlessing GRANT_SUNS_BLESSING = (ItemGrantSunsBlessing) new ItemGrantSunsBlessing(new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab).maxStackSize(1)).setRegistryName("grant_suns_blessing");
    public static final ItemIceCrystal ICE_CRYSTAL = (ItemIceCrystal) new ItemIceCrystal(new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab).defaultMaxDamage(ConfigHandler.TOOLS_AND_ABILITIES.ICE_CRYSTAL.durability)).setRegistryName("ice_crystal");
    public static final ItemEarthTalisman EARTH_TALISMAN = (ItemEarthTalisman) new ItemEarthTalisman(new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab).maxStackSize(1)).setRegistryName("earth_talisman");
    public static final ItemCapturedGrottol CAPTURED_GROTTOL = (ItemCapturedGrottol) new ItemCapturedGrottol(new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab).maxStackSize(1)).setRegistryName("captured_grottol");
    public static final ItemGlowingJelly GLOWING_JELLY = (ItemGlowingJelly) new ItemGlowingJelly( new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab).food(ItemGlowingJelly.GLOWING_JELLY_FOOD)).setRegistryName("glowing_jelly");
    public static final ItemNagaFang NAGA_FANG = (ItemNagaFang) new ItemNagaFang(new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("naga_fang");
    public static final ItemNagaFangDagger NAGA_FANG_DAGGER = (ItemNagaFangDagger) new ItemNagaFangDagger(new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("naga_fang_dagger");
    public static final ItemLogo LOGO = (ItemLogo) new ItemLogo(new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("logo");

    public static final SpawnEggItem FOLIAATH_SPAWN_EGG = (SpawnEggItem) new SpawnEggItem(EntityHandler.FOLIAATH, 0x47CC3B, 0xC03BCC, new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("foliaath_spawn_egg");
    public static final SpawnEggItem WROUGHTNAUT_SPAWN_EGG = (SpawnEggItem) new SpawnEggItem(EntityHandler.WROUGHTNAUT, 0x8C8C8C, 0xFFFFFF, new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("wroughtnaut_spawn_egg");
    public static final SpawnEggItem BARAKOA_SPAWN_EGG = (SpawnEggItem) new SpawnEggItem(EntityHandler.BARAKOAYA, 0xBA6656, 0xFAFA78, new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("barakoa_spawn_egg");
    public static final SpawnEggItem BARAKOANA_SPAWN_EGG = (SpawnEggItem) new SpawnEggItem(EntityHandler.BARAKOANA, 0xBA6656, 0xFAFA78, new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("barakoana_spawn_egg");
    public static final SpawnEggItem BARAKO_SPAWN_EGG = (SpawnEggItem) new SpawnEggItem(EntityHandler.BARAKO, 0xBA6656, 0xFFFF00, new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("barako_spawn_egg");
    public static final SpawnEggItem FROSTMAW_SPAWN_EGG = (SpawnEggItem) new SpawnEggItem(EntityHandler.FROSTMAW, 0xf7faff, 0xafcdff, new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("frostmaw_spawn_egg");
    public static final SpawnEggItem GROTTOL_SPAWN_EGG = (SpawnEggItem) new SpawnEggItem(EntityHandler.GROTTOL, 0x777777, 0xbce0ff, new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("grottol_spawn_egg");
    public static final SpawnEggItem LANTERN_SPAWN_EGG = (SpawnEggItem) new SpawnEggItem(EntityHandler.LANTERN, 0x6dea00, 0x235a10, new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("lantern_spawn_egg");
    public static final SpawnEggItem NAGA_SPAWN_EGG = (SpawnEggItem) new SpawnEggItem(EntityHandler.NAGA, 0x154850, 0x8dd759, new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("naga_spawn_egg");

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                FOLIAATH_SEED,
                //TEST_STRUCTURE,
                MOB_REMOVER,
                WROUGHT_AXE,
                WROUGHT_HELMET,
                BARAKO_MASK,
                BARAKOA_MASK_FURY,
                BARAKOA_MASK_BLISS,
                BARAKOA_MASK_FEAR,
                BARAKOA_MASK_MISERY,
                BARAKOA_MASK_RAGE,
                DART,
                SPEAR,
                BLOWGUN,
                GRANT_SUNS_BLESSING,
                ICE_CRYSTAL,
                EARTH_TALISMAN,
                CAPTURED_GROTTOL,
                GLOWING_JELLY,
                NAGA_FANG,
                NAGA_FANG_DAGGER,
                LOGO,
                FOLIAATH_SPAWN_EGG,
                WROUGHTNAUT_SPAWN_EGG,
                BARAKOA_SPAWN_EGG,
                BARAKOANA_SPAWN_EGG,
                BARAKO_SPAWN_EGG,
                FROSTMAW_SPAWN_EGG,
                GROTTOL_SPAWN_EGG,
                LANTERN_SPAWN_EGG,
                NAGA_SPAWN_EGG
                //new BlockItem(BlockHandler.PAINTED_ACACIA, new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName(BlockHandler.PAINTED_ACACIA.getRegistryName()),
                //new BlockItem(BlockHandler.PAINTED_ACACIA_SLAB, new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName(BlockHandler.PAINTED_ACACIA_SLAB.getRegistryName())
        );
    }

    /*public static void addItemText(Item item, List<String> lines) {
        String keyStart = item.getTranslationKey() + ".text.";
        for (int line = 0;; line++) {
            String key = keyStart + line;
            if (I18n.canTranslate(key)) {
                lines.add(I18n.translateToLocal(key));
            } else {
                break;
            }
        }
    }*/ // TODO
}