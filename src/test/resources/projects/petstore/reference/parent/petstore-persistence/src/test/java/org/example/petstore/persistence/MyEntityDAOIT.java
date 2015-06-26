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
package org.example.petstore.persistence;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.example.petstore.test.TestInjector;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Stefan Heimberg <kontakt@stefanheimberg.ch>
 */
public class MyEntityDAOIT {
    
    private static final String PERSISTENCE_UNIT = "petstoreIT";
    private static final String TESTDATA = "data/testdata.xml";
    
    private static EntityManagerFactory emf;
    private static EntityManager em;
    private static IDatabaseConnection dbConnection;
    private static FlatXmlDataSet dataSet;

    @BeforeClass
    public static void beforeClass() throws DatabaseUnitException {
        if (null == emf) {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        }
        if (null == em) {
            em = emf.createEntityManager();
        }
        
        em.getTransaction().begin();
        final Connection jdbcConnection = em.unwrap(Connection.class);
        em.getTransaction().rollback();

        dbConnection = new DatabaseConnection(jdbcConnection);

        final InputStream is = MyEntityDAOIT.class.getClassLoader().getResourceAsStream(TESTDATA);
        final FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);
        dataSet = builder.build(is);
    }

    @AfterClass
    public static void afterClass() {
        if (null != em) {
            if (em.isOpen()) {
                em.close();
            }
            em = null;
        }
        if (null != emf) {
            if (emf.isOpen()) {
                emf.close();
            }
            emf = null;
        }
    }

    private EntityTransaction et;
    private final MyEntityDAO myEntityDAO = new MyEntityDAO();;

    @Before
    public void before() throws DatabaseUnitException, SQLException {
        TestInjector.inject(myEntityDAO, PersistenceContext.class, EntityManager.class, em);
        
        em.clear();
        emf.getCache().evictAll();

        DatabaseOperation.CLEAN_INSERT.execute(dbConnection, dataSet);
        
        et = em.getTransaction();
    }

    @After
    public void after() throws DatabaseUnitException, SQLException {
        if (null != et) {
            if (et.isActive()) {
                et.rollback();
            }
            et = null;
        }
        DatabaseOperation.DELETE_ALL.execute(dbConnection, dataSet);
    }

    @Test
    public void assert_findAll_returning_all_entities() {
        final List<MyEntity> result = myEntityDAO.findAll();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

}
