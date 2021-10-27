package me.joansiitoh.lunarparty.commands.args;

import me.joansiitoh.lunarparty.Language;
import me.joansiitoh.lunarparty.commands.PartyArgument;
import me.joansiitoh.lunarparty.events.PartyDisbandEvent;
import me.joansiitoh.lunarparty.party.Party;
import org.bukkit.entity.Player;

public class DisbandArgument extends PartyArgument {

    public DisbandArgument() {
        super("disband", "lunarparty.disband", 1);
        setDescription("Disband your current party.");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (Party.getPlayerParty(player) == null) {
            player.sendMessage(Language.NOT_IN_PARTY.toString(true));
            return;
        }

        Party party = Party.getPlayerParty(player);
        if (!party.getOwner().equals(player.getUniqueId())) {
            player.sendMessage(Language.ONLY_OWNER.toString(true));
            return;
        }

        PartyDisbandEvent event = new PartyDisbandEvent(player, party);
        INSTANCE.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        player.sendMessage(Language.DISBAND.toString(true));
        party.getPlayers().stream().filter(member -> !member.getUniqueId().equals(player.getUniqueId())).forEach(member -> {
            member.sendMessage(Language.DISBANDED.toString(true));
        });
        party.disband();
    }

}
