Feature: Company CRUD

  Scenario: Save a company request
    Given a request to save company with name Yaegar is received
    When the lambda is triggered
    Then the company with uuid and name Yaegar is saved in the database