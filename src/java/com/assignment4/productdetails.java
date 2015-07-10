/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.assignment4;


import java.io.PrintWriter;
import java.io.StringReader;
import static java.lang.Integer.parseInt;
import static java.lang.Integer.parseInt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * REST Web Service
 *
 * @author Nimya 
 */
@Path("products")
public class productdetails {
    productConnection conec=new productConnection();
    Connection conn=null;
    @Context
    private UriInfo context;
    
    /**
     * Creates a new instance of ProductResource
     */
    public productdetails() {
       conn=conec.getConnection();
    }

    /**
     * Retrieves representation of an instance of com.oracle.products.ProductResource
     * @return an instance of java.lang.String
     */
   @GET
   @Produces(MediaType.APPLICATION_JSON)
   public String getAllProducts() throws SQLException
   {
       if(conn==null)
       {
           return "it is not connected";
       }
       else {
           String query="Select * from products";
           PreparedStatement pstate = conn.prepareStatement(query);
           ResultSet res = pstate.executeQuery();
           String results="";
           JSONArray prodArr = new JSONArray();
           while (res.next()) {
                Map pmap = new LinkedHashMap();
              pmap.put("productID", res.getInt("product_id"));
                pmap.put("name", res.getString("name"));
                pmap.put("description", res.getString("description"));
               pmap.put("quantity", res.getInt("quantity"));
                prodArr.add(pmap);
           }
            results = prodArr.toString();
          return  results.replace("},", "},\n");
        }
       
   }
   
   @GET
   @Path("{id}")
   @Produces(MediaType.APPLICATION_JSON)
   public String getproduct(@PathParam("id") int id) throws SQLException {
   
      if(conn==null)
       {
           return "it not able to connect";
       }
       else {
           String query="Select * from products where product_id = ?";
           PreparedStatement prestmt = conn.prepareStatement(query);
           prestmt.setInt(1,id);
           ResultSet res = prestmt.executeQuery();
           String results="";
           JSONArray podtAr = new JSONArray();
           while (res.next()) {
                 Map pdtmap = new LinkedHashMap();
                pdtmap.put("productID", res.getInt("product_id"));
                pdtmap.put("name", res.getString("name"));
                pdtmap.put("description", res.getString("description"));
               pdtmap.put("quantity", res.getInt("quantity"));
                podtAr.add(pdtmap);
           }    
                results = podtAr.toString();
                
                 return results;
      }
   
   }
   
   @POST
   @Path("/products")
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.TEXT_PLAIN)
   public String postProduct(String string) throws SQLException{
       JsonParser parse= Json.createParser(new StringReader(string));
       Map<String,String> pdtmap = new LinkedHashMap<String,String>();
       String key="";
       String value="";
       
       while(parse.hasNext()){
        JsonParser.Event event=parse.next();
            switch (event){
            case KEY_NAME :
              key = parse.getString();
              break;
            case VALUE_STRING:
              value=parse.getString();
              pdtmap.put(key, value);
              break;
            case VALUE_NUMBER:     
              value=parse.getString();
              pdtmap.put(key, value);
              break;  
            default :
              break;  
            }
       }    
       if(conn == null){
           return "Not able to  connect";
       }
       else {
            String query="INSERT INTO products (product_id,name,decription,quantity) VALUES (?,?,?)";
            PreparedStatement pstate=conn.prepareStatement(query);
            pstate.setInt(1, Integer.parseInt(pdtmap.get ("product_id")));
            pstate.setString(2, pdtmap.get("name"));
            pstate.setString(3, pdtmap.get("description"));
            pstate.setInt(4, Integer.parseInt(pdtmap.get("quantity")));
            pstate.executeUpdate();
            return " the row has been inserted into the database";
           }
       
       
   }
   
   
   @PUT
   @Path("{id}")
   @Consumes(MediaType.APPLICATION_JSON)
   public String  putProduct(@PathParam("id")  int id,String str) throws SQLException{
    JsonParser parse= Json.createParser(new StringReader(str));
       Map<String,String> pdtmap = new LinkedHashMap<String,String>();
       String key="";
       String value="";
       
       while(parse.hasNext()){
        JsonParser.Event event=parse.next();
            switch (event){
            case KEY_NAME :
              key = parse.getString();
              break;
            case VALUE_STRING:
              value=parse.getString();
              pdtmap.put(key, value);
              break;
            case VALUE_NUMBER:     
              value=parse.getString();
              pdtmap.put(key, value);
              break;  
            default :
              break;  
            }
       }    
       if(conn == null){
           return "Not able to connect";
       }
       else {
            String query="UPDATE product SET  name = ?, description = ?, quantity = ? WHERE product_id =?" ;          PreparedStatement pstate=conn.prepareStatement(query);
            pstate.setString(1, pdtmap.get("name"));
            pstate.setString(2, pdtmap.get("description"));
            pstate.setInt(3, Integer.parseInt(pdtmap.get("quantity")));
            pstate.setInt(4, id);
            pstate.executeUpdate();
            return "row has been updated into the database";
           }
   
   }
 
   @DELETE
   @Path("{id}")
   @Consumes(MediaType.TEXT_PLAIN)
   @Produces(MediaType.TEXT_PLAIN)
   public String deleteProduct(@PathParam("id") int id) throws SQLException{
       
        if(conn==null)
        {
           return "Not able to connected";
        }
        else {
           String query="DELETE FROM products WHERE product_id = ?";
           PreparedStatement pstate = conn.prepareStatement(query);
           pstate.setInt(1,id);
           pstate.executeUpdate();
           return "The specified row is deleted";
           
        }
   
    }
}


