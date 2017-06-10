package yang.brickfw.File;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import yang.brickfw.BrickElement;
import yang.brickfw.BrickHolder;

/**
 * author: Matthew Yang on 17/6/8
 * e-mail: yangtian@yy.com
 */

public class HolderFileBuilder {

    private BrickElement brickElement;
    private String packageName;
    private String brickType;

    public HolderFileBuilder(String packageName, BrickElement brickElement, String brickType) {
        this.packageName = packageName;
        this.brickElement = brickElement;
        this.brickType = brickType;
    }

    /**
     * 构造BrickHolder注解
     * @return
     */
    private AnnotationSpec holderAnnotation() {
        return AnnotationSpec.builder(BrickHolder.class)
                .addMember("value", "$S", brickType)
                .build();
    }

    /**
     * 构造itemView成员变量
     * @return
     */
    private FieldSpec viewField() {
        FieldSpec.Builder viewBuilder = FieldSpec
                .builder(brickElement.getViewTypeName(), brickElement.getViewParamName())
                .addModifiers(Modifier.PRIVATE);
        return viewBuilder.build();
    }

    /**
     * 构造方法
     * @return
     */
    private MethodSpec constructorMethod() {
        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(brickElement.getViewTypeName(), "view")
                .addStatement("super(view)")
                .addStatement("this.$L = view", brickElement.getViewParamName());
        return constructorBuilder.build();
    }

    /**
     * 构造setBrickData方法
     * @return
     */
    private MethodSpec setBrickInfoMethod() {
        TypeName extraType = ClassName.get(brickElement.getDataMethodParameter(0).asType());
        MethodSpec.Builder setBrickBuilder = MethodSpec.methodBuilder("setBrickInfo")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.get(packageName, "BrickInfo"), "info")
                .addStatement("$T data = ($T) info.getExtra()", extraType, extraType)
                .addStatement("$L.$N(data)", brickElement.getViewParamName(), brickElement.getDataMethodSimpleName());
        return setBrickBuilder.build();
    }

    /**
     * 生成BrickBuilder类
     * @return
     */
    private TypeSpec builderClass() {
        Element viewElement = brickElement.getViewElement();
        return TypeSpec.classBuilder(viewElement.getSimpleName() + "Holder")
                .addAnnotation(holderAnnotation())
                .addModifiers(Modifier.PUBLIC)
                .addField(viewField())
                .addMethod(constructorMethod())
                .addMethod(setBrickInfoMethod())
                .superclass(ClassName.get(packageName, "AbstractBrickHolder"))
                .build();
    }

    public JavaFile build() {
        return JavaFile.builder(packageName, builderClass()).build();
    }
}
