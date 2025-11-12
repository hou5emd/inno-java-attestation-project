package ru.inno.attestation.attestation03.utils.processors;


import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import ru.inno.attestation.attestation03.utils.annotations.GenerateSortFields;
import ru.inno.attestation.attestation03.utils.interfaces.SortableField;


import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes("ru.inno.attestation.attestation03.utils.annotations.GenerateSortFields")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class GenerateSortFieldsProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(GenerateSortFields.class)) {
            if (!(element instanceof TypeElement typeElement)) continue;
            System.out.println("Processing " + typeElement.getSimpleName());
            String className = typeElement.getSimpleName() + "SortField";
            String packageName = processingEnv.getElementUtils()
                    .getPackageOf(typeElement).getQualifiedName().toString();

            TypeSpec.Builder enumBuilder = TypeSpec.enumBuilder(className)
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(SortableField.class)
                    .addField(String.class, "fieldName", Modifier.PRIVATE, Modifier.FINAL)
                    .addMethod(MethodSpec.constructorBuilder()
                            .addParameter(String.class, "fieldName")
                            .addStatement("this.fieldName = fieldName")
                            .build())
                    .addMethod(MethodSpec.methodBuilder("getFieldName")
                            .addAnnotation(Override.class)
                            .addModifiers(Modifier.PUBLIC)
                            .returns(String.class)
                            .addStatement("return this.fieldName")
                            .build());

            for (Element field : typeElement.getEnclosedElements()) {
                if (field.getKind() == ElementKind.FIELD) {
                    String name = field.getSimpleName().toString();
                    enumBuilder.addEnumConstant(name.toUpperCase(),
                            TypeSpec.anonymousClassBuilder("$S", name).build());
                }
            }

            JavaFile javaFile = JavaFile.builder(packageName, enumBuilder.build()).build();
            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }
}