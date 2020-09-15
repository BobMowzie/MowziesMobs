package com.ilexiconn.llibrary.server.nbt.parser;

import net.minecraft.nbt.NBTBase;

/**
 * @author iLexiconn
 * @since 1.1.0
 */
public interface INBTParser<V, T extends NBTBase> {
    V parseTag(T tag);

    T parseValue(V value);
}
