package batch;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"batch"})

public class ListOfFilmsMain {
	public static void main(String[] args) {
		new SpringApplicationBuilder(ListOfFilmsMain.class)
        .web(WebApplicationType.NONE)
        .run(args);
	}

}
