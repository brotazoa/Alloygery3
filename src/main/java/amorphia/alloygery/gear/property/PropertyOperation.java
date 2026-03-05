package amorphia.alloygery.gear.property;

import com.mojang.serialization.Codec;

import java.util.Arrays;
import java.util.Locale;

public enum PropertyOperation
{
    BASE,
    ADDITION,
    MULTIPLY_BASE,
    MULTIPLY_TOTAL,
	MULTIPLY_PART,
    ;

    public static final Codec<PropertyOperation> CODEC = Codec.STRING.xmap(PropertyOperation::getByName, PropertyOperation::getName);

    public static final PropertyOperation[] VALUES_CACHE = PropertyOperation.values();

    public static PropertyOperation getByName(String name)
    {
        return Arrays.stream(VALUES_CACHE).filter(value -> value.name().equalsIgnoreCase(name)).findFirst().orElseThrow();
    }

    public String getName()
    {
        return name().toLowerCase(Locale.ROOT);
    }

	public static boolean isMultiplication(PropertyOperation operation)
	{
		return operation == MULTIPLY_BASE || operation == MULTIPLY_TOTAL || operation == MULTIPLY_PART;
	}

	public static boolean isAddition(PropertyOperation operation)
	{
		return operation == BASE || operation == ADDITION;
	}
}
