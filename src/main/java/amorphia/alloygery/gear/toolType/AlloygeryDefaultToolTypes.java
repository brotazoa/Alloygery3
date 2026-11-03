package amorphia.alloygery.gear.toolType;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.gear.item.PartTypes;
import amorphia.alloygery.gear.property.AttributeProperty;
import amorphia.alloygery.gear.property.PropertyOperation;
import com.google.common.collect.Lists;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import de.dafuqs.additionalentityattributes.AdditionalEntityAttributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.List;

public class AlloygeryDefaultToolTypes
{
	// basic
	public static final ToolType HATCHET;
	public static final ToolType KNIFE;

	// vanilla
	public static final ToolType AXE;
	public static final ToolType HOE;
	public static final ToolType PICKAXE;
	public static final ToolType SHOVEL;
	public static final ToolType SWORD;

	// 3x3 aoe
	public static final ToolType EXCAVATOR;
	public static final ToolType HAMMER;
	//public static final ToolType SCYTHE;

	// melee
	//public static final ToolType SPEAR;

	// bows
	//public static final ToolType BOW;
	//public static final ToolType LONGBOW;
	//public static final ToolType RECURVE;
	//public static final ToolType CROSSBOW;

	// shield
	//public static final ToolType SHIELD;
	//public static final ToolType BUCKLER;

	public static final List<ToolType> ALL_DEFAULT_TOOL_TYPES;

	private static ToolType register(ToolType toolType)
	{
		return ToolTypeRegistry.register(toolType.identifier, toolType);
	}

	private static ResourceLocation asToolType(String name)
	{
		return Alloygery.asResource("alloygery_tool_types/" + name);
	}

	static
	{
		HATCHET = register(new ToolType.ToolTypeBuilder(asToolType("hatchet"))
				.property(AttributeProperty.of(Attributes.ATTACK_DAMAGE, PartTypes.TOOL_TYPE, PropertyOperation.BASE, 3.0f))
				.property(AttributeProperty.of(Attributes.ATTACK_SPEED, PartTypes.TOOL_TYPE, PropertyOperation.BASE, -2.6f))
				.property(AttributeProperty.of(ReachEntityAttributes.ATTACK_RANGE, PartTypes.TOOL_TYPE, PropertyOperation.BASE, -1.0f, true))
				.property(AttributeProperty.of(ReachEntityAttributes.REACH, PartTypes.TOOL_TYPE, PropertyOperation.BASE, -1.0f, true))
				.build()
		);

		KNIFE = register(new ToolType.ToolTypeBuilder(asToolType("knife"))
				.property(AttributeProperty.of(Attributes.ATTACK_DAMAGE, PartTypes.TOOL_TYPE, PropertyOperation.BASE, 1.0f))
				.property(AttributeProperty.of(Attributes.ATTACK_SPEED, PartTypes.TOOL_TYPE, PropertyOperation.BASE, -0.4f))
				.property(AttributeProperty.of(ReachEntityAttributes.ATTACK_RANGE, PartTypes.TOOL_TYPE, PropertyOperation.BASE, -1.0f, true))
				.property(AttributeProperty.of(ReachEntityAttributes.REACH, PartTypes.TOOL_TYPE, PropertyOperation.BASE, -1.0f, true))
				.property(AttributeProperty.of(AdditionalEntityAttributes.BONUS_RARE_LOOT_ROLLS, PartTypes.TOOL_TYPE, PropertyOperation.BASE, 2.0f))
				.build()
		);

		AXE = register(new ToolType.ToolTypeBuilder(asToolType("axe"))
				.property(AttributeProperty.of(Attributes.ATTACK_DAMAGE, PartTypes.TOOL_TYPE, PropertyOperation.BASE, 5.0f))
				.property(AttributeProperty.of(Attributes.ATTACK_SPEED, PartTypes.TOOL_TYPE, PropertyOperation.BASE, -3.0f))
				.build()
		);

		HOE = register(new ToolType.ToolTypeBuilder(asToolType("hoe"))
				.property(AttributeProperty.of(Attributes.ATTACK_DAMAGE, PartTypes.TOOL_TYPE, PropertyOperation.BASE, -2.0f))
				.property(AttributeProperty.of(Attributes.ATTACK_SPEED, PartTypes.TOOL_TYPE, PropertyOperation.BASE, -2.8f))
				.build()
		);

		PICKAXE = register(new ToolType.ToolTypeBuilder(asToolType("pickaxe"))
				.property(AttributeProperty.of(Attributes.ATTACK_DAMAGE, PartTypes.TOOL_TYPE, PropertyOperation.BASE, 1.0f))
				.property(AttributeProperty.of(Attributes.ATTACK_SPEED, PartTypes.TOOL_TYPE, PropertyOperation.BASE, -2.8f))
				.build()
		);

		SHOVEL = register(new ToolType.ToolTypeBuilder(asToolType("shovel"))
				.property(AttributeProperty.of(Attributes.ATTACK_DAMAGE, PartTypes.TOOL_TYPE, PropertyOperation.BASE, 1.5f))
				.property(AttributeProperty.of(Attributes.ATTACK_SPEED, PartTypes.TOOL_TYPE, PropertyOperation.BASE, -3.0f))
				.build()
		);

		SWORD = register(new ToolType.ToolTypeBuilder(asToolType("sword"))
				.property(AttributeProperty.of(Attributes.ATTACK_DAMAGE, PartTypes.TOOL_TYPE, PropertyOperation.BASE, 3.0f))
				.property(AttributeProperty.of(Attributes.ATTACK_SPEED, PartTypes.TOOL_TYPE, PropertyOperation.BASE, -2.4f))
				.build()
		);

		EXCAVATOR = register(new ToolType.ToolTypeBuilder(asToolType("excavator"))
				.property(AttributeProperty.of(Attributes.ATTACK_DAMAGE, PartTypes.TOOL_TYPE, PropertyOperation.BASE, 2.0f))
				.property(AttributeProperty.of(Attributes.ATTACK_SPEED, PartTypes.TOOL_TYPE, PropertyOperation.BASE, -3.0f))
				.build()
		);

		HAMMER = register(new ToolType.ToolTypeBuilder(asToolType("hammer"))
				.property(AttributeProperty.of(Attributes.ATTACK_DAMAGE, PartTypes.TOOL_TYPE, PropertyOperation.BASE, 2.0f))
				.property(AttributeProperty.of(Attributes.ATTACK_SPEED, PartTypes.TOOL_TYPE, PropertyOperation.BASE, -3.0f))
				.build()
		);

		ALL_DEFAULT_TOOL_TYPES = List.of(HATCHET, KNIFE, AXE, HOE, PICKAXE, SHOVEL, SWORD, EXCAVATOR, HAMMER);
	}
}
