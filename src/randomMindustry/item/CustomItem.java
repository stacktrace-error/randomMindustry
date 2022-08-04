package randomMindustry.item;

import arc.graphics.*;
import arc.math.*;
import arc.scene.ui.layout.*;
import mindustry.*;
import mindustry.type.*;
import randomMindustry.texture.*;

public class CustomItem extends Item {
    public CustomItem(String name, Color color) {
        super(name, color);
    }

    public void edit() {
        localizedName = "fake item";
        explosiveness = 100;
        radioactivity = -1;
        flammability = 1;
        Item item = Vars.content.items().select(it -> !(it instanceof CustomItem)).random();
        float hue = Mathf.random(360f);
        fullIcon = TextureManager.alloc(item.fullIcon);
        uiIcon = TextureManager.alloc(item.uiIcon);
        TextureManager.hueRegion(fullIcon, hue);
        TextureManager.hueRegion(uiIcon, hue);
        color = color.cpy().hue(hue);
    }

    @Override
    public void displayExtra(Table t) {
        t.label(() -> "get real").row();
    }
}
