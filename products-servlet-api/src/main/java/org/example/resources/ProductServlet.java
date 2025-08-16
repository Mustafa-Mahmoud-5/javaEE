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
import java.util.List;
import java.util.Map;

@WebServlet("/products/*")
public class ProductServlet extends HttpServlet {
    private ProductService productService = new ProductService();
    private ObjectMapper mapper = new ObjectMapper(); // jackson


    // GET /products
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            resp.setContentType("application/json");
            List<Product> products = productService.getAllProducts();
            mapper.writeValue(resp.getWriter(), products);
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
