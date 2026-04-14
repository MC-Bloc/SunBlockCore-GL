package ca.milieux.sunblock.core.application.block;

import ca.milieux.sunblock.core.application.config.ConfigHandlerServer;
import ca.milieux.sunblock.core.registry.ModSounds;
import ca.milieux.sunblock.core.services.DataQueryProcess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Random;

//Solar-powered toggle button
public class PowerButton extends ButtonBlock {

    private static final Random RAND = new Random();
    private static final DustParticleOptions YELLOW_DUST =
            new DustParticleOptions(new Vector3f(1.0F, 0.96F, 0.01F), 1.0F);

    public PowerButton(Properties props, BlockSetType type, int ticksToStayPressed, boolean arrowsCanPress) {
        super(props, type, ticksToStayPressed, arrowsCanPress);
    }

    //right‑click behaviour
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {
        //vanilla press logic (handles POWERED state & click sounds)
        InteractionResult result = super.use(state, level, pos, player, hand, hit);

        //server‑side custom logic
        if (!level.isClientSide && result.consumesAction()) {
            boolean isSaver = "Power Saver".equals(DataQueryProcess.PowerProfile());

            if (isSaver) {
                DataQueryProcess.PerformanceMode();
                play(level, pos, ModSounds.POWER_ON.get());
            } else {
                DataQueryProcess.PowerSaverMode();
                play(level, pos, ModSounds.POWER_OFF.get());
            }
            spawnParticles(level, pos, state);
        }
        return result;
    }

    private void play(Level level, BlockPos pos, net.minecraft.sounds.SoundEvent snd) {
        level.playSound(null, pos, snd, SoundSource.BLOCKS, 1.0f,
                level.random.nextFloat() * 0.2f + 0.9f);
    }

    //Emit yellow sparks
    private void spawnParticles(Level level, BlockPos pos, BlockState state) {
        if (!(level instanceof net.minecraft.server.level.ServerLevel server)) return;

        Direction face = state.getValue(ButtonBlock.FACING);
        double ox = pos.getX() + 0.5 - face.getStepX() * 0.01;
        double oy = pos.getY() + 0.5 - face.getStepY() * 0.01;
        double oz = pos.getZ() + 0.5 - face.getStepZ() * 0.01;

        double spread = 0.5;
        for (int i = 0; i < 8; i++) {
            double offX = (RAND.nextDouble() - 0.5) * (face.getAxis() == Direction.Axis.X ? 0 : spread);
            double offY = (RAND.nextDouble() - 0.5) * (face.getAxis() == Direction.Axis.Y ? 0 : spread);
            double offZ = (RAND.nextDouble() - 0.5) * (face.getAxis() == Direction.Axis.Z ? 0 : spread);

            double vx = offX * 0.8 + face.getStepX() * 0.03;
            double vy = offY * 0.8 + face.getStepY() * 0.03;
            double vz = offZ * 0.8 + face.getStepZ() * 0.03;

            server.sendParticles(YELLOW_DUST,
                    ox + offX, oy + offY, oz + offZ,
                    2, vx, vy, vz, 0.15);
        }
    }

    //tooltip
    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Switch power profile of the system (right‑click)!"));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
