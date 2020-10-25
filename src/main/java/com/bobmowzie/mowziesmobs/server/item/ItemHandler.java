package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.MaskType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import java.util.List;

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
    public static final ItemSpawnEgg SPAWN_EGG = (ItemSpawnEgg) new ItemSpawnEgg(new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("spawn_egg");
    public static final ItemGrantSunsBlessing GRANT_SUNS_BLESSING = (ItemGrantSunsBlessing) new ItemGrantSunsBlessing(new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab).maxStackSize(1)).setRegistryName("grant_suns_blessing");
    public static final ItemIceCrystal ICE_CRYSTAL = (ItemIceCrystal) new ItemIceCrystal(new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab).defaultMaxDamage(ConfigHandler.TOOLS_AND_ABILITIES.ICE_CRYSTAL.durability)).setRegistryName("ice_crystal");
    public static final ItemEarthTalisman EARTH_TALISMAN = (ItemEarthTalisman) new ItemEarthTalisman(new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab).maxStackSize(1)).setRegistryName("earth_talisman");
    public static final ItemCapturedGrottol CAPTURED_GROTTOL = (ItemCapturedGrottol) new ItemCapturedGrottol(new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab).maxStackSize(1)).setRegistryName("captured_grottol");
    public static final ItemGlowingJelly GLOWING_JELLY = (ItemGlowingJelly) new ItemGlowingJelly( new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab).food(ItemGlowingJelly.GLOWING_JELLY_FOOD)).setRegistryName("glowing_jelly");
    public static final ItemNagaFang NAGA_FANG = (ItemNagaFang) new ItemNagaFang(new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("naga_fang");
    public static final ItemNagaFangDagger NAGA_FANG_DAGGER = (ItemNagaFangDagger) new ItemNagaFangDagger(new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("naga_fang_dagger");
    public static final ItemLogo LOGO = (ItemLogo) new ItemLogo(new Item.Properties().group(CreativeTabHandler.INSTANCE.creativeTab)).setRegistryName("logo");

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
                SPAWN_EGG,
                GRANT_SUNS_BLESSING,
                ICE_CRYSTAL,
                EARTH_TALISMAN,
                CAPTURED_GROTTOL,
                GLOWING_JELLY,
                NAGA_FANG,
                NAGA_FANG_DAGGER,
                LOGO
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