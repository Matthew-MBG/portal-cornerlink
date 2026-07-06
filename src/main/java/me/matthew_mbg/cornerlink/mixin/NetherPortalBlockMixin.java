package me.matthew_mbg.cornerlink.mixin;

import me.matthew_mbg.cornerlink.PortalCorners;
import me.matthew_mbg.cornerlink.PortalHelper;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.portal.DimensionTransition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NetherPortalBlock.class)
public abstract class NetherPortalBlockMixin {

    @Inject(method = "getPortalDestination", at = @At("HEAD"), cancellable = true)
    private void inject(ServerLevel world, Entity entity, BlockPos pos, CallbackInfoReturnable<DimensionTransition> cir)
    {
        PortalCorners corners = PortalHelper.getCornersVectorAt(entity.level(), pos);

        if( corners.hasLinkingBlocks())
        {
            ResourceKey<Level> destinationKey = world.dimension() == Level.NETHER ? Level.OVERWORLD : Level.NETHER;
            ServerLevel destinationWorld = world.getServer().getLevel(destinationKey);
            if (destinationWorld == null) {
                return;
            }

            boolean destinationIsNether = destinationWorld.dimension() == Level.NETHER;
            WorldBorder worldBorder = destinationWorld.getWorldBorder();
            double scale = DimensionType.getTeleportationScale(world.dimensionType(), destinationWorld.dimensionType());
            BlockPos scaledPos = worldBorder.clampToBounds(entity.getX() * scale, entity.getY(), entity.getZ() * scale);
            var portalRect = PortalHelper.modifiedGetPortalRect(destinationWorld, scaledPos, destinationIsNether, worldBorder, corners);

            if(portalRect.isPresent())
            {
                DimensionTransition.PostDimensionTransition postDimensionTransition = DimensionTransition.PLAY_PORTAL_SOUND.then(entityx ->
                    entityx.placePortalTicket(portalRect.get().minCorner)
                );

                var teleportTarget = cornerlink$getDimensionTransitionFromExit(entity, pos, portalRect.get(), destinationWorld, postDimensionTransition);
                cir.setReturnValue(teleportTarget);
            }
        }
    }

    @Invoker("getDimensionTransitionFromExit")
    private static DimensionTransition cornerlink$getDimensionTransitionFromExit(
            Entity entity,
            BlockPos portalPos,
            BlockUtil.FoundRectangle portalRect,
            ServerLevel destinationWorld,
            DimensionTransition.PostDimensionTransition postDimensionTransition
    ) {
        throw new AssertionError();
    }
}
