Feature: Account management

  Scenario: create a new account from customer
    * def uuid = function(){ return java.util.UUID.randomUUID() + '' }
    * def number = function(max){ return Math.floor(Math.random() * max) }
    * def phone = number(100)

    Given url 'http://localhost:8080/api/accounts/create'
    And request { name: "#(uuid())", email: "#(uuid())@gmail.com", mobileNumber: "#(phone)" }
    And method post
    Then status 201
    And match response == { "statusCode": "201", "statusMsg": "Account created successfully" }

    When url 'http://localhost:8080/api/accounts/fetch'
    And params { mobileNumber: "#(phone)" }
    And method get
    Then status 200