package me.joansiitoh.lunarparty.party;

import me.joansiitoh.lunarparty.sLunar;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Joansiitoh (DragonsTeam && SkillTeam)
 * Date: 01/10/2021 - 0:40.
 */
public class PartyManager {

    public void invitePlayer(Party party, Player target) {
        Player player = party.getBukkitOwner();
        if (Party.getPlayerParty(target) != null) {
            player.sendMessage("ya tiene party");
            return;
        }

        if (PartyInvitation.getInvitation(target, player) != null) {
            player.sendMessage("ya le has invitado");
            return;
        }

        new PartyInvitation(player, target);
        player.sendMessage("has invitado jijii");
        target.sendMessage("has sido invitado jijii");
    }

    public void acceptInvitation(Player player, Player target) {
        PartyInvitation invitation = PartyInvitation.getInvitation(player, target);
        if (invitation != null) {
            Party party = Party.getPlayerParty(target);
            if (party == null) {
                player.sendMessage("el jugador " + target.getName() + " ya no tiene party.");
                return;
            }

            invitation.remove();
            party.addMember(player.getUniqueId());
            player.sendMessage("yujuuuu, acpetado");
            return;
        }

        player.sendMessage("ese jugador no te ha invitado");
    }

    public static void sendTeamMate(Player player, List<Player> targets) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        os.write(13);

        os.write(BufferUtils.writeBoolean(true));
        os.write(BufferUtils.getBytesFromUUID(player.getUniqueId()));
        os.write(BufferUtils.writeLong(10L));

        Map<UUID, Map<String, Double>> playerMap = new HashMap<>();


        for (Player member : targets) {
            Map<String, Double> posMap = new HashMap<>();

            posMap.put("x", member.getLocation().getX());
            posMap.put("y", member.getLocation().getY());
            posMap.put("z", member.getLocation().getZ());

            playerMap.put(member.getUniqueId(), posMap);
        }

        Map<String, Double> posMap = new HashMap<>();

        posMap.put("x", player.getLocation().getX());
        posMap.put("y", player.getLocation().getY());
        posMap.put("z", player.getLocation().getZ());

        playerMap.put(player.getUniqueId(), posMap);

        os.write(BufferUtils.writeVarInt(playerMap.size()));

        for (Map.Entry<UUID, Map<String, Double>> entry : playerMap.entrySet()) {
            os.write(BufferUtils.getBytesFromUUID(entry.getKey()));
            os.write(BufferUtils.writeVarInt(entry.getValue().size()));
            for (Map.Entry<String, Double> posEntry : entry.getValue().entrySet()) {
                os.write(BufferUtils.writeString(posEntry.getKey()));
                os.write(BufferUtils.writeDouble(posEntry.getValue()));
            }
        }

        os.close();

        player.sendPluginMessage(sLunar.INSTANCE, "Lunar-Client", os.toByteArray());
    }

    public static void resetTeamMates(Player player) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        os.write(13);

        os.write(BufferUtils.writeBoolean(false));
        os.write(BufferUtils.writeLong(10L));
        os.write(BufferUtils.writeVarInt(0));

        os.close();

        player.sendPluginMessage(sLunar.INSTANCE, "Lunar-Client", os.toByteArray());
    }

}
