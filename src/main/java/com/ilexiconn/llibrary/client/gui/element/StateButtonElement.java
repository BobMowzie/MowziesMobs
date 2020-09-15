package com.ilexiconn.llibrary.client.gui.element;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.function.Function;

@SideOnly(Side.CLIENT)
public class StateButtonElement<T extends IElementGUI> extends ButtonElement<T> {
    private List<String> states;
    private int state;

    public StateButtonElement(T handler, float posX, float posY, int width, int height, List<String> states, Function<ButtonElement<T>, Boolean> function) {
        super(handler, states.get(0), posX, posY, width, height, function);
        this.states = states;
    }

    @Override
    public boolean mouseClicked(float mouseX, float mouseY, int button) {
        if (this.isSelected(mouseX, mouseY)) {
            if (this.function.apply(this)) {
                this.state++;
                if (this.state >= this.states.size()) {
                    this.state = 0;
                }
                this.text = this.states.get(this.state);
                this.gui.playClickSound();
            }
            return true;
        } else {
            return false;
        }
    }

    public int getState() {
        return this.state;
    }

    public StateButtonElement<T> withState(int state) {
        this.state = state;
        if (this.state >= this.states.size()) {
            this.state = 0;
        } else if (this.state < 0) {
            this.state = this.states.size() - 1;
        }
        this.text = this.states.get(this.state);
        return this;
    }
}
