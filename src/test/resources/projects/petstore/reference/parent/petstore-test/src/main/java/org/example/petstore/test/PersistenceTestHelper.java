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
package org.example.petstore.test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
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

/**
 *
 * @author Stefan Heimberg <kontakt@stefanheimberg.ch>
 */
public final class PersistenceTestHelper {

    public static final String DEFAULT_PERSISTENCE_UNIT = "petstoreIT";
    public static final String DEFAULT_TESTDATA = "data/testdata.xml";

    private EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction et;
    private Connection connection;
    private IDatabaseConnection dbConnection;
    private FlatXmlDataSet dataSet;

    public void initEntityManagerFactory(String persistenceUnitName) {
        if (null == persistenceUnitName) {
            persistenceUnitName = DEFAULT_PERSISTENCE_UNIT;
        }
        emf = Persistence.createEntityManagerFactory(persistenceUnitName);
    }

    public void initEntityManager(final String persistenceUnitName) {
        if (null == emf) {
            initEntityManagerFactory(persistenceUnitName);
        }
        if (null == em) {
            em = emf.createEntityManager();
        }
    }

    public void initJDBC(final String persistenceUnitName) {
        if (null == em) {
            initEntityManager(persistenceUnitName);
        }
        if (null == connection) {
            em.getTransaction().begin();
            connection = em.unwrap(Connection.class);
            em.getTransaction().rollback();
        }
    }

    public void initDBUnit(final String persistenceUnitName, final String datasetXml) throws DatabaseUnitException {
        if (null == connection) {
            initJDBC(persistenceUnitName);
        }
        if (null == dbConnection) {
            dbConnection = new DatabaseConnection(connection);
        }
        if (null == dataSet) {
            final InputStream is = getClass().getClassLoader().getResourceAsStream(datasetXml);
            final FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
            builder.setColumnSensing(true);
            dataSet = builder.build(is);
        }
    }

    public void injectEntityManager(final Object injectionTarget) {
        TestInjector.inject(injectionTarget, PersistenceContext.class, EntityManager.class, em);
    }

    public void executeDBUnitOperation(DatabaseOperation operation) throws DatabaseUnitException, SQLException {
        if (null != operation) {
            operation.execute(dbConnection, dataSet);
        }
    }

    public void clearAllEntityManagerCaches() {
        if (null != em) {
            em.clear();
        }
        if (null != emf) {
            emf.getCache().evictAll();
        }
    }

}
