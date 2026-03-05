package amorphia.alloygery.gear.client;

import amorphia.alloygery.Alloygery;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;

import java.util.Map;

public class AlloygeryMaterialColors
{
    public static final AlloygeryMaterialColor UNKNOWN_COLOR = new AlloygeryMaterialColor(16253176);

    private static final Map<ResourceLocation, AlloygeryMaterialColor> ALLOYGERY_MATERIAL_COLORS = Maps.newHashMap();

	public static AlloygeryMaterialColor register(ResourceLocation identifier, AlloygeryMaterialColor color)
	{
		ALLOYGERY_MATERIAL_COLORS.put(identifier, color);
		return get(identifier);
	}

    private static AlloygeryMaterialColor register(ResourceLocation identifier, int color)
    {
		return register(identifier, AlloygeryMaterialColor.of(color));
    }

    private static AlloygeryMaterialColor register(String name, int color)
    {
        return  register(of(name), color);
    }

    private static ResourceLocation of(String name)
    {
        return Alloygery.asResource("alloygery_material/" + name);
    }

    public static AlloygeryMaterialColor get(ResourceLocation identifier)
    {
        return ALLOYGERY_MATERIAL_COLORS.getOrDefault(identifier, UNKNOWN_COLOR);
    }

    public static AlloygeryMaterialColor get(String name)
    {
        return ALLOYGERY_MATERIAL_COLORS.getOrDefault(of(name), UNKNOWN_COLOR);
    }

    public static AlloygeryMaterialColor getFromTrim(ItemStack itemStack)
    {
        if(itemStack == null || itemStack.isEmpty())
            return UNKNOWN_COLOR;

        ArmorTrim trim = ArmorTrim.getTrim(Minecraft.getInstance().level.registryAccess(), itemStack).orElse(null);
        return trim == null ? UNKNOWN_COLOR : getFromTrim(trim);
    }

    public static AlloygeryMaterialColor getFromTrim(ArmorTrim trim)
    {
        return get(trim.material().value().assetName());
    }

    public record AlloygeryMaterialColor(int color)
    {
        public static AlloygeryMaterialColor of(int color)
        {
            return new AlloygeryMaterialColor(color);
        }
    }

    static
    {
		// TODO: make this asset driven
        // meta
        register("hidden", 0);

        // metals
        register("tin", 14547455);
        register("copper", 15433553);
        register("bronze", 7556410);
        register("iron", 15198183);
        register("gold", 16573743);
        register("antanium", 14329677);
        register("steel", 4408907);
        register("netherite", 5192766);
        register("nickel", 6314062);
        register("invar", 10789019);
        register("constantan", 11558984);
        register("titanium", 5990506);
        register("titanium_gold", 13086590);
        register("nitinol", 6185051);

        // gems
        register("amethyst", 7294891);
        register("diamond", 3402699);
        register("emerald", 1564002);
        register("flint", 3026221);
        register("lapis", 5931746);
        register("redstone", 11144961);
        register("quartz", 16250354);
        register("prismarine", 10801602);

        // wood
        register("acacia", 12215095);
        register("bamboo", 6386476);
        register("birch", 14139781);
        register("cherry", 14135723);
        register("crimson", 8272470);
        register("dark_oak", 5190168);
        register("jungle", 12093284);
        register("mangrove", 7288111);
        register("mushroom_stem", 12828082);
        register("oak", 12096607);
        register("spruce", 8544570);
        register("stick", 6835742);
        register("warped", 3769218);

        // leather, pelts, and other animal drops
        register("leather", 10511680); // v 10511680 a 14117699
        register("rabbit_hide", 13082215);
        register("phantom_membrane", 9338746);
        register("turtle_scute", 4170818);
        register("wool", 16777215);

        // misc
        register("stone", 7697781);
        register("string", 15266815);
        register("paper", 16579826);
        register("bone", 16579565);
        register("dried_kelp", 3945252);
    }
}
