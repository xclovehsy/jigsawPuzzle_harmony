package com.example.jigsawpuzzle.slice;

import com.example.jigsawpuzzle.ResourceTable;
import com.example.jigsawpuzzle.game.Block;
import com.example.jigsawpuzzle.game.MyImage;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.render.Texture;
import ohos.agp.utils.Color;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.IDialog;
import ohos.backgroundtaskmgr.BackgroundTaskManager;
import ohos.global.resource.NotExistException;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.ImageInfo;
import ohos.media.image.common.Rect;
import ohos.media.image.common.Size;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_CONTENT;


public class HuarongRoadNine extends AbilitySlice {
    static final HiLogLabel label = new HiLogLabel(HiLog.LOG_APP, 0, "MY_TAG");
    private Image img_tip;
    private List<Image> blockList = new LinkedList<>();
    private Map<Image, Block> BlockMap = new HashMap<>();
    private int jigsawCnt = 0;
    private List<MyImage> imageList = new ArrayList<>();
    private Button btn_begin, btn_end;
    private int jigsawId = ResourceTable.Media_dog;
    private TableLayout jigsawLayout = null;
    private PixelMap jigsawPixelMap = null;
    private TickTimer tickTimer;
    private int jigsawRowCnt = 5;
    long startTime = 0, passTime = 0;
    private int pm_px;  // 手机设备的宽
    private int pg_px;  // 手机设备的高
    private Text stepCntText;
    private int stepCnt = 0;
    private boolean isShowNum = true; //是否显示数字
    private Button backbtn;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_huarong_slice);


        this.jigsawId = intent.getIntParam("jigsawId", ResourceTable.Media_dog);
        this.jigsawRowCnt = intent.getIntParam("diff", 3);
        this.isShowNum = intent.getBooleanParam("isShowNum", true);
        this.jigsawCnt = (int) Math.pow(this.jigsawRowCnt, 2);
        HiLog.info(label, "jigsawCnt=" + jigsawCnt);

        pm_px = AttrHelper.vp2px(getContext().getResourceManager().getDeviceCapability().width, this);
        pg_px = AttrHelper.vp2px(getContext().getResourceManager().getDeviceCapability().height, this);

        // 获取拼图图片资源
        this.jigsawPixelMap = getPixelMap(jigsawId);

        // 拼图切割
        this.imageList = getCutPixelMap(this.jigsawPixelMap);

        // 初始化布局
        initLayout();

        // 设置chip图片
        setChip();

        // 添加响应事件
        addListener();
    }

    /**
     * 设置步数text
     */
    private void setStepCntText() {
        String str = this.stepCnt + "步";
        stepCntText.setText(str);
    }

    /**
     * 初始化布局函数
     */
    private void initLayout() {
        int textSize = (int) (this.pm_px * 0.05);
        int textMargin = (int) (pm_px * 0.1);
        int tipSize = (int) (this.pm_px * 0.5);
        int tableLayoutWidth = (int) (pm_px * 0.7);
        int blockWidth = (int) (tableLayoutWidth / this.jigsawRowCnt);
        int blockMargin = (int) (blockWidth * 0.01);

//        // 设置主布局
//        DirectionalLayout mainLayout = new DirectionalLayout(this);
//        mainLayout.setOrientation(Component.VERTICAL);
//        mainLayout.setWidth(ComponentContainer.LayoutConfig.MATCH_PARENT);
//        mainLayout.setHeight(ComponentContainer.LayoutConfig.MATCH_PARENT);
//        mainLayout.setAlignment(1);

//        // 头部信息布局
//        DirectionalLayout headLayout = new DirectionalLayout(this);
//        headLayout.setOrientation(Component.HORIZONTAL);
//        headLayout.setWidth(ComponentContainer.LayoutConfig.MATCH_CONTENT);
//        headLayout.setHeight(ComponentContainer.LayoutConfig.MATCH_CONTENT);
//        headLayout.setAlignment(1);

        // 头部信息
//        Text timeText = new Text(this), stepText = new Text(this);
        this.stepCntText = findComponentById(ResourceTable.Id_stepCntText);
//        timeText.setText("用时："); stepText.setText("步数："); stepCntText.setText("10 步");
//        timeText.setTextSize(textSize); stepText.setTextSize(textSize); stepCntText.setTextSize(textSize);
//
//        timeText.setWidth(ComponentContainer.LayoutConfig.MATCH_CONTENT);
//        timeText.setHeight(ComponentContainer.LayoutConfig.MATCH_CONTENT);
//        stepText.setWidth(ComponentContainer.LayoutConfig.MATCH_CONTENT);
//        stepText.setHeight(ComponentContainer.LayoutConfig.MATCH_CONTENT);
//        stepCntText.setWidth(ComponentContainer.LayoutConfig.MATCH_CONTENT);
//        stepCntText.setHeight(ComponentContainer.LayoutConfig.MATCH_CONTENT);
//        stepText.setMarginLeft(textMargin);

        // 添加计时器
        this.tickTimer = findComponentById(ResourceTable.Id_ticktimer);
//        tickTimer.setWidth(ComponentContainer.LayoutConfig.MATCH_CONTENT);
//        tickTimer.setHeight(ComponentContainer.LayoutConfig.MATCH_CONTENT);
//        tickTimer.setVisibility(Component.INVISIBLE);
        tickTimer.setFormat("mm:ss:SSSS");
        tickTimer.setCountDown(false);
//        tickTimer.setTextSize(textSize);

//        headLayout.addComponent(timeText);headLayout.addComponent(tickTimer);
//        headLayout.addComponent(stepText);headLayout.addComponent(stepCntText);
//        mainLayout.addComponent(headLayout);
        stepCnt = 0;
        setStepCntText();

        // 添加拼图布局
//        this.jigsawLayout = new TableLayout(this);
//        this.jigsawLayout.setColumnCount(this.jigsawRowCnt);
//        this.jigsawLayout.setRowCount(this.jigsawRowCnt);
//        this.jigsawLayout.setOrientation(Component.HORIZONTAL);
//        this.jigsawLayout.setWidth(ComponentContainer.LayoutConfig.MATCH_CONTENT);
//        this.jigsawLayout.setHeight(ComponentContainer.LayoutConfig.MATCH_CONTENT);
        this.jigsawLayout = findComponentById(ResourceTable.Id_jigsaw_layout);
        jigsawLayout.removeAllComponents();
        jigsawLayout.setColumnCount(jigsawRowCnt);
        jigsawLayout.setRowCount(jigsawRowCnt);

        // 向tableLayout中添加Image
        this.blockList = new LinkedList<>();
        for (int i = 1; i <= this.jigsawRowCnt; i++) {
            for (int j = 1; j <= this.jigsawRowCnt; j++) {
                Image block = new Image(this);
                block.setWidth(blockWidth);
                block.setHeight(blockWidth);
                block.setScaleMode(Image.ScaleMode.STRETCH);
                block.setMarginBottom(blockMargin);
                block.setMarginLeft(blockMargin);
                block.setMarginRight(blockMargin);
                block.setMarginTop(blockMargin);
//                block.setPixelMap(this.jigsawPixelMap);
                this.jigsawLayout.addComponent(block);
                this.blockList.add(block);
            }
        }
//        mainLayout.addComponent(this.jigsawLayout);

        // 开始结束按钮布局
//        DirectionalLayout buttonLayout = new DirectionalLayout(this);
//        buttonLayout.setOrientation(Component.HORIZONTAL);
//        buttonLayout.setWidth(ComponentContainer.LayoutConfig.MATCH_CONTENT);
//        buttonLayout.setHeight(ComponentContainer.LayoutConfig.MATCH_CONTENT);
//        buttonLayout.setAlignment(1);

        this.btn_begin = findComponentById(ResourceTable.Id_btn_begin);
//        btn_begin.setText("开始游戏");
//        btn_begin.setTextSize(textSize);
//        btn_begin.setWidth(ComponentContainer.LayoutConfig.MATCH_CONTENT);
//        btn_begin.setHeight(ComponentContainer.LayoutConfig.MATCH_CONTENT);

        this.btn_end = findComponentById(ResourceTable.Id_btn_end);
//        btn_end.setText("结束");
//        btn_end.setTextSize(textSize);
//        btn_end.setWidth(ComponentContainer.LayoutConfig.MATCH_CONTENT);
//        btn_end.setHeight(ComponentContainer.LayoutConfig.MATCH_CONTENT);
//        btn_end.setMarginLeft(textMargin);
//        buttonLayout.addComponent(btn_begin);
//        buttonLayout.addComponent(btn_end);
//        mainLayout.addComponent(buttonLayout);
        btn_end.setClickable(false);

        this.img_tip = findComponentById(ResourceTable.Id_img_tip);
//        img_tip.setWidth(tipSize);
//        img_tip.setHeight(tipSize);
//        img_tip.setScaleMode(Image.ScaleMode.STRETCH);
        img_tip.setPixelMap(this.jigsawPixelMap);
//        mainLayout.addComponent(img_tip);

        passTime = 0;
        startTime = System.currentTimeMillis();
        tickTimer.setBaseTime(startTime - passTime);

        this.backbtn = findComponentById(ResourceTable.Id_btn_giveup);

//        super.setUIContent(mainLayout);
    }

    /**
     * 添加响应事件函数
     */
    private void addListener() {

        btn_begin.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                // 设置时钟
                TimerBegin();
                // 开始游戏
                GameBegin();

                btn_end.setClickable(true);
                btn_begin.setClickable(false);
            }
        });

        btn_end.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                isBINGO();
            }
        });

        backbtn.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                AbilitySlice slice = new SelectSlice();
                Intent intent = new Intent();
                present(slice, intent);
            }
        });



    }

    private void TimerBegin() {
        // 设置时钟
        passTime = 0;
        startTime = System.currentTimeMillis();
        tickTimer.setBaseTime(startTime - passTime);
        tickTimer.start();
        tickTimer.setVisibility(Component.VISIBLE);
    }

    /**
     * 切割图片功能
     *
     * @param pixelMap
     * @return
     */
    private List<PixelMap> cutPic(PixelMap pixelMap) {
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
        int pieceSize = (int) Math.floor(size / (float) this.jigsawRowCnt);
        for (int i = 0; i < this.jigsawRowCnt; i++) {
            for (int j = 0; j < this.jigsawRowCnt; j++) {
                int x = j * pieceSize;
                int y = i * pieceSize;
                Rect rect1 = new Rect();
                rect1.height = pieceSize;
                rect1.width = pieceSize;
                rect1.minX = x;
                rect1.minY = y;
                PixelMap temp = PixelMap.create(pixelMap1, rect1, options);

                if (this.isShowNum) {
                    Canvas canvas = new Canvas(new Texture((temp)));
                    Paint paint = new Paint();
                    paint.setTextSize(pieceSize * 2);
                    paint.setColor(Color.RED);
                    t++;
                    canvas.drawText(paint, "" + t, pieceSize, pieceSize * 2);
                }

                pixelMapList.add(temp);
            }
        }

        HiLog.info(label, "temp=" + pixelMapList.size());
        return pixelMapList;
    }

    /**
     * 获取图片pixelmap资源
     *
     * @param imageId
     * @return
     */
    PixelMap getPixelMap(int imageId) {
        ImageSource imageSource = null;
        InputStream inputStream = null;
        try {
            inputStream = getContext().getResourceManager().getResource(imageId);
            imageSource = ImageSource.create(inputStream, null);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotExistException e) {
            e.printStackTrace();
        }
        PixelMap pix = imageSource.createPixelmap(null);
        return pix;
    }

    /**
     * 交换block
     */
    private void blockSwap(Block block) {
        int x = block.getX();
        int y = block.getY();

        for (Block block1 : BlockMap.values()) {
            int tx = block1.getX(), ty = block1.getY();
            if (Math.abs(tx + ty - x - y) == 1 && block1.isBlank()) {
                MyImage timg = block.getImage();
                block.setImage(block1.getImage());
                block1.setImage(timg);

                block.setBlank(true);
                block1.setBlank(false);

                // 设置
                block.getView().setVisibility(Component.INVISIBLE);
                block1.getView().setVisibility(Component.VISIBLE);
                block1.RenewImage();

                this.stepCnt += 1;
                setStepCntText();
            }
        }
    }

    /**
     * 重新挑战游戏
     */
    private void rePlay() {
        tickTimer.setVisibility(Component.INVISIBLE);
        // 重新设置chip的图片
        setChip();
        btn_begin.setClickable(true);
        btn_end.setClickable(false);

        this.stepCnt = 0;
        this.setStepCntText();
    }


    /**
     * 获取正确拼图数量
     *
     * @return
     */
    private int getBINGOChipCnt() {
        int cnt = 0;
        for (Block block : BlockMap.values()) {
            HiLog.info(label, "test: x=" + block.getX() + ", y=" + block.getY() + ", rx=" + block.getImage().getRightX() + ", ry=" + block.getImage().getRightY());
            if (block.isRightPosition()) cnt++;
        }
        return cnt;
    }

    // 时钟停止
    private void TimerEnd() {
        // 停止计时器
        passTime = System.currentTimeMillis() - startTime + passTime;
        tickTimer.setBaseTime(System.currentTimeMillis() - passTime);
        tickTimer.stop();
    }


    /**
     * 获取拼图完成时间
     *
     * @return
     */
    private String getPlayTime() {
        String[] timeList = tickTimer.getText().split(":");
        return timeList[0] + "'" + timeList[1] + "''" + timeList[2];
    }

    /**
     * 获取星星的数量
     *
     * @return
     */
    private int getStarCnt() {
        String[] timeList = tickTimer.getText().split(":");
        float time = Integer.parseInt(timeList[0]) + (float) (Integer.parseInt(timeList[1]) / 60.0);
        float timeperchip = time / this.jigsawCnt;  // 获取每个完成每块拼图的时间
        float stepperchip = (float) stepCnt / this.jigsawCnt;
        int starfortime = 0, starforstep = 0;
        int starcnt = 0;

        if (timeperchip <= 0.05) {  // 3秒一个
            starfortime = 3;
        } else if (timeperchip > 0.05 && timeperchip <= 0.1) {
            starfortime = 2;     // 6秒一个
        } else {
            starfortime = 1;
        }

        if (stepperchip <= 2) {
            starforstep = 3;
        } else if (stepperchip > 2 && stepperchip <= 4) {
            starforstep = 2;
        } else {
            starforstep = 1;
        }

        starcnt = Math.min(starforstep, starfortime);
        return starcnt;

    }

    /**
     * 判断是否成功完成拼图 并完成成功响应
     */
    private boolean isBINGO() {
        HiLog.info(label, "bingoCnt=" + getBINGOChipCnt());

        if (getBINGOChipCnt() == jigsawCnt) {
            TimerEnd();


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
                    AbilitySlice slice = new SelectSlice();
                    Intent intent = new Intent();
                    present(slice, intent);
                    cd.destroy();
                }
            });

            Text playtimetext = dl.findComponentById(ResourceTable.Id_dialog_time_text);
            playtimetext.setText(getPlayTime());

            Text steptext = dl.findComponentById(ResourceTable.Id_dialog_step_text);
            steptext.setText(this.stepCnt + "");

            cd.setSize(1000, MATCH_CONTENT);
            cd.setContentCustomComponent(dl);
            cd.show();

            DirectionalLayout starlayout = dl.findComponentById(ResourceTable.Id_starlayout);
            starlayout.removeAllComponents();
            int starwidth = (int) (pm_px * 0.1);
            int starmargin = (int) (pm_px * 0.02);
            int starcnt = this.getStarCnt();
            for (int i = 0; i < starcnt; i++) {
                Image star = new Image(this);
                star.setPixelMap(ResourceTable.Media_star);
                star.setWidth(starwidth);
                star.setHeight(starwidth);
                star.setScaleMode(Image.ScaleMode.STRETCH);
                star.setMarginRight(starmargin);
                star.setMarginRight(starmargin);
                starlayout.addComponent(star);
            }


