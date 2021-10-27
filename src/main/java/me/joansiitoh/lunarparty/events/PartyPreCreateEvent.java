package me.joansiitoh.lunarparty.events;

import lombok.Getter;
import lombok.Setter;
import me.joansiitoh.lunarparty.party.Party;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@Setter
public final class PartyPreCreateEvent extends Event implements Cancellable {

    private static final HandlerList list = new HandlerList();

    private final Player player;
    private boolean cancelled;

    public PartyPreCreateEvent(Player player) {
        this.player = player;
        this.cancelled = false;
    }

    public static HandlerList getHandlerList() {
        return list;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

}
