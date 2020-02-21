package com.once.facturas.controller;

import com.once.facturas.model.Producto;
import com.once.facturas.model.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/productos/")
public class ProductoHtmlController {

    /** SOLO CON GET Y POST
     * Escucha en las siguientes rutas
     * 
     * GET /productos/ - Nos devolverá una lista de productos OK
     * GET /productos/1/ - Nos devolverá el producto con id = 1 (READ) OK
     * DELETE /productos/1/ - Nos borrará el producto número 1 (DELETE) OK
     * PUT /productos/1/ - Nos modificará el producto número 1 (UPDATE) OK
     * POST /productos/ - Nos crea un nuevo producto (CREATE)
     * Y luego está el PATCH: PATCH /productos/1/ nos permite modificar
     * una factura pero de la forma "añade 2 euros a la factura".
     * 
    */
    @Autowired
    ProductoRepository pr;

    @GetMapping("/")
    public ModelAndView listaProductos(){
        ModelAndView modelAndView = new ModelAndView("listaProductos");
        Iterable<Producto> todosLosProductos = pr.findAll();
        modelAndView.addObject("productos", todosLosProductos);
        return modelAndView;
    }  
    
    @GetMapping("/{id}/")
    public ModelAndView listaProducto(@PathVariable("id") Long id){
        ModelAndView modelAndView = new ModelAndView("listaProductos");
        Producto elProducto = pr.findById(id).get();
        modelAndView.addObject("productos", elProducto);
        return modelAndView;
    }

    @GetMapping("/{id}/delete/")
    public ModelAndView eliminarProducto(@PathVariable("id") Long id){
        ModelAndView modelAndView = new ModelAndView("productoBorrado");
        Producto elProducto = pr.findById(id).get();
        modelAndView.addObject("productos", elProducto);
        pr.deleteById(id);
        return modelAndView;
    }

    private Long idActualizar = Long.valueOf("0");

    @GetMapping("/{id}/update/")
    public ModelAndView updateProductoGet(@PathVariable("id") Long id){
        ModelAndView modelAndView=new ModelAndView("updateProducto");
        Producto elProducto = pr.findById(id).get();
        modelAndView.addObject("producto", elProducto);
        idActualizar = id;
        return modelAndView;
    }


    @PostMapping("/update")
    public ModelAndView clientePost(
        @RequestParam("descripcion") String descripcion,
        @RequestParam("fabricante") String fabricante,
        @RequestParam("precio") Float precio
    ){
        ModelAndView modelAndView=new ModelAndView("updateProducto");
        //System.out.println(idActualizar);
        Producto actual = pr.findById(idActualizar).get();
        actual.setDescripcion(descripcion);
        actual.setFabricante(fabricante);
        actual.setPrecio(precio);
        actual = pr.save(actual);
        modelAndView.addObject(
            "todos", 
            "Id: "+actual.getId()
            +" Fabricante: "+ actual.getFabricante()
            +" Descripcion: "+ actual.getDescripcion()
            +" Precio: "+ actual.getPrecio()+" actualizado...");
        return modelAndView;
    }

    @GetMapping("/crearproducto")
    public ModelAndView crearProducto() {
        ModelAndView modelAndView = new ModelAndView("crearProducto");
        modelAndView.addObject("mensaje", "");
        return modelAndView;
    }

    @PostMapping("/crearProducto")
    public ModelAndView insertarHTMLPost(
        @RequestParam("fabricante") String fabricante,
        @RequestParam("descripcion") String descripcion, 
        @RequestParam("precio") Float precio) {

        ModelAndView modelAndView = new ModelAndView("crearProducto");
        Producto nuevo =  new Producto();
        nuevo.setDescripcion(descripcion);
        nuevo.setFabricante(fabricante);
        nuevo.setPrecio(precio);
        nuevo = pr.save(nuevo);
        modelAndView.addObject(
            "mensaje", 
            "Id: "+nuevo.getId()
            +" Fabricante: "+ nuevo.getFabricante()
            +" Descripcion: "+ nuevo.getDescripcion()
            +" Precio: "+ nuevo.getPrecio()+" introducido...");
        return modelAndView;
    }


}