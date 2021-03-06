package id.co.dropshipper.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.co.dropshipper.model.Lokasi;
@Service
public class LokasiDAO {
	@Autowired
	private EntityManagerFactory factory;
	public List<Lokasi> getAllLokasi(){
		return factory.createEntityManager()
				.createQuery("from Lokasi ")
				.getResultList();
	}
	public Lokasi getLokasiid(short id) {
		return (Lokasi) factory.createEntityManager()
				.createQuery("from Lokasi where lokasiid = " + id)
				.getSingleResult();
	}
	public boolean tambahlokasi(Lokasi objlokasi) {
		EntityManager eManager = factory.createEntityManager();
		EntityTransaction transaksi= null;
		boolean isSuccess = true;
		try {
			transaksi = eManager.getTransaction();
			
			transaksi.begin();
			objlokasi.setIsactive(1);
			eManager.persist(objlokasi);
			transaksi.commit();
		} catch (Exception e) {
			transaksi.rollback();
			isSuccess = false;
			
			System.out.println(e.getMessage());
		}
		return isSuccess;
	}
	public boolean updateLok(Lokasi updateloka) {
		EntityManager eManager = factory.createEntityManager();
		EntityTransaction transaksi= null;
		boolean isSuccess = true;
		try {
			transaksi = eManager.getTransaction();
			transaksi.begin();
			Lokasi exLokasi = (Lokasi) eManager.find(Lokasi.class, updateloka.getLokasiid());
			exLokasi.setAlamatlengkap(updateloka.getAlamatlengkap());
			exLokasi.setKodepos(updateloka.getKodepos());
			exLokasi.setWilayahid(updateloka.getWilayahid());
			exLokasi.setIsactive(updateloka.getIsactive());
			transaksi.commit();

		} catch (Exception e) {
			transaksi.rollback();
			isSuccess = false;
			System.out.println(e.getMessage());
		}
		return isSuccess;
	}
}
