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

import javax.persistence.EntityManager;
import org.example.petstore.persistence.MyEntity;
import org.example.petstore.persistence.MyEntityDAO;
import static org.junit.Assert.assertEquals;
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
    private MyEntityDAO myEntityDAO;
    
    @Mock
    private EntityManager em;
    
    @Before
    public void initMocks() {
        final MyEntity entity99 = new MyEntity("hallo");
        when(em.find(eq(MyEntity.class), eq(99l))).thenReturn(entity99);
    }
    
    @Test
    public void test() {
        assertEquals("**hallo**", myService.getCalculatedMessage(99l));
        assertEquals("****", myService.getCalculatedMessage(88l));
    }
    
}
