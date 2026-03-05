package amorphia.alloygery.gear.item;

import amorphia.alloygery.gear.property.*;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import com.mojang.serialization.Codec;
import de.dafuqs.additionalentityattributes.AdditionalEntityAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public enum ImprovementTypes
{
	// TODO: make this data driven

    // tools
    TIPPED(0.1f),
    PLATED(0.2f),
    WRAPPED(0.3f),

    // armor
    PADDED(0.4f),
    ENGRAVED(0.5f),
    REINFORCED(0.6f),
    ;

    public static final Codec<ImprovementTypes> CODEC = Codec.STRING.xmap(ImprovementTypes::getByName, ImprovementTypes::getName);

    public static final ImprovementTypes[] VALUES_CACHE = ImprovementTypes.values();

    public static ImprovementTypes getByName(String name)
    {
        return Arrays.stream(VALUES_CACHE).filter(v -> v.name().equalsIgnoreCase(name)).findFirst().orElseThrow();
    }

    private final float type;

    ImprovementTypes(float type)
    {
        this.type = type;
    }

    public float getTypeFloat()
    {
        return type;
    }

    public String getName()
    {
        return name().toLowerCase(Locale.ROOT);
    }

	public void addImprovementProperties(List<Property> properties)
	{
//		switch (this)
//		{
//			case TIPPED -> {
//				properties.add(MiningLevelProperty.of(PartTypes.TOOL_HEAD, PropertyOperation.MULTIPLY_PART, 1.5f));
//			}
//			case PLATED -> {
//				properties.add(DurabilityProperty.of(PartTypes.TOOL_HEAD, PropertyOperation.MULTIPLY_PART, 2.0f));
//			}
//			case WRAPPED -> {
//				properties.add(AttributeProperty.of(ReachEntityAttributes.REACH, PartTypes.TOOL_IMPROVEMENT, PropertyOperation.MULTIPLY_TOTAL, 1.5f));
//				properties.add(AttributeProperty.of(ReachEntityAttributes.ATTACK_RANGE, PartTypes.TOOL_IMPROVEMENT, PropertyOperation.MULTIPLY_TOTAL, 1.5f));
//			}
//
//			case PADDED -> {
//				properties.add(FreezeProtectionProperty.of(PartTypes.ARMOR_IMPROVEMENT, PropertyOperation.MULTIPLY_PART, 1.5f));
//				properties.add(MobilityProperty.of(PartTypes.ARMOR_IMPROVEMENT, PropertyOperation.MULTIPLY_PART, 1.5f));
//			}
//			case ENGRAVED -> {
//
//			}
//			case REINFORCED -> {
//				properties.add(DurabilityProperty.of(PartTypes.ARMOR_IMPROVEMENT, PropertyOperation.MULTIPLY_PART, 2.0f));
//			}
//		}
	}
}
