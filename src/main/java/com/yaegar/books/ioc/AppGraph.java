package com.yaegar.books.ioc;

import dagger.Component;

@Component(modules = AppModule.class)
public interface AppGraph extends Graph {
}
