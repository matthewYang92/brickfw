package yang.brickfw.file;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.List;
import java.util.Map;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import yang.brickfw.BrickEventElement;
import yang.brickfw.BrickEventHandler;
import yang.brickfw.OnBrickEvent;
import yang.brickfw.OnBrickItemClick;
import yang.brickfw.OnBrickItemLongClick;

/**
 * author: Matthew Yang on 17/6/8
 * e-mail: yangtian@yy.com
 */

public class EventBinderFileBuilder {

    private String packageName;
    private Element typeElement;
    private BrickEventElement eventElement;

    public EventBinderFileBuilder(String packageName, Element typeElement, BrickEventElement eventElement) {
        this.packageName = packageName;
        this.typeElement = typeElement;
        this.eventElement = eventElement;
    }

    /**
     * 构造处理onClick事件方法
     * @return
     */
    private MethodSpec handleClickMethod() {
        MethodSpec.Builder handleEventBuilder = MethodSpec.methodBuilder("handleClick")
                .addModifiers(Modifier.PRIVATE)
                .addParameter(ClassName.get(typeElement.asType()), "handler")
                .addParameter(ParameterSpec.builder(ClassName.get(packageName, "BrickInfo"), "info").build())
                .addParameter(ClassName.get("android.view", "View"), "itemView");
        Map<Element, List<ExecutableElement>> eventMap = eventElement.getViewEventElementMap();
        for (Element viewElement : eventMap.keySet()) {
            for (ExecutableElement methodElement : eventMap.get(viewElement)) {
                OnBrickItemClick clickAnnotation = methodElement.getAnnotation(OnBrickItemClick.class);
                if (null != clickAnnotation) {
                    handleEventBuilder.beginControlFlow("if(info.getType().equals($S))",
                            clickAnnotation.value())
                            .addStatement("handler.$N(info, ($T) itemView)", methodElement.getSimpleName(), viewElement.asType())
                            .endControlFlow();
                }
            }
        }
        return handleEventBuilder.build();
    }

    /**
     * 构造处理onLongClick事件方法
     * @return
     */
    private MethodSpec handleLongClickMethod() {
        MethodSpec.Builder handleEventBuilder = MethodSpec.methodBuilder("handleLongClick")
                .addModifiers(Modifier.PRIVATE)
                .returns(TypeName.BOOLEAN)
                .addParameter(ClassName.get(typeElement.asType()), "handler")
                .addParameter(ParameterSpec.builder(ClassName.get(packageName, "BrickInfo"), "info").build())
                .addParameter(ClassName.get("android.view", "View"), "itemView");
        Map<Element, List<ExecutableElement>> eventMap = eventElement.getViewEventElementMap();
        for (Element viewElement : eventMap.keySet()) {
            for (ExecutableElement methodElement : eventMap.get(viewElement)) {
                OnBrickItemLongClick longClickAnnotation = methodElement.getAnnotation(OnBrickItemLongClick.class);
                if (null != longClickAnnotation) {
                    handleEventBuilder.beginControlFlow("if(info.getType().equals($S))", longClickAnnotation.value())
                            .addStatement("return handler.$N(info, ($T) itemView)", methodElement.getSimpleName(), viewElement.asType())
                            .endControlFlow();
                }
            }
        }
        handleEventBuilder.addStatement("return false");
        return handleEventBuilder.build();
    }

    /**
     * 构造处理onBrickEvent事件方法
     * @return
     */
    private MethodSpec handleBrickEventMethod() {
        MethodSpec.Builder handleEventBuilder = MethodSpec.methodBuilder("handleBrickEvent")
                .addModifiers(Modifier.PRIVATE)
                .addParameter(ParameterSpec
                        .builder(ClassName.get(typeElement.asType()), "handler")
                        .addModifiers(Modifier.FINAL).build())
                .addParameter(ParameterSpec.builder(ClassName.get(packageName, "BrickInfo"), "info")
                        .addModifiers(Modifier.FINAL).build())
                .addParameter(ClassName.get("android.view", "View"), "itemView");
        Map<Map.Entry<Element, Element>, List<ExecutableElement>> eventMap = eventElement.getBrickEventElementMap();
        for (Map.Entry<Element, Element> entry : eventMap.keySet()) {
            Element handlerElement = entry.getValue();
            Element viewElement = entry.getKey();
            String value = handlerElement.getAnnotation(BrickEventHandler.class).value();
            handleEventBuilder.beginControlFlow("if(info.getType().equals($S))", value)
                    .beginControlFlow("(($T) itemView).$N = new $T()"
                            , viewElement.asType()
                            , handlerElement.getSimpleName()
                            , handlerElement.asType())
                    .beginControlFlow("@Override public void handleBrickEvent(int eventType, Object... params)");
            for (ExecutableElement methodElement : eventMap.get(entry)) {
                OnBrickEvent brickEventAnnotation = methodElement.getAnnotation(OnBrickEvent.class);
                if (null != brickEventAnnotation) {
                    int eventType = brickEventAnnotation.eventType();
                    handleEventBuilder.beginControlFlow("if (eventType == $L)", eventType)
                            .addStatement("handler.$N(info, params)", methodElement.getSimpleName())
                            .endControlFlow();
                }
            }
            handleEventBuilder.endControlFlow()
                    .endControlFlow("")
                    .endControlFlow();
        }
        return handleEventBuilder.build();
    }


