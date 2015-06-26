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
package org.example.petstore.common;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Stefan Heimberg <kontakt@stefanheimberg.ch>
 */
public class ToStringBuilder {

    private final Object obj;
    private final Map<String, Object> attributes;

    public ToStringBuilder(Object obj) {
        this.obj = obj;
        this.attributes = new LinkedHashMap<>();
    }

    public ToStringBuilder add(String name, Object value) {
        attributes.put(name, value);
        return this;
    }

    public String build() {
        final StringBuilder sb = new StringBuilder();
        if (null != obj) {
            sb.append(obj.getClass().getSimpleName());
        }
        sb.append("{");
        final Iterator<Entry<String, Object>> it = attributes.entrySet().iterator();
        while (it.hasNext()) {
            final Entry<String, Object> entry = it.next();
            sb.append(MessageFormat.format("{0}={1}", entry.getKey(), entry.getValue()));
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }
    
}