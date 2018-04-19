package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import tmall.bean.User;
import tmall.util.DBUtil;

public class UserDao 
{
	public int getTotal() 
	{
		int total = 0;
		
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) 
		{
			String sql = "SELECT count(*) from tmall.user";
			ResultSet rs = s.executeQuery(sql);
			while (rs.next()) {
				total = rs.getInt(1);
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return total;
	}
	
	public void add(User bean) 
	{	
		String sql = "INSERT INTO tmall.user VALUES (null, ?, ?)";
		
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);)
		{
			ps.setString(1, bean.getUsername());
			ps.setString(2, bean.getPassword());
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				bean.setId(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void update(User bean) 
	{
		String sql = "UPDATE tmall.user SET name = ?, password = ? WHERE id = ?";
		
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) 
		{
			ps.setString(1, bean.getUsername());
			ps.setString(2, bean.getPassword());
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
			String sql = "DELETE FROM tmall.user WHERE id =" + id;
			s.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public User get(int id)
	{
		User bean = null;
		
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) 
		{
			String sql = "SELECT * FROM tmall.user WHERE id =" + id;
			ResultSet rs = s.executeQuery(sql);
			
			if (rs.next()) {
				bean = new User();
				bean.setId(id);
				bean.setUsername(rs.getString("name"));
				bean.setPassword(rs.getString("password"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return bean;
	}
	
	public List<User> list(int start, int end) 
	{
		List<User> beans = new ArrayList<>();
		String sql = "SELECT * FROM tmall.user ORDER BY id LIMIT ?,?";
		
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, start);
			ps.setInt(2, end);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				User bean = new User();
				bean.setId(rs.getInt(1));
				bean.setUsername(rs.getString("name"));
				bean.setPassword(rs.getString("password"));
				beans.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return beans;
	}
	
	public List<User> list()
	{
		return list(0, Short.MAX_VALUE);
	}
	
	//ҵ�񷽷�������UserService
	
	public User get(String name) 
	{
		User bean = null;
		String sql = "SELECT * FROM tmall.user WHERE name = ?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				bean = new User();
				
				bean.setId(rs.getInt("id"));
				bean.setUsername(name);
				bean.setPassword(rs.getString("password"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	public boolean isExist(String name) {
		return get(name) != null;
	}
	
	public User get(String name, String password) 
	{
		User bean = null;
		String sql = "SELECT * FROM tmall.user WHERE name = ?, password = ?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setString(1, name);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				bean = new User();
				
				bean.setId(rs.getInt("id"));
				bean.setUsername(name);
				bean.setPassword(password);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	public static void main(String[] args) {
		System.out.println(new UserDao().get(2).getUsername());
	}
}
