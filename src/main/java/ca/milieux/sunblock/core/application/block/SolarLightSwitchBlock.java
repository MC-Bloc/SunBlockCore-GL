package ca.milieux.sunblock.core.application.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;

public class SolarLightSwitchBlock extends Block {
    public static final EnumProperty<SolarLightSwitchState> SWITCH_STATE =
            EnumProperty.create("switch_state", SolarLightSwitchState.class);

    public static final BooleanProperty TURNING_ON =
            BooleanProperty.create("turning_on");

    public static final DirectionProperty FACING =
            BlockStateProperties.FACING;

    private static final int PRESS_TIME_TICKS = 10; // 10 ticks for pressed animation

    private static final VoxelShape FLOOR_SHAPE = Block.box(
            3, 0, 3,
            13, 2, 13
    );

    private static final VoxelShape NORTH_WALL_SHAPE = Block.box(
            3, 3, 14,
            13, 13, 16
    );

    private static final VoxelShape SOUTH_WALL_SHAPE = Block.box(
            3, 3, 0,
            13, 13, 2
    );

    private static final VoxelShape EAST_WALL_SHAPE = Block.box(
            0, 3, 3,
            2, 13, 13
    );

    private static final VoxelShape WEST_WALL_SHAPE = Block.box(
            14, 3, 3,
            16, 13, 13
    );

    public SolarLightSwitchBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(SWITCH_STATE, SolarLightSwitchState.OFF)
                .setValue(TURNING_ON, false)
                .setValue(FACING, Direction.UP));
    }

    // Tooltip shown when hovering over the Solar Light Switch item in an inventory.
    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context,
                                List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.sunblockcore.solar_light_switch"));
        super.appendHoverText(stack, context, tooltip, flag);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction clickedFace = context.getClickedFace();

        // Ceiling placement is disabled.
        if (clickedFace == Direction.DOWN) {
            return null;
        }

        BlockState blockState = this.defaultBlockState()
                .setValue(FACING, clickedFace)
                .setValue(SWITCH_STATE, SolarLightSwitchState.OFF)
                .setValue(TURNING_ON, false);

        if (blockState.canSurvive(context.getLevel(), context.getClickedPos())) {
            return blockState;
        }

        return null;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        BlockPos supportPos = pos.relative(facing.getOpposite());

        return level.getBlockState(supportPos).isFaceSturdy(level, supportPos, facing);
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (direction == state.getValue(FACING).getOpposite() && !this.canSurvive(state, level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        SolarLightSwitchState currentState = state.getValue(SWITCH_STATE);

        // Prevent clicking again while already pressed.
        if (currentState == SolarLightSwitchState.PRESSED) {
            return InteractionResult.CONSUME;
        }

        // OFF -> pressed -> ON
        // ON  -> pressed -> OFF
        boolean shouldTurnOn = currentState == SolarLightSwitchState.OFF;

        BlockState pressedState = state
                .setValue(SWITCH_STATE, SolarLightSwitchState.PRESSED)
                .setValue(TURNING_ON, shouldTurnOn);

        level.setBlock(pos, pressedState, Block.UPDATE_ALL);

        level.playSound(
                null,
                pos,
                SoundEvents.STONE_BUTTON_CLICK_ON,
                SoundSource.BLOCKS,
                0.4F,
                1.0F
        );

        level.scheduleTick(pos, this, PRESS_TIME_TICKS);

        return InteractionResult.CONSUME;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(SWITCH_STATE) != SolarLightSwitchState.PRESSED) {
            return;
        }

        boolean shouldTurnOn = state.getValue(TURNING_ON);

        BlockState finalState = state
                .setValue(SWITCH_STATE, shouldTurnOn ? SolarLightSwitchState.ON : SolarLightSwitchState.OFF)
                .setValue(TURNING_ON, false);

        level.setBlock(pos, finalState, Block.UPDATE_ALL);

        level.playSound(
                null,
                pos,
                shouldTurnOn ? SoundEvents.LEVER_CLICK : SoundEvents.STONE_BUTTON_CLICK_OFF,
                SoundSource.BLOCKS,
                0.4F,
                shouldTurnOn ? 1.2F : 0.8F
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SWITCH_STATE, TURNING_ON, FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);

        return switch (facing) {
            case NORTH -> NORTH_WALL_SHAPE;
            case SOUTH -> SOUTH_WALL_SHAPE;
            case EAST -> EAST_WALL_SHAPE;
            case WEST -> WEST_WALL_SHAPE;
            default -> FLOOR_SHAPE;
        };
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    public enum SolarLightSwitchState implements StringRepresentable {
        OFF("off"),
        PRESSED("pressed"),
        ON("on");

        private final String name;

        SolarLightSwitchState(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}