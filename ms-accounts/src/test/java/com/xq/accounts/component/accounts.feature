Feature: Account management

  Background: Given account name and email are generated
    * def uuid = function(){ return java.util.UUID.randomUUID() + '' }
    * def number = function(max){ return Math.floor(Math.random() * max) }
    * def createEndpoint = 'http://localhost:8080/api/accounts/create'
    * def fetchEndpoint = 'http://localhost:8080/api/accounts/fetch'
    * def updateEndpoint = 'http://localhost:8080/api/accounts/update'
    * def numRange = 500

  Scenario: customer can create a new account
    * def name = uuid()
    * def phone = number(numRange)

    Given url createEndpoint
    And request { name: "#(name)", email: "#(name)@gmail.com", mobileNumber: "#(phone)" }
    And method post
    Then status 201
    And match response == { "statusCode": "201", "statusMsg": "Account created successfully" }

    When url fetchEndpoint
    And params { mobileNumber: "#(phone)" }
    And method get
    Then status 200

  Scenario: customer can not create existing account
    * def name = uuid()
    * def phone = number(numRange)

    Given url createEndpoint
    And request { name: "#(name)", email: "#(name)@gmail.com", mobileNumber: "#(phone)" }
    And method post
    Then status 201

    Given url createEndpoint
    And request { name: "#(name)", email: "#(name)@gmail.com", mobileNumber: "#(phone)" }
    And method post
    Then status 400

  Scenario: customer can update an account by accountNumber
    * def name = uuid()
    * def phone = number(numRange)

    Given url createEndpoint
    And request { name: "#(name)", email: "#(name)@gmail.com", mobileNumber: "#(phone)" }
    And method post
    Then status 201

    When url fetchEndpoint
    And params { mobileNumber: "#(phone)" }
    And method get
    Then status 200

    * def payload = response
    * def newPhone = number(200)
    * def name = uuid()
    * payload.name = name
    * payload.email = name + "gmail.com"
    * payload.mobileNumber = newPhone.toString()
    * payload.account.accountType = "HomeLoan"
    When url updateEndpoint
    And request payload
    And method put
    Then status 200
    And match response == { "statusCode": "200", "statusMsg": "Request processed successfully" }

    When url fetchEndpoint
    And params { mobileNumber: "#(newPhone)" }
    When method get
    Then status 200
    And match response == payload


  Scenario: customer can not update an account when accountNumber does not exist
    * def name = uuid()
    * def phone = number(200)
    Given url updateEndpoint
    When request { name: "#(name)", email: "#(name)@gmail.com", mobileNumber: "#(phone)", account: { accountType: "HomeLoan", accountNumber: "1234512309", branchAddress: "124 Bourke Street" } }
    And method put
    Then status 404
    And match response.errorMessage == "Account not found with the given input data AccountNumber:'1234512309'"
    And match response.errorCode == "NOT_FOUND"

