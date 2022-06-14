package ru.job4j.hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HbmRun {
    public static void main(String[] args) {
        Candidate rsl = null;
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry)
                    .buildMetadata()
                    .buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Vacancy vacancy1 = Vacancy.of("name1", "description1");
            Vacancy vacancy2 = Vacancy.of("name2", "description2");
            session.save(vacancy1);
            session.save(vacancy2);

            DbVacancies dbVacancies = DbVacancies.of("dbName");
            dbVacancies.addVacancy(vacancy1);
            dbVacancies.addVacancy(vacancy2);
            session.save(dbVacancies);

            Candidate one = Candidate.of("Alex", 2.5, 700, dbVacancies);
            session.save(one);



            rsl = session.createQuery(
                    "select distinct st from Candidate st "
                            + "join fetch st.dbVacancies a "
                            + "join fetch a.vacancies b "
                            + "where st.id = :sId", Candidate.class
            ).setParameter("sId", 12).uniqueResult();

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
        System.out.println(rsl);
    }
}
