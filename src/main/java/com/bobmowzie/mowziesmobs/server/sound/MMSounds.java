package com.bobmowzie.mowziesmobs.server.sound;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.bobmowzie.mowziesmobs.MowziesMobs;

@Mod.EventBusSubscriber
public class MMSounds {
    // Generic
    public static SoundEvent LASER;
    public static SoundEvent SUNSTRIKE;

    // Wroughtnaut
    public static SoundEvent ENTITY_WROUGHT_PRE_SWING_1;
    public static SoundEvent ENTITY_WROUGHT_PRE_SWING_2;
    public static SoundEvent ENTITY_WROUGHT_CREAK;
    public static SoundEvent ENTITY_WROUGHT_SWING_1;
    public static SoundEvent ENTITY_WROUGHT_SWING_2;
    public static SoundEvent ENTITY_WROUGHT_SHOUT_2;
    public static SoundEvent ENTITY_WROUGHT_PULL_1;
    public static SoundEvent ENTITY_WROUGHT_PULL_5;
    public static SoundEvent ENTITY_WROUGHT_RELEASE_2;
    public static SoundEvent ENTITY_WROUGHT_WHOOSH;
    public static SoundEvent ENTITY_WROUGHT_HURT_1;
    public static SoundEvent ENTITY_WROUGHT_SCREAM;
    public static SoundEvent ENTITY_WROUGHT_GRUNT_1;
    public static SoundEvent ENTITY_WROUGHT_GRUNT_2;
    public static SoundEvent ENTITY_WROUGHT_AMBIENT;

    // Barakoa
    public static SoundEvent ENTITY_BARAKOA_INHALE;
    public static SoundEvent ENTITY_BARAKOA_BLOWDART;
    public static SoundEvent ENTITY_BARAKOA_BATTLECRY;
    public static SoundEvent ENTITY_BARAKOA_BATTLECRY_2;
    public static SoundEvent ENTITY_BARAKOA_SWING;
    public static SoundEvent ENTITY_BARAKOA_EMERGE;
    public static SoundEvent ENTITY_BARAKOA_HURT;
    public static SoundEvent ENTITY_BARAKOA_DIE;
    public static SoundEvent ENTITY_BARAKOA_SHOUT;
    public static SoundEvent ENTITY_BARAKOA_TALK_1;
    public static SoundEvent ENTITY_BARAKOA_TALK_2;
    public static SoundEvent ENTITY_BARAKOA_TALK_3;
    public static SoundEvent ENTITY_BARAKOA_TALK_4;
    public static SoundEvent ENTITY_BARAKOA_TALK_5;
    public static SoundEvent ENTITY_BARAKOA_TALK_6;
    public static SoundEvent ENTITY_BARAKOA_TALK_7;
    public static SoundEvent[] ENTITY_BARAKOA_TALK = {
		ENTITY_BARAKOA_TALK_1,
		ENTITY_BARAKOA_TALK_2,
		ENTITY_BARAKOA_TALK_3,
		ENTITY_BARAKOA_TALK_4,
		ENTITY_BARAKOA_TALK_5,
		ENTITY_BARAKOA_TALK_6, 		
		ENTITY_BARAKOA_TALK_7    		
    };
    public static SoundEvent ENTITY_BARAKOA_ANGRY_1;
    public static SoundEvent ENTITY_BARAKOA_ANGRY_2;
    public static SoundEvent ENTITY_BARAKOA_ANGRY_3;
    public static SoundEvent ENTITY_BARAKOA_ANGRY_4;
    public static SoundEvent ENTITY_BARAKOA_ANGRY_5;
    public static SoundEvent ENTITY_BARAKOA_ANGRY_6;
    public static SoundEvent[] ENTITY_BARAKOA_ANGRY = {
        ENTITY_BARAKOA_ANGRY_1,
        ENTITY_BARAKOA_ANGRY_2,
        ENTITY_BARAKOA_ANGRY_3,
        ENTITY_BARAKOA_ANGRY_4,
        ENTITY_BARAKOA_ANGRY_5,
        ENTITY_BARAKOA_ANGRY_6
    };

