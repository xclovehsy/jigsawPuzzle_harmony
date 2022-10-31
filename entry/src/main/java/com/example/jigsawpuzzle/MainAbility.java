package com.example.jigsawpuzzle;

import com.example.jigsawpuzzle.slice.*;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class MainAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());
//        super.setMainRoute(PlayFourSlice.class.getName());
//        super.setMainRoute(PlayNineSlice.class.getName());
//        super.setMainRoute(SelectSlice.class.getName());
//        super.setMainRoute(HuarongRoadNine.class.getName());
//        super.setMainRoute(jigsawSlice.class.getName());



    }
}
