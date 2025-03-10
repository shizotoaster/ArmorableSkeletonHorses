package net.vipryx.mixin;

import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.HorseArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import net.vipryx.ArmorCheck;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@Mixin(SkeletonHorseEntity.class)
public abstract class SkeletonHorseMixin extends AbstractHorseEntity {
    protected SkeletonHorseMixin(EntityType<? extends AbstractHorseEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void onInventoryChanged(Inventory sender) {
        super.onInventoryChanged(sender);
        ItemStack armorStack = this.items.getStack(1);
        if (SkeletonHorseMixin.this.items.getStack(1) == ItemStack.EMPTY) {
            SkeletonHorseMixin.this.equipStack(EquipmentSlot.LEGS, ItemStack.EMPTY);
        }
        if (armorStack == null || !ArmorCheck.isHorseArmor(armorStack)) {
            return;
        }
        if (SkeletonHorseMixin.this.items.getStack(1).getItem() instanceof HorseArmorItem) {
            SkeletonHorseMixin.this.equipStack(EquipmentSlot.LEGS, SkeletonHorseMixin.this.items.getStack(1));
        }
        this.getAttributeInstance(EntityAttributes.GENERIC_ARMOR).setBaseValue(((HorseArmorItem) armorStack.getItem()).getBonus());
    }

    @Inject(at = @At("RETURN"), method = "writeCustomDataToNbt")
    public void saveArmor(NbtCompound nbt, CallbackInfo ci) {
        if(this.items.getStack(1) != ItemStack.EMPTY) {
            nbt.put("Armor", items.getStack(1).writeNbt(new NbtCompound()));
        }
    }

    @Inject(at = @At("RETURN"), method = "readCustomDataFromNbt")
    public void readArmor(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("Armor", 10)) {
            ItemStack itemStack = ItemStack.fromNbt(nbt.getCompound("Armor"));
            if (!itemStack.isEmpty() && this.isHorseArmor(itemStack)) {
                this.getAttributeInstance(EntityAttributes.GENERIC_ARMOR).setBaseValue(((HorseArmorItem)itemStack.getItem()).getBonus());
                this.equipStack(EquipmentSlot.LEGS, itemStack);
                this.items.setStack(1, itemStack);
            }
        }
    }

    @Override
    public boolean hasArmorSlot() {
        return true;
    }

//Unused implemented methods.
    @Override
    public boolean canBeSaddled() {
        return true;
    }

    @Override
    public boolean cannotBeSilenced() {
        return super.cannotBeSilenced();
    }

    @Override
    public @Nullable LivingEntity getOwner() {
        return super.getOwner();
    }

    @Override
    public <A> @Nullable A getAttached(AttachmentType<A> type) {
        return super.getAttached(type);
    }

    @Override
    public <A> A getAttachedOrThrow(AttachmentType<A> type) {
        return super.getAttachedOrThrow(type);
    }

    @Override
    public <A> A getAttachedOrSet(AttachmentType<A> type, A defaultValue) {
        return super.getAttachedOrSet(type, defaultValue);
    }

    @Override
    public <A> A getAttachedOrCreate(AttachmentType<A> type, Supplier<A> initializer) {
        return super.getAttachedOrCreate(type, initializer);
    }

    @Override
    public <A> A getAttachedOrCreate(AttachmentType<A> type) {
        return super.getAttachedOrCreate(type);
    }

    @Override
    public <A> A getAttachedOrElse(AttachmentType<A> type, @Nullable A defaultValue) {
        return super.getAttachedOrElse(type, defaultValue);
    }

    @Override
    public <A> A getAttachedOrGet(AttachmentType<A> type, Supplier<A> defaultValue) {
        return super.getAttachedOrGet(type, defaultValue);
    }

    @Override
    public <A> @Nullable A setAttached(AttachmentType<A> type, @Nullable A value) {
        return super.setAttached(type, value);
    }

    @Override
    public boolean hasAttached(AttachmentType<?> type) {
        return super.hasAttached(type);
    }

    @Override
    public <A> @Nullable A removeAttached(AttachmentType<A> type) {
        return super.removeAttached(type);
    }

    @Override
    public <A> @Nullable A modifyAttached(AttachmentType<A> type, UnaryOperator<A> modifier) {
        return super.modifyAttached(type, modifier);
    }

    @Override
    public int getJumpCooldown() {
        return super.getJumpCooldown();
    }

    @Override
    public SoundEvent getSaddleSound() {
        return super.getSaddleSound();
    }
}
