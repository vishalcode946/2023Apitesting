package com.swasthyaingit
import io.gatling.core.Predef.*
import io.gatling.http.Predef.*

import scala.util.Random

class Doctorflow extends Simulation{

  val httpProtocol=http.baseUrl("https://beta-api.swasthyaingit.in")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36")
  val rnd = new Random()

  def Random_mobile_num(length: Int) = {
    rnd.alphanumeric.filter(_.isDigit).take(length).mkString
  }
  val csvFeeder=csv("data/doc_users.csv").circular
  val customFeeder = Iterator.continually(Map(
    "mobile1" -> Random_mobile_num(10),
    "Authtoken1" -> "4r3AvshjduIbagybY7IflveWvVY7vZoxCyoHjhYrYzAMZCdWgJ89zhYzjPrF90bnpa5ZS3NJEsp8YKi0qN5tlbvtiVgcE9vrpkvbBAh500j93LvZcdF8lGyELp8J7KPKpa5ZS3NJEsp/Kgxh8LUffCz6d23LsV354OlLPW46I2WSep0NvVvlt3xgqLSo3m2VVLPatO2dZYNA2MUKeQ6LAvZBIt2p6XGWKSNrd8H3gQJHdyhdqYGXWBhAxc5nnKAkZBfEK6DW2wrtMlGPIB7Fap+pQN7+4uQBymjq05eC2klXS2obQJbC7ajxVNH0QlycjeofBU3/5msliWBnb/AU5O9FkyeJdgO7JKJodVyyY29b8WVhCa080wh51HCmoxDdZV1U7VPCAdsJrH0DwsMvFFfy+2IXLKtoH1K/D0SCfcbKthYprTTB6qeyWNTv+v7KqvKrAeZofJFPsMWDXKMGI09a0MU+7/hx2AiyPE4RVQoLxTTfox7v/knzpHGGzmYNVtrlskwjpP8komh1XLJjb/JLp6uJbRU9G1oKbnLz7OkVPJnUKtutN/B2gOZt/D/VNhiCKhs9MPI0fgE8iP+LnNhUmNfb1Auw6a/m0ULHrnqmhQpgoM8t9eWdFEixnmYzrA0ua1zMSQGkGqb93hI8pbfLGwvAe6yDHLLMwucQVSX71r9qxbnVxfG+nU6v86ARdgCUbIIpphqW1rBDoB6DbQ=="
  ))


  def authenticate()={
    feed(customFeeder)
    .exec(http("Login doctor")
    .post("/api/UnAuth/SignInDoctor")
    .body(ElFileBody("com/swasthyaingit/Doctorflowjson/DoctorLogin.json")).asJson
    .check(jsonPath("$.token").saveAs("OTPreceived"))
      .check(jsonPath("$..institutionId").saveAs("institutionId"))
     .check(jsonPath("$..mobile").saveAs("mobile"))
      .check(status is (200))
    .check(bodyString.saveAs("login response")))
      .exec{session => println(session("login response").as[String]);session}

  }

  def authenticate_csv() = {
    feed(csvFeeder)
      .exec(http("Login doctor")
        .post("/api/UnAuth/SignInDoctor")
        .body(ElFileBody("com/swasthyaingit/Doctorflowjson/DoctorLogincsv.json")).asJson
        .check(jsonPath("$.token").saveAs("OTPreceived"))
        .check(jsonPath("$..institutionId").saveAs("institutionId"))
        .check(jsonPath("$..mobile").saveAs("mobile"))
        .check(status is(200))
        .check(bodyString.saveAs("login response")))
      .exec { session => println(session("login response").as[String]); session }

  }
//  def verify_doctor_otp()={
//    exec(http("Verfiy Doctor OTP")
//      .post("/api/UnAuth/DoctorLoginVerifyOTP")
//      .body(ElFileBody("com/swasthyaingit/Doctorflowjson/verifydoctorOTP.json")).asJson
//      .check(jsonPath("$.token").saveAs("jwttoken"))
//      .check(bodyString.saveAs("verify otp response")))
//      .exec { session => println(session("verify otp response").as[String]); session }
//
//  }
//
//  def man_verify_doctor_otp() = {
//    exec(http("manual request Verfiy Doctor OTP")
//      .post("/api/UnAuth/DoctorLoginVerifyOTP")
//      .body(ElFileBody("com/swasthyaingit/Doctorflowjson/DoctorLoginVerifyOTP_man.json")).asJson
//      .check(jsonPath("$.token").saveAs("jwttoken"))
//      .check(bodyString.saveAs("verify otp response")))
//      .exec { session => println(session("verify otp response").as[String]); session }

 // }

    def Check_avaliabilty() = {
      feed(customFeeder)
    .exec(http("Check doctor avaliabilty")
      .get("/api/Consultation/CheckAvailability")
      .header("authorization", "Bearer ${Authtoken1}")
      .check(status is(200))
      .check(bodyString.saveAs("Response CheckAvailability")))
      .exec { session => println(session("Response CheckAvailability").as[String]); session }
      .pause(2)

    }
    def Case_recevied_complete() ={
      feed(customFeeder)
      .exec(http("Check case received on complete tab")
        .post("/api/Consultation/ReceivedList")
        .header("authorization", "Bearer ${Authtoken1}")
        .body(ElFileBody("com/swasthyaingit/Doctorflowjson/casereceviedcompleted.json"))
        .check(status is(200))
        .check(bodyString.saveAs("Response Check case received on complete tab")))
        .exec { session => println(session("Response Check case received on complete tab").as[String]); session }
        .pause(2)
    }

  def Case_recevied_inprogress() = {
    feed(customFeeder)
    .exec(http("Check case received on complete tab")
      .post("/api/Consultation/ReceivedList")
      .header("authorization", "Bearer ${Authtoken1}")
      .body(ElFileBody("com/swasthyaingit/Doctorflowjson/casereceviedinprogress.json"))
      .check(status is (200))
      .check(bodyString.saveAs("Response Check case received on complete tab")))
      .exec { session => println(session("Response Check case received on complete tab").as[String]); session }
      .pause(2)
  }




  val scn1= scenario("login doctor")
    .exec(authenticate())
   // .exec(authenticate_csv())
    //.pause(2)
   //.exec(verify_doctor_otp())
   // .exec(man_verify_doctor_otp())
   //.exec(Check_avaliabilty())
  // .exec(Case_recevied_complete())
    //.exec(Case_recevied_inprogress())


  setUp(
    scn1.inject(nothingFor(5),
      constantUsersPerSec(30).during(300),
  ).protocols(httpProtocol)
  )
}
