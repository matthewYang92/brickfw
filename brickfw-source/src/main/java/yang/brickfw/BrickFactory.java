package yang.brickfw;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 砖厂
 *
 * 负责 砖块(Brick) 的类别管理与生产
 *
 */
public class BrickFactory {
    private static final String TAG = "BrickFactory";
    private static final Map<String, AbstractBrickBuilder> sBrickBuilders = new HashMap<>();
    private static final Map<String, AbstractBrickEventBinder> sBrickEventBinderMap = new HashMap<>(); // Map<handlerClassName, binder>


    public static void init() {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Class<?> brickInitClass = classLoader.loadClass(BrickHolder.class.getPackage().getName() + ".BrickInitializer");
            brickInitClass.getMethod("initBrickBuilderMap", Map.class).invoke(null, sBrickBuilders);
            brickInitClass.getMethod("initBrickBinderMap", Map.class).invoke(null, sBrickEventBinderMap);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    static void bindEvent(Object handler, View itemView, BrickInfo info) {
        String className = handler.getClass().getName();
        AbstractBrickEventBinder eventHandler = sBrickEventBinderMap.get(className);
        if (null != eventHandler) {
            if (classMethodsContainAnnotation(handler.getClass(), OnBrickItemClick.class)) {
                eventHandler.bindBrickOnItemClick(handler, itemView, info);
            }
            if (classMethodsContainAnnotation(handler.getClass(), OnBrickItemLongClick.class)) {
                eventHandler.bindBrickOnItemLongClick(handler, itemView, info);
            }
            if (classMethodsContainAnnotation(handler.getClass(), OnBrickEvent.class)) {
                eventHandler.bindBrickOnEvent(handler, itemView, info);
            }
        } else {
            Log.w(TAG, "unSupport handler class " + className);
        }
    }

    private static  boolean classMethodsContainAnnotation(Class<?> aClass, Class<? extends Annotation> annotationType) {
        for(Method method : aClass.getDeclaredMethods()) {
            if (null != method.getAnnotation(annotationType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 创建指定类型的Brick模块
     *
     * @param parent
     * @param type
     * @return
     */
    public static AbstractBrickHolder createBrick(@NonNull ViewGroup parent, @Nullable String type) {
        AbstractBrickBuilder build = sBrickBuilders.get(type);
        return build != null ? build.create(parent.getContext()) : newEmptyBrick(parent, type);
    }

    private static GhostBrick newEmptyBrick(final ViewGroup parent, String type) {
        Log.w(TAG, "new empty brick with type: " + type);
        return new GhostBrick(parent);
    }
}
