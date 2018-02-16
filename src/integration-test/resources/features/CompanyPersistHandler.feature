Feature: Company CRUD

  Scenario: Save a company request
    Given a request to save company with name Yaegar and principal principaluuid is received
    When the lambda is triggered
    Then the company with uuid and administratorAndName is saved in the database