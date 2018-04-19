package tmall.servlet;

import tmall.bean.Product;
import tmall.bean.ProductImage;
import tmall.dao.ProductImageDao;
import tmall.util.DBUtil;
import tmall.util.ImageUtil;
import tmall.util.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductImageServlet extends BaseBackServlet
{
    @Override
    public String add(HttpServletRequest req, HttpServletResponse res, Page page)
    {
        Map<String, String> params = new HashMap<>();
        InputStream is = parseUpload(req, params);

        String type = params.get("type");
        int pid = Integer.parseInt(params.get("id"));

        Product p = productDao.get(pid);
        ProductImage pi = new ProductImage();

        pi.setType(type);
        pi.setProduct(p);
        productImageDao.add(pi);

        String fileName = pi.getId() + ".jpg";
        String imageFolder, imageFolder_small = null, imageFolder_middle = null;

        if (ProductImageDao.TYPE_SINGLE.equals(type))
        {
            imageFolder = req.getServletContext().getRealPath("img/productSingle");
            imageFolder_small = req.getServletContext().getRealPath("img/productSingle_small");
            imageFolder_middle  = req.getServletContext().getRealPath("img/productSingle_middle");
        }
        else {
            imageFolder = req.getServletContext().getRealPath("img/productDetail");
        }

        File file = new File(imageFolder, fileName);
        file.getParentFile().mkdirs();

        DBUtil.copyFile(is, file);

        //添加另外的两个图片文件（如果TYPE_SINGLE）
        if(ProductImageDao.TYPE_SINGLE.equals(pi.getType())){
            File file_small = new File(imageFolder_small, fileName);
            File file_middle = new File(imageFolder_middle, fileName);

            ImageUtil.resizeImage(file, 56, 56, file_small);
            ImageUtil.resizeImage(file, 217, 190, file_middle);
        }

        return "@admin_productImage_list?pid=" + pid;
    }

    @Override
    public String delete(HttpServletRequest req, HttpServletResponse res, Page page)
    {
        int id = Integer.parseInt(req.getParameter("id"));

        ProductImage pi = productImageDao.get(id);
        productImageDao.delete(id);

        String fileName = pi.getId() + ".jpg";
        String imageFolder, imageFolder_small = null, imageFolder_middle = null;

        if (ProductImageDao.TYPE_SINGLE.equals(pi.getType()))
        {
            imageFolder = req.getServletContext().getRealPath("img/productSingle");
            imageFolder_small = req.getServletContext().getRealPath("img/productSingle_small");
            imageFolder_middle  = req.getServletContext().getRealPath("img/productSingle_middle");
        }
        else
            imageFolder = req.getServletContext().getRealPath("img/productDetail");

        File file = new File(fileName, imageFolder);
        file.delete();

        if (ProductImageDao.TYPE_SINGLE.equals(pi.getType()))
        {
            File file_small = new File(imageFolder_small, fileName);
            file_small.delete();
            File file_middle = new File(imageFolder_middle, fileName);
            file_middle.delete();
        }

        return "@admin_productImage_list?pid=" + pi.getProduct().getId();
    }

    @Override
    public String edit(HttpServletRequest req, HttpServletResponse res, Page page) {
        return null;
    }

    @Override
    public String update(HttpServletRequest req, HttpServletResponse res, Page page) {
        return null;
    }

    public String list(HttpServletRequest req, HttpServletResponse res, Page page)
    {
        int pid = Integer.parseInt(req.getParameter("pid"));
        Product p = productDao.get(pid);

        List<ProductImage> pisSingle = productImageDao.list(p, ProductImageDao.TYPE_SINGLE);
        List<ProductImage> pisDetail = productImageDao.list(p, ProductImageDao.TYPE_DETAIL);

        req.setAttribute("pisSingle", pisSingle);
        req.setAttribute("pisDetail", pisDetail);
        req.setAttribute("p", p);

        return "admin/listProductImage.jsp";
    }
}
