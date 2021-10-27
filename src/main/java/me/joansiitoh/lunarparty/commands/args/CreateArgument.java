package me.joansiitoh.lunarparty.commands.args;

import me.joansiitoh.lunarparty.Language;
import me.joansiitoh.lunarparty.commands.PartyArgument;
import me.joansiitoh.lunarparty.events.PartyCreateEvent;
import me.joansiitoh.lunarparty.events.PartyPreCreateEvent;
import me.joansiitoh.lunarparty.party.Party;
import org.bukkit.entity.Player;

public class CreateArgument extends PartyArgument {

    public CreateArgument() {
        super("create", "lunarparty.create", 1);
        setDescription("Create new party.");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (Party.getPlayerParty(player) != null) {
            player.sendMessage(Language.ALREADY_IN_PARTY.toString(true));
            return;
        }

        PartyPreCreateEvent preEvent = new PartyPreCreateEvent(player);
        INSTANCE.getServer().getPluginManager().callEvent(preEvent);

        if (preEvent.isCancelled()) return;

        Party party = new Party(player.getUniqueId());
        PartyCreateEvent event = new PartyCreateEvent(player, party);
        INSTANCE.getServer().getPluginManager().callEvent(event);

        player.sendMessage(Language.CREATED.toString(true));
    }

}
