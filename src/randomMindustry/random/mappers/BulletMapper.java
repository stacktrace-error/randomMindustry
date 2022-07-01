package randomMindustry.random.mappers;

import arc.struct.*;
import mindustry.gen.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;

import randomMindustry.random.util.*;

import static mindustry.Vars.*;

public class BulletMapper{
    public static void init(){
        Seq<Effect> effects = Effect.all.select(effect -> effect != Fx.dynamicExplosion);
        
        Seq<BulletType> bullets = content.bullets();
        RandomUtil.shuffle(bullets);
        bullets.each(bullet -> {
            // sfx
            bullet.hitSound = Sounds.getSound(RandomUtil.getRand().random(0, 71));
            bullet.despawnSound = Sounds.getSound(RandomUtil.getRand().random(0, 71));
            
            // fx
            bullet.hitEffect = effects.random(RandomUtil.getRand());
            bullet.despawnEffect = effects.random(RandomUtil.getRand());
            bullet.shootEffect = effects.random(RandomUtil.getRand());
            bullet.chargeEffect = effects.random(RandomUtil.getRand());
            bullet.smokeEffect = effects.random(RandomUtil.getRand());
            bullet.trailEffect = effects.random(RandomUtil.getRand());
            bullet.healEffect = effects.random(RandomUtil.getRand());
            
            // weavin 
            bullet.weaveScale = RandomUtil.getRand().random(0.5f, 20f);
            bullet.weaveMag = RandomUtil.getRand().random(0.5f, 10f);
            
            // other stats
            bullet.rangeChange = RandomUtil.getRand().random(0.5f, 20f);
            bullet.scaleLife = RandomUtil.getRand().random(-2f, 2f) < 0f;
            bullet.healPercent = RandomUtil.getRand().random(0f, 100f);
            if(bullet.healPercent > 0) bullet.collidesTiles = bullet.collidesTeam = bullet.collides = true;
            bullet.lifetime = RandomUtil.getRand().random(bullet.lifetime + (bullet.lifetime / 2f));
            bullet.recoil = RandomUtil.getRand().random(-1f, 1f);
            bullet.knockback = RandomUtil.getRand().random(-10f, 10f);
            bullet.hitShake = RandomUtil.getRand().random(-10f, 10f);
            bullet.despawnShake = RandomUtil.getRand().random(-10f, 10f);

            // size and rotation
            if (bullet instanceof BasicBulletType basicBulletType) {
                basicBulletType.width = RandomUtil.getRand().random(basicBulletType.width + (basicBulletType.width / 2f));
                basicBulletType.height = RandomUtil.getRand().random(basicBulletType.height + (basicBulletType.height / 2f));
                basicBulletType.shrinkX = RandomUtil.getRand().random(-1f, 1f);
                basicBulletType.shrinkY = RandomUtil.getRand().random(-1f, 1f);
                basicBulletType.spin = RandomUtil.getRand().random(45f);
                basicBulletType.shrinkX = RandomUtil.getRand().random(-1f, 1f);
                basicBulletType.shrinkY = RandomUtil.getRand().random(-1f, 1f);
                basicBulletType.spin = RandomUtil.getRand().random(45f);
            }
            
            // status effect 
            bullet.status = content.statusEffects().random(RandomUtil.getRand());
            bullet.statusDuration = RandomUtil.getRand().random(600f);
        });
    }
}
