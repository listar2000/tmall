package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tmall.bean.Order;
import tmall.bean.User;
import tmall.util.DBUtil;
import tmall.util.DateUtil;

public class OrderDao 
{
	public static final String waitPay = "waitPay";
	public static final String waitDelivery = "waitDelivery";
	public static final String waitConfirm = "waitConfirm";
	public static final String waitReview = "waitReview";
	public static final String finish = "finish";
	public static final String delete = "delete";
	
	public int getTotal() 
	{
		int total = 0;
		
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) 
		{
			String sql = "SELECT count(*) from tmall.order_";
			ResultSet rs = s.executeQuery(sql);
			while (rs.next()) {
				total = rs.getInt(1);
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return total;
	}
	
	public void add(Order bean) 
	{	
		String sql = "INSERT INTO tmall.order_ VALUES (null,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);)
		{
			ps.setString(1, bean.getOrderCode());
            ps.setString(2, bean.getAddress());
            ps.setString(3, bean.getPost());
            ps.setString(4, bean.getReceiver());
            ps.setString(5, bean.getMobile());
            ps.setString(6, bean.getUserMessage());
             
            ps.setTimestamp(7,  DateUtil.d2t(bean.getCreateDate()));
            ps.setTimestamp(8,  DateUtil.d2t(bean.getPayDate()));
            ps.setTimestamp(9,  DateUtil.d2t(bean.getDeliveryDate()));
            ps.setTimestamp(10,  DateUtil.d2t(bean.getConfirmDate()));
            ps.setInt(11, bean.getUser().getId());
            ps.setString(12, bean.getStatus());
 
            ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				bean.setId(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void update(Order bean) 
	{
		String sql = "UPDATE order_ SET address= ?, post=?, "
				+ "receiver=?,mobile=?,userMessage=? ,createDate = ? , "
				+ "payDate =? , deliveryDate =?, confirmDate = ? , orderCode =?, "
				+ "uid=?, status=? WHERE id = ?";
		
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) 
		{
			ps.setString(1, bean.getAddress());
            ps.setString(2, bean.getPost());
            ps.setString(3, bean.getReceiver());
            ps.setString(4, bean.getMobile());
            ps.setString(5, bean.getUserMessage());
            ps.setTimestamp(6, DateUtil.d2t(bean.getCreateDate()));;
            ps.setTimestamp(7, DateUtil.d2t(bean.getPayDate()));;
            ps.setTimestamp(8, DateUtil.d2t(bean.getDeliveryDate()));;
            ps.setTimestamp(9, DateUtil.d2t(bean.getConfirmDate()));;
            ps.setString(10, bean.getOrderCode());
            ps.setInt(11, bean.getUser().getId());
            ps.setString(12, bean.getStatus());
            ps.setInt(13, bean.getId());
            ps.execute();
            
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void delete(int id) 
	{
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) 
		{
			String sql = "DELETE FROM tmall.order_ WHERE id =" + id;
			s.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Order get(int id)
	{
		Order bean = null;
		
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) 
		{
			String sql = "SELECT * FROM tmall.order_ WHERE id =" + id;
			ResultSet rs = s.executeQuery(sql);
			
			if (rs.next()) {
				bean = new Order();
				bean.setId(id);
				bean.setAddress(rs.getString("address"));
				bean.setConfirmDate(DateUtil.t2d(rs.getTimestamp("confirmDate")));
				bean.setCreateDate(DateUtil.t2d(rs.getTimestamp("createDate")));
				bean.setDeliveryDate(DateUtil.t2d(rs.getTimestamp("deliveryDate")));
				bean.setMobile(rs.getString("mobile"));
				bean.setOrderCode(rs.getString("orderCode"));
				bean.setPayDate(DateUtil.t2d(rs.getTimestamp("payDate")));
				bean.setPost(rs.getString("post"));
				bean.setReceiver(rs.getString("receiver"));
				bean.setStatus(rs.getString("status"));
				bean.setUserMessage(rs.getString("userMessage"));
				bean.setUser(new UserDao().get(rs.getInt("uid")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return bean;
	}
	
	public List<Order> list(int start, int end) 
	{
		List<Order> beans = new ArrayList<>();
		String sql = "SELECT * FROM tmall.order_ ORDER BY id LIMIT ?,?";
		
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);)
		{
			ps.setInt(1, start);
			ps.setInt(2, end);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) 
			{
				Order bean = new Order();
				bean.setId(rs.getInt(1));
				bean.setAddress(rs.getString("address"));
				bean.setConfirmDate(DateUtil.t2d(rs.getTimestamp("confirmDate")));
				bean.setCreateDate(DateUtil.t2d(rs.getTimestamp("createDate")));
				bean.setDeliveryDate(DateUtil.t2d(rs.getTimestamp("deliveryDate")));
				bean.setMobile(rs.getString("mobile"));
				bean.setOrderCode(rs.getString("orderCode"));
				bean.setPayDate(DateUtil.t2d(rs.getTimestamp("payDate")));
				bean.setPost(rs.getString("post"));
				bean.setReceiver(rs.getString("receiver"));
				bean.setStatus(rs.getString("status"));
				bean.setUserMessage(rs.getString("userMessage"));
				bean.setUser(new UserDao().get(rs.getInt("uid")));
				
				beans.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return beans;
	}
	
	public List<Order> list()
	{
		return list(0, Short.MAX_VALUE);
	}
	
	public List<Order> list(int uid, String excludedStatus) {
		return list(uid, excludedStatus, 0, Short.MAX_VALUE);
	}
	
	public List<Order> list(int uid, String excludedStatus, int start, int count) 
	{
        List<Order> beans = new ArrayList<Order>();
         
        String sql = "SELECT * from tmall.order_ where uid = ? and status != ? order by id limit ?,? ";
         
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
             
            ps.setInt(1, uid);
            ps.setString(2, excludedStatus);
            ps.setInt(3, start);
            ps.setInt(4, count);
             
            ResultSet rs = ps.executeQuery();
             
            while (rs.next()) {
                Order bean = new Order();
                String orderCode =rs.getString("orderCode");
                String address = rs.getString("address");
                String post = rs.getString("post");
                String receiver = rs.getString("receiver");
                String mobile = rs.getString("mobile");
                String userMessage = rs.getString("userMessage");
                String status = rs.getString("status");
                Date createDate = DateUtil.t2d( rs.getTimestamp("createDate"));
                Date payDate = DateUtil.t2d( rs.getTimestamp("payDate"));
                Date deliveryDate = DateUtil.t2d( rs.getTimestamp("deliveryDate"));
                Date confirmDate = DateUtil.t2d( rs.getTimestamp("confirmDate"));
                
                int id = rs.getInt("id");
                bean.setId(id);
                bean.setOrderCode(orderCode);
                bean.setAddress(address);
                bean.setPost(post);
                bean.setReceiver(receiver);
                bean.setMobile(mobile);
                bean.setUserMessage(userMessage);
                bean.setCreateDate(createDate);
                bean.setPayDate(payDate);
                bean.setDeliveryDate(deliveryDate);
                bean.setConfirmDate(confirmDate);
                User user = new UserDao().get(uid);
                bean.setStatus(status);
                bean.setUser(user);
                beans.add(bean);
            }
        } catch (SQLException e) {
             
            e.printStackTrace();
        }
        return beans;
    }
	
}
