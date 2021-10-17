package me.joansiitoh.lunarparty.party;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by Joansiitoh (DragonsTeam && SkillTeam)
 * Date: 01/10/2021 - 0:41.
 */
@Getter
public class PartyInvitation {

    private static final List<PartyInvitation> invitations = new ArrayList<>();

    public static PartyInvitation getInvitation(Player player, Player target) {
        for (PartyInvitation invitation : invitations) {
            if (invitation.getReceiver().equals(player)) {
                if (invitation.getSender().equals(target)) return invitation;
            }
        }

        return null;
    }

    ////////////////////////////////////////////////////////////////////

    private final Player sender, receiver;
    private final long createdLong;

    public PartyInvitation(Player sender, Player receiver) {
        this.sender = sender;
        this.receiver = receiver;
        this.createdLong = System.currentTimeMillis();

        invitations.add(this);
    }

    public void remove() {
        invitations.remove(this);
    }

    public boolean hasExpired() {
        return this.createdLong + 30000 < System.currentTimeMillis();
    }

}
