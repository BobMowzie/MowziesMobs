package com.ilexiconn.llibrary.server.nbt.parser;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.BlockPos;

/**
 * @author iLexiconn
 * @since 1.1.0
 */
public enum NBTParsers implements INBTParser {
    BOOLEAN {
        @Override
        public Object parseTag(NBTBase tag) {
            return ((NBTTagByte) tag).getByte() != 0;
        }

        @Override
        public NBTBase parseValue(Object value) {
            return new NBTTagByte((byte) ((Boolean) value ? 1 : 0));
        }
    },

    BYTE {
        @Override
        public Object parseTag(NBTBase tag) {
            return ((NBTTagByte) tag).getByte();
        }

        @Override
        public NBTBase parseValue(Object value) {
            return new NBTTagByte((Byte) value);
        }
    },

    CHAR {
        @Override
        public Object parseTag(NBTBase tag) {
            return (char) ((NBTTagShort) tag).getShort();
        }

        @Override
        public NBTBase parseValue(Object value) {
            return new NBTTagShort((short) ((Character) value).charValue());
        }
    },

    SHORT {
        @Override
        public Object parseTag(NBTBase tag) {
            return ((NBTTagShort) tag).getShort();
        }

        @Override
        public NBTBase parseValue(Object value) {
            return new NBTTagShort((Short) value);
        }
    },

    INTEGER {
        @Override
        public Object parseTag(NBTBase tag) {
            return ((NBTTagInt) tag).getInt();
        }

        @Override
        public NBTBase parseValue(Object value) {
            return new NBTTagInt((Integer) value);
        }
    },

    FLOAT {
        @Override
        public Object parseTag(NBTBase tag) {
            return ((NBTTagFloat) tag).getFloat();
        }

        @Override
        public NBTBase parseValue(Object value) {
            return new NBTTagFloat((Float) value);
        }
    },

    LONG {
        @Override
        public Object parseTag(NBTBase tag) {
            return ((NBTTagLong) tag).getLong();
        }

        @Override
        public NBTBase parseValue(Object value) {
            return new NBTTagLong((Long) value);
        }
    },

    DOUBLE {
        @Override
        public Object parseTag(NBTBase tag) {
            return ((NBTTagDouble) tag).getDouble();
        }

        @Override
        public NBTBase parseValue(Object value) {
            return new NBTTagDouble((Double) value);
        }
    },

    STRING {
        @Override
        public Object parseTag(NBTBase tag) {
            return ((NBTTagString) tag).getString();
        }

        @Override
        public NBTBase parseValue(Object value) {
            return new NBTTagString((String) value);
        }
    },

    BOOLEAN_ARRAY {
        @Override
        public Object parseTag(NBTBase tag) {
            NBTTagCompound list = (NBTTagCompound) tag;
            Boolean[] boolArray = new Boolean[list.getInteger("length")];
            byte[] boolList = list.getByteArray("array");
            for (int i = 0; i < boolArray.length; i++) {
                boolArray[i] = ((boolList[i / 8] >>> (i % 8)) & 1) != 0;
            }
            return boolArray;
        }

        @Override
        public NBTBase parseValue(Object value) {
            Boolean[] valueBoolArray = (Boolean[]) value;
            byte[] valueByteArray = new byte[(valueBoolArray.length + 7) / 8];
            for (int i = 0; i < valueBoolArray.length; i++) {
                valueByteArray[i / 8] |= (byte) ((valueBoolArray[i] ? 1 : 0) << (i % 8));
            }
            NBTTagCompound byteArrayCompound = new NBTTagCompound();
            byteArrayCompound.setInteger("length", valueBoolArray.length);
            byteArrayCompound.setByteArray("array", valueByteArray);
            return byteArrayCompound;
        }
    },

    BOOLEAN_ARRAY_PRIM {
        @Override
        public Object parseTag(NBTBase tag) {
            NBTTagCompound list = (NBTTagCompound) tag;
            boolean[] boolArray = new boolean[list.getInteger("length")];
            byte[] boolList = list.getByteArray("array");
            for (int i = 0; i < boolArray.length; i++) {
                boolArray[i] = ((boolList[i / 8] >>> (i % 8)) & 1) != 0;
            }
            return boolArray;
        }

        @Override
        public NBTBase parseValue(Object value) {
            boolean[] valueBoolArray = (boolean[]) value;
            byte[] valueByteArray = new byte[(valueBoolArray.length + 7) / 8];
            for (int i = 0; i < valueBoolArray.length; i++) {
                valueByteArray[i / 8] |= (byte) ((valueBoolArray[i] ? 1 : 0) << (i % 8));
            }
            NBTTagCompound byteArrayCompound = new NBTTagCompound();
            byteArrayCompound.setInteger("length", valueBoolArray.length);
            byteArrayCompound.setByteArray("array", valueByteArray);
            return byteArrayCompound;
        }
    },

