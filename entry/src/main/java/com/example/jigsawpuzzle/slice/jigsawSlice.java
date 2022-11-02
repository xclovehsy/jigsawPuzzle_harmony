package com.example.jigsawpuzzle.slice;

import com.example.jigsawpuzzle.game.Blank;
import com.example.jigsawpuzzle.ResourceTable;
import com.example.jigsawpuzzle.game.Chip;
import com.example.jigsawpuzzle.game.MyImage;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.render.Texture;
import ohos.agp.utils.Color;
import ohos.agp.utils.Point;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.IDialog;
import ohos.global.resource.NotExistException;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.ImageInfo;
import ohos.media.image.common.Rect;
import ohos.media.image.common.Size;
import ohos.multimodalinput.event.TouchEvent;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_CONTENT;


public class jigsawSlice extends AbilitySlice {
    static final HiLogLabel label = new HiLogLabel(HiLog.LOG_APP,0,"MY_TAG");
    private Map<Image, Chip> chipMap = new HashMap<>();

    private List<Image> imageList = new LinkedList<>();
    private float maxChipX = 0, minChipX=0, maxChipY=0, minChipY=0;
    private Button btn_begin, btn_tip, btn_giveup;
    private String jigsawName = "dog";
    private TickTimer tickTimer;
    long startTime = 0, passTime = 0;
    private List<Blank> BlankList = new LinkedList<>();

    private int jigsawId = ResourceTable.Media_dog;
    private PixelMap jigsawPixelMap = null;
    private int jigsawCnt = 4;
    private int jigsawRowCnt = 2;
    private int pm_px;  // 手机设备的宽
    private int pg_px;  // 手机设备的高
    private Text stepCntText;
    private int stepCnt = 0;
    private boolean isShowNum = true; //是否显示数字
    private TableLayout blocklayout = null;
    private List<PixelMap> pixelMapList = null;
    private int chipWidth = 0;
    private DirectionalLayout mainlayout = null;
    private boolean isSelectFromAlbum = false;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_jigsaw_slice);

        this.jigsawId = intent.getIntParam("jigsawId", ResourceTable.Media_dog);
        this.jigsawRowCnt = intent.getIntParam("diff", 3);
        this.isShowNum = intent.getBooleanParam("isShowNum", true);
        this.jigsawCnt = (int) Math.pow(this.jigsawRowCnt, 2);
        this.isSelectFromAlbum = intent.getBooleanParam("isselectFromAlbum", false);

        HiLog.info(label, "jigsawCnt="+jigsawCnt);

        pm_px=AttrHelper.vp2px(getContext().getResourceManager().getDeviceCapability().width,this);
        pg_px=AttrHelper.vp2px(getContext().getResourceManager().getDeviceCapability().height,this);

        // 获取拼图图片资源
//        if(isSelectFromAlbum){
////            DirectionalLayout dl = (DirectionalLayout) LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_select_slice, null, false);
//            this.jigsawPixelMap = image.getPixelMap();
//        }else{
//            this.jigsawPixelMap = getPixelMap(jigsawId);
//        }
        this.jigsawPixelMap = getPixelMap(jigsawId);
//        Image image = (Image) findComponentById(ResourceTable.Id_showChooseImg);
//        Image image2 = (Image) findComponentById(ResourceTable.Id_albumImage);
//        PixelMap p = image.getPixelMap();

        Image cur1= (Image) findComponentById(ResourceTable.Id_albumImage);
        Image te = new Image(this);
        te.setHeight(0);
        te.setWidth(0);
//        te.setPixelMap(cur1.getPixelMap());


        // 拼图切割
        this.pixelMapList = cutPic(this.jigsawPixelMap);


        // 初始化组件
        initComponent();

//         设置chip图片
        setChip();

