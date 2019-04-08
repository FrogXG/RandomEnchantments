package com.tfar.randomenchantments.ench.enchantment;

import com.tfar.randomenchantments.util.GlobalVars;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentArrowInfinite;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

import static com.tfar.randomenchantments.init.ModEnchantment.BACK_TO_THE_CHAMBER;

@Mod.EventBusSubscriber(modid = GlobalVars.MOD_ID)

public class EnchantmentBackToTheChamber extends Enchantment {
  public EnchantmentBackToTheChamber() {
    super(Rarity.RARE, EnumEnchantmentType.BOW, new EntityEquipmentSlot[]{
            EntityEquipmentSlot.MAINHAND
    });
    this.setRegistryName("back_to_the_chamber");
    this.setName("back_to_the_chamber");
  }

  @Override
  public int getMinEnchantability(int level) {
    return 5 + 10 * (level - 1);
  }

  @Override
  public int getMaxEnchantability(int level) {
    return super.getMinEnchantability(level) + 25;
  }

  @Override
  public int getMaxLevel() {
    return 5;
  }

  @Override
  public boolean canApplyTogether(Enchantment ench) {
    return !(ench instanceof EnchantmentArrowInfinite) && super.canApplyTogether(ench);
  }

  @SubscribeEvent
  public static void arrowHit(ProjectileImpactEvent event) {
    if (!(event.getRayTraceResult().entityHit instanceof EntityLivingBase)) return;
    Entity entity = event.getEntity();
    if (!(entity instanceof EntityArrow)) return;
    Entity shooter = ((EntityArrow) entity).shootingEntity;
    if (!(shooter instanceof EntityPlayer)) return;
    EntityPlayer player = (EntityPlayer) shooter;
    int level = EnchantmentHelper.getMaxEnchantmentLevel(BACK_TO_THE_CHAMBER, player);
    if (5 * Math.random() > level) return;
    if (!player.world.isRemote) {
      List<ItemStack> inventory = player.inventory.mainInventory;
      for (ItemStack stack : inventory) {
        if (!(stack.getItem() == Items.ARROW || stack.getItem() == Items.TIPPED_ARROW || stack.getItem() == Items.TIPPED_ARROW))
          continue;
        stack.grow(1);
        break;
      }
    }
  }
}