    private MethodSpec bindOnItemClickMethod() {
        MethodSpec.Builder bindClickBuilder = MethodSpec.methodBuilder("bindBrickOnItemClick")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(ClassName.get(Object.class), "handler")
                        .addModifiers(Modifier.FINAL).build())
                .addParameter(ParameterSpec.builder(ClassName.get("android.view", "View"), "view")
                        .addModifiers(Modifier.FINAL).build())
                .addParameter(ParameterSpec.builder(ClassName.get(packageName, "BrickInfo"), "info")
                        .addModifiers(Modifier.FINAL).build())
                .beginControlFlow("View.OnClickListener onClickListener = new View.OnClickListener()")
                .beginControlFlow("@Override public void onClick(View v)")
                .addStatement("handleClick(($T) handler, info, view)", typeElement.asType())
                .endControlFlow()
                .endControlFlow("")
                .addStatement("view.setOnClickListener(onClickListener)");
        return bindClickBuilder.build();
    }

    private MethodSpec bindOnItemLongClickMethod() {
        MethodSpec.Builder bindLongClickBuilder = MethodSpec.methodBuilder("bindBrickOnItemLongClick")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(ClassName.get(Object.class), "handler")
                        .addModifiers(Modifier.FINAL).build())
                .addParameter(ParameterSpec.builder(ClassName.get("android.view", "View"), "view")
                        .addModifiers(Modifier.FINAL).build())
                .addParameter(ParameterSpec.builder(ClassName.get(packageName, "BrickInfo"), "info")
                        .addModifiers(Modifier.FINAL).build())
                .beginControlFlow("View.OnLongClickListener onLongClickListener = new View.OnLongClickListener()")
                .beginControlFlow("@Override public boolean onLongClick(View v)")
                .addStatement("return handleLongClick(($T) handler, info, view)", typeElement.asType())
                .endControlFlow()
                .endControlFlow("")
                .addStatement("view.setOnLongClickListener(onLongClickListener)");
        return bindLongClickBuilder.build();
    }

    private MethodSpec bindBrickOnEventMethod() {
        MethodSpec.Builder bindLongClickBuilder = MethodSpec.methodBuilder("bindBrickOnEvent")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(ClassName.get(Object.class), "handler")
                        .addModifiers(Modifier.FINAL).build())
                .addParameter(ParameterSpec.builder(ClassName.get("android.view", "View"), "view")
                        .addModifiers(Modifier.FINAL).build())
                .addParameter(ParameterSpec.builder(ClassName.get(packageName, "BrickInfo"), "info")
                        .addModifiers(Modifier.FINAL).build())
                .addStatement("handleBrickEvent(($T) handler, info, view)", typeElement.asType());
        return bindLongClickBuilder.build();
    }

    /**
     * 生成BrickBuilder类
     * @return
     */
    private TypeSpec eventBinderClass() {
        return TypeSpec.classBuilder(typeElement.getSimpleName() + "EventBinder")
                .addModifiers(Modifier.PUBLIC)
                .addMethod(handleClickMethod())
                .addMethod(bindOnItemClickMethod())
                .addMethod(handleLongClickMethod())
                .addMethod(bindOnItemLongClickMethod())
                .addMethod(handleBrickEventMethod())
                .addMethod(bindBrickOnEventMethod())
                .superclass(ClassName.get(packageName, "AbstractBrickEventBinder"))
                .build();
    }

    public JavaFile build() {
        return JavaFile.builder(packageName, eventBinderClass()).build();
    }
}
