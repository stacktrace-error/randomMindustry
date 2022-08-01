package randomMindustry.random.mappers;

import arc.struct.*;
import arc.graphics.g2d.*;
import mindustry.type.*;
import mindustry.type.unit.*;
import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.entities.pattern.*;
import mindustry.entities.part.*;

import randomMindustry.random.util.*;
import randomMindustry.util.*;

import static mindustry.Vars.*;

// oh god no
public class UnitMapper{
    public static void init(){
        Seq<UnitType> units = content.units().copy();
        RandomUtil.shuffle(units);
        units.each(UnitMapper::modify);
    }
    
    public static void modify(UnitType unit){
        Seq<Effect> effects = Effect.all.select(effect -> effect != Fx.dynamicExplosion);
        
        float hue = RandomUtil.getRand().random(360f);
        
        if(!headless){
            unit.localizedName = StringGenerator.generateUnitName();
            TextureGenerator.changeHue(unit.fullIcon, hue);
            TextureGenerator.changeHue(unit.uiIcon, hue);
            TextureGenerator.changeHue(unit.baseRegion, hue);
            TextureGenerator.changeHue(unit.legRegion, hue);
            TextureGenerator.changeHue(unit.region, hue);
            TextureGenerator.changeHue(unit.previewRegion, hue);
            TextureGenerator.changeHue(unit.itemCircleRegion, hue);
            TextureGenerator.changeHue(unit.jointRegion, hue);
            TextureGenerator.changeHue(unit.footRegion, hue);
            TextureGenerator.changeHue(unit.baseJointRegion, hue);
            TextureGenerator.changeHue(unit.treadRegion, hue);
            for(TextureRegion wregion : unit.wreckRegions) TextureGenerator.changeHue(wregion, hue);
            for(TextureRegion sregion : unit.segmentRegions) TextureGenerator.changeHue(sregion, hue);
            for(TextureRegion[] tregionArr : unit.treadRegions){
                for(TextureRegion tregion : tregionArr) TextureGenerator.changeHue(tregion, hue);
            }
            unit.parts.each(part -> huePart(part, hue));
        }
        
        if(!unit.flying && !unit.naval){
            unit.canBoost = RandomUtil.getRand().random(-2f, 2f) > 0;
            unit.boostMultiplier = RandomUtil.getRand().random(5f);
        }
        
        if(!unit.naval) unit.canDrown = RandomUtil.getRand().random(-2f, 2f) > 0;
        unit.createWreck = RandomUtil.getRand().random(-2f, 2f) > 0;
        unit.createScorch = RandomUtil.getRand().random(-2f, 2f) > 0;
        
        unit.legCount = RandomUtil.getRandomIntMult(2, 20, 2);
        unit.legGroupSize = RandomUtil.getRandomIntMult(2, 10, 2);
        unit.legLength = RandomUtil.getRand().random(-unit.hitSize*2f, unit.hitSize*2f);
        unit.legSpeed = RandomUtil.getRand().random(0.1f, 3f);
        unit.legStraightness = RandomUtil.getRand().random(1f);
        unit.lockLegBase = RandomUtil.getRand().random(-2f, 2f) > 0;
        unit.legContinuousMove = RandomUtil.getRand().random(-2f, 2f) > 0;
        
        unit.deathSound = Util.generateSound();
        unit.mineSound = Util.generateSound();
        
        unit.fallEffect = effects.random(RandomUtil.getRand());
        unit.fallEngineEffect = effects.random(RandomUtil.getRand());
        unit.deathExplosionEffect = effects.random(RandomUtil.getRand());

        // unit.engineLayer = RandomUtil.getRand().random(Layer.min, Layer.max);
        // unit.groundLayer = RandomUtil.getRand().random(Layer.min, Layer.max);

        unit.targetAir = unit.canHeal = unit.targetGround = false;
        if(unit instanceof MissileUnitType) return;
        unit.weapons.each(weapon -> {
            if(!headless){
                TextureGenerator.changeHue(weapon.region, hue);
                weapon.parts.each(part -> huePart(part, hue));
            }
            
            weapon.shootSound = Util.generateSound();
            weapon.chargeSound = Util.generateSound();
            weapon.noAmmoSound = Util.generateSound();
            weapon.reload = RandomUtil.getRand().random(100f);
            weapon.shootCone = RandomUtil.getRand().random(1f, 360f);
            weapon.inaccuracy = RandomUtil.getRand().random(1f, 180f);
            weapon.rotateSpeed = RandomUtil.getRand().random(1f, 20f);
            weapon.rotationLimit = RandomUtil.getRand().random(45f, 361f);
            
            weapon.bullet = content.bullets().select(b -> !(b instanceof MassDriverBolt)).random(RandomUtil.getRand());
            unit.targetAir = weapon.bullet.collidesAir;
            unit.targetGround = weapon.bullet.collidesGround;
            unit.canHeal = weapon.bullet.healPercent > 0;

            int pattern = RandomUtil.getRand().random(0, 5);
            switch (pattern) {
                case 0 -> weapon.shoot = new ShootPattern();
                case 1 -> weapon.shoot = new ShootAlternate(RandomUtil.getRand().random(1f, 15f)) {{
                    barrels = RandomUtil.getRand().random(1, 5);
                }};
                case 2 -> weapon.shoot = new ShootBarrel() {{
                    int barrelAmount = RandomUtil.getRand().random(1, 10);
                    float[] barrelArray = new float[barrelAmount * 3];
                    for (int i = 0; i < barrelArray.length; i += 3) {
                        barrelArray[i] = RandomUtil.getRand().random(-10f, 10f);
                        barrelArray[i + 1] = RandomUtil.getRand().random(-10f, 10f);
                        barrelArray[i + 2] = RandomUtil.getRand().random(0, 360f);
                    }
                    barrels = barrelArray;
                }};
                case 3 -> weapon.shoot = new ShootHelix() {{
                    scl = RandomUtil.getRand().random(1f, 20f);
                    mag = RandomUtil.getRand().random(1f, 10f);
                }};
                case 4 -> weapon.shoot = new ShootSine(RandomUtil.getRand().random(1f, 20f), RandomUtil.getRand().random(1f, 10f));
                case 5 -> weapon.shoot = new ShootSpread() {{
                    spread = RandomUtil.getRand().random(1f, 15f);
                }};
            }
            weapon.shoot.shots = RandomUtil.getRand().random(1, 10);
            weapon.shoot.shotDelay = RandomUtil.getRand().random(1f, 60f);
        });

        unit.range = Float.MAX_VALUE;
        unit.maxRange = -Float.MAX_VALUE;
        for(Weapon weapon : unit.weapons){
            unit.range = Math.min(unit.range, weapon.range() - 4);
            unit.maxRange = Math.max(unit.maxRange, weapon.range() - 4);
        }
    }
    
