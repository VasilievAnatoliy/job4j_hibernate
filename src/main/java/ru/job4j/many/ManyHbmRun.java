package ru.job4j.many;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class ManyHbmRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Model one = Model.of("X-Trail");
            Model two = Model.of("Qashqai");
            Model three = Model.of("Terrano");
            Model four = Model.of("Murano");
            Model five = Model.of("Pathfinder");
            session.save(one);
            session.save(two);
            session.save(three);
            session.save(four);
            session.save(five);

            Brand nissan = Brand.of("Nissan");
            nissan.addModel(session.load(Model.class, 1));
            nissan.addModel(session.load(Model.class, 2));
            nissan.addModel(session.load(Model.class, 3));
            nissan.addModel(session.load(Model.class, 4));
            nissan.addModel(session.load(Model.class, 5));

            session.save(nissan);

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
