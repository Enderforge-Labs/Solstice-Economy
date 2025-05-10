package com.herrkatze.solsticeEconomy.modules.economy;

import eu.pb4.placeholders.impl.placeholder.builtin.ServerPlaceholders;
import me.alexdevs.solstice.Solstice;
import me.alexdevs.solstice.modules.notifications.NotificationsModule;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class NotificationManager {
    public static void sendNotification(Notification notification, ServerPlayer player){
        var message = notification.getMessage();
        var sound = notification.getSound();
        var pitch = notification.getPitch();
        var volume = notification.getVolume();
        if (sound == null) {// null means use solstice notification sound for player pings
            var settings = Solstice.modules.getModule(NotificationsModule.class).getPlayerSettings(player);
             sound = ResourceLocation.tryParse(settings.soundId());
            if (sound == null) { // Failsafe. should never be called
                Solstice.LOGGER.warn("ECONOMY: Player {} has invalid Sound event set. using note block bell",player.getName());
                sound = ResourceLocation.tryParse("minecraft:block.note_block.bell"); // Default value in case something is wrong with a player's solstice settings
            }
            // If sound is null, ignore pitch and volume from the Notification, and use solstice player volumes.
            pitch = settings.pitch();
            volume = settings.volume();
        }
        player.playNotifySound(SoundEvent.createVariableRangeEvent(sound), SoundSource.MASTER,volume,pitch);
        player.sendSystemMessage(message);
    }
}
