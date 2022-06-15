package ru.job4j.integration;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class OrdersStoreTest {
    private BasicDataSource pool = new BasicDataSource();

    @Before
    public void setUp() throws SQLException {
        pool.setDriverClassName("org.hsqldb.jdbcDriver");
        pool.setUrl("jdbc:hsqldb:mem:tests;sql.syntax_pgs=true");
        pool.setUsername("sa");
        pool.setPassword("");
        pool.setMaxTotal(2);
        StringBuilder builder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream("./db/scripts/update_001.sql")))
        ) {
            br.lines().forEach(line -> builder.append(line).append(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool.getConnection().prepareStatement(builder.toString()).executeUpdate();
    }

    @After
    public void dropTable() throws SQLException {
        pool.getConnection().prepareStatement("drop table orders").executeUpdate();
    }

    @Test
    public void whenSaveOrderAndFindAllOneRowWithDescription() {
        OrdersStore store = new OrdersStore(pool);
        store.save(Order.of("name1", "description1"));
        List<Order> all = (List<Order>) store.findAll();
        assertThat(all.size(), is(1));
        assertThat(all.get(0).getDescription(), is("description1"));
        assertThat(all.get(0).getId(), is(1));
    }

    @Test
    public void whenSaveOrderAndFindById() {
        OrdersStore store = new  OrdersStore(pool);
        Order order1 = Order.of("name1", "description1");
        Order order2 = Order.of("name2", "description2");
        Order order3 = Order.of("name3", "description3");
        store.save(order1);
        store.save(order2);
        order3 = store.save(order3);
        int id = order3.getId();
        assertThat(store.findById(id).getDescription(), is("description3"));
        assertThat(store.findById(id).getId(), is(3));
    }

    @Test
    public void whenSaveOrderAndFindByName() {
        OrdersStore store = new  OrdersStore(pool);
        Order order1 = Order.of("name1", "description1");
        Order order2 = Order.of("name2", "description2");
        Order order3 = Order.of("name3", "description3");
        store.save(order1);
        store.save(order2);
        order3 = store.save(order3);
        String name = order3.getName();
        assertThat(store.findByName(name).getDescription(), is("description3"));
        assertThat(store.findByName(name).getId(), is(3));
    }

    @Test
    public void whenSaveOrderAndUpdate() {
        OrdersStore store = new  OrdersStore(pool);
        Order order1 = Order.of("name1", "description1");
        Order order2 = Order.of("name2", "description2");
        order1 = store.save(order1);
        int id = order1.getId();
        assertTrue(store.update(order2, id));
        assertThat(store.findById(id).getDescription(), is("description2"));
        assertThat(store.findById(id).getId(), is(1));
    }
}