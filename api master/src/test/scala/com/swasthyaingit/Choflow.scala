package com.swasthyaingit
import io.gatling.http.Predef.*
import io.gatling.core.Predef.*

import scala.util.Random

class Choflow  extends Simulation{

val httpProtocol=http.baseUrl("https://beta-api.swasthyaingit.in")
  .acceptHeader("application/json, text/plain, */*")
  .contentTypeHeader("application/json")
  .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36")

  val headers_1 = Map(
    "accept" -> "application/json, text/plain, */*",
    "accept-encoding" -> "gzip, deflate, br",
    "accept-language" -> "en-US,en;q=0.9",
    "content-type" -> "application/json",
    "origin" -> "https://app.swasthyaingit.in",
    "referer" -> "https://app.swasthyaingit.in/",
//    "sec-ch-ua" -> """Not_A Brand";v="99", "Google Chrome";v="109", "Chromium";v="109""",
//    "sec-ch-ua-mobile" -> "?0",
//    "sec-ch-ua-platform" -> "macOS",
//    "sec-fetch-dest" -> "empty",
    "sec-fetch-mode" -> "cors",
     "sec-fetch-site" -> "same-site",
    "user-agent" -> "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36")
  val rnd = new Random()
  def Random_String(length: Int) = {
    rnd.alphanumeric.filter(_.isLetter).take(length).mkString
  }

  val csvFeeder=csv("data/cho_users.csv").circular
  val customFeeder = Iterator.continually(Map(
    "FirstName" -> Random_String(10),
    "LastName" -> Random_String(10),
    "Authtoken1" -> "4r3AvshjduIbagybY7IflveWvVY7vZoxCyoHjhYrYzAMZCdWgJ89zhYzjPrF90bnpa5ZS3NJEsrhBdGY5TDiRdHquBjH2shTgRKzE46Ap1Xr6bw4AJcmceEXX1we+y1PWcImLiavNUHm7m91p81cuZnm+XQjn4/Q8W8tncKHZJ0oBnXsq2wBxcPxWJKaVH42Y3+LACuuL5tOEutHmigLOy1Nd0TrprKzwjJ/IKHEuD1l10z/GmjUGBtwOj4Cxb/OnuBjZLajsN7FbXEUf2TVvIuJRM1hD28DDPJz72+nDtrk+t+QshKXuFvxZWEJrTzTCHnUcKajEN2xHinnnh8oT6jxVNH0QlycjeofBU3/5msliWBnb/AU5O9FkyeJdgO7vTLUSzan3jhQXTWy6hUI1FWY1RXHooeHwSpdXRb61IrNslwsi6QoQxA1ee8mNyFTNcgOvXaVDRnhrZOzP5hv/Z63S51jbdbMTmuZXgc328dAVolNUzyaXI3qHwVN/+Zr9N/r5i0SfQOffd7bDxcEmo3qHwVN/+ZrLNNPKGIVGDZ8O3hhtuvMlI3qHwVN/+Zr0DHOgoE5BXay83zShKzA44exJ4Rr5DPCFzfXyAOB4qAM8nPvb6cO2puljs+W5FiuGW/kvj57/TGN6h8FTf/ma5Sf1hM8NY5Mi6SGvlWQk5L/IHORtBwsda/KIIsBvbt4HcNrbvvo8Ksot51bB/FAbKgTCkud+rZNE8WuGQLPzQK5FWvbD4t8/8M7ZMwMmht8NeR0tuG45Idt9BA98eYXzqJj8dCd+Ff86Rji09aqqnMLh1FYgWuSD/giOjZSMvuQ/2sTLC1SBmcgyTeNd6J+/g=="
  ))

def login_cho()={
  exec(http("Login with CHO")
  .post("/api/UnAuth/SignInCHO")
//    .headers(headers_1)
  .body(ElFileBody("com/swasthyaingit/choflow1/CHOlogin.json")).asJson
  .check(jsonPath("$.token").saveAs("Authtoken"))
  .check(status is(200))
  .check(bodyString.saveAs("Login with CHO")))
  .exec{session => println(session("Login with CHO").as[String]);session}}

