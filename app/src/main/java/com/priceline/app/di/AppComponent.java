package com.priceline.app.di;


import com.priceline.app.view.viewmodel.BooksListViewModel;
import com.priceline.app.PricelineApplication;
import com.priceline.app.di.module.AppModule;
import com.priceline.app.di.module.NetModule;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(
        modules = {AppModule.class, NetModule.class}
)
public interface AppComponent {

    public void inject(PricelineApplication pricelineApplication);
    public void inject(BooksListViewModel booksNameListViewModel);

}
