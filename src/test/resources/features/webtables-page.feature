@WebTablesPage
Feature: DemoQa Web Tables page test scenarios
  As a test automation engineer
  I want to test the DemoQa Web Tables page
  To demonstrate selenium usage on tables

  Background:
    Given the web tables page is opened
    Then there should be 3 rows in the table on the current page

  Scenario: Adding an item to the Web Table on the page
    When the add new record button is clicked
      And valid input is prepared for the add table row form
      And all input fields are filled with the prepared valid input
    When the submit button is clicked
    Then there should be 4 rows in the table on the current page
      And 1 new row should be present with the prepared values

  @OperaNotSupported
  Scenario: Adding the same item multiple times and checking pagination of the Web Table on the page
    Given the display results per page dropdown is set to 5 rows
      And the total number of pages in the table should be 1
      And the page index input field of the pagination should contain 1
      And the previous page pagination button should be disabled
      And the next page pagination button should be disabled
    When valid input is prepared for the add table row form
      And the prepared test data is added to the table 3 times
    Then there should be 5 rows in the table on the current page
      And 2 new rows should be present with the prepared values
      And the total number of pages in the table should be 2
      And the previous page pagination button should be disabled
      And the next page pagination button should be enabled
    When the next page pagination button is clicked
    Then there should be 1 row in the table on the current page
      And the page index input field of the pagination should contain 2
      And 1 new row should be present with the prepared values
      And the previous page pagination button should be enabled
      And the next page pagination button should be disabled

  Rule: The current page displayed can be changed by setting the page index input. But it cannot jump to a bigger page index than the total page number.
    @OperaNotSupported
    Scenario Outline: Modifying the page index of the pagination of the Web Table on the page
    Given the display results per page dropdown is set to <rowsPerPage> rows
      And the total number of pages in the table should be 1
      And the page index input field of the pagination should contain 1
    When valid input is prepared for the add table row form
      And the prepared test data is added to the table <numberOfItemsToAdd> times
    Then the total number of pages in the table should be <expectedTotalPageNumber>
    When the current page index is set to <pageIndexSet>
    Then the page index input field of the pagination should contain <expectedPageIndex>
      And there should be <expectedNumberOfRows> rows in the table on the current page

  Examples:
    | rowsPerPage | numberOfItemsToAdd | expectedTotalPageNumber | pageIndexSet | expectedPageIndex | expectedNumberOfRows |
    | 5           | 3                  | 2                       | 2            | 2                 | 1                    |
    | 5           | 8                  | 3                       | 0            | 3                 | 1                    |
    | 5           | 8                  | 3                       | -1           | 1                 | 5                    |
    | 10          | 1                  | 1                       | 2            | 1                 | 4                    |

  @platformChangeInScenario
  Scenario: Deleting an item from the Web Table on the page
    Given the platform is switched to nexus7
      And the web tables page is opened
    When the delete button for item number 1 is clicked
    Then there should be 2 rows in the table on the current page

  Scenario: Deleting all items from the Web Table on the page
    When the delete button for item number 1 is clicked
      And the delete button for item number 1 is clicked
      And the delete button for item number 1 is clicked
    Then there should be 0 rows in the table on the current page
      And the 'No rows found' text should be visible in the table
