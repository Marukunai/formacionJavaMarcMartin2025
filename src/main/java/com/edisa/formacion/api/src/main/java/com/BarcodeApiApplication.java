package com.edisa.formacion.api.src.main.java.com;

import com.edisa.formacion.api.src.main.java.com.resources.BarcodeResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class BarcodeApiApplication extends Application<BarcodeApiConfiguration> {
    public static void main(String[] args) throws Exception {
        new BarcodeApiApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<BarcodeApiConfiguration> bootstrap) {}

    @Override
    public void run(BarcodeApiConfiguration configuration, Environment environment) {
        environment.jersey().register(new BarcodeResource());
    }
}
