package br.com.redewalker.rankup.commands;

import br.com.redewalker.api.commands.Command;
import br.com.redewalker.rankup.Rankup;
import br.com.redewalker.rankup.systems.rankplayer.coins.CoinsRanking;
import br.com.redewalker.rankup.systems.rankplayer.RankPlayer;
import br.com.redewalker.rankup.systems.rankplayer.enums.Preference;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.NumberFormat;

public class CoinsCommand extends Command {

    private final Rankup main;

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
        RankPlayer rankPlayer = main.getRankPlayerManager().getRankupPlayer(sender.getName());

        if(args.length == 0){
            player.sendMessage(ChatColor.GREEN + "Coins: " + ChatColor.WHITE + NumberFormat.getInstance().format(rankPlayer.getCoins()));
            return true;
        }

        if(args[0].equalsIgnoreCase("pay") || args[0].equalsIgnoreCase("send") ||
                args[0].equalsIgnoreCase("enviar") || args[0].equalsIgnoreCase("pagar")){

            // PAY SUBCOMMAND
            if(args.length != 3){
                player.sendMessage(ChatColor.RED + "Formato incorreto! Utilize /coins pay <nick> <quantia>.");
                return true;
            }

            String target = args[1];
            RankPlayer rankupplayerTarget = main.getRankPlayerManager().getRankupPlayer(target);
            if(rankupplayerTarget == null){
                player.sendMessage(ChatColor.RED + "Jogador não encontrado!");
                return true;
            }

            if(rankupplayerTarget.getName().equalsIgnoreCase(player.getName())){
                player.sendMessage(ChatColor.RED + "O jogador informado não pode ser você.");
                return true;
            }

            if(!rankupplayerTarget.getPreferences().get(Preference.RECEIVE_COINS)){
                player.sendMessage(ChatColor.RED + "O jogador informado desativou o recebimento de coins.");
                return true;
            }

            if(!isNumber(args[2])){
                player.sendMessage(ChatColor.RED + "Número inválido.");
                return true;
            }

            double coins = Double.parseDouble(args[2]);

            if(coins <= 0){
                player.sendMessage(ChatColor.RED + "O valor informado deve ser maior que 0.");
                return true;
            }


            if(rankPlayer.getCoins() < coins){
                player.sendMessage(ChatColor.RED + "Saldo insuficiente.");
                return true;
            }

            rankPlayer.removeCoins(coins);
            rankupplayerTarget.addCoins(coins);
            player.sendMessage(ChatColor.GREEN + "Você enviou " + ChatColor.WHITE + format(coins) + ChatColor.GREEN + " coins ao jogador " + ChatColor.WHITE +
                    rankupplayerTarget.getName() + ChatColor.GREEN + ".");
            if(Bukkit.getPlayer(rankupplayerTarget.getName()) != null) Bukkit.getPlayer(rankupplayerTarget.getName()).sendMessage(
                    ChatColor.GREEN + "Você recebeu " + ChatColor.WHITE + format(rankupplayerTarget.getCoins()) +
                            ChatColor.GREEN + " coins do jogador " + ChatColor.WHITE + player.getName() + ChatColor.GREEN + ".");

        }else if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("ajuda")){

            // HELP SUBCOMMAND
            sendHelp(sender);


        }else if(args[0].equalsIgnoreCase("top")){

            // COINS TOP SUBCOMMAND
            sendTop(sender);


        }else if(args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("definir")){

            // SET SUBCOMMAND
            if(!player.hasPermission("walker.rankup.manageeconomy")){
                player.sendMessage(ChatColor.RED + "Você não possui permissões!");
                return true;
            }

            if(args.length != 3){
                player.sendMessage(ChatColor.RED + "Formato incorreto! Utilize /coins set <nick> <quantia>.");
                return true;
            }

            String target = args[1];
            RankPlayer rankupplayerTarget = main.getRankPlayerManager().getRankupPlayer(target);
            if(rankupplayerTarget == null){
                player.sendMessage(ChatColor.RED + "Jogador não encontrado!");
                return true;
            }

            if(!isNumber(args[2])){
                player.sendMessage(ChatColor.RED + "Número inválido.");
                return true;
            }

            double coins = Double.parseDouble(args[2]);

            if(coins < 0){
                player.sendMessage(ChatColor.RED + "O valor informado deve ser maior ou igual a 0.");
                return true;
            }

            rankupplayerTarget.setCoins(coins);
            player.sendMessage(ChatColor.GREEN + "Você definiu as coins de " + ChatColor.WHITE + rankupplayerTarget.getName()
                    + ChatColor.GREEN + " para " + ChatColor.WHITE + NumberFormat.getInstance().format(rankupplayerTarget.getCoins()) + ChatColor.GREEN + ".");
            if(Bukkit.getPlayer(rankupplayerTarget.getName()) != null) Bukkit.getPlayer(rankupplayerTarget.getName()).sendMessage(
                    ChatColor.GREEN + "Suas coins foram definidas para " + ChatColor.WHITE + NumberFormat.getInstance().format(rankupplayerTarget.getCoins()) +
                            ChatColor.GREEN + ".");

        }else if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("adicionar")){

            // ADD SUBCOMMAND
            if(!player.hasPermission("walker.rankup.manageeconomy")){
                player.sendMessage(ChatColor.RED + "Você não possui permissões!");
                return true;
            }

            if(args.length != 3){
                player.sendMessage(ChatColor.RED + "Formato incorreto! Utilize /coins set <nick> <quantia>.");
                return true;
            }

            String target = args[1];
            RankPlayer rankupplayerTarget = main.getRankPlayerManager().getRankupPlayer(target);
            if(rankupplayerTarget == null){
                player.sendMessage(ChatColor.RED + "Jogador não encontrado!");
                return true;
            }

            if(!isNumber(args[2])){
                player.sendMessage(ChatColor.RED + "Número inválido.");
                return true;
            }

            double coins = Double.parseDouble(args[2]);

            if(coins <= 0){
                player.sendMessage(ChatColor.RED + "O valor informado deve ser maior que 0.");
                return true;
            }


            rankupplayerTarget.addCoins(coins);
            player.sendMessage(ChatColor.GREEN + "Você adicionou " + ChatColor.WHITE + format(coins) + ChatColor.GREEN + " coins ao jogador " +
                    ChatColor.WHITE + rankupplayerTarget.getName() + ChatColor.GREEN + ".");
            if(Bukkit.getPlayer(rankupplayerTarget.getName()) != null) Bukkit.getPlayer(rankupplayerTarget.getName()).sendMessage(
                    ChatColor.GREEN + "Foram adicionados " + ChatColor.WHITE + NumberFormat.getInstance().format(rankupplayerTarget.getCoins()) +
                            ChatColor.GREEN + " coins na sua conta.");

        }else if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("remover")) {

            // REMOVE SUBCOMMAND
            if(!player.hasPermission("walker.rankup.manageeconomy")){
                player.sendMessage(ChatColor.RED + "Você não possui permissões!");
                return true;
            }


            if(args.length != 3){
                player.sendMessage(ChatColor.RED + "Formato incorreto! Utilize /coins set <nick> <quantia>.");
                return true;
            }

            String target = args[1];
            RankPlayer rankupplayerTarget = main.getRankPlayerManager().getRankupPlayer(target);
            if(rankupplayerTarget == null){
                player.sendMessage(ChatColor.RED + "Jogador não encontrado!");
                return true;
            }

            if(!isNumber(args[2])){
                player.sendMessage(ChatColor.RED + "Número inválido.");
                return true;
            }

            double coins = Double.parseDouble(args[2]);

            if(coins <= 0){
                player.sendMessage(ChatColor.RED + "O valor informado deve ser maior que 0.");
                return true;
            }

            rankupplayerTarget.removeCoins(coins);
            player.sendMessage(ChatColor.GREEN + "Você removeu " + ChatColor.WHITE + format(coins) + ChatColor.GREEN + " coins do jogador " +
                    ChatColor.WHITE + rankupplayerTarget.getName() + ChatColor.GREEN + ".");
            if(Bukkit.getPlayer(rankupplayerTarget.getName()) != null) Bukkit.getPlayer(rankupplayerTarget.getName()).sendMessage(
                    ChatColor.GREEN + "Foram removidos " + ChatColor.WHITE + NumberFormat.getInstance().format(rankupplayerTarget.getCoins()) +
                            ChatColor.GREEN + " coins da sua conta.");

        }else if(args[0].equalsIgnoreCase("loadtop") || args[0].equalsIgnoreCase("carregartop")){

            // LOADTOP SUBCOMMAND
            if(!player.hasPermission("walker.rankup.manageeconomy")){
                player.sendMessage(ChatColor.RED + "Você não possui permissões!");
                return true;
            }

            main.updateCoinsRanking();
            player.sendMessage(ChatColor.GREEN + "Top de coins atualizado!");

        }else{

            // VISUALIZE SUBCOMMAND
            String target = args[0];
            RankPlayer rankupplayerTarget = main.getRankPlayerManager().getRankupPlayer(target);
            if(rankupplayerTarget == null){
                player.sendMessage(ChatColor.RED + "Jogador não encontrado!");
                return true;
            }

            player.sendMessage(ChatColor.GREEN + "Coins de " + rankupplayerTarget.getName() + ": " + ChatColor.WHITE + NumberFormat.getInstance().format(rankupplayerTarget.getCoins()));

        }

        return false;
    }

    private String format(double coins){
        return NumberFormat.getInstance().format(coins);
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

    private void sendHelp(CommandSender sender){
        sender.sendMessage(ChatColor.GREEN + "Comandos de economia:");
        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.WHITE + "/coins - Veja suas coins.");
        sender.sendMessage(ChatColor.WHITE + "/coins <nick> - Veja as coins de um jogador.");
        sender.sendMessage(ChatColor.WHITE + "/coins top - Veja os jogadores mais ricos.");
        sender.sendMessage(ChatColor.WHITE + "/coins enviar <nick> <quantia> - Envie coins para um jogador.");
        if(sender.hasPermission("walker.rankup.manageeconomy")){
            sender.sendMessage(ChatColor.RED + "/coins definir <nick> <quantia> - Defina as coins de um jogador.");
            sender.sendMessage(ChatColor.RED + "/coins add <nick> <quantia> - Adicione coins para um jogador.");
            sender.sendMessage(ChatColor.RED + "/coins remove <nick> <quantia> - Remova coins de um jogador.");
            sender.sendMessage(ChatColor.RED + "/coins loadtop - Atualize o top coins.");
        }
        sender.sendMessage(" ");
    }
}
