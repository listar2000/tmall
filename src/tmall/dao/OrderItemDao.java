package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import tmall.bean.Order;
import tmall.bean.OrderItem;
import tmall.bean.Product;
import tmall.bean.User;
import tmall.util.DBUtil;

public class OrderItemDao 
{
	public int getTotal() {
		
        int total = 0;
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
  
            String sql = "select count(*) from OrderItem";
  
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
        return total;
    }
  
    public void add(OrderItem bean) {
 
        String sql = "insert into OrderItem values(null,?,?,?,?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);) {
  
            ps.setInt(1, bean.getProduct().getId());
             
            //订单项在创建的时候，是没有订单信息的
            if(null==bean.getOrder())
                ps.setInt(2, -1);
            else
                ps.setInt(2, bean.getOrder().getId()); 
             
            ps.setInt(3, bean.getUser().getId());
            ps.setInt(4, bean.getNumber());
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
  
    public void update(OrderItem bean) {
 
        String sql = "update OrderItem set pid= ?, oid=?, uid=?, number=?  where id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
 
            ps.setInt(1, bean.getProduct().getId());
            if(null==bean.getOrder())
                ps.setInt(2, -1);
            else
                ps.setInt(2, bean.getOrder().getId());             
            ps.setInt(3, bean.getUser().getId());
            ps.setInt(4, bean.getNumber());
             
            ps.setInt(5, bean.getId());
            ps.execute();
  
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
  
    }
  
    public void delete(int id) {
  
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
  
            String sql = "delete from OrderItem where id = " + id;
  
            s.execute(sql);
  
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
    }
  
    public OrderItem get(int id) {
        OrderItem bean = new OrderItem();
  
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
  
            String sql = "select * from OrderItem where id = " + id;
  
            ResultSet rs = s.executeQuery(sql);
  
            if (rs.next()) {
                int pid = rs.getInt("pid");
                int oid = rs.getInt("oid");
                int uid = rs.getInt("uid");
                int number = rs.getInt("number");
                Product product = new ProductDao().get(pid);
                User user = new UserDao().get(uid);
                bean.setProduct(product);
                bean.setUser(user);
                bean.setNumber(number);
                 
                if(-1!=oid){
                    Order order= new OrderDao().get(oid);
                    bean.setOrder(order);                  
                }
                 
                bean.setId(id);
            }
  
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
        return bean;
    }
    
    public int getSaleCount(int pid) 
    {
    	String sql = "SELECT sum(number) FROM tmall.orderitem WHERE pid = ?";
    	int total = 0;
    	
    	try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) 
    	{
    		ps.setInt(1, pid);
    		
    		ResultSet rs = ps.executeQuery();
    		
    		while (rs.next()) {
    			total = rs.getInt(1);
    		}
    		
    	} catch (SQLException e) {
			e.printStackTrace();
		}
    	return total;
    }
    
    public List<OrderItem> listByUser(int uid, int start, int end) 
    {
    	List<OrderItem> beans = new ArrayList<>();
    	
    	String sql = "select * from tmall.orderitem where uid = ? and oid=-1 order by id desc limit ?,?";
    	try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) 
    	{
    		ps.setInt(1, uid);
    		ps.setInt(2, start);
    		ps.setInt(3, end);
    		
    		ResultSet rs = ps.executeQuery();
    		
    		while (rs.next()) {
    			OrderItem bean = new OrderItem();
    			bean.setId(rs.getInt(1));
    			bean.setNumber(rs.getInt("number"));
    			bean.setProduct(new ProductDao().get(rs.getInt("pid")));
    			bean.setUser(new UserDao().get(rs.getInt("uid")));
    			
    			beans.add(bean);
    		}
    		
    	} catch (SQLException e) {
			e.printStackTrace();
		}
    	return beans;
    }
    
    public List<OrderItem> listByOrder(int oid, int start, int end) 
    {
    	List<OrderItem> beans = new ArrayList<>();
    	
    	String sql = "select * from tmall.orderitem where oid= ? order by id desc limit ?,?";
    	try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) 
    	{
    		ps.setInt(1, oid);
    		ps.setInt(2, start);
    		ps.setInt(3, end);
    		
    		ResultSet rs = ps.executeQuery();
    		
    		while (rs.next()) {
    			OrderItem bean = new OrderItem();
    			
    			if (oid != -1) {
    				Order order = new OrderDao().get(oid);
    				bean.setOrder(order);
    			}
    			
    			bean.setId(rs.getInt(1));
    			bean.setNumber(rs.getInt("number"));
    			bean.setProduct(new ProductDao().get(rs.getInt("pid")));
    			bean.setUser(new UserDao().get(rs.getInt("uid")));
    			
    			beans.add(bean);
    		}
    		
    	} catch (SQLException e) {
			e.printStackTrace();
		}
    	return beans;
    }
    
    public List<OrderItem> listByProduct(int pid, int start, int end) 
    {
    	List<OrderItem> beans = new ArrayList<>();
    	
    	String sql = "select * from tmall.orderitem where pid = ? and oid != -1 order by id desc limit ?,?";
    	try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) 
    	{
    		ps.setInt(1, pid);
    		ps.setInt(2, start);
    		ps.setInt(3, end);
    		
    		ResultSet rs = ps.executeQuery();
    		
    		while (rs.next()) {
    			OrderItem bean = new OrderItem();
    			
    			bean.setOrder(new OrderDao().get(rs.getInt("oid")));
    			bean.setId(rs.getInt(1));
    			bean.setNumber(rs.getInt("number"));
    			bean.setProduct(new ProductDao().get(rs.getInt("pid")));
    			bean.setUser(new UserDao().get(rs.getInt("uid")));
    			
    			beans.add(bean);
    		}
    		
    	} catch (SQLException e) {
			e.printStackTrace();
		}
    	return beans;
    }
    
    public List<OrderItem> listByProduct(int pid) {
    	return listByProduct(pid, 0, Short.MAX_VALUE);
    }
    
    public List<OrderItem> listByOrder(int oid) {
    	return listByOrder(oid, 0, Short.MAX_VALUE);
    }
    
    public List<OrderItem> listByUser(int uid) {
    	return listByUser(uid, 0, Short.MAX_VALUE);
    }
    
    public void fill(Order o) {
    	List<OrderItem> ois = listByOrder(o.getId());
    	int totalNum = 0;
    	float total = 0;
    	for (OrderItem oi: ois) {
    		totalNum += oi.getNumber();
    		total += oi.getNumber() * oi.getProduct().getPromotePrice();
    	}
    	o.setTotal(total);
    	o.setTotalNumber(totalNum);
    	o.setOrderItems(ois);
    }
    
    public void fill(List<Order> os) {
    	for (Order o: os) fill(o);
    }
}