    public static SoundEvent ENTITY_BARAKO_BELLY;
    public static SoundEvent ENTITY_BARAKO_BURST;
    public static SoundEvent ENTITY_BARAKO_ATTACK;
    public static SoundEvent ENTITY_BARAKO_HURT;
    public static SoundEvent ENTITY_BARAKO_DIE;
    public static SoundEvent ENTITY_BARAKO_BLESS;
    public static SoundEvent ENTITY_BARAKO_TALK_1;
    public static SoundEvent ENTITY_BARAKO_TALK_2;
    public static SoundEvent ENTITY_BARAKO_TALK_3;
    public static SoundEvent ENTITY_BARAKO_TALK_4;
    public static SoundEvent ENTITY_BARAKO_TALK_5;
    public static SoundEvent ENTITY_BARAKO_TALK_6;
    public static SoundEvent[] ENTITY_BARAKO_TALK = {
        ENTITY_BARAKO_TALK_1,
        ENTITY_BARAKO_TALK_2,
        ENTITY_BARAKO_TALK_3,
        ENTITY_BARAKO_TALK_4,
        ENTITY_BARAKO_TALK_5,
        ENTITY_BARAKO_TALK_6
    };
    public static SoundEvent ENTITY_BARAKO_ANGRY_1;
    public static SoundEvent ENTITY_BARAKO_ANGRY_2;
    public static SoundEvent ENTITY_BARAKO_ANGRY_3;
    public static SoundEvent ENTITY_BARAKO_ANGRY_4;
    public static SoundEvent ENTITY_BARAKO_ANGRY_5;
    public static SoundEvent ENTITY_BARAKO_ANGRY_6;
    public static SoundEvent[] ENTITY_BARAKO_ANGRY = {
        ENTITY_BARAKO_ANGRY_1,
        ENTITY_BARAKO_ANGRY_2,
        ENTITY_BARAKO_ANGRY_3,
        ENTITY_BARAKO_ANGRY_4,
        ENTITY_BARAKO_ANGRY_5,
        ENTITY_BARAKO_ANGRY_6
    };

    // Foliaath
    public static SoundEvent ENTITY_FOLIAATH_GRUNT;
    public static SoundEvent ENTITY_FOLIAATH_RUSTLE;
    public static SoundEvent ENTITY_FOLIAATH_MERGE;
    public static SoundEvent ENTITY_FOLIAATH_RETREAT;
    public static SoundEvent ENTITY_FOLIAATH_PANT_1;
    public static SoundEvent ENTITY_FOLIAATH_PANT_2;
    public static SoundEvent ENTITY_FOLIAATH_BITE_1;
    public static SoundEvent ENTITY_FOLIAATH_HURT;
    public static SoundEvent ENTITY_FOLIAATH_DIE;
    public static SoundEvent ENTITY_FOLIAATH_BABY_EAT;

    public static SoundEvent ENTITY_FROSTMAW_ROAR;
    public static SoundEvent ENTITY_FROSTMAW_DIE;
    public static SoundEvent ENTITY_FROSTMAW_WHOOSH;
    public static SoundEvent ENTITY_FROSTMAW_ICEBREATH;
    public static SoundEvent ENTITY_FROSTMAW_ICEBREATH_START;
    public static SoundEvent ENTITY_FROSTMAW_FROZEN_CRASH;
    public static SoundEvent ENTITY_FROSTMAW_STEP;
    public static SoundEvent ENTITY_FROSTMAW_LAND;
    public static SoundEvent ENTITY_FROSTMAW_ATTACK_1;
    public static SoundEvent ENTITY_FROSTMAW_ATTACK_2;
    public static SoundEvent ENTITY_FROSTMAW_ATTACK_3;
    public static SoundEvent ENTITY_FROSTMAW_ATTACK_4;
    public static SoundEvent[] ENTITY_FROSTMAW_ATTACK = {
            ENTITY_FROSTMAW_ATTACK_1,
            ENTITY_FROSTMAW_ATTACK_2,
            ENTITY_FROSTMAW_ATTACK_3,
            ENTITY_FROSTMAW_ATTACK_4
    };
    public static SoundEvent ENTITY_FROSTMAW_BREATH_1;
    public static SoundEvent ENTITY_FROSTMAW_BREATH_2;
    public static SoundEvent[] ENTITY_FROSTMAW_BREATH = {
            ENTITY_FROSTMAW_BREATH_1,
            ENTITY_FROSTMAW_BREATH_2
    };
    public static SoundEvent ENTITY_FROSTMAW_LIVING_1;
    public static SoundEvent ENTITY_FROSTMAW_LIVING_2;
    public static SoundEvent[] ENTITY_FROSTMAW_LIVING = {
            ENTITY_FROSTMAW_LIVING_1,
            ENTITY_FROSTMAW_LIVING_2
    };
    public static SoundEvent ENTITY_FROSTMAW_WAKEUP;

