package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import tmall.bean.Property;
import tmall.util.DBUtil;

public class PropertyDao 
{
	public int getTotal(int cid) 
	{
		int total = 0;
		String sql = "SELECT count(*) WHERE cid = ? FROM tmall.property";
		
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) 
		{		
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				total = rs.getInt(1);
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return total;
	}
	
	public int getTotal() 
	{
		int total = 0;
		
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) 
		{
			String sql = "SELECT count(*) from tmall.property";
			ResultSet rs = s.executeQuery(sql);
			while (rs.next()) {
				total = rs.getInt(1);
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return total;
	}
	
	public void add(Property bean) 
	{	
		String sql = "INSERT INTO tmall.property VALUES (null, ?, ?)";
		
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);)
		{
			ps.setInt(1, bean.getCatagory().getId());
			ps.setString(2, bean.getName());
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				bean.setId(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void update(Property bean) 
	{
		String sql = "UPDATE tmall.property SET cid = ?, name = ? WHERE id = ?";
		
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) 
		{
			ps.setInt(1, bean.getCatagory().getId());
			ps.setString(2, bean.getName());
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
			String sql = "DELETE FROM tmall.property WHERE id =" + id;
			s.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Property get(int id)
	{
		Property bean = null;
		
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) 
		{
			String sql = "SELECT * FROM tmall.property WHERE id =" + id;
			ResultSet rs = s.executeQuery(sql);
			
			if (rs.next()) {
				bean = new Property();
				bean.setId(id);
				bean.setName(rs.getString("name"));
				bean.setCatagory(new CategoryDao().get(rs.getInt("cid")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return bean;
	}
	
	public List<Property> list(int cid, int start, int end) 
	{
		List<Property> beans = new ArrayList<>();
		String sql = "SELECT * FROM tmall.property WHERE cid = ? ORDER BY id LIMIT ?,?";
		
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, cid);
			ps.setInt(2, start);
			ps.setInt(3, end);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				Property bean = new Property();
				bean.setId(rs.getInt("id"));
				bean.setName(rs.getString("name"));
				bean.setCatagory(new CategoryDao().get(cid));
				
				beans.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return beans;
	}
	
	public List<Property> list(int cid)
	{
		return list(cid, 0, Short.MAX_VALUE);
	}
	
	public static void main(String[] args) {
		new PropertyDao().get(1);
	}
}
