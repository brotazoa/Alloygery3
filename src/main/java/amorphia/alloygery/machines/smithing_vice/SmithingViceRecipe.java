package amorphia.alloygery.machines.smithing_vice;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.craftingMaterials.item.DyeablePanelItem;
import amorphia.alloygery.gear.item.IDynamicGear;
import amorphia.alloygery.gear.item.IDynamicTool;
import amorphia.alloygery.gear.item.PartTypes;
import amorphia.alloygery.gear.item.ToolHeadPartItem;
import amorphia.alloygery.gear.item.armor.ArmorBaseItem;
import amorphia.alloygery.gear.item.armor.DyeableArmorBaseItem;
import amorphia.alloygery.gear.item.part.DyeableArmorPart;
import amorphia.alloygery.gear.item.part.DyeableToolBinding;
import amorphia.alloygery.gear.nbt.NBTHelper;
import amorphia.alloygery.machines.AbstractSingleIngredientMenu;
import amorphia.alloygery.machines.AbstractSingleIngredientRecipe;
import amorphia.alloygery.machines.MachinesModule;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Map;

public class SmithingViceRecipe extends AbstractSingleIngredientRecipe
{
	public SmithingViceRecipe(ResourceLocation id, ItemStack result, Ingredient ingredient, int materialCost)
	{
		super(Type.INSTANCE, MachinesModule.SMITHING_VICE_RECIPE_SERIALIZER, id, result, ingredient, materialCost);
	}

	@Override
	public ItemStack assemble(Container container, RegistryAccess registryAccess)
	{
		ItemStack inputStack = container.getItem(AbstractSingleIngredientMenu.INPUT_SLOT);
		ItemStack partStack = super.assemble(container, registryAccess);

		if (inputStack.getItem() instanceof IDynamicGear)
		{
			if (partStack.getItem() instanceof ToolHeadPartItem || partStack.getItem() instanceof ArmorBaseItem)
			{
				final Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(inputStack);
				EnchantmentHelper.setEnchantments(enchantments, partStack);

				final float damage = inputStack.getMaxDamage() - inputStack.getDamageValue();
				final float damage_as_percent_of_max = (damage/inputStack.getMaxDamage());
				final float part_damage = partStack.getMaxDamage() - (partStack.getMaxDamage() * damage_as_percent_of_max);
				partStack.setDamageValue(Math.round(part_damage));
			}

			if(partStack.getItem() instanceof DyeableToolBinding dyeableToolBinding)
			{
				final int color = NBTHelper.getPartDyeColorFromStack(inputStack, PartTypes.TOOL_BINDING);
				dyeableToolBinding.setColor(partStack, color);
			}

			if (partStack.getItem() instanceof DyeableArmorBaseItem dyeableArmorBaseItem)
			{
				final int color = NBTHelper.getPartDyeColorFromStack(inputStack, PartTypes.ARMOR_BASE);
				dyeableArmorBaseItem.setColor(partStack, color);
			}

			if (partStack.getItem() instanceof DyeableArmorPart dyeableArmorPart)
			{
				final int color = NBTHelper.getPartDyeColorFromStack(inputStack, PartTypes.ARMOR_PLATE);
				dyeableArmorPart.setColor(partStack, color);
			}

			if (partStack.getItem() instanceof DyeablePanelItem dyeablePanelItem)
			{
				final int color = NBTHelper.getPartDyeColorFromStack(inputStack, inputStack.getItem() instanceof IDynamicTool ? PartTypes.TOOL_IMPROVEMENT : PartTypes.ARMOR_IMPROVEMENT);
				dyeablePanelItem.setColor(partStack, color);
			}
		}
		else
		{
			final Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(inputStack);
			EnchantmentHelper.setEnchantments(enchantments, partStack);

			final float damage = inputStack.getMaxDamage() - inputStack.getDamageValue();
			final float damage_as_percent_of_max = (damage/inputStack.getMaxDamage());
			final float part_damage = partStack.getMaxDamage() - (partStack.getMaxDamage() * damage_as_percent_of_max);
			partStack.setDamageValue(Math.round(part_damage));
		}

		return partStack;
	}

	@Override
	public ItemStack getToastSymbol()
	{
		return new ItemStack(MachinesModule.SMITHING_VICE);
	}

	public static class Type implements RecipeType<SmithingViceRecipe>
	{
		private Type() {} // no op

		public static final ResourceLocation ID = Alloygery.asResource("smithing_vice");
		public static final SmithingViceRecipe.Type INSTANCE = new SmithingViceRecipe.Type();
	}

	public static class Serializer extends AbstractSingleIngredientRecipe.Serializer<SmithingViceRecipe>
	{
		public Serializer(SingleIngredientRecipeMaker<SmithingViceRecipe> factory)
		{
			super(factory);
		}
	}
}
