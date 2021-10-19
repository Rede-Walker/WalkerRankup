package br.com.redewalker.rankup.systems.virtualchests.inventories;

import br.com.redewalker.api.utils.ItemBuilder;
import br.com.redewalker.rankup.systems.rankplayer.RankPlayer;
import br.com.redewalker.rankup.systems.virtualchests.VirtualChest;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class VirtualChestAdminInventory {

    @Getter
    private final Gui gui;
    @Getter private final Player player;
    @Getter private final RankPlayer rankPlayer;

    public VirtualChestAdminInventory(Player player, RankPlayer rankPlayer){
        this.player = player;
        this.rankPlayer = rankPlayer;

        int size = 3;

        if(rankPlayer.getVirtualChests().size() > 7 && rankPlayer.getVirtualChests().size() < 15) size = 4;
        else if (rankPlayer.getVirtualChests().size() >= 15) size = 5;

        this.gui = Gui.gui().title(Component.text("Baús de " + rankPlayer.getName())).rows(size).create();

        int last = 0;
        for(int i = 10; i < 35; i++){
            if ((i >= 9) && (i != 17) && (i != 26) && (i % 9 != 0)) {
                if (last >= this.rankPlayer.getVirtualChests().size()) break;

                this.gui.setItem(i, virtualChestGuiItem(this.rankPlayer.getVirtualChests().get(last)));
                last++;
            }
        }
    }


    public void open() { this.gui.open(this.player); }

    private GuiItem virtualChestGuiItem(VirtualChest virtualChest){
        GuiItem virtualChestItem = new GuiItem(new ItemBuilder(virtualChest.getChestIcon()).setName(virtualChest.getChestName()).setLore(Arrays.asList(
                ChatColor.GRAY + "Clique " + ChatColor.WHITE + "esquerdo" + ChatColor.GRAY + " para " + ChatColor.WHITE + "abrir baú",
                " ",
                ChatColor.DARK_GRAY + "Baú " + virtualChest.getSizeType().getName().toLowerCase(),
                ChatColor.DARK_GRAY + "/bau " + virtualChest.getChestId() + " " + this.rankPlayer.getName()
        )).toItemStack());
        virtualChestItem.setAction(action-> action.getWhoClicked().openInventory(virtualChest.getInventory()));
        return virtualChestItem;
    }
}
