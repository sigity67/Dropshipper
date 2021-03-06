package id.co.dropshipper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import id.co.dropshipper.model.Barang;
import id.co.dropshipper.model.Kategori;

@RestController
public class SearchAPI {

	private static final Logger log = LoggerFactory.getLogger(SearchAPI.class);
	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@GetMapping("/tambahkeranjang/{nama}/{sku}/{jumlah}")
	public void setTambahKeranjang(HttpServletResponse response,@PathVariable("nama") String nama, @PathVariable("sku") String sku, @PathVariable("jumlah") String jumlah) throws IOException {
		//stringRedisTemplate.opsForList().leftPush("keranjang", sku);
		//stringRedisTemplate.opsForZSet().add("keranjang", sku, 1.00d);
		//double x = (double) jumlah;
		//stringRedisTemplate.opsForZSet().incrementScore("keranjang-" + nama, sku, x);
		stringRedisTemplate.opsForHash().put("keranjang-" + nama, sku, jumlah);
		response.sendRedirect("/user/barang");
	}
	
	@GetMapping("/hapuskeranjang/{nama}/{sku}")
	public void setHapusKeranjang(HttpServletResponse response,@PathVariable("nama") String nama, @PathVariable("sku") String sku) throws IOException {
		stringRedisTemplate.opsForHash().delete("keranjang-" + nama, sku);
		response.sendRedirect("/user/transaksi");
	}
	
	@GetMapping("/munculkan/{nama}/{sku}")
	public Object getKeranjang(@PathVariable("nama") String nama, @PathVariable("sku") String sku){
		String key = "keranjang-" + nama;
		return stringRedisTemplate.opsForHash().get(key, sku);
	}
	
	@GetMapping("/munculkan/{nama}")
	public List<Object> getKeranjang2(@PathVariable("nama") String nama){
		//String key = "keranjang-" + nama;
		//Set<Object> key = stringRedisTemplate.opsForHash().keys("keranjang-" + nama);
		String value="";
		Map<Object, Object> jumlah = stringRedisTemplate.opsForHash().entries("keranjang-" + nama);
//		List<String> valueBarang = new ArrayList<String>();
		for(Object valueJumlah : jumlah.keySet()) {
			
//            valueBarang.add(jumlah.get(valueJumlah).toString());
		}
		List<Object> key = stringRedisTemplate.opsForHash().values("keranjang-" + nama);
		System.out.println(jumlah);
		return key;
	}
	
	@GetMapping ("/kategoriname")
	public List<String> getKategoriname() {
		StringBuilder builder ;
		
		List<String> hasil = new ArrayList<>();
		
		List<Kategori> listkategori = 
				entityManagerFactory
				.createEntityManager()
				.createQuery("from Kategori")
				.getResultList();
		
		for(Kategori kategori: listkategori) {
			builder = new StringBuilder();
			builder.append(kategori.getKategoriname());
			hasil.add(builder.toString());
		}
			
		return hasil;
	}
	@GetMapping ("/kategoriid")
	public List<String> getKategoriid() {
		StringBuilder builder ;
		
		List<String> hasil = new ArrayList<>();
		
		List<Kategori> listkategori = 
				entityManagerFactory
				.createEntityManager()
				.createQuery("from Kategori")
				.getResultList();
		
		for(Kategori kategori: listkategori) {
			builder = new StringBuilder();
			builder.append(kategori.getKategoriid());
			hasil.add(builder.toString());
		}
			
		return hasil;
	}
}
