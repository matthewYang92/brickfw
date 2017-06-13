package yang.brickfw;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import yang.brickfw.file.BuilderFileBuilder;
import yang.brickfw.file.EventBinderFileBuilder;
import yang.brickfw.file.HolderFileBuilder;
import yang.brickfw.file.InitializerFileBuilder;

/**
 * author: Matthew Yang on 17/6/7
 * e-mail: yangtian@yy.com
 */

@AutoService(Processor.class)
public class BrickProcessor extends AbstractProcessor {

    private Filer mFiler; //文件相关的辅助类
    private Elements mElementUtils; //元素相关的辅助类
    private Messager mMessager; //日志相关的辅助类
    private Types mTypes; //Type相关辅助类
    private String packageName = "yang.brickfw";

    private Map<String, BrickElement> brickElementMap = new HashMap<>();
    private Map<Element, BrickEventElement> brickEventElementMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
        mTypes = processingEnv.getTypeUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> holderElementSet = new HashSet<>();
        Set<? extends Element> viewElementSet = new HashSet<>();
        Set<? extends Element> dataMethodElementSet = new HashSet<>();
        Set<? extends Element> itemClickElementSet = new HashSet<>();
        Set<? extends Element> itemLongClickElementSet = new HashSet<>();
        Set<? extends Element> brickEventElementSet = new HashSet<>();
        Set<? extends Element> brickEventHandlerElementSet = new HashSet<>();
        for (TypeElement typeElement : annotations) {
            if (TypeName.get(typeElement.asType()).equals(TypeName.get(BrickView.class))) {
                viewElementSet = roundEnv.getElementsAnnotatedWith(typeElement);
                note("annotations viewElementSet : %s", viewElementSet);
            } else if (TypeName.get(typeElement.asType()).equals(TypeName.get(BrickHolder.class))) {
                holderElementSet = roundEnv.getElementsAnnotatedWith(typeElement);
                note("annotations holderElementSet : %s", holderElementSet);
            } else if (TypeName.get(typeElement.asType()).equals(TypeName.get(SetBrickData.class))) {
                dataMethodElementSet = roundEnv.getElementsAnnotatedWith(typeElement);
                note("annotations dataElementSet : %s", dataMethodElementSet);
            } else if (TypeName.get(typeElement.asType()).equals(TypeName.get(OnBrickItemClick.class))) {
                itemClickElementSet = roundEnv.getElementsAnnotatedWith(typeElement);
                note("annotations itemClickElementSet : %s", itemClickElementSet);
            } else if (TypeName.get(typeElement.asType()).equals(TypeName.get(OnBrickItemLongClick.class))) {
                itemLongClickElementSet = roundEnv.getElementsAnnotatedWith(typeElement);
                note("annotations itemLongClickElementSet : %s", itemClickElementSet);
            } else if (TypeName.get(typeElement.asType()).equals(TypeName.get(OnBrickEvent.class))) {
                brickEventElementSet = roundEnv.getElementsAnnotatedWith(typeElement);
                note("annotations brickEventElementSet : %s", brickEventElementSet);
            } else if (TypeName.get(typeElement.asType()).equals(TypeName.get(BrickEventHandler.class))) {
                brickEventHandlerElementSet = roundEnv.getElementsAnnotatedWith(typeElement);
                note("annotations brickEventHandlerElementSet : %s", brickEventHandlerElementSet);
            }
        }

        updateEventElementMap(viewElementSet, itemClickElementSet, itemLongClickElementSet, brickEventHandlerElementSet, brickEventElementSet);

        updateBrickElementMap(holderElementSet, viewElementSet, dataMethodElementSet);

        genHolderAndBuilderFiles();

        genEventFiles();

        if (roundEnv.processingOver()) {
            genInitializerFile();
        }

