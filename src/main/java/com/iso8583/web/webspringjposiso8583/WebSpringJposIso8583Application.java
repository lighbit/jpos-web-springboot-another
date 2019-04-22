package com.iso8583.web.webspringjposiso8583;

import base.service.payments.Container;
import com.iso8583.web.webspringjposiso8583.controller.PaymentController;
import org.jpos.q2.Q2;
import org.jpos.q2.iso.QMUX;
import org.jpos.util.NameRegistrar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = Container.class)
@ComponentScan(basePackageClasses = PaymentController.class)
public class WebSpringJposIso8583Application {

	public static void main(String[] args) {
		Q2 q2 = new Q2();
		q2.start();
		System.out.println("Server is Running!");
		SpringApplication.run(WebSpringJposIso8583Application.class, args);
	}
	@Bean
	public QMUX exposeQmux() throws Exception {
		return (QMUX) NameRegistrar.get("mux.kisel");
	}

}
