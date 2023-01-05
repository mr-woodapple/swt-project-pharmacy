/*
 * Copyright 2014-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pharmacy;

import org.salespointframework.EnableSalespoint;
import org.salespointframework.SalespointSecurityConfiguration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pharmacy.messages.MessagesEntry;
import pharmacy.messages.MessagesRepository;
import java.util.stream.Stream;

@EnableSalespoint
public class Application {

	private static final String LOGIN_ROUTE = "/login";

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Configuration
	static class PharmacyWebConfiguration implements WebMvcConfigurer {

		@Override
		public void addViewControllers(ViewControllerRegistry registry) {
			registry.addViewController("/").setViewName("login");
			registry.addViewController(LOGIN_ROUTE).setViewName("login");
			registry.addViewController("new-product-order").setViewName("new-product-order");
			registry.addViewController("invoice").setViewName("invoice");
			registry.addViewController("register").setViewName("register");
			registry.addViewController("employee-edit").setViewName("employee-edit");
			registry.addViewController("doc-catalog").setViewName("doc-catalog");
			registry.addViewController("doc-cart").setViewName("doc-cart");
		}
	}

	@Configuration
	static class WebSecurityConfiguration extends SalespointSecurityConfiguration {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable();  // for lab purposes, that's ok!
			http.authorizeRequests().antMatchers("/**").permitAll().and()
					.formLogin().loginPage("/login").and()
					.logout().logoutUrl("/logout").logoutSuccessUrl("/");
		}
	}


	/**
	 * Adds some messages on application startup.
	 *
	 * @param messages Non-Null.
	 * @return The set of messages we want to have displayed at startup.
	 */
	@Bean
	CommandLineRunner init(MessagesRepository messages) {
		return args -> {

			Stream.of(
					new MessagesEntry("NO payrise", "Seriously guys, please stop asking. I need to buy my Tesla first."),
					new MessagesEntry("Tesla", "Actually, could you please do some unpaid overtime? You know, the Tesla..."))
					.forEach(messages::save);
		};
	}
}
