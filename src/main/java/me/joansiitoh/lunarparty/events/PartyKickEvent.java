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
public final class PartyKickEvent extends Event implements Cancellable {

    private static final HandlerList list = new HandlerList();

    private final Player player, target;
    private final Party party;

    private boolean cancelled;

    public PartyKickEvent(Player player, Player target, Party party) {
        this.player = player;
        this.target = target;
        this.party = party;
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
