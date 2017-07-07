package yang.brickfw;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import yang.brickfw.util.FP;

public class BrickEventElement {

    private TypeElement typeElement;
    private Map<Element, List<ExecutableElement>> viewEventElementMap = new HashMap<>();
    private Map<Map.Entry<Element, Element>, List<ExecutableElement>> brickEventElementMap = new HashMap<>();
    private String packageName;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

    public void setTypeElement(TypeElement typeElement) {
        this.typeElement = typeElement;
    }

    public Map<Element, List<ExecutableElement>> getViewEventElementMap() {
        return viewEventElementMap;
    }

    public void setViewEventElementMap(Map<Element, List<ExecutableElement>> viewEventElementMap) {
        this.viewEventElementMap = viewEventElementMap;
    }

    public Map<Map.Entry<Element, Element>, List<ExecutableElement>> getBrickEventElementMap() {
        return brickEventElementMap;
    }

    public void setBrickEventElementMap(
            Map<Map.Entry<Element, Element>, List<ExecutableElement>> brickEventElementMap) {
        this.brickEventElementMap = brickEventElementMap;
    }

    public void addEventElement(Element viewElement, ExecutableElement eventMethodElement) {
        List<ExecutableElement> eventMethodElements = viewEventElementMap.get(viewElement);
        if (FP.empty(eventMethodElements)) {
            eventMethodElements = new ArrayList<>();
            viewEventElementMap.put(viewElement, eventMethodElements);
        }
        eventMethodElements.add(eventMethodElement);
    }


    public int addBrickEventElement(Element viewElement, Element handlerElement, ExecutableElement eventMethodElement) {
        Map.Entry<Element, Element> entry = new AbstractMap.SimpleEntry<>(viewElement, handlerElement);
        List<ExecutableElement> eventMethodElements = brickEventElementMap.get(entry);
        if (FP.empty(eventMethodElements)) {
            eventMethodElements = new ArrayList<>();
            brickEventElementMap.put(entry, eventMethodElements);
        }
        eventMethodElements.add(eventMethodElement);
        return brickEventElementMap.size();
    }

    //viewEventElementMap为空时无效
    public boolean isValidity() {
        return !FP.empty(viewEventElementMap) || !FP.empty(brickEventElementMap);
    }

}