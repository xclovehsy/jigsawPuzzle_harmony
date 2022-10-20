package com.example.jigsawpuzzle.slice;

import com.example.jigsawpuzzle.ResourceTable;
import com.example.jigsawpuzzle.conponent.TestPageProvider;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.PageSlider;

import java.util.ArrayList;

public class SelectSlice extends AbilitySlice implements Component.ClickedListener {
    private Button btn_back, btn_play;
    private int diff;
    private String jigsawName = "ultraman";
    private PageSlider pageSlider;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_select_slice);
        initCom();

        pageSlider.addPageChangedListener(new PageSlider.PageChangedListener() {
            @Override
            public void onPageSliding(int itemPos, float itemPosOffset, int itemPosPixles) {
            }
            @Override
            public void onPageSlideStateChanged(int state) {
            }
            @Override
            public void onPageChosen(int itemPos) {
            }
        });

        diff = 4;


    }

    /**
     * 初始化组件
     */
    private void initCom() {
        btn_play = findComponentById(ResourceTable.Id_btn_play);
        btn_back = findComponentById(ResourceTable.Id_btn_back);
        btn_play.setClickedListener(this);
        btn_back.setClickedListener(this);

        initPageSlider();
    }

    private void initPageSlider() {
        pageSlider = (PageSlider) findComponentById(ResourceTable.Id_page_slider);
        pageSlider.setProvider(new TestPageProvider(getData(), this));
        pageSlider.setReboundEffect(true);
    }

    private ArrayList<TestPageProvider.DataItem> getData() {
        ArrayList<TestPageProvider.DataItem> dataItems = new ArrayList<>();
        dataItems.add(new TestPageProvider.DataItem("Page A"));
        dataItems.add(new TestPageProvider.DataItem("Page B"));
        dataItems.add(new TestPageProvider.DataItem("Page C"));
        dataItems.add(new TestPageProvider.DataItem("Page D"));
        return dataItems;
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
        if (component == btn_back) {
            AbilitySlice slice = new MainAbilitySlice();
            Intent intent = new Intent();
            present(slice, intent);
        } else if (component == btn_play) {
            if (diff == 4) {
                AbilitySlice slice = new PlayFourSlice();
                Intent intent = new Intent();
                intent.setParam("jigsawName", jigsawName);
                present(slice, intent);
            }
        }
    }
}
