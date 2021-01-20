package com.waracle.cake.service;

import com.waracle.cake.api.Cake;
import com.waracle.cake.api.CakeApi;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/cakes")
@CrossOrigin(origins = "http://localhost:3000")
public class CakeController implements CakeApi {

    private final CakeService cakeService;

    public CakeController(CakeService cakeService) {
        this.cakeService = cakeService;
    }

    @Override
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<Cake> get() {
        return cakeService.get();
    }

    @Override
    @GetMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Cake get(@PathVariable("id") int id) {
        return cakeService.get(id);
    }

    @Override
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Cake create(@RequestBody Cake cake) {
        return cakeService.create(cake);
    }

    @Override
    @PutMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Cake update(@PathVariable("id") int id,
                       @RequestBody Cake cake) {
        return cakeService.update(id, cake);
    }

    @Override
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") int id) {
        cakeService.delete(id);
    }
}
