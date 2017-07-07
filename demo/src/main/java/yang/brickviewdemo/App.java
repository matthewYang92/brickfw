package yang.brickviewdemo;

import android.app.Application;

import yang.brickfw.BrickFactory;
import yang.brickfw.BrickInit;

/**
 * author: Matthew Yang on 17/6/6
 * e-mail: yangtian@yy.com
 */
@BrickInit
public class App extends Application {

    @Override public void onCreate() {
        super.onCreate();
        BrickFactory.init(getClass());
    }
}