    public static SoundEvent EFFECT_GEOMANCY_SMALL_CRASH;
    public static SoundEvent EFFECT_GEOMANCY_MAGIC_SMALL;
    public static SoundEvent EFFECT_GEOMANCY_MAGIC_BIG;
    public static SoundEvent EFFECT_GEOMANCY_BREAK_LARGE_1;
    public static SoundEvent EFFECT_GEOMANCY_BREAK_LARGE_2;
    public static SoundEvent EFFECT_GEOMANCY_BREAK_MEDIUM_1;
    public static SoundEvent EFFECT_GEOMANCY_BREAK_MEDIUM_2;
    public static SoundEvent EFFECT_GEOMANCY_BREAK_MEDIUM_3;
    public static SoundEvent[] EFFECT_GEOMANCY_BREAK_MEDIUM = {
            EFFECT_GEOMANCY_BREAK_MEDIUM_1,
            EFFECT_GEOMANCY_BREAK_MEDIUM_2,
            EFFECT_GEOMANCY_BREAK_MEDIUM_3
    };
    public static SoundEvent EFFECT_GEOMANCY_HIT;
    public static SoundEvent EFFECT_GEOMANCY_HIT_MEDIUM_1;
    public static SoundEvent EFFECT_GEOMANCY_HIT_MEDIUM_2;
    public static SoundEvent[] EFFECT_GEOMANCY_HIT_MEDIUM = {
            EFFECT_GEOMANCY_HIT_MEDIUM_1,
            EFFECT_GEOMANCY_HIT_MEDIUM_2
    };
    public static SoundEvent EFFECT_GEOMANCY_BREAK;
    public static SoundEvent EFFECT_GEOMANCY_CRASH;
    public static SoundEvent EFFECT_GEOMANCY_CRUMBLE;
    public static SoundEvent EFFECT_GEOMANCY_RUMBLE_1;
    public static SoundEvent EFFECT_GEOMANCY_RUMBLE_2;
    public static SoundEvent EFFECT_GEOMANCY_RUMBLE_3;
    public static SoundEvent[] EFFECT_GEOMANCY_RUMBLE = {
            EFFECT_GEOMANCY_RUMBLE_1,
            EFFECT_GEOMANCY_RUMBLE_2,
            EFFECT_GEOMANCY_RUMBLE_3
    };
    public static SoundEvent EFFECT_GEOMANCY_HIT_SMALL;
    public static SoundEvent EFFECT_GEOMANCY_BOULDER_CHARGE;
    public static SoundEvent EFFECT_GEOMANCY_MAGIC_CHARGE_SMALL;

