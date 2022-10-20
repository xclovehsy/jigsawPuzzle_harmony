package com.example.jigsawpuzzle;

import com.example.jigsawpuzzle.slice.MainAbilitySlice;
import com.example.jigsawpuzzle.slice.PlayFourSlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class MainAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());
//        super.setMainRoute(PlayFourSlice.class.getName());
    }
}
