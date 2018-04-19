package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import tmall.bean.Review;
import tmall.util.DBUtil;
import tmall.util.DateUtil;

public class ReviewDao 
{
	public int getTotal() 
	{
		int total = 0;
		
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) 
		{
			String sql = "SELECT count(*) from tmall.review";
			ResultSet rs = s.executeQuery(sql);
			while (rs.next()) {
				total = rs.getInt(1);
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return total;
	}
	
	public void add(Review bean) 
	{	
		String sql = "INSERT INTO tmall.review VALUES (null, ?, ?, ?, ?)";
		
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);)
		{
			ps.setString(1, bean.getContent());
			ps.setInt(2, bean.getUser().getId());
			ps.setInt(3, bean.getProduct().getId());
			ps.setTimestamp(4, DateUtil.d2t(bean.getCreateDate()));
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				bean.setId(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void update(Review bean) 
	{
		String sql = "UPDATE tmall.review SET content = ?, uid = ?, pid = ?, createDate = ? WHERE id = ?";
		
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) 
		{
			ps.setString(1, bean.getContent());
			ps.setInt(2, bean.getUser().getId());
			ps.setInt(3, bean.getProduct().getId());
			ps.setTimestamp(4, DateUtil.d2t(bean.getCreateDate()));
			ps.setInt(5, bean.getId());
			
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void delete(int id) 
	{
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) 
		{
			String sql = "DELETE FROM tmall.review WHERE id =" + id;
			s.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Review get(int id)
	{
		Review bean = null;
		
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) 
		{
			String sql = "SELECT * FROM tmall.review WHERE id =" + id;
			ResultSet rs = s.executeQuery(sql);
			
			if (rs.next()) {
				bean = new Review();
				bean.setId(id);
				bean.setUser(new UserDao().get(rs.getInt("uid")));
				bean.setProduct(new ProductDao().get(rs.getInt("pid")));
				bean.setContent(rs.getString("content"));
				bean.setCreateDate(DateUtil.t2d(rs.getTimestamp("createDate")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return bean;
	}
	
	public List<Review> list(int pid, int start, int end)
	{
		List<Review> beans = new ArrayList<>();
		String sql = "SELECT * FROM tmall.review WHERE pid = ? ORDER BY id LIMIT ?, ?";
		
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) 
		{
		
			ps.setInt(1, pid);
			ps.setInt(2, start);
			ps.setInt(3, end);
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				Review bean = new Review();
				bean.setId(rs.getInt(1));
				bean.setProduct(new ProductDao().get(pid));
				bean.setUser(new UserDao().get(rs.getInt("uid")));
				bean.setContent(rs.getString("content"));
				bean.setCreateDate(DateUtil.t2d(rs.getTimestamp("createDate")));
				beans.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}
	
	public List<Review> list(int pid) {
		return list(pid, 0, Short.MAX_VALUE);
	}
	
	public int getCount(int pid) {
		int total = 0;
		
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) 
		{
			String sql = "SELECT count(*) from tmall.review WHERE pid = "+ pid;
			ResultSet rs = s.executeQuery(sql);
			while (rs.next()) {
				total = rs.getInt(1);
				return total;
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public boolean isExist(String content, int pid) 
	{
		String sql = "SELECT * FROM tmall.review WHERE content = ?, pid = ?";
		
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) 
		{
			
			ps.setString(1, content);
			ps.setInt(2, pid);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
