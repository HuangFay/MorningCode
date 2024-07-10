package com.tabletype.model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;





public class HibernateTest {
	public static void main(String[] args) {
	 	StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
	        	.configure()
	        	.build();
	
	SessionFactory factory = new MetadataSources()
			.buildMetadata(registry)
			.buildSessionFactory();
	Session session = factory.openSession();
	Transaction tx = session.beginTransaction();

	TableTypeVO dept = new TableTypeVO();
	dept.setTableType(60);
	dept.setTableTypeNumber(20);
	session.save(dept);
	
	
	
tx.commit();
	session.close();
	factory.close();
	}
}
