package com.yaegar.books.ioc;

import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
        CommonModule.class,
        AppModule.class
})
public interface AppGraph extends Graph {
}
