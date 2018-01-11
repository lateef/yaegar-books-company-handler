Feature: Company CRUD

  Scenario: Add company
    Given i create a company name with name Yaegar
    When i save the company
    Then i should have a company with name Yaegar saved in the database