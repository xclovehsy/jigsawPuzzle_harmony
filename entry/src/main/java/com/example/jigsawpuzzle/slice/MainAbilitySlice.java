package com.example.jigsawpuzzle.slice;

import com.example.jigsawpuzzle.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;

public class MainAbilitySlice extends AbilitySlice implements Component.ClickedListener {
    private Button btn_about, btn_guide, btn_play;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        initCom();
    }

    /**
     * 初始化组件
     */
    private void initCom(){
        btn_about = findComponentById(ResourceTable.Id_btn_about);
        btn_play = findComponentById(ResourceTable.Id_btn_play);
        btn_guide = findComponentById(ResourceTable.Id_btn_guide);
        btn_about.setClickedListener(this);
        btn_play.setClickedListener(this);
        btn_guide.setClickedListener(this);
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    @Override
    public void onClick(Component component) {
        AbilitySlice slice = null;
        if(component == btn_about){
            slice = new AboutMeSlice();
        }else if(component == btn_guide){
            slice = new GuideSlice();
        } else if(component == btn_play){
            slice = new SelectSlice();
        }

        Intent intent = new Intent();
        present(slice, intent);

    }
}
