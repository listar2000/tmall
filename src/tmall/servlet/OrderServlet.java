package tmall.servlet;

import tmall.bean.Order;
import tmall.dao.OrderDao;
import tmall.util.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

public class OrderServlet extends BaseBackServlet {

    public String list(HttpServletRequest req, HttpServletResponse res, Page page)
    {
        List<Order> os = orderDao.list(page.getStart(), page.getEnd());
        int total = orderDao.getTotal();

        orderItemDao.fill(os);

        page.setTotal(total);

        req.setAttribute("os", os);
        req.setAttribute("page", page);

        return "admin/listOrder.jsp";
    }

    public String delivery(HttpServletRequest req, HttpServletResponse res, Page page)
    {
        int id = Integer.parseInt(req.getParameter("id"));

        Order o = orderDao.get(id);

        o.setDeliveryDate(new Date());
        o.setStatus(OrderDao.waitConfirm);
        orderDao.update(o);

        return "@admin_order_list";
    }

    @Override
    public String add(HttpServletRequest req, HttpServletResponse res, Page page) {
        return null;
    }

    @Override
    public String delete(HttpServletRequest req, HttpServletResponse res, Page page) {
        return null;
    }

    @Override
    public String edit(HttpServletRequest req, HttpServletResponse res, Page page) {
        return null;
    }

    @Override
    public String update(HttpServletRequest req, HttpServletResponse res, Page page) {
        return null;
    }
}
