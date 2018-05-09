package yang.brickfw;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import yang.brickfw.util.StringUtil;

public class BrickElement {

    public static final int GEN_HOLDER = 1;
    public static final int GEN_BUILDER = 2;

    private String packageName;
    private Element holderElement;
    private Element viewElement;
    private ExecutableElement dataMethodElement;
    private ExecutableElement dataPayloadMethodElement;
    private ExecutableElement recycledMethodElement;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Element getHolderElement() {
        return holderElement;
    }

    public void setHolderElement(Element holderElement) {
        this.holderElement = holderElement;
    }

    public Element getViewElement() {
        return viewElement;
    }

    public void setViewElement(Element viewElement) {
        this.viewElement = viewElement;
    }

    public ExecutableElement getDataMethodElement() {
        return dataMethodElement;
    }

    public void setDataMethodElement(ExecutableElement dataMethodElement) {
        this.dataMethodElement = dataMethodElement;
    }

    public void setRecycledElement(ExecutableElement methodElement) {
        this.recycledMethodElement = methodElement;
    }

    public void setDataPayloadMethodElement(ExecutableElement dataPayloadMethodElement) {
        this.dataPayloadMethodElement = dataPayloadMethodElement;
    }

    public ExecutableElement getDataPayloadMethodElement() {
        return dataPayloadMethodElement;
    }

    public ExecutableElement getRecycledMethodElement() {
        return recycledMethodElement;
    }

    //view不为空的情况下 holder或dataMethod不为空
    public boolean isValidity() {
        return null != viewElement;
    }

    public int getGenFileType() {
        if (null != holderElement) {
            return GEN_BUILDER;
        }
        return GEN_HOLDER;
    }

    public Name getViewSimpleName() {
        return getViewElement().getSimpleName();
    }

    public String getViewParamName() {
        return StringUtil.toLowerCaseFirstOne(getViewSimpleName().toString());
    }

    public TypeName getViewTypeName() {
        return ClassName.get(getViewElement().asType());
    }

    public Name getHolderSimpleName() {
        return getHolderElement().getSimpleName();
    }

    public String getHolderParamName() {
        return StringUtil.toLowerCaseFirstOne(getHolderSimpleName().toString());
    }

    public TypeName getHolderTypeName() {
        return ClassName.get(getHolderElement().asType());
    }

    public Name getDataMethodSimpleName() {
        return getDataMethodElement().getSimpleName();
    }

    public VariableElement getDataMethodParameter(int index) throws IndexOutOfBoundsException{
        return getDataMethodElement().getParameters().get(index);
    }

    public Name getDataPayloadMethodSimpleName() {
        return getDataPayloadMethodElement().getSimpleName();
    }

    public VariableElement getDataPayloadMethodParameter(int index) throws IndexOutOfBoundsException{
        return getDataPayloadMethodElement().getParameters().get(index);
    }

    public Name getRecycledMethodSimpleName() {
        return getRecycledMethodElement().getSimpleName();
    }

    public VariableElement getRecycledMethodParameter(int index) throws IndexOutOfBoundsException{
        return getRecycledMethodElement().getParameters().get(index);
    }
}