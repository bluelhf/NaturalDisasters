package io.github.bluelhf.naturaldisasters.command;

import com.moderocky.mask.command.ArgString;
import com.moderocky.mask.command.Commander;
import com.moderocky.mask.template.WrappedCommand;
import io.github.bluelhf.naturaldisasters.NaturalDisasters;
import io.github.bluelhf.naturaldisasters.disaster.LocationedDisaster;
import io.github.bluelhf.naturaldisasters.disaster.MeteoriteDisaster;
import io.github.bluelhf.naturaldisasters.disaster.NaturalDisaster;
import io.github.bluelhf.naturaldisasters.util.BlockUtils;
import org.bukkit.Location;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class NaturalDisastersCommand extends Commander<CommandSender> implements WrappedCommand {
    @Override
    public @NotNull List<String> getAliases() {
        return Arrays.asList("nd");
    }

    @Override
    public @NotNull String getUsage() {
        return "§cUsage: §7/naturaldisasters (create <disaster name>|list)";
    }

    @Override
    public @NotNull String getDescription() {
        return "Command to create or list natural disasters.";
    }

    @Override
    public @Nullable String getPermission() {
        return "naturaldisasters.naturaldisasters";
    }

    @Override
    public @Nullable String getPermissionMessage() {
        return "§cYou do not have permission to execute this commmand!";
    }

    @Override
    protected CommandImpl create() {
        return command("naturaldisasters")
                .arg("create",
                        arg((sender, input) -> {
                            NaturalDisaster disaster = NaturalDisasters.getInstance().getRegisteredDisaster((String) input[0]);
                            if (disaster == null) {
                                sender.sendMessage("§cThat disaster doesn't exist");
                                return;
                            }
                            if (disaster instanceof LocationedDisaster) {
                                // Special handling to set the location of locationed disasters
                                Location location = BlockUtils.senderLocation(sender);
                                if (location != null) ((LocationedDisaster) disaster).setLocation(location);
                            }
                            sender.sendMessage("§aOne " + disaster.getName().toLowerCase() + ", coming up!");
                            disaster.start();
                        }, new ArgString())
                )
                .arg("list", sender -> {
                    sender.sendMessage("§lNatural Disasters:");
                    int ctr = 1;
                    for (NaturalDisaster disaster : NaturalDisasters.getInstance().getRegisteredDisasters()) {
                        sender.sendMessage("  " + ctr + ". " + disaster.getName());
                        ctr++;
                    }
                });
    }

    @Override
    public CommandSingleAction<CommandSender> getDefault() {
        return sender -> sender.sendMessage("§lNatural Disasters v" + NaturalDisasters.getInstance().getVersion());
    }

    @Override
    public @NotNull String getCommand() {
        return "naturaldisasters";
    }

    @Override
    public @Nullable List<String> getCompletions(int i) {
        if (i == 1) return Arrays.asList("create", "list");
        return Collections.emptyList();
    }

    @Override
    public @Nullable List<String> getCompletions(int i, @NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (i == 1) return Arrays.asList("create", "list");
        if (i == 2 && args[0].equalsIgnoreCase("create")) {
            return NaturalDisasters.getInstance().getRegisteredDisasters().stream()
                    .map(NaturalDisaster::getName)
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return execute(sender, args);
    }
}
