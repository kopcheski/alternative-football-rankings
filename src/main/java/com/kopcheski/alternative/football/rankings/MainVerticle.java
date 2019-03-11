package com.kopcheski.alternative.football.rankings;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kopcheski.alternative.football.rankings.model.matches.MatchesData;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MainVerticle extends AbstractVerticle {

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public void start(Future<Void> startFuture) throws Exception {

		WebClient client = WebClient.create(vertx);
		Router getATP = Router.router(vertx);

		getATP.get("/atp/:competition").handler(handler -> {
			String competition = handler.pathParam("competition");

			String to = LocalDate.now().format(formatter);
			String from = LocalDate.now().minusWeeks(52).format(formatter);

			client.get(80, "api.football-data.org", "/v2/competitions/" + competition + "/matches")
					.addQueryParam("dateFrom", from)
					.addQueryParam("dateTo", to)
					.putHeader("X-Auth-Token", System.getProperty("apiToken"))
					.send(ar -> {
						if (ar.succeeded()) {
							HttpResponse<Buffer> response = ar.result();
							JsonObject lastMatches = response.bodyAsJsonObject();
							MatchesData matchesData = getMatchesData(lastMatches);
							AtpStyle atpStyle = new AtpStyle(matchesData);
							handler.response().end(atpStyle.classification().toString());
						} else {
							System.err.println("Ops");
						}
					});
		});

		vertx.createHttpServer()
				.requestHandler(getATP::accept)
				.listen(8080);
	}

	private MatchesData getMatchesData(JsonObject lastMatches) {
		MatchesData matchesData = null;
		try {
			matchesData = new ObjectMapper().readValue(lastMatches.toString(), MatchesData.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return matchesData;
	}

}
