package net.prosavage.savageskyblock.cmd;

import net.prosavage.savageskyblock.SavageSkyblock;
import net.prosavage.savageskyblock.util.GeneralUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


import java.util.ArrayList;
import java.util.List;

public class CommandManager implements CommandExecutor {

    private SavageSkyblock instance;

    private List<AbstractCommand> commands = new ArrayList<>();

    public CommandManager(SavageSkyblock instance) {
        this.instance = instance;




    }

    private AbstractCommand addCommand(AbstractCommand abstractCommand) {
        commands.add(abstractCommand);
        return abstractCommand;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        for (AbstractCommand abstractCommand : commands) {
            if (abstractCommand.getCommand().equalsIgnoreCase(command.getName())) {
                if (strings.length == 0) {
                    processPerms(abstractCommand, commandSender, strings);
                    return true;
                }
            } else if (strings.length != 0 && abstractCommand.getParent() != null && abstractCommand.getParent().getCommand().equalsIgnoreCase(command.getName())) {
                String cmd = strings[0];
                if (cmd.equalsIgnoreCase(abstractCommand.getCommand())) {
                    processPerms(abstractCommand, commandSender, strings);
                    return true;
                }
            }
        }
        commandSender.sendMessage(GeneralUtil.color("&b(!) &7The command you entered does not exist or is spelt incorrectly."));
        return true;
    }

    private void processPerms(AbstractCommand command, CommandSender sender, String[] strings) {
        if (command.getPermissionNode() == null || sender.hasPermission(command.getPermissionNode())) {
            command.runCommand(instance, sender, strings);
            return;
        }
        sender.sendMessage(GeneralUtil.color("&b(!) &7You do not have permission to perform this action."));

    }
}