  def login_cho_csv() = {
    feed(csvFeeder)
    .exec(http("Login with CHO csv")
      .post("/api/UnAuth/SignInCHO")
      .headers(headers_1)
      .body(ElFileBody("com/swasthyaingit/choflow1/CHologincsv.json")).asJson
      .check(jsonPath("$.token").saveAs("Authtoken"))
      .check(status is (200))
      .check(bodyString.saveAs("Login with CHO csv")))
      .exec { session => println(session("Login with CHO csv").as[String]); session }

}
def Registered_Patients_mysk()={
  feed(customFeeder)
  .exec(http("Registered Patients my sk")
  .get("/api/CHOPatient/GetCHOPatientList?SkipRecords=0&LimitRecords=100&LoadAll=false")
    .header("authorization","Bearer ${Authtoken1}")
    .check(status is (200))
  .check(bodyString.saveAs("Registered Patients my sk")))
  .exec{session => println(session("Registered Patients my sk").as[String]);session}
}
  def Registered_Patients_allsk()={
    feed(customFeeder)
    .exec(http("Registered Patients all sk")
      .get("/api/CHOPatient/GetCHOPatientList?SkipRecords=0&LimitRecords=100&LoadAll=true")
      .header("authorization","Bearer ${Authtoken1}")
      .check(status is (200))
      .check(bodyString.saveAs("Registered Patients my sk")))
    .exec{session => println(session("Registered Patients my sk").as[String]);session}
  }

  def get_doctor_for_consult() = {
    feed(customFeeder)
      .exec(http("get_doctor_for_consult")
        .get(s"/api/CHOPatient/GetOnlineDoctors")
        .header("authorization", "Bearer ${Authtoken1}")
        .queryParam("SkipRecords", "0")
        .queryParam("LimitRecords", "100")
        .queryParam("spl_id", "0")
        .queryParam("DoctorID", "114800")
        .check(status is (200))
        .check(jsonPath("$.lstModel[0].doctor_id").saveAs("doctorid"))
        .check(bodyString.saveAs("get_doctor_for_consult")))
      .exec { session => println(session("get_doctor_for_consult").as[String]); session }
  }
  def get_Draft_CHO_cases() = {
    feed(customFeeder)
    .exec(http("get_Draft_CHO_cases")
      .post("/api/Consultation/ReferredList")
      .header("authorization", "Bearer ${Authtoken1}")
      .body(ElFileBody("com/swasthyaingit/choflow1/CHOcasedraft.json"))
      .check(status is (200))
      .check(bodyString.saveAs("get_Draft_CHO_cases")))
    .exec { session => println(session("get_Draft_CHO_cases").as[String]); session }
  }

  def get_inprogress_CHO_cases() = {
    feed(customFeeder)
    .exec(http("get_inprogress_CHO_cases")
      .post("/api/Consultation/ReferredList")
      .header("authorization", "Bearer ${Authtoken1}")
      .body(ElFileBody("com/swasthyaingit/choflow1/CHOinprogresscases.json"))
      .check(status is (200))
      .check(bodyString.saveAs("get_inprogress_CHO_cases")))
    .exec { session => println(session("get_inprogress_CHO_cases").as[String]); session }
  }

  def get_completed_CHO_cases() = {
    feed(customFeeder)
    .exec(http("get_completed_CHO_cases")
      .post("/api/Consultation/ReferredList")
      .header("authorization", "Bearer ${Authtoken1}")
      .body(ElFileBody("com/swasthyaingit/choflow1/CHOcompletedcases.json"))
      .check(status is (200))
      .check(bodyString.saveAs("get_completed_CHO_cases")))
    .exec { session => println(session("get_completed_CHO_cases").as[String]); session }
  }

  def Add_family_member() = {
    repeat(1, "i") {
      feed(customFeeder)

        .exec(http("Registered Patients my sk")
          .get("/api/CHOPatient/GetCHOPatientList?SkipRecords=0&LimitRecords=100&LoadAll=false")
          .header("authorization", "Bearer ${Authtoken1}")
          .check(status is (200))
          .check(jsonPath("$.lstModel[${i}].patientInfoId").saveAs("patientInfoID"))
          .check(bodyString.saveAs("Registered Patients my sk")))
        .exec { session => println(session("Registered Patients my sk").as[String]); session }

        .exec(http("createfamilymember")
          .post("/api/CHOPatient/CreateUserFamilyMember")
          .header("authorization", "Bearer ${Authtoken1}")
          .body(ElFileBody("com/swasthyaingit/choflow1/Createfamilymemeber.json")).asJson
          .check(status is (200))
          .check(bodyString.saveAs("addfamilymember")))
        .exec { session => println(session("addfamilymember").as[String]); session }

    }
  }


