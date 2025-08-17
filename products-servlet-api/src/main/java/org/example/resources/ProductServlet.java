package org.example.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.errors.ApiException;
import org.example.models.Product;
import org.example.services.ProductService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/products/*")
public class ProductServlet extends HttpServlet {
    private ProductService productService = new ProductService();
    private ObjectMapper mapper = new ObjectMapper(); // jackson


    // GET /products
    // GET /products/id
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            resp.setContentType("application/json");
            String path = req.getPathInfo();
            if(path != null && !path.equals("/")) {
                int productId = Integer.parseInt(path.substring(1));
                Product product = productService.getProductById(productId);
                resp.setStatus(HttpServletResponse.SC_OK);
                mapper.writeValue(resp.getWriter(), product);
            } else {
                List<Product> products = productService.getAllProducts();
                resp.setStatus(HttpServletResponse.SC_OK);
                mapper.writeValue(resp.getWriter(), products);
            }
        } catch (Exception e) {
            handleError(resp, e);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Product product = mapper.readValue(req.getReader(), Product.class);
            productService.addProduct(product);
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_CREATED);
            mapper.writeValue(resp.getWriter(), "Product Created Successfully");
        } catch (Exception e) {
            handleError(resp, e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String path = req.getPathInfo();
            if(path == null || path.equals("/")) throw new ApiException(HttpServletResponse.SC_BAD_REQUEST, "product id path param is mandatory");

            int productId = Integer.parseInt(path.substring(1));
            productService.deleteProduct(productId);

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            mapper.writeValue(resp.getWriter(), "Product Deleted Successfully");
        } catch (Exception e) {
            handleError(resp, e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String path = req.getPathInfo();
            if(path == null || path.equals("/")) {
                throw new ApiException(HttpServletResponse.SC_BAD_REQUEST, "product id path param is mandatory");
            }

            int productId = Integer.parseInt(path.substring(1));
            Product product = mapper.readValue(req.getReader(), Product.class);

            Product updatedProduct = productService.updateProduct(productId, product);



            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);

            Map<String, Object> res = new HashMap<>();
            res.put("message", "Product updated Successfully");
            res.put("product", updatedProduct);

            mapper.writeValue(resp.getWriter(), res);
        } catch (Exception e) {
            handleError(resp, e);
        }
    }

    private void handleError(HttpServletResponse resp, Exception e) throws IOException {
        int status;
        String message;

        if (e instanceof ApiException) {
            status = ((ApiException) e).getStatusCode();
            message = e.getMessage();
        } else {
            status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            message = "Internal server error: " +   e.getMessage();
        }

        resp.setStatus(status);
        resp.setContentType("application/json");

        mapper.writeValue(resp.getWriter(), Map.of("status", status, "error", message));
    }
}
