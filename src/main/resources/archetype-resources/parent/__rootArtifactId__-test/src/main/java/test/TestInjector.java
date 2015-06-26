/*
 * Copyright 2015 Stefan Heimberg <kontakt@stefanheimberg.ch>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ${package}.test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 *
 * @author Stefan Heimberg <kontakt@stefanheimberg.ch>
 */
public class TestInjector {

    public static <A extends Annotation, T> void inject(final Object intoObject, final Class<A> annotationType, final Class<T> fieldType, final T object) {
        Class<?> mockClass = intoObject.getClass();
        while (mockClass != null) {
            for (Field field : mockClass.getDeclaredFields()) {
                boolean isAnnotationTypeIdentical = true;
                if(null != annotationType) {
                    final A annotation = field.getAnnotation(annotationType);
                    if(null != annotation) {
                        isAnnotationTypeIdentical = annotationType.isAssignableFrom(annotation.annotationType());
                    }
                }

                final boolean isFieldTypeIdentical = fieldType.isAssignableFrom(field.getType());
                
                if (isAnnotationTypeIdentical && isFieldTypeIdentical) {
                    field.setAccessible(true);
                    try {
                        field.set(intoObject, object);
                    } catch (final IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    return;
                }
            }
            mockClass = mockClass.getSuperclass();
        }
        // Could not find the annotated field.
        String message = "Could not find field of type '" + fieldType + "' ";
        if (annotationType != null) {
            message += "with annotation '@" + annotationType + "' ";
        }
        message += "for test object '" + intoObject.getClass().getSimpleName() + "'";
        throw new RuntimeException(message);
    }

    private TestInjector() {
    }

}
