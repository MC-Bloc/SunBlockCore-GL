package ca.milieux.sunblock.sunblockcore.application.block;

import ca.milieux.sunblock.sunblockcore.services.DataQueryProcess;
import ca.milieux.sunblock.sunblockcore.services.SolarDataTypes;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class PowerButton extends ButtonBlock {

    public PowerButton(Properties pProperties, BlockSetType pType, int pTicksToStayPressed, boolean pArrowsCanPress) {
        super(pProperties, pType, pTicksToStayPressed, pArrowsCanPress);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                                 Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        String profile = DataQueryProcess.PowerProfile();

        if (profile.equals("Power Saver")) {
            this.PerformanceMode();
        } else {
            this.PowerSaverMode();
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        pTooltip.add(Component.literal("Switch Power Profile of system right-clicked!"));
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }



    public void PerformanceMode(){
        try {
            URL switch_performance = new URL("https://photon.sunblockone.milieux.ca/performance-mode");
            URLConnection yc = switch_performance.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        } catch (Exception e) {
            System.err.println("SunBlockCore::DataQueryProcess -- PerformanceMode() Error " + e.getMessage());
        }
    }

    public void PowerSaverMode(){
        try {
            URL switch_powersaver = new URL("https://photon.sunblockone.milieux.ca/power-saver-mode");
            URLConnection yc = switch_powersaver.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

        } catch (Exception e) {
            System.err.println("SunBlockCore::DataQueryProcess -- PowerSaverMode() Error " + e.getMessage());
        }
    }
}