//            // 创建弹窗
//            CommonDialog cd = new CommonDialog(this);
//            cd.setTitleText("拼图完成");
//            cd.setContentText("恭喜，您的成绩是: " + getPlayTime());
//            cd.setAutoClosable(true);
//            cd.setButton(0, "选择图片难度", new IDialog.ClickedListener() {
//                @Override
//                public void onClick(IDialog iDialog, int i) {
//                    AbilitySlice slice = new SelectSlice();
//                    Intent intent = new Intent();
//                    present(slice, intent);
//                    cd.destroy();
//                }
//            });
//            cd.setButton(1, "重新挑战", new IDialog.ClickedListener() {
//                @Override
//                public void onClick(IDialog iDialog, int i) {
//                    rePlay();
//                    cd.destroy();
//                    btn_end.setClickable(false);
//                    btn_begin.setClickable(true);
//                }
//            });
//            cd.setButton(2, "确定", new IDialog.ClickedListener() {
//                @Override
//                public void onClick(IDialog iDialog, int i) {
//                    cd.destroy();
//                }
//            });
//            cd.show();
        } else {

            CommonDialog cd = new CommonDialog(getContext());
            cd.setCornerRadius(50);
            DirectionalLayout dl = (DirectionalLayout) LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_huarong_fail, null, false);
            Button replaybtn = dl.findComponentById(ResourceTable.Id_replay_btn);
            replaybtn.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    rePlay();
                    cd.destroy();
                }
            });

            Button giveupbtn = dl.findComponentById(ResourceTable.Id_choosejig_btn);
            giveupbtn.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    AbilitySlice slice = new SelectSlice();
                    Intent intent = new Intent();
                    present(slice, intent);
                    cd.destroy();
                }
            });

            Button gonobtn = dl.findComponentById(ResourceTable.Id_goon_btn);
            gonobtn.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    cd.destroy();
                }
            });

            cd.setSize(1000, MATCH_CONTENT);
            cd.setContentCustomComponent(dl);
            cd.show();

