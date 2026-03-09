package dev.nullpointercoding.zdeatharcade.Utils.VaultHookFolder;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.Utils.BankConfigManager;
import dev.nullpointercoding.zdeatharcade.Utils.PlayerConfigManager;
import net.milkbowl.vault2.economy.AccountPermission;
import net.milkbowl.vault2.economy.Economy;
import net.milkbowl.vault2.economy.EconomyResponse;
import net.milkbowl.vault2.economy.EconomyResponse.ResponseType;

public class VaultHook implements Economy {

    @Override
    public boolean accountSupportsCurrency(@NotNull String arg0, @NotNull UUID arg1, @NotNull String arg2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'accountSupportsCurrency'");
    }

    @Override
    public boolean accountSupportsCurrency(@NotNull String arg0, @NotNull UUID arg1, @NotNull String arg2,
            @NotNull String arg3) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'accountSupportsCurrency'");
    }

    @Override
    public boolean addAccountMember(@NotNull String arg0, @NotNull UUID arg1, @NotNull UUID arg2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addAccountMember'");
    }

    @Override
    public boolean addAccountMember(@NotNull String arg0, @NotNull UUID arg1, @NotNull UUID arg2,
            @NotNull AccountPermission... arg3) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addAccountMember'");
    }

    @Override
    public boolean createAccount(@NotNull UUID arg0, @NotNull String arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createAccount'");
    }

    @Override
    public boolean createAccount(@NotNull UUID arg0, @NotNull String arg1, boolean arg2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createAccount'");
    }

    @Override
    public boolean createAccount(@NotNull UUID arg0, @NotNull String arg1, @NotNull String arg2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createAccount'");
    }

    @Override
    public boolean createAccount(@NotNull UUID arg0, @NotNull String arg1, @NotNull String arg2, boolean arg3) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createAccount'");
    }

    @Override
    public boolean createSharedAccount(@NotNull String arg0, @NotNull UUID arg1, @NotNull String arg2,
            @NotNull UUID arg3) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSharedAccount'");
    }

    @Override
    public @NotNull Collection<String> currencies() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'currencies'");
    }

    @Override
    public @NotNull String defaultCurrencyNamePlural(@NotNull String arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'defaultCurrencyNamePlural'");
    }

    @Override
    public @NotNull String defaultCurrencyNameSingular(@NotNull String arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'defaultCurrencyNameSingular'");
    }

    @Override
    public boolean deleteAccount(@NotNull String arg0, @NotNull UUID arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAccount'");
    }

    @Override
    public @NotNull EconomyResponse deposit(@NotNull String arg0, @NotNull UUID arg1, @NotNull BigDecimal arg2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deposit'");
    }

    @Override
    public @NotNull EconomyResponse deposit(@NotNull String arg0, @NotNull UUID arg1, @NotNull String arg2,
            @NotNull BigDecimal arg3) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deposit'");
    }

    @Override
    public @NotNull EconomyResponse deposit(@NotNull String arg0, @NotNull UUID arg1, @NotNull String arg2,
            @NotNull String arg3, @NotNull BigDecimal arg4) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deposit'");
    }

    @Override
    public @NotNull String format(@NotNull BigDecimal arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'format'");
    }

    @Override
    public @NotNull String format(@NotNull String arg0, @NotNull BigDecimal arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'format'");
    }

    @Override
    public @NotNull String format(@NotNull BigDecimal arg0, @NotNull String arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'format'");
    }

    @Override
    public @NotNull String format(@NotNull String arg0, @NotNull BigDecimal arg1, @NotNull String arg2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'format'");
    }

    @Override
    public int fractionalDigits(@NotNull String arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fractionalDigits'");
    }

    @Override
    public Optional<String> getAccountName(@NotNull UUID arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAccountName'");
    }

    @Override
    public @NotNull BigDecimal getBalance(@NotNull String arg0, @NotNull UUID arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBalance'");
    }

    @Override
    public @NotNull BigDecimal getBalance(@NotNull String arg0, @NotNull UUID arg1, @NotNull String arg2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBalance'");
    }

    @Override
    public @NotNull BigDecimal getBalance(@NotNull String arg0, @NotNull UUID arg1, @NotNull String arg2,
            @NotNull String arg3) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBalance'");
    }

    @Override
    public @NotNull String getDefaultCurrency(@NotNull String arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDefaultCurrency'");
    }

    @Override
    public @NotNull String getName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getName'");
    }

    @Override
    public @NotNull Map<UUID, String> getUUIDNameMap() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUUIDNameMap'");
    }

    @Override
    public boolean has(@NotNull String arg0, @NotNull UUID arg1, @NotNull BigDecimal arg2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'has'");
    }

    @Override
    public boolean has(@NotNull String arg0, @NotNull UUID arg1, @NotNull String arg2, @NotNull BigDecimal arg3) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'has'");
    }

    @Override
    public boolean has(@NotNull String arg0, @NotNull UUID arg1, @NotNull String arg2, @NotNull String arg3,
            @NotNull BigDecimal arg4) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'has'");
    }

    @Override
    public boolean hasAccount(@NotNull UUID arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasAccount'");
    }

    @Override
    public boolean hasAccount(@NotNull UUID arg0, @NotNull String arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasAccount'");
    }

    @Override
    public boolean hasAccountPermission(@NotNull String arg0, @NotNull UUID arg1, @NotNull UUID arg2,
            @NotNull AccountPermission arg3) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasAccountPermission'");
    }

    @Override
    public boolean hasCurrency(@NotNull String arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasCurrency'");
    }

    @Override
    public boolean hasMultiCurrencySupport() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasMultiCurrencySupport'");
    }

    @Override
    public boolean hasSharedAccountSupport() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasSharedAccountSupport'");
    }

    @Override
    public boolean isAccountMember(@NotNull String arg0, @NotNull UUID arg1, @NotNull UUID arg2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isAccountMember'");
    }

    @Override
    public boolean isAccountOwner(@NotNull String arg0, @NotNull UUID arg1, @NotNull UUID arg2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isAccountOwner'");
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isEnabled'");
    }

    @Override
    public boolean removeAccountMember(@NotNull String arg0, @NotNull UUID arg1, @NotNull UUID arg2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeAccountMember'");
    }

    @Override
    public boolean renameAccount(@NotNull UUID arg0, @NotNull String arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'renameAccount'");
    }

    @Override
    public boolean renameAccount(@NotNull String arg0, @NotNull UUID arg1, @NotNull String arg2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'renameAccount'");
    }

    @Override
    public boolean setOwner(@NotNull String arg0, @NotNull UUID arg1, @NotNull UUID arg2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setOwner'");
    }

    @Override
    public boolean updateAccountPermission(@NotNull String arg0, @NotNull UUID arg1, @NotNull UUID arg2,
            @NotNull AccountPermission arg3, boolean arg4) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateAccountPermission'");
    }

    @Override
    public @NotNull EconomyResponse withdraw(@NotNull String arg0, @NotNull UUID arg1, @NotNull BigDecimal arg2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'withdraw'");
    }

    @Override
    public @NotNull EconomyResponse withdraw(@NotNull String arg0, @NotNull UUID arg1, @NotNull String arg2,
            @NotNull BigDecimal arg3) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'withdraw'");
    }

    @Override
    public @NotNull EconomyResponse withdraw(@NotNull String arg0, @NotNull UUID arg1, @NotNull String arg2,
            @NotNull String arg3, @NotNull BigDecimal arg4) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'withdraw'");
    }


}
