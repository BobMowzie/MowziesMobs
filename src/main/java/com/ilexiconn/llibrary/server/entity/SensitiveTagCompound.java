package com.ilexiconn.llibrary.server.entity;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.nbt.*;

public class SensitiveTagCompound extends CompoundNBT {
// TODO: this.tagMap is private now! What do I do?
	private boolean changed = false;
	private Set<String> changedTags = new HashSet<>();

	@Override
	public void putByte(String key, byte value) {
		INBT b = this.tagMap.put(key, new ByteNBT(value));
		if (b == null) updateChanged(key);
		else if (b instanceof ByteNBT && ((ByteNBT) b).getByte() != value) updateChanged(key);
	}

	@Override
	public void putByteArray(String key, byte[] value) {
		INBT b = this.tagMap.put(key, new ByteArrayNBT(value));
		if (b == null) updateChanged(key);
		else if (b instanceof ByteArrayNBT && ((ByteArrayNBT) b).getByteArray() != value) updateChanged(key);
	}

	@Override
	public void putDouble(String key, double value) {
		INBT b = this.tagMap.put(key, new DoubleNBT(value));
		if (b == null) updateChanged(key);
		else if (b instanceof DoubleNBT && ((DoubleNBT) b).getDouble() != value) updateChanged(key);
	}

	@Override
	public void putFloat(String key, float value) {
		INBT b = this.tagMap.put(key, new FloatNBT(value));
		if (b == null) updateChanged(key);
		else if (b instanceof FloatNBT && ((FloatNBT) b).getFloat() != value) updateChanged(key);
	}

	@Override
	public void putIntArray(String key, int[] value) {
		INBT b = this.tagMap.put(key, new IntArrayNBT(value));
		if (b == null) updateChanged(key);
		else if (b instanceof IntArrayNBT && ((IntArrayNBT) b).getIntArray() != value) updateChanged(key);
	}

	@Override
	public void putInt(String key, int value) {
		INBT b = this.tagMap.put(key, new IntNBT(value));
		if (b == null) updateChanged(key);
		else if (b instanceof IntNBT && ((IntNBT) b).getInt() != value) updateChanged(key);
	}

	@Override
	public void putLong(String key, long value) {
		INBT b = this.tagMap.put(key, new LongNBT(value));
		if (b == null) updateChanged(key);
		else if (b instanceof LongNBT && ((LongNBT) b).getLong() != value) updateChanged(key);
	}

	@Override
	public void putShort(String key, short value) {
		INBT b = this.tagMap.put(key, new ShortNBT(value));
		if (b == null) updateChanged(key);
		else if (b instanceof ShortNBT && ((ShortNBT) b).getShort() != value) updateChanged(key);
	}

	@Override
	public void putString(String key, String value) {
		INBT b = this.tagMap.put(key, new StringNBT(value));
		if (b == null) updateChanged(key);
		else if (b instanceof StringNBT && !((StringNBT) b).getString().equals(value)) updateChanged(key);
	}

	@Override
	public void put(String key, INBT value) {
		INBT b = this.tagMap.put(key, value);
		if (b == null) updateChanged(key);
		else if (!b.equals(value)) updateChanged(key);
	}

	public void updateChanged(String key) {
		changedTags.add(key);
		changed = true;
	}

	public CompoundNBT getChangedCopy() {
		CompoundNBT tag = new CompoundNBT();
		for (String s : changedTags)
			tag.put(s, this.get(s));
		return tag;
	}

	public boolean hasChanged() {
		return changed;
	}

	public void reset() {
		changed = false;
		changedTags.clear();
	}

}
