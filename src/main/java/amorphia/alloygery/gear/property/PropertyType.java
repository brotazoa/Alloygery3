package amorphia.alloygery.gear.property;

import amorphia.alloygery.Alloygery;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public interface PropertyType<P extends Property>
{
    ResourceKey<Registry<PropertyType<?>>> KEY = ResourceKey.createRegistryKey(Alloygery.asResource("property_types"));
    Registry<PropertyType<?>> REGISTRY = new MappedRegistry<>(KEY, Lifecycle.stable());
    Codec<PropertyType<?>> CODEC = PropertyType.REGISTRY.byNameCodec();

    PropertyType<AttributeProperty> ATTRIBUTE = register("attribute", AttributeProperty.CODEC);
    PropertyType<DurabilityProperty> DURABILITY = register("durability", DurabilityProperty.CODEC);
    PropertyType<EnchantabilityProperty> ENCHANTABILITY = register("enchantability", EnchantabilityProperty.CODEC);
	PropertyType<EncumbranceProperty> ENCUMBRANCE = register("encumbrance", EncumbranceProperty.CODEC);
    PropertyType<FireproofProperty> FIREPROOF = register("fireproof", FireproofProperty.CODEC);
    PropertyType<FireProtectionProperty> FIRE_PROTECTION = register("fire_protection", FireProtectionProperty.CODEC);
    PropertyType<FreezeProtectionProperty> FREEZE_PROTECTION = register("freeze_protection", FreezeProtectionProperty.CODEC);
    PropertyType<HoningProperty> HONING = register("honing", HoningProperty.CODEC);
    PropertyType<LovedByPiglinsProperty> LOVED_BY_PIGLINS = register("loved_by_piglins", LovedByPiglinsProperty.CODEC);
    PropertyType<MiningLevelProperty> MINING_LEVEL = register("mining_level", MiningLevelProperty.CODEC);
    PropertyType<MiningSpeedProperty> MINING_SPEED = register("mining_speed", MiningSpeedProperty.CODEC);
	PropertyType<MobilityProperty> MOBILITY = register("mobility", MobilityProperty.CODEC);
	PropertyType<OccludeVibrationsProperty> OCCLUDE_VIBRATIONS = register("occlude_vibrations", OccludeVibrationsProperty.CODEC);
    PropertyType<PrimitiveProperty> PRIMITIVE = register("primitive", PrimitiveProperty.CODEC);
    PropertyType<WalkOnPowderedSnowProperty> WALK_ON_POWDERED_SNOW = register("walk_on_powdered_snow", WalkOnPowderedSnowProperty.CODEC);

    Codec<P> codec();

    private static <P extends Property> PropertyType<P> register(String name, Codec<P> codec)
    {
        return register(Alloygery.asResource(name), codec);
    }

    static <P extends Property> PropertyType<P> register(ResourceLocation location, Codec<P> codec)
    {
        return Registry.register(REGISTRY, location, () -> codec);
    }
}
