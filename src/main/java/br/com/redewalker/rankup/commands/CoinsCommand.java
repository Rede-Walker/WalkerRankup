package br.com.redewalker.rankup.commands;

import br.com.redewalker.api.commands.Command;
import br.com.redewalker.api.systems.messages.MessageManager;
import br.com.redewalker.api.systems.messages.enums.Messages;
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
            MessageManager.instance().get(Messages.ONLY_PLAYERS_CAN).send(sender);
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
                MessageManager.instance().get(Messages.PLAYER_NOT_FOUND).send(player);
                return true;
            }

            if(rankupplayerTarget.getName().equalsIgnoreCase(player.getName())){
                MessageManager.instance().messageCustom(ChatColor.RED + "O jogador alvo não pode ser você!").send(player);
                return true;
            }

            if(!rankupplayerTarget.getPreferences().get(Preference.RECEIVE_COINS)){
                MessageManager.instance().messageCustom(ChatColor.RED + "O jogador informado desativou o recebimento de coins.").send(player);
                return true;
            }

            if(!isNumber(args[2])){
                MessageManager.instance().messageCustom(ChatColor.RED + "Número inválido.").send(player);
                return true;
            }

            double coins = Double.parseDouble(args[2]);

            if(coins <= 0){
                MessageManager.instance().messageCustom(ChatColor.RED + "O valor informado deve ser maior que 0.").send(player);
                return true;
            }


            if(rankPlayer.getCoins() < coins){
                MessageManager.instance().messageCustom(ChatColor.RED + "Saldo insuficiente.").send(player);
                return true;
            }

            rankPlayer.removeCoins(coins);
            rankupplayerTarget.addCoins(coins);
            MessageManager.instance().messageCustom(ChatColor.GREEN + "Você enviou " + ChatColor.WHITE + format(coins) + ChatColor.GREEN + " coins ao jogador " + ChatColor.WHITE +
                    rankupplayerTarget.getName() + ChatColor.GREEN + ".").send(player);
            if(Bukkit.getPlayer(rankupplayerTarget.getName()) != null) MessageManager.instance().messageCustom(
                    ChatColor.GREEN + "Você recebeu " + ChatColor.WHITE + format(rankupplayerTarget.getCoins()) +
                            ChatColor.GREEN + " coins do jogador " + ChatColor.WHITE + player.getName() + ChatColor.GREEN + ".").send(Bukkit.getPlayer(rankupplayerTarget.getName()));

        }else if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("ajuda")){

            // HELP SUBCOMMAND
            sendHelp(sender);


        }else if(args[0].equalsIgnoreCase("top")){

            // COINS TOP SUBCOMMAND
            sendTop(sender);


        }else if(args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("definir")){

            // SET SUBCOMMAND
            if(!player.hasPermission("walker.rankup.manageeconomy")){
                MessageManager.instance().get(Messages.WITHOUT_PERMISSION).send(player);
                return true;
            }

            if(args.length != 3){
                MessageManager.instance().messageCustom(ChatColor.RED + "Formato incorreto! Utilize /coins set <nick> <quantia>.").send(player);
                return true;
            }

            String target = args[1];
            RankPlayer rankupplayerTarget = main.getRankPlayerManager().getRankupPlayer(target);
            if(rankupplayerTarget == null){
                MessageManager.instance().get(Messages.PLAYER_NOT_FOUND).send(player);
                return true;
            }

            if(!isNumber(args[2])){
                MessageManager.instance().messageCustom(ChatColor.RED + "Número inválido.").send(player);
                return true;
            }

            double coins = Double.parseDouble(args[2]);

            if(coins < 0){
                MessageManager.instance().messageCustom(ChatColor.RED + "O valor informado deve ser maior ou igual a 0.").send(player);
                return true;
            }

            rankupplayerTarget.setCoins(coins);
            MessageManager.instance().messageCustom(ChatColor.GREEN + "Você definiu as coins de " + ChatColor.WHITE + rankupplayerTarget.getName()
                    + ChatColor.GREEN + " para " + ChatColor.WHITE + NumberFormat.getInstance().format(rankupplayerTarget.getCoins()) + ChatColor.GREEN + ".").send(player);
            if(Bukkit.getPlayer(rankupplayerTarget.getName()) != null) MessageManager.instance().messageCustom(
                    ChatColor.GREEN + "Suas coins foram definidas para " + ChatColor.WHITE + NumberFormat.getInstance().format(rankupplayerTarget.getCoins()) +
                            ChatColor.GREEN + ".").send(Bukkit.getPlayer(rankupplayerTarget.getName()));

        }else if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("adicionar")){

            // ADD SUBCOMMAND
            if(!player.hasPermission("walker.rankup.manageeconomy")){
                MessageManager.instance().get(Messages.WITHOUT_PERMISSION).send(player);
                return true;
            }

            if(args.length != 3){
                MessageManager.instance().messageCustom(ChatColor.RED + "Formato incorreto! Utilize /coins set <nick> <quantia>.").send(player);
                return true;
            }

            String target = args[1];
            RankPlayer rankupplayerTarget = main.getRankPlayerManager().getRankupPlayer(target);
            if(rankupplayerTarget == null){
                MessageManager.instance().get(Messages.PLAYER_NOT_FOUND).send(player);
                return true;
            }

            if(!isNumber(args[2])){
                MessageManager.instance().messageCustom(ChatColor.RED + "Número inválido.").send(player);
                return true;
            }

            double coins = Double.parseDouble(args[2]);

            if(coins <= 0){
                MessageManager.instance().messageCustom(ChatColor.RED + "O valor informado deve ser maior que 0.").send(player);
                return true;
            }


            rankupplayerTarget.addCoins(coins);
            MessageManager.instance().messageCustom(ChatColor.GREEN + "Você adicionou " + ChatColor.WHITE + format(coins) + ChatColor.GREEN + " coins ao jogador " +
                    ChatColor.WHITE + rankupplayerTarget.getName() + ChatColor.GREEN + ".").send(player);
            if(Bukkit.getPlayer(rankupplayerTarget.getName()) != null) MessageManager.instance().messageCustom(
                    ChatColor.GREEN + "Foram adicionados " + ChatColor.WHITE + NumberFormat.getInstance().format(rankupplayerTarget.getCoins()) +
                            ChatColor.GREEN + " coins na sua conta.").send(Bukkit.getPlayer(rankupplayerTarget.getName()));

        }else if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("remover")) {

            // REMOVE SUBCOMMAND
            if(!player.hasPermission("walker.rankup.manageeconomy")){
                MessageManager.instance().get(Messages.WITHOUT_PERMISSION).send(player);
                return true;
            }


            if(args.length != 3){
                MessageManager.instance().messageCustom(ChatColor.RED + "Formato incorreto! Utilize /coins set <nick> <quantia>.").send(player);
                return true;
            }

            String target = args[1];
            RankPlayer rankupplayerTarget = main.getRankPlayerManager().getRankupPlayer(target);
            if(rankupplayerTarget == null){
                MessageManager.instance().get(Messages.PLAYER_NOT_FOUND).send(player);
                return true;
            }

            if(!isNumber(args[2])){
                MessageManager.instance().messageCustom(ChatColor.RED + "Número inválido.").send(player);
                return true;
            }

            double coins = Double.parseDouble(args[2]);

            if(coins <= 0){
                MessageManager.instance().messageCustom(ChatColor.RED + "O valor informado deve ser maior que 0.").send(player);
                return true;
            }

            rankupplayerTarget.removeCoins(coins);
            MessageManager.instance().messageCustom(ChatColor.GREEN + "Você removeu " + ChatColor.WHITE + format(coins) + ChatColor.GREEN + " coins do jogador " +
                    ChatColor.WHITE + rankupplayerTarget.getName() + ChatColor.GREEN + ".").send(player);
            if(Bukkit.getPlayer(rankupplayerTarget.getName()) != null) MessageManager.instance().messageCustom(
                    ChatColor.GREEN + "Foram removidos " + ChatColor.WHITE + NumberFormat.getInstance().format(rankupplayerTarget.getCoins()) +
                            ChatColor.GREEN + " coins da sua conta.").send(player);

        }else if(args[0].equalsIgnoreCase("loadtop") || args[0].equalsIgnoreCase("carregartop")){

            // LOADTOP SUBCOMMAND
            if(!player.hasPermission("walker.rankup.manageeconomy")){
                MessageManager.instance().get(Messages.WITHOUT_PERMISSION).send(player);
                return true;
            }

            main.updateCoinsRanking();
            MessageManager.instance().messageCustom(ChatColor.GREEN + "Top de coins atualizado!").send(player);

        }else{

            // VISUALIZE SUBCOMMAND
            String target = args[0];
            RankPlayer rankupplayerTarget = main.getRankPlayerManager().getRankupPlayer(target);
            if(rankupplayerTarget == null){
                MessageManager.instance().get(Messages.PLAYER_NOT_FOUND).send(player);
                return true;
            }

            MessageManager.instance().messageCustom(ChatColor.GREEN + "Coins de " + rankupplayerTarget.getName() + ": " + ChatColor.WHITE + NumberFormat.getInstance().format(rankupplayerTarget.getCoins())).send(player);

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
