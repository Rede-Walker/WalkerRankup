package br.com.redewalker.rankup.commands;

import br.com.redewalker.api.commands.Command;
import br.com.redewalker.rankup.Rankup;
import br.com.redewalker.rankup.objects.CoinsRanking;
import br.com.redewalker.rankup.objects.RankupPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.NumberFormat;

public class CoinsCommand extends Command {

    private Rankup main;

    public CoinsCommand(Rankup main){
        super("coins", "money");
        this.main = main;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {

        if(!(sender instanceof Player)){
            System.out.println("Apenas in-game!");
            return true;
        }

        Player player = (Player) sender;
        RankupPlayer rankupPlayer = main.getRankupPlayerManager().getRankupPlayer(sender.getName());

        if(args.length == 0){
            player.sendMessage(ChatColor.GREEN + "Coins: " + ChatColor.WHITE + NumberFormat.getInstance().format(rankupPlayer.getCoins()));
            return true;
        }

        if(args[0].equalsIgnoreCase("pay")){

        }else if(args[0].equalsIgnoreCase("help")){

        }else if(args[0].equalsIgnoreCase("top")){
            sendTop(sender);
        }else if(args[0].equalsIgnoreCase("set")){
            if(!player.hasPermission("walker.commands.setcoins")){
                player.sendMessage(ChatColor.RED + "Você não possui permissões!");
                return true;
            }

            if(args.length != 3){
                player.sendMessage(ChatColor.RED + "Formato incorreto! Utilize /coins set <nick> <quantia>.");
                return true;
            }

            String target = args[1];
            RankupPlayer rankupplayerTarget = main.getRankupPlayerManager().getRankupPlayer(target);
            if(rankupplayerTarget == null){
                player.sendMessage(ChatColor.RED + "Jogador não encontrado!");
                return true;
            }

            if(!isNumber(args[2])){
                player.sendMessage(ChatColor.RED + "Número inválido.");
                return true;
            }

            double coins = Double.parseDouble(args[2]);
            rankupplayerTarget.setCoins(coins);
            player.sendMessage(ChatColor.GREEN + "Você definiu as coins de " + ChatColor.WHITE + rankupplayerTarget.getName() + ChatColor.GREEN + " para " + ChatColor.WHITE + NumberFormat.getInstance().format(rankupplayerTarget.getCoins()) + ChatColor.GREEN + ".");


        }else if(args[0].equalsIgnoreCase("add")){

        }else if(args[0].equalsIgnoreCase("remove")){

        }else{
            String target = args[0];
            RankupPlayer rankupplayerTarget = main.getRankupPlayerManager().getRankupPlayer(target);
            if(rankupplayerTarget == null){
                player.sendMessage(ChatColor.RED + "Jogador não encontrado!");
                return true;
            }

            player.sendMessage(ChatColor.GREEN + "Coins de " + rankupplayerTarget.getName() + ": " + ChatColor.WHITE + NumberFormat.getInstance().format(rankupplayerTarget.getCoins()));

        }

        return false;
    }

    private boolean isNumber(String x){
        try{
            Double.parseDouble(x);
            return true;
        }catch(NumberFormatException ignored){}

        return false;
    }

    private void sendTop(CommandSender sender){
        sender.sendMessage(ChatColor.GREEN + "TOP Jogadores mais ricos do servidor " + ChatColor.GRAY + "(Atualizado a cada 10 minutos)");
        sender.sendMessage(" ");
        if(main.getCoinsRankingManager().getRanking().isEmpty()) return;
        for(int i = 0; i < 11; i++){
            if(i >= main.getCoinsRankingManager().getRanking().size()){
                break;
            }
            CoinsRanking coinsRanking = main.getCoinsRankingManager().getRanking().get(i);
            sender.sendMessage(coinsRanking.getPosition() + "º " + ChatColor.GREEN + coinsRanking.getName() + ": " + ChatColor.WHITE + NumberFormat.getInstance().format(coinsRanking.getCoins()));
        }
        sender.sendMessage(" ");
    }
}
