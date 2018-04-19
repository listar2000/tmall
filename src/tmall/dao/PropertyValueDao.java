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
import tmall.bean.Property;
import tmall.bean.PropertyValue;
import tmall.util.DBUtil;

public class PropertyValueDao 
{
	public int getTotal() 
	{
		int total = 0;
		
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) 
		{
			String sql = "SELECT count(*) from tmall.propertyvalue";
			ResultSet rs = s.executeQuery(sql);
			while (rs.next()) {
				total = rs.getInt(1);
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return total;
	}
	
	public void add(PropertyValue bean) 
	{	
		String sql = "INSERT INTO tmall.propertyvalue VALUES (null, ?, ?, ?)";
		
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);)
		{
			ps.setInt(1, bean.getProduct().getId());
			ps.setInt(2, bean.getProperty().getId());
			ps.setString(3, bean.getValue());
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				bean.setId(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void update(PropertyValue bean) 
	{
		String sql = "UPDATE tmall.propertyvalue SET pid = ?, ptid = ?, value = ? WHERE id = ?";
		
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) 
		{
			ps.setInt(1, bean.getProduct().getId());
			ps.setInt(2, bean.getProperty().getId());
			ps.setString(3, bean.getValue());
			ps.setInt(4, bean.getId());
			
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void delete(int id) 
	{
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) 
		{
			String sql = "DELETE FROM tmall.propertyvalue WHERE id =" + id;
			s.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public PropertyValue get(int id)
	{
		PropertyValue bean = null;
		
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) 
		{
			String sql = "SELECT * FROM tmall.propertyvalue WHERE id =" + id;
			ResultSet rs = s.executeQuery(sql);
			
			if (rs.next()) {
				bean = new PropertyValue();
				bean.setId(id);
				bean.setProduct(new ProductDao().get(rs.getInt("pid")));
				bean.setProperty(new PropertyDao().get(rs.getInt("ptid")));
				bean.setValue(rs.getString("value"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return bean;
	}
	
	public List<PropertyValue> list() {
		return list(0, Short.MAX_VALUE);
	}
	
	public List<PropertyValue> list(int start, int end) 
	{
		List<PropertyValue> beans = new ArrayList<>();
		String sql = "SELECT * FROM tmall.propertyvalue ORDER BY id LIMIT ?, ?";
		
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) 
		{
		
			ps.setInt(1, start);
			ps.setInt(2, end);
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				PropertyValue bean = new PropertyValue();
				bean.setId(rs.getInt(1));
				bean.setProduct(new ProductDao().get(rs.getInt("pid")));
				bean.setProperty(new PropertyDao().get(rs.getInt("ptid")));
				bean.setValue(rs.getString("value"));
				beans.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}
	
	public List<PropertyValue> list(int pid) 
	{
		List<PropertyValue> beans = new ArrayList<>();
		String sql = "SELECT * FROM tmall.propertyvalue WHERE pid = ? ORDER BY ptid";
		
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) 
		{
		
			ps.setInt(1, pid);
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				PropertyValue bean = new PropertyValue();
				bean.setId(rs.getInt(1));
				bean.setProduct(new ProductDao().get(rs.getInt("pid")));
				bean.setProperty(new PropertyDao().get(rs.getInt("ptid")));
				bean.setValue(rs.getString("value"));
				beans.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}
	
	public PropertyValue get(int pid, int ptid) 
	{
		PropertyValue bean = null;
		
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) 
		{
			String sql = "SELECT * FROM tmall.propertyvalue WHERE pid =" + pid + " and ptid = " + ptid;
			ResultSet rs = s.executeQuery(sql);
			
			if (rs.next()) {
				bean = new PropertyValue();
				bean.setId(rs.getInt(1));
				bean.setProduct(new ProductDao().get(pid));
				bean.setProperty(new PropertyDao().get(ptid));
				bean.setValue(rs.getString("value"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return bean;
	}
	
	public void init(Product p)
	{
		List<Property> pts = new PropertyDao().list(p.getCategory().getId());
		
		for (Property pt: pts) {
			PropertyValue pv = get(p.getId(), pt.getId());
			if (pv == null) {
				pv = new PropertyValue();
				pv.setProduct(p);
				pv.setProperty(pt);
				this.add(pv);
			}
		}
	}
}
