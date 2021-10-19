package br.com.redewalker.rankup.systems.virtualchests.inventories;

import br.com.redewalker.api.API;
import br.com.redewalker.api.systems.groups.Groups;
import br.com.redewalker.api.systems.users.User;
import br.com.redewalker.api.utils.ItemBuilder;
import br.com.redewalker.rankup.Rankup;
import br.com.redewalker.rankup.systems.rankplayer.RankPlayer;
import br.com.redewalker.rankup.systems.virtualchests.VirtualChest;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collections;

public class VirtualChestInventory {

    @Getter private final Gui gui;
    @Getter private final Player player;
    @Getter private final RankPlayer rankPlayer;
    @Getter private final MenuType menuType;
    @Getter private final User user;


    public VirtualChestInventory(Player player){
        this.player = player;
        this.rankPlayer = Rankup.getRankup().getRankPlayerManager().getRankupPlayer(player.getName());
        this.user = API.getInstance().getUserManager().getUser(player.getName());
        if(rankPlayer.getVirtualChests().isEmpty()){
            this.gui = Gui.gui().rows(3).title(Component.text("Baús de " + player.getName())).create();
            this.menuType = MenuType.NO_CHESTS;
        }else{
            int amountOfChests = rankPlayer.getVirtualChests().size();
            if(amountOfChests > 7){
                int rows = (amountOfChests / 7) + 4;
                this.gui = Gui.gui().rows(rows).title(Component.text("Baús de " + player.getName())).create();
                this.menuType = MenuType.MULTIPLES_ROWS;
            }else{
                this.gui = Gui.gui().rows(4).title(Component.text("Baús de " + player.getName())).create();
                this.menuType = MenuType.ONE_ROW;
            }
        }
        this.gui.disableAllInteractions();
    }


    public void open(){
        setupItems();
        this.gui.open(this.player);
    }

    public void setupItems(){

        GuiItem buyItem = new GuiItem(new ItemBuilder(Material.EMERALD).setName(ChatColor.GREEN + "Comprar Baú Virtual").setLore(Arrays.asList(
                ChatColor.WHITE + "Adquira um baú virtual!",
                " ",
                ChatColor.WHITE + "Custo: " + ChatColor.GRAY + NumberFormat.getInstance().format(Rankup.getRankup().getVirtualChestManager().getVirtualChestPrice()),
                this.user.getCash() >= Rankup.getRankup().getVirtualChestManager().getVirtualChestPrice() ? ChatColor.GREEN + "Clique aqui para comprar." : ChatColor.RED + "Você não possui cash suficiente!"
        )).toItemStack());
        buyItem.setAction(action-> {
            if(this.rankPlayer.getVirtualChests().size() >= Rankup.getRankup().getVirtualChestManager().getMaxVirtualChests()){
                action.getWhoClicked().sendMessage(ChatColor.RED + "Você já atingiu o limite de baús virtuais disponíveis para compra.");
            }else{
                if(this.user.getCash() >= Rankup.getRankup().getVirtualChestManager().getVirtualChestPrice()) new VirtualChestShopInventory(this.player).open();
            }
        });

        GuiItem enderChest = new GuiItem(new ItemBuilder(Material.ENDER_CHEST).setName(ChatColor.GREEN + "Enderchest").setLore(Collections.singletonList(ChatColor.WHITE + "Clique para abrir seu enderchest.")).toItemStack());
        enderChest.setAction(action->{
            if(this.user.getGroupNetwork() == Groups.MEMBRO){
                action.getWhoClicked().sendMessage(ChatColor.RED + "Você precisa de um grupo VIP para isso!");
            }else{
                action.getWhoClicked().openInventory(action.getWhoClicked().getEnderChest());
            }
        });

        if(menuType == MenuType.NO_CHESTS){
            this.gui.setItem(12, buyItem);
            this.gui.setItem(14, enderChest);
        }else if(menuType == MenuType.ONE_ROW){

            int lastSlot = 10;
            for(VirtualChest virtualChest : this.rankPlayer.getVirtualChests()){
                this.gui.setItem(lastSlot, virtualChestGuiItem(virtualChest));
                lastSlot++;
            }

            this.gui.setItem(30, buyItem);
            this.gui.setItem(32, enderChest);
        }else if(menuType == MenuType.MULTIPLES_ROWS){

            int last = 0;
            for(int i = 0; i < 35;i++){
                if ((i >= 9) && (i != 17) && (i != 26) && (i % 9 != 0)) {
                    if(last >= this.rankPlayer.getVirtualChests().size()) break;

                    VirtualChest virtualChest = this.rankPlayer.getVirtualChests().get(last);
                    this.gui.setItem(last, virtualChestGuiItem(virtualChest));

                    last++;
                }
            }

            this.gui.setItem(48, buyItem);
            this.gui.setItem(50, enderChest);
        }
    }

