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
package ${package}.persistence;

import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Stefan Heimberg <kontakt@stefanheimberg.ch>
 */
@Stateless
public class MyEntityRepository {
    
    private static final Logger LOG = LogManager.getLogger();
    
    @PersistenceContext
    private EntityManager em;
    
    public List<MyEntity> findAll() {
        LOG.debug("findAll called");
        
        final TypedQuery<MyEntity> q = em.createNamedQuery(MyEntity.FIND_ALL, MyEntity.class);
        final List<MyEntity> result = q.getResultList();
        
        LOG.debug("found {} items", result.size());
        
        return Collections.unmodifiableList(result);
    }
    
}
