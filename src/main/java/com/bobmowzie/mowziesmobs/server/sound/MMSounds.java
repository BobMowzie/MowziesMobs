package com.bobmowzie.mowziesmobs.server.sound;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID)
public final class MMSounds {
    private MMSounds() {
    }

    public static final DeferredRegister<SoundEvent> REG = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MowziesMobs.MODID);

    // Generic
    public static final RegistryObject<SoundEvent> LASER = create("laser");
    public static final RegistryObject<SoundEvent> SUNSTRIKE = create("sunstrike");

    // Wroughtnaut
    public static final RegistryObject<SoundEvent> ENTITY_WROUGHT_PRE_SWING_1 = create("wroughtnaut.pre_swing1");
    public static final RegistryObject<SoundEvent> ENTITY_WROUGHT_PRE_SWING_2 = create("wroughtnaut.pre_swing2");
    public static final RegistryObject<SoundEvent> ENTITY_WROUGHT_PRE_SWING_3 = create("wroughtnaut.pre_swing3");
    public static final RegistryObject<SoundEvent> ENTITY_WROUGHT_CREAK = create("wroughtnaut.creak");
    public static final RegistryObject<SoundEvent> ENTITY_WROUGHT_SWING_1 = create("wroughtnaut.swing1");
    public static final RegistryObject<SoundEvent> ENTITY_WROUGHT_SWING_2 = create("wroughtnaut.swing2");
    public static final RegistryObject<SoundEvent> ENTITY_WROUGHT_SWING_3 = create("wroughtnaut.swing3");
    public static final RegistryObject<SoundEvent> ENTITY_WROUGHT_SHOUT_1 = create("wroughtnaut.shout1");
    public static final RegistryObject<SoundEvent> ENTITY_WROUGHT_SHOUT_2 = create("wroughtnaut.shout2");
    public static final RegistryObject<SoundEvent> ENTITY_WROUGHT_SHOUT_3 = create("wroughtnaut.shout3");
    public static final RegistryObject<SoundEvent> ENTITY_WROUGHT_PULL_1 = create("wroughtnaut.pull1");
    public static final RegistryObject<SoundEvent> ENTITY_WROUGHT_PULL_2 = create("wroughtnaut.pull2");
    public static final RegistryObject<SoundEvent> ENTITY_WROUGHT_PULL_5 = create("wroughtnaut.pull5");
    public static final RegistryObject<SoundEvent> ENTITY_WROUGHT_RELEASE_2 = create("wroughtnaut.release2");
    public static final RegistryObject<SoundEvent> ENTITY_WROUGHT_WHOOSH = create("wroughtnaut.whoosh");
    public static final RegistryObject<SoundEvent> ENTITY_WROUGHT_HURT_1 = create("wroughtnaut.hurt1");
    public static final RegistryObject<SoundEvent> ENTITY_WROUGHT_SCREAM = create("wroughtnaut.scream");
    public static final RegistryObject<SoundEvent> ENTITY_WROUGHT_GRUNT_1 = create("wroughtnaut.grunt1");
    public static final RegistryObject<SoundEvent> ENTITY_WROUGHT_GRUNT_2 = create("wroughtnaut.grunt2");
    public static final RegistryObject<SoundEvent> ENTITY_WROUGHT_GRUNT_3 = create("wroughtnaut.grunt3");
    public static final RegistryObject<SoundEvent> ENTITY_WROUGHT_AMBIENT = create("wroughtnaut.ambient");
    public static final RegistryObject<SoundEvent> ENTITY_WROUGHT_STEP = create("wroughtnaut.step");
    public static final RegistryObject<SoundEvent> ENTITY_WROUGHT_UNDAMAGED = create("wroughtnaut.undamaged");
    public static final RegistryObject<SoundEvent> ENTITY_WROUGHT_AXE_LAND = create("wroughtnaut.axe_land");
    public static final RegistryObject<SoundEvent> ENTITY_WROUGHT_AXE_HIT = create("wroughtnaut.axe_hit");

    // Barakoa
    public static final RegistryObject<SoundEvent> ENTITY_BARAKOA_INHALE = create("barakoa.inhale");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKOA_BLOWDART = create("barakoa.blowdart");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKOA_BATTLECRY = create("barakoa.battlecry");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKOA_BATTLECRY_2 = create("barakoa.battlecry2");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKOA_SWING = create("barakoa.swing");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKOA_EMERGE = create("barakoa.emerge");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKOA_RETRACT = create("barakoa.retract");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKOA_HURT = create("barakoa.hurt");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKOA_DIE = create("barakoa.die");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKOA_SHOUT = create("barakoa.shout");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKOA_TALK_1 = create("barakoa.talk1");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKOA_TALK_2 = create("barakoa.talk2");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKOA_TALK_3 = create("barakoa.talk3");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKOA_TALK_4 = create("barakoa.talk4");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKOA_TALK_5 = create("barakoa.talk5");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKOA_TALK_6 = create("barakoa.talk6");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKOA_TALK_7 = create("barakoa.talk7");
    public static final ImmutableList<Supplier<SoundEvent>> ENTITY_BARAKOA_TALK = ImmutableList.of(
            ENTITY_BARAKOA_TALK_1::get,
            ENTITY_BARAKOA_TALK_2::get,
            ENTITY_BARAKOA_TALK_3::get,
            ENTITY_BARAKOA_TALK_4::get,
            ENTITY_BARAKOA_TALK_5::get,
            ENTITY_BARAKOA_TALK_6::get,
            ENTITY_BARAKOA_TALK_7::get
    );
    public static final RegistryObject<SoundEvent> ENTITY_BARAKOA_ANGRY_1 = create("barakoa.angry1");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKOA_ANGRY_2 = create("barakoa.angry2");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKOA_ANGRY_3 = create("barakoa.angry3");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKOA_ANGRY_4 = create("barakoa.angry4");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKOA_ANGRY_5 = create("barakoa.angry5");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKOA_ANGRY_6 = create("barakoa.angry6");
    public static final ImmutableList<Supplier<SoundEvent>> ENTITY_BARAKOA_ANGRY = ImmutableList.of(
            ENTITY_BARAKOA_ANGRY_1::get,
            ENTITY_BARAKOA_ANGRY_2::get,
            ENTITY_BARAKOA_ANGRY_3::get,
            ENTITY_BARAKOA_ANGRY_4::get,
            ENTITY_BARAKOA_ANGRY_5::get,
            ENTITY_BARAKOA_ANGRY_6::get
    );
    public static final RegistryObject<SoundEvent> ENTITY_BARAKOA_HEAL_START_1 = create("barakoa.healstart1");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKOA_HEAL_START_2 = create("barakoa.healstart2");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKOA_HEAL_START_3 = create("barakoa.healstart3");
    public static final ImmutableList<Supplier<SoundEvent>> ENTITY_BARAKOA_HEAL_START = ImmutableList.of(
            ENTITY_BARAKOA_HEAL_START_1::get,
            ENTITY_BARAKOA_HEAL_START_2::get,
            ENTITY_BARAKOA_HEAL_START_3::get
    );
    public static final RegistryObject<SoundEvent> ENTITY_BARAKOA_TELEPORT_1 = create("barakoa.teleport1");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKOA_TELEPORT_2 = create("barakoa.teleport2");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKOA_TELEPORT_3 = create("barakoa.teleport3");
    public static final ImmutableList<Supplier<SoundEvent>> ENTITY_BARAKOA_TELEPORT = ImmutableList.of(
            ENTITY_BARAKOA_TELEPORT_1::get,
            ENTITY_BARAKOA_TELEPORT_2::get,
            ENTITY_BARAKOA_TELEPORT_3::get
    );

    public static final RegistryObject<SoundEvent> ENTITY_BARAKOA_HEAL_LOOP = create("barakoa.healloop");

    public static final RegistryObject<SoundEvent> ENTITY_BARAKO_BELLY = create("barako.belly");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKO_BURST = create("barako.burst");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKO_SCREAM = create("barako.scream");
    public static final RegistryObject<SoundEvent> ENTITY_SUPERNOVA_START = create("supernova.start");
    public static final RegistryObject<SoundEvent> ENTITY_SUPERNOVA_BLACKHOLE = create("supernova.blackhole");
    public static final RegistryObject<SoundEvent> ENTITY_SUPERNOVA_END = create("supernova.end");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKO_ATTACK = create("barako.attack");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKO_HURT = create("barako.hurt");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKO_DIE = create("barako.die");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKO_BLESS = create("barako.bless");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKO_TALK_1 = create("barako.talk1");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKO_TALK_2 = create("barako.talk2");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKO_TALK_3 = create("barako.talk3");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKO_TALK_4 = create("barako.talk4");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKO_TALK_5 = create("barako.talk5");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKO_TALK_6 = create("barako.talk6");
    public static final ImmutableList<Supplier<SoundEvent>> ENTITY_BARAKO_TALK = ImmutableList.of(
            ENTITY_BARAKO_TALK_1::get,
            ENTITY_BARAKO_TALK_2::get,
            ENTITY_BARAKO_TALK_3::get,
            ENTITY_BARAKO_TALK_4::get,
            ENTITY_BARAKO_TALK_5::get,
            ENTITY_BARAKO_TALK_6::get
    );
    public static final RegistryObject<SoundEvent> ENTITY_BARAKO_ANGRY_1 = create("barako.angry1");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKO_ANGRY_2 = create("barako.angry2");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKO_ANGRY_3 = create("barako.angry3");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKO_ANGRY_4 = create("barako.angry4");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKO_ANGRY_5 = create("barako.angry5");
    public static final RegistryObject<SoundEvent> ENTITY_BARAKO_ANGRY_6 = create("barako.angry6");
    public static final ImmutableList<Supplier<SoundEvent>> ENTITY_BARAKO_ANGRY = ImmutableList.of(
            ENTITY_BARAKO_ANGRY_1::get,
            ENTITY_BARAKO_ANGRY_2::get,
            ENTITY_BARAKO_ANGRY_3::get,
            ENTITY_BARAKO_ANGRY_4::get,
            ENTITY_BARAKO_ANGRY_5::get,
            ENTITY_BARAKO_ANGRY_6::get
    );

    // Foliaath
    public static final RegistryObject<SoundEvent> ENTITY_FOLIAATH_GRUNT = create("foliaath.grunt");
    public static final RegistryObject<SoundEvent> ENTITY_FOLIAATH_RUSTLE = create("foliaath.rustle");
    public static final RegistryObject<SoundEvent> ENTITY_FOLIAATH_MERGE = create("foliaath.emerge");
    public static final RegistryObject<SoundEvent> ENTITY_FOLIAATH_RETREAT = create("foliaath.retreat");
    public static final RegistryObject<SoundEvent> ENTITY_FOLIAATH_PANT_1 = create("foliaath.pant1");
    public static final RegistryObject<SoundEvent> ENTITY_FOLIAATH_PANT_2 = create("foliaath.pant2");
    public static final RegistryObject<SoundEvent> ENTITY_FOLIAATH_BITE_1 = create("foliaath.bite1");
    public static final RegistryObject<SoundEvent> ENTITY_FOLIAATH_HURT = create("foliaath.hurt");
    public static final RegistryObject<SoundEvent> ENTITY_FOLIAATH_DIE = create("foliaath.die");
    public static final RegistryObject<SoundEvent> ENTITY_FOLIAATH_BABY_EAT = create("foliaath.baby.eat");

    public static final RegistryObject<SoundEvent> ENTITY_FROSTMAW_ROAR = create("frostmaw.roar");
    public static final RegistryObject<SoundEvent> ENTITY_FROSTMAW_DIE = create("frostmaw.die");
    public static final RegistryObject<SoundEvent> ENTITY_FROSTMAW_WHOOSH = create("frostmaw.whoosh");
    public static final RegistryObject<SoundEvent> ENTITY_FROSTMAW_ICEBREATH = create("frostmaw.icebreath");
    public static final RegistryObject<SoundEvent> ENTITY_FROSTMAW_ICEBREATH_START = create("frostmaw.icebreathstart");
    public static final RegistryObject<SoundEvent> ENTITY_FROSTMAW_ICEBALL_CHARGE = create("frostmaw.iceballcharge");
    public static final RegistryObject<SoundEvent> ENTITY_FROSTMAW_ICEBALL_SHOOT = create("frostmaw.iceballshoot");
    public static final RegistryObject<SoundEvent> ENTITY_FROSTMAW_FROZEN_CRASH = create("frostmaw.frozencrash");
    public static final RegistryObject<SoundEvent> ENTITY_FROSTMAW_STEP = create("frostmaw.step");
    public static final RegistryObject<SoundEvent> ENTITY_FROSTMAW_LAND = create("frostmaw.land");
    public static final RegistryObject<SoundEvent> ENTITY_FROSTMAW_ATTACK_1 = create("frostmaw.attack1");
    public static final RegistryObject<SoundEvent> ENTITY_FROSTMAW_ATTACK_2 = create("frostmaw.attack2");
    public static final RegistryObject<SoundEvent> ENTITY_FROSTMAW_ATTACK_3 = create("frostmaw.attack3");
    public static final RegistryObject<SoundEvent> ENTITY_FROSTMAW_ATTACK_4 = create("frostmaw.attack4");
    public static final ImmutableList<Supplier<SoundEvent>> ENTITY_FROSTMAW_ATTACK = ImmutableList.of(
            ENTITY_FROSTMAW_ATTACK_1::get,
            ENTITY_FROSTMAW_ATTACK_2::get,
            ENTITY_FROSTMAW_ATTACK_3::get,
            ENTITY_FROSTMAW_ATTACK_4::get
    );
    public static final RegistryObject<SoundEvent> ENTITY_FROSTMAW_BREATH_1 = create("frostmaw.breath1");
    public static final RegistryObject<SoundEvent> ENTITY_FROSTMAW_BREATH_2 = create("frostmaw.breath2");
    public static final ImmutableList<Supplier<SoundEvent>> ENTITY_FROSTMAW_BREATH = ImmutableList.of(
            ENTITY_FROSTMAW_BREATH_1::get,
            ENTITY_FROSTMAW_BREATH_2::get
    );
    public static final RegistryObject<SoundEvent> ENTITY_FROSTMAW_LIVING_1 = create("frostmaw.living1");
    public static final RegistryObject<SoundEvent> ENTITY_FROSTMAW_LIVING_2 = create("frostmaw.living2");
    public static final ImmutableList<Supplier<SoundEvent>> ENTITY_FROSTMAW_LIVING = ImmutableList.of(
            ENTITY_FROSTMAW_LIVING_1::get,
            ENTITY_FROSTMAW_LIVING_2::get
    );
    public static final RegistryObject<SoundEvent> ENTITY_FROSTMAW_WAKEUP = create("frostmaw.wakeup");

    public static final RegistryObject<SoundEvent> ENTITY_GROTTOL_STEP = create("grottol.step");
    public static final RegistryObject<SoundEvent> ENTITY_GROTTOL_UNDAMAGED = create("grottol.undamaged");
    public static final RegistryObject<SoundEvent> ENTITY_GROTTOL_BURROW = create("grottol.burrow");
    public static final RegistryObject<SoundEvent> ENTITY_GROTTOL_DIE = create("grottol.die");

    public static final RegistryObject<SoundEvent> ENTITY_LANTERN_POP = create("lantern.pop");
    public static final RegistryObject<SoundEvent> ENTITY_LANTERN_PUFF = create("lantern.puff");

    public static final RegistryObject<SoundEvent> ENTITY_NAGA_ACID_CHARGE = create("naga.acidcharge");
    public static final RegistryObject<SoundEvent> ENTITY_NAGA_ACID_HIT = create("naga.acidhit");
    public static final RegistryObject<SoundEvent> ENTITY_NAGA_ACID_SPIT = create("naga.acidspit");
    public static final RegistryObject<SoundEvent> ENTITY_NAGA_ACID_SPIT_HISS = create("naga.acidspithiss");
    public static final RegistryObject<SoundEvent> ENTITY_NAGA_FLAP_1 = create("naga.flap1");
    public static final RegistryObject<SoundEvent> ENTITY_NAGA_GROWL_1 = create("naga.growl1");
    public static final RegistryObject<SoundEvent> ENTITY_NAGA_GROWL_2 = create("naga.growl2");
    public static final RegistryObject<SoundEvent> ENTITY_NAGA_GROWL_3 = create("naga.growl3");
    public static final RegistryObject<SoundEvent> ENTITY_NAGA_GRUNT_1 = create("naga.grunt1");
    public static final RegistryObject<SoundEvent> ENTITY_NAGA_GRUNT_2 = create("naga.grunt2");
    public static final RegistryObject<SoundEvent> ENTITY_NAGA_GRUNT_3 = create("naga.grunt3");
    public static final RegistryObject<SoundEvent> ENTITY_NAGA_ROAR_1 = create("naga.roar1");
    public static final RegistryObject<SoundEvent> ENTITY_NAGA_ROAR_2 = create("naga.roar2");
    public static final RegistryObject<SoundEvent> ENTITY_NAGA_ROAR_3 = create("naga.roar3");
    public static final RegistryObject<SoundEvent> ENTITY_NAGA_ROAR_4 = create("naga.roar4");
    public static final RegistryObject<SoundEvent> ENTITY_NAGA_SWOOP = create("naga.swoop");
    public static final ImmutableList<Supplier<SoundEvent>> ENTITY_NAGA_ROAR = ImmutableList.of(
            ENTITY_NAGA_ROAR_1::get,
            ENTITY_NAGA_ROAR_2::get,
            ENTITY_NAGA_ROAR_3::get,
            ENTITY_NAGA_ROAR_4::get
    );
    public static final ImmutableList<Supplier<SoundEvent>> ENTITY_NAGA_GRUNT = ImmutableList.of(
            ENTITY_NAGA_GRUNT_1::get,
            ENTITY_NAGA_GRUNT_2::get,
            ENTITY_NAGA_GRUNT_3::get
    );
    public static final ImmutableList<Supplier<SoundEvent>> ENTITY_NAGA_GROWL = ImmutableList.of(
            ENTITY_NAGA_GROWL_1::get,
            ENTITY_NAGA_GROWL_2::get,
            ENTITY_NAGA_GROWL_3::get
    );

    public static final RegistryObject<SoundEvent> EFFECT_GEOMANCY_SMALL_CRASH = create("geomancy.smallcrash");
    public static final RegistryObject<SoundEvent> EFFECT_GEOMANCY_MAGIC_SMALL = create("geomancy.hitsmall");
    public static final RegistryObject<SoundEvent> EFFECT_GEOMANCY_MAGIC_BIG = create("geomancy.hitbig");
    public static final RegistryObject<SoundEvent> EFFECT_GEOMANCY_BREAK_LARGE_1 = create("geomancy.breaklarge");
    public static final RegistryObject<SoundEvent> EFFECT_GEOMANCY_BREAK_LARGE_2 = create("geomancy.breaklarge2");
    public static final RegistryObject<SoundEvent> EFFECT_GEOMANCY_BREAK_MEDIUM_1 = create("geomancy.breakmedium");
    public static final RegistryObject<SoundEvent> EFFECT_GEOMANCY_BREAK_MEDIUM_2 = create("geomancy.breakmedium2");
    public static final RegistryObject<SoundEvent> EFFECT_GEOMANCY_BREAK_MEDIUM_3 = create("geomancy.breakmedium3");
    public static final ImmutableList<Supplier<SoundEvent>> EFFECT_GEOMANCY_BREAK_MEDIUM = ImmutableList.of(
            EFFECT_GEOMANCY_BREAK_MEDIUM_1::get,
            EFFECT_GEOMANCY_BREAK_MEDIUM_2::get,
            EFFECT_GEOMANCY_BREAK_MEDIUM_3::get
    );
    public static final RegistryObject<SoundEvent> EFFECT_GEOMANCY_HIT = create("geomancy.hit");
    public static final RegistryObject<SoundEvent> EFFECT_GEOMANCY_HIT_MEDIUM_1 = create("geomancy.hitmedium");
    public static final RegistryObject<SoundEvent> EFFECT_GEOMANCY_HIT_MEDIUM_2 = create("geomancy.hitmedium2");
    public static final ImmutableList<Supplier<SoundEvent>> EFFECT_GEOMANCY_HIT_MEDIUM = ImmutableList.of(
            EFFECT_GEOMANCY_HIT_MEDIUM_1::get,
            EFFECT_GEOMANCY_HIT_MEDIUM_2::get
    );
    public static final RegistryObject<SoundEvent> EFFECT_GEOMANCY_BREAK = create("geomancy.rockbreak");
    public static final RegistryObject<SoundEvent> EFFECT_GEOMANCY_CRASH = create("geomancy.rockcrash1");
    public static final RegistryObject<SoundEvent> EFFECT_GEOMANCY_CRUMBLE = create("geomancy.rockcrumble");
    public static final RegistryObject<SoundEvent> EFFECT_GEOMANCY_RUMBLE_1 = create("geomancy.rumble1");
    public static final RegistryObject<SoundEvent> EFFECT_GEOMANCY_RUMBLE_2 = create("geomancy.rumble2");
    public static final RegistryObject<SoundEvent> EFFECT_GEOMANCY_RUMBLE_3 = create("geomancy.rumble3");
    public static final ImmutableList<Supplier<SoundEvent>> EFFECT_GEOMANCY_RUMBLE = ImmutableList.of(
            EFFECT_GEOMANCY_RUMBLE_1::get,
            EFFECT_GEOMANCY_RUMBLE_2::get,
            EFFECT_GEOMANCY_RUMBLE_3::get
    );
    public static final RegistryObject<SoundEvent> EFFECT_GEOMANCY_HIT_SMALL = create("geomancy.smallrockhit");
    public static final RegistryObject<SoundEvent> EFFECT_GEOMANCY_BOULDER_CHARGE = create("geomancy.bouldercharge");
    public static final RegistryObject<SoundEvent> EFFECT_GEOMANCY_MAGIC_CHARGE_SMALL = create("geomancy.magicchargesmall");

    public static final RegistryObject<SoundEvent> MISC_GROUNDHIT_1 = create("misc.groundhit1");
    public static final RegistryObject<SoundEvent> MISC_GROUNDHIT_2 = create("misc.groundhit2");

    // Music
    public static final RegistryObject<SoundEvent> MUSIC_BLACK_PINK = create("music.black_pink");
    public static final RegistryObject<SoundEvent> MUSIC_PETIOLE = create("music.petiole");
    public static final RegistryObject<SoundEvent> MUSIC_BARAKO_THEME = create("music.barako_theme");
    public static final RegistryObject<SoundEvent> MUSIC_FERROUS_WROUGHTNAUT_THEME = create("music.ferrous_wroughtnaut_theme");
    public static final RegistryObject<SoundEvent> MUSIC_FROSTMAW_THEME = create("music.frostmaw_theme");

    private static RegistryObject<SoundEvent> create(String name) {
        return REG.register(name, () -> new SoundEvent(new ResourceLocation(MowziesMobs.MODID, name)));
    }
}