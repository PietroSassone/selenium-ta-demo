@HomePage
Feature: DemoQa home page test scenarios
  As a test automation engineer
  I want to test the DemoQa home page
  To demonstrate selenium usage

  Scenario: Simple test to verify that all UI elements are visible and correct on the home page
    Given the demo QA homepage is opened
    Then the header image should be visible
      And the header link should be correct
      And the certification training image should be visible
      And the join now link should be correct
      And there should be 6 category widgets on the page
      And the category widgets should be the following in order:
        | Elements                |
        | Forms                   |
        | Alerts, Frame & Windows |
        | Widgets                 |
        | Interactions            |
        | Book Store Application  |
      And the footer should be visible and correct
