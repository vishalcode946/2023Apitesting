package com.swasthyaingit
import io.gatling.http.Predef.*
import io.gatling.core.Predef.*

import scala.util.Random
class patientflow extends Simulation{
  val httpProtocol = http.baseUrl("https://beta-api.swasthyaingit.in")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")

  val rnd = new Random()

  def Random_mobile_num(length: Int)={
     rnd.alphanumeric.filter(_.isDigit).take(length).mkString
  }
  val customFeeder=Iterator.continually(Map(
    "mobile1" -> Random_mobile_num(10)
  ))
  def authenticate() = {
    feed(customFeeder)
    .exec(http("Login doctor")
      .post("/api/UnAuth/SendOTP")
      .body(ElFileBody("com/swasthyaingit/Patientflow/PatientLogin.json")).asJson
      .check(jsonPath("$.token").saveAs("OTPreceived"))
      .check(status is(200))
      .check(bodyString.saveAs("login response")))
      .exec { session => println(session("login response").as[String]); session }
      .pause(2)
  }

  def verify_patient_otp() = {
    exec(http("Verfiy Doctor OTP")
      .post("/api/UnAuth/VerifyOTP")
      .body(ElFileBody("com/swasthyaingit/Patientflow/VerifyPatient.json")).asJson
      .check(status is(200))
      .check(jsonPath("$.token").saveAs("jwttoken"))
      .check(bodyString.saveAs("verify otp response")))
      .exec { session => println(session("verify otp response").as[String]); session }
      .pause(1)
  }
  val scn1= scenario("Patient flow")
  .exec(authenticate())
  .exec(verify_patient_otp())

  setUp(scn1.inject(atOnceUsers(1))
  ).protocols(httpProtocol)
}
