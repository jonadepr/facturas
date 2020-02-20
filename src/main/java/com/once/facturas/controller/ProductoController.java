package com.once.facturas.controller;

import java.util.NoSuchElementException;

import com.once.facturas.model.Producto;
import com.once.facturas.model.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * ProductoController es el controlador de la aplicación Facturas,
 * que es una aplicación tipo MVC (Modelo Vista Controlador)
 * 
 * 
 */

@RestController // Devuelvo siempre algo convertido en json
@RequestMapping(value = "/productos/") // Mapeo de este controlador
class ProductoController {
    /**
     * Escucha en las siguientes rutas
     * GET /productos/
     * GET /productos/{id}
     * GET /productos/hello
     * GET /productos/count
     * 
    */
    @Autowired
    ProductoRepository pr; // Interface for generic CRUD operations 
                           // sobre la base de datos, crea la clase que hace la acción

    @GetMapping("/")
    public Iterable<Producto> getAllProductos() {
        return pr.findAll(); // Devolvemos objetos del tipo Producto
    }

    @GetMapping("/{id}/")
    public Producto getProducto(@PathVariable("id") Long id){
        Producto pro;
        try{
            pro = pr.findById(id).get(); // otherwise throws NoSuchElementException
        }catch(NoSuchElementException e){
            e = new NoSuchElementException("No se encuentra el producto con id "+id);
            System.out.println(e.getMessage());
            return null;
        }
        return pro;
    }

    @PostMapping("/")
    public Producto crearProducto(
        @RequestBody Producto producto
    ) {
        Producto p = pr.save(producto);
        return p;
    }



    @GetMapping("/hello") // Escucho al GET en /hello
    @ResponseBody // Haré un body html para devolver la página completa
    public String hello() { // Método para devolver un string para responsebody
        return "Hola Mundo"; // Devuelvo Hola Mundo
    }

    @GetMapping("/count") // Escucho al GET en /count
    @ResponseBody // Haré un body html para devolver la página completa
    public String count() { // Método para devolver un string con responsebody
        return "Tengo "+String.valueOf(pr.count())+" productos"; // Devuelvo el número de productos desde pr.count()
    }

}