    public static void huePart(DrawPart part, float hue){
        if(part instanceof RegionPart regionpart){
            TextureGenerator.changeHue(regionpart.heat, hue);
            
            for(TextureRegion region : regionpart.regions) TextureGenerator.changeHue(region, hue);
            
            if(regionpart.color != null) regionpart.color = regionpart.color.cpy().hue(hue);
            if(regionpart.colorTo != null) regionpart.colorTo = regionpart.colorTo.cpy().hue(hue);
            if(regionpart.mixColor != null) regionpart.mixColor = regionpart.mixColor.cpy().hue(hue);
            if(regionpart.mixColorTo != null) regionpart.mixColorTo = regionpart.mixColorTo.cpy().hue(hue);
            regionpart.heatColor = regionpart.heatColor.cpy().hue(hue);
            
            regionpart.children.each(child -> huePart(child, hue));
        }
        if(part instanceof HaloPart halopart){
            halopart.color = halopart.color.cpy().hue(hue);
            if(halopart.colorTo != null) halopart.colorTo = halopart.colorTo.cpy().hue(hue);
        }
        if(part instanceof ShapePart shapepart){
            shapepart.color = shapepart.color.cpy().hue(hue);
            if(shapepart.colorTo != null) shapepart.colorTo = shapepart.colorTo.cpy().hue(hue);
        }
        if(part instanceof FlarePart flarepart){
            flarepart.color1 = flarepart.color1.cpy().hue(hue);
            flarepart.color2 = flarepart.color2.cpy().hue(hue);
        }
        if(part instanceof HoverPart hoverpart) hoverpart.color = hoverpart.color.cpy().hue(hue);
    }
}
