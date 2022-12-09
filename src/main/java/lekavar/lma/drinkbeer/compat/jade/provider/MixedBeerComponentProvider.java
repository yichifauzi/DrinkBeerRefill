package lekavar.lma.drinkbeer.compat.jade.provider;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.blockentities.MixedBeerBlockEntity;
import lekavar.lma.drinkbeer.blocks.MixedBeerBlock;
import lekavar.lma.drinkbeer.managers.MixedBeerManager;
import lekavar.lma.drinkbeer.managers.SpiceAndFlavorManager;
import lekavar.lma.drinkbeer.utils.mixedbeer.Flavors;
import lekavar.lma.drinkbeer.utils.mixedbeer.Spices;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.impl.ui.TextElement;

import java.util.List;

public class MixedBeerComponentProvider implements IBlockComponentProvider {
    public static MixedBeerComponentProvider INSTANCE = new MixedBeerComponentProvider();
    public static final float TEXT_HEIGHT = new TextElement(Component.nullToEmpty("")).getSize().y;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig pluginConfig) {
        if(!(accessor.getBlock() instanceof MixedBeerBlock &&
                accessor.getBlockEntity() instanceof MixedBeerBlockEntity mixedBeer))
            return;

        IElementHelper helper = tooltip.getElementHelper();
        ItemStack stack = mixedBeer.getPickStack();

        List<Integer> spiceList = MixedBeerManager.getSpiceList(stack);
        if (!spiceList.isEmpty()) {
            for (int spiceId : spiceList) {
                Spices spice = Spices.byId(spiceId);
                Item spiceItem = spice.getSpiceItem();
                Flavors flavor = spice.getFlavor();

                // [Flavor icon] Flavor name
                tooltip.add(List.of(
                        JadeHelper.translateDelta(JadeHelper.createCenteredItem(helper, spiceItem.getDefaultInstance(), 0.8F, TEXT_HEIGHT), 0, -2),
                        helper.text(Component.translatable(SpiceAndFlavorManager.getFlavorTranslationKey(flavor.getId())).withStyle(ChatFormatting.RED))
                ));
            }

            //Flavor combination(if exists)
            Flavors combinedFlavor = SpiceAndFlavorManager.getCombinedFlavor(spiceList);
            if (combinedFlavor != null) {
                tooltip.add(Component.literal("")
                        .append("\"")
                        .append(Component.translatable(SpiceAndFlavorManager.getFlavorTranslationKey(combinedFlavor.getId())))
                        .append("\"")
                        .withStyle(ChatFormatting.DARK_RED));
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(DrinkBeer.MOD_ID,"mixinedBeer");
    }
}