        return true;
    }

    private void updateEventElements(Set<? extends Element> viewElementSet
            , Set<? extends Element> eventElementSet
            , Class<? extends Annotation> eventAnnotation) {
        for (Element element : eventElementSet) {
            Element typeElement = element.getEnclosingElement();
            BrickEventElement eventElement = brickEventElementMap.get(typeElement);
            if (null == eventElement) {
                eventElement = new BrickEventElement();
                brickEventElementMap.put(typeElement, eventElement);
            }
            String value = getBrickAnnotationValue(element, eventAnnotation);
            Element viewElement = findViewElementByValue(value, viewElementSet);
            if (null != viewElement) {
                eventElement.addEventElement(viewElement, (ExecutableElement) element);
            } else {
                warn("inValidity %s has not BrickView support : %s", element, value);
            }
        }
    }

    private void updateBrickEventElements(Set<? extends Element> viewElementSet
            , Set<? extends Element> brickEventHandlerElementSet
            , Set<? extends Element> eventElementSet) {
        int i = 0;
        for (Element element : eventElementSet) {
            Element typeElement = element.getEnclosingElement();
            BrickEventElement eventElement = brickEventElementMap.get(typeElement);
            if (null == eventElement) {
                eventElement = new BrickEventElement();
                brickEventElementMap.put(typeElement, eventElement);
            }
            String value = getBrickAnnotationValue(element, OnBrickEvent.class);
            Element handlerElement = findHandlerElementByValue(value, brickEventHandlerElementSet);
            Element viewElement = findViewElementByValue(value, viewElementSet);
            if (null != handlerElement && null != viewElement) {
                i = eventElement.addBrickEventElement(viewElement, handlerElement, (ExecutableElement) element);
            } else {
                warn("inValidity %s has not IBrickHandler or BrickView support : %s", element, value);
            }
        }
        note("addBrickEventElement size:" + i);
    }

    private void updateEventElementMap(Set<? extends Element> viewElementSet
            , Set<? extends Element> itemClickElementSet
            , Set<? extends Element> itemLongClickElementSet
            , Set<? extends Element> brickEventHandlerElementSet
            , Set<? extends Element> brickEventElementSet) {
        updateEventElements(viewElementSet, itemClickElementSet, OnBrickItemClick.class);
        updateEventElements(viewElementSet, itemLongClickElementSet, OnBrickItemLongClick.class);
        updateBrickEventElements(viewElementSet, brickEventHandlerElementSet, brickEventElementSet);
        //去除无效的brickEventElement
        Iterator<Element> it = brickEventElementMap.keySet().iterator();
        while (it.hasNext()) {
            Element typeElement = it.next();
            BrickEventElement eventElement = brickEventElementMap.get(typeElement);
            if (!eventElement.isValidity()) {
                it.remove();
                warn("inValidity brickEventElement type class : %s has not event", typeElement);
            }
        }
    }

    private Element findViewElementByValue(String value, Set<? extends Element> viewElementSet) {
        for (Element element : viewElementSet) {
            String viewValue = element.getAnnotation(BrickView.class).value();
            if (value.equals(viewValue)) {
                return element;
            }
        }
        return null;
    }

    private Element findHandlerElementByValue(String value, Set<? extends Element> brickEventHandlerElementSet) {
        for (Element element : brickEventHandlerElementSet) {
            String handlerValue = element.getAnnotation(BrickEventHandler.class).value();
            if (value.equals(handlerValue)) {
                return element;
            }
        }
        return null;
    }

    private String getBrickAnnotationValue(Element element, Class<? extends Annotation> eventAnnotation) {
        String value = null;
        try {
            Annotation annotation = element.getAnnotation(eventAnnotation);
            value = (String) eventAnnotation.getMethod("value").invoke(annotation);
        } catch (IllegalAccessException e) {
            error(e.getMessage());
        } catch (InvocationTargetException e) {
            error(e.getMessage());
        } catch (NoSuchMethodException e) {
            error(e.getMessage());
        }
        return value;
    }

    private void updateBrickElementMap(Set<? extends Element> holderElementSet,
            Set<? extends Element> viewElementSet,
            Set<? extends Element> dataMethodElementSet) {
        for (Element viewElement : viewElementSet) {
            String viewValue = viewElement.getAnnotation(BrickView.class).value();
            BrickElement brickElement = brickElementMap.get(viewValue);
            if (brickElement == null) {
                brickElement = new BrickElement();
                brickElementMap.put(viewValue, brickElement);
            }
            brickElement.setViewElement(viewElement);
        }
        for (Element holderElement : holderElementSet) {
            String holderValue = holderElement.getAnnotation(BrickHolder.class).value();
            BrickElement brickElement = brickElementMap.get(holderValue);
            if (brickElement == null) {
                brickElement = new BrickElement();
                brickElementMap.put(holderValue, brickElement);
            }
            brickElement.setHolderElement(holderElement);
        }
        for (Element dataMethodElement : dataMethodElementSet) {
            String dataMethodValue = dataMethodElement.getAnnotation(SetBrickData.class).value();
            BrickElement brickElement = brickElementMap.get(dataMethodValue);
            if (brickElement == null) {
                brickElement = new BrickElement();
                brickElementMap.put(dataMethodValue, brickElement);
            }
            brickElement.setDataMethodElement((ExecutableElement) dataMethodElement);
        }
        //去除无效的brickElement
        Iterator<String> it = brickElementMap.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            BrickElement brickElement = brickElementMap.get(key);
            if (!brickElement.isValidity()) {
                it.remove();
                warn("inValidity brick value : %s has not BrickView", key);
            }
        }
    }

    private void genEventFiles() {
        for (Element typeElement : brickEventElementMap.keySet()) {
            genEventBinderFile(typeElement, brickEventElementMap.get(typeElement));
        }
    }

    private void genHolderAndBuilderFiles() {
        for (String brickType : brickElementMap.keySet()) {
            BrickElement brickElement = brickElementMap.get(brickType);
            switch (brickElement.getGenFileType()) {
                case BrickElement.GEN_BUILDER:
                    genBuilderFile(brickElement);
                    break;
                case BrickElement.GEN_HOLDER:
                    genHolderFile(brickElement, brickType);
                    break;
                default:
                    break;
            }
        }
    }

    private void genEventBinderFile(Element typeElement, BrickEventElement eventElement) {
        writeFile(new EventBinderFileBuilder(packageName, typeElement, eventElement).build());
    }

    private void genHolderFile(BrickElement brickElement, String brickType) {
        writeFile(new HolderFileBuilder(packageName, brickElement, brickType).build());
    }

    private void genBuilderFile(BrickElement brickElement) {
        writeFile(new BuilderFileBuilder(packageName, brickElement).build());
    }

    private void genInitializerFile() {
        writeFile(new InitializerFileBuilder(packageName, brickElementMap, brickEventElementMap.keySet()).build());
    }

    private void writeFile(JavaFile javaFile) {
        try {
            javaFile.writeTo(mFiler);
        } catch (IOException e) {
            note("Generate file failed, reason: %s", e.getMessage());
        }
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(BrickHolder.class.getCanonicalName());
        types.add(BrickView.class.getCanonicalName());
        types.add(SetBrickData.class.getCanonicalName());
        types.add(OnBrickItemClick.class.getCanonicalName());
        types.add(OnBrickItemLongClick.class.getCanonicalName());
        types.add(OnBrickEvent.class.getCanonicalName());
        types.add(BrickEventHandler.class.getCanonicalName());
        return types;
    }

    private void note(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, String.format(msg, args));
    }

    private void warn(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.WARNING, String.format(msg, args));
    }

    private void error(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }

}
