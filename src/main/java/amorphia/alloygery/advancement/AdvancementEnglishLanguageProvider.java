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
		makeTranslation("iron_requires_bronze", "Mining Iron", "Mining Iron requires a Bronze Pickaxe or better", translationBuilder);

		// game changes logs require tool
		makeTranslation("logs_require_tool", "Punching Wood", "Harvesting wood requires a tool for item drops", translationBuilder);

		// game changes smithing requires steel
		makeTranslation("smithing_requires_steel", "Nether Gated Smithing", "The Smithing Table requires Steel Ingots to craft. Making it obtainable after going to the Nether.", translationBuilder);

		// game changes anvil requires steel
		makeTranslation("anvil_requires_steel", "Nether Gated Anvil", "The Anvil requires Steel Ingots to craft. Making it obtainable after going to the Nether.", translationBuilder);
	}

	private void makeTranslation(String key, String title, String description, FabricLanguageProvider.TranslationBuilder translationBuilder)
	{
		translationBuilder.add("advancements.alloygery." + key + ".title", title);
		translationBuilder.add("advancements.alloygery." + key + ".description", description);
	}
}
