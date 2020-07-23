package io.github.bluelhf.naturaldisasters.event;

import io.github.bluelhf.naturaldisasters.disaster.NaturalDisaster;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class NaturalDisasterEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean isCancelled = false;
    private NaturalDisaster disaster;

    public NaturalDisasterEvent(@NotNull NaturalDisaster disaster) {
        this.disaster = disaster;
        this.isCancelled = false;
    }

    @NotNull
    public NaturalDisaster getDisaster() {
        return this.disaster;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
