package me.joansiitoh.lunarparty;

import me.joansiitoh.lunarparty.party.Party;
import me.joansiitoh.lunarparty.party.PartyInvitation;
import me.joansiitoh.lunarparty.party.PartyManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Joansiitoh (DragonsTeam && SkillTeam)
 * Date: 01/10/2021 - 0:59.
 */
public class SquadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        // PARTY CREATE
        // PARTY DISBAND
        // PARTY ACCEPT PLAYER
        // PARTY INVITE PLAYER
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("create")) {
                if (Party.getPlayerParty(player) != null) {
                    player.sendMessage("Ya perteneces a un squad.");
                    return true;
                }

                new Party(player.getUniqueId());
                player.sendMessage("El squad ha sido creado.");
                return true;
            }

            if (args[0].equalsIgnoreCase("disband")) {
                if (Party.getPlayerParty(player) == null) {
                    player.sendMessage("No perteneces a ningun squad.");
                    return true;
                }

                Party party = Party.getPlayerParty(player);
                if (!party.getOwner().equals(player.getUniqueId())) {
                    player.sendMessage("Solo el dueño puede borrar el squad.");
                    return true;
                }

                player.sendMessage("Squad eliminado.");
                party.disband();
                return true;
            }

            if (args[0].equalsIgnoreCase("leave")) {
                if (Party.getPlayerParty(player) == null) {
                    player.sendMessage("No perteneces a ningun squad.");
                    return true;
                }

                Party party = Party.getPlayerParty(player);
                if (party.getOwner().equals(player.getUniqueId())) {
                    player.sendMessage("Squad eliminado.");
                    party.disband();
                    return true;
                }

                player.sendMessage("Has abandonado el squad.");
                party.deleteMember(player.getUniqueId());
                return true;
            }
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("invite")) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage("El jugador " + target.getName() + " no existe o no se encuentra conectado.");
                    return true;
                }

                Party party = Party.getPlayerParty(player);
                if (party == null) {
                    player.sendMessage("No perteneces a ningun squad.");
                    return true;
                }

                if (!party.getOwner().equals(player.getUniqueId())) {
                    player.sendMessage("Solo el dueño puede invitar a gente.");
                    return true;
                }

                PartyManager.invitePlayer(party, target);
                return true;
            }

            if (args[0].equalsIgnoreCase("accept")) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage("El jugador " + target.getName() + " no existe o no se encuentra conectado.");
                    return true;
                }

                if (Party.getPlayerParty(player) != null) {
                    player.sendMessage("Ya perteneces a un squad.");
                    return true;
                }

                PartyManager.acceptInvitation(player, target);
                return true;
            }
        }
        return false;
    }

}