    private GuiItem virtualChestGuiItem(VirtualChest virtualChest){
        GuiItem virtualChestItem = new GuiItem(new ItemBuilder(virtualChest.getChestIcon()).setName(virtualChest.getChestName()).setLore(Arrays.asList(
                ChatColor.GRAY + "Clique " + ChatColor.WHITE + "esquerdo" + ChatColor.GRAY + " para " + ChatColor.WHITE + "abrir baú",
                ChatColor.GRAY + "Clique " + ChatColor.WHITE + "direito" + ChatColor.GRAY + " para " + ChatColor.WHITE + "editar baú",
                " ",
                ChatColor.DARK_GRAY + "Baú " + virtualChest.getSizeType().getName().toLowerCase(),
                ChatColor.DARK_GRAY + "/bau " + virtualChest.getChestId()
        )).toItemStack());
        virtualChestItem.setAction(action->{
            if(action.isLeftClick()){
                action.getWhoClicked().openInventory(virtualChest.getInventory());
            }else{
                new VirtualChestEditInventory((Player) action.getWhoClicked(), virtualChest).open();
            }
        });
        return virtualChestItem;
    }

    public enum MenuType{
        NO_CHESTS,
        ONE_ROW,
        MULTIPLES_ROWS
    }

    private class VirtualChestShopInventory{

        @Getter private final Gui gui;
        @Getter private final Player player;
        @Getter private final RankPlayer rankPlayer;
        @Getter private final User user;

        public VirtualChestShopInventory(Player player){
            this.player = player;
            this.rankPlayer = Rankup.getRankup().getRankPlayerManager().getRankupPlayer(player.getName());
            this.user = API.getInstance().getUserManager().getUser(player.getName());
            this.gui = Gui.gui().title(Component.text("Adquirindo baú virtual")).rows(3).create();

            GuiItem confirmationItem = new GuiItem(new ItemBuilder(Material.WOOL, 5).setName(ChatColor.GREEN + "Confirmação (leia abaixo)")
                    .setLore(Collections.singletonList(ChatColor.WHITE + "Você irá gastar " + NumberFormat.getInstance().format(Rankup.getRankup().getVirtualChestManager().getVirtualChestPrice()) + " cash.")).toItemStack());
            confirmationItem.setAction(action->{
                if(this.user.getCash() < Rankup.getRankup().getVirtualChestManager().getVirtualChestPrice()){
                    action.getWhoClicked().closeInventory();
                    action.getWhoClicked().sendMessage(ChatColor.RED + "Saldo insuficiente!");
                }else{
                    action.getWhoClicked().sendMessage(ChatColor.GREEN + "Baú virtual adquirido com sucesso!");
                    this.user.setCash(this.user.getCash() - Rankup.getRankup().getVirtualChestManager().getVirtualChestPrice());
                    this.rankPlayer.addVirtualChest();
                    action.getWhoClicked().closeInventory();
                    ((Player)action.getWhoClicked()).playSound(action.getWhoClicked().getLocation(), Sound.VILLAGER_YES, 1.0F, 1.0F);
                }
            });

            GuiItem cancelItem = new GuiItem(new ItemBuilder(Material.WOOL, 14).setName(ChatColor.RED + "Negar").setLore(Collections.singletonList(ChatColor.WHITE +
                    "Cancelar essa operação.")).toItemStack());
            cancelItem.setAction(action->{
                action.getWhoClicked().closeInventory();
                action.getWhoClicked().sendMessage(ChatColor.RED + "Operação cancelada!");
            });

            this.gui.disableAllInteractions();
            this.gui.setItem(12, confirmationItem);
            this.gui.setItem(14, cancelItem);
        }

        public void open(){
            this.gui.open(this.player);
        }
    }
}
