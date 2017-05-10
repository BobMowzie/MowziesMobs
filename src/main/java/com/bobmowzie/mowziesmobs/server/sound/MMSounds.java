package com.bobmowzie.mowziesmobs.server.sound;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.bobmowzie.mowziesmobs.MowziesMobs;

public enum MMSounds {
    INSTANCE;

    // Generic

    public static final SoundEvent LASER = reg("laser");

    public static final SoundEvent SUNSTRIKE = reg("sunstrike");

    // Wroughtnaut

    public static final SoundEvent ENTITY_WROUGHT_PRE_SWING_1 = reg("wroughtnaut.preSwing1");

    public static final SoundEvent ENTITY_WROUGHT_PRE_SWING_2 = reg("wroughtnaut.preSwing2");

    public static final SoundEvent ENTITY_WROUGHT_CREAK = reg("wroughtnaut.creak");

    public static final SoundEvent ENTITY_WROUGHT_SWING_1 = reg("wroughtnaut.swing1");

    public static final SoundEvent ENTITY_WROUGHT_SWING_2 = reg("wroughtnaut.swing2");

    public static final SoundEvent ENTITY_WROUGHT_SHOUT_2 = reg("wroughtnaut.shout2");

    public static final SoundEvent ENTITY_WROUGHT_PULL_1 = reg("wroughtnaut.pull1");

    public static final SoundEvent ENTITY_WROUGHT_PULL_5 = reg("wroughtnaut.pull5");

    public static final SoundEvent ENTITY_WROUGHT_RELEASE_2 = reg("wroughtnaut.release2");

    public static final SoundEvent ENTITY_WROUGHT_WHOOSH = reg("wroughtnaut.whoosh");

    public static final SoundEvent ENTITY_WROUGHT_HURT_1 = reg("wroughtnaut.hurt1");

    public static final SoundEvent ENTITY_WROUGHT_SCREAM = reg("wroughtnaut.scream");

    public static final SoundEvent ENTITY_WROUGHT_GRUNT_1 = reg("wroughtnaut.grunt1");

    public static final SoundEvent ENTITY_WROUGHT_GRUNT_2 = reg("wroughtnaut.grunt2");

    public static final SoundEvent ENTITY_WROUGHT_AMBIENT = reg("wroughtnaut.ambient");

    // Barakoa

    public static final SoundEvent ENTITY_BARAKOA_INHALE = reg("barakoa.inhale");

    public static final SoundEvent ENTITY_BARAKOA_BLOWDART = reg("barakoa.blowdart");

    public static final SoundEvent ENTITY_BARAKOA_BATTLECRY = reg("barakoa.battlecry");

    public static final SoundEvent ENTITY_BARAKOA_BATTLECRY_2 = reg("barakoa.battlecry2");

    public static final SoundEvent ENTITY_BARAKOA_SWING = reg("barakoa.swing");

    public static final SoundEvent ENTITY_BARAKOA_EMERGE = reg("barakoa.emerge");

    public static final SoundEvent ENTITY_BARAKOA_HURT = reg("barakoa.hurt");

    public static final SoundEvent ENTITY_BARAKOA_DIE = reg("barakoa.die");

    public static final SoundEvent ENTITY_BARAKOA_SHOUT = reg("barakoa.shout");

    public static final SoundEvent ENTITY_BARAKOA_TALK_1 = reg("barakoa.talk1");

    public static final SoundEvent ENTITY_BARAKOA_TALK_2 = reg("barakoa.talk2");

    public static final SoundEvent ENTITY_BARAKOA_TALK_3 = reg("barakoa.talk3");

    public static final SoundEvent ENTITY_BARAKOA_TALK_4 = reg("barakoa.talk4");

    public static final SoundEvent ENTITY_BARAKOA_TALK_5 = reg("barakoa.talk5");

    public static final SoundEvent ENTITY_BARAKOA_TALK_6 = reg("barakoa.talk6");

    public static final SoundEvent ENTITY_BARAKOA_TALK_7 = reg("barakoa.talk7");

