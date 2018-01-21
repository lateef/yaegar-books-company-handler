package bdd.ioc;

import com.yaegar.books.ioc.CommonModule;
import com.yaegar.books.ioc.Graph;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
        CommonModule.class,
        TestAppModule.class
})
public interface TestAppGraph extends Graph {
}
