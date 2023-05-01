package dev.nullpointercoding.zdeatharcade.Utils.VaultHookFolder;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.Utils.BankConfigManager;
import dev.nullpointercoding.zdeatharcade.Utils.PlayerConfigManager;
import io.papermc.paper.annotation.DoNotUse;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class VaultHook implements Economy{



    @Override
    public EconomyResponse bankBalance(String arg0) {
        BankConfigManager bankConfigManager = new BankConfigManager(arg0 + ".yml");
        YamlConfiguration bankConfig = (YamlConfiguration) bankConfigManager.getConfig();
        return new EconomyResponse(0,bankConfig.getDouble(arg0 + ".Bank_Account.Balance"),
        EconomyResponse.ResponseType.SUCCESS, "Successfully retrieved " + arg0 + "'s bank account balance!");
    }

    @Override
    public EconomyResponse bankDeposit(String arg0, double arg1) {
        BankConfigManager bankConfigManager = new BankConfigManager(arg0 + ".yml");
        YamlConfiguration bankConfig = (YamlConfiguration) bankConfigManager.getConfig();
        bankConfig.set(arg0 + ".Bank_Account.Balance", bankConfig.getDouble(arg0 + ".Bank_Account.Balance") + arg1);
        bankConfigManager.saveConfig();
        return new EconomyResponse(arg1, bankConfig.getDouble(arg0 + ".Bank_Account.Balance"), 
        EconomyResponse.ResponseType.SUCCESS, "Successfully deposited " + arg1 + " into " + arg0 + "'s bank account!");
    }

    @Override
    public EconomyResponse bankHas(String arg0, double arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'bankHas'");
    }

    @Override
    public EconomyResponse bankWithdraw(String arg0, double arg1) {
        BankConfigManager bankConfigManager = new BankConfigManager(arg0 + ".yml");
        YamlConfiguration bankConfig = (YamlConfiguration) bankConfigManager.getConfig();
        bankConfig.set(arg0 + ".Bank_Account.Balance", bankConfig.getDouble(arg0 + ".Bank_Account.Balance") - arg1);
        bankConfigManager.saveConfig();
        return new EconomyResponse(arg1, bankConfig.getDouble(arg0 + ".Bank_Account.Balance"), 
        EconomyResponse.ResponseType.SUCCESS, "Successfully withdrew " + arg1 + " from " + arg0 + "'s bank account!");
    }

    @Deprecated
    @Override
    public EconomyResponse createBank(String arg0, String arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createBank'");
    }

    @Override
    public EconomyResponse createBank(String arg0, OfflinePlayer op) {
        BankConfigManager bankConfigManager = new BankConfigManager(op.getUniqueId().toString() + ".yml");
        YamlConfiguration bankConfig = (YamlConfiguration) bankConfigManager.getConfig();
        bankConfig.set(op.getUniqueId().toString() + ".Bank_Account.Balance", (double) 0.0);
        bankConfig.set(op.getUniqueId().toString() + ".Bank_Account.Interest", (float) 0.82);
        bankConfigManager.saveConfig();
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.SUCCESS, "Bank created successfully!");

    }

    @Override
    @DoNotUse
    public boolean createPlayerAccount(String arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createPlayerAccount'");
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer arg0) {
        if(!(hasAccount(arg0))){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean createPlayerAccount(String arg0, String arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createPlayerAccount'");
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer arg0, String arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createPlayerAccount'");
    }

    @Override
    public String currencyNamePlural() {
        return new String("dollars");
    }

    @Override
    public String currencyNameSingular() {
        return new String("dollar");
    }

    @Override
    public EconomyResponse deleteBank(String arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteBank'");
    }

    @Override
    public EconomyResponse depositPlayer(String arg0, double arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'depositPlayer'");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer arg0, double arg1) {
        PlayerConfigManager pcm = new PlayerConfigManager(arg0.getUniqueId().toString());
        pcm.setBalance(pcm.getBalance() + arg1);
        return new EconomyResponse(arg1, pcm.getBalance(), EconomyResponse.ResponseType.SUCCESS, "Successfully deposited " + arg1 + " into " + arg0.getName() + "'s account!");
    }

    @Override
    public EconomyResponse depositPlayer(String arg0, String arg1, double arg2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'depositPlayer'");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer arg0, String arg1, double arg2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'depositPlayer'");
    }

    @Override
    public String format(double arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'format'");
    }

    @Override
    public int fractionalDigits() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fractionalDigits'");
    }

    @Override
    public double getBalance(String arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBalance'");
    }

    @Override
    public double getBalance(OfflinePlayer arg0) {
        PlayerConfigManager pcm = new PlayerConfigManager(arg0.getUniqueId().toString());
        return pcm.getBalance();
    }

    @Override
    public double getBalance(String arg0, String arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBalance'");
    }

    @Override
    public double getBalance(OfflinePlayer arg0, String arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBalance'");
    }

    @Override
    public List<String> getBanks() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBanks'");
    }

    @Override
    public String getName() {
        return new String("§4§lz§a§oDeath§b§oArcade§r");
    }

    @Override
    public boolean has(String arg0, double arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'has'");
    }

    @Override
    public boolean has(OfflinePlayer arg0, double arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'has'");
    }

    @Override
    public boolean has(String arg0, String arg1, double arg2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'has'");
    }

    @Override
    public boolean has(OfflinePlayer arg0, String arg1, double arg2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'has'");
    }

    @Override
    public boolean hasAccount(String arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasAccount'");
    }

    @Override
    public boolean hasAccount(OfflinePlayer arg0) {
        boolean exists = false;
        for(File f : Main.getInstance().getPlayerDataFolder().listFiles()){
            if(f.getName().equals(arg0.getUniqueId().toString())){
                exists = true;
                return true;
            }
            if(!(f.getName().equalsIgnoreCase(arg0.getUniqueId().toString()))){
                exists = false;
                return true;
            }
        }
        return exists;
    }

    @Override
    public boolean hasAccount(String arg0, String arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasAccount'");
    }

    @Override
    public boolean hasAccount(OfflinePlayer arg0, String arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasAccount'");
    }

    @Override
    public boolean hasBankSupport() {
        return true;
    }

    @Override
    public EconomyResponse isBankMember(String arg0, String arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isBankMember'");
    }

    @Override
    public EconomyResponse isBankMember(String arg0, OfflinePlayer arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isBankMember'");
    }

    @Override
    public EconomyResponse isBankOwner(String arg0, String arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isBankOwner'");
    }

    @Override
    public EconomyResponse isBankOwner(String arg0, OfflinePlayer arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isBankOwner'");
    }

    @Override
    public boolean isEnabled() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }else{
            return true;
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(String arg0, double arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'withdrawPlayer'");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer arg0, double arg1) {
        PlayerConfigManager pcm = new PlayerConfigManager(arg0.getUniqueId().toString());
        pcm.setBalance(pcm.getBalance() - arg1);
        return new EconomyResponse(arg1, pcm.getBalance(), EconomyResponse.ResponseType.SUCCESS, "Withdrawn successfully!");
    }

    @Override
    public EconomyResponse withdrawPlayer(String arg0, String arg1, double arg2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'withdrawPlayer'");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer arg0, String arg1, double arg2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'withdrawPlayer'");
    }
    
}
