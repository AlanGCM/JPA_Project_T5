package cs4347.hibernateProject.ecomm.services.impl;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cs4347.hibernateProject.ecomm.entity.Product;
import cs4347.hibernateProject.ecomm.entity.Purchase;
import cs4347.hibernateProject.ecomm.services.PurchasePersistenceService;
import cs4347.hibernateProject.ecomm.services.PurchaseSummary;
import cs4347.hibernateProject.ecomm.util.DAOException;

public class PurchasePersistenceServiceImpl implements PurchasePersistenceService
{
	@PersistenceContext 
	private EntityManager em; 
	
	public PurchasePersistenceServiceImpl(EntityManager em) {
		this.em = em;
	}
	
	@Override
	public void create(Purchase purchase) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();
			em.persist(purchase);
			em.getTransaction().commit();
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public Purchase retrieve(Long id) throws SQLException, DAOException
	{
		em.getTransaction().begin();
		Purchase pur = em.find(Purchase.class, id);
		em.getTransaction().commit();
		return pur;
	}

	@Override
	public void update(Purchase purchase) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();
			Purchase p2 = em.find(Purchase.class, purchase.getId());
			p2.setCustomer(purchase.getCustomer());
			p2.setProduct(purchase.getProduct());
			p2.setPurchaseAmount(purchase.getPurchaseAmount());
			p2.setPurchaseDate(purchase.getPurchaseDate());
			
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
		Purchase pur= em.find(Purchase.class, id);
		em.remove(pur);;
		em.getTransaction().commit();
	}

	@Override
	public List<Purchase> retrieveForCustomerID(Long customerID) throws SQLException, DAOException
	{
		em.getTransaction().begin();
		List<Purchase> purs = (List<Purchase>)em.createQuery("from Purchase as p where p.customer.id = :cusID")
				.setParameter("cusID", customerID)
				.getResultList();
		em.getTransaction().commit();
		return purs;
	}

	@Override
	public PurchaseSummary retrievePurchaseSummary(Long customerID) throws SQLException, DAOException
	{
		PurchaseSummary purSum = new PurchaseSummary();
		em.getTransaction().begin();
		/*PurchaseSummary purSum = (PurchaseSummary)em.createQuery("SELECT MIN(purchaseAmount), AVG(purchaseAmount), MAX(purchaseAmount) from Purchase as p where p.customer.id = :cusID")
				.setParameter("cusID", customerID)
				.getSingleResult(); */
		purSum.minPurchase= (Double)em.createQuery("SELECT MIN(purchaseAmount) from Purchase as p where p.customer.id = :cusID")
				.setParameter("cusID", customerID)
				.getSingleResult();
		
		purSum.avgPurchase= (Double)em.createQuery("SELECT AVG(purchaseAmount) from Purchase as p where p.customer.id = :cusID")
				.setParameter("cusID", customerID)
				.getSingleResult();
		//purSum.avgPurchase = pur.getPurchaseAmount();
		
		purSum.maxPurchase= (Double)em.createQuery("SELECT MAX(purchaseAmount) from Purchase as p where p.customer.id = :cusID")
				.setParameter("cusID", customerID)
				.getSingleResult();
		//purSum.maxPurchase = pur.getPurchaseAmount();
		
		em.getTransaction().commit();
		return purSum;
	}

	@Override
	public List<Purchase> retrieveForProductID(Long productID) throws SQLException, DAOException
	{
		em.getTransaction().begin();
		List<Purchase> purs = (List<Purchase>)em.createQuery("from Purchase as p where p.product.id = :prodID")
				.setParameter("prodID", productID)
				.getResultList();
		em.getTransaction().commit();
		return purs;
	}
}
