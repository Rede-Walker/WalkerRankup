package br.com.redewalker.rankup.inventories;

import br.com.redewalker.rankup.Rankup;
import br.com.redewalker.rankup.objects.RankPlayer;
import dev.triumphteam.gui.guis.Gui;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

@Getter
public class VirtualChestInventory {

    private Gui gui;
    private Player player;
    private RankPlayer rankPlayer;
    

    public VirtualChestInventory(Player player){
        this.player = player;
        this.rankPlayer = Rankup.getRankup().getRankPlayerManager().getRankupPlayer(player.getName());
        if(rankPlayer.getVirtualChests().isEmpty()){
            this.gui = Gui.gui().rows(3).title(Component.text("Baús de " + player.getName())).create();
        }else{
            int amountOfChests = rankPlayer.getVirtualChests().size();
            if(amountOfChests > 7){
                int rows = (amountOfChests / 7) + 4;
                this.gui = Gui.gui().rows(rows).title(Component.text("Baús de " + player.getName())).create();
            }else{
                this.gui = Gui.gui().rows(4).title(Component.text("Baús de " + player.getName())).create();
            }
        }
    }


    public void open(){
        this.gui.open(this.player);
        setupItems();
    }

    public void setupItems(){

    }





}
