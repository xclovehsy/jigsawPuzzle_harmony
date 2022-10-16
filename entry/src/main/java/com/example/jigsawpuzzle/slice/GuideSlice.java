package com.example.jigsawpuzzle.slice;

import com.example.jigsawpuzzle.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;

public class GuideSlice extends AbilitySlice {
    private Button btn_back;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_guide_slice);
        btn_back = findComponentById(ResourceTable.Id_btn_back);
        btn_back.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                AbilitySlice slice = new MainAbilitySlice();
                Intent intent = new Intent();
                present(slice,intent);
            }
        });

    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}