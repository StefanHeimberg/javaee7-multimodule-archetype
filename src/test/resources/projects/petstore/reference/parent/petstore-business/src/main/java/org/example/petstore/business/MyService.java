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
package org.example.petstore.business;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.example.petstore.persistence.MyEntity;
import org.example.petstore.persistence.MyEntityDAO;

/**
 *
 * @author Stefan Heimberg <kontakt@stefanheimberg.ch>
 */
@Stateless
public class MyService {
    
    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private MyEntityDAO myEntityDAO;

    public List<MyEntity> findAll() {
        return myEntityDAO.findAll();
    }

    public MyEntity findById(final Long id) {
        return em.find(MyEntity.class, id);
    }
    
    public String getCalculatedMessage(final Long Id) {
        final MyEntity entity = findById(Id);
        
        final StringBuilder sb = new StringBuilder();
        sb.append("**");
        if(null != entity) {
            sb.append(entity.getText());
        }
        sb.append("**");
        
        return sb.toString();
    }
    
}
