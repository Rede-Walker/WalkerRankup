package br.com.redewalker.rankup.commands;

import br.com.redewalker.api.commands.Command;
import br.com.redewalker.rankup.Rankup;
import br.com.redewalker.rankup.inventories.VirtualChestInventory;
import br.com.redewalker.rankup.objects.RankPlayer;
import br.com.redewalker.rankup.objects.VirtualChest;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChestCommand extends Command {

    public ChestCommand(){
        super("chest", "bau", "baus");
    }


    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {

        if(!(sender instanceof Player)){

            return true;
        }

        Player player = (Player) sender;
        if(args.length == 0){
            new VirtualChestInventory(player).open();
            return true;
        }

        String a = args[0];
        int id;

        try{
            id = Integer.parseInt(a);
        }catch(NumberFormatException e){
            player.sendMessage(ChatColor.RED + "Apenas números.");
            return true;
        }

        RankPlayer rankPlayer = Rankup.getRankup().getRankPlayerManager().getRankupPlayer(player.getName());
        if(rankPlayer.getVirtualChests().isEmpty()){
            player.sendMessage(ChatColor.RED + "Você ainda não possui um baú virtual, adquira um pelo menu em /baus.");
            return true;
        }

        VirtualChest virtualChest = getByID(id, rankPlayer);
        if(virtualChest == null){
            player.sendMessage(ChatColor.RED + "Você não possui o baú #" + id + "!");
        }else{
            player.openInventory(virtualChest.getInventory());
        }

        return false;
    }
    
    private VirtualChest getByID(int id, RankPlayer rankPlayer){
        if(!hasByID(id, rankPlayer)) return null;
        VirtualChest virtualChest = null;
        for(VirtualChest virtualChest1 : rankPlayer.getVirtualChests()){
            if(virtualChest1.getChestId() == id) virtualChest = virtualChest1;
        }
        return virtualChest;
    }
    
    private boolean hasByID(int id, RankPlayer rankPlayer){
        for(VirtualChest virtualChest : rankPlayer.getVirtualChests()){
            if(virtualChest.getChestId() == id) return true;
        }
        return false;
    }
}
