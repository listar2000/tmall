package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import tmall.bean.Category;
import tmall.util.DBUtil;

public class CategoryDao 
{
	public int getTotal() 
	{
		int total = 0;
		
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) 
		{
			String sql = "SELECT count(*) from tmall.category";
			ResultSet rs = s.executeQuery(sql);
			while (rs.next()) {
				total = rs.getInt(1);
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return total;
	}
	
	public void add(Category bean) 
	{	
		String sql = "INSERT INTO tmall.category VALUES (null, ?)";
		
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);) 
		{
			ps.setString(1, bean.getName());
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				bean.setId(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void update(Category bean) 
	{
		String sql = "UPDATE tmall.category SET name = ? WHERE id = ?";
		
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) 
		{
			ps.setString(1, bean.getName());
			ps.setInt(2, bean.getId());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void delete(int id) 
	{
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) 
		{
			String sql = "DELETE FROM tmall.category WHERE id =" + id;
			s.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Category get(int id)
	{
		Category bean = null;
		
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) 
		{
			String sql = "SELECT * FROM tmall.category WHERE id =" + id;
			ResultSet rs = s.executeQuery(sql);
			
			if (rs.next()) {
				bean = new Category();
				bean.setId(id);
				bean.setName(rs.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return bean;
	}
	
	//下面的方法提供一个数据库的列表
	
	public List<Category> list(int start, int end) 
	{
		List<Category> beans = new ArrayList<>();
		String sql = "SELECT * FROM tmall.category ORDER BY id LIMIT ?,?";
		
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, start);
			ps.setInt(2, end);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				Category bean = new Category();
				bean.setId(rs.getInt(1));
				bean.setName(rs.getString(2));
				beans.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return beans;
	}
	
	public List<Category> list()
	{
		return list(0, Short.MAX_VALUE);
	}
	
}
