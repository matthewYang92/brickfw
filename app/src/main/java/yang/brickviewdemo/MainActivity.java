package yang.brickviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import yang.brickfw.BrickRecyclerView;
import yang.brickfw.OnBrickEvent;
import yang.brickfw.OnBrickItemClick;
import yang.brickfw.OnBrickItemLongClick;
import yang.brickviewdemo.entity.ImageText;
import yang.brickviewdemo.widget.ImageTextView;

public class MainActivity extends AppCompatActivity {

    BrickRecyclerView brickRecyclerView;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        brickRecyclerView = (BrickRecyclerView) findViewById(R.id.recycler);
        brickRecyclerView.setEventHandler(this);
        String url = "http://img95.699pic.com/photo/00010/8940.jpg_wh300.jpg";
        String content = "this is a brick text";
        for (int i = 0; i < 5; i++) {
            brickRecyclerView.addData("simple_text", content);
        }
        for (int i = 0; i < 6; i++) {
            brickRecyclerView.addData("simple_image", url, 2);
        }
        for (int i = 0; i < 5; i++) {
            brickRecyclerView.addData("image_and_text", new ImageText(url, content));
        }
    }

    //@OnBrickItemClick("image_and_text")
    public void onClickImageText(ImageTextView imageTextView) {
        Toast.makeText(getApplicationContext(),
                String.format("onClick class %s hashCode %s", imageTextView.getClass(), imageTextView.hashCode())
                , Toast.LENGTH_LONG).show();
    }

    @OnBrickItemClick("simple_image")
    public void onClickImage(ImageView imageView) {
        Toast.makeText(getApplicationContext(),
                String.format("onClick class %s hashCode %s", imageView.getClass(), imageView.hashCode())
                , Toast.LENGTH_LONG).show();
    }

    @OnBrickItemLongClick("simple_image")
    public boolean onLongClickImage(ImageView imageView) {
        Toast.makeText(getApplicationContext(),
                String.format("onLongClickImage class %s hashCode %s", imageView.getClass(), imageView.hashCode())
                , Toast.LENGTH_LONG).show();
        return true;
    }

    @OnBrickEvent(value = "image_and_text", eventType = 0)
    public void handle0(Object... params) {
        Toast.makeText(getApplicationContext(),
                String.format("handle params: %s", params)
                , Toast.LENGTH_LONG).show();

    }

    @OnBrickEvent(value = "image_and_text", eventType = 1)
    public void handle1(Object... params) {
        Toast.makeText(getApplicationContext(),
                String.format("handle params: %s", params)
                , Toast.LENGTH_LONG).show();

    }

}
