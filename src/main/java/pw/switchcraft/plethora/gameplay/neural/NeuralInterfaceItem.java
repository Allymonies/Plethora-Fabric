package pw.switchcraft.plethora.gameplay.neural;

import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.items.IComputerItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.switchcraft.plethora.gameplay.BaseItem;
import pw.switchcraft.plethora.util.TinySlot;

import java.util.List;
import java.util.Objects;

import static pw.switchcraft.plethora.Plethora.MOD_ID;
import static pw.switchcraft.plethora.gameplay.neural.ComputerItemHandler.COMPUTER_ID;
import static pw.switchcraft.plethora.gameplay.neural.ComputerItemHandler.DIRTY;
import static pw.switchcraft.plethora.gameplay.neural.NeuralHelpers.ARMOR_SLOT;

public class NeuralInterfaceItem extends ArmorItem implements IComputerItem {
    private static final ArmorMaterial NEURAL_ARMOR_MATERIAL = new NeuralArmorMaterial();

    public NeuralInterfaceItem(Settings settings) {
        super(NEURAL_ARMOR_MATERIAL, ARMOR_SLOT, settings);
    }

    @Override
    public String getTranslationKey() {
        return "item." + MOD_ID + ".neuralInterface";
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(new TranslatableText(getTranslationKey(stack) + ".desc")
            .formatted(Formatting.GRAY));

        NbtCompound nbt = stack.getNbt();
        if (context.isAdvanced()) {
            if (nbt != null && nbt.contains(COMPUTER_ID)) {
                tooltip.add(new TranslatableText("gui.plethora.tooltip.computer_id", nbt.getInt(COMPUTER_ID)));
            }
        }
    }

    private static void onUpdate(ItemStack stack, TinySlot inventory, LivingEntity player, boolean forceActive) {
        if (player.getEntityWorld().isClient) {
            // Ensure the ClientComputer is available
            if (forceActive && player instanceof PlayerEntity) ComputerItemHandler.getClient(stack);
        } else {
            NbtCompound nbt = BaseItem.getNbt(stack);
            NeuralComputer neural;

            // Fetch computer
            if (forceActive) {
                neural = ComputerItemHandler.getServer(stack, player, inventory);
                neural.keepAlive();
            } else {
                neural = ComputerItemHandler.tryGetServer(stack);
                if (neural == null) return;
            }

            boolean dirty = false;

            // Sync computer ID
            int newId = neural.getID();
            if (!nbt.contains(COMPUTER_ID) || nbt.getInt(COMPUTER_ID) != newId) {
                nbt.putInt(COMPUTER_ID, newId);
                dirty = true;
            }

            // Sync Label
            String newLabel = neural.getLabel();
            String label = stack.hasCustomName() ? stack.getName().getString() : null;
            if (!Objects.equals(newLabel, label)) {
                if (newLabel == null || newLabel.isEmpty()) {
                    stack.removeCustomName();
                } else {
                    stack.setCustomName(new LiteralText(newLabel));
                }
                dirty = true;
            }

            // Sync and update peripherals
            short dirtyStatus = nbt.getShort(DIRTY);
            if (dirtyStatus != 0) {
                nbt.putShort(DIRTY, (short) 0);
                dirty = true;
            }

            if (neural.update(player, stack, dirtyStatus)) {
                dirty = true;
            }

            if (dirty && inventory != null) {
                inventory.markDirty();
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (entity instanceof LivingEntity living) {
            // 1.18: onArmorTick and onUpdate were merged into one combined inventoryTick. We need to force the tick if
            // the neural interface is in the helmet slot, but I don't want to hardcode the helmet slot ID, so here we
            // check if the ItemStack in the helmet slot is the same as the ItemStack being ticked, and if it is, we
            // force the computer to become active.
            TinySlot tinySlot;
            boolean forceActive = false;

            if (entity instanceof PlayerEntity player) {
                ItemStack headItem = living.getEquippedStack(EquipmentSlot.HEAD);
                tinySlot = new TinySlot.InventorySlot(stack, player.getInventory());
                forceActive = headItem == stack;
            } else {
                tinySlot = new TinySlot(stack);
            }

            onUpdate(stack, tinySlot, living, forceActive);
        }
    }

    @Override
    public int getComputerID(@NotNull ItemStack stack) {
        return IComputerItem.super.getComputerID(stack);
    }

    @Override
    public String getLabel(@NotNull ItemStack stack) {
        return stack.hasCustomName() ? stack.getName().getString() : null;
    }

    @Override
    public ComputerFamily getFamily() {
        return ComputerFamily.ADVANCED;
    }

    @Override
    public ItemStack withFamily(@NotNull ItemStack stack, @NotNull ComputerFamily family) {
        return stack;
    }
}
