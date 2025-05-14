package com.herrkatze.solsticeEconomy.modules.economy.integration.computercraft;

import dan200.computercraft.api.lua.IComputerSystem;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CCEvents {
    private static final Map<UUID,ArrayList<IComputerSystem>> computerBindingMap = new HashMap<>();

    public static void addComputer(UUID licenseKey, IComputerSystem computer) {
        if (!computerBindingMap.containsKey(licenseKey)) {
            var list = new ArrayList<IComputerSystem>();
            list.add(computer);
            computerBindingMap.put(licenseKey,list);
        }
        else {
            computerBindingMap.get(licenseKey).add(computer);
        }
    }
    public static void removeAllComputers(UUID licenseKey) {
        computerBindingMap.remove(licenseKey);
    }
    public static void removeComputer(IComputerSystem computer){
        for (Map.Entry<UUID,ArrayList<IComputerSystem>> i : computerBindingMap.entrySet()){
            i.getValue().remove(computer);
        }
    }
    public static void removeComputerFromLicense(UUID licenseKey, IComputerSystem computer){
        computerBindingMap.get(licenseKey).remove(computer);
    }
    public static void fireEvent(UUID owner, String name, @Nullable Object... objects){
        var licenseKey = LicenseManager.getKey(owner);
        var computers = computerBindingMap.get(licenseKey);
        if(computers == null) {
            return;
        }
        for (IComputerSystem computer:computers) {
            computer.queueEvent(name,objects);
        }
    }

}
