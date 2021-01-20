package com.waracle.cake.service;

import com.waracle.cake.api.Cake;
import com.waracle.cake.api.CakeApi;
import com.waracle.cake.data.CakeEntity;
import com.waracle.cake.data.CakeEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class CakeService implements CakeApi {

    private final CakeEntityRepository cakeEntityRepository;

    public CakeService(CakeEntityRepository cakeEntityRepository) {
        this.cakeEntityRepository = cakeEntityRepository;
    }

    private static Cake cakeEntityToCake(CakeEntity cakeEntity) {
        return new Cake(cakeEntity.getId(),
                        cakeEntity.getTitle(),
                        cakeEntity.getDescription(),
                        cakeEntity.getImage());
    }

    private static CakeEntity cakeToCakeEntity(Cake cake) {
        CakeEntity cakeEntity = new CakeEntity();
        cakeEntity.setTitle(cake.getTitle());
        cakeEntity.setDescription(cake.getDescription());
        cakeEntity.setImage(cake.getImage());

        return cakeEntity;
    }

    private static ResponseStatusException resourceNotFoundException(int id) {
        throw new ResponseStatusException(
                NOT_FOUND, format("No cake found with id '%d'", id));
    }

    @Override
    public List<Cake> get() {
        List<CakeEntity> cakeEntities = cakeEntityRepository.findAll();
        return cakeEntities.stream()
                           .map(CakeService::cakeEntityToCake)
                           .collect(toList());
    }

    @Override
    public Cake get(int id) {
        if (!cakeEntityRepository.existsById(id)) {
            throw resourceNotFoundException(id);
        }
        return cakeEntityToCake(cakeEntityRepository.getOne(id));
    }

    @Override
    public Cake create(Cake cake) {
        CakeEntity cakeEntity = cakeToCakeEntity(cake);
        return cakeEntityToCake(cakeEntityRepository.save(cakeEntity));
    }

    @Override
    public Cake update(int id, Cake cake) {
        if (!cakeEntityRepository.existsById(id)) {
            throw resourceNotFoundException(id);
        }

        CakeEntity cakeEntity = cakeEntityRepository.getOne(id);
        cakeEntity.setTitle(cake.getTitle());
        cakeEntity.setDescription(cake.getDescription());
        cakeEntity.setImage(cake.getImage());

        return cakeEntityToCake(cakeEntityRepository.save(cakeEntity));
    }

    @Override
    public void delete(int id) {
        if (!cakeEntityRepository.existsById(id)) {
            throw resourceNotFoundException(id);
        }
        cakeEntityRepository.deleteById(id);
    }
}
