package com.waracle.cake.api;

import java.util.List;

public interface CakeApi {

    List<Cake> get();

    Cake get(int id);

    Cake create(Cake cake);

    Cake update(int id, Cake cake);

    void delete(int id);
}