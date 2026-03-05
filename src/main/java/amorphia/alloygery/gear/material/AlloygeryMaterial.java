package amorphia.alloygery.gear.material;

import amorphia.alloygery.gear.item.PartTypes;
import amorphia.alloygery.gear.property.Property;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AlloygeryMaterial
{
    private static final Codec<Ingredient> INGREDIENT_CODEC = Codec.PASSTHROUGH.comapFlatMap(dynamic -> {
        try
        {
            Ingredient ingredient = Ingredient.fromJson(dynamic.convert(JsonOps.INSTANCE).getValue());
            return DataResult.success(ingredient);
        }
        catch (Exception thrown)
        {
            return DataResult.error(thrown::getMessage);
        }
    }, ingredient -> new Dynamic<>(JsonOps.INSTANCE, ingredient.toJson()));

    public static final Codec<AlloygeryMaterial> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("material").forGetter(AlloygeryMaterial::getMaterialIdentifier),
            INGREDIENT_CODEC.fieldOf("repair_ingredient").forGetter(AlloygeryMaterial::getRepairIngredient),
            Codec.BOOL.fieldOf("dyeable").forGetter(AlloygeryMaterial::isDyeable),
            Property.CODEC.listOf().fieldOf("properties").forGetter(AlloygeryMaterial::getProperties)
    ).apply(instance, (resourceLocation, ingredient, dyeable, properties) -> {
        MaterialHelper.MaterialBuilder builder = new MaterialHelper.MaterialBuilder(resourceLocation);
        builder.repairIngredientFromIngredient(ingredient);
        builder.dyeable(dyeable);
        for(Property p : properties)
        {
            builder.property(p);
        }
        AlloygeryMaterial material = builder.build();
        return material;
    }));

    static final List<Property> EMPTY = List.of();

    ResourceLocation materialIdentifier;

    final List<Property> materialProperties = Lists.newArrayList();
    final Map<PartTypes, List<Property>> materialPropertiesByPartType = Maps.newHashMap();

    Ingredient repairIngredient = Ingredient.EMPTY;
    boolean dyeable = false;

    public ResourceLocation getMaterialIdentifier()
    {
        return materialIdentifier;
    }

    public String getIdentifierString()
    {
        return materialIdentifier.toString();
    }

    public List<Property> getProperties()
    {
        return Collections.unmodifiableList(materialProperties);
    }

    public List<Property> getPropertiesByPart(PartTypes partType)
    {
        final List<Property> properties = materialPropertiesByPartType.get(partType);
        return Collections.unmodifiableList(properties == null ? EMPTY : properties);
    }

    <P extends Property> void addProperty(P property)
    {
        if(property == null)
            return;

        Property duplicate = materialProperties.stream().filter(p -> p.equalsIgnoreValue(property)).findFirst().orElse(null);
        if(duplicate != null)
        {
            materialProperties.remove(duplicate);
            List<Property> propertiesForPart = materialPropertiesByPartType.computeIfAbsent(duplicate.getPartType(), list -> Lists.newArrayList());
            propertiesForPart.remove(duplicate);
        }

        materialProperties.add(property);
        List<Property> propertiesForPart = materialPropertiesByPartType.computeIfAbsent(property.getPartType(), list -> Lists.newArrayList());
        propertiesForPart.add(property);
    }

    public Ingredient getRepairIngredient()
    {
        return repairIngredient;
    }

    public boolean isDyeable()
    {
        return dyeable;
    }
}
