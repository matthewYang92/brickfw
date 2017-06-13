package yang.brickfw.file;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import javax.lang.model.element.Modifier;
import yang.brickfw.BrickElement;

/**
 * author: Matthew Yang on 17/6/8
 * e-mail: yangtian@yy.com
 */

public class BuilderFileBuilder {

    private BrickElement brickElement;
    private String packageName;

    public BuilderFileBuilder(String packageName, BrickElement brickElement) {
        this.packageName = packageName;
        this.brickElement = brickElement;
    }

    /**
     * 构造create方法
     * @return
     */
    private MethodSpec createMethod() {
        MethodSpec.Builder createBuilder = MethodSpec.methodBuilder("create")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(brickElement.getHolderTypeName())
                .addParameter(ClassName.get("android.content","Context"), "context")
                .addStatement("return new $T(new $T(context))",
                        brickElement.getHolderTypeName(),
                        brickElement.getViewTypeName());
        return createBuilder.build();
    }

    /**
     * 生成BrickBuilder类
     * @return
     */
    private TypeSpec builderClass() {
        TypeSpec builderClass = TypeSpec.classBuilder(brickElement.getViewSimpleName() + "Builder")
                .addModifiers(Modifier.PUBLIC)
                .addMethod(createMethod())
                .superclass(ClassName.get(packageName, "AbstractBrickBuilder"))
                .build();
        return builderClass;
    }

    public JavaFile build() {
        return JavaFile.builder(packageName, builderClass()).build();
    }
}
