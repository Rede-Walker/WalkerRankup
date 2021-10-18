package br.com.redewalker.rankup.inventories;

import br.com.redewalker.api.API;
import br.com.redewalker.api.systems.users.User;
import br.com.redewalker.api.utils.ItemBuilder;
import br.com.redewalker.rankup.Rankup;
import br.com.redewalker.rankup.listeners.VirtualChestListeners;
import br.com.redewalker.rankup.objects.VirtualChest;
import br.com.redewalker.rankup.objects.enums.VirtualChestType;
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

public class VirtualChestEditInventory {

    @Getter
    private final Gui gui;
    @Getter private final Player player;
    @Getter private final User user;
    @Getter private final VirtualChest virtualChest;

    public VirtualChestEditInventory(Player player, VirtualChest virtualChest){
        this.player = player;
        this.virtualChest = virtualChest;
        this.user = API.getInstance().getUserManager().getUser(player.getName());

        this.gui = Gui.gui().rows(4).title(Component.text("Editando baú #" + virtualChest.getChestId())).create();
        this.gui.disableAllInteractions();

        GuiItem returnItem = new GuiItem(new ItemBuilder(Material.ARROW).setName(ChatColor.RED + "Voltar").toItemStack());
        returnItem.setAction(action-> {
            new VirtualChestInventory((Player) action.getWhoClicked()).open();
        });

        GuiItem iconsItem = new GuiItem(new ItemBuilder(virtualChest.getChestIcon()).setName(ChatColor.YELLOW + "Ícone").setLore(Collections.singletonList(
                ChatColor.WHITE + "Clique para alterar o ícone do baú.")).toItemStack());
        iconsItem.setAction(action->{
            new VirtualChestIconChangeInventory((Player) action.getWhoClicked(), virtualChest).open();
        });

        GuiItem renameItem = new GuiItem(new ItemBuilder(Material.NAME_TAG).setName(ChatColor.YELLOW + "Renomear").setLore(Collections.singletonList(
                ChatColor.WHITE + "Clique para renomear esse baú.")).toItemStack());
        renameItem.setAction(action->{
            action.getWhoClicked().closeInventory();
            action.getWhoClicked().sendMessage(" ");
            action.getWhoClicked().sendMessage(ChatColor.YELLOW + "Escreva no chat um novo nome para o báu #" + virtualChest.getChestId() + ".");
            action.getWhoClicked().sendMessage(ChatColor.WHITE + "Para cancelar, escreva 'cancelar'.");
            action.getWhoClicked().sendMessage(" ");
            VirtualChestListeners.renameVirtualChest.put((Player) action.getWhoClicked(), virtualChest);
        });

        GuiItem upgradeItem = new GuiItem(new ItemBuilder(Material.BOOK).setName(ChatColor.YELLOW + "Melhoria").setLore(Arrays.asList(
                ChatColor.WHITE + "Clique para duplicar o tamanho do baú.",
                " ",
                ChatColor.WHITE + "Custo: " + ChatColor.GRAY + NumberFormat.getInstance().format(VirtualChestType.COMPLETE.getCost()),
                this.user.getCash() >= VirtualChestType.COMPLETE.getCost() ? ChatColor.GREEN + "Clique aqui para comprar." : ChatColor.RED + "Você não possui cash suficiente!"
        )).toItemStack());
        upgradeItem.setAction(action->{
            if(this.user.getCash() >= VirtualChestType.COMPLETE.getCost()) new VirtualChestUpgradeShopInventory(player, virtualChest).open();
        });

        if(virtualChest.getSizeType() == VirtualChestType.COMPLETE){
            this.gui.setItem(12, renameItem);
            this.gui.setItem(14, iconsItem);
        }else{
            this.gui.setItem(11, renameItem);
            this.gui.setItem(13, iconsItem);
            this.gui.setItem(15, upgradeItem);
        }
        this.gui.setItem(31, returnItem);
    }

    public void open(){
        this.gui.open(this.player);
    }

    private class VirtualChestIconChangeInventory{

        @Getter private final Gui gui;
        @Getter private final Player player;
        @Getter private final VirtualChest virtualChest;
        @Getter private final User user;

        public VirtualChestIconChangeInventory(Player player, VirtualChest virtualChest){
            this.player = player;
            this.virtualChest = virtualChest;
            this.user = API.getInstance().getUserManager().getUser(player.getName());
            this.gui = Gui.gui().title(Component.text("Alterando ícone do baú virtual")).rows(6).create();

            this.gui.disableAllInteractions();
            int last = 0;
            for(int slot = 0; slot < 55; slot++){
                if(last >= Rankup.getRankup().getVirtualChestManager().getIconList().size()) break;

                this.gui.setItem(slot, createGuiItem(Rankup.getRankup().getVirtualChestManager().getIconList().get(last)));
                last++;
            }
        }

        public void open(){
            this.gui.open(this.player);
        }

        private GuiItem createGuiItem(Material material){
            GuiItem guiItem = new GuiItem(material);
            guiItem.setAction(action->{
                action.getWhoClicked().sendMessage(ChatColor.GREEN + "Ícone do baú virtual alterado com sucesso!");
                action.getWhoClicked().closeInventory();
                this.virtualChest.setMaterial(guiItem.getItemStack().getType());
                Rankup.getRankup().getRankPlayerManager().getDao().saveEntity(Rankup.getRankup().getRankPlayerManager().getRankupPlayer(player.getName()));
                ((Player)action.getWhoClicked()).playSound(action.getWhoClicked().getLocation(), Sound.VILLAGER_YES, 1.0F, 1.0F);
            });
            return guiItem;
        }
    }

    private class VirtualChestUpgradeShopInventory{

        @Getter private final Gui gui;
        @Getter private final Player player;
        @Getter private final VirtualChest virtualChest;
        @Getter private final User user;

        public VirtualChestUpgradeShopInventory(Player player, VirtualChest virtualChest){
            this.player = player;
            this.virtualChest = virtualChest;
            this.user = API.getInstance().getUserManager().getUser(player.getName());
            this.gui = Gui.gui().title(Component.text("Aumentando baú virtual")).rows(3).create();

            GuiItem confirmationItem = new GuiItem(new ItemBuilder(Material.WOOL, 5).setName(ChatColor.GREEN + "Confirmação (leia abaixo)")
                    .setLore(Collections.singletonList(ChatColor.WHITE + "Você irá gastar " + NumberFormat.getInstance().format(VirtualChestType.COMPLETE.getCost()) + " cash.")).toItemStack());
            confirmationItem.setAction(action->{
                if(this.user.getCash() < VirtualChestType.COMPLETE.getCost()){
                    action.getWhoClicked().closeInventory();
                    action.getWhoClicked().sendMessage(ChatColor.RED + "Saldo insuficiente!");
                }else{
                    action.getWhoClicked().sendMessage(ChatColor.GREEN + "Baú virtual melhorado com sucesso!");
                    this.user.setCash(this.user.getCash() - VirtualChestType.COMPLETE.getCost());
                    this.virtualChest.upgradeVirtualChest();
                    Rankup.getRankup().getRankPlayerManager().getDao().saveEntity(Rankup.getRankup().getRankPlayerManager().getRankupPlayer(player.getName()));
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
