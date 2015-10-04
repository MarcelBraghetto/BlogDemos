package io.github.marcelbraghetto.redhotcompiler;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * Created by Marcel Braghetto on 21/09/15.
 */
@SupportedAnnotationTypes({ "io.github.marcelbraghetto.RedHotSetter" })
public class RedHotAnnotationProcessor extends AbstractProcessor {
  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    Messager messager = processingEnv.getMessager();

    for (TypeElement te : annotations) {
      for (Element e : roundEnv.getElementsAnnotatedWith(te)) {
        messager.printMessage(Diagnostic.Kind.NOTE, "Printing: " + e.toString());
      }
    }

    return true;
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latest();
  }
}