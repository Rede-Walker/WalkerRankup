package br.com.redewalker.rankup.systems.virtualchests;

import br.com.redewalker.rankup.Rankup;
import br.com.redewalker.rankup.systems.virtualchests.enums.VirtualChestType;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.io.IOException;

@Getter
public class VirtualChest {

    private String owner;
    private int chestId;
    private String chestInventory;
    private int chestSize;
    private String chestName;
    private String chestTitle;
    private Material chestIcon;

    public VirtualChest(String owner, int id) {
        this.owner = owner;
        this.chestId = id;
        this.chestInventory = Rankup.getRankup().getVirtualChestManager().InventoryToString(Bukkit.createInventory(null, 27));
        this.chestSize = 27;
        this.chestName = ChatColor.YELLOW + "Baú #" + this.chestId;
        this.chestTitle = ChatColor.stripColor(this.chestName);
        this.chestIcon = Material.CHEST;
    }

    public VirtualChestType getSizeType(){
        return VirtualChestType.getBySlots(this.chestSize);
    }

    public void setMaterial(Material material) { this.chestIcon = material;}
    public void setName(String name) { this.chestName = name; this.chestTitle = ChatColor.stripColor(name);}
    public void upgradeVirtualChest(){
        if(getSizeType() == VirtualChestType.COMPLETE) return;
        this.chestSize = 54;
    }
    public void setChestInventory(String name) { this.chestInventory = name; }


    public Inventory getInventory(){
        try{
            return Rankup.getRankup().getVirtualChestManager().StringToInventory(this.chestInventory, this.chestSize, this.chestTitle);
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
