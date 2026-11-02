package amorphia.alloygery.advancement;

import amorphia.alloygery.datagen.AlloygeryEnglishLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class AdvancementEnglishLanguageProvider implements AlloygeryEnglishLanguageProvider.IAlloygeryEnglishLanguageProvider
{
	@Override
	public void generateTranslations(FabricLanguageProvider.TranslationBuilder translationBuilder)
	{
		// alloygery root
		makeTranslation("root", "Surviving With Alloygery", "Thank you for installing Alloygery", translationBuilder);

		// flint age
		makeTranslation("flint_age", "Flint Age", "Mine gravel to obtain flint, and harvest leaves to obtain sticks. Then craft a Flint Hatchet.", translationBuilder);

		// crafting
		makeTranslation("crafting", "Get Crafting", "Use your hatchet to harvest wood. Then craft and place a Crafting Table.", translationBuilder);

		// stone age
		translationBuilder.add("advancements.alloygery.mine_stone.description", "Craft a Flint Pickaxe and harvest stone.");

		// alloying
		makeTranslation("alloying", "Alloying", "Craft and place an Alloy Kiln", translationBuilder);

		// bronze age
		makeTranslation("bronze_age", "Bronze Age", "Make a Bronze Ingot by alloying Copper and Tin", translationBuilder);

		// bronze tools
		makeTranslation("bronze_tools", "Bronze Tools", "Craft a Bronze Pickaxe", translationBuilder);

		// smithing anvil
		makeTranslation("smithing_anvil", "Smiting Anvil", "Craft and place a Smithing Anvil", translationBuilder);

		// iron pickaxe
		translationBuilder.add("advancements.alloygery.iron_tools.description", "Craft an Iron Pickaxe");

		// suit up
		translationBuilder.add("advancements.alloygery.obtain_armor.description", "Protect yourself with a piece of armor");

		// shiny gear
		translationBuilder.add("advancements.alloygery.shiny_gear.description", "Get a full suit of Diamond armor");

		// diamond tipped
		makeTranslation("diamond_tipped", "Diamond Tipped", "Improve a pickaxe with a Tipped Improvement and a Diamond at the Smithing Table", translationBuilder);

		// tailoring
		makeTranslation("tailoring", "Tailoring", "Craft and place a Tailoring Table", translationBuilder);

		// plate up
		makeTranslation("plate_up", "Plate Up", "Apply Armor Plating to your Armor at the Crafting Table", translationBuilder);

		// heated exchange
		makeTranslation("heated_exchange", "Heated Exchange", "Craft and place a Heat Exchanger", translationBuilder);

		// cooking with conduction
		makeTranslation("cooking_with_conduction", "Cooking with Conduction", "Place an Alloy Kiln on top of a Heat Exchanger", translationBuilder);

		// steel age
		makeTranslation("steel_age", "Steel Age", "Make a Steel Ingot from alloying Iron and Charcoal", translationBuilder);

		// titanium age
		makeTranslation("titanium_age", "Titanium Age", "Smelt a Titanium Ingot", translationBuilder);

		// game changes root
		makeTranslation("game_changes", "Change, by Alloygery", "Major Progression Changes Made By Alloygery", translationBuilder);

		// game changes iron requires bronze
		makeTranslation("game_changes.iron_requires_bronze", "Iron Is Harder Than Stone", "Mining Iron requires a Bronze Pickaxe or better", translationBuilder);

		// game changes logs require tool
		makeTranslation("game_changes.logs_require_tool", "Punching Wood", "Harvesting wood requires a tool for item drops", translationBuilder);

		// game changes ore veins
		makeTranslation("game_changes.veins_of_ore", "It Comes In Veins?", "Alloygery adds ore veins for tin, copper, gold, iron, nickel, and titanium.", translationBuilder);

		// game changes alloying
		makeTranslation("game_changes.alloying", "Alloying", "Alloygery adds a way to smelt metals into alloys using the Alloy Kiln.", translationBuilder);

		// game changes bronze
		makeTranslation("game_changes.bronze", "Bronze", "A simple alloy of copper and tin, it is the first alloy that can be obtained. Tools made from bronze are the first able to mine iron ore, establishing bronze as a significant stepping stone in the development of a new world..", translationBuilder);

		// game changes antanium
		makeTranslation("game_changes.antanium", "Antanium", "A complex alloy of iron, copper, and gold, antanium strives to attain the benefits of its component parts while mitigating their weaknesses.", translationBuilder);

		// game changes steel
		makeTranslation("game_changes.steel", "Steel", "Made by adding carbon to iron, steel's high durability is unmatched even among late game alloys.", translationBuilder);

		// game changes constantan
		makeTranslation("game_changes.constantan", "Constantan", "An alloy of nickel and copper. Constantan shines in its ability to efficiently transfer heat.", translationBuilder);

		// game changes invar
		makeTranslation("game_changes.invar", "Invar", "A simple alloy of nickel and iron. Invar combines the best of nickel's tenacity, and iron's strength, and is one of the most formidable alloys for weapons and armor.", translationBuilder);

		// game changes titanium gold
		makeTranslation("game_changes.titanium_gold", "Titanium Gold", "As the name suggests, titanium gold is an alloy of titanium and gold. Enhancing titanium with gold's mining speed properties, titanium gold makes a great choice for mining tools without many compromises.", translationBuilder);

		// game changes nitinol
		makeTranslation("game_changes.nitinol", "Nitinol", "An alloy of titanium and nickel. Nitinol combines the devastating damage output of nickel, and the unbelievable speed of titanium. A solid choice for any tool or weapon part.", translationBuilder);
	}

	private void makeTranslation(String key, String title, String description, FabricLanguageProvider.TranslationBuilder translationBuilder)
	{
		translationBuilder.add("advancements.alloygery." + key + ".title", title);
		translationBuilder.add("advancements.alloygery." + key + ".description", description);
	}
}
