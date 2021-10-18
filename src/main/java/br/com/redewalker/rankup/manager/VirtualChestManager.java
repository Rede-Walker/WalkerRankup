package br.com.redewalker.rankup.manager;

import br.com.redewalker.rankup.Rankup;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VirtualChestManager {

    @Getter private final long virtualChestPrice;
    @Getter private final int maxVirtualChests;
    @Getter private final List<Material> iconList;

    public VirtualChestManager(Rankup rankup){
        this.virtualChestPrice = 1000;
        this.maxVirtualChests = 3;
        this.iconList = new ArrayList<>();
        loadIcons();
    }

    private void loadIcons(){
        iconList.add(Material.STONE); iconList.add(Material.GRASS); iconList.add(Material.BEDROCK); iconList.add(Material.SPONGE); iconList.add(Material.GLASS); iconList.add(Material.WOOD);
        iconList.add(Material.SANDSTONE); iconList.add(Material.BRICK); iconList.add(Material.BOOKSHELF); iconList.add(Material.OBSIDIAN); iconList.add(Material.EMERALD_BLOCK); iconList.add(Material.DIAMOND_BLOCK);
        iconList.add(Material.GOLD_BLOCK); iconList.add(Material.IRON_BLOCK); iconList.add(Material.LAPIS_BLOCK); iconList.add(Material.REDSTONE_BLOCK); iconList.add(Material.COAL_BLOCK);
        iconList.add(Material.PACKED_ICE); iconList.add(Material.PUMPKIN); iconList.add(Material.GLOWSTONE); iconList.add(Material.SMOOTH_BRICK); iconList.add(Material.ENDER_STONE); iconList.add(Material.NETHER_BRICK);
        iconList.add(Material.SEA_LANTERN); iconList.add(Material.SLIME_BLOCK); iconList.add(Material.BANNER); iconList.add(Material.BED); iconList.add(Material.POTION); iconList.add(Material.GOLDEN_APPLE);
        iconList.add(Material.CAKE); iconList.add(Material.TNT); iconList.add(Material.BEACON); iconList.add(Material.ENCHANTMENT_TABLE); iconList.add(Material.ENDER_PORTAL_FRAME); iconList.add(Material.ENDER_CHEST);
        iconList.add(Material.ANVIL); iconList.add(Material.DIAMOND); iconList.add(Material.ENDER_PEARL); iconList.add(Material.EYE_OF_ENDER); iconList.add(Material.EXP_BOTTLE); iconList.add(Material.ENCHANTED_BOOK);
        iconList.add(Material.BOOK); iconList.add(Material.FIREBALL); iconList.add(Material.BLAZE_POWDER); iconList.add(Material.NETHER_STAR); iconList.add(Material.DIAMOND_SWORD); iconList.add(Material.BOW);
        iconList.add(Material.DIAMOND_HELMET); iconList.add(Material.DIAMOND_CHESTPLATE); iconList.add(Material.DIAMOND_LEGGINGS); iconList.add(Material.DIAMOND_BOOTS); iconList.add(Material.DIAMOND_SPADE);
        iconList.add(Material.DIAMOND_PICKAXE); iconList.add(Material.DIAMOND_AXE);
    }

    public String InventoryToString(Inventory inventory) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeInt(inventory.getSize());

            // Save every element in the list
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }
    public Inventory StringToInventory(String data, int slots, String nome) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(null, slots, nome.replaceAll("&", ChatColor.COLOR_CHAR + ""));

            int bauzin = dataInput.readInt();
            // Read the serialized inventory
            for (int i = 0; i < bauzin; i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }

            dataInput.close();
            return inventory;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }
}
