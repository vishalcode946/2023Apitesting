package com.swasthyaingit

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class RecordedSimulation extends Simulation {

	val httpProtocol = http
		.baseUrl("https://api.swasthyaingit.in")
		.disableFollowRedirect
		.disableAutoReferer


	val headers_1 = Map(
		"accept" -> "application/json, text/plain, */*",
		"accept-encoding" -> "gzip, deflate, br",
		"accept-language" -> "en-US,en;q=0.9",
		"content-type" -> "application/json",
		"origin" -> "https://app.swasthyaingit.in",
		"referer" -> "https://app.swasthyaingit.in/",
		"sec-ch-ua" -> """Not_A Brand";v="99", "Google Chrome";v="109", "Chromium";v="109""",
		"sec-ch-ua-mobile" -> "?0",
		"sec-ch-ua-platform" -> "macOS",
		"sec-fetch-dest" -> "empty",
		"sec-fetch-mode" -> "cors",
		"sec-fetch-site" -> "same-site",
		"user-agent" -> "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36")




	val scn = scenario("loginCHO")

		.exec(http("LoginCHO")
			.post("/api/UnAuth/SignInCHO")
			.headers(headers_1)
			.body(RawFileBody("com/swasthyaingit/recordedsimulation/0001_request.json"))
			.check(jsonPath("$.token").saveAs("Authtoken"))
			.check(status is (200))
			.check(bodyString.saveAs("Login with CHO")))
		.exec { session => println(session("Login with CHO").as[String]); session }



	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}