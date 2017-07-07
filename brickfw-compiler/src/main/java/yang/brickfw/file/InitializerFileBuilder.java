package yang.brickfw.file;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import yang.brickfw.BrickElement;
import yang.brickfw.util.StringUtil;

/**
 * author: Matthew Yang on 17/6/8
 * e-mail: yangtian@yy.com
 */

public class InitializerFileBuilder {

    private Map<String, BrickElement> brickBuilderMap = new HashMap<>();
    private Set<Element> eventHandlerElements = new HashSet<>();
    private String packageName;
    private String initPackageName;

    public InitializerFileBuilder(String packageName, String initPackageName, Map<String, BrickElement> brickBuilderMap, Set<Element> eventHandlerElements) {
        this.brickBuilderMap = brickBuilderMap;
        this.eventHandlerElements = eventHandlerElements;
        this.packageName = packageName;
        this.initPackageName = initPackageName;
    }

    /**
     * 构造initBrickBuilderMap方法
     * @return
     */
    private MethodSpec initBuilderMethod() {
        //构造initBrickBuilderMap方法
        MethodSpec.Builder initBuilder = MethodSpec.methodBuilder("initBrickBuilderMap")
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .addParameter(ParameterizedTypeName
                                .get(ClassName.get(Map.class)
                                        , TypeName.get(String.class)
                                        , ClassName.get(packageName, "AbstractBrickBuilder"))
                        , "brickBuilderMap");
        //先clear原始map
        initBuilder.addStatement("brickBuilderMap.clear()");
        //将@BrickView @BrickHolder注释的类添加到map中
        for (String type : brickBuilderMap.keySet()) {
            BrickElement brickElement = brickBuilderMap.get(type);
            String simpleName = brickElement.getHolderSimpleName().toString().replace("Holder", "");
            String builderName = simpleName + "Builder";
            initBuilder.addStatement("$T $L = new $T()"
                    , ClassName.get(brickElement.getPackageName(), builderName)
                    , StringUtil.toLowerCaseFirstOne(builderName)
                    , ClassName.get(brickElement.getPackageName(), builderName));
            initBuilder.addStatement("brickBuilderMap.put($S, $L)"
                    , type
                    , StringUtil.toLowerCaseFirstOne(builderName));
        }
        return initBuilder.build();
    }

    /**
     * 构造initBrickBinderMap方法
     * @return
     */
    private MethodSpec initBinderMethod() {
        //构造initBrickBuilderMap方法
        MethodSpec.Builder initBuilder = MethodSpec.methodBuilder("initBrickBinderMap")
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .addParameter(ParameterizedTypeName
                                .get(ClassName.get(Map.class)
                                        , TypeName.get(String.class)
                                        , ClassName.get(packageName, "AbstractBrickEventBinder"))
                        , "brickBinderMap");
        //先clear原始map
        initBuilder.addStatement("brickBinderMap.clear()");
        //将生成的binder类添加到map中
        for (Element element : eventHandlerElements) {
            String builderName = element.getSimpleName() + "EventBinder";
            initBuilder.addStatement("$T $L = new $T()"
                    , ClassName.get(element.getEnclosingElement().toString(), builderName)
                    , StringUtil.toLowerCaseFirstOne(builderName)
                    , ClassName.get(element.getEnclosingElement().toString(), builderName));
            initBuilder.addStatement("brickBinderMap.put($S, $L)"
                    , ((TypeElement) element).getQualifiedName()
                    , StringUtil.toLowerCaseFirstOne(builderName));
        }
        return initBuilder.build();
    }

    /**
     * 生成BrickInitializer类
     * @return
     */
    private TypeSpec initClass() {
        return TypeSpec.classBuilder("BrickInitializer")
                .addModifiers(Modifier.PUBLIC)
                .addMethod(initBuilderMethod())
                .addMethod(initBinderMethod())
                .build();
    }

    public JavaFile build() {
        return JavaFile.builder(initPackageName, initClass()).build();
    }

}