    BYTE_ARRAY {
        @Override
        public Object parseTag(NBTBase tag) {
            byte[] bytePrimArray = ((NBTTagByteArray) tag).getByteArray();
            Byte[] byteArray = new Byte[bytePrimArray.length];
            for (int i = 0; i < byteArray.length; i++) {
                byteArray[i] = bytePrimArray[i];
            }
            return byteArray;
        }

        @Override
        public NBTBase parseValue(Object value) {
            Byte[] valueByteArray = (Byte[]) value;
            byte[] valuePrimByteArray = new byte[valueByteArray.length];
            for (int i = 0; i < valueByteArray.length; i++) {
                valuePrimByteArray[i] = valueByteArray[i];
            }
            return new NBTTagByteArray(valuePrimByteArray);
        }
    },

    BYTE_ARRAY_PRIM {
        @Override
        public Object parseTag(NBTBase tag) {
            return ((NBTTagByteArray) tag).getByteArray();
        }

        @Override
        public NBTBase parseValue(Object value) {
            return new NBTTagByteArray((byte[]) value);
        }
    },

    CHAR_ARRAY {
        @Override
        public Object parseTag(NBTBase tag) {
            NBTTagList charList = (NBTTagList) tag;
            Character[] charArray = new Character[charList.tagCount()];
            for (int i = 0; i < charArray.length; i++) {
                charArray[i] = (char) ((NBTTagShort) charList.get(i)).getShort();
            }
            return charArray;
        }

        @Override
        public NBTBase parseValue(Object value) {
            Character[] valueCharArray = (Character[]) value;
            NBTTagList list = new NBTTagList();
            for (Character aValueCharArray : valueCharArray) {
                list.appendTag(new NBTTagShort((short) aValueCharArray.charValue()));
            }
            return list;
        }
    },

    CHAR_ARRAY_PRIM {
        @Override
        public Object parseTag(NBTBase tag) {
            NBTTagList charList = (NBTTagList) tag;
            char[] charArray = new char[charList.tagCount()];
            for (int i = 0; i < charArray.length; i++) {
                charArray[i] = (char) ((NBTTagShort) charList.get(i)).getShort();
            }
            return charArray;
        }

        @Override
        public NBTBase parseValue(Object value) {
            char[] valueCharArray = (char[]) value;
            NBTTagList list = new NBTTagList();
            for (Character aValueCharArray : valueCharArray) {
                list.appendTag(new NBTTagShort((short) aValueCharArray.charValue()));
            }
            return list;
        }
    },

    SHORT_ARRAY {
        @Override
        public Object parseTag(NBTBase tag) {
            NBTTagList charList = (NBTTagList) tag;
            Short[] charArray = new Short[charList.tagCount()];
            for (int i = 0; i < charArray.length; i++) {
                charArray[i] = ((NBTTagShort) charList.get(i)).getShort();
            }
            return charArray;
        }

        @Override
        public NBTBase parseValue(Object value) {
            Short[] valueShortArray = (Short[]) value;
            NBTTagList list = new NBTTagList();
            for (Short aValueShortArray : valueShortArray) {
                list.appendTag(new NBTTagShort(aValueShortArray));
            }
            return list;
        }
    },

    SHORT_ARRAY_PRIM {
        @Override
        public Object parseTag(NBTBase tag) {
            NBTTagList charList = (NBTTagList) tag;
            short[] charArray = new short[charList.tagCount()];
            for (int i = 0; i < charArray.length; i++) {
                charArray[i] = ((NBTTagShort) charList.get(i)).getShort();
            }
            return charArray;
        }

        @Override
        public NBTBase parseValue(Object value) {
            short[] valueShortArray = (short[]) value;
            NBTTagList list = new NBTTagList();
            for (Short aValueShortArray : valueShortArray) {
                list.appendTag(new NBTTagShort(aValueShortArray));
            }
            return list;
        }
    },

    INTEGER_ARRAY {
        @Override
        public Object parseTag(NBTBase tag) {
            NBTTagIntArray intList = (NBTTagIntArray) tag;
            int[] intPrimArray = intList.getIntArray();
            Integer[] intArray = new Integer[intPrimArray.length];
            for (int i = 0; i < intArray.length; i++) {
                intArray[i] = intPrimArray[i];
            }
            return intArray;
        }

        @Override
        public NBTBase parseValue(Object value) {
            Integer[] valueIntArray = (Integer[]) value;
            int[] valuePrimIntArray = new int[valueIntArray.length];
            for (int i = 0; i < valueIntArray.length; i++) {
                valuePrimIntArray[i] = valueIntArray[i];
            }
            return new NBTTagIntArray(valuePrimIntArray);
        }
    },

