package cs4347.hibernateProject.ecomm.services.impl;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cs4347.hibernateProject.ecomm.entity.Customer;
import cs4347.hibernateProject.ecomm.services.CustomerPersistenceService;
import cs4347.hibernateProject.ecomm.util.DAOException;

public class CustomerPersistenceServiceImpl implements CustomerPersistenceService
{
	@PersistenceContext 
	private EntityManager em; 
	
	public CustomerPersistenceServiceImpl(EntityManager em) {
		this.em = em;
	}
	
	/**
	 */
	@Override
	public void create(Customer customer) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();
			em.persist(customer);
			em.getTransaction().commit();
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public Customer retrieve(Long id) 
	{
		em.getTransaction().begin();
		Customer cust = em.find(Customer.class, id);
		em.getTransaction().commit();
		return cust;
		
	}

	@Override
	public void update(Customer c1) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();
			Customer c2 = em.find(Customer.class, c1.getId());
			c2.setAddress(c1.getAddress());
			c2.setCreditCard(c1.getCreditCard());
			c2.setDob(c1.getDob());
			c2.setEmail(c1.getEmail());
			c2.setFirstName(c1.getFirstName());
			c2.setLastName(c1.getLastName());
			c2.setGender(c1.getGender());
			
			
			em.getTransaction().commit();
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public void delete(Long id) throws SQLException, DAOException
	{
		em.getTransaction().begin();
		Customer cust = em.find(Customer.class, id);
		em.remove(cust);
		em.getTransaction().commit();
	}

	@Override
	public List<Customer> retrieveByZipCode(String zipCode) throws SQLException, DAOException
	{
		em.getTransaction().begin();
		List<Customer> custs = (List<Customer>)em.createQuery("from Customer as c where c.address.zipcode = :zipcode")
				.setParameter("zipcode", zipCode)
				.getResultList();
		em.getTransaction().commit();
		
		return custs;
	}

	@Override
	public List<Customer> retrieveByDOB(Date startDate, Date endDate) throws SQLException, DAOException
	{
		em.getTransaction().begin();
		List<Customer> custs = (List<Customer>)em.createQuery("from Customer as c where (c.dob between :startDate and :endDate)")
				.setParameter("startDate", startDate).setParameter("endDate", endDate)
				.getResultList();
		em.getTransaction().commit();
		return custs;
	}
}
