package com.github.avec112;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class VaadinAuthSimulation extends Simulation {
    private static final String BASE_URL = System.getProperty("baseUrl", "http://localhost:8080");
    private static final String LOGIN_PAGE_PATH = System.getProperty("loginPagePath", "/login");
    private static final String LOGIN_POST_PATH = System.getProperty("loginPostPath", "/login");
    private static final String PROTECTED_PATH = System.getProperty("protectedPath", "/");

    private static final int USERS = Integer.parseInt(System.getProperty("users", "2"));
    private static final int RAMP_SECONDS = Integer.parseInt(System.getProperty("rampSeconds", "30"));

    HttpProtocolBuilder httpProtocol =
            http.baseUrl(BASE_URL)
                    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .userAgentHeader("Mozilla/5.0 (X11; Linux x86_64) Gatling/Java");

    ScenarioBuilder loginScenario =
            scenario("Vaadin Authentication Scenario")
                    .feed(csv("data/users.csv").circular())
                    .exec(
                            http("GET login page")
                                    .get(LOGIN_PAGE_PATH)
                                    .check(status().is(200))
                                    .check(
                                            regex("name=[\"']_csrf[\"'][^>]*value=[\"']([^\"']+)")
                                                    .optional()
                                                    .saveAs("csrfToken")))
                    .exec(session -> session.contains("csrfToken") ? session : session.set("csrfToken", ""))
                    .exec(
                            http("POST credentials")
                                    .post(LOGIN_POST_PATH)
                                    .disableFollowRedirect()
                                    .formParam("username", "#{username}")
                                    .formParam("password", "#{password}")
                                    .formParam("_csrf", "#{csrfToken}")
                                    .check(status().is(302))
                    )
                    .exec(
                            http("Follow redirect")
                                    .get(PROTECTED_PATH)
                                    .check(status().is(200))
//                                    .check(regex("Hello World").exists())
                    );

    {
        setUp(loginScenario.injectOpen(rampUsers(USERS).during(Duration.ofSeconds(RAMP_SECONDS))))
                .protocols(httpProtocol);
    }
}
