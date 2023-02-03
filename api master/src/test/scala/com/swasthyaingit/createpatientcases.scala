package com.swasthyaingit
import io.gatling.core.Predef.*
import io.gatling.http.Predef.*

import scala.util.Random
class createpatientcases extends Simulation {

  val httpProtocol = http.baseUrl("https://beta-api.swasthyaingit.in")
    .acceptHeader("application/json")
    .contentTypeHeader(" application/json")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36")

  val rnd = new Random()

  def Random_String(length: Int) = {
    rnd.alphanumeric.filter(_.isLetter).take(length).mkString
  }

  def Random_num(length: Int) = {
    rnd.alphanumeric.filter(_.isDigit).take(length).mkString
  }

  val customFeeder = Iterator.continually(Map(
    "FirstName" -> Random_String(10),
    "LastName" -> Random_String(10),
    "guardian_Name" -> Random_String(10),
    "num" -> Random_num(10),
    //username-load, 123456
    "Authtoken1" -> "4r3AvshjduIbagybY7IflveWvVY7vZoxCyoHjhYrYzAMZCdWgJ89zhYzjPrF90bnpa5ZS3NJEsrhBdGY5TDiRdHquBjH2shTgRKzE46Ap1Xr6bw4AJcmceEXX1we+y1PWcImLiavNUHm7m91p81cuZnm+XQjn4/Q8W8tncKHZJ0oBnXsq2wBxcPxWJKaVH42Y3+LACuuL5tOEutHmigLOy1Nd0TrprKzwjJ/IKHEuD1l10z/GmjUGBtwOj4Cxb/OnuBjZLajsN7FbXEUf2TVvIuJRM1hD28DDPJz72+nDtrk+t+QshKXuFvxZWEJrTzTCHnUcKajEN2xHinnnh8oT6jxVNH0QlycjeofBU3/5msliWBnb/AU5O9FkyeJdgO7vTLUSzan3jhQXTWy6hUI1FWY1RXHooeHwSpdXRb61IrNslwsi6QoQxA1ee8mNyFTNcgOvXaVDRnhrZOzP5hv/Z63S51jbdbMTmuZXgc328dAVolNUzyaXI3qHwVN/+Zr9N/r5i0SfQOffd7bDxcEmo3qHwVN/+ZrLNNPKGIVGDZ8O3hhtuvMlI3qHwVN/+Zr0DHOgoE5BXay83zShKzA44exJ4Rr5DPCFzfXyAOB4qAM8nPvb6cO2puljs+W5FiuGW/kvj57/TGN6h8FTf/ma5Sf1hM8NY5M4nSTgwfxXpDzCXrm1ucSjq/KIIsBvbt4CcVhcdeJ3XJagX30cUSwrKgTCkud+rZNE8WuGQLPzQK5FWvbD4t8/8M7ZMwMmht8NeR0tuG45Idt9BA98eYXznvAPGF2t+Bqjc2BE74qW0zLDiS2H5T63MIOnOE0TVGaIwWo8jNnDCkYisdLbXmK/A==",
    //Doctorload,123456
  "authdoc" -> "4r3AvshjduIbagybY7IflveWvVY7vZoxCyoHjhYrYzAMZCdWgJ89zhYzjPrF90bnpa5ZS3NJEsp8YKi0qN5tlbvtiVgcE9vrpkvbBAh500j93LvZcdF8lGyELp8J7KPKpa5ZS3NJEsp/Kgxh8LUffCz6d23LsV354OlLPW46I2WSep0NvVvlt3xgqLSo3m2VVLPatO2dZYNA2MUKeQ6LAvZBIt2p6XGWKSNrd8H3gQJHdyhdqYGXWBhAxc5nnKAkZBfEK6DW2wrtMlGPIB7Fap+pQN7+4uQBymjq05eC2klXS2obQJbC7ajxVNH0QlycjeofBU3/5msliWBnb/AU5O9FkyeJdgO7JKJodVyyY29b8WVhCa080wh51HCmoxDdZV1U7VPCAdsJrH0DwsMvFFfy+2IXLKtoH1K/D0SCfcbKthYprTTB6qeyWNTv+v7KqvKrAeZofJFPsMWDXKMGI09a0MU+7/hx2AiyPE4RVQoLxTTfox7v/knzpHGGzmYNVtrlskwjpP8komh1XLJjb/JLp6uJbRU9G1oKbnLz7OlymMFajPjLTvB2gOZt/D/VNhiCKhs9MPIQEAjsRPgAoNhUmNfb1Auw6a/m0ULHrnqmhQpgoM8t9eWdFEixnmYzrA0ua1zMSQGkGqb93hI8pdUgxMdCD+QHclxlAbpQrINTU9mB+i4iX4e3IQtoQA46kC6cravwucpH+hvC165Zsw==",

  ))

