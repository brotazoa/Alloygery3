package amorphia.alloygery.machines.alloyKiln;

import amorphia.alloygery.machines.MachinesModule;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlloyKilnBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, RecipeHolder, StackedContentsCompatible
{
	private static final int[] TOP_SLOTS = new int[] { 0, 1, 2, 3 };
	private static final int[] SIDE_SLOTS = new int[] { 4 };
	private static final int[] BOTTOM_SLOTS = new int[] { 5 };

	protected static final int FUEL_SLOT = 4;
	protected static final int OUTPUT_SLOT = 5;

	private static int getSmeltingDuration(Level level, AlloyKilnBlockEntity blockEntity)
	{
		return blockEntity.quickCheck.getRecipeFor(blockEntity, level).map(SimpleAlloyingRecipe::getSmeltingTime).orElse(200);
	}

	public static boolean canSmelt(AlloyKilnBlockEntity blockEntity, SimpleAlloyingRecipe recipe, Level level)
	{
		ItemStack actual = blockEntity.getItem(OUTPUT_SLOT);
		ItemStack expected = recipe.getResultItem(level.registryAccess());
		return recipe.matches(blockEntity, level) && (actual.isEmpty() || ItemStack.isSameItem(actual, expected) && actual.getCount() + expected.getCount() <= actual.getMaxStackSize());
	}

	protected NonNullList<ItemStack> inventory = NonNullList.withSize(6, ItemStack.EMPTY);
	protected int litTimeRemaining;
	protected int litDuration;
	protected int smeltingProgress;
	protected int smeltingDuration;

	protected final ContainerData dataAccess = new ContainerData() {
		@Override
		public int get(int index)
		{
			return switch (index)
			{
				case 0 -> AlloyKilnBlockEntity.this.litTimeRemaining;
				case 1 -> AlloyKilnBlockEntity.this.litDuration;
				case 2 -> AlloyKilnBlockEntity.this.smeltingProgress;
				case 3 -> AlloyKilnBlockEntity.this.smeltingDuration;
				default -> 0;
			};
		}

		@Override
		public void set(int index, int value)
		{
			switch (index)
			{
				case 0:
				{
					AlloyKilnBlockEntity.this.litTimeRemaining = value;
					break;
				}
				case 1:
				{
					AlloyKilnBlockEntity.this.litDuration = value;
					break;
				}
				case 2:
				{
					AlloyKilnBlockEntity.this.smeltingProgress = value;
					break;
				}
				case 3:
				{
					AlloyKilnBlockEntity.this.smeltingDuration = value;
					break;
				}
			}
		}

		@Override
		public int getCount()
		{
			return 4;
		}
	};

	private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();
	private final RecipeManager.CachedCheck<Container, SimpleAlloyingRecipe> quickCheck;

	public AlloyKilnBlockEntity(BlockPos pos, BlockState blockState)
	{
		super(MachinesModule.ALLOY_KILN_BLOCK_ENTITY, pos, blockState);
		this.quickCheck = RecipeManager.createCheck(SimpleAlloyingRecipe.Type.INSTANCE);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, AlloyKilnBlockEntity blockEntity)
	{
		boolean wasLit = blockEntity.isLit();
		boolean changed = false;

		// decrease lit time remaining clamped at 0
		blockEntity.litTimeRemaining = blockEntity.litTimeRemaining > 0 ? blockEntity.litTimeRemaining - 1 : blockEntity.litTimeRemaining;

		// check for valid recipe
		Optional<SimpleAlloyingRecipe> recipeOptional = blockEntity.quickCheck.getRecipeFor(blockEntity, level);
		if (recipeOptional.isPresent() && canSmelt(blockEntity, recipeOptional.get(), level))
		{
			// set smelting duration to smelting time from recipe
			blockEntity.smeltingDuration = recipeOptional.get().getSmeltingTime();

			// consume fuel if needed
			if(blockEntity.burnFuel()) changed = true;

			// if lit time remaining, increase smelting progress
			blockEntity.smeltingProgress = blockEntity.litTimeRemaining > 0 ? blockEntity.smeltingProgress + 1 :
					// if no lit time remaining, decrease smelting progress clamped at 0
					blockEntity.smeltingProgress > 0 ? blockEntity.smeltingProgress - 1 : blockEntity.smeltingProgress;

			// if smelting progress is higher than smelting duration, craft output
			if (blockEntity.smeltingProgress >= blockEntity.smeltingDuration)
			{
				changed = true;

				// reset smelting progress
				blockEntity.smeltingProgress = 0;

				// remove recipe ingredients from input
				recipeOptional.get().getIngredients().forEach(blockEntity::removeIngredient);

				// update the output stack
				ItemStack outputStack = blockEntity.getItem(OUTPUT_SLOT);
				ItemStack result = recipeOptional.get().getResultItem(level.registryAccess());
				if (outputStack.isEmpty())
				{
					blockEntity.setItem(OUTPUT_SLOT, result);
				}
				else
				{
					outputStack.grow(result.getCount());
				}

				// set the last used recipe
				blockEntity.setRecipeUsed(recipeOptional.get());
			}
		}
		else blockEntity.smeltingProgress = 0;

		// update lit property
		if (wasLit != blockEntity.isLit())
		{
			changed = true;
			state = state.setValue(AlloyKilnBlock.LIT, blockEntity.isLit());
			level.setBlock(pos, state, 3);
		}

		// if changed, send updates
		if (changed) setChanged(level, pos, state);
	}

	/**
	 *
	 * @return true if new fuel was consumed
	 */
	protected boolean burnFuel()
	{
		if(this.litTimeRemaining > 0) return false;

		ItemStack fuelStack = this.inventory.get(FUEL_SLOT);
		if(fuelStack.isEmpty()) return false;

		this.litTimeRemaining = AbstractFurnaceBlockEntity.getFuel().getOrDefault(fuelStack.getItem(), 0);
		this.litDuration = this.litTimeRemaining;

		if (litTimeRemaining > 0)
		{
			Item fuelItem = fuelStack.getItem();
			fuelStack.shrink(1);
			if (fuelStack.isEmpty())
			{
				Item remainder = fuelItem.getCraftingRemainingItem();
				this.inventory.set(FUEL_SLOT, remainder == null ? ItemStack.EMPTY : new ItemStack(remainder));
			}

			return true;
		}

		return false;
	}

	protected boolean isLit()
	{
		return this.smeltingProgress > 0;
	}

	public void dropExperience(ServerPlayer player, ServerLevel level, Vec3 position)
	{
		if(recipesUsed == null || recipesUsed.isEmpty()) return;

		List<Recipe<?>> recipes = new ArrayList<>();
		for(Object2IntMap.Entry<ResourceLocation> entry : this.recipesUsed.object2IntEntrySet())
		{
			level.getRecipeManager().byKey(entry.getKey()).ifPresent(recipe -> {
				recipes.add(recipe);
				float exp = (float) entry.getIntValue() * ((SimpleAlloyingRecipe)recipe).getExperience();
				int expAsInt = Mth.floor(exp);
				float random = Mth.frac(exp);
				if (random != 0.0f && Math.random() < random)
				{
					expAsInt++;
				}

				ExperienceOrb.award(level, position, expAsInt);
			});
		}

		if(player != null)
			player.awardRecipes(recipes);

		this.recipesUsed.clear();
	}

	// allows the removal of multiple ingredients from one stack
	public void removeIngredient(Ingredient ingredient)
	{
		for(int i = 0; i < 4; i++)
		{
			ItemStack itemStack = this.getItem(i);
			if (!itemStack.isEmpty() && ingredient.test(itemStack))
			{
				itemStack.shrink(1);
				this.setItem(i, itemStack.isEmpty() ? ItemStack.EMPTY : itemStack);
				break;
			}
		}
	}

	@Override
	public void load(@NotNull CompoundTag tag)
	{
		super.load(tag);

		this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(tag, this.inventory);
		this.litTimeRemaining = tag.getInt("LitTime");
		this.litDuration = tag.getInt("LitDuration");
		this.smeltingProgress = tag.getInt("SmeltingTime");
		this.smeltingDuration = tag.getInt("SmeltingDuration");
		CompoundTag recipesUsedTag = tag.getCompound("RecipesUsed");
		recipesUsedTag.getAllKeys().forEach(key -> this.recipesUsed.put(new ResourceLocation(key), recipesUsedTag.getInt(key)));

		setChanged();
	}

	@Override
	protected void saveAdditional(CompoundTag tag)
	{
		super.saveAdditional(tag);

		ContainerHelper.saveAllItems(tag, this.inventory);
		tag.putInt("LitTime", this.litTimeRemaining);
		tag.putInt("LitDuration", this.litDuration);
		tag.putInt("SmeltingTime", this.smeltingProgress);
		tag.putInt("SmeltingDuration", this.smeltingDuration);
		CompoundTag recipesUsedTag = new CompoundTag();
		this.recipesUsed.forEach((id, i) -> recipesUsedTag.putInt(id.toString(), i));
		tag.put("RecipesUsed", recipesUsedTag);
	}

	@Override
	protected Component getDefaultName()
	{
		return Component.translatable("container.alloygery.alloy_kiln");
	}

	@Override
	protected AbstractContainerMenu createMenu(int containerId, Inventory inventory)
	{
		return new AlloyKilnMenu(containerId, inventory, this, dataAccess);
	}

	@Override
	public int[] getSlotsForFace(Direction side)
	{
		return switch (side)
		{
			case DOWN -> BOTTOM_SLOTS;
			case UP -> TOP_SLOTS;
			default -> SIDE_SLOTS;
		};
	}

	@Override
	public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction)
	{
		return this.canPlaceItem(index, itemStack);
	}

	@Override
	public boolean canPlaceItem(int index, ItemStack stack)
	{
		if(index == OUTPUT_SLOT) return false;

		if(index != FUEL_SLOT) return true;

		ItemStack fuelStack = this.inventory.get(FUEL_SLOT);
		return AbstractFurnaceBlockEntity.isFuel(stack) || stack.is(Items.BUCKET) && !fuelStack.is(Items.BUCKET);
	}

	@Override
	public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction)
	{
		if (direction == Direction.DOWN && index == FUEL_SLOT)
		{
			return stack.is(Items.WATER_BUCKET) || stack.is(Items.BUCKET);
		}

		return true;
	}

	@Override
	public int getContainerSize()
	{
		return this.inventory.size();
	}

	@Override
	public boolean isEmpty()
	{
		return this.inventory.stream().allMatch(ItemStack::isEmpty);
	}

	@Override
	public ItemStack getItem(int slot)
	{
		return this.inventory.get(slot);
	}

	@Override
	public ItemStack removeItem(int slot, int amount)
	{
		return ContainerHelper.removeItem(this.inventory, slot, amount);
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot)
	{
		return ContainerHelper.takeItem(this.inventory, slot);
	}

	@Override
	public void setItem(int slot, ItemStack stack)
	{
		ItemStack itemStack = this.inventory.get(slot);
		boolean sameNotEmpty = !stack.isEmpty() && ItemStack.isSameItemSameTags(itemStack, stack);
		this.inventory.set(slot, stack);
		if (stack.getCount() > this.getMaxStackSize())
		{
			stack.setCount(this.getMaxStackSize());
		}

		if (slot < FUEL_SLOT && !sameNotEmpty)
		{
			this.smeltingDuration = AlloyKilnBlockEntity.getSmeltingDuration(this.level, this);
			this.smeltingProgress = 0;
			this.setChanged();
		}
	}

	@Override
	public boolean stillValid(Player player)
	{
		return Container.stillValidBlockEntity(this, player);
	}

	@Override
	public void clearContent()
	{
		this.inventory.clear();
	}

	@Override
	public void setRecipeUsed(Recipe<?> recipe)
	{
		if(recipe == null) return;

		ResourceLocation id = recipe.getId();
		this.recipesUsed.addTo(id, 1);
	}

	@Nullable
	@Override
	public Recipe<?> getRecipeUsed()
	{
		return null;
	}

	@Override
	public void awardUsedRecipes(Player player, List<ItemStack> items) {}

	@Override
	public void fillStackedContents(StackedContents contents)
	{
		for(ItemStack itemStack : this.inventory)
		{
			contents.accountStack(itemStack);
		}
	}
}