    @SubscribeEvent
    public static void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {  	
        LASER = registerSound(event, "laser");
        SUNSTRIKE = registerSound(event, "sunstrike");

        // Wroughtnaut
        ENTITY_WROUGHT_PRE_SWING_1 = registerSound(event, "wroughtnaut.preSwing1");
        ENTITY_WROUGHT_PRE_SWING_2 = registerSound(event, "wroughtnaut.preSwing2");
        ENTITY_WROUGHT_CREAK = registerSound(event, "wroughtnaut.creak");
        ENTITY_WROUGHT_SWING_1 = registerSound(event, "wroughtnaut.swing1");
        ENTITY_WROUGHT_SWING_2 = registerSound(event, "wroughtnaut.swing2");
        ENTITY_WROUGHT_SHOUT_2 = registerSound(event, "wroughtnaut.shout2");
        ENTITY_WROUGHT_PULL_1 = registerSound(event, "wroughtnaut.pull1");
        ENTITY_WROUGHT_PULL_5 = registerSound(event, "wroughtnaut.pull5");
        ENTITY_WROUGHT_RELEASE_2 = registerSound(event, "wroughtnaut.release2");
        ENTITY_WROUGHT_WHOOSH = registerSound(event, "wroughtnaut.whoosh");
        ENTITY_WROUGHT_HURT_1 = registerSound(event, "wroughtnaut.hurt1");
        ENTITY_WROUGHT_SCREAM = registerSound(event, "wroughtnaut.scream");
        ENTITY_WROUGHT_GRUNT_1 = registerSound(event, "wroughtnaut.grunt1");
        ENTITY_WROUGHT_GRUNT_2 = registerSound(event, "wroughtnaut.grunt2");
        ENTITY_WROUGHT_AMBIENT = registerSound(event, "wroughtnaut.ambient");

        // Barakoa
        ENTITY_BARAKOA_INHALE = registerSound(event, "barakoa.inhale");
        ENTITY_BARAKOA_BLOWDART = registerSound(event, "barakoa.blowdart");
        ENTITY_BARAKOA_BATTLECRY = registerSound(event, "barakoa.battlecry");
        ENTITY_BARAKOA_BATTLECRY_2 = registerSound(event, "barakoa.battlecry2");
        ENTITY_BARAKOA_SWING = registerSound(event, "barakoa.swing");
        ENTITY_BARAKOA_EMERGE = registerSound(event, "barakoa.emerge");
        ENTITY_BARAKOA_HURT = registerSound(event, "barakoa.hurt");
        ENTITY_BARAKOA_DIE = registerSound(event, "barakoa.die");
        ENTITY_BARAKOA_SHOUT = registerSound(event, "barakoa.shout");
        ENTITY_BARAKOA_TALK_1 = registerSound(event, "barakoa.talk1");
        ENTITY_BARAKOA_TALK_2 = registerSound(event, "barakoa.talk2");
        ENTITY_BARAKOA_TALK_3 = registerSound(event, "barakoa.talk3");
        ENTITY_BARAKOA_TALK_4 = registerSound(event, "barakoa.talk4");
        ENTITY_BARAKOA_TALK_5 = registerSound(event, "barakoa.talk5");
        ENTITY_BARAKOA_TALK_6 = registerSound(event, "barakoa.talk6");
        ENTITY_BARAKOA_TALK_7 = registerSound(event, "barakoa.talk7");
        ENTITY_BARAKOA_ANGRY_1 = registerSound(event, "barakoa.angry1");
        ENTITY_BARAKOA_ANGRY_2 = registerSound(event, "barakoa.angry2");
        ENTITY_BARAKOA_ANGRY_3 = registerSound(event, "barakoa.angry3");
        ENTITY_BARAKOA_ANGRY_4 = registerSound(event, "barakoa.angry4");
        ENTITY_BARAKOA_ANGRY_5 = registerSound(event, "barakoa.angry5");
        ENTITY_BARAKOA_ANGRY_6 = registerSound(event, "barakoa.angry6");

        ENTITY_BARAKO_BELLY = registerSound(event, "barako.belly");
        ENTITY_BARAKO_BURST = registerSound(event, "barako.burst");
        ENTITY_BARAKO_ATTACK = registerSound(event, "barako.attack");
        ENTITY_BARAKO_HURT = registerSound(event, "barako.hurt");
        ENTITY_BARAKO_DIE = registerSound(event, "barako.die");
        ENTITY_BARAKO_BLESS = registerSound(event, "barako.bless");
        ENTITY_BARAKO_TALK_1 = registerSound(event, "barako.talk1");
        ENTITY_BARAKO_TALK_2 = registerSound(event, "barako.talk2");
        ENTITY_BARAKO_TALK_3 = registerSound(event, "barako.talk3");
        ENTITY_BARAKO_TALK_4 = registerSound(event, "barako.talk4");
        ENTITY_BARAKO_TALK_5 = registerSound(event, "barako.talk5");
        ENTITY_BARAKO_TALK_6 = registerSound(event, "barako.talk6");
        ENTITY_BARAKO_ANGRY_1 = registerSound(event, "barako.angry1");
        ENTITY_BARAKO_ANGRY_2 = registerSound(event, "barako.angry2");
        ENTITY_BARAKO_ANGRY_3 = registerSound(event, "barako.angry3");
        ENTITY_BARAKO_ANGRY_4 = registerSound(event, "barako.angry4");
        ENTITY_BARAKO_ANGRY_5 = registerSound(event, "barako.angry5");
        ENTITY_BARAKO_ANGRY_6 = registerSound(event, "barako.angry6");

        // Foliaath
        ENTITY_FOLIAATH_GRUNT = registerSound(event, "foliaath.grunt");
        ENTITY_FOLIAATH_RUSTLE = registerSound(event, "foliaath.rustle");
        ENTITY_FOLIAATH_MERGE = registerSound(event, "foliaath.emerge");
        ENTITY_FOLIAATH_RETREAT = registerSound(event, "foliaath.retreat");
        ENTITY_FOLIAATH_PANT_1 = registerSound(event, "foliaath.pant1");
        ENTITY_FOLIAATH_PANT_2 = registerSound(event, "foliaath.pant2");
        ENTITY_FOLIAATH_BITE_1 = registerSound(event, "foliaath.bite1");
        ENTITY_FOLIAATH_HURT = registerSound(event, "foliaath.hurt");
        ENTITY_FOLIAATH_DIE = registerSound(event, "foliaath.die");
        ENTITY_FOLIAATH_BABY_EAT = registerSound(event, "foliaath.baby.eat");

        ENTITY_FROSTMAW_ROAR = registerSound(event, "frostmaw.roar");
        ENTITY_FROSTMAW_DIE = registerSound(event, "frostmaw.die");
        ENTITY_FROSTMAW_WHOOSH = registerSound(event, "frostmaw.whoosh");
        ENTITY_FROSTMAW_ICEBREATH = registerSound(event, "frostmaw.icebreath");
        ENTITY_FROSTMAW_ICEBREATH_START = registerSound(event, "frostmaw.icebreathstart");
        ENTITY_FROSTMAW_FROZEN_CRASH = registerSound(event, "frostmaw.frozencrash");
        ENTITY_FROSTMAW_STEP = registerSound(event, "frostmaw.step");
        ENTITY_FROSTMAW_LAND = registerSound(event, "frostmaw.land");
        ENTITY_FROSTMAW_ATTACK_1 = registerSound(event, "frostmaw.attack1");
        ENTITY_FROSTMAW_ATTACK_2 = registerSound(event, "frostmaw.attack2");
        ENTITY_FROSTMAW_ATTACK_3 = registerSound(event, "frostmaw.attack3");
        ENTITY_FROSTMAW_ATTACK_4 = registerSound(event, "frostmaw.attack4");
        ENTITY_FROSTMAW_BREATH_1 = registerSound(event, "frostmaw.breath1");
        ENTITY_FROSTMAW_BREATH_2 = registerSound(event, "frostmaw.breath2");
        ENTITY_FROSTMAW_LIVING_1 = registerSound(event, "frostmaw.living1");
        ENTITY_FROSTMAW_LIVING_2 = registerSound(event, "frostmaw.living2");
        ENTITY_FROSTMAW_WAKEUP = registerSound(event, "frostmaw.wakeup");

        EFFECT_GEOMANCY_SMALL_CRASH = registerSound(event, "geomancy.smallcrash");
        EFFECT_GEOMANCY_MAGIC_SMALL = registerSound(event, "geomancy.hitsmall");
        EFFECT_GEOMANCY_MAGIC_BIG = registerSound(event, "geomancy.hitbig");
        EFFECT_GEOMANCY_BREAK_LARGE_1 = registerSound(event, "geomancy.breaklarge");
        EFFECT_GEOMANCY_BREAK_LARGE_2 = registerSound(event, "geomancy.breaklarge2");
        EFFECT_GEOMANCY_BREAK_MEDIUM_1 = registerSound(event, "geomancy.breakmedium");
        EFFECT_GEOMANCY_BREAK_MEDIUM_2 = registerSound(event, "geomancy.breakmedium2");
        EFFECT_GEOMANCY_BREAK_MEDIUM_3 = registerSound(event, "geomancy.breakmedium3");
        EFFECT_GEOMANCY_HIT = registerSound(event, "geomancy.hit");
        EFFECT_GEOMANCY_HIT_MEDIUM_1 = registerSound(event, "geomancy.hitmedium");
        EFFECT_GEOMANCY_HIT_MEDIUM_2 = registerSound(event, "geomancy.hitmedium2");
        EFFECT_GEOMANCY_BREAK = registerSound(event, "geomancy.rockbreak");
        EFFECT_GEOMANCY_CRASH = registerSound(event, "geomancy.rockcrash1");
        EFFECT_GEOMANCY_CRUMBLE = registerSound(event, "geomancy.rockcrumble");
        EFFECT_GEOMANCY_RUMBLE_1 = registerSound(event, "geomancy.rumble1");
        EFFECT_GEOMANCY_RUMBLE_2 = registerSound(event, "geomancy.rumble2");
        EFFECT_GEOMANCY_RUMBLE_3 = registerSound(event, "geomancy.rumble3");
        EFFECT_GEOMANCY_HIT_SMALL = registerSound(event, "geomancy.smallrockhit");
        EFFECT_GEOMANCY_BOULDER_CHARGE = registerSound(event, "geomancy.bouldercharge");
        EFFECT_GEOMANCY_MAGIC_CHARGE_SMALL = registerSound(event, "geomancy.magicchargesmall");
    }

    private static SoundEvent registerSound(RegistryEvent.Register<SoundEvent> event, String name) {
        ResourceLocation id = new ResourceLocation(MowziesMobs.MODID, name);
        event.getRegistry().register(new SoundEvent(id).setRegistryName(id));
        SoundEvent soundEvent = SoundEvent.REGISTRY.getObject(id);       
        if (soundEvent == null) {
            throw new IllegalStateException("Invalid Sound requested: " + id);
        } else {
            return soundEvent;
        }
    }
}
