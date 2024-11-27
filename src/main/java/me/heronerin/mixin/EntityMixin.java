package me.heronerin.mixin;

import me.heronerin.PseudoRot;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    public boolean inTravelFunc = false;
    @Inject(at = @At(value = "HEAD"), method = "isSprinting", cancellable = true)
    void isSprinting(CallbackInfoReturnable<Boolean> cir) {
        if (!inTravelFunc) return;
        cir.setReturnValue(false);
        cir.cancel();
    }

    @Invoker(value = "isPlayer")
    abstract boolean isPlayerAcc();


    @Mixin(LivingEntity.class)
    static abstract class LivingEntityMixin extends EntityMixin{
        @Inject(at=@At(value="HEAD"), method = "getBaseMovementSpeedMultiplier", cancellable = true)
        void getBaseMovementSpeedMultiplier(CallbackInfoReturnable<Float> cir){
            if (!PseudoRot.do_fake_rotation) return;
            cir.setReturnValue(0.1f);
            cir.cancel();
        }

        @Inject(at=@At(value="HEAD"), method = "travel")
        void travelHead(Vec3d movementInput, CallbackInfo ci){
            if (!this.isPlayerAcc()) return;
            inTravelFunc=true;

        }
        @Inject(at=@At(value="RETURN"), method = "travel")
        void travelReturn(Vec3d movementInput, CallbackInfo ci){
            if (!this.isPlayerAcc()) return;
            inTravelFunc=false;
        }
    }



}
