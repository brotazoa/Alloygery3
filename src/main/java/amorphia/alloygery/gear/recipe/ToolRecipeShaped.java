package amorphia.alloygery.gear.recipe;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.gear.item.GearPartItem;
import amorphia.alloygery.gear.item.IDynamicTool;
import amorphia.alloygery.gear.item.PartTypes;
import amorphia.alloygery.gear.item.ToolHeadPartItem;
import amorphia.alloygery.gear.item.part.HandGuard;
import amorphia.alloygery.gear.item.part.ToolBinding;
import amorphia.alloygery.gear.item.part.ToolHandle;
import amorphia.alloygery.gear.nbt.AlloygeryNBTKeys;
import amorphia.alloygery.gear.nbt.NBTHelper;
import amorphia.alloygery.gear.property.Property;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class ToolRecipeShaped extends ShapedRecipe
{
    protected final ItemStack headPart;
    protected final ItemStack bindingPart;
    protected final ItemStack handlePart;
    protected final List<Property> properties;

    public ToolRecipeShaped(ShapedRecipe recipe, ItemStack headPart, ItemStack bindingPart, ItemStack handlePart, List<Property> properties)
    {
        super(recipe.getId(), recipe.getGroup(), recipe.category(), recipe.getWidth(), recipe.getHeight(), recipe.getIngredients(), recipe.getResultItem(RegistryAccess.EMPTY));
        this.headPart = headPart;
        this.bindingPart = bindingPart;
        this.handlePart = handlePart;
        this.properties = properties;
    }

    @Override
    public @NotNull ItemStack assemble(CraftingContainer container, @NotNull RegistryAccess registryAccess)
    {
		ItemStack headStack = headPart == null || headPart.isEmpty() ? container.getItems().stream().filter(stack -> stack.getItem() instanceof ToolHeadPartItem).findFirst().orElse(ItemStack.EMPTY) : headPart.copy();
		ItemStack bindingStack = bindingPart == null || bindingPart.isEmpty() ? container.getItems().stream().filter(stack -> stack.getItem() instanceof ToolBinding || stack.getItem() instanceof HandGuard).findFirst().orElse(ItemStack.EMPTY) : bindingPart.copy();
		ItemStack handleStack = handlePart == null || handlePart.isEmpty() ? container.getItems().stream().filter(stack -> stack.getItem() instanceof ToolHandle).findFirst().orElse(ItemStack.EMPTY) : handlePart.copy();

        ItemStack toolStack = super.getResultItem(registryAccess).copy();
        if(toolStack.isEmpty())
            return ItemStack.EMPTY;

        if (toolStack.getItem() instanceof IDynamicTool && headStack.getItem() instanceof ToolHeadPartItem headItem && handleStack.getItem() instanceof ToolHandle toolHandle)
        {
            if (bindingStack.getItem() instanceof GearPartItem gearPartItem)
            {
                toolStack.getOrCreateTag().put(AlloygeryNBTKeys.NBT, NBTHelper.createAlloygeryDataTag(
                        toolStack.getItem(),
                        NBTHelper.createPartsListTag(new PartTypes[] { PartTypes.TOOL_HEAD, PartTypes.TOOL_BINDING, PartTypes.TOOL_HANDLE }),
                        NBTHelper.createPartTag(headItem),
                        NBTHelper.createPartTag(bindingStack, gearPartItem.getPartType(), gearPartItem.getPartMaterial()),
                        NBTHelper.createPartTag(toolHandle)
                ));
            }
            else
            {
                toolStack.getOrCreateTag().put(AlloygeryNBTKeys.NBT, NBTHelper.createAlloygeryDataTag(
                        toolStack.getItem(),
                        NBTHelper.createPartsListTag(new PartTypes[] { PartTypes.TOOL_HEAD, PartTypes.TOOL_HANDLE }),
                        NBTHelper.createPartTag(headItem),
                        NBTHelper.createPartTag(toolHandle)
                ));
            }

            final Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(headStack);
            EnchantmentHelper.setEnchantments(enchantments, toolStack);

            final float damage = headStack.getMaxDamage() - headStack.getDamageValue();
            final float damageAsPercentOfMax = (damage/headStack.getMaxDamage());
            final float toolDamage = (toolStack.getMaxDamage() * damageAsPercentOfMax) - toolStack.getMaxDamage();
            toolStack.setDamageValue(Math.round(toolDamage));

            NBTHelper.setNBTProperties(toolStack, properties);
        }

        return toolStack;
    }

    public static class Type implements RecipeType<ToolRecipeShaped>
    {
        public static final ResourceLocation ID = Alloygery.asResource("tool_shaped");
        public static final Type INSTANCE = new Type();
        private Type() {} // no op
    }

    public static class Serializer extends ShapedRecipe.Serializer
    {
        public static final Serializer INSTANCE = new Serializer();

        private Serializer() {} // no op

        @Override
        public @NotNull ToolRecipeShaped fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json)
        {
            JsonObject partsObject = json.has("substitute_parts") ? json.getAsJsonObject("substitute_parts") : null;

			ItemStack head = partsObject == null ? null : partsObject.has(PartTypes.TOOL_HEAD.getName()) ? ShapedRecipe.itemStackFromJson(partsObject.getAsJsonObject(PartTypes.TOOL_HEAD.getName())) : null;
			ItemStack binding = partsObject == null ? null : partsObject.has(PartTypes.TOOL_BINDING.getName()) ? ShapedRecipe.itemStackFromJson(partsObject.getAsJsonObject(PartTypes.TOOL_BINDING.getName())) : null;
			ItemStack handle = partsObject == null ? null : partsObject.has(PartTypes.TOOL_HANDLE.getName()) ? ShapedRecipe.itemStackFromJson(partsObject.getAsJsonObject(PartTypes.TOOL_HANDLE.getName())) : null;

            JsonArray propertiesArray = json.has("properties_on_result") ? json.getAsJsonArray("properties_on_result") : null;
            List<Property> properties = Lists.newArrayList();
            if (propertiesArray != null)
            {
                DataResult<List<Property>> result = Property.CODEC.listOf().parse(JsonOps.INSTANCE, propertiesArray);
                result.result().ifPresent(properties::addAll);
            }

            return new ToolRecipeShaped(super.fromJson(recipeId, json), head, binding, handle, properties);
        }

        @Override
        public @NotNull ToolRecipeShaped fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer)
        {
            final ShapedRecipe shapedRecipe = super.fromNetwork(recipeId, buffer);
            final List<CompoundTag> propertiesTagList = buffer.readList(FriendlyByteBuf::readNbt);

			final ItemStack headPart = buffer.readItem();
			final ItemStack bindingPart = buffer.readItem();
			final ItemStack handlePart = buffer.readItem();

            List<Property> propertiesList = Lists.newArrayList();
            for(CompoundTag ptag : propertiesTagList)
            {
                DataResult<Property> result = Property.CODEC.parse(NbtOps.INSTANCE, ptag);
                Property p = result.getOrThrow(false, Alloygery.LOGGER::error);
                propertiesList.add(p);
            }

            return new ToolRecipeShaped(shapedRecipe, headPart, bindingPart, handlePart, propertiesList);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull ShapedRecipe recipe)
        {
            super.toNetwork(buffer, recipe);
			ToolRecipeShaped recipeAsToolRecipe = (ToolRecipeShaped) recipe;

			buffer.writeItem(recipeAsToolRecipe.headPart);
			buffer.writeItem(recipeAsToolRecipe.bindingPart);
			buffer.writeItem(recipeAsToolRecipe.handlePart);

            List<CompoundTag> propertiesTagList = Lists.newArrayList();
            for(Property p : ((ToolRecipeShaped) recipe).properties)
            {
                DataResult<Tag> result = Property.CODEC.encodeStart(NbtOps.INSTANCE, p);
                Tag propertyTag = result.getOrThrow(false, Alloygery.LOGGER::error);
                CompoundTag compoundTag = (CompoundTag) propertyTag;
                propertiesTagList.add(compoundTag);
            }

            buffer.writeCollection(propertiesTagList, FriendlyByteBuf::writeNbt);
        }
    }
}
