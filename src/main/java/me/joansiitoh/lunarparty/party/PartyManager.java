package me.joansiitoh.lunarparty.party;

import club.skilldevs.utils.ChatUtils;
import club.skilldevs.utils.FileConfig;
import club.skilldevs.utils.texts.FancyMessage;
import com.lunarclient.bukkitapi.nethandler.client.LCPacketTeammates;
import me.joansiitoh.lunarparty.Language;
import me.joansiitoh.lunarparty.events.PartyDisbandEvent;
import me.joansiitoh.lunarparty.events.PartyInviteEvent;
import me.joansiitoh.lunarparty.sLunar;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by Joansiitoh (DragonsTeam && SkillTeam)
 * Date: 01/10/2021 - 0:40.
 */
public class PartyManager {

    private final List<UUID> chatters;

    public PartyManager() {
        this.chatters = new ArrayList<>();
    }

    public boolean isChatting(Player player) {
        return chatters.contains(player.getUniqueId());
    }

    public void toggleChat(Player player) {
        if (!chatters.contains(player.getUniqueId()))
            chatters.add(player.getUniqueId());
        else chatters.remove(player.getUniqueId());

        player.sendMessage(Language.CHAT_TOGGLED.toString(true).replace("<party_value>",
                chatters.contains(player.getUniqueId()) ? ChatColor.GREEN + "ON" : ChatColor.RED + "OFF"));
    }

    public void invite(Party party, Player target) {
        Player player = party.getBukkitOwner();
        if (Party.getPlayerParty(target) != null) {
            player.sendMessage(Language.ALREADY_HAS_PARTY.toString(true).replace("<party_target>", target.getName()));
            return;
        }

        if (Invitations.getInvitation(target, player) != null) {
            player.sendMessage(Language.ALREADY_INVITED.toString(true).replace("<party_target>", target.getName()));
            return;
        }

        ConfigurationSection section = sLunar.INSTANCE.getSettingsFile().getSection("MAX-MEMBERS");
        int max = sLunar.INSTANCE.getSettingsFile().getInt("MAX-MEMBERS.DEFAULT");
        for (String keys : section.getKeys(false)) {
            if (keys.equalsIgnoreCase("DEFAULT")) continue;

            String perm = section.getString(keys + ".PERMISSION");
            int x = section.getInt(keys + ".MAX");
            if (party.getBukkitOwner() != null) {
                if (party.getBukkitOwner().hasPermission(perm) && x > max) max = x;
            } else if (player.hasPermission(perm) && x > max) max = x;
        }

        if (party.getMembers().size() >= max)  {
            player.sendMessage(Language.INVITE_MAX.toString(true));
            return;
        }

        PartyInviteEvent event = new PartyInviteEvent(player, target, party);
        sLunar.INSTANCE.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        new Invitations(player, target);
        player.sendMessage(Language.INVITE.toString(true).replace("<party_target>", target.getName()));

        String prefix = "<hover>", suffix = "</hover>";
        if (Language.INVITED.toStringList() != null) {
            for (String s : Language.INVITED.toStringList()) {
                String between = StringUtils.substringBetween(s, prefix, suffix);
                if (between != null && !between.equalsIgnoreCase("")) {
                    String[] arg = s.split(between);
                    FancyMessage message = new FancyMessage(arg[0]
                            .replace(prefix, "").replace("<party_target>", player.getName()));

                    message.then(between).tooltip(Language.INVITED_HOVER.toString().replace("<party_target>", player.getName()));
                    message.command("/party accept " + player.getName());

                    message.then(arg[1].replace(suffix, "").replace("<party_target>", player.getName()));
                    message.send(target);
                    continue;
                }

                target.sendMessage(ChatUtils.translate(s.replace("<party_target>", player.getName())));
            }
            return;
        }
        target.sendMessage(Language.INVITED.toString(true).replace("<party_target>", player.getName()));
    }

    public void accept(Player player, Player target) {
        Invitations invitation = Invitations.getInvitation(player, target);
        Party party = Party.getPlayerParty(target);
        if (party.getMembers().size() >= 10) {
            return;
        }

        if (invitation != null) {
            if (!party.exist()) {
                player.sendMessage(Language.NOT_IN_PARTY.toString(true).replace("<party_target>", target.getName()));
                return;
            }

            invitation.remove();
            party.addMember(player.getUniqueId());
            player.sendMessage(Language.ACCEPT.toString(true).replace("<party_target>", target.getName()));
            target.sendMessage(Language.ACCEPTED.toString(true).replace("<party_target>", player.getName()));
            return;
        }

        player.sendMessage(Language.NOT_INVITED.toString(true).replace("<party_target>", target.getName()));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    public void sendTeamMate(Player player, List<Player> targets) throws IOException {
        if (player == null) return;

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        os.write(13);

        os.write(BufferUtils.writeBoolean(true));
        os.write(BufferUtils.getBytesFromUUID(player.getUniqueId()));
        os.write(BufferUtils.writeLong(10L));

        Map<UUID, Map<String, Double>> playerMap = new HashMap<>();


        for (Player member : targets) {
            if (member == null) continue;
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

        sLunar.INSTANCE.getLunarClientAPI().sendTeammates(player, new LCPacketTeammates(player.getUniqueId(), 1, playerMap));
    }

    public void resetTeamMates(Player player) throws IOException {
        if (player == null) return;
        sLunar.INSTANCE.getLunarClientAPI().sendTeammates(player, new LCPacketTeammates(null, 10L, new HashMap<>()));
    }

}