  def login_cho() = {
    exec(http("Login with CHO")
      .post("/api/UnAuth/SignInCHO")
      .body(ElFileBody("com/swasthyaingit/choflow1/CHOlogin.json")).asJson
      .check(jsonPath("$.token").saveAs("Authtoken"))
      .check(status is (200))
      .check(bodyString.saveAs("Login with CHO")))
      .exec { session => println(session("Login with CHO").as[String]); session }

  }

  def create_patient_via_CHO() = {
    repeat(1) {
      feed(customFeeder)
        .exec(http("Create Patient CHO")
          .post("/api/CHOPatient/AddPatient_Optim")
          .body(ElFileBody("com/swasthyaingit/Createpatient/CreatepatientCHO.json")).asJson
          .header("authorization", "Bearer ${Authtoken1}")
          .check(status is (200))
          .check(bodyString.saveAs("Create Patient CHO")))
        .exec { session => println(session("Create Patient CHO").as[String]); session }
        .pause(1)
    }
  }

  def Registered_Patients_mysk_with_draft() = {    //imp for consutltaion
    repeat(1, "i") {
      feed(customFeeder)
        .exec(http("Registered Patients my sk")
          .get("/api/CHOPatient/GetCHOPatientList?SkipRecords=0&LimitRecords=100&LoadAll=false")
          .header("authorization", "Bearer ${Authtoken1}")
          .check(jsonPath("$.lstModel[${i}].patientInfoId").saveAs("Patientid"))
          .check(jsonPath("$.lstModel[${i}].firstName").saveAs("firstname"))
          .check(jsonPath("$.lstModel[${i}].lastName").saveAs("lastname"))
          .check(bodyString.saveAs("Registered Patients my sk")))
        .pause(1)

        .exec(http("Draftcase")
          .post("/api/Consultation/DraftConsultation_Optim")
          .header("authorization", "Bearer ${Authtoken1}")
          .body(ElFileBody("com/swasthyaingit/Createpatient/Draftcase.json")).asJson
          .check(status is (200))
          .check(jsonPath("$.model.consultationModel.consultationId").saveAs("consultationId"))
          .check(bodyString.saveAs("Draftcase")))
        .exec { session => println(session("Draftcase").as[String]); session }

    }
  }

