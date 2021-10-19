package br.com.redewalker.rankup.commands;

import br.com.redewalker.api.commands.Command;
import br.com.redewalker.api.systems.messages.MessageManager;
import br.com.redewalker.api.systems.messages.enums.Messages;
import br.com.redewalker.rankup.Rankup;
import br.com.redewalker.rankup.systems.virtualchests.inventories.VirtualChestAdminInventory;
import br.com.redewalker.rankup.systems.virtualchests.inventories.VirtualChestInventory;
import br.com.redewalker.rankup.systems.rankplayer.RankPlayer;
import br.com.redewalker.rankup.systems.virtualchests.VirtualChest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChestCommand extends Command {

    public ChestCommand(){
        super("chest", "bau", "baus");
    }


    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {

        if(!(sender instanceof Player)){
            MessageManager.instance().get(Messages.ONLY_PLAYERS_CAN).send(sender);
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

            if(player.hasPermission("walker.commands.adminchest")){

                RankPlayer rankPlayerTarget = Rankup.getRankup().getRankPlayerManager().getRankupPlayer(a);
                if(rankPlayerTarget == null){
                    MessageManager.instance().get(Messages.PLAYER_NOT_FOUND).send(player);
                    return true;
                }

                if(rankPlayerTarget.getVirtualChests().isEmpty()){
                    MessageManager.instance().messageCustom("<error>O jogador informado não possui baús.").send(player);
                    return true;
                }

                if(args.length == 1){
                    new VirtualChestAdminInventory(player, rankPlayerTarget).open();
                    return true;
                }

                String b = args[1];
                try{
                    id = Integer.parseInt(b);
                }catch(NumberFormatException ex){
                    MessageManager.instance().messageCustom("<error>Apenas números.").send(player);
                    return true;
                }

                VirtualChest virtualChest = getByID(id, rankPlayerTarget);
                if(virtualChest == null){
                    MessageManager.instance().messageCustom("<error>Esse jogador não possui o baú #" + id + ".").send(player);
                }else{
                    player.openInventory(virtualChest.getInventory());
                }

                return true;
            }

            MessageManager.instance().messageCustom("<error>Apenas números.").send(player);
            return true;
        }

        RankPlayer rankPlayer = Rankup.getRankup().getRankPlayerManager().getRankupPlayer(player.getName());
        if(rankPlayer.getVirtualChests().isEmpty()){
            MessageManager.instance().messageCustom("<error>Você ainda não possui um baú virtual, adquira um pelo menu em /baus.").send(player);
            return true;
        }

        VirtualChest virtualChest = getByID(id, rankPlayer);
        if(virtualChest == null){
            MessageManager.instance().messageCustom("<error>Você não possui o baú #" + id + ".").send(player);
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
