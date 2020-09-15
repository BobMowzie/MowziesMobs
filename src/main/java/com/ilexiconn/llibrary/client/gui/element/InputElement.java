package com.ilexiconn.llibrary.client.gui.element;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.Consumer;
import java.util.function.Function;

@SideOnly(Side.CLIENT)
public class InputElement<T extends IElementGUI> extends InputElementBase<T> {
    private Consumer<InputElementBase<T>> onSubmit;
    private Function<Integer, Boolean> allowKey;

    public InputElement(T gui, float posX, float posY, int width, String text, Consumer<InputElementBase<T>> onSubmit) {
        this(gui, posX, posY, width, text, true, onSubmit, (key) -> true);
    }

    public InputElement(T gui, float posX, float posY, int width, String text, boolean editable, Consumer<InputElementBase<T>> onSubmit, Function<Integer, Boolean> allowKey) {
        super(gui, posX, posY, width, editable, text);
        this.onSubmit = onSubmit;
        this.allowKey = allowKey;
    }

    @Override
    protected void onSubmit() {
        this.onSubmit.accept(this);
    }

    @Override
    protected boolean allowKey(int key) {
        return this.allowKey.apply(key);
    }
}