  def get_Draft_CHO_cases() = {
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

        .exec(http("Draftcase")
          .post("/api/Consultation/DraftConsultation_Optim")
          .header("authorization", "Bearer ${Authtoken1}")
          .body(ElFileBody("com/swasthyaingit/Createpatient/Draftcase.json")).asJson
          .check(status is (200))
          .check(jsonPath("$.model.consultationModel.createdBy").saveAs("CHOID"))
        // .check(jsonPath("$.model.consultationModel.consultationId").saveAs("consultationId"))
          .check(bodyString.saveAs("Draftcase")))
        .exec { session => println(session("Draftcase").as[String]); session }

        .exec(http("GetOnlineDoctors")
          .get("/api/CHOPatient/GetOnlineDoctors?SkipRecords=0&LimitRecords=100&DoctorID=${CHOID}&spl_id=0")
          .header("authorization", "Bearer ${Authtoken1}")
          .check(status is (200))
          .check(jsonPath("$.lstModel[0].doctor_id").saveAs("doctorid"))
          .check(bodyString.saveAs("GetOnlineDoctors")))
        .exec { session => println(session("GetOnlineDoctors").as[String]); session }

        .exec(http("ConsultationOnlineDoctor")
          .get("/api/CHOPatient/ConsultationOnlineDoctor?ConsultationId=${consultationId}&PatientInfoId=${patientInfoID}&spl_id=0&DoctorId=${doctorid}&count=1")
          .header("authorization", "Bearer ${Authtoken1}")
          .check(status is (200))
          //.check(jsonPath("$.lstModel[1].doctor_id").saveAs("doctorid"))
          .check(bodyString.saveAs("ConsultationOnlineDoctor")))
        .exec { session => println(session("ConsultationOnlineDoctor").as[String]); session }

        .exec(http("Update doctor status consultation")
          .post("/api/Consultation/UpdateConsultationStatus")
          .header("authorization", "Bearer ${Authtoken1}")
          .body(ElFileBody("com/swasthyaingit/Createpatient/Updatestatusconsultation.json")).asJson
          .check(bodyString.saveAs("Update doctor status consultation")))
        .exec { session => println(session("Update doctor status consultation").as[String]); session }
        .pause(1)

//        .exec(http("polling avaliabilty")
//          .post("/api/Consultation/AvailabilityPolling")
//          .header("authorization", "Bearer ${authdoc}")
//          .body(ElFileBody("com/swasthyaingit/Createpatient/pollingConsultion.json")).asJson
//          .check(status is (200))
//          .check(bodyString.saveAs("doctor close the consultation")))
//        .pause(1)
//        .exec { session => println(session("doctor close the consultation").as[String]); session }

        .exec(http("doctor close the consultation")
          .post("/api/Consultation/InsertResponseConsultation")
          .header("authorization", "Bearer ${authdoc}")
          .body(ElFileBody("com/swasthyaingit/Createpatient/CloseConsultion.json")).asJson
          .check(status is (200))
          .check(bodyString.saveAs("doctor close the consultation")))
        .pause(1)
        .exec { session => println(session("doctor close the consultation").as[String]); session }

        .exec(http("free doctor")
          .get("/api/Consultation/FreeDoctorResponseCall?ConsultationId=${consultationId}&DoctorID=119695")
          .header("authorization", "Bearer ${authdoc}")
          .check(status is (200))
          .check(bodyString.saveAs("free doctor")))
        .exec { session => println(session("free doctor").as[String]); session }
        .pause(1)
    }
  }

  def create_admin_doctor() = {
    repeat(100) {
      feed(customFeeder)
        .exec(http("create doctor ")
          .post("/api/Doctors/InsertDoctor")
          .header("authorization", "Bearer 4r3AvshjduI6rBO5vK+Uq9oyvvjEGEbaNN3BEEhIQNK2XxxUEsv4abWTMiib07kjg2GEHqGKBxCuDCQAHS1SXusXP8rSxKxFuu5NzLX5pt8yK1QSFf2TXvjPMJO3mUDFJKSVG4eokYgWHp65OCY935M6qOL/RMpCK4jMI9dzOQrZHO4Ij1kFPgrquHjiBnlwBi1WWYK3p4O0AyJeFlGqjNYbSgQnS2sXdwoaWqBhvnkE8VgnNclgyzAFj6fiLeZrbs7WA7uw9d9lke2Fhe0rOy5KFHjxGZpjyU5YY2gs4v7XTrYVKCP+OSKrhRJdNk263nIb7c6u7FGlJVP3rj3NvXQqJeqZO0LgwggNY4AR4c6J33r78YDsfZ8l1TLSOkMtC8U036Me7/5jjMyeTkD4u36rGz262wBinrdLnWNt1syGFewf8giLUme3O4e5xCwmDPJz72+nDtpZHyOYRypAuOzemeJsB7fIHwRfMnXSsBcm1doSh6nsFio4QZVb/eGaAUwUYYXku8rQHYoeruNdiP8HVgCdTSIplbnZ912e+5LYCLI8ThFVCgvFNN+jHu/+YKVX7f8FZ69W2uWyTCOk/ySiaHVcsmNv8kunq4ltFT3ll/ZZTc5MEVVW6MMZhvN+8HaA5m38P9WeYpdd0yf08DESQCtyHq2M2FSY19vUC7Dpr+bRQseueqaFCmCgzy315Z0USLGeZjOsDS5rXMxJAaQapv3eEjyl6jQ+A2p3KSrKPJlF/ZDUhh5EPXPfCjXlG6GE+FCp2iIthJg42kvGWV60OeNvpfDm")
          .body(ElFileBody("com/swasthyaingit/Createpatient/createdoctor1.json")).asJson
          .check(bodyString.saveAs("create doctor")))
        .exec { session => println(session("create doctor").as[String]); session }
    }
  }

  val scn = scenario("Create Patient API")
    .exec(create_patient_via_CHO())
    //.exec(Registered_Patients_mysk_with_draft())
 // .exec(get_Draft_CHO_cases())
  // .exec(get_Draft_CHO_cases())


  setUp(
    scn.inject(nothingFor(5),
      constantUsersPerSec(1).during(60)
  ).protocols(httpProtocol))
}
