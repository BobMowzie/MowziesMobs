package com.bobmowzie.mowziesmobs.server.ability;

public abstract class AbilitySection {
    public enum AbilitySectionType {
        STARTUP,
        ACTIVE,
        ENDLAG,
        MISC
    }
    public final AbilitySectionType sectionType;

    protected AbilitySection(AbilitySectionType sectionType) {
        this.sectionType = sectionType;
    }

    public static class AbilitySectionInstant extends AbilitySection {
        public AbilitySectionInstant(AbilitySectionType sectionType) {
            super(sectionType);
        }
    }

    public static class AbilitySectionDuration extends AbilitySection {
        public final int duration;
        public AbilitySectionDuration(AbilitySectionType sectionType, int duration) {
            super(sectionType);
            this.duration = duration;
        }
    }

    public static class AbilitySectionInfinite extends AbilitySection {
        public AbilitySectionInfinite(AbilitySectionType sectionType) {
            super(sectionType);
        }
    }
}
