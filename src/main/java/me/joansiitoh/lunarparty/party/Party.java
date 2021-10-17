package me.joansiitoh.lunarparty.party;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by Joansiitoh (DragonsTeam && SkillTeam)
 * Date: 01/10/2021 - 0:35.
 */
@Getter @Setter
public class Party {

    public static final HashMap<UUID, Party> partyHash = new HashMap<>();

    public static Party getPlayerParty(Player player) {
        return partyHash.get(player.getUniqueId());
    }

    /////////////////////////////////////////////////

    private final UUID owner;
    private final List<UUID> members;

    public Party(UUID owner) {
        this.owner = owner;
        this.members = new ArrayList<>();

        this.members.add(owner);
        partyHash.put(owner, this);
    }

    public void disband() {
        members.forEach(partyHash::remove);
    }

    //////////////////////////////////////////////////////////////////////////////

    public Player getBukkitOwner() {
        return Bukkit.getPlayer(owner);
    }

    public List<Player> getPlayers() {
        List<Player> list = new ArrayList<>();
        members.forEach(player -> list.add(Bukkit.getPlayer(player)));
        return list;
    }

    public void addMember(UUID uuid) {
        if (!members.contains(uuid))
            members.add(uuid);

        partyHash.put(uuid, this);
        refresh();
    }

    public void deleteMember(UUID uuid) {
        if (!members.contains(uuid)) return;
        members.remove(uuid);
        partyHash.remove(uuid);

        refresh();

        try {
            PartyManager.resetTeamMates(Bukkit.getPlayer(uuid));
        } catch (IOException ignored) {}
    }

    public void refresh() {
        members.forEach(player -> {
            try {
                PartyManager.sendTeamMate(Bukkit.getPlayer(player), getPlayers());
            } catch (IOException ignored) {}
        });
    }

}
