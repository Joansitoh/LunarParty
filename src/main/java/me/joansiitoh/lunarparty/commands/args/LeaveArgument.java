package me.joansiitoh.lunarparty.commands.args;

import me.joansiitoh.lunarparty.Language;
import me.joansiitoh.lunarparty.commands.PartyArgument;
import me.joansiitoh.lunarparty.party.Party;
import org.bukkit.entity.Player;

public class LeaveArgument extends PartyArgument {

    public LeaveArgument() {
        super("leave", "lunarparty.leave", 1);
        setDescription("Left your party.");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (Party.getPlayerParty(player) == null) {
            player.sendMessage(Language.NOT_IN_PARTY.toString(true));
            return;
        }

        Party party = Party.getPlayerParty(player);
        if (party.getOwner().equals(player.getUniqueId())) {
            player.sendMessage(Language.DISBAND.toString(true));
            party.getPlayers().stream().filter(member -> !member.getUniqueId().equals(player.getUniqueId())).forEach(member -> {
                member.sendMessage(Language.DISBANDED.toString(true));
            });
            party.disband();
            return;
        }

        player.sendMessage(Language.LEAVE.toString(true));
        party.getPlayers().stream().filter(member -> !member.getUniqueId().equals(player.getUniqueId()))
                .forEach(member -> member.sendMessage(Language.LEFT.toString(true).replace("<party_target>", player.getName())));
        party.deleteMember(player.getUniqueId());
    }

}
