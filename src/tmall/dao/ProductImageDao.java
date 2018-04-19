package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import tmall.bean.Product;
import tmall.bean.ProductImage;
import tmall.util.DBUtil;

public class ProductImageDao 
{
	public static final String TYPE_SINGLE = "type_single";
	public static final String TYPE_DETAIL = "type_detail";
	
	public int getTotal() 
	{
		int total = 0;
		
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) 
		{
			String sql = "SELECT count(*) from tmall.productimage";
			ResultSet rs = s.executeQuery(sql);
			while (rs.next()) {
				total = rs.getInt(1);
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return total;
	}
	
	public void add(ProductImage bean) 
	{	
		String sql = "INSERT INTO tmall.productimage VALUES (null, ?, ?)";
		
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);) 
		{
			ps.setInt(1, bean.getProduct().getId());
			ps.setString(2, bean.getType());
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				bean.setId(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void update(ProductImage bean) 
	{
		String sql = "UPDATE tmall.productimage SET cid = ?, type = ? WHERE id = ?";
		
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) 
		{
			ps.setInt(1, bean.getProduct().getId());
			ps.setString(2, bean.getType());
			ps.setInt(3, bean.getId());
			
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void delete(int id) 
	{
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) 
		{
			String sql = "DELETE FROM tmall.productimage WHERE id =" + id;
			s.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ProductImage get(int id)
	{
		ProductImage bean = null;
		
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) 
		{
			String sql = "SELECT * FROM tmall.productimage WHERE id =" + id;
			ResultSet rs = s.executeQuery(sql);
			
			if (rs.next()) {
				bean = new ProductImage();
				bean.setId(id);
				bean.setProduct(new ProductDao().get(rs.getInt("pid")));
				bean.setType(rs.getString("type"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return bean;
	}
	
	public List<ProductImage> list(Product p, String type) {
		return list(p, type, 0, Short.MAX_VALUE);
	}
	
	public List<ProductImage> list(Product p, String type, int start, int end) 
	{
		List<ProductImage> beans = new ArrayList<>();
		String sql = "SELECT * FROM tmall.productimage WHERE pid = ? and type = ? ORDER BY id LIMIT ?, ?";
		
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) 
		{
			ps.setInt(1, p.getId());
			ps.setString(2, type);
			ps.setInt(3, start);
			ps.setInt(4, end);
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				ProductImage bean = new ProductImage();
				bean.setId(rs.getInt(1));
				bean.setProduct(p);
				bean.setType(type);
				beans.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}
}
