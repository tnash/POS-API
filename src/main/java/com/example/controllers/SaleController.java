package com.example.controllers;

import java.util.List;
import java.util.Set;
import com.example.domain.models.Sale;
import com.example.services.BaseService;
import com.example.services.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/1.0/sales")
public class SaleController extends BaseController<Sale, Long> {

    /**
     * Service that manages the sales
     */
    private final SaleService saleService;

    @Autowired
    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @Override
    protected BaseService<Sale, Long> getService() {
        return saleService;
    }

    @PostMapping()
    public Sale create() {
        return saleService.createSale();
    }

    @RequestMapping(method = RequestMethod.GET)
    public Set<Sale> find(@RequestParam(value = "ids", required = false) final List<Long> ids) {
        return saleService.findAll();
    }

    /**
     * Retrieve a Sale by id.
     *
     * @param id Identifier of the Sale
     * @return Sale matching the specified identifier
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public Sale get(@PathVariable final Long id) {
        return super.get(id);
    }

    /**
     * Add a transaction to the sale
     *
     * @param id  Identifier of the Sale
     * @param sku The SKU of the product to transact
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public Sale scan(@PathVariable final Long id, @RequestParam final String sku) {
        return saleService.scan(id, sku);
    }

    /**
     * Calculate the total of all of the transactions in the sale.
     *
     * @param id Identifier of the Sale
     * @return Total of all transactions in the sale.
     */
//    @RequestMapping(method = RequestMethod.HEAD, value = "/{id}")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/total")
    public Double totalSale(@PathVariable final Long id) {
        return saleService.saleTotal(id);
    }
}