//         添加响应事件
        addListener();

    }

    /**
     * 切割图片功能
     * @param pixelMap
     * @return
     */
    private List<PixelMap> cutPic(PixelMap pixelMap){
        ImageInfo imageInfo = pixelMap.getImageInfo();
        int height = imageInfo.size.height;
        int width = imageInfo.size.width;
        int size = Math.min(height, width);


        PixelMap.InitializationOptions options = new PixelMap.InitializationOptions();
        options.pixelFormat = imageInfo.pixelFormat;
        options.editable = true;
        options.size = new Size();
        options.size.width = size;
        options.size.height = size;
        Rect rect = new Rect();
        rect.minX = 0;
        rect.minY = 0;
        rect.width = size;
        rect.height = size;
        PixelMap pixelMap1 = PixelMap.create(pixelMap, rect, options);

        List<PixelMap> pixelMapList = new LinkedList<>();
        int t = 0;
        int pieceSize = (int)Math.floor(size / (float)this.jigsawRowCnt);
        for(int i = 0; i<this.jigsawRowCnt; i++){
            for(int j = 0; j<this.jigsawRowCnt; j++){
                int x = j*pieceSize;
                int y = i*pieceSize;
                Rect rect1 = new Rect();
                rect1.height = pieceSize;
                rect1.width = pieceSize;
                rect1.minX = x;
                rect1.minY = y;
                PixelMap temp = PixelMap.create(pixelMap1, rect1, options);

                if(this.isShowNum){
                    Canvas canvas = new Canvas(new Texture((temp)));
                    Paint paint = new Paint();
                    paint.setTextSize(pieceSize*2);
                    paint.setColor(Color.RED);
                    t++;
                    canvas.drawText(paint, ""+t, pieceSize, pieceSize*2);
                }

                pixelMapList.add(temp);
            }
        }

        HiLog.info(label, "temp="+pixelMapList.size());
        return pixelMapList;
    }


    /**
     * 获取图片pixelmap资源
     * @param imageId
     * @return
     */
    PixelMap getPixelMap(int imageId)
    {
        ImageSource imageSource=null;
        InputStream inputStream=null;
        try {
            inputStream = getContext().getResourceManager().getResource(imageId);
            imageSource = ImageSource.create(inputStream, null);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotExistException e) {
            e.printStackTrace();
        }
        PixelMap pix= imageSource.createPixelmap(null);
        return pix;
    }

    /**
     * 设置步数text
     */
    private void setStepCntText(){
        String str = this.stepCnt + "步";
        stepCntText.setText(str);
    }

    /**
     * 添加响应事件函数
     */
    private void addListener() {

        btn_giveup.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                clearChipCom();
                AbilitySlice slice = new SelectSlice();
                Intent intent = new Intent();
                present(slice, intent);
            }
        });
        btn_tip.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                CommonDialog cd = new CommonDialog(getContext());
                cd.setCornerRadius(50);
                DirectionalLayout dl = (DirectionalLayout) LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_jigsawtip_dialog, null, false);
                Button ok_btn = dl.findComponentById(ResourceTable.Id_ok_btn);
                ok_btn.setClickedListener(new Component.ClickedListener() {
                    @Override
                    public void onClick(Component component) {
                        cd.destroy();
                    }
                });
                Image tip_img = dl.findComponentById(ResourceTable.Id_tip_image);
                tip_img.setPixelMap(jigsawPixelMap);

                cd.setSize(800, MATCH_CONTENT);
                cd.setContentCustomComponent(dl);
                cd.show();
            }
        });
        btn_begin.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                // 设置时钟
                passTime = 0;
                startTime = System.currentTimeMillis();
                tickTimer.setBaseTime(startTime - passTime);
                tickTimer.start();
                tickTimer.setVisibility(Component.VISIBLE);

                //重新设置chip的位置
                relocateChipPosition();
                // 设置图片可见
                for (Chip chip: chipMap.values()){
                    chip.getImage().setVisibility(Component.VISIBLE);
                }

                btn_begin.setClickable(false);
            }
        });

        // 为chip添加响应事件
        for (Chip chip: chipMap.values()){
            chip.getImage().setDraggedListener(Component.DRAG_HORIZONTAL_VERTICAL, new Component.DraggedListener(){
                private Point point;
                //拖拽位置
                private Point componentPo;
                @Override
                public void onDragDown(Component component, DragInfo dragInfo) {

                }

                @Override
                public void onDragStart(Component component, DragInfo dragInfo) {
                    point = dragInfo.startPoint;
                    //获取组件在父组件中的起始位置
                    componentPo = new Point(component.getContentPositionX(), component.getContentPositionY());

                    HiLog.info(label, "up");
                    Chip curChip = chipMap.get((Image) component);
                    HiLog.info(label, "chipRightX="+curChip.getRightX() + ", chipRightY="+ curChip.getRightY());
                    if (curChip.isInSpace()) {
//                            Log.d(TAG, "onTouch->"+chip.getCurX()+" "+chip.getCurY());
                        for (Blank blank : BlankList) {
                            if (blank.getX() == curChip.getCurX() && blank.getY() == curChip.getCurY()) {
                                blank.setOccupy(false);
                                break;
                            }
                        }
                        curChip.outOfSpace();
                    }
                }

                @Override
                public void onDragUpdate(Component component, DragInfo dragInfo) {
                    //计算每一次更新时的偏移量。draginfo中的偏移量是相对于上一次拖拽时的偏移量，在方向发	生冲突时，会产生位置动态刷新问题
                    float xOffset = dragInfo.updatePoint.getPointX() - point.getPointX();
                    float yOffset = dragInfo.updatePoint.getPointY() - point.getPointY();
                    //setContentPosition为组件在父组件中的位置
                    component.setContentPosition(componentPo.getPointX() + xOffset, componentPo.getPointY() + yOffset);
                    componentPo = new Point(component.getContentPositionX(), component.getContentPositionY());
                }

                @Override
                public void onDragEnd(Component component, DragInfo dragInfo) {
                    HiLog.info(label, "down x=" + component.getContentPositionX() + " y="+ component.getContentPositionY());
                    Chip curChip = chipMap.get((Image) component);
                    for(Blank blank: BlankList){
                        if(inPosition(component, blank.getImage()) && !blank.isOccupy()){
                            Component parent = (Component) blank.getImage().getComponentParent();
                            component.setContentPositionX(blank.getImage().getLeft()+parent.getLeft());
                            component.setContentPositionY(blank.getImage().getTop()+parent.getTop());


                            HiLog.info(label, "parentLeft=" + parent.getLeft()+", parentTop="+parent.getTop() );

                            // 设置当前chip的所在位置
                            curChip.setCurX(blank.getX());
                            curChip.setCurY(blank.getY());

                            blank.setOccupy(true);
                            HiLog.info(label, "ChipId="+component.getId() + ", X=" + blank.getX() + ", Y=" + blank.getY());

                            // 判断是否完成拼图
                            isBINGO();

                            stepCnt += 1;
                            setStepCntText();

                        }
                    }
                }

                @Override
                public void onDragCancel(Component component, DragInfo dragInfo) {

                }
            });
        }

    }
    /**
     * 重新挑战游戏
     */
    private void rePlay() {
        for (Blank blank: BlankList){
            blank.setOccupy(false);
        }
        tickTimer.setVisibility(Component.INVISIBLE);

        // 重新设置chip的位置
        relocateChipPosition();

        stepCnt = 0;
        setStepCntText();

        // 重新设置chip的图片
        setChip();

        btn_begin.setClickable(true);
    }


    /**
     * 获取正确拼图数量
     *
     * @return
     */
    private int getBINGOChipCnt() {
        int cnt = 0;
        for (Chip chip : chipMap.values()) {
            if (chip.isRightPosition()) cnt++;
        }
        return cnt;
    }


    /**
     * 获取拼图完成时间
     * @return
     */
    private String getPlayTime(){
        String[] timeList = tickTimer.getText().split(":");
        return timeList[0] + "'" + timeList[1] + "''" + timeList[2];
    }

    /**
     * 判断是否成功完成拼图 并完成成功响应
     */
    private boolean isBINGO() {
        HiLog.info(label, "bingoCnt=" + getBINGOChipCnt());

        if (getBINGOChipCnt() == jigsawCnt) {
            // 停止计时器
            passTime = System.currentTimeMillis() - startTime + passTime;
            tickTimer.setBaseTime(System.currentTimeMillis() - passTime);
            tickTimer.stop();

            CommonDialog cd = new CommonDialog(getContext());
            cd.setCornerRadius(50);
            DirectionalLayout dl = (DirectionalLayout) LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_gameover_dialog, null, false);
            Button replaybtn = dl.findComponentById(ResourceTable.Id_replay_btn);
            replaybtn.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    rePlay();
                    cd.destroy();
                }
            });

            Button choosejigbtn = dl.findComponentById(ResourceTable.Id_choosejig_btn);
            choosejigbtn.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    clearChipCom();
                    AbilitySlice slice = new SelectSlice();
                    Intent intent = new Intent();
                    present(slice, intent);
                    cd.destroy();
                }
            });

            Text playtimetext = dl.findComponentById(ResourceTable.Id_dialog_time_text);
            playtimetext.setText(getPlayTime());

            Text steptext = dl.findComponentById(ResourceTable.Id_dialog_step_text);
            steptext.setText(this.stepCnt+"");

            cd.setSize(1000, MATCH_CONTENT);
            cd.setContentCustomComponent(dl);
            cd.show();

            DirectionalLayout starlayout = dl.findComponentById(ResourceTable.Id_starlayout);
            starlayout.removeAllComponents();
            int starwidth = (int)(pm_px*0.1);
            int starmargin = (int)(pm_px*0.02);
            int starcnt = this.getStarCnt();
            for(int i = 0; i<starcnt; i++){
                Image star = new Image(this);
                star.setPixelMap(ResourceTable.Media_star);
                star.setWidth(starwidth);
                star.setHeight(starwidth);
                star.setMarginRight(starmargin);
                star.setMarginRight(starmargin);
                star.setScaleMode(Image.ScaleMode.STRETCH);
                starlayout.addComponent(star);
            }

        }
        return false;
    }

    /**
     * 获取星星的数量
     * @return
     */
    private int getStarCnt(){
        String[] timeList = tickTimer.getText().split(":");
        float time = Integer.parseInt(timeList[0])+ (float)(Integer.parseInt(timeList[1])/60.0);
        float timeperchip = time/this.jigsawCnt;  // 获取每个完成每块拼图的时间
        float stepperchip = (float) stepCnt/this.jigsawCnt;
        int starfortime = 0, starforstep = 0;
        int starcnt = 0;

        if(timeperchip <= 0.05){  // 3秒一个
            starfortime = 3;
        }else if(timeperchip>0.05 && timeperchip <= 0.1){
            starfortime = 2;     // 6秒一个
        }else{
            starfortime = 1;
        }

        if(stepperchip<= 2){
            starforstep = 3;
        }else if(stepperchip >2 && stepperchip<=4){
            starforstep = 2;
        }else{
            starforstep = 1;
        }

        starcnt = Math.min(starforstep, starfortime);
        return starcnt;

    }


    /**
     * 清空chipimage
     */
    private void clearChipCom(){
        for (Chip chip : chipMap.values()){
            mainlayout.removeComponent(chip.getImage());
        }
    }

    /**
     * 初始化组件
     */
    void initComponent() {
        int textSize = (int)(this.pm_px*0.05);
        int textMargin = (int)(pm_px*0.1);
        int tipSize = (int)(this.pm_px*0.5);
        int tableLayoutWidth = (int)(pm_px*0.7);
        chipWidth = (int)(tableLayoutWidth/this.jigsawRowCnt);
        int blockMargin = (int) (chipWidth*0.01);

        blocklayout = findComponentById(ResourceTable.Id_jigsaw_layout);
        blocklayout.removeAllComponents();
        blocklayout.setRowCount(this.jigsawRowCnt);
        blocklayout.setColumnCount(this.jigsawRowCnt);

        // 向tableLayout中添加Image
        for(int i = 1; i<=this.jigsawRowCnt; i++){
            for(int j = 1; j<=this.jigsawRowCnt; j++){
                Image block = new Image(this);
                block.setWidth(chipWidth);
                block.setHeight(chipWidth);
                block.setScaleMode(Image.ScaleMode.STRETCH);
                block.setMarginBottom(blockMargin);
                block.setMarginLeft(blockMargin);
                block.setMarginRight(blockMargin);
                block.setMarginTop(blockMargin);
                block.setPixelMap(ResourceTable.Media_question);
                this.blocklayout.addComponent(block);
                this.BlankList.add(new Blank(block, j, i));
            }
        }

        btn_begin = findComponentById(ResourceTable.Id_btn_begin);
        btn_tip =findComponentById(ResourceTable.Id_btn_tip);
        btn_giveup = findComponentById(ResourceTable.Id_btn_giveup);

        // 初始化定时器
        tickTimer = findComponentById(ResourceTable.Id_ticktimer);
        tickTimer.setCountDown(false);
        tickTimer.setFormat("mm:ss:SSSS");
        tickTimer.setVisibility(Component.INVISIBLE);

        stepCntText = findComponentById(ResourceTable.Id_stepCntText);

//         初始化chipImage
        mainlayout = findComponentById(ResourceTable.Id_mainlayout);
        imageList.clear();
        for(int i = 1; i<=this.jigsawRowCnt; i++){
            for(int j = 1; j<=this.jigsawRowCnt; j++){
                Image image = new Image(this);
                image.setWidth(chipWidth);
                image.setHeight(chipWidth);
                image.setScaleMode(Image.ScaleMode.STRETCH);
                image.setPixelMap(this.jigsawPixelMap);
                mainlayout.addComponent(image);
                imageList.add(image);
            }
        }

        minChipY = (int)(pg_px*0.6);maxChipY = (int)(pg_px*0.9)-chipWidth;
        minChipX = (int)(pm_px*0.1); maxChipX = (int)(pm_px*0.9)-chipWidth;

    }

    /**
     * 初始化Chip图片并设置映射关系
     */
    void setChip(){
        Collections.shuffle(imageList);

//         设置chip背景图片
        int k = 0;
        chipMap.clear();
        for(int i = 1; i<= jigsawRowCnt; i++){
            for(int j = 1; j<= jigsawRowCnt; j++, k++){
                // 获取组件
                Image chipView = imageList.get(k);
                chipView.setPixelMap(pixelMapList.get(k));
                // 添加映射关系
                chipMap.put(chipView, new Chip(i, j, chipView));
            }
        }
        relocateChipPosition();

        // 设置图片不可见
        for (Chip chip: chipMap.values()){
            chip.getImage().setVisibility(Component.INVISIBLE);
        }
    }

    /**
     * 判断chip是否在合适的位置
     * @param chip
     * @param blank
     * @return
     */
    private boolean inPosition(Component chip, Component blank) {
        int len = (int)(chipWidth*0.15);
        Component parent = (Component) blank.getComponentParent();
        float chipX = chip.getContentPositionX(), chipY = chip.getContentPositionY();
        float blankX = blank.getLeft()+parent.getLeft(), blankY = blank.getTop()+parent.getTop();
        return chipX <= blankX + len && chipX >= blankX - len && chipY <= blankY + len && chipY >= blankY - len;
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    /**
     * 重新定位chip的位置
     */
    private void relocateChipPosition() {
        Random random = new Random();
        for(int i = 0; i< jigsawCnt; i++){
            float x = minChipX + random.nextFloat()*(maxChipX-minChipX);
            float y = minChipY + random.nextFloat()*(maxChipY-minChipY);
            this.imageList.get(i).setContentPositionX(x);
            this.imageList.get(i).setContentPositionY(y);
        }
    }
}

