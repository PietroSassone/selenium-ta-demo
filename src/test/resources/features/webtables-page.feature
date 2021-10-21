@WebTablesPage
Feature: DemoQa home page test scenarios
  As a test automation engineer
  I want to test the DemoQa Web Tables page
  To demonstrate selenium usage on tables

  Scenario: Adding an item to the Web Table on the page
    Given the web tables page is opened
    Then there should be 3 rows in the table
    When the add new record button is clicked
      And valid input is prepared for the add table row form
      And all input fields are filled with the prepared valid input
    When the submit button is clicked
    Then there should be 4 rows in the table
      And 1 new row should be present with the prepared values

  Scenario: Adding the same item twice to the Web Table on the page
    Given the web tables page is opened
    Then there should be 3 rows in the table
    When the add new record button is clicked
      And valid input is prepared for the add table row form
      And all input fields are filled with the prepared valid input
    When the submit button is clicked
      And the add new record button is clicked
      And all input fields are filled with the prepared valid input
    When the submit button is clicked
    Then there should be 5 rows in the table
      And 2 new row should be present with the prepared values


  Scenario: Deleting an item from the Web Table on the page
    Given the web tables page is opened
    Then there should be 3 rows in the table
