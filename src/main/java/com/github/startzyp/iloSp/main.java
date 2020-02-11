package com.github.startzyp.iloSp;

import com.mchim.ItemLoreOrigin.API.ItemLoreAPI;
import com.mchim.ItemLoreOrigin.Event.ItemLoreDamageEvent;
import com.mchim.ItemLoreOrigin.Event.ItemLoreStatusEvent;
import com.mchim.ItemLoreOrigin.Event.ItemLoreTickEvent;
import com.mchim.ItemLoreOrigin.ItemLoreData.ItemLoreManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class main extends JavaPlugin implements Listener {

    private HashMap<Integer,String> info = new HashMap<>();

    @Override
    public void onEnable() {
        File config = new File(getDataFolder() + File.separator + "config.yml");
        if (!config.exists()) {
            getConfig().options().copyDefaults(true);
        }
        saveDefaultConfig();
        ReloadConfig();
        Bukkit.getServer().getPluginManager().registerEvents(this,this);
        super.onEnable();
    }

    private void ReloadConfig() {
        reloadConfig();
        Set<String> mines = getConfig().getConfigurationSection("iloSp").getKeys(false);
        for (String temp : mines) {
            int solt = Integer.parseInt(temp);
            info.put(solt, getConfig().getString("iloSp."+temp+".iloSpLore"));
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }


    @EventHandler
    public void damage(ItemLoreDamageEvent e) {
        if (e.getDamager() != null && e.getDamager() instanceof Player) {
            AddiloSp((Player) e.getDamager(), e.getDamagerManager());
        }

    }
    private void AddiloSp(Player p, List<ItemLoreManager> list) {
        if (list != null) {
            Set<Integer> integers = info.keySet();
            for (int num:integers){
                ItemStack item = p.getPlayer().getInventory().getItem(num);
                if (item!=null){
                    if (item.hasItemMeta()) {
                        ItemMeta meta = item.getItemMeta();
                        if (meta.hasLore()) {
                            String s = meta.getLore().toString();
                            if (s.contains(info.get(num))){
                                ItemLoreManager ilm = new ItemLoreManager(p);
                                ilm.initLoreData(item);
                                list.add(ilm);
                            }
                        }
                    }
                }
            }

        }
    }

    @EventHandler
    public void tick(ItemLoreTickEvent e) {
        this.AddiloSp(e.getEntity(), e.getManager());
    }

    @EventHandler
    public void status(ItemLoreStatusEvent e) {
        this.AddiloSp(e.getEntity(), e.getManager());
    }
}
