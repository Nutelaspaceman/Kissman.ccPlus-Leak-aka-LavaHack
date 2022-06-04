package com.kisman.cc.event.events;

import com.kisman.cc.event.Event;
import net.minecraft.util.EnumHandSide;

public class TransformSideFirstPersonEvent extends Event {
    private final EnumHandSide enumHandSide;

    public TransformSideFirstPersonEvent(EnumHandSide enumHandSide) {
        this.enumHandSide = enumHandSide;
    }

    public EnumHandSide getEnumHandSide() {
        return this.enumHandSide;
    }
}
