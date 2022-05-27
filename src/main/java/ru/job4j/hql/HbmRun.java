package ru.job4j.hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

public class HbmRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Candidate one = Candidate.of("Alex", 2.5, 700);
            Candidate two = Candidate.of("Nikolay", 5, 1000);
            Candidate three = Candidate.of("Nikita", 0.5, 400);
            session.save(one);
            session.save(two);
            session.save(three);


            Query findById = session.createQuery("from Candidate s where s.id = :fId");
            findById.setParameter("fId", 1);
            System.out.println(findById.uniqueResult());


            Query findByName = session.createQuery("from Candidate s where s.name = :fName");
            findByName.setParameter("fName", "Nikita");
            System.out.println(findByName.uniqueResult());


            session.createQuery("update Candidate s set s.experience = :newExperience,"
                            + " s.salary = :newSalary where s.id = :fId")
                    .setParameter("newExperience", 0.8)
                    .setParameter("newSalary", 500)
                    .setParameter("fId", 3)
                    .executeUpdate();


            session.createQuery("delete from Candidate where id = :fId")
                    .setParameter("fId", 2)
                    .executeUpdate();

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
