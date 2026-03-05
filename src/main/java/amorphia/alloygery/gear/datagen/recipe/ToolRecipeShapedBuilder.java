package amorphia.alloygery.gear.datagen.recipe;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.gear.item.PartTypes;
import amorphia.alloygery.gear.property.Property;
import amorphia.alloygery.gear.recipe.ToolRecipeShaped;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.*;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class ToolRecipeShapedBuilder extends CraftingRecipeBuilder implements RecipeBuilder
{
    private final RecipeCategory category;
    private final Item result;
    private final int count;
    private final List<String> rows = Lists.newArrayList();
    private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();
    @Nullable
    private String group;
    private boolean showNotification = true;

    private final Map<PartTypes, ItemStack> substituteIngredientsByPart = Maps.newLinkedHashMap();

    private final List<Property> properties = Lists.newArrayList();

    public ToolRecipeShapedBuilder(RecipeCategory category, ItemLike result, int count) {
        this.category = category;
        this.result = result.asItem();
        this.count = count;
    }

    public static ToolRecipeShapedBuilder shaped(RecipeCategory category, ItemLike result) {
        return shaped(category, result, 1);
    }

    public static ToolRecipeShapedBuilder shaped(RecipeCategory category, ItemLike result, int count) {
        return new ToolRecipeShapedBuilder(category, result, count);
    }

    public ToolRecipeShapedBuilder define(Character symbol, TagKey<Item> tag) {
        return this.define(symbol, Ingredient.of(tag));
    }

    public ToolRecipeShapedBuilder define(Character symbol, ItemLike item) {
        return this.define(symbol, Ingredient.of(new ItemLike[]{item}));
    }

    public ToolRecipeShapedBuilder define(Character symbol, Ingredient ingredient) {
        if (this.key.containsKey(symbol)) {
            throw new IllegalArgumentException("Symbol '" + symbol + "' is already defined!");
        } else if (symbol == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        } else {
            this.key.put(symbol, ingredient);
            return this;
        }
    }

    public ToolRecipeShapedBuilder pattern(String pattern) {
        if (!this.rows.isEmpty() && pattern.length() != ((String)this.rows.get(0)).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        } else {
            this.rows.add(pattern);
            return this;
        }
    }

    public ToolRecipeShapedBuilder substitute(PartTypes type, ItemStack substitute)
    {
        if(substitute == null || substitute.isEmpty())
            throw new IllegalArgumentException("Substitute ingredient shouldn't be empty or null");

        if(substituteIngredientsByPart.containsKey(type))
            throw new IllegalArgumentException("Substitute part type: " + type.getName() + " is already defined!");

        substituteIngredientsByPart.put(type, substitute);
        return this;
    }

    public ToolRecipeShapedBuilder property(Property property)
    {
        if(property == null)
            throw new IllegalArgumentException("Property shouldn't be null");

        properties.add(property);
        return this;
    }

    public @NotNull ToolRecipeShapedBuilder unlockedBy(@NotNull String criterionName, @NotNull CriterionTriggerInstance criterionTrigger) {
        this.advancement.addCriterion(criterionName, criterionTrigger);
        return this;
    }

    public @NotNull ToolRecipeShapedBuilder group(@Nullable String groupName) {
        this.group = groupName;
        return this;
    }

    public ToolRecipeShapedBuilder showNotification(boolean showNotification) {
        this.showNotification = showNotification;
        return this;
    }

    public @NotNull Item getResult() {
        return this.result;
    }

    public void save(Consumer<FinishedRecipe> finishedRecipeConsumer, @NotNull ResourceLocation recipeId) {
        this.ensureValid(recipeId);
        this.advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId)).rewards(net.minecraft.advancements.AdvancementRewards.Builder.recipe(recipeId)).requirements(
                RequirementsStrategy.OR);
        finishedRecipeConsumer.accept(new Result(recipeId, getResult(), this.count, this.group == null ? "" : this.group, determineBookCategory(this.category), this.rows, this.key, this.substituteIngredientsByPart, this.properties, this.advancement, recipeId.withPrefix("recipes/" + this.category.getFolderName() + "/"), this.showNotification));
    }

    private void ensureValid(ResourceLocation id) {
        if (this.rows.isEmpty()) {
            throw new IllegalStateException("No pattern is defined for shaped recipe " + id + "!");
        } else {
            Set<Character> set = Sets.newHashSet(this.key.keySet());
            set.remove(' ');

            for(String string : this.rows) {
                for(int i = 0; i < string.length(); ++i) {
                    char c = string.charAt(i);
                    if (!this.key.containsKey(c) && c != ' ') {
                        throw new IllegalStateException("Pattern in recipe " + id + " uses undefined symbol '" + c + "'");
                    }

                    set.remove(c);
                }
            }

            if (!set.isEmpty()) {
                throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe " + id);
            } else if (this.rows.size() == 1 && ((String)this.rows.get(0)).length() == 1) {
                throw new IllegalStateException("Shaped recipe " + id + " only takes in a single item - should it be a shapeless recipe instead?");
            } else if (this.advancement.getCriteria().isEmpty()) {
                throw new IllegalStateException("No way of obtaining recipe " + id);
            }
        }
    }

    static class Result extends CraftingRecipeBuilder.CraftingResult {
        private final ResourceLocation id;
        private final Item result;
        private final int count;
        private final String group;
        private final List<String> pattern;
        private final Map<Character, Ingredient> key;
        private final Map<PartTypes, ItemStack> sub;
        private final List<Property> properties;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;
        private final boolean showNotification;

        public Result(ResourceLocation id, Item result, int count, String group, CraftingBookCategory category, List<String> pattern, Map<Character, Ingredient> key, Map<PartTypes, ItemStack> sub, List<Property> properties, Advancement.Builder advancement, ResourceLocation advancementId, boolean showNotification) {
            super(category);
            this.id = id;
            this.result = result;
            this.count = count;
            this.group = group;
            this.pattern = pattern;
            this.key = key;
            this.sub = sub;
            this.properties = properties;
            this.advancement = advancement;
            this.advancementId = advancementId;
            this.showNotification = showNotification;
        }

        public void serializeRecipeData(@NotNull JsonObject json) {
            super.serializeRecipeData(json);
            if (!this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }

            JsonArray jsonArray = new JsonArray();
            for(String string : this.pattern) {
                jsonArray.add(string);
            }
            json.add("pattern", jsonArray);

            JsonObject jsonObject = new JsonObject();
            for(Map.Entry<Character, Ingredient> entry : this.key.entrySet()) {
                jsonObject.add(String.valueOf(entry.getKey()), ((Ingredient)entry.getValue()).toJson());
            }
            json.add("key", jsonObject);

            if(!this.sub.isEmpty())
            {
                JsonObject subObject = new JsonObject();
                for(Map.Entry<PartTypes, ItemStack> entry : this.sub.entrySet())
                {
					JsonObject itemObject = new JsonObject();
					itemObject.addProperty("item", BuiltInRegistries.ITEM.getKey(entry.getValue().getItem()).toString());
                    subObject.add(entry.getKey().getName(), itemObject);
                }
                json.add("substitute_parts", subObject);
            }

            if (!this.properties.isEmpty())
            {
                DataResult<JsonElement> dataResult = Property.CODEC.listOf().encodeStart(JsonOps.INSTANCE, this.properties);
                JsonElement element = dataResult.getOrThrow(false, Alloygery.LOGGER::error);
                if(!element.isJsonArray())
                    throw new IllegalArgumentException("Properties Element is not an Array");

                JsonArray array = element.getAsJsonArray();
                json.add("properties_on_result", array);
            }

            JsonObject jsonObject2 = new JsonObject();
            jsonObject2.addProperty("item", BuiltInRegistries.ITEM.getKey(this.result).toString());
            if (this.count > 1) {
                jsonObject2.addProperty("count", this.count);
            }
            json.add("result", jsonObject2);

            json.addProperty("show_notification", this.showNotification);
        }

        public @NotNull RecipeSerializer<?> getType() {
            return ToolRecipeShaped.Serializer.INSTANCE;
        }

        public @NotNull ResourceLocation getId() {
            return this.id;
        }

        @Nullable
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Nullable
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}
