package io.github.bluelhf.naturaldisasters;

import com.moderocky.mask.template.BukkitPlugin;
import io.github.bluelhf.naturaldisasters.command.NaturalDisastersCommand;
import io.github.bluelhf.naturaldisasters.disaster.EarthquakeDisaster;
import io.github.bluelhf.naturaldisasters.disaster.MeteoriteDisaster;
import io.github.bluelhf.naturaldisasters.disaster.NaturalDisaster;
import io.github.bluelhf.naturaldisasters.disaster.SinkholeDisaster;
import io.github.bluelhf.naturaldisasters.exception.DuplicateDisasterException;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NaturalDisasters extends BukkitPlugin {
    private static NaturalDisasters instance;
    private ArrayList<NaturalDisaster> registeredDisasters = new ArrayList<>();
    @Override
    public void startup() {
        instance = this;
        try {
            registerDisaster(new MeteoriteDisaster());
            registerDisaster(new SinkholeDisaster());
            registerDisaster(new EarthquakeDisaster());
        } catch (DuplicateDisasterException ignored) { /* We can ignore this since we ensured everything is unregistered on disable */ }
    }

    @Override
    protected void registerCommands() {
        register(new NaturalDisastersCommand());
    }

    @Override
    public void disable() {
        instance = null;
        registeredDisasters.clear();
    }

    public ArrayList<NaturalDisaster> getRegisteredDisasters() {
        return registeredDisasters;
    }

    @Nullable
    public NaturalDisaster getRegisteredDisaster(String name) {
        return registeredDisasters.stream().filter((d) -> d.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    }

    public void registerDisaster(NaturalDisaster disaster) throws DuplicateDisasterException {
        List<String> nameList = registeredDisasters.stream()
                .map(NaturalDisaster::getName)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        if (nameList.contains(disaster.getName().toLowerCase())) {
            throw new DuplicateDisasterException("Cannot register disaster " + disaster.getName().toLowerCase() + " because a disaster by that name is already registered!");
        }
        registeredDisasters.add(disaster);
    }

    public void unregisterDisaster(String name) {
        registeredDisasters.removeIf((d) -> d.getName().equalsIgnoreCase(name));
    }

    public static NaturalDisasters getInstance() {
        return instance;
    }
}