    INTEGER_ARRAY_PRIM {
        @Override
        public Object parseTag(NBTBase tag) {
            return ((NBTTagIntArray) tag).getIntArray();
        }

        @Override
        public NBTBase parseValue(Object value) {
            return new NBTTagIntArray((int[]) value);
        }
    },

    FLOAT_ARRAY {
        @Override
        public Object parseTag(NBTBase tag) {
            NBTTagList floatList = (NBTTagList) tag;
            Float[] floatArray = new Float[floatList.tagCount()];
            for (int i = 0; i < floatArray.length; i++) {
                floatArray[i] = ((NBTTagFloat) floatList.get(i)).getFloat();
            }
            return floatArray;
        }

        @Override
        public NBTBase parseValue(Object value) {
            Float[] valueFloatArray = (Float[]) value;
            NBTTagList list = new NBTTagList();
            for (Float aValueFloatArray : valueFloatArray) {
                list.appendTag(new NBTTagFloat(aValueFloatArray));
            }
            return list;
        }
    },

    FLOAT_ARRAY_PRIM {
        @Override
        public Object parseTag(NBTBase tag) {
            NBTTagList floatList = (NBTTagList) tag;
            float[] floatArray = new float[floatList.tagCount()];
            for (int i = 0; i < floatArray.length; i++) {
                floatArray[i] = ((NBTTagFloat) floatList.get(i)).getFloat();
            }
            return floatArray;
        }

        @Override
        public NBTBase parseValue(Object value) {
            float[] valueFloatArray = (float[]) value;
            NBTTagList list = new NBTTagList();
            for (float aValueFloatArray : valueFloatArray) {
                list.appendTag(new NBTTagFloat(aValueFloatArray));
            }
            return list;
        }
    },

    LONG_ARRAY {
        @Override
        public Object parseTag(NBTBase tag) {
            NBTTagList longList = (NBTTagList) tag;
            Long[] longArray = new Long[longList.tagCount()];
            for (int i = 0; i < longArray.length; i++) {
                longArray[i] = ((NBTTagLong) longList.get(i)).getLong();
            }
            return longArray;
        }

        @Override
        public NBTBase parseValue(Object value) {
            Long[] valueLongArray = (Long[]) value;
            NBTTagList list = new NBTTagList();
            for (Long aValueLongArray : valueLongArray) {
                list.appendTag(new NBTTagLong(aValueLongArray));
            }
            return list;
        }
    },

    LONG_ARRAY_PRIM {
        @Override
        public Object parseTag(NBTBase tag) {
            NBTTagList longList = (NBTTagList) tag;
            long[] longArray = new long[longList.tagCount()];
            for (int i = 0; i < longArray.length; i++) {
                longArray[i] = ((NBTTagLong) longList.get(i)).getLong();
            }
            return longArray;
        }

        @Override
        public NBTBase parseValue(Object value) {
            long[] valueLongArray = (long[]) value;
            NBTTagList list = new NBTTagList();
            for (long aValueLongArray : valueLongArray) {
                list.appendTag(new NBTTagLong(aValueLongArray));
            }
            return list;
        }
    },

    DOUBLE_ARRAY {
        @Override
        public Object parseTag(NBTBase tag) {
            NBTTagList doubleList = (NBTTagList) tag;
            Double[] doubleArray = new Double[doubleList.tagCount()];
            for (int i = 0; i < doubleArray.length; i++) {
                doubleArray[i] = ((NBTTagDouble) doubleList.get(i)).getDouble();
            }
            return doubleArray;
        }

        @Override
        public NBTBase parseValue(Object value) {
            Double[] valueDoubleArray = (Double[]) value;
            NBTTagList list = new NBTTagList();
            for (Double aValueDoubleArray : valueDoubleArray) {
                list.appendTag(new NBTTagDouble(aValueDoubleArray));
            }
            return list;
        }
    },

    DOUBLE_ARRAY_PRIM {
        @Override
        public Object parseTag(NBTBase tag) {
            NBTTagList doubleList = (NBTTagList) tag;
            double[] doubleArray = new double[doubleList.tagCount()];
            for (int i = 0; i < doubleArray.length; i++) {
                doubleArray[i] = ((NBTTagDouble) doubleList.get(i)).getDouble();
            }
            return doubleArray;
        }

        @Override
        public NBTBase parseValue(Object value) {
            double[] valueDoubleArray = (double[]) value;
            NBTTagList list = new NBTTagList();
            for (double aValueDoubleArray : valueDoubleArray) {
                list.appendTag(new NBTTagDouble(aValueDoubleArray));
            }
            return list;
        }
    },

