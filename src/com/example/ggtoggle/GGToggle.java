package com.example.ggtoggle;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.utils.Config;

import java.util.ArrayList;
import java.util.List;

public class GGToggle extends PluginBase {

    private Config data;
    private List<String> allowedPlayers;

    @Override
    public void onLoad() {
        this.getLogger().info(TextFormat.GREEN + "GGToggle плагин загружен!");
    }

    @Override
    public void onEnable() {
        // Создаём папку плагина если нет
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdirs();
        }
        
        // Загружаем список разрешённых игроков
        this.data = new Config(this.getDataFolder() + "/players.yml", Config.YAML);
        this.allowedPlayers = new ArrayList<>(data.getStringList("allowed"));
        
        this.getLogger().info(TextFormat.GREEN + "GGToggle плагин включен!");
        this.getLogger().info(TextFormat.AQUA + "Разрешённых игроков: " + allowedPlayers.size());
    }

    @Override
    public void onDisable() {
        saveData();
        this.getLogger().info(TextFormat.RED + "GGToggle плагин отключен!");
    }
    
    private void saveData() {
        data.set("allowed", allowedPlayers);
        data.save();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        // Команда /gm для игроков - переключить свой режим
        if (command.getName().equalsIgnoreCase("gm")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(TextFormat.RED + "Эту команду может использовать только игрок!");
                return true;
            }
            
            Player player = (Player) sender;
            String playerName = player.getName().toLowerCase();
            
            // Проверяем есть ли право
            if (!isAllowed(playerName)) {
                player.sendMessage(TextFormat.RED + "У тебя нет права менять режим! Попроси админа выдать через /gg " + player.getName());
                return true;
            }
            
            // Переключаем режим
            if (player.isCreative()) {
                player.setGamemode(Player.SURVIVAL);
                player.sendMessage(TextFormat.GREEN + "Режим изменен на ВЫЖИВАНИЕ!");
            } else {
                player.setGamemode(Player.CREATIVE);
                player.sendMessage(TextFormat.GREEN + "Режим изменен на ТВОРЧЕСКИЙ!");
            }
            
            return true;
        }
        
        // Команда /gg для админов
        if (command.getName().equalsIgnoreCase("gg")) {
            if (args.length < 1) {
                sender.sendMessage(TextFormat.YELLOW + "Использование:");
                sender.sendMessage(TextFormat.WHITE + "/gg <ник>" + TextFormat.GRAY + " - дать право менять режим");
                sender.sendMessage(TextFormat.WHITE + "/gg remove <ник>" + TextFormat.GRAY + " - забрать право");
                sender.sendMessage(TextFormat.WHITE + "/gg list" + TextFormat.GRAY + " - список игроков с правом");
                return true;
            }
            
            // /gg list - показать список
            if (args[0].equalsIgnoreCase("list")) {
                if (allowedPlayers.isEmpty()) {
                    sender.sendMessage(TextFormat.YELLOW + "Список пуст!");
                } else {
                    sender.sendMessage(TextFormat.GREEN + "Игроки с правом менять режим:");
                    for (String name : allowedPlayers) {
                        sender.sendMessage(TextFormat.WHITE + "- " + name);
                    }
                }
                return true;
            }
            
            // /gg remove <ник> - забрать право
            if (args[0].equalsIgnoreCase("remove")) {
                if (args.length < 2) {
                    sender.sendMessage(TextFormat.RED + "Использование: /gg remove <ник>");
                    return true;
                }
                
                String targetName = args[1].toLowerCase();
                
                if (!isAllowed(targetName)) {
                    sender.sendMessage(TextFormat.RED + "Игрок " + args[1] + " и так не имеет права!");
                    return true;
                }
                
                allowedPlayers.remove(targetName);
                saveData();
                
                sender.sendMessage(TextFormat.GREEN + "Право на смену режима забрано у игрока " + args[1]);
                
                // Если игрок онлайн - уведомить его
                Player target = this.getServer().getPlayerExact(args[1]);
                if (target != null) {
                    target.sendMessage(TextFormat.RED + "Твоё право менять режим было забрано!");
                }
                
                return true;
            }
            
            // /gg <ник> - дать право
            String targetName = args[0].toLowerCase();
            
            if (isAllowed(targetName)) {
                sender.sendMessage(TextFormat.RED + "Игрок " + args[0] + " уже имеет право!");
                return true;
            }
            
            allowedPlayers.add(targetName);
            saveData();
            
            sender.sendMessage(TextFormat.GREEN + "Игроку " + args[0] + " выдано право менять режим командой /gm");
            
            // Если игрок онлайн - уведомить его
            Player target = this.getServer().getPlayerExact(args[0]);
            if (target != null) {
                target.sendMessage(TextFormat.GREEN + "Тебе выдано право менять режим! Используй /gm");
            }
            
            return true;
        }

        return false;
    }
    
    private boolean isAllowed(String name) {
        return allowedPlayers.contains(name.toLowerCase());
    }
                                       }