    public static final SoundEvent[] ENTITY_BARAKOA_TALK = {
        ENTITY_BARAKOA_TALK_1,
        ENTITY_BARAKOA_TALK_2,
        ENTITY_BARAKOA_TALK_3,
        ENTITY_BARAKOA_TALK_4,
        ENTITY_BARAKOA_TALK_5,
        ENTITY_BARAKOA_TALK_6,
        ENTITY_BARAKOA_TALK_7
    };

    public static final SoundEvent ENTITY_BARAKOA_ANGRY_1 = reg("barakoa.angry1");

    public static final SoundEvent ENTITY_BARAKOA_ANGRY_2 = reg("barakoa.angry2");

    public static final SoundEvent ENTITY_BARAKOA_ANGRY_3 = reg("barakoa.angry3");

    public static final SoundEvent ENTITY_BARAKOA_ANGRY_4 = reg("barakoa.angry4");

    public static final SoundEvent ENTITY_BARAKOA_ANGRY_5 = reg("barakoa.angry5");

    public static final SoundEvent ENTITY_BARAKOA_ANGRY_6 = reg("barakoa.angry6");

    public static final SoundEvent[] ENTITY_BARAKOA_ANGRY = {
        ENTITY_BARAKOA_ANGRY_1,
        ENTITY_BARAKOA_ANGRY_2,
        ENTITY_BARAKOA_ANGRY_3,
        ENTITY_BARAKOA_ANGRY_4,
        ENTITY_BARAKOA_ANGRY_5,
        ENTITY_BARAKOA_ANGRY_6
    };

    public static final SoundEvent ENTITY_BARAKO_BELLY = reg("barako.belly");

    public static final SoundEvent ENTITY_BARAKO_BURST = reg("barako.burst");

    public static final SoundEvent ENTITY_BARAKO_ATTACK = reg("barako.attack");

    public static final SoundEvent ENTITY_BARAKO_HURT = reg("barako.hurt");

    public static final SoundEvent ENTITY_BARAKO_DIE = reg("barako.die");

    public static final SoundEvent ENTITY_BARAKO_BLESS = reg("barako.bless");

    public static final SoundEvent ENTITY_BARAKO_TALK_1 = reg("barako.talk1");

    public static final SoundEvent ENTITY_BARAKO_TALK_2 = reg("barako.talk2");

    public static final SoundEvent ENTITY_BARAKO_TALK_3 = reg("barako.talk3");

    public static final SoundEvent ENTITY_BARAKO_TALK_4 = reg("barako.talk4");

    public static final SoundEvent ENTITY_BARAKO_TALK_5 = reg("barako.talk5");

    public static final SoundEvent ENTITY_BARAKO_TALK_6 = reg("barako.talk6");

    public static final SoundEvent[] ENTITY_BARAKO_TALK = {
        ENTITY_BARAKO_TALK_1,
        ENTITY_BARAKO_TALK_2,
        ENTITY_BARAKO_TALK_3,
        ENTITY_BARAKO_TALK_4,
        ENTITY_BARAKO_TALK_5,
        ENTITY_BARAKO_TALK_6
    };

    public static final SoundEvent ENTITY_BARAKO_ANGRY_1 = reg("barako.angry1");

    public static final SoundEvent ENTITY_BARAKO_ANGRY_2 = reg("barako.angry2");

    public static final SoundEvent ENTITY_BARAKO_ANGRY_3 = reg("barako.angry3");

    public static final SoundEvent ENTITY_BARAKO_ANGRY_4 = reg("barako.angry4");

    public static final SoundEvent ENTITY_BARAKO_ANGRY_5 = reg("barako.angry5");

    public static final SoundEvent ENTITY_BARAKO_ANGRY_6 = reg("barako.angry6");

    public static final SoundEvent[] ENTITY_BARAKO_ANGRY = {
        ENTITY_BARAKO_ANGRY_1,
        ENTITY_BARAKO_ANGRY_2,
        ENTITY_BARAKO_ANGRY_3,
        ENTITY_BARAKO_ANGRY_4,
        ENTITY_BARAKO_ANGRY_5,
        ENTITY_BARAKO_ANGRY_6
    };

    // Foliaath

    public static final SoundEvent ENTITY_FOLIAATH_GRUNT = reg("foliaath.grunt");

    public static final SoundEvent ENTITY_FOLIAATH_RUSTLE = reg("foliaath.rustle");

