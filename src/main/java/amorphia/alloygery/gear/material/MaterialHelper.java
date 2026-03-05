package amorphia.alloygery.gear.material;

import amorphia.alloygery.gear.data.IAlloygeryMaterialData;
import amorphia.alloygery.gear.nbt.NBTHelper;
import amorphia.alloygery.gear.item.PartTypes;
import amorphia.alloygery.gear.property.*;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import de.dafuqs.additionalentityattributes.AdditionalEntityAttributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class MaterialHelper
{
    public static AlloygeryMaterial copy(AlloygeryMaterial original)
    {
        AlloygeryMaterial copy = new AlloygeryMaterial();
        copy.materialIdentifier = original.materialIdentifier;
        override(copy, original);
        return copy;
    }

    public static AlloygeryMaterial override(AlloygeryMaterial original, AlloygeryMaterial other)
    {
        if(other == null || other == original)
            return original;

        original.materialProperties.clear();
        original.materialPropertiesByPartType.clear();

        original.repairIngredient = other.repairIngredient;
        original.dyeable = other.dyeable;

        other.materialProperties.forEach(original::addProperty);

        return original;
    }

    public static AlloygeryMaterial merge(AlloygeryMaterial original, AlloygeryMaterial other)
    {
        if(other == null || other == original)
            return original;

        original.repairIngredient = other.repairIngredient;
        original.dyeable = other.dyeable;

        other.materialProperties.forEach(original::addProperty);

        return original;
    }

    public static IAlloygeryMaterialData createMaterialDataFromMaterial(AlloygeryMaterial material)
    {
        return new IAlloygeryMaterialData()
        {
            final AlloygeryMaterial dataHolder = copy(material);
            @Override
            public AlloygeryMaterial applyTo(AlloygeryMaterial applyToMaterial)
            {
                return override(applyToMaterial, dataHolder);
            }
        };
    }

    public static String getSimpleName(AlloygeryMaterial material)
    {
        return material.getMaterialIdentifier().getPath().replace("alloygery_material/", "");
    }

    public static AlloygeryMaterial getMaterialFromIdentifier(ResourceLocation identifier)
    {
        return AlloygeryMaterialRegistry.get(identifier);
    }

    public static Ingredient getRepairIngredientForMaterial(AlloygeryMaterial material)
    {
        return material == null ? Ingredient.EMPTY : material.repairIngredient;
    }

    public static AlloygeryMaterial getMaterialForPartType(PartTypes partType, ItemStack stack)
    {
        return getMaterialFromIdentifier(NBTHelper.getMaterialIdentifierFromTag(NBTHelper.getPartTagFromTag(partType, NBTHelper.getAlloygeryDataTag(stack))));
    }

    public static class MaterialBuilder
    {
        private final AlloygeryMaterial material;

        public MaterialBuilder(ResourceLocation identifier)
        {
            this.material = new AlloygeryMaterial();
            this.material.materialIdentifier = identifier;
        }

        public MaterialBuilder repairIngredientFromIngredient(Ingredient ingredient)
        {
            this.material.repairIngredient = ingredient == null ? Ingredient.EMPTY : ingredient;
            return this;
        }

        public MaterialBuilder repairIngredientFromItem(ItemLike item)
        {
            return repairIngredientFromIngredient(Ingredient.of(item));
        }

        public MaterialBuilder repairIngredientFromTag(TagKey<Item> tag)
        {
            return repairIngredientFromIngredient(Ingredient.of(tag));
        }

        public MaterialBuilder dyeable(boolean dyeable)
        {
            this.material.dyeable = dyeable;
            return this;
        }

		public MaterialBuilder toolAttributes(Attribute attribute, float base, float addition, float multiplier)
		{
			property(AttributeProperty.of(attribute, PartTypes.TOOL_HEAD, PropertyOperation.BASE, base));
			property(AttributeProperty.of(attribute, PartTypes.TOOL_BINDING, PropertyOperation.ADDITION, addition));
			property(AttributeProperty.of(attribute, PartTypes.TOOL_HANDLE, PropertyOperation.MULTIPLY_BASE, multiplier));
			return this;
		}

		public MaterialBuilder standardToolProperties(int durability, int miningLevel, int enchantability, float miningSpeed, float attackSpeed, float attackDamage)
		{
			return this.property(DurabilityProperty.of(PartTypes.TOOL_HEAD, PropertyOperation.BASE, durability))
					.property(MiningLevelProperty.of(PartTypes.TOOL_HEAD, PropertyOperation.BASE, miningLevel))
					.property(EnchantabilityProperty.of(PartTypes.TOOL_HEAD, PropertyOperation.BASE, enchantability))
					.property(MiningSpeedProperty.of(PartTypes.TOOL_HEAD, PropertyOperation.BASE, miningSpeed))
					.property(AttributeProperty.of(Attributes.ATTACK_SPEED, PartTypes.TOOL_HEAD, PropertyOperation.BASE, attackSpeed))
					.property(AttributeProperty.of(Attributes.ATTACK_DAMAGE, PartTypes.TOOL_HEAD, PropertyOperation.BASE, attackDamage));
		}

		public MaterialBuilder standardToolHandleProperties(float durability, float miningSpeed, float attackSpeed, float attackDamage, float blockReach, float attackReach)
		{
			return this.property(DurabilityProperty.of(PartTypes.TOOL_HANDLE, PropertyOperation.MULTIPLY_BASE, durability))
					.property(MiningSpeedProperty.of(PartTypes.TOOL_HANDLE, PropertyOperation.MULTIPLY_BASE, miningSpeed))
					.property(AttributeProperty.of(Attributes.ATTACK_SPEED, PartTypes.TOOL_HANDLE, PropertyOperation.MULTIPLY_BASE, attackSpeed))
					.property(AttributeProperty.of(Attributes.ATTACK_DAMAGE, PartTypes.TOOL_HANDLE, PropertyOperation.MULTIPLY_BASE, attackDamage))
					.property(AttributeProperty.of(ReachEntityAttributes.REACH, PartTypes.TOOL_HANDLE, PropertyOperation.MULTIPLY_BASE, blockReach))
					.property(AttributeProperty.of(ReachEntityAttributes.ATTACK_RANGE, PartTypes.TOOL_HANDLE, PropertyOperation.MULTIPLY_BASE, attackReach));
		}

		public MaterialBuilder standardToolBindingProperties(int durability, int miningLevel, int enchantability, float miningSpeed, float attackSpeed, float attackDamage)
		{
			return this.property(DurabilityProperty.of(PartTypes.TOOL_BINDING, PropertyOperation.ADDITION, durability))
					.property(MiningLevelProperty.of(PartTypes.TOOL_BINDING, PropertyOperation.ADDITION, miningLevel))
					.property(EnchantabilityProperty.of(PartTypes.TOOL_BINDING, PropertyOperation.ADDITION, enchantability))
					.property(MiningSpeedProperty.of(PartTypes.TOOL_BINDING, PropertyOperation.ADDITION, miningSpeed))
					.property(AttributeProperty.of(Attributes.ATTACK_SPEED, PartTypes.TOOL_BINDING, PropertyOperation.ADDITION, attackSpeed))
					.property(AttributeProperty.of(Attributes.ATTACK_DAMAGE, PartTypes.TOOL_BINDING, PropertyOperation.ADDITION, attackDamage));
		}

		public MaterialBuilder standardBaseArmorProperties(int durability, int enchantability, float armor, float toughness, float knockbackResistance)
		{
			return this.property(DurabilityProperty.of(PartTypes.ARMOR_BASE, PropertyOperation.BASE, durability))
					.property(EnchantabilityProperty.of(PartTypes.ARMOR_BASE, PropertyOperation.BASE, enchantability))
					.property(AttributeProperty.of(Attributes.ARMOR, PartTypes.ARMOR_BASE, PropertyOperation.BASE, armor))
					.property(AttributeProperty.of(Attributes.ARMOR_TOUGHNESS, PartTypes.ARMOR_BASE, PropertyOperation.BASE, toughness))
					.property(AttributeProperty.of(Attributes.KNOCKBACK_RESISTANCE, PartTypes.ARMOR_BASE, PropertyOperation.BASE, knockbackResistance));
		}

		public MaterialBuilder standardArmorPlateProperties(int durability, int enchantability, float armor, float toughness, float knockbackResistance)
		{
			return this.property(DurabilityProperty.of(PartTypes.ARMOR_PLATE, PropertyOperation.ADDITION, durability))
					.property(EnchantabilityProperty.of(PartTypes.ARMOR_PLATE, PropertyOperation.ADDITION, enchantability))
					.property(AttributeProperty.of(Attributes.ARMOR, PartTypes.ARMOR_PLATE, PropertyOperation.ADDITION, armor))
					.property(AttributeProperty.of(Attributes.ARMOR_TOUGHNESS, PartTypes.ARMOR_PLATE, PropertyOperation.ADDITION, toughness))
					.property(AttributeProperty.of(Attributes.KNOCKBACK_RESISTANCE, PartTypes.ARMOR_PLATE, PropertyOperation.ADDITION, knockbackResistance));
		}

        public MaterialBuilder property(Property property)
        {
            this.material.addProperty(property);
            return this;
        }

        public MaterialBuilder applyData(IAlloygeryMaterialData data)
        {
            if(data != null)
                data.applyTo(this.material);

            return this;
        }

        public AlloygeryMaterial build()
        {
            return material;
        }
    }
}
