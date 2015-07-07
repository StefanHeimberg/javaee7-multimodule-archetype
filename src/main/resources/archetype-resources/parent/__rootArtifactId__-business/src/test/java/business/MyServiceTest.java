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
package ${package}.business;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import ${package}.persistence.MyEntity;
import ${package}.persistence.MyEntityRepository;
import ${package}.test.TestInjector;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author Stefan Heimberg <kontakt@stefanheimberg.ch>
 */
@RunWith(MockitoJUnitRunner.class)
public class MyServiceTest {
    
    @InjectMocks
    private MyService myService;
    
    @Mock
    private MyEntityRepository myEntityRepository;
    
    @Mock
    private EntityManager em;
    
    @Before
    public void initMocks() {
        final MyEntity entity77 = new MyEntity("hallo 77");
        TestInjector.inject(entity77, Id.class, Long.class, 77l);
        
        final MyEntity entity88 = new MyEntity("hallo 88");
        TestInjector.inject(entity88, Id.class, Long.class, 88l);
        
        final MyEntity entity99 = new MyEntity("hallo 99");
        TestInjector.inject(entity99, Id.class, Long.class, 99l);
        
        when(em.find(eq(MyEntity.class), eq(77l))).thenReturn(entity77);
        when(em.find(eq(MyEntity.class), eq(88l))).thenReturn(entity88);
        when(em.find(eq(MyEntity.class), eq(99l))).thenReturn(entity99);
        
        final List<MyEntity> entities = new ArrayList<>();
        entities.add(entity77);
        entities.add(entity88);
        entities.add(entity99);
        
        when(myEntityRepository.findAll()).thenReturn(entities);
    }
    
    @Test
    public void test_getCalculatedMessage() {
        assertEquals("****", myService.getCalculatedMessage(66l));
        assertEquals("**hallo 77**", myService.getCalculatedMessage(77l));
        assertEquals("**hallo 88**", myService.getCalculatedMessage(88l));
        assertEquals("**hallo 99**", myService.getCalculatedMessage(99l));
    }
    
    @Test
    public void test_findAll() {
        final List<MyEntity> result = myService.findAll();
        assertNotNull(result);
        assertEquals(3, result.size());
        
        assertFalse("66", containsEntityWithId(result, 66l));
        assertTrue("77", containsEntityWithId(result, 77l));
        assertTrue("88", containsEntityWithId(result, 88l));
        assertTrue("99", containsEntityWithId(result, 99l));
    }
    
    @Test
    public void test_findById() {
        assertNull(myService.findById(66l));
        
        final MyEntity entity77 = myService.findById(77l);
        assertNotNull(entity77);
        assertEquals(Long.valueOf(77l), entity77.getId());
        assertEquals("hallo 77", entity77.getText());
        
        final MyEntity entity88 = myService.findById(88l);
        assertNotNull(entity88);
        assertEquals(Long.valueOf(88l), entity88.getId());
        assertEquals("hallo 88", entity88.getText());
        
        final MyEntity entity99 = myService.findById(99l);
        assertNotNull(entity99);
        assertEquals(Long.valueOf(99l), entity99.getId());
        assertEquals("hallo 99", entity99.getText());
    }
    
    private boolean containsEntityWithId(List<MyEntity> entities, final Long id) {
        boolean found = false;
        for(MyEntity entity : entities) {
            if(id.equals(entity.getId())) {
                found = true;
                break;
            }
        }
        return found;
    }
    
}