    public static final SoundEvent ENTITY_FOLIAATH_MERGE = reg("foliaath.emerge");

    public static final SoundEvent ENTITY_FOLIAATH_RETREAT = reg("foliaath.retreat");

    public static final SoundEvent ENTITY_FOLIAATH_PANT_1 = reg("foliaath.pant1");

    public static final SoundEvent ENTITY_FOLIAATH_PANT_2 = reg("foliaath.pant2");

    public static final SoundEvent ENTITY_FOLIAATH_BITE_1 = reg("foliaath.bite1");

    public static final SoundEvent ENTITY_FOLIAATH_HURT = reg("foliaath.hurt");

    public static final SoundEvent ENTITY_FOLIAATH_DIE = reg("foliaath.die");

    public static final SoundEvent ENTITY_FOLIAATH_BABY_EAT = reg("foliaath.baby.eat");

    public static final SoundEvent EFFECT_GEOMANCY_SMALL_CRASH = reg("geomancy.smallcrash");

    public static final SoundEvent EFFECT_GEOMANCY_MAGIC_SMALL = reg("geomancy.hitsmall");

    public static final SoundEvent EFFECT_GEOMANCY_MAGIC_BIG = reg("geomancy.hitbig");

    public static final SoundEvent EFFECT_GEOMANCY_BREAK_LARGE_1 = reg("geomancy.breaklarge");

    public static final SoundEvent EFFECT_GEOMANCY_BREAK_LARGE_2 = reg("geomancy.breaklarge2");

    public static final SoundEvent EFFECT_GEOMANCY_BREAK_MEDIUM_1 = reg("geomancy.breakmedium");

    public static final SoundEvent EFFECT_GEOMANCY_BREAK_MEDIUM_2 = reg("geomancy.breakmedium2");

    public static final SoundEvent EFFECT_GEOMANCY_BREAK_MEDIUM_3 = reg("geomancy.breakmedium3");

    public static final SoundEvent[] EFFECT_GEOMANCY_BREAK_MEDIUM = {
            EFFECT_GEOMANCY_BREAK_MEDIUM_1,
            EFFECT_GEOMANCY_BREAK_MEDIUM_2,
            EFFECT_GEOMANCY_BREAK_MEDIUM_3
    };

    public static final SoundEvent EFFECT_GEOMANCY_HIT = reg("geomancy.hit");

    public static final SoundEvent EFFECT_GEOMANCY_HIT_MEDIUM_1 = reg("geomancy.hitmedium");

    public static final SoundEvent EFFECT_GEOMANCY_HIT_MEDIUM_2 = reg("geomancy.hitmedium2");

    public static final SoundEvent[] EFFECT_GEOMANCY_HIT_MEDIUM = {
            EFFECT_GEOMANCY_HIT_MEDIUM_1,
            EFFECT_GEOMANCY_HIT_MEDIUM_2
    };

    public static final SoundEvent EFFECT_GEOMANCY_BREAK = reg("geomancy.rockbreak");

    public static final SoundEvent EFFECT_GEOMANCY_CRASH = reg("geomancy.rockcrash1");

    public static final SoundEvent EFFECT_GEOMANCY_CRUMBLE = reg("geomancy.rockcrumble");

    public static final SoundEvent EFFECT_GEOMANCY_RUMBLE_1 = reg("geomancy.rumble1");

    public static final SoundEvent EFFECT_GEOMANCY_RUMBLE_2 = reg("geomancy.rumble2");

    public static final SoundEvent EFFECT_GEOMANCY_RUMBLE_3 = reg("geomancy.rumble3");

    public static final SoundEvent EFFECT_GEOMANCY_HIT_SMALL = reg("geomancy.smallrockhit");

    public static final SoundEvent EFFECT_GEOMANCY_BOULDER_CHARGE = reg("geomancy.bouldercharge");

    public static final SoundEvent EFFECT_GEOMANCY_MAGIC_CHARGE_SMALL = reg("geomancy.magicchargesmall");

    public void onInit() {}

    private static SoundEvent reg(String name) {
        ResourceLocation id = new ResourceLocation(MowziesMobs.MODID, name);
        return GameRegistry.register(new SoundEvent(id).setRegistryName(id));
    }
}