    def get_Draft_CHO_cases1() = {
      repeat(1, "i") {
        feed(customFeeder)

          .exec(http("get_Draft_CHO_cases")
            .post("/api/Consultation/ReferredList")
            .header("authorization", "Bearer ${Authtoken1}")
            .body(ElFileBody("com/swasthyaingit/choflow1/CHOcasedraft.json")).asJson
            .check(jsonPath("$.lstModel[${i}].consultationId").saveAs("consultationId"))
            .check(jsonPath("$.lstModel[${i}].patientInfoID").saveAs("patientInfoID"))
            .check(jsonPath("$.lstModel[${i}].patientFirstName").saveAs("firstname"))
            .check(jsonPath("$.lstModel[${i}].patientMiddleName").saveAs("midname"))
            .check(jsonPath("$.lstModel[${i}].patientLastName").saveAs("lastname"))
            .check(bodyString.saveAs("get_Draft_CHO_cases")))
          .pause(1)
          .exec { session => println(session("get_Draft_CHO_cases").as[String]); session }
          .exec(http("ConsultationOnlineDoctor")
            .get("/api/CHOPatient/ConsultationOnlineDoctor?ConsultationId=${consultationId}&PatientInfoId=${patientInfoID}&spl_id=0&DoctorId=100442&count=1")
            .header("authorization", "Bearer ${Authtoken1}")
            .check(status is (200))
            //.check(jsonPath("$.lstModel[1].doctor_id").saveAs("doctorid"))
            .check(bodyString.saveAs("ConsultationOnlineDoctor")))
          .exec { session => println(session("ConsultationOnlineDoctor").as[String]); session }
      }
    }
      def get_consultation_details() = {
        repeat(1, "i") {
          feed(customFeeder)

//            .exec(http("get_Draft_CHO_cases")
//              .post("/api/Consultation/ReferredList")
//              .header("authorization", "Bearer ${Authtoken1}")
//              .body(ElFileBody("com/swasthyaingit/choflow1/CHOcasedraft.json")).asJson
//              .check(jsonPath("$.lstModel[${i}].consultationId").saveAs("consultationId"))
//              .check(jsonPath("$.lstModel[${i}].patientInfoID").saveAs("patientInfoID"))
//              .check(jsonPath("$.lstModel[${i}].patientFirstName").saveAs("firstname"))
//              .check(jsonPath("$.lstModel[${i}].patientMiddleName").saveAs("midname"))
//              .check(jsonPath("$.lstModel[${i}].patientLastName").saveAs("lastname"))
//              .check(bodyString.saveAs("get_Draft_CHO_cases")))
//            .pause(1)
//            .exec { session => println(session("get_Draft_CHO_cases").as[String]); session }
            .exec(http("Consultation details")
              .get("/api/Consultation/23610157")
              .header("authorization", "Bearer ${Authtoken1}")
              .check(status is (200))
              //.check(jsonPath("$.lstModel[1].doctor_id").saveAs("doctorid"))
              .check(bodyString.saveAs("Consultation details")))
            .exec { session => println(session("Consultation details").as[String]); session }
        }
    }


  val scn1 = scenario("CHO flow")
  // .exec(login_cho())
   //.exec(login_cho_csv())
   //.exec(Registered_Patients_mysk())
  //.exec(Registered_Patients_allsk())
 // .exec(get_doctor_for_consult())
//    .exec(get_Draft_CHO_cases())
//   .exec(get_inprogress_CHO_cases())
//   .exec(get_completed_CHO_cases())
// .exec(Add_family_member())
  //  .exec(get_Draft_CHO_cases1())
  .exec(get_consultation_details())

  setUp(scn1.inject(nothingFor(5),
    constantUsersPerSec(40).during(120)
  )).protocols(httpProtocol)
}

