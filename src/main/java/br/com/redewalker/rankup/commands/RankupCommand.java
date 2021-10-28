package br.com.redewalker.rankup.commands;

import br.com.redewalker.api.commands.Command;
import br.com.redewalker.api.systems.messages.MessageManager;
import br.com.redewalker.api.systems.messages.enums.Messages;
import br.com.redewalker.rankup.Rankup;
import br.com.redewalker.rankup.systems.rank.events.PlayerRankupEvent;
import br.com.redewalker.rankup.systems.rankplayer.RankPlayer;
import br.com.redewalker.rankup.systems.rankplayer.exceptions.RankupException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankupCommand extends Command {

    public RankupCommand(){
        super("rankup");
    }


    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {

        if(!(sender instanceof Player)){
            MessageManager.instance().get(Messages.ONLY_PLAYERS_CAN).send(sender);
            return true;
        }

        Player player = (Player) sender;
        RankPlayer rankPlayer = Rankup.getRankup().getRankPlayerManager().getRankupPlayer(player.getName());

        if(args.length > 0){
            if(!player.hasPermission("walker.rankup.others")){
                MessageManager.instance().get(Messages.WITHOUT_PERMISSION).send(player);
                return true;
            }

            String target = args[0];
            RankPlayer rankPlayerTarget = Rankup.getRankup().getRankPlayerManager().getRankupPlayer(target);
            if(rankPlayerTarget == null){
                MessageManager.instance().get(Messages.PLAYER_NOT_FOUND).send(player);
                return true;
            }

            // do stuff
            return true;
        }


        try{

            rankPlayer.rankup();
            PlayerRankupEvent rankupEvent = new PlayerRankupEvent(player, rankPlayer.getRank());
            Bukkit.getPluginManager().callEvent(rankupEvent);

        }catch(RankupException e){
            MessageManager.instance().messageCustom("<error>" + e.getMessage()).send(player);
        }

        return false;
    }
}
