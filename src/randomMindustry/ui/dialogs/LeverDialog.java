package randomMindustry.ui.dialogs;

import arc.util.*;
import arc.math.*;
import arc.scene.ui.*;
import arc.scene.event.*;
import arc.scene.actions.*;
import mindustry.gen.*;
import mindustry.ui.dialogs.*;
import randomMindustry.*;

import static mindustry.Vars.*;

public class LeverDialog extends BaseDialog{
    private Button button;
    
    public LeverDialog(){
        super("");
        button = new Button();
        
        shown(() -> button.touchable = Touchable.enabled);
        onResize(() -> button.actions(Actions.moveBy(0f, -50f, 0.01f, Interp.linear)));
        
        addCloseButton();
    }
    
    public void show(int num){
        cont.clear();
        
        cont.add("@rm-lever").row();
        
        button.clearListeners();
        button.label(() -> Integer.toString(num));
        cont.add(button).size(160f, 40f);
        button.clicked(() -> {
            Sounds.rockBreak.play();
            button.touchable = Touchable.disabled;
            button.actions(Actions.moveBy(0f, 50f, 0.125f, Interp.pow2In));
            Time.runTask(60f, () -> {
                hide();
                if(Main.phase == num){
                    if(Main.phase >= 7){
                        ui.showInfo("@msg.rm-sequence-finish");
                    }else{
                        ui.showInfo("@msg.rm-sequence-continue");
                        Main.phase++;
                    }
                }else{
                    ui.showInfo("@msg.rm-sequence-fail");
                }
            });
        });
        
        show();
    }
}
