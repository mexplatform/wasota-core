package wasota.core;

import java.io.File;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import wasota.core.authentication.UserAuth;
import wasota.core.exceptions.CannotAddMexNamespaces;
import wasota.core.exceptions.graph.NotPossibleToLoadGraph;
import wasota.core.experiments.ExperimentsServiceInterface;
import wasota.core.experiments.impl.ExperimentServicesImpl;
import wasota.core.graph.GraphStoreInterface;
import wasota.core.graph.WasotaGraphInterface;
import wasota.core.graph.impl.GraphStoreFSImpl;
import wasota.core.graph.impl.WasotaGraphJenaImpl;
import wasota.properties.WasotaProperties;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@SpringBootApplication
public class Application {

	public static void main(String[] args) {

		new WasotaProperties().loadProperties();

		SpringApplication.run(Application.class, args);

		// make local folders
		File file = new File(WasotaProperties.GRAPH_FOLDER_PATH);
		if (!file.exists())
			file.mkdirs();
		file = new File(WasotaProperties.INDEX_FOLDER_PATH);
		if (!file.exists())
			file.mkdirs();
	}
	
	@Autowired
	GraphStoreInterface graphStore;
	
	@Autowired
	WasotaGraphInterface wasotaGraph;

	@PostConstruct
	public void doStuff() {
		try {

			// add MEX namespaces to graph
			wasotaGraph.addMexNamespacesToModel();

			List<String> graphNames = graphStore.getAllGraphNames();

			// load all stored graphs
			for (String namedGraph : graphNames) {
				graphStore.loadGraph(namedGraph, wasotaGraph, "ttl");
			}

		} catch (CannotAddMexNamespaces | NotPossibleToLoadGraph e) {
			e.printStackTrace();
		}
	}

}

@Configuration
class WasotaConfig {

	@Bean
	public GraphStoreInterface getGraphStoreFS() {
		return new GraphStoreFSImpl();
	}

//	@Bean
//	public WasotaGraphInterface getWasotaGraph() {
//		return new WasotaGraphJenaImpl();
//	}

	@Bean
	public ExperimentsServiceInterface getExperimentServices() {
		return new ExperimentServicesImpl();
	}

}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(userDetailsService());
	}

	@Autowired
	UserAuthenticationServiceInterface userAuth = new UserAuthenticationMongoImpl();

	@Bean
	UserDetailsService userDetailsService() {
		return new UserDetailsService() {

			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

				UserAuth user = userAuth.loadUser(username);

				if (user != null)
					return new User(user.getUser(), user.getPassword(), true, true, true, true,
							AuthorityUtils.createAuthorityList("USER"));
				else
					throw new UsernameNotFoundException("Could not find the user '" + username + "'");
			}

		};
	}
}

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/graph", "/graphFile", "/user/add", "/context", "/performance", "/performance/get",
						"/experiments/size", "/experiments/list", "/experiments/graphContext")
				.permitAll().anyRequest().authenticated().and().httpBasic().and().csrf().disable();
	}

}
