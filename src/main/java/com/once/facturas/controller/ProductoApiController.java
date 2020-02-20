package com.once.facturas.controller;

import java.util.NoSuchElementException;

import com.once.facturas.model.Producto;
import com.once.facturas.model.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
@RequestMapping(value = "/api/productos/") // Mapeo de este controlador
class ProductoApiController {
    /**
     * Escucha en las siguientes rutas
     * GET /api/productos/
     * GET /api/productos/{id}
     * GET /api/productos/hello
     * GET /api/productos/count
     * 
     * GET /api/productos/ - Nos devolverá una lista de facturas OK
     * GET /api/productos/1/ - Nos devolverá la factura número 1 (READ) OK 
     * DELETE /api/productos/1/ - Nos borrará la factura número 1 (DELETE) OK
     * PUT /api/productos/1/ - Nos modificará la factura número 1 (UPDATE) OK
     * POST /api/productos/ - Nos crea una nueva factura (CREATE) OK
     * Y luego está el PATCH: PATCH /api/productos/1/ nos permite modificar OK
     * una factura pero de la forma "añade 2 euros a la factura".
     * 
     * 
     * 
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

    // DELETE /api/facturas/{id}/
    @DeleteMapping("/{id}/")
    public Producto eliminarProducto(@PathVariable("id") Long id){
        try{
            pr.deleteById(id);
        }catch(IllegalArgumentException e){
            e = new IllegalArgumentException("No se ha encontrado el producto a eliminar");
            System.out.println(e.getMessage());
            return null;
        }
        return null;
    }

    // PUT /api/facturas/1/ - Nos modificará la factura número 1 (UPDATE) 
    @PutMapping("/{id}/")
    public Producto modificarProducto(
        @PathVariable("id") Long id, 
        @RequestBody Producto producto){
        Producto aModif = pr.findById(id).get();
        aModif.setDescripcion(producto.getDescripcion());
        aModif.setFabricante(producto.getFabricante());
        aModif.setPrecio(producto.getPrecio());
        return pr.save(aModif);
    }

    /**
     * Y luego está el PATCH: PATCH /api/facturas/1/ nos permite modificar 
     * una factura pero de la forma "añade 2 euros a la factura".
     * 
     */
    @PatchMapping("/{id}/")
    public Producto modificarProductoAnyadeEuros(
        @PathVariable("id") Long id,
        @RequestBody Producto producto){
            Producto aModif = pr.findById(id).get();
            aModif.setPrecio(aModif.getPrecio() + producto.getPrecio());
            pr.save(aModif);
            return null;
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