    STRING_ARRAY {
        @Override
        public Object parseTag(NBTBase tag) {
            NBTTagList stringList = (NBTTagList) tag;
            String[] stringArray = new String[stringList.tagCount()];
            for (int i = 0; i < stringArray.length; i++) {
                stringArray[i] = ((NBTTagString) stringList.get(i)).getString();
            }
            return stringArray;
        }

        @Override
        public NBTBase parseValue(Object value) {
            String[] valueStringArray = (String[]) value;
            NBTTagList list = new NBTTagList();
            for (String aValueStringArray : valueStringArray) {
                list.appendTag(new NBTTagString(aValueStringArray));
            }
            return list;
        }
    },

    ITEM_STACK {
        @Override
        public Object parseTag(NBTBase tag) {
            return new ItemStack((NBTTagCompound) tag);
        }

        @Override
        public NBTBase parseValue(Object value) {
            NBTTagCompound itemStackCompound = new NBTTagCompound();
            ((ItemStack) value).writeToNBT(itemStackCompound);
            return itemStackCompound;
        }
    },

    BLOCK_POS {
        @Override
        public Object parseTag(NBTBase tag) {
            NBTTagCompound blockPosCompound = (NBTTagCompound) tag;
            return new BlockPos(blockPosCompound.getInteger("x"), blockPosCompound.getInteger("y"), blockPosCompound.getInteger("z"));
        }

        @Override
        public NBTBase parseValue(Object value) {
            NBTTagCompound blockPosCompound = new NBTTagCompound();
            blockPosCompound.setInteger("x", ((BlockPos) value).getX());
            blockPosCompound.setInteger("y", ((BlockPos) value).getY());
            blockPosCompound.setInteger("z", ((BlockPos) value).getZ());
            return blockPosCompound;
        }
    };

    public static <V, T extends NBTBase> INBTParser<V, T> getBuiltinParser(Class<V> type) {
        if (Boolean.class.isAssignableFrom(type) || boolean.class.isAssignableFrom(type)) {
            return BOOLEAN;
        } else if (Byte.class.isAssignableFrom(type) || byte.class.isAssignableFrom(type)) {
            return BYTE;
        } else if (Character.class.isAssignableFrom(type) || char.class.isAssignableFrom(type)) {
            return CHAR;
        } else if (Short.class.isAssignableFrom(type) || short.class.isAssignableFrom(type)) {
            return SHORT;
        } else if (Integer.class.isAssignableFrom(type) || int.class.isAssignableFrom(type)) {
            return INTEGER;
        } else if (Float.class.isAssignableFrom(type) || float.class.isAssignableFrom(type)) {
            return FLOAT;
        } else if (Long.class.isAssignableFrom(type) || long.class.isAssignableFrom(type)) {
            return LONG;
        } else if (Double.class.isAssignableFrom(type) || double.class.isAssignableFrom(type)) {
            return DOUBLE;
        } else if (String.class.isAssignableFrom(type)) {
            return STRING;
        } else if (Boolean[].class.isAssignableFrom(type)) {
            return BOOLEAN_ARRAY;
        } else if (Byte[].class.isAssignableFrom(type)) {
            return BYTE_ARRAY;
        } else if (Character[].class.isAssignableFrom(type)) {
            return CHAR_ARRAY;
        } else if (Short[].class.isAssignableFrom(type)) {
            return SHORT_ARRAY;
        } else if (Integer[].class.isAssignableFrom(type)) {
            return INTEGER_ARRAY;
        } else if (Float[].class.isAssignableFrom(type)) {
            return FLOAT_ARRAY;
        } else if (Long[].class.isAssignableFrom(type)) {
            return LONG_ARRAY;
        } else if (Double[].class.isAssignableFrom(type)) {
            return DOUBLE_ARRAY;
        } else if (boolean[].class.isAssignableFrom(type)) {
            return BOOLEAN_ARRAY_PRIM;
        } else if (byte[].class.isAssignableFrom(type)) {
            return BYTE_ARRAY_PRIM;
        } else if (char[].class.isAssignableFrom(type)) {
            return CHAR_ARRAY_PRIM;
        } else if (short[].class.isAssignableFrom(type)) {
            return SHORT_ARRAY_PRIM;
        } else if (int[].class.isAssignableFrom(type)) {
            return INTEGER_ARRAY_PRIM;
        } else if (float[].class.isAssignableFrom(type)) {
            return FLOAT_ARRAY_PRIM;
        } else if (long[].class.isAssignableFrom(type)) {
            return LONG_ARRAY_PRIM;
        } else if (double[].class.isAssignableFrom(type)) {
            return DOUBLE_ARRAY_PRIM;
        } else if (String[].class.isAssignableFrom(type)) {
            return STRING_ARRAY;
        } else if (type == ItemStack.class) {
            return ITEM_STACK;
        } else if (type == BlockPos.class) {
            return BLOCK_POS;
        } else {
            return null;
        }
    }
}
