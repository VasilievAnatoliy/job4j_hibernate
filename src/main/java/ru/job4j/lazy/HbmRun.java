package ru.job4j.lazy;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;
import java.util.List;

public class HbmRun {
    public static void main(String[] args) {
        List<CarBrand> list = new ArrayList<>();
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            CarBrand nissan = CarBrand.of("Nissan");
            CarBrand bmv = CarBrand.of("BMV");
            session.save(nissan);
            session.save(bmv);

            CarModel one = CarModel.of("X-Trail", nissan);
            CarModel two = CarModel.of("Qashqai", nissan);
            CarModel three = CarModel.of("Terrano", nissan);
            CarModel four = CarModel.of("x1", bmv);
            CarModel five = CarModel.of("x3", bmv);
            CarModel six = CarModel.of("x5", bmv);
            session.save(one);
            session.save(two);
            session.save(three);
            session.save(four);
            session.save(five);
            session.save(six);

            list = session.createQuery(
                    "select distinct s from CarBrand s join fetch s.models"
            ).list();

            /*
            list = session.createQuery("from CarBrand").list();
            for (CarBrand brand : list) {
                for (CarModel model : brand.getModels()) {
                    System.out.println(model);
                }
            }
            */

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
        for (CarBrand brand : list) {
            for (CarModel model : brand.getModels()) {
                System.out.println(model);
            }
        }
    }
}
