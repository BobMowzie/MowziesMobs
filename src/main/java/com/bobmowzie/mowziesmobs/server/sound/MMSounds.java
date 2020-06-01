package com.bobmowzie.mowziesmobs.server.sound;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID)
public final class MMSounds {
    private MMSounds() {
    }

    private static final SoundEvent NIL = null;

    // Generic
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":laser")
    public static final SoundEvent LASER = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":sunstrike")
    public static final SoundEvent SUNSTRIKE = NIL;

    // Wroughtnaut
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":wroughtnaut.preSwing1")
    public static final SoundEvent ENTITY_WROUGHT_PRE_SWING_1 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":wroughtnaut.preSwing2")
    public static final SoundEvent ENTITY_WROUGHT_PRE_SWING_2 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":wroughtnaut.preSwing3")
    public static final SoundEvent ENTITY_WROUGHT_PRE_SWING_3 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":wroughtnaut.creak")
    public static final SoundEvent ENTITY_WROUGHT_CREAK = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":wroughtnaut.swing1")
    public static final SoundEvent ENTITY_WROUGHT_SWING_1 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":wroughtnaut.swing2")
    public static final SoundEvent ENTITY_WROUGHT_SWING_2 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":wroughtnaut.swing3")
    public static final SoundEvent ENTITY_WROUGHT_SWING_3 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":wroughtnaut.shout1")
    public static final SoundEvent ENTITY_WROUGHT_SHOUT_1 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":wroughtnaut.shout2")
    public static final SoundEvent ENTITY_WROUGHT_SHOUT_2 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":wroughtnaut.shout3")
    public static final SoundEvent ENTITY_WROUGHT_SHOUT_3 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":wroughtnaut.pull1")
    public static final SoundEvent ENTITY_WROUGHT_PULL_1 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":wroughtnaut.pull2")
    public static final SoundEvent ENTITY_WROUGHT_PULL_2 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":wroughtnaut.pull5")
    public static final SoundEvent ENTITY_WROUGHT_PULL_5 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":wroughtnaut.release2")
    public static final SoundEvent ENTITY_WROUGHT_RELEASE_2 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":wroughtnaut.whoosh")
    public static final SoundEvent ENTITY_WROUGHT_WHOOSH = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":wroughtnaut.hurt1")
    public static final SoundEvent ENTITY_WROUGHT_HURT_1 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":wroughtnaut.scream")
    public static final SoundEvent ENTITY_WROUGHT_SCREAM = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":wroughtnaut.grunt1")
    public static final SoundEvent ENTITY_WROUGHT_GRUNT_1 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":wroughtnaut.grunt2")
    public static final SoundEvent ENTITY_WROUGHT_GRUNT_2 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":wroughtnaut.grunt3")
    public static final SoundEvent ENTITY_WROUGHT_GRUNT_3 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":wroughtnaut.ambient")
    public static final SoundEvent ENTITY_WROUGHT_AMBIENT = NIL;

    // Barakoa
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barakoa.inhale")
    public static final SoundEvent ENTITY_BARAKOA_INHALE = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barakoa.blowdart")
    public static final SoundEvent ENTITY_BARAKOA_BLOWDART = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barakoa.battlecry")
    public static final SoundEvent ENTITY_BARAKOA_BATTLECRY = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barakoa.battlecry2")
    public static final SoundEvent ENTITY_BARAKOA_BATTLECRY_2 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barakoa.swing")
    public static final SoundEvent ENTITY_BARAKOA_SWING = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barakoa.emerge")
    public static final SoundEvent ENTITY_BARAKOA_EMERGE = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barakoa.retract")
    public static final SoundEvent ENTITY_BARAKOA_RETRACT = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barakoa.hurt")
    public static final SoundEvent ENTITY_BARAKOA_HURT = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barakoa.die")
    public static final SoundEvent ENTITY_BARAKOA_DIE = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barakoa.shout")
    public static final SoundEvent ENTITY_BARAKOA_SHOUT = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barakoa.talk1")
    public static final SoundEvent ENTITY_BARAKOA_TALK_1 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barakoa.talk2")
    public static final SoundEvent ENTITY_BARAKOA_TALK_2 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barakoa.talk3")
    public static final SoundEvent ENTITY_BARAKOA_TALK_3 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barakoa.talk4")
    public static final SoundEvent ENTITY_BARAKOA_TALK_4 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barakoa.talk5")
    public static final SoundEvent ENTITY_BARAKOA_TALK_5 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barakoa.talk6")
    public static final SoundEvent ENTITY_BARAKOA_TALK_6 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barakoa.talk7")
    public static final SoundEvent ENTITY_BARAKOA_TALK_7 = NIL;
    public static final ImmutableList<Supplier<SoundEvent>> ENTITY_BARAKOA_TALK = ImmutableList.of(
            () -> ENTITY_BARAKOA_TALK_1,
            () -> ENTITY_BARAKOA_TALK_2,
            () -> ENTITY_BARAKOA_TALK_3,
            () -> ENTITY_BARAKOA_TALK_4,
            () -> ENTITY_BARAKOA_TALK_5,
            () -> ENTITY_BARAKOA_TALK_6,
            () -> ENTITY_BARAKOA_TALK_7
    );
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barakoa.angry1")
    public static final SoundEvent ENTITY_BARAKOA_ANGRY_1 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barakoa.angry2")
    public static final SoundEvent ENTITY_BARAKOA_ANGRY_2 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barakoa.angry3")
    public static final SoundEvent ENTITY_BARAKOA_ANGRY_3 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barakoa.angry4")
    public static final SoundEvent ENTITY_BARAKOA_ANGRY_4 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barakoa.angry5")
    public static final SoundEvent ENTITY_BARAKOA_ANGRY_5 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barakoa.angry6")
    public static final SoundEvent ENTITY_BARAKOA_ANGRY_6 = NIL;
    public static final ImmutableList<Supplier<SoundEvent>> ENTITY_BARAKOA_ANGRY = ImmutableList.of(
            () -> ENTITY_BARAKOA_ANGRY_1,
            () -> ENTITY_BARAKOA_ANGRY_2,
            () -> ENTITY_BARAKOA_ANGRY_3,
            () -> ENTITY_BARAKOA_ANGRY_4,
            () -> ENTITY_BARAKOA_ANGRY_5,
            () -> ENTITY_BARAKOA_ANGRY_6
    );

    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barako.belly")
    public static final SoundEvent ENTITY_BARAKO_BELLY = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barako.burst")
    public static final SoundEvent ENTITY_BARAKO_BURST = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barako.scream")
    public static final SoundEvent ENTITY_BARAKO_SCREAM = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":supernova.start")
    public static final SoundEvent ENTITY_SUPERNOVA_START = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":supernova.blackhole")
    public static final SoundEvent ENTITY_SUPERNOVA_BLACKHOLE = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":supernova.end")
    public static final SoundEvent ENTITY_SUPERNOVA_END = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barako.attack")
    public static final SoundEvent ENTITY_BARAKO_ATTACK = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barako.hurt")
    public static final SoundEvent ENTITY_BARAKO_HURT = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barako.die")
    public static final SoundEvent ENTITY_BARAKO_DIE = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barako.bless")
    public static final SoundEvent ENTITY_BARAKO_BLESS = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barako.talk1")
    public static final SoundEvent ENTITY_BARAKO_TALK_1 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barako.talk2")
    public static final SoundEvent ENTITY_BARAKO_TALK_2 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barako.talk3")
    public static final SoundEvent ENTITY_BARAKO_TALK_3 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barako.talk4")
    public static final SoundEvent ENTITY_BARAKO_TALK_4 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barako.talk5")
    public static final SoundEvent ENTITY_BARAKO_TALK_5 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barako.talk6")
    public static final SoundEvent ENTITY_BARAKO_TALK_6 = NIL;
    public static final ImmutableList<Supplier<SoundEvent>> ENTITY_BARAKO_TALK = ImmutableList.of(
            () -> ENTITY_BARAKO_TALK_1,
            () -> ENTITY_BARAKO_TALK_2,
            () -> ENTITY_BARAKO_TALK_3,
            () -> ENTITY_BARAKO_TALK_4,
            () -> ENTITY_BARAKO_TALK_5,
            () -> ENTITY_BARAKO_TALK_6
    );
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barako.angry1")
    public static final SoundEvent ENTITY_BARAKO_ANGRY_1 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barako.angry2")
    public static final SoundEvent ENTITY_BARAKO_ANGRY_2 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barako.angry3")
    public static final SoundEvent ENTITY_BARAKO_ANGRY_3 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barako.angry4")
    public static final SoundEvent ENTITY_BARAKO_ANGRY_4 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barako.angry5")
    public static final SoundEvent ENTITY_BARAKO_ANGRY_5 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":barako.angry6")
    public static final SoundEvent ENTITY_BARAKO_ANGRY_6 = NIL;
    public static final ImmutableList<Supplier<SoundEvent>> ENTITY_BARAKO_ANGRY = ImmutableList.of(
            () -> ENTITY_BARAKO_ANGRY_1,
            () -> ENTITY_BARAKO_ANGRY_2,
            () -> ENTITY_BARAKO_ANGRY_3,
            () -> ENTITY_BARAKO_ANGRY_4,
            () -> ENTITY_BARAKO_ANGRY_5,
            () -> ENTITY_BARAKO_ANGRY_6
    );

    // Foliaath
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":foliaath.grunt")
    public static final SoundEvent ENTITY_FOLIAATH_GRUNT = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":foliaath.rustle")
    public static final SoundEvent ENTITY_FOLIAATH_RUSTLE = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":foliaath.emerge")
    public static final SoundEvent ENTITY_FOLIAATH_MERGE = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":foliaath.retreat")
    public static final SoundEvent ENTITY_FOLIAATH_RETREAT = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":foliaath.pant1")
    public static final SoundEvent ENTITY_FOLIAATH_PANT_1 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":foliaath.pant2")
    public static final SoundEvent ENTITY_FOLIAATH_PANT_2 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":foliaath.bite1")
    public static final SoundEvent ENTITY_FOLIAATH_BITE_1 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":foliaath.hurt")
    public static final SoundEvent ENTITY_FOLIAATH_HURT = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":foliaath.die")
    public static final SoundEvent ENTITY_FOLIAATH_DIE = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":foliaath.baby.eat")
    public static final SoundEvent ENTITY_FOLIAATH_BABY_EAT = NIL;

    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":frostmaw.roar")
    public static final SoundEvent ENTITY_FROSTMAW_ROAR = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":frostmaw.die")
    public static final SoundEvent ENTITY_FROSTMAW_DIE = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":frostmaw.whoosh")
    public static final SoundEvent ENTITY_FROSTMAW_WHOOSH = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":frostmaw.icebreath")
    public static final SoundEvent ENTITY_FROSTMAW_ICEBREATH = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":frostmaw.icebreathstart")
    public static final SoundEvent ENTITY_FROSTMAW_ICEBREATH_START = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":frostmaw.iceballcharge")
    public static final SoundEvent ENTITY_FROSTMAW_ICEBALL_CHARGE = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":frostmaw.iceballshoot")
    public static final SoundEvent ENTITY_FROSTMAW_ICEBALL_SHOOT = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":frostmaw.frozencrash")
    public static final SoundEvent ENTITY_FROSTMAW_FROZEN_CRASH = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":frostmaw.step")
    public static final SoundEvent ENTITY_FROSTMAW_STEP = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":frostmaw.land")
    public static final SoundEvent ENTITY_FROSTMAW_LAND = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":frostmaw.attack1")
    public static final SoundEvent ENTITY_FROSTMAW_ATTACK_1 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":frostmaw.attack2")
    public static final SoundEvent ENTITY_FROSTMAW_ATTACK_2 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":frostmaw.attack3")
    public static final SoundEvent ENTITY_FROSTMAW_ATTACK_3 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":frostmaw.attack4")
    public static final SoundEvent ENTITY_FROSTMAW_ATTACK_4 = NIL;
    public static final ImmutableList<Supplier<SoundEvent>> ENTITY_FROSTMAW_ATTACK = ImmutableList.of(
            () -> ENTITY_FROSTMAW_ATTACK_1,
            () -> ENTITY_FROSTMAW_ATTACK_2,
            () -> ENTITY_FROSTMAW_ATTACK_3,
            () -> ENTITY_FROSTMAW_ATTACK_4
    );
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":frostmaw.breath1")
    public static final SoundEvent ENTITY_FROSTMAW_BREATH_1 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":frostmaw.breath2")
    public static final SoundEvent ENTITY_FROSTMAW_BREATH_2 = NIL;
    public static final ImmutableList<Supplier<SoundEvent>> ENTITY_FROSTMAW_BREATH = ImmutableList.of(
            () -> ENTITY_FROSTMAW_BREATH_1,
            () -> ENTITY_FROSTMAW_BREATH_2
    );
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":frostmaw.living1")
    public static final SoundEvent ENTITY_FROSTMAW_LIVING_1 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":frostmaw.living2")
    public static final SoundEvent ENTITY_FROSTMAW_LIVING_2 = NIL;
    public static final ImmutableList<Supplier<SoundEvent>> ENTITY_FROSTMAW_LIVING = ImmutableList.of(
            () -> ENTITY_FROSTMAW_LIVING_1,
            () -> ENTITY_FROSTMAW_LIVING_2
    );
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":frostmaw.wakeup")
    public static final SoundEvent ENTITY_FROSTMAW_WAKEUP = NIL;

    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":grottol.step")
    public static final SoundEvent ENTITY_GROTTOL_STEP = NIL;

    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":lantern.pop")
    public static final SoundEvent ENTITY_LANTERN_POP = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":lantern.puff")
    public static final SoundEvent ENTITY_LANTERN_PUFF = NIL;

    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":naga.acidcharge")
    public static final SoundEvent ENTITY_NAGA_ACID_CHARGE = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":naga.acidhit")
    public static final SoundEvent ENTITY_NAGA_ACID_HIT = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":naga.acidspit")
    public static final SoundEvent ENTITY_NAGA_ACID_SPIT = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":naga.acidspithiss")
    public static final SoundEvent ENTITY_NAGA_ACID_SPIT_HISS = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":naga.flap1")
    public static final SoundEvent ENTITY_NAGA_FLAP_1 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":naga.growl1")
    public static final SoundEvent ENTITY_NAGA_GROWL_1 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":naga.growl2")
    public static final SoundEvent ENTITY_NAGA_GROWL_2 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":naga.growl3")
    public static final SoundEvent ENTITY_NAGA_GROWL_3 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":naga.grunt1")
    public static final SoundEvent ENTITY_NAGA_GRUNT_1 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":naga.grunt2")
    public static final SoundEvent ENTITY_NAGA_GRUNT_2 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":naga.grunt3")
    public static final SoundEvent ENTITY_NAGA_GRUNT_3 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":naga.roar1")
    public static final SoundEvent ENTITY_NAGA_ROAR_1 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":naga.roar2")
    public static final SoundEvent ENTITY_NAGA_ROAR_2 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":naga.roar3")
    public static final SoundEvent ENTITY_NAGA_ROAR_3 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":naga.roar4")
    public static final SoundEvent ENTITY_NAGA_ROAR_4 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":naga.swoop")
    public static final SoundEvent ENTITY_NAGA_SWOOP = NIL;
    public static final ImmutableList<Supplier<SoundEvent>> ENTITY_NAGA_ROAR = ImmutableList.of(
            () -> ENTITY_NAGA_ROAR_1,
            () -> ENTITY_NAGA_ROAR_2,
            () -> ENTITY_NAGA_ROAR_3,
            () -> ENTITY_NAGA_ROAR_4
    );
    public static final ImmutableList<Supplier<SoundEvent>> ENTITY_NAGA_GRUNT = ImmutableList.of(
            () -> ENTITY_NAGA_GRUNT_1,
            () -> ENTITY_NAGA_GRUNT_2,
            () -> ENTITY_NAGA_GRUNT_3
    );
    public static final ImmutableList<Supplier<SoundEvent>> ENTITY_NAGA_GROWL = ImmutableList.of(
            () -> ENTITY_NAGA_GROWL_1,
            () -> ENTITY_NAGA_GROWL_2,
            () -> ENTITY_NAGA_GROWL_3
    );

    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":geomancy.smallcrash")
    public static final SoundEvent EFFECT_GEOMANCY_SMALL_CRASH = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":geomancy.hitsmall")
    public static final SoundEvent EFFECT_GEOMANCY_MAGIC_SMALL = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":geomancy.hitbig")
    public static final SoundEvent EFFECT_GEOMANCY_MAGIC_BIG = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":geomancy.breaklarge")
    public static final SoundEvent EFFECT_GEOMANCY_BREAK_LARGE_1 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":geomancy.breaklarge2")
    public static final SoundEvent EFFECT_GEOMANCY_BREAK_LARGE_2 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":geomancy.breakmedium")
    public static final SoundEvent EFFECT_GEOMANCY_BREAK_MEDIUM_1 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":geomancy.breakmedium2")
    public static final SoundEvent EFFECT_GEOMANCY_BREAK_MEDIUM_2 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":geomancy.breakmedium3")
    public static final SoundEvent EFFECT_GEOMANCY_BREAK_MEDIUM_3 = NIL;
    public static final ImmutableList<Supplier<SoundEvent>> EFFECT_GEOMANCY_BREAK_MEDIUM = ImmutableList.of(
            () -> EFFECT_GEOMANCY_BREAK_MEDIUM_1,
            () -> EFFECT_GEOMANCY_BREAK_MEDIUM_2,
            () -> EFFECT_GEOMANCY_BREAK_MEDIUM_3
    );
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":geomancy.hit")
    public static final SoundEvent EFFECT_GEOMANCY_HIT = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":geomancy.hitmedium")
    public static final SoundEvent EFFECT_GEOMANCY_HIT_MEDIUM_1 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":geomancy.hitmedium2")
    public static final SoundEvent EFFECT_GEOMANCY_HIT_MEDIUM_2 = NIL;
    public static final ImmutableList<Supplier<SoundEvent>> EFFECT_GEOMANCY_HIT_MEDIUM = ImmutableList.of(
            () -> EFFECT_GEOMANCY_HIT_MEDIUM_1,
            () -> EFFECT_GEOMANCY_HIT_MEDIUM_2
    );
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":geomancy.rockbreak")
    public static final SoundEvent EFFECT_GEOMANCY_BREAK = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":geomancy.rockcrash1")
    public static final SoundEvent EFFECT_GEOMANCY_CRASH = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":geomancy.rockcrumble")
    public static final SoundEvent EFFECT_GEOMANCY_CRUMBLE = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":geomancy.rumble1")
    public static final SoundEvent EFFECT_GEOMANCY_RUMBLE_1 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":geomancy.rumble2")
    public static final SoundEvent EFFECT_GEOMANCY_RUMBLE_2 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":geomancy.rumble3")
    public static final SoundEvent EFFECT_GEOMANCY_RUMBLE_3 = NIL;
    public static final ImmutableList<Supplier<SoundEvent>> EFFECT_GEOMANCY_RUMBLE = ImmutableList.of(
            () -> EFFECT_GEOMANCY_RUMBLE_1,
            () -> EFFECT_GEOMANCY_RUMBLE_2,
            () -> EFFECT_GEOMANCY_RUMBLE_3
    );
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":geomancy.smallrockhit")
    public static final SoundEvent EFFECT_GEOMANCY_HIT_SMALL = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":geomancy.bouldercharge")
    public static final SoundEvent EFFECT_GEOMANCY_BOULDER_CHARGE = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":geomancy.magicchargesmall")
    public static final SoundEvent EFFECT_GEOMANCY_MAGIC_CHARGE_SMALL = NIL;

    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":misc.groundhit1")
    public static final SoundEvent MISC_GROUNDHIT_1 = NIL;
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":misc.groundhit2")
    public static final SoundEvent MISC_GROUNDHIT_2 = NIL;

    // Easter Eggs
    @GameRegistry.ObjectHolder(MowziesMobs.MODID + ":music.black_pink")
    public static final SoundEvent MUSIC_BLACK_PINK = NIL;

    @SubscribeEvent
    public static void register(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(
                create("laser"),
                create("sunstrike"),

                create("wroughtnaut.preSwing1"),
                create("wroughtnaut.preSwing2"),
                create("wroughtnaut.preSwing3"),
                create("wroughtnaut.creak"),
                create("wroughtnaut.swing1"),
                create("wroughtnaut.swing2"),
                create("wroughtnaut.swing3"),
                create("wroughtnaut.shout1"),
                create("wroughtnaut.shout2"),
                create("wroughtnaut.shout3"),
                create("wroughtnaut.pull1"),
                create("wroughtnaut.pull2"),
                create("wroughtnaut.pull5"),
                create("wroughtnaut.release2"),
                create("wroughtnaut.whoosh"),
                create("wroughtnaut.hurt1"),
                create("wroughtnaut.scream"),
                create("wroughtnaut.grunt1"),
                create("wroughtnaut.grunt2"),
                create("wroughtnaut.grunt3"),
                create("wroughtnaut.ambient"),

                create("barakoa.inhale"),
                create("barakoa.blowdart"),
                create("barakoa.battlecry"),
                create("barakoa.battlecry2"),
                create("barakoa.swing"),
                create("barakoa.emerge"),
                create("barakoa.retract"),
                create("barakoa.hurt"),
                create("barakoa.die"),
                create("barakoa.shout"),
                create("barakoa.talk1"),
                create("barakoa.talk2"),
                create("barakoa.talk3"),
                create("barakoa.talk4"),
                create("barakoa.talk5"),
                create("barakoa.talk6"),
                create("barakoa.talk7"),
                create("barakoa.angry1"),
                create("barakoa.angry2"),
                create("barakoa.angry3"),
                create("barakoa.angry4"),
                create("barakoa.angry5"),
                create("barakoa.angry6"),

                create("barako.belly"),
                create("barako.burst"),
                create("barako.scream"),
                create("supernova.start"),
                create("supernova.blackhole"),
                create("supernova.end"),
                create("barako.attack"),
                create("barako.hurt"),
                create("barako.die"),
                create("barako.bless"),
                create("barako.talk1"),
                create("barako.talk2"),
                create("barako.talk3"),
                create("barako.talk4"),
                create("barako.talk5"),
                create("barako.talk6"),
                create("barako.angry1"),
                create("barako.angry2"),
                create("barako.angry3"),
                create("barako.angry4"),
                create("barako.angry5"),
                create("barako.angry6"),

                create("foliaath.grunt"),
                create("foliaath.rustle"),
                create("foliaath.emerge"),
                create("foliaath.retreat"),
                create("foliaath.pant1"),
                create("foliaath.pant2"),
                create("foliaath.bite1"),
                create("foliaath.hurt"),
                create("foliaath.die"),
                create("foliaath.baby.eat"),

                create("frostmaw.roar"),
                create("frostmaw.die"),
                create("frostmaw.whoosh"),
                create("frostmaw.icebreath"),
                create("frostmaw.icebreathstart"),
                create("frostmaw.iceballcharge"),
                create("frostmaw.iceballshoot"),
                create("frostmaw.frozencrash"),
                create("frostmaw.step"),
                create("frostmaw.land"),
                create("frostmaw.attack1"),
                create("frostmaw.attack2"),
                create("frostmaw.attack3"),
                create("frostmaw.attack4"),
                create("frostmaw.breath1"),
                create("frostmaw.breath2"),
                create("frostmaw.living1"),
                create("frostmaw.living2"),
                create("frostmaw.wakeup"),

                create("grottol.step"),

                create("lantern.puff"),
                create("lantern.pop"),

                create("naga.acidcharge"),
                create("naga.acidhit"),
                create("naga.acidspit"),
                create("naga.acidspithiss"),
                create("naga.flap1"),
                create("naga.growl1"),
                create("naga.growl2"),
                create("naga.growl3"),
                create("naga.grunt1"),
                create("naga.grunt2"),
                create("naga.grunt3"),
                create("naga.roar1"),
                create("naga.roar2"),
                create("naga.roar3"),
                create("naga.roar4"),
                create("naga.swoop"),

                create("geomancy.smallcrash"),
                create("geomancy.hitsmall"),
                create("geomancy.hitbig"),
                create("geomancy.breaklarge"),
                create("geomancy.breaklarge2"),
                create("geomancy.breakmedium"),
                create("geomancy.breakmedium2"),
                create("geomancy.breakmedium3"),
                create("geomancy.hit"),
                create("geomancy.hitmedium"),
                create("geomancy.hitmedium2"),
                create("geomancy.rockbreak"),
                create("geomancy.rockcrash1"),
                create("geomancy.rockcrumble"),
                create("geomancy.rumble1"),
                create("geomancy.rumble2"),
                create("geomancy.rumble3"),
                create("geomancy.smallrockhit"),
                create("geomancy.bouldercharge"),
                create("geomancy.magicchargesmall"),

                create("misc.groundhit1"),
                create("misc.groundhit2"),

                create("music.black_pink")
        );
    }

    private static SoundEvent create(String name) {
        return new SoundEvent(new ResourceLocation(MowziesMobs.MODID, name)).setRegistryName(name);
    }
}