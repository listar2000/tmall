package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tmall.bean.Category;
import tmall.bean.Product;
import tmall.bean.ProductImage;
import tmall.util.DBUtil;
import tmall.util.DateUtil;

public class ProductDao {
	
	public final int PRODUCT_PER_ROW = 8;

	public int getTotal(int cid) {
		int total = 0;
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {

			String sql = "select count(*) from Product where cid = " + cid;

			ResultSet rs = s.executeQuery(sql);
			while (rs.next()) {
				total = rs.getInt(1);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return total;
	}

	public void add(Product bean) {

		String sql = "insert into Product values(null,?,?,?,?,?,?,?)";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);)
		{
			ps.setString(1, bean.getName());
			ps.setString(2, bean.getSubTitle());
			ps.setFloat(3, bean.getOriginalPrice());
			ps.setFloat(4, bean.getPromotePrice());
			ps.setInt(5, bean.getStock());
			ps.setInt(6, bean.getCategory().getId());
			ps.setTimestamp(7, DateUtil.d2t(bean.getCreateDate()));
			ps.execute();

			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				int id = rs.getInt(1);
				bean.setId(id);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public void update(Product bean) {

		String sql = "update Product set name= ?, subTitle=?, orignalPrice=?,promotePrice=?,stock=?, cid = ?, createDate=? where id = ?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

			ps.setString(1, bean.getName());
			ps.setString(2, bean.getSubTitle());
			ps.setFloat(3, bean.getOriginalPrice());
			ps.setFloat(4, bean.getPromotePrice());
			ps.setInt(5, bean.getStock());
			ps.setInt(6, bean.getCategory().getId());
			ps.setTimestamp(7, DateUtil.d2t(bean.getCreateDate()));
			ps.setInt(8, bean.getId());
			ps.execute();

		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public void delete(int id) {

		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {

			String sql = "delete from Product where id = " + id;

			s.execute(sql);

		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public Product get(int id) {
		Product bean = new Product();

		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {

			String sql = "select * from Product where id = " + id;

			ResultSet rs = s.executeQuery(sql);

			if (rs.next()) {

				String name = rs.getString("name");
				String subTitle = rs.getString("subTitle");
				float orignalPrice = rs.getFloat("orignalPrice");
				float promotePrice = rs.getFloat("promotePrice");
				int stock = rs.getInt("stock");
				int cid = rs.getInt("cid");
				Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));

				bean.setName(name);
				bean.setSubTitle(subTitle);
				bean.setOriginalPrice(orignalPrice);
				bean.setPromotePrice(promotePrice);
				bean.setStock(stock);
				Category category = new CategoryDao().get(cid);
				bean.setCategory(category);
				bean.setCreateDate(createDate);
				bean.setId(id);
				setFirstProductImage(bean);
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return bean;
	}

	public List<Product> list(int cid, int start, int count) {
		List<Product> beans = new ArrayList<Product>();
		Category category = new CategoryDao().get(cid);
		String sql = "select * from Product where cid = ? order by id limit ?,? ";

		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
			ps.setInt(1, cid);
			ps.setInt(2, start);
			ps.setInt(3, count);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Product bean = new Product();
				int id = rs.getInt(1);
				String name = rs.getString("name");
				String subTitle = rs.getString("subTitle");
				float orignalPrice = rs.getFloat("orignalPrice");
				float promotePrice = rs.getFloat("promotePrice");
				int stock = rs.getInt("stock");
				Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));

				bean.setName(name);
				bean.setSubTitle(subTitle);
				bean.setOriginalPrice(orignalPrice);
				bean.setPromotePrice(promotePrice);
				bean.setStock(stock);
				bean.setCreateDate(createDate);
				bean.setId(id);
				bean.setCategory(category);
				setFirstProductImage(bean);
				beans.add(bean);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return beans;
	}

	public List<Product> list() {
		return list(0, Short.MAX_VALUE);
	}

	public List<Product> list(int cid) {
		return list(cid, 0, Short.MAX_VALUE);
	}

	public List<Product> list(int start, int count) {
		List<Product> beans = new ArrayList<Product>();

		String sql = "select * from Product limit ?,? ";

		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

			ps.setInt(1, start);
			ps.setInt(2, count);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Product bean = new Product();
				int id = rs.getInt(1);
				int cid = rs.getInt("cid");
				String name = rs.getString("name");
				String subTitle = rs.getString("subTitle");
				float orignalPrice = rs.getFloat("orignalPrice");
				float promotePrice = rs.getFloat("promotePrice");
				int stock = rs.getInt("stock");
				Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));

				bean.setName(name);
				bean.setSubTitle(subTitle);
				bean.setOriginalPrice(orignalPrice);
				bean.setPromotePrice(promotePrice);
				bean.setStock(stock);
				bean.setCreateDate(createDate);
				bean.setId(id);

				Category category = new CategoryDao().get(cid);
				bean.setCategory(category);
				beans.add(bean);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return beans;
	}

	public void fill(Category c) {
		List<Product> ps = this.list(c.getId());
		c.setProducts(ps);
	}

	public void fillByRow(List<Category> cs) 
	{
		for (Category c: cs) {
			List<Product> ps = c.getProducts();
			List<List<Product>> productByRows = new ArrayList<>();
			for (int i = 0; i < ps.size(); i += PRODUCT_PER_ROW) {
				int size = i + PRODUCT_PER_ROW;
				size = size > ps.size() ? ps.size() : size;
				List <Product> row = ps.subList(i, size);
				productByRows.add(row);
			}
			c.setProductsByRow(productByRows);
		}
	}
	
	public void setFirstProductImage(Product p) 
	{
		List<ProductImage> list = new ProductImageDao().list(p, ProductImageDao.TYPE_SINGLE);
		if (!list.isEmpty()) {
			p.setFirstProductImage(list.get(0));
		}
	}
	
	public void setSaleAndReviewNumber(Product p) 
	{
		int saleCount = new OrderItemDao().getSaleCount(p.getId());
		p.setSaleCount(saleCount);
		int reviewCount = new ReviewDao().getCount(p.getId());
		p.setReviewCount(reviewCount);
	}
	
	public void setSaleAndReviewNumber(List<Product> ps) {
		for (Product p: ps) setSaleAndReviewNumber(p);
	}
	
	public List<Product> search(String keyword, int start, int end) 
	{
		List<Product> beans = new ArrayList<>();
		if (null==keyword || keyword.trim().length()==0) return beans;
		String sql = "SELECT * FROM tmall.product WHERE name LIKE ? LIMIT ?, ?";
		
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) 
		{
			ps.setString(1, keyword);
			ps.setInt(2, start);
			ps.setInt(3, end);
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				Product bean = new Product();

                bean.setName(rs.getString("name"));
                bean.setSubTitle(rs.getString("subTitle"));
                bean.setOriginalPrice(rs.getFloat("orignalPrice"));
                bean.setPromotePrice(rs.getFloat("promotePrice"));
                bean.setStock( rs.getInt("stock"));
                bean.setCreateDate(DateUtil.t2d( rs.getTimestamp("createDate")));
                bean.setId(rs.getInt(1));
                bean.setCategory(new CategoryDao().get(rs.getInt("cid")));
                setFirstProductImage(bean);               
                beans.add(bean);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}
	
}
