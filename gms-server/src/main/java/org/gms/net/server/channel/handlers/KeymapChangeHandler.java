/*
	This file is part of the OdinMS Maple Story Server
    Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
		       Matthias Butz <matze@odinms.de>
		       Jan Christian Meyer <vimes@odinms.de>

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
package org.gms.net.server.channel.handlers;

import org.gms.client.Client;
import org.gms.client.Skill;
import org.gms.client.SkillFactory;
import org.gms.client.inventory.InventoryType;
import org.gms.client.keybind.KeyBinding;
import org.gms.constants.game.GameConstants;
import org.gms.net.AbstractPacketHandler;
import org.gms.net.packet.InPacket;

public final class KeymapChangeHandler extends AbstractPacketHandler {
    @Override
    public final void handlePacket(InPacket p, Client c) {
        if (p.available() >= 8) {
            int mode = p.readInt();
            if (mode == 0) {
                int numChanges = p.readInt();
                for (int i = 0; i < numChanges; i++) {
                    int key = p.readInt();
                    int type = p.readByte();
                    int action = p.readInt();

                    if (type == 1) {
                        Skill skill = SkillFactory.getSkill(action);
                        boolean isBanndedSkill;
                        if (skill != null) {
                            isBanndedSkill = GameConstants.bannedBindSkills(skill.getId());
                            if (isBanndedSkill || (!c.getPlayer().isGM() && GameConstants.isGMSkills(skill.getId())) || (!GameConstants.isInJobTree(skill.getId(), c.getPlayer().getJob().getId()) && !c.getPlayer().isGM())) { //for those skills are are "technically" in the beginner tab, like bamboo rain in Dojo or skills you find in PYPQ
                                //AutobanFactory.PACKET_EDIT.alert(c.getPlayer(), c.getPlayer().getName() + " tried to packet edit keymapping.");
                                //FilePrinter.printError(FilePrinter.EXPLOITS + c.getPlayer().getName() + ".txt", c.getPlayer().getName() + " tried to use skill " + skill.getId());
                                //c.disconnect(true, false);
                                //return;

                                continue;   // fk that
                            }
                                                        /* if (c.getPlayer().getSkillLevel(skill) < 1) {    HOW WOULD A SKILL EVEN BE AVAILABLE TO KEYBINDING
                                                                continue;                                   IF THERE IS NOT EVEN A SINGLE POINT USED INTO IT??
                                                        } */
                        }
                    }

                    c.getPlayer().changeKeybinding(key, new KeyBinding(type, action));
                }
            } else if (mode == 1) { // Auto HP Potion
                int itemID = p.readInt();
                if (itemID != 0 && c.getPlayer().getInventory(InventoryType.USE).findById(itemID) == null) {
                    c.disconnect(false, false); // Don't let them send a packet with a use item they dont have.
                    return;
                }
                c.getPlayer().changeKeybinding(91, new KeyBinding(7, itemID));
            } else if (mode == 2) { // Auto MP Potion
                int itemID = p.readInt();
                if (itemID != 0 && c.getPlayer().getInventory(InventoryType.USE).findById(itemID) == null) {
                    c.disconnect(false, false); // Don't let them send a packet with a use item they dont have.
                    return;
                }
                c.getPlayer().changeKeybinding(92, new KeyBinding(7, itemID));
            }
        }
    }
}
