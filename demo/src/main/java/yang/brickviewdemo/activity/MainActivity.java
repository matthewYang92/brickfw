package yang.brickviewdemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import yang.brickfw.BrickInfo;
import yang.brickfw.BrickRecyclerView;
import yang.brickfw.OnBrickEvent;
import yang.brickfw.OnBrickItemClick;
import yang.brickfw.OnBrickItemLongClick;
import yang.brickviewdemo.BrickType;
import yang.brickviewdemo.R;
import yang.brickviewdemo.entity.ImageText;

public class MainActivity extends AppCompatActivity {

    BrickRecyclerView brickRecyclerView;
    String[] titles = {"图片列表", "自定义图文列表", "多类型混合列表"};
    String[] imageUrls = {"http://image.uuu9.com/pcgame/dota2//UploadFiles//201509/15091109384873610.png"
            , "http://image.uuu9.com/pcgame/dota2//UploadFiles//201509/15091109384947018.png"
            , "http://image.uuu9.com/pcgame/dota2//UploadFiles//201509/150911094129546134.png"
            , "http://image.uuu9.com/pcgame/dota2//UploadFiles//201509/150911094129905141.png"
            , "http://image.uuu9.com/pcgame/dota2//UploadFiles//201509/150911094130217149.png"
            , "http://image.uuu9.com/pcgame/dota2//UploadFiles//201509/150911094130249150.png"
            , "http://image.uuu9.com/pcgame/dota2//UploadFiles//201509/150911094130483155.png"
            , "http://image.uuu9.com/pcgame/dota2//UploadFiles//201509/150911094130592157.png"
            , "http://image.uuu9.com/pcgame/dota2//UploadFiles//201509/150911094130092146.png"
            , "http://image.uuu9.com/pcgame/dota2//UploadFiles//201509/150911094132186193.png"};
    String[] imageTexts = {"树枝"
            , "魔瓶"
            , "相位鞋"
            , "暗影护符"
            , "跳刀"
            , "支援之鞋"
            , "刃甲"
            , "夜叉"
            , "团队之手"
            , "代达罗斯之殇"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        brickRecyclerView = (BrickRecyclerView) findViewById(R.id.recycler);
        //注册事件响应
        brickRecyclerView.setEventHandler(this);
        brickRecyclerView.setNormalLayout(getApplicationContext(), 2);
        setTextList();
    }

    private void setTextList() {
        brickRecyclerView.clear();
        for (String content : titles) {
            brickRecyclerView.addData(BrickType.TEXT, content);
        }
    }

    private void setImageList() {
        brickRecyclerView.clear();
        for (String url : imageUrls) {
            BrickInfo info = new BrickInfo(BrickType.IMAGE, url, 2); //类型 数据 占位数
            brickRecyclerView.addBrick(info);
        }
    }

    private void setImageTextList() {
        brickRecyclerView.clear();
        List<BrickInfo> brickInfos = new ArrayList<>();
        for (int i = 0; i < imageTexts.length; i++) {
            ImageText imageText = new ImageText(imageUrls[i], imageTexts[i]);
            brickInfos.add(new BrickInfo(BrickType.IMAGE_TEXT, imageText, 2));
        }
        brickRecyclerView.setBrickList(brickInfos);
    }

    private void setMultipleList() {
        brickRecyclerView.clear();
        List<BrickInfo> brickInfos = new ArrayList<>();
        /*
        依次添加 4*IMAGE 2*TEXT 6*IMAGE_TEXT 2*TEXT 4*IMAGE
         */
        for (int i = 0; i < 4; i++) {
            brickInfos.add(new BrickInfo(BrickType.IMAGE, imageUrls[i], 2));
        }
        for (int i = 0; i < 2; i++) {
            brickInfos.add(new BrickInfo(BrickType.TEXT, imageTexts[i]));
        }
        for (int i = 0; i < 6; i++) {
            brickInfos.add(new BrickInfo(BrickType.IMAGE_TEXT, new ImageText(imageUrls[i], imageTexts[i]), 2));
        }
        for (int i = 0; i < 2; i++) {
            brickInfos.add(new BrickInfo(BrickType.TEXT, imageTexts[9 - i]));
        }
        for (int i = 0; i < 4; i++) {
            brickInfos.add(new BrickInfo(BrickType.IMAGE, imageUrls[9 - i], 2));
        }
        brickRecyclerView.setBrickList(brickInfos);
    }

    private void showToast(String content) {
        Toast.makeText(getApplicationContext(), content, Toast.LENGTH_LONG).show();
    }

    @OnBrickItemClick(BrickType.TEXT)
    public void onClickText(BrickInfo info, TextView textView) {
        String content = textView.getText().toString();
        switch (content) {
            case "图片列表":
                setImageList();
                break;
            case "自定义图文列表":
                setImageTextList();
                break;
            case "多类型混合列表":
                setMultipleList();
                break;
            default:
                showToast(content);
                break;
        }
    }

    @OnBrickItemClick(BrickType.IMAGE)
    public void onClickImage(BrickInfo info, ImageView imageView) {
        showToast("onClick Image position:" + info.getPositionInfo().getIdxInGlobal());
    }

    @OnBrickItemLongClick(BrickType.IMAGE)
    public boolean onLongClickImage(BrickInfo info, ImageView imageView) {
        showToast("onLongClick Image hashCode:" + imageView.hashCode());
        return true;
    }

    @OnBrickEvent(value = BrickType.IMAGE_TEXT, eventType = 0)
    public void handleImageTextClickEvent(BrickInfo info, Object... args) {
        ImageText data = (ImageText) args[0];
        showToast("handleImageTextClickEvent content : " + data.getContent());
    }

    @OnBrickEvent(value = BrickType.IMAGE_TEXT, eventType = 1)
    public void handleImageTextLongClickEvent(BrickInfo info, Object... args) {
        ImageText data = (ImageText) args[0];
        showToast("handleImageTextLongClickEvent content : " + data.getContent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("reset").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("reset")) {
            brickRecyclerView.setNormalLayout(getApplicationContext(), 2);
            setTextList();
        }
        return super.onOptionsItemSelected(item);
    }
}
