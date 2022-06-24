package com.gorodmi.randomMindustry.content;

import arc.*;
import arc.util.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;

import static arc.Core.*;
import static mindustry.Vars.*;

public class RMSettings{
    public void load(){
        SettingsMenuDialog dialog = ui.settings;
        
        dialog.addCategory("@setting.rm", Icon.effect /* TODO */, c -> {
            c.checkPref("rm-ammo", true);
            c.checkPref("rm-block-output", true);
            c.checkPref("rm-block-input", true);
        });
    }
}