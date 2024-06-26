/*
    This file is part of the HeavenMS MapleStory Server, commands OdinMS-based
    Copyleft (L) 2016 - 2019 RonanLana

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation version 3 as published by
    the Free Software Foundation. You may not use, modify or distribute
    this program under any other version of the GNU Affero General Public
    License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

/*
   @Author: Arthur L - Refactored command content into modules
*/
package org.gms.client.command.commands.gm2;

import org.gms.client.Character;
import org.gms.client.Client;
import org.gms.client.command.Command;
import org.gms.constants.id.MobId;
import org.gms.net.server.Server;
import org.gms.server.life.LifeFactory;
import org.gms.tools.PacketCreator;

public class BombCommand extends Command {
    {
        setDescription("Bomb a player, dealing damage.");
    }

    @Override
    public void execute(Client c, String[] params) {
        Character player = c.getPlayer();
        if (params.length > 0) {
            Character victim = c.getWorldServer().getPlayerStorage().getCharacterByName(params[0]);
            if (victim != null) {
                victim.getMap().spawnMonsterOnGroundBelow(LifeFactory.getMonster(MobId.ARPQ_BOMB), victim.getPosition());
                Server.getInstance().broadcastGMMessage(c.getWorld(), PacketCreator.serverNotice(5, player.getName() + " used !bomb on " + victim.getName()));
            } else {
                player.message("Player '" + params[0] + "' could not be found on this world.");
            }
        } else {
            player.getMap().spawnMonsterOnGroundBelow(LifeFactory.getMonster(MobId.ARPQ_BOMB), player.getPosition());
        }
    }
}