//            // 创建弹窗
//            CommonDialog cd = new CommonDialog(this);
//            cd.setTitleText("拼图未完成");
//            cd.setContentText("您未将所有拼图放到正确的位置！");
//            cd.setAutoClosable(true);
//            cd.setButton(0, "放弃挑战", new IDialog.ClickedListener() {
//                @Override
//                public void onClick(IDialog iDialog, int i) {
//                    AbilitySlice slice = new SelectSlice();
//                    Intent intent = new Intent();
//                    present(slice, intent);
//                    cd.destroy();
//                }
//            });
//            cd.setButton(1, "重新挑战", new IDialog.ClickedListener() {
//                @Override
//                public void onClick(IDialog iDialog, int i) {
//                    rePlay();
//                    cd.destroy();
//                }
//            });
//            cd.setButton(2, "确定", new IDialog.ClickedListener() {
//                @Override
//                public void onClick(IDialog iDialog, int i) {
//                    cd.destroy();
//                }
//            });
//            cd.show();
        }
        return false;
    }

    /**
     * 获取切割拼图
     *
     * @param pixelMap
     * @return
     */
    List<MyImage> getCutPixelMap(PixelMap pixelMap) {
        List<PixelMap> pixelMapList = cutPic(pixelMap);
        List<MyImage> tempPixelMapList = new LinkedList<>();
        int k = 0;
        for (int i = 1; i <= this.jigsawRowCnt; i++) {
            for (int j = 1; j <= this.jigsawRowCnt; j++, k++) {
                if (k == jigsawCnt - 1) {
                    break;
                }
                tempPixelMapList.add(new MyImage(i, j, pixelMapList.get(k)));
            }
        }
        HiLog.info(label, "tempPixelMapListSize=" + tempPixelMapList.size());
        return tempPixelMapList;
    }

    /**
     * 开始游戏
     */
    void GameBegin() {
        List<MyImage> temp = new LinkedList<>(this.imageList);

        // 打乱
        Collections.shuffle(temp);

        // 设置chip背景图片
        int k = 0;
        BlockMap.clear();
        for (int i = 1; i <= this.jigsawRowCnt; i++) {
            for (int j = 1; j <= this.jigsawRowCnt; j++, k++) {
                if (k == jigsawCnt - 1) {
                    break;
                }
                // 获取组件
                Image blockView = this.blockList.get(k);
                blockView.setPixelMap(temp.get(k).getPixelMap());
                blockView.setVisibility(Component.VISIBLE);
                // 添加映射关系
                BlockMap.put(this.blockList.get(k), new Block(i, j, temp.get(k), blockView, false));
            }
        }

//        添加最后一个空白
        Image blockView = this.blockList.get(jigsawCnt - 1);
        // 设置最后一个不可见
        blockView.setVisibility(Component.INVISIBLE);
        // 添加映射关系
        BlockMap.put(this.blockList.get(jigsawCnt - 1), new Block(this.jigsawRowCnt, this.jigsawRowCnt, new MyImage(this.jigsawRowCnt, this.jigsawRowCnt, null), blockView, true));

        // 添加响应事件
        for (Block block : BlockMap.values()) {
            block.getView().setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    Block myBlock = BlockMap.get((Image) component);
                    blockSwap(myBlock);
                }
            });
        }
    }

    /**
     * 初始化Chip图片并设置映射关系
     */
    void setChip() {
        // 设置chip背景图片
        int k = 0;
        for (int i = 1; i <= this.jigsawRowCnt; i++) {
            for (int j = 1; j <= this.jigsawRowCnt; j++, k++) {
                if (k == jigsawCnt - 1) {
                    break;
                }
                // 获取组件
                Image blockView = this.blockList.get(k);
                blockView.setPixelMap(this.imageList.get(k).getPixelMap());
                blockView.setVisibility(Component.VISIBLE);
                blockView.setClickedListener(null);
            }
        }
//        添加最后一个空白
        Image blockView = this.blockList.get(jigsawCnt - 1);
        // 设置最后一个不可见
        blockView.setVisibility(Component.INVISIBLE);
        blockView.setClickedListener(null);
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