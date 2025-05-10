package com.herrkatze.solsticeEconomy.modules.economy;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.Nullable;

public class Notification {
    // A notification object is just a message and a sound that can be sent
    private Component message;
    @Nullable // Null uses default sound for Solstice's notification module.
    private ResourceLocation sound; // Currently unused, but is available in the API
    private float pitch = 1f;
    private float volume = 1f;
    public Notification(Component message, ResourceLocation sound, float pitch, float volume) {
        this.message = message;
        this.sound = sound;
        this.pitch = pitch;
        this.volume = volume;
    }
    public Notification(Component message, @Nullable ResourceLocation sound) {
        this.message = message;
        this.sound = sound;
    }
    public Notification(Component message){
        this.message = message;
        this.sound = null;
    }

    public Component getMessage() {
        return message;
    }

    public @Nullable ResourceLocation getSound() {
        return sound;
    }

    public float getPitch() {
        return pitch;
    }

    public float getVolume() {
        return volume;
    }
}
