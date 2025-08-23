package org.example.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.errors.ApiException;
import org.example.models.Product;
import org.example.services.ProductService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    private final ProductService productService = new ProductService();


    // GET /products
    @GET
    @Path("/")
    public Response getProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            return Response.status(Response.Status.OK).entity(products).build();
        } catch (Exception e) {
            return handleError(e);
        }
    }


    // GET /products/id
    @GET
    @Path("/{id}")
    public Response getProduct(@PathParam("id") int productId) {
        try {
            Product product = productService.getProductById(productId);
            return Response.status(Response.Status.OK).entity(product).build();
        } catch (Exception e) {
            return handleError(e);
        }
    }


    @POST
    @Path("/")
    public Response addProduct(Product product) {
        try {
            productService.addProduct(product);
            return Response.status(Response.Status.CREATED.getStatusCode()).entity("Product Created Successfully").build();
        } catch (Exception e) {
            return handleError(e);
        }
    }


    @PUT
    @Path("/{id}")
    public Response putProduct(@PathParam("id") int id,  Product product) {
        try {
          Product updateProduct = productService.updateProduct(id, product);

          Map<String, Object> res = new HashMap<>();
          res.put("message", "Product Updated Successfully");
          res.put("product", updateProduct);

          return Response.status(Response.Status.OK).entity(res).build();
        } catch (Exception e) {
            return  handleError(e);
        }
    }



    public Response handleError(Exception e) {
        int status;
        String message;

        if(e instanceof ApiException) {
            status = ((ApiException) e).getStatusCode();
            message = ((ApiException) e).getMessage();
        } else {
            status = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
            message = "Something Went Wrong";
        }


        Map<String, Object> res = new HashMap<>();
        res.put("status", status);
        res.put("message", message);

        return Response.status(status).entity(res).build();
    